package com.ppx.cloud.search.create;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.util.DateUtils;
import com.ppx.cloud.search.common.SGrantContext;
import com.ppx.cloud.search.util.BitSetUtils;
import com.ppx.cloud.search.util.WordUtils;
import com.ppx.cloud.store.search.version.SearchVersion;

@Repository
public class SSearchCreateService extends MyDaoSupport {
	// 2, "已生成"; 3, "使用中"
	@Transactional
	public int useIndex(String versionName) {
		int updator = SGrantContext.getSession().getAccountId();
		
		int mId = SGrantContext.getSession().getmId();
		String otherSql = "update search_version set UPDATED = now(), UPDATOR = ?, VERSION_STATUS = ? where MERCHANT_ID = ? and VERSION_STATUS = ?";
		getJdbcTemplate().update(otherSql, updator, 2, mId, 3);
		String updateStatus = "update search_version set UPDATED = now(), UPDATOR = ?, VERSION_STATUS = ? where MERCHANT_ID = ? and VERSION_NAME = ?";
		getJdbcTemplate().update(updateStatus, updator, 3, mId, versionName);
		
		
		BitSetUtils.setVersionMap(mId, versionName);
		
		return 1;
		
	}
	
	
	
	@Transactional
	public int createIndex(String versionName) {
		
		int merchantId = SGrantContext.getSession().getmId();
		//String 
		String beginUpdateSql = "update search_version set CREATE_BEGIN = now() where MERCHANT_ID = ? and VERSION_NAME = ?";
		getJdbcTemplate().update(beginUpdateSql, merchantId, versionName);
		
		long t = System.currentTimeMillis();
		Map<String, Integer> spendMap = new LinkedHashMap<String, Integer>();
		
		
		Map<String, Integer> resultMap = new LinkedHashMap<String, Integer>();
		// 综合排序normal 新品优先new
		// BitSetUtils.ORDER_NORMAL, BitSetUtils.ORDER_NEW
		
		String[] orderType = {BitSetUtils.ORDER_NORMAL, BitSetUtils.ORDER_NEW};
		
		// 2.删除index
		Map<String, Integer> map2 = BitSetUtils.removeVersionPath(versionName);
		spendMap.put("spendTime2", (int)(System.currentTimeMillis() - t));
		t = System.currentTimeMillis();

		
		for (int i = 0; i < orderType.length; i++) {
			
			
			// 1.初始化word
			Map<String, Integer> map1 = initSearchWords(versionName, orderType[i]);
			spendMap.put("spendTime1", (int)(System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			
			// 3.store index
			Map<String, Integer> map3 = createStoreIndex(versionName, orderType[i]);
			spendMap.put("spendTime3", (int)(System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			
			// 4.title index
			Map<String, Integer> map4 = createTitleIndex(versionName, orderType[i]);
			spendMap.put("spendTime4", (int)(System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			
			// 5.cat index
			Map<String, Integer> map5 = createCatIndex(versionName, orderType[i]);
			spendMap.put("spendTime5", (int)(System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			
			// 6.brand index
			Map<String, Integer> map6 = createBrandIndex(versionName, orderType[i]);
			spendMap.put("spendTime6", (int)(System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			
			// 7.theme index
			Map<String, Integer> map7 = createThemeIndex(versionName, orderType[i]);
			spendMap.put("spendTime7", (int)(System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			
			// 8.promo index
			Map<String, Integer> map8 = createPromoIndex(versionName, orderType[i]);
			spendMap.put("spendTime8", (int)(System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			
			
			// 提示信息
			resultMap.putAll(map1);
			resultMap.putAll(map2);
			resultMap.putAll(map3);
			resultMap.putAll(map4);
			resultMap.putAll(map5);
			resultMap.putAll(map6);
			resultMap.putAll(map7);
			resultMap.putAll(map8);
		}
		
		resultMap.putAll(spendMap);
		
		
		String createInfo = "";
		try {
			createInfo = new ObjectMapper().writeValueAsString(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int updator = SGrantContext.getSession().getAccountId();
		// 2, "已生成"; 3, "使用中"
		String endUpdateSql = "update search_version join (select if (count(*) >= 1, 2, 3) STATUS from search_version " +
			"where MERCHANT_ID = ? and VERSION_STATUS = ?) t set CREATE_END = now(), UPDATED = now(), UPDATOR = ?," +
			"VERSION_STATUS = t.STATUS, CREATE_INFO = ? where MERCHANT_ID = ? and VERSION_NAME = ?";
		getJdbcTemplate().update(endUpdateSql, merchantId, 3, updator, createInfo, merchantId, versionName);
	
		return 1;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// orderType: normal new
	public Map<String, Integer> initSearchWords(String versionName, String orderType) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		int merchantId = SGrantContext.getSession().getmId();
	
		String extSql = "select group_concat(EXT_WORD) from search_ext_word where MERCHANT_ID = ?";
		String extWords = getJdbcTemplate().queryForObject(extSql, String.class, merchantId);
		WordUtils.setExtWord(extWords);
		
		String sql = "select PROD_ID, PROD_TITLE from product where MERCHANT_ID = ? order by PROD_PRIO, PROD_ID";
		if (orderType.startsWith(BitSetUtils.ORDER_NEW)) {
			sql = "select PROD_ID, PROD_TITLE from product where MERCHANT_ID = ? order by PROD_ID desc";
		}
		
		List<Map<String, Object>> prodList = getJdbcTemplate().queryForList(sql, merchantId);
		
		getJdbcTemplate().update("delete from " + orderType + "_" + versionName + " where MERCHANT_ID = ?", merchantId);
		
		String insertSql = "insert into " + orderType + "_" + versionName + "(PROD_ID, INDEX_ID, MERCHANT_ID, WORDS) values(?, ?, " + merchantId + ", ?)";
		
		List<Object[]> argsList = new ArrayList<Object[]>();
		
		for (int i = 0; i < prodList.size(); i++) {
			Map<String, Object> map = prodList.get(i);
			int prodId = (Integer)map.get("PROD_ID");
			String prodTitle = (String)map.get("PROD_TITLE");			
			String words = WordUtils.splitWord(prodTitle);
			Object[] arg = {prodId, i + 1, words};
			argsList.add(arg);
		}
		
		getJdbcTemplate().batchUpdate(insertSql, argsList);
		
		returnMap.put("productSize", prodList.size());
		return returnMap;
	}
	
	public Map<String, Integer> createStoreIndex(String versionName, String orderType) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		String path = orderType + "/" + BitSetUtils.PATH_STORE;
		BitSetUtils.initPath(versionName, path);
		
		int merchantId = SGrantContext.getSession().getmId();
		
		String sql = "select STORE_ID from store where MERCHANT_ID = ?";
		List<Integer> storeIdList = getJdbcTemplate().queryForList(sql, Integer.class, merchantId);
		
		// 加上条件是上架的产品
		String prodSql = "select (select INDEX_ID from " + orderType + "_" + versionName + " where PROD_ID = product.PROD_ID)"
				+ " from product where REPO_ID in (select REPO_ID from store_map_repo where STORE_ID = ?) and PROD_STATUS = ?";
		for (Integer storeId : storeIdList) {
			BitSet storeBs = new BitSet();
			// 2:上架
			List<Integer> prodIdList = getJdbcTemplate().queryForList(prodSql, Integer.class, storeId, 2);
			for (Integer prodId : prodIdList) {
				storeBs.set(prodId);
			}
			BitSetUtils.writeBitSet(versionName, path, storeId + "", storeBs);
			
			returnMap.put("store_" + storeId, storeBs.cardinality());
		}
		
		// 创建local store索引
		String fastSql = "select (select INDEX_ID from " + orderType + "_" + versionName + " where PROD_ID = product.PROD_ID) from product where REPO_ID = ?";
		
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
	
	
	
	
	public Map<String, Integer> createTitleIndex(String versionName, String orderType) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		String path = orderType + "/" + BitSetUtils.PATH_TITLE;
		BitSetUtils.initPath(versionName, path);
		int merchantId = SGrantContext.getSession().getmId();
		
		Map<String, BitSet> wordMap = new HashMap<String, BitSet>();		
		String sql = "select INDEX_ID, WORDS from " + orderType + "_" + versionName + " where MERCHANT_ID = ?";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, merchantId);
		for (Map<String, Object> map : list) {
			int searchProdId = (Integer)map.get("INDEX_ID");
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
		
		returnMap.put("titleProductSize", list.size());
		returnMap.put("wordSize", set.size());
		return returnMap;
	}
	
	
	public Map<String, Integer> createCatIndex(String versionName, String orderType) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		String path = orderType + "/" + BitSetUtils.PATH_CAT;
		BitSetUtils.initPath(versionName, path);
		int merchantId = SGrantContext.getSession().getmId();
		
		// 创建子类索引
		String subCatSql = "select CAT_ID from category where MERCHANT_ID = ? and PARENT_ID != ? and RECORD_STATUS = ?";
		List<Integer> catIdList =  getJdbcTemplate().queryForList(subCatSql, Integer.class, merchantId, -1, 1);
		String prodSql = "select (select INDEX_ID from " + orderType + "_" + versionName + " where PROD_ID = product.PROD_ID) INDEX_ID from product where CAT_ID = ?";
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
		
		returnMap.put("subCatSize", catIdList.size());
		returnMap.put("mainCatSize", mainCatIdList.size());
		return returnMap;
	}
	
	
	public Map<String, Integer> createBrandIndex(String versionName, String orderType) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		String path = orderType + "/" + BitSetUtils.PATH_BRAND;
		BitSetUtils.initPath(versionName, path);
		int merchantId = SGrantContext.getSession().getmId();
		
		String brandSql =  "select BRAND_ID from brand where MERCHANT_ID = ? and RECORD_STATUS = ?";	
		List<Integer> brandList =  getJdbcTemplate().queryForList(brandSql, Integer.class, merchantId, 1);
		for (Integer brandId : brandList) {
			BitSet bs = new BitSet();
			String prodSql = "select (select INDEX_ID from " + orderType + "_" + versionName + " where PROD_ID = product.PROD_ID) from product where BRAND_ID = ?";		
			List<Integer> prodList =  getJdbcTemplate().queryForList(prodSql, Integer.class, brandId);
			for (Integer prodId : prodList) {
				bs.set(prodId);
			}
			if (bs.cardinality() != 0) {
				BitSetUtils.writeBitSet(versionName, path, brandId + "", bs);
			}
		}
		
		returnMap.put("brandSize", brandList.size());
		return returnMap;
	}
	
	
	public Map<String, Integer> createThemeIndex(String versionName, String orderType) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		String path = orderType + "/" + BitSetUtils.PATH_THEME;
		BitSetUtils.initPath(versionName, path);
		int merchantId = SGrantContext.getSession().getmId();
		
		String themeSql =  "select THEME_ID from theme where MERCHANT_ID = ? and RECORD_STATUS = ?";	
		List<Integer> themeList =  getJdbcTemplate().queryForList(themeSql, Integer.class, merchantId, 1);
		for (Integer themeId : themeList) {
			BitSet bs = new BitSet();
			String prodSql = "select (select INDEX_ID from " + orderType + "_" + versionName + " where PROD_ID = theme_map_prod.PROD_ID) from theme_map_prod where THEME_ID = ?";
			List<Integer> prodList =  getJdbcTemplate().queryForList(prodSql, Integer.class, themeId);
			for (Integer prodId : prodList) {
				bs.set(prodId);
			}
			if (bs.cardinality() != 0) {
				BitSetUtils.writeBitSet(versionName, path, themeId + "", bs);
			}
		}
		
		returnMap.put("themeSize", themeList.size());
		return returnMap;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/////////////////////////////////////////////promo生成今天和明天的
	
	public Map<String, Integer> createPromoIndex(String versionName, String orderType) {
		Map<String, Integer> returnMap = new HashMap<String, Integer>();
		
		// 处理promo索引
		int merchantId = SGrantContext.getSession().getmId();
		
		
		// >>>>>>>>>>>>>>>>>>>>>>>>>today
		String todaySql = "select (select INDEX_ID from " + orderType + "_" + versionName + " where PROD_ID = t.PROD_ID) INDEX_ID, t.PROG_ID from (select * from program_index where " + 
				"MERCHANT_ID = ? and curdate() between INDEX_BEGIN and INDEX_END order by INDEX_PRIO desc) t group by t.PROD_ID";
		List<Map<String, Object>> todayList = getJdbcTemplate().queryForList(todaySql, merchantId);
		createDateProme(versionName, orderType, DateUtils.today(), todayList);
		
		// >>>>>>>>>>>>>>>>>>>>>>>>>>tomorrow
		String tomorrowSql = "select (select INDEX_ID from " + orderType + "_" + versionName + " where PROD_ID = t.PROD_ID) INDEX_ID, t.PROG_ID from (select * from program_index where " + 
				"MERCHANT_ID = ? and DATE_SUB(curdate(),INTERVAL -1 DAY) between INDEX_BEGIN and INDEX_END order by INDEX_PRIO desc) t group by t.PROD_ID";
		List<Map<String, Object>> tomorrowList = getJdbcTemplate().queryForList(tomorrowSql, merchantId);
		createDateProme(versionName, orderType, DateUtils.tomorrow(), tomorrowList);
		
		
		
		
		
		returnMap.put("todayProductSize", todayList.size());
		returnMap.put("tomorrowProductSize", tomorrowList.size());
		return returnMap;
	}
	
	
	
	private void createDateProme(String versionName, String orderType, String date, List<Map<String, Object>> indexList) {
		String path = orderType + "/" + BitSetUtils.PATH_PROMO + "/" + date;
		BitSetUtils.initPath(versionName, path);
		
		Map<Integer, BitSet> bitSetMap = new HashMap<Integer, BitSet>();
		
		for (Map<String, Object> map : indexList) {
			Integer indexId = ((Long)map.get("INDEX_ID")).intValue();
			Integer progId = (Integer)map.get("PROG_ID");
			
			BitSet bs = bitSetMap.get(progId);
			if (bs == null) {
				BitSet newBs = new BitSet();
				newBs.set(indexId);
				bitSetMap.put(progId, newBs);
			}
			else {
				bs.set(indexId);
			}
		}
		
		for (Integer progId : bitSetMap.keySet()) {
			BitSetUtils.writeBitSet(versionName, path, progId + "", bitSetMap.get(progId));
		}
	}
	
	
	
	

	// 初始化版本
	public void initVersion() {
		// 3:使用中
		String sql = "select MERCHANT_ID, VERSION_NAME from search_version where VERSION_STATUS = ?";
		List<SearchVersion> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(SearchVersion.class), 3);
		for (SearchVersion v : list) {
			BitSetUtils.setVersionMap(v.getMerchantId(), v.getVersionName());
		}
	}

	
}



