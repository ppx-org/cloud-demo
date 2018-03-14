package com.ppx.cloud.search.query;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ppx.cloud.common.controller.ControllerContext;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.demo.common.query.QueryCommonService;
import com.ppx.cloud.demo.common.query.QueryProduct;
import com.ppx.cloud.monitor.AccessLog;
import com.ppx.cloud.monitor.AccessUtils;
import com.ppx.cloud.search.query.bean.QueryBrand;
import com.ppx.cloud.search.query.bean.QueryCategory;
import com.ppx.cloud.search.query.bean.QueryPageList;
import com.ppx.cloud.search.query.bean.QueryPromo;
import com.ppx.cloud.search.query.bean.QueryTheme;
import com.ppx.cloud.search.util.BitSetUtils;
import com.ppx.cloud.search.util.WordUtils;


@Service
public class SQueryService extends MyDaoSupport {
	
	@Autowired
	private QueryCommonService commonServ;
	
	
	private List<Integer> changeProdId(String versionName, List<Integer> indexIdList, String orderType) {
		
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("indexIdList", indexIdList);	
		
		String sql = "select PROD_ID from " + orderType + "_" + versionName + " where INDEX_ID in (:indexIdList) order by INDEX_ID";
		
		List<Integer> prodIdList = jdbc.queryForList(sql, paramMap, Integer.class);
		return prodIdList;
		
	}
	
	@SuppressWarnings("unchecked")
	public QueryPageList query(Integer mId, Integer sId, String w, MPage p, String date, Integer cId, 
			Integer bId, Integer tId, Integer gId, Integer fast, String orderType) {
		
		Map<String, Object> findMap = findProdId(mId, sId, w, p, date, cId, bId, tId, gId, fast, orderType);
				
		if (p.getTotalRows() == 0) {
			return new QueryPageList(); 
		}
		else {
			// id转换 转成真实的prodId
			List<Integer> newProdIdList = changeProdId(BitSetUtils.getCurrentV(), (List<Integer>)findMap.get("prodIdList"), orderType);
			findMap.put("prodIdList", newProdIdList);
			
			List<QueryProduct> prodList = commonServ.listProduct(newProdIdList, sId);
			
			// cat
			List<QueryCategory> catInitList = (List<QueryCategory>)findMap.get("catList");
			List<QueryCategory> catList	= listCategory(catInitList);
			
			// brand
			List<QueryBrand> brandInitList = (List<QueryBrand>)findMap.get("brandList");
			List<QueryBrand> brandList= listBrand(brandInitList);
			
			// theme
			List<QueryTheme> themeInitList = (List<QueryTheme>)findMap.get("themeList");
			List<QueryTheme> themeList = listTheme(themeInitList);
			
			// promo
			List<QueryPromo> promoInitList = (List<QueryPromo>)findMap.get("progList");
			List<QueryPromo> promoList = listPromo(promoInitList);
			
			int fastN = (Integer)findMap.get("fastN");			
			return new QueryPageList(p, prodList, catList, brandList, themeList, promoList, fastN);
		}
	}
	
	private Map<String, Object> findProdId(Integer mId, Integer sId, String w, MPage p, String date, 
			Integer cId, Integer bId, Integer tId, Integer gId, Integer fast, String orderType) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		BitSet resultBs = null;
		
