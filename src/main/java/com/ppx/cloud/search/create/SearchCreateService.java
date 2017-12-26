package com.ppx.cloud.search.create;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.util.DateUtils;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.search.util.BitSetUtils;
import com.ppx.cloud.search.util.WordUtils;

@Repository
public class SearchCreateService extends MyDaoSupport {
	// 2, "已生成"; 3, "使用中"
	@Transactional
	public int useIndex(String versionName) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String otherSql = "update search_version set UPDATED = now(), VERSION_STATUS = ? where MERCHANT_ID = ? and VERSION_STATUS = ?";
		getJdbcTemplate().update(otherSql, 2, merchantId, 3);
		String updateStatus = "update search_version set UPDATED = now(), VERSION_STATUS = ? where MERCHANT_ID = ? and VERSION_NAME = ?";
		getJdbcTemplate().update(updateStatus, 3, merchantId, versionName);
		return 1;
		
	}
	
	@Transactional
	public int createIndex(String versionName) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		//String 
		String beginUpdateSql = "update search_version set CREATE_BEGIN = now() where MERCHANT_ID = ? and VERSION_NAME = ?";
		getJdbcTemplate().update(beginUpdateSql, merchantId, versionName);
		
		
		// 1.初始化
		Map<String, Integer> map1 = initSearchWords();
		System.out.println("..............1:" + map1);
		
		// 2.删除index
		Map<String, Integer> map2 = BitSetUtils.removeVersionPath(versionName);
		System.out.println("..............2:" + map2);
		
		// 3.st index
		Map<String, Integer> map3 = createStoreIndex(versionName);
		System.out.println("..............3:" + map3);
		
		// 4.title index
		Map<String, Integer> map4 = createTitleIndex(versionName);
		System.out.println("..............4:" + map4);
		
		// 5.cat index
		Map<String, Integer> map5 = createCatIndex(versionName);
		System.out.println("..............5:" + map5);
		
		// 6.brand index
		Map<String, Integer> map6 = createBrandIndex(versionName);
		System.out.println("..............6:" + map6);
		
		// 7.theme index
		Map<String, Integer> map7 = createThemeIndex(versionName);
		System.out.println("..............7:" + map7);
		
		// 8.theme index
		Map<String, Integer> map8 = createPromoIndex(versionName);
		System.out.println("..............8:" + map8);
		
		
		
		
		
				
				
				
		
		
		
		
				
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// 2, "已生成"; 3, "使用中"
		String endUpdateSql = "update search_version join (select if (count(*) >= 1, 2, 3) STATUS from search_version " +
			"where MERCHANT_ID = ? and VERSION_STATUS = ?) t set CREATE_END = now(), UPDATED = now()," +
			"VERSION_STATUS = t.STATUS where MERCHANT_ID = ? and VERSION_NAME = ?";
		getJdbcTemplate().update(endUpdateSql, merchantId, 3, merchantId, versionName);
		
		return 1;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private Map<String, Integer> initSearchWords() {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		// ext
		String extSql = "select group_concat(EXT_WORD) from search_ext_word where MERCHANT_ID = ?";
		String extWords = getJdbcTemplate().queryForObject(extSql, String.class, merchantId);
		WordUtils.setExtWord(extWords);
		
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
		
		returnMap.put("prodSize", list.size());
		return returnMap;
	}

	
	
	public Map<String, Integer> createStoreIndex(String versionName) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		String path = "store";
		BitSetUtils.initPath(versionName, path);
		
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
			BitSetUtils.writeBitSet(versionName, path, storeId + "", storeBs);
			
			returnMap.put("store_" + storeId, storeBs.cardinality());
		}
		
		// 创建local store索引
		String fastSql = "select PROD_ID from product where REPO_ID = ?";
		
		for (Integer storeId : storeIdList) {
			BitSet fastBs = new BitSet();
			List<Integer> prodIdList = getJdbcTemplate().queryForList(fastSql, Integer.class, storeId);			
			for (Integer prodId : prodIdList) {
				fastBs.set(prodId);
			}
			BitSetUtils.writeBitSet(versionName, path, "local" + storeId, fastBs);
			returnMap.put("local_store_" + storeId, fastBs.cardinality());
		}
		
		return returnMap;
	}
	
	
	
	
	public Map<String, Integer> createTitleIndex(String versionName) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		String path = "title";
		BitSetUtils.initPath(versionName, path);
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		Map<String, BitSet> wordMap = new HashMap<String, BitSet>();		
		String sql = "select PROD_ID, WORDS from search_words where MERCHANT_ID = ?";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, merchantId);
		for (Map<String, Object> map : list) {
			int searchProdId = (Integer)map.get("PROD_ID");
			String searchWords = (String)map.get("WORDS");
			
			String word[] = searchWords.split(",");
			for (int i = 0; i < word.length; i++) {
				if ("".equals(word[i])) continue;
				BitSet bs = wordMap.get(word[i]);
				if (bs == null) bs = new BitSet();
				bs.set(searchProdId);
				wordMap.put(word[i], bs);
			}
		}
		
		Set<String> set =  wordMap.keySet();
		for (String key : set) {
			BitSet bs = wordMap.get(key);			
			BitSetUtils.writeBitSet(versionName, path, key, bs);
		}
		
		returnMap.put("prodNum", list.size());
		returnMap.put("wordNum", set.size());
		return returnMap;
	}
	
	
	public Map<String, Integer> createCatIndex(String versionName) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		String path = "cat";
		BitSetUtils.initPath(versionName, path);
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
			BitSetUtils.writeBitSet(versionName, path, catId + "", bs);
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
					BitSet bs = BitSetUtils.readBitSet(versionName, path, subId[i] + "");
					if (bs == null) continue; // 如果是删除的catId，则bs就是null
					if (mainBs == null) mainBs = bs;
					else mainBs.or(bs);
				}
				BitSetUtils.writeBitSet(versionName, path, mainCatId + "", mainBs);
			}
		}
		
		returnMap.put("subCatNum", catIdList.size());
		returnMap.put("mainCatNum", mainCatIdList.size());
		return returnMap;
	}
	
	
	public Map<String, Integer> createBrandIndex(String versionName) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		String path = "brand";
		BitSetUtils.initPath(versionName, path);
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
				BitSetUtils.writeBitSet(versionName, path, brandId + "", bs);
			}
		}
		
		returnMap.put("brandNum", brandList.size());
		return returnMap;
	}
	
	
	public Map<String, Integer> createThemeIndex(String versionName) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		String path = "theme";
		BitSetUtils.initPath(versionName, path);
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		String themeSql =  "select THEME_ID from theme where MERCHANT_ID = ? and RECORD_STATUS = ?";	
		List<Integer> themeList =  getJdbcTemplate().queryForList(themeSql, Integer.class, merchantId, 1);
		for (Integer themeId : themeList) {
			BitSet bs = new BitSet();
			String prodSql = "select PROD_ID from theme_map_prod where THEME_ID = ?";
			List<Integer> prodList =  getJdbcTemplate().queryForList(prodSql, Integer.class, themeId);
			for (Integer prodId : prodList) {
				bs.set(prodId);
			}
			if (bs.cardinality() != 0) {
				BitSetUtils.writeBitSet(versionName, path, themeId + "", bs);
			}
		}
		
		returnMap.put("themeNum", themeList.size());
		return returnMap;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/////////////////////////////////////////////promo生成今天和明天的
	
	public Map<String, Integer> createPromoIndex(String versionName) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		// 处理promo索引
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		
		
		
		// >>>>>>>>>>>>>>>>>>>>>>>>>today
		String todaySql = "select t.PROD_ID, t.PROG_ID from (select * from program_index where " + 
				"MERCHANT_ID = ? and curdate() between INDEX_BEGIN and INDEX_END order by INDEX_PRIO desc) t group by t.PROD_ID";
		List<Map<String, Object>> todayList = getJdbcTemplate().queryForList(todaySql, merchantId);
		createDateProme(DateUtils.today(), versionName, todayList);
		
		// >>>>>>>>>>>>>>>>>>>>>>>>>>tomorrow
		String tomorrowSql = "select t.PROD_ID, t.PROG_ID from (select * from program_index where " + 
				"MERCHANT_ID = ? and DATE_SUB(curdate(),INTERVAL -1 DAY) between INDEX_BEGIN and INDEX_END order by INDEX_PRIO desc) t group by t.PROD_ID";
		List<Map<String, Object>> tomorrowList = getJdbcTemplate().queryForList(tomorrowSql, merchantId);
		createDateProme(DateUtils.tomorrow(), versionName, tomorrowList);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		returnMap.put("todayListSize", todayList.size());
		returnMap.put("tomorrowListSize", tomorrowList.size());
		return returnMap;
	}
	
	
	
	private void createDateProme(String date, String versionName, List<Map<String, Object>> indexList) {
		String path = "promo/" + date;
		BitSetUtils.initPath(versionName, path);
		
		Map<Integer, BitSet> bitSetMap = new HashMap<Integer, BitSet>();
		
		for (Map<String, Object> map : indexList) {
			Integer prodId = (Integer)map.get("PROD_ID");
			Integer progId = (Integer)map.get("PROG_ID");
			
			BitSet bs = bitSetMap.get(progId);
			if (bs == null) {
				BitSet newBs = new BitSet();
				newBs.set(prodId);
				bitSetMap.put(progId, newBs);
			}
			else {
				bs.set(prodId);
			}
		}
		
		for (Integer progId : bitSetMap.keySet()) {
			BitSetUtils.writeBitSet(versionName, path, progId + "", bitSetMap.get(progId));
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
