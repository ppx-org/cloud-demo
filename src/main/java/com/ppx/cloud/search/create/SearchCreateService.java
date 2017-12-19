package com.ppx.cloud.search.create;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.util.DateUtils;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.search.util.BitSetUtils;
import com.ppx.cloud.search.util.WordUtils;

@Repository
public class SearchCreateService extends MyDaoSupport {
	
	@Transactional
	public int init() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		String sql = "select PROD_ID, PROD_TITLE from product where MERCHANT_ID = ?";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, merchantId);
		
		String delSql = "delete from search_words where MERCHANT_ID = ?";
		getJdbcTemplate().update(delSql, merchantId);
		
		String insertSql = "insert into search_words(PROD_ID, WORDS, MERCHANT_ID) values(?, ?, " + merchantId + ")";
		
		List<Object[]> argsList = new ArrayList<Object[]>();
		
		for (Map<String, Object> map : list) {
			int prodId = (Integer)map.get("PROD_ID");
			String prodTitle = (String)map.get("PROD_TITLE");			
			String words = WordUtils.splitWord(prodTitle);
			Object[] arg = {prodId, words};
			argsList.add(arg);
		}
		
		int r[] = getJdbcTemplate().batchUpdate(insertSql, argsList);
		
		return 1;
	}
	
	
	
	
	
	
	public int createStoreIndex() {
		String path = "store";
		BitSetUtils.initPath(path);
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		String sql = "select STORE_ID from store where MERCHANT_ID = ?";
		List<Integer> storeIdList = getJdbcTemplate().queryForList(sql, Integer.class, merchantId);
		
		String prodSql = "select PROD_ID from product where REPO_ID in (select REPO_ID from store_map_repo where STORE_ID = ?)";
		for (Integer storeId : storeIdList) {
			BitSet storeBs = new BitSet();
			List<Integer> prodIdList = getJdbcTemplate().queryForList(prodSql, Integer.class, storeId);
			for (Integer prodId : prodIdList) {
				storeBs.set(prodId);
			}
			BitSetUtils.writeBitSet(path, storeId + "", storeBs);
		}
		
		// 创建local store索引
		String fastSql = "select PROD_ID from product where REPO_ID = ?";
		
		for (Integer storeId : storeIdList) {
			BitSet fastBs = new BitSet();
			List<Integer> prodIdList = getJdbcTemplate().queryForList(fastSql, Integer.class, storeId);			
			for (Integer prodId : prodIdList) {
				fastBs.set(prodId);
			}
			BitSetUtils.writeBitSet(path, "local" + storeId, fastBs);
		}
		
		return 1;
	}
	
	
	
	
	public int createTitleIndex() {
		String path = "title";
		BitSetUtils.initPath(path);
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		Map<String, BitSet> workMap = new HashMap<String, BitSet>();		
		String sql = "select PROD_ID, WORDS from search_words where MERCHANT_ID = ?";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, merchantId);
		for (Map<String, Object> map : list) {
			int searchProdId = (Integer)map.get("PROD_ID");
			String searchWords = (String)map.get("WORDS");
			
			String word[] = searchWords.split(",");
			for (int i = 0; i < word.length; i++) {
				if ("".equals(word[i])) continue;
				BitSet bs = workMap.get(word[i]);
				if (bs == null) bs = new BitSet();
				bs.set(searchProdId);
				workMap.put(word[i], bs);
			}
		}
		
		Set<String> set =  workMap.keySet();	
		for (String key : set) {
			BitSet bs = workMap.get(key);			
			BitSetUtils.writeBitSet(path, key, bs);
		}
		return 1;
	}
	
	
	public int createCatIndex() {
		String path = "cat";
		BitSetUtils.initPath(path);
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		// 创建子类索引
		String subCatSql = "select CAT_ID from category where MERCHANT_ID = ? and PARENT_ID != ? and RECORD_STATUS = ?";
		List<Integer> catIdList =  getJdbcTemplate().queryForList(subCatSql, Integer.class, merchantId, -1, 1);
		String prodSql = "select PROD_ID from product where CAT_ID = ?"; 
		for (Integer catId : catIdList) {
			List<Integer> prodIdList =  getJdbcTemplate().queryForList(prodSql, Integer.class, catId);
			BitSet bs = new BitSet();
			for (Integer prodId : prodIdList) {
				bs.set(prodId);
			}
			BitSetUtils.writeBitSet(path, catId + "", bs);
		}
		
		// 创建父类索引
		String mainCatSql = "select CAT_ID, (select group_concat(CAT_ID) from category where PARENT_ID = c.CAT_ID) SUB_CAT_IDS " +
			"from category c where MERCHANT_ID = ? and PARENT_ID = ? and RECORD_STATUS = ?";
		List<Map<String, Object>> mainCatIdList =  getJdbcTemplate().queryForList(mainCatSql, merchantId, -1, 1);
		
		for (Map<String, Object> map : mainCatIdList) {
			int mainCatId = (Integer)map.get("CAT_ID");
			String subIds = (String)map.get("SUB_CAT_IDS");
			if (subIds != null) {
				String[] subId = subIds.split(",");
				BitSet mainBs = null;
				for (int i = 0; i < subId.length; i++) {
					BitSet bs = BitSetUtils.readBitSet(path, subId[i] + "");
					if (bs == null) continue; // 如果是删除的catId，则bs就是null
					if (mainBs == null) mainBs = bs;
					else mainBs.or(bs);
				}
				BitSetUtils.writeBitSet(path, mainCatId + "", mainBs);
			}
		}
		
		return 1;
	}
	
	
	public int createBrandIndex() {		
		String path = "brand";
		BitSetUtils.initPath(path);
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		String brandSql =  "select BRAND_ID from brand where MERCHANT_ID = ? and RECORD_STATUS = ?";	
		List<Integer> brandList =  getJdbcTemplate().queryForList(brandSql, Integer.class, merchantId, 1);
		for (Integer brandId : brandList) {
			BitSet bs = new BitSet();
			String prodSql = "select PROD_ID from product where BRAND_ID = ?";		
			List<Integer> prodList =  getJdbcTemplate().queryForList(prodSql, Integer.class, brandId);
			for (Integer prodId : prodList) {
				bs.set(prodId);
			}
			if (bs.cardinality() != 0) {
				BitSetUtils.writeBitSet(path, brandId + "", bs);
			}
		}
		return 1;
	}
	
	
	public int createTopicIndex() {		
		String path = "topic";
		BitSetUtils.initPath(path);
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		String topicSql =  "select TOPIC_ID from topic where MERCHANT_ID = ? and RECORD_STATUS = ?";	
		List<Integer> topicList =  getJdbcTemplate().queryForList(topicSql, Integer.class, merchantId, 1);
		for (Integer topicId : topicList) {
			BitSet bs = new BitSet();
			String prodSql = "select PROD_ID from topic_map_prod where TOPIC_ID = ?";
			List<Integer> prodList =  getJdbcTemplate().queryForList(prodSql, Integer.class, topicId);
			for (Integer prodId : prodList) {
				bs.set(prodId);
			}
			if (bs.cardinality() != 0) {
				BitSetUtils.writeBitSet(path, topicId + "", bs);
			}
		}
		return 1;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/////////////////////////////////////////////promo生成今天和明天的
	
	public int createPromoIndex() {
		
		// 处理promo索引
		
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		
		// theme
		// TODO 加上日期
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// PROD_ID唯一
		String todaySql = "select t.PROG_ID, t.PROD_ID from (select * from program_index where MERCHANT_ID = -1 order by INDEX_PRIO desc) t group by t.PROD_ID";
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// 找PROG_ID
		String progIdSql = "select PROG_ID from program_index where MERCHANT_ID = -1 group by PROG_ID";
		List<Integer> progIdList =  getJdbcTemplate().queryForList(progIdSql, Integer.class, merchantId);
		
		
		
		// ----------今天----------
		String path = "promo/" + DateUtils.today();
		BitSetUtils.initPath(path);
		
		List<Map> todayMap =  getJdbcTemplate().queryForList(todaySql, Map.class, merchantId, 1);
		for (Integer progId : progIdList) {
			BitSet bs = new BitSet();
			for (Map today : todayMap) {
				Integer id = (Integer)today.get("PROG_ID");
				if (progId.intValue() == id.intValue()) {
					bs.set(id);
				}
			}
			BitSetUtils.writeBitSet(path, progId + "", bs);
		}
		
		return 1;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