		if (StringUtils.isEmpty(w)) {
			resultBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_STORE, sId + "");
		}
		else {
			resultBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_TITLE, w);
			
			if (resultBs.cardinality() == 0 && w.length() > 1) {
				BitSet splitBs = new BitSet();
				String[] word = WordUtils.splitWord(w).split(",");
				for (String s : word) {
					BitSet bs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_TITLE, s);
					splitBs.or(bs);
				}
				resultBs = splitBs;
			}
			
			if (resultBs.cardinality() != 0) {
				BitSet storeBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_STORE, sId + "");
				resultBs.and(storeBs);
			}
			else {
				return returnMap;
			}
		}
		
		
		
		// 除了关键字的查询条件 >>>>>>>>>>>>>>>>>>>>>>>
		
		BitSet fastBs = null;
		// 查fast
		if (fast != null) {
			fastBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_STORE, "local" + sId);
			resultBs.and(fastBs);
		}
		
		// 查cId
		if (cId != null) {
			BitSet catBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_CAT, cId + "");
			resultBs.and(catBs);
		}
		
		// 查bId
		if (bId != null) {
			BitSet brandBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_BRAND, bId + "");
			resultBs.and(brandBs);
		}
		
		// 查tId
		if (tId != null) {
			BitSet themeBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_THEME, tId + "");
			resultBs.and(themeBs);
		}
		
		// 查gId
		if (gId != null) {
			BitSet promoBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_PROMO + "/" + date, gId + "");
			resultBs.and(promoBs);
		}
		
		
		p.setTotalRows(resultBs.cardinality());
		List<Integer> prodIdList = BitSetUtils.bsToPage(resultBs, (p.getPageNumber() - 1) * p.getPageSize(), p.getPageSize());
		returnMap.put("prodIdList", prodIdList);
		
		// fast statistic
		if (fastBs == null) {
			fastBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_STORE, "local" + sId);
		}
		fastBs.and(resultBs);
		int fastN = fastBs.cardinality();
		returnMap.put("fastN", fastN);
		
		
		// cat statistic 改成bs从上面读
		List<QueryCategory> catList = new ArrayList<QueryCategory>();
		List<Integer> catIdList = listCatId(orderType);		
		for (Integer catId : catIdList) {			
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_CAT, catId + "");
			bs.and(resultBs);
			int n = bs.cardinality();
			if (n != 0) catList.add(new QueryCategory(catId, n));
		}
		returnMap.put("catList", catList);
		
		// brand statistic
		List<QueryBrand> brandList = new ArrayList<QueryBrand>();
		List<Integer> brandIdList = listBrandId(orderType);		
		for (Integer brandId : brandIdList) {			
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_BRAND, brandId + "");
			bs.and(resultBs);
			int n = bs.cardinality();
			if (n != 0) brandList.add(new QueryBrand(brandId, n));
		}
		returnMap.put("brandList", brandList);
		
		
		// theme statistic
		List<QueryTheme> themeList = new ArrayList<QueryTheme>();
		List<Integer> themeIdList = listThemeId(orderType);		
		for (Integer themeId : themeIdList) {			
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_THEME, themeId + "");
			bs.and(resultBs);
			int n = bs.cardinality();
			if (n != 0) themeList.add(new QueryTheme(themeId, n));
		}
		returnMap.put("themeList", themeList);
		
		
		// promo statistic
		List<QueryPromo> progList = new ArrayList<QueryPromo>();
		List<Integer> progIdList = listProgId(orderType, date);		
		for (Integer progId : progIdList) {	
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_PROMO + "/" + date, progId + "");
			bs.and(resultBs);
			int n = bs.cardinality();
			if (n != 0) progList.add(new QueryPromo(progId, n));
		}
		returnMap.put("progList", progList);
		
		
		return returnMap;
	}
	
	private List<QueryCategory> listCategory(List<QueryCategory> catList) {
		if (catList.size() == 0) {
			return new ArrayList<QueryCategory>();
		}
		
		Map<Integer, Integer> catIdMapNum = new HashMap<Integer, Integer>();
		for (QueryCategory c : catList) {
			catIdMapNum.put(c.getCid(), c.getN());
		}
		
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("catIdList", catIdMapNum.keySet());	
		
		String catSql = "select CAT_ID CID, PARENT_ID PID, CAT_NAME CN from category where CAT_ID in (:catIdList) order by CAT_PRIO";
		
		List<QueryCategory> resultCatList = jdbc.query(catSql, paramMap, BeanPropertyRowMapper.newInstance(QueryCategory.class));
		for (QueryCategory c : resultCatList) {
			c.setN(catIdMapNum.get(c.getCid()));
		}
		
		return resultCatList;
	}
	
	
	private List<QueryPromo> listPromo(List<QueryPromo> promoList) {
		if (promoList.size() == 0) {
			return new ArrayList<QueryPromo>();
		}
		
		Map<Integer, Integer> progIdMapNum = new HashMap<Integer, Integer>();
		for (QueryPromo p : promoList) {
			progIdMapNum.put(p.getGid(), p.getN());
		}
		
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("progIdList", progIdMapNum.keySet());	
		
		String progSql = "select PROG_ID GID, PROG_NAME GN from program where PROG_ID in (:progIdList) order by PROG_PRIO";
		
		List<QueryPromo> resultPromoList = jdbc.query(progSql, paramMap, BeanPropertyRowMapper.newInstance(QueryPromo.class));
		
		for (QueryPromo p : resultPromoList) {
			p.setN(progIdMapNum.get(p.getGid()));
		}
		
		return resultPromoList;
	}
	
	private List<QueryBrand> listBrand(List<QueryBrand> brandList) {
		if (brandList.size() == 0) {
			return new ArrayList<QueryBrand>();
		}
		
		Map<Integer, Integer> brandIdMapNum = new HashMap<Integer, Integer>();
		for (QueryBrand p : brandList) {
			brandIdMapNum.put(p.getBid(), p.getN());
		}
		
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("brandIdList", brandIdMapNum.keySet());
		
		String progSql = "select BRAND_ID BID, BRAND_NAME BN from brand where BRAND_ID in (:brandIdList) order by BRAND_PRIO";
		
		List<QueryBrand> resultBrandList = jdbc.query(progSql, paramMap, BeanPropertyRowMapper.newInstance(QueryBrand.class));
		
		for (QueryBrand b : resultBrandList) {
			b.setN(brandIdMapNum.get(b.getBid()));
		}
		
		return resultBrandList;
	}	
	
	private List<QueryTheme> listTheme(List<QueryTheme> themeList) {
		if (themeList.size() == 0) {
			return new ArrayList<QueryTheme>();
		}
		
		Map<Integer, Integer> themeIdMapNum = new HashMap<Integer, Integer>();
		for (QueryTheme t : themeList) {
			themeIdMapNum.put(t.getTid(), t.getN());
		}
		
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("themeIdList", themeIdMapNum.keySet());	
		
		String progSql = "select THEME_ID TID, THEME_NAME TN from theme where THEME_ID in (:themeIdList) order by THEME_PRIO";
		
		List<QueryTheme> resultBrandList = jdbc.query(progSql, paramMap, BeanPropertyRowMapper.newInstance(QueryTheme.class));
		
		for (QueryTheme t : resultBrandList) {
			t.setN(themeIdMapNum.get(t.getTid()));
		}
		
		return resultBrandList;
	}
	
	
	private List<Integer> listCatId(String orderType) {
		List<Integer> returnList = new ArrayList<Integer>();
		String[] fileName = new File(BitSetUtils.getRealPath(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_CAT)).list();
		for (String catIdName : fileName) {		
			catIdName = catIdName.replace("_", "");
			returnList.add(Integer.parseInt(catIdName));
		}
		return returnList;
	}
	
	private List<Integer> listBrandId(String orderType) {
		List<Integer> returnList = new ArrayList<Integer>();
		String[] fileName = new File(BitSetUtils.getRealPath(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_BRAND)).list();
		for (String brandIdName : fileName) {		
			brandIdName = brandIdName.replace("_", "");
			returnList.add(Integer.parseInt(brandIdName));
		}
		return returnList;
	}
	
	private List<Integer> listThemeId(String orderType) {
		List<Integer> returnList = new ArrayList<Integer>();
		String[] fileName = new File(BitSetUtils.getRealPath(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_THEME)).list();
		for (String themeIdName : fileName) {		
			themeIdName = themeIdName.replace("_", "");
			returnList.add(Integer.parseInt(themeIdName));
		}
		return returnList;
	}
	
	private List<Integer> listProgId(String orderType, String date) {
		List<Integer> returnList = new ArrayList<Integer>();
		String[] fileName = new File(BitSetUtils.getRealPath(BitSetUtils.getCurrentV(), orderType + "/" + BitSetUtils.PATH_PROMO + "/" + date)).list();
		if (fileName == null) return new ArrayList<Integer>(); 
		for (String progIdName : fileName) {	
			progIdName = progIdName.replace("_", "");
			returnList.add(Integer.parseInt(progIdName));
		}
		return returnList;
	}
	
	// 异步插入
	public void asynInsertSearchHistory(Integer storeId, String openid, String w) {
		AccessLog log = new AccessLog();
		log.setUri("asynInsertSearchHistory");
		log.setBeginTime(new Date());
		log.setIp("127.0.0.0");
		ControllerContext.setAccessLog(log);
		try {
			String insertSql = "insert into search_history_word(OPENID, STORE_ID, HIS_WORD) values(?, ?, ?)"; 
			getJdbcTemplate().update(insertSql, openid, w);
			
			String selectSql = "select ifnull((select LAST_WORD from search_last_word where OPENID = ?"
					+ " and CREATED = (select min(CREATED) from search_last_word where OPENID = ?)"
					+ " and (select count(*) from search_last_word where OPENID = ?) > ?), '') LAST_WORD";
			String deleteLastWord = getJdbcTemplate().queryForObject(selectSql, String.class, openid, openid, openid, 3);
			
			if (!StringUtils.isEmpty(deleteLastWord)) {
				String deleteSql = "delete from search_last_word where OPENID = ? and LAST_WORD = ?";
				getJdbcTemplate().update(deleteSql, openid, deleteLastWord);
			}
			
			String insertLastSql = "insert into search_last_word(OPENID, LAST_WORD) values(?,?) on duplicate key update CREATED = now()";
			getJdbcTemplate().update(insertLastSql, openid, w);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.setSpendTime(System.currentTimeMillis() - log.getBeginTime().getTime());
		AccessUtils.writeQueue(log);
	}
	
}






