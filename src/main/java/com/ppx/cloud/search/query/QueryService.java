package com.ppx.cloud.search.query;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.search.query.bean.QueryBrand;
import com.ppx.cloud.search.query.bean.QueryCategory;
import com.ppx.cloud.search.query.bean.QueryPage;
import com.ppx.cloud.search.query.bean.QueryPageList;
import com.ppx.cloud.search.query.bean.QueryProduct;
import com.ppx.cloud.search.query.bean.QueryPromo;
import com.ppx.cloud.search.query.bean.QueryTheme;
import com.ppx.cloud.search.util.BitSetUtils;
import com.ppx.cloud.search.util.WordUtils;


@Service
public class QueryService extends MyDaoSupport {
	
	
	public QueryPageList query(String w, QueryPage p, String date, Integer cId, Integer gId, Integer fast) {
		
		Map<String, Object> findMap = findProdId(w, p, date, cId, gId, fast);
		
		
		if (p.getTotalRows() == 0) {
			return new QueryPageList(); 
		}
		else {
			@SuppressWarnings("unchecked")
			List<Integer> prodIdList = (List<Integer>)findMap.get("prodIdList");
			List<QueryProduct> prodList = listProduct(prodIdList);
			
			// cat
			@SuppressWarnings("unchecked")
			List<QueryCategory> catInitList = (List<QueryCategory>)findMap.get("catList");
			List<QueryCategory> catList	= listCategory(catInitList);
			
			
			// promo
			@SuppressWarnings("unchecked")
			List<QueryPromo> promoInitList = (List<QueryPromo>)findMap.get("progList");
			List<QueryPromo> promoList = listPromo(promoInitList);
			
			int fastN = (Integer)findMap.get("fastN");			
			return new QueryPageList(p, prodList, catList, promoList, fastN);
		}
	}
	
	private Map<String, Object> findProdId(String w, QueryPage p, String date, Integer cId, Integer gId, Integer fast) {
		
		int storeId = 1;
		
		if (StringUtils.isEmpty(w)) {
			return null;
		}
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		BitSet resultBs = BitSetUtils.readBitSet(BitSetUtils.PATH_TITLE, w);
		
		if (resultBs.cardinality() == 0 && w.length() > 1) {
			BitSet splitBs = new BitSet();
			String[] word = WordUtils.splitWord(w).split(",");
			for (String s : word) {
				BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_TITLE, s);
				splitBs.or(bs);
			}
			resultBs = splitBs;
		}
		
		if (resultBs.cardinality() != 0) {
			BitSet storeBs = BitSetUtils.readBitSet(BitSetUtils.PATH_STORE, storeId + "");
			resultBs.and(storeBs);
		}
		else {
			return returnMap;
		}
		
		BitSet fastBs = null;
		// 查fast
		if (fast != null) {
			fastBs = BitSetUtils.readBitSet(BitSetUtils.PATH_STORE, "local" + storeId);
			resultBs.and(fastBs);
		}
		
		// 查catId
		if (cId != null) {
			BitSet catBs = BitSetUtils.readBitSet(BitSetUtils.PATH_CAT, cId + "");
			resultBs.and(catBs);
		}
		
		// 查gId
		if (gId != null) {
			BitSet promoBs = BitSetUtils.readBitSet(BitSetUtils.PATH_PROMO + "/" + date, gId + "");
			resultBs.and(promoBs);
		}
		
		
		
		p.setTotalRows(resultBs.cardinality());
		List<Integer> prodIdList = BitSetUtils.bsToPage(resultBs, (p.getPageNumber() - 1) * p.getPageSize(), p.getPageSize());
		returnMap.put("prodIdList", prodIdList);
		
		// fast statistic
		if (fastBs == null) {
			fastBs = BitSetUtils.readBitSet(BitSetUtils.PATH_STORE, "local" + storeId);
		}
		fastBs.and(resultBs);
		int fastN = fastBs.cardinality();
		returnMap.put("fastN", fastN);
		
		
		// cat statistic 改成bs从上面读
		List<QueryCategory> catList = new ArrayList<QueryCategory>();
		List<Integer> catIdList = listCatId();		
		for (Integer catId : catIdList) {			
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_CAT, catId + "");
			bs.and(resultBs);
			int n = bs.cardinality();
			if (n != 0) catList.add(new QueryCategory(catId, n));
		}
		returnMap.put("catList", catList);
		
		// promo statistic 改成bs从上面读
		List<QueryPromo> progList = new ArrayList<QueryPromo>();
		List<Integer> progIdList = listProgId(date);		
		for (Integer progId : progIdList) {	
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_PROMO + "/" + date, progId + "");
			bs.and(resultBs);
			int n = bs.cardinality();
			if (n != 0) progList.add(new QueryPromo(progId, n));
		}
		returnMap.put("progList", progList);
		
		
	
		
		
		
		return returnMap;
	}

	
	private List<QueryProduct> listProduct(List<Integer> prodIdList) {
		if (prodIdList.size() == 0) {
			return new ArrayList<QueryProduct>();
		}
		
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("prodIdList", prodIdList);	
		
		String prodSql = "select p.PROD_ID PID, p.PROD_TITLE T, s.PRICE P, img.SKU_IMG_SRC SRC, idx.PROG_ID GID, idx.POLICY ARG from product p join sku s on p.PROD_ID = s.PROD_ID left join " +
			"(select t.SKU_ID, t.SKU_IMG_SRC from (select * from sku_img order by SKU_IMG_PRIO desc) t group by t.SKU_ID) img on s.SKU_ID = img.SKU_ID left join " +
			"(select t.PROD_ID, t.PROG_ID, t.INDEX_POLICY POLICY from (select * from program_index i where curdate() between INDEX_BEGIN and INDEX_END order by INDEX_PRIO desc) t group by t.PROD_ID) idx on p.PROD_ID = idx.PROD_ID " + 
			"where p.PROD_ID in (:prodIdList)";
		
		List<QueryProduct> prodList = jdbc.query(prodSql, paramMap, BeanPropertyRowMapper.newInstance(QueryProduct.class));
		return prodList;
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
		
		String progSql = "select PROG_ID GID, POLICY_ARGS ARG from program where PROG_ID in (:progIdList) order by PROG_PRIO";
		
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
	
	
	
	
	
	private List<Integer> listCatId() {
		List<Integer> returnList = new ArrayList<Integer>();
		String[] fileName = new File(BitSetUtils.getRealPath(BitSetUtils.PATH_CAT)).list();
		for (String catIdName : fileName) {		
			catIdName = catIdName.replace("_", "");
			returnList.add(Integer.parseInt(catIdName));
		}
		return returnList;
	}
	
	private List<Integer> listBrandId() {
		List<Integer> returnList = new ArrayList<Integer>();
		String[] fileName = new File(BitSetUtils.getRealPath(BitSetUtils.PATH_BRAND)).list();
		for (String brandIdName : fileName) {		
			brandIdName = brandIdName.replace("_", "");
			returnList.add(Integer.parseInt(brandIdName));
		}
		return returnList;
	}
	
	private List<Integer> listThemeId() {
		List<Integer> returnList = new ArrayList<Integer>();
		String[] fileName = new File(BitSetUtils.getRealPath(BitSetUtils.PATH_THEME)).list();
		for (String themeIdName : fileName) {		
			themeIdName = themeIdName.replace("_", "");
			returnList.add(Integer.parseInt(themeIdName));
		}
		return returnList;
	}
	
	private List<Integer> listProgId(String date) {
		List<Integer> returnList = new ArrayList<Integer>();
		String[] fileName = new File(BitSetUtils.getRealPath(BitSetUtils.PATH_PROMO + "/" + date)).list();
		if (fileName == null) return new ArrayList<Integer>(); 
		for (String progIdName : fileName) {	
			progIdName = progIdName.replace("_", "");
			returnList.add(Integer.parseInt(progIdName));
		}
		return returnList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>cat
	public QueryPageList queryCat(Integer storeId, String date, QueryPage p, Integer cId) {
		BitSet resultBs = BitSetUtils.readBitSet(BitSetUtils.PATH_STORE, storeId + "");
		if (cId != null) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_CAT, cId + "");
			resultBs.and(bs);
		}
		
		
		// statistic
		List<QueryCategory> initCatList = new ArrayList<QueryCategory>();
		List<Integer> catIdList = listCatId();				
		for (Integer catId : catIdList) {	
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_CAT, catId + "");
			bs.and(resultBs);
			int n = bs.cardinality();
			if (n != 0) initCatList.add(new QueryCategory(catId, n));
		}
		
		p.setTotalRows(resultBs.cardinality());
		List<Integer> prodIdList = BitSetUtils.bsToPage(resultBs, (p.getPageNumber() - 1) * p.getPageSize(), p.getPageSize());
		List<QueryProduct> prodList = listProduct(prodIdList);
		
		List<QueryCategory> catList = listCategory(initCatList);
		QueryPageList queryPageList = new QueryPageList(p, prodList);
		queryPageList.setCatList(catList);
		
		return queryPageList;
	}
	
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>brand
	public QueryPageList queryBrand(Integer storeId, String date, QueryPage p, Integer bId) {
		
		BitSet resultBs = BitSetUtils.readBitSet(BitSetUtils.PATH_STORE, storeId + "");
		if (bId != null) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_BRAND, bId + "");
			resultBs.and(bs);
		}
		
		
		// statistic
		List<QueryBrand> initBrandList = new ArrayList<QueryBrand>();
		List<Integer> brandIdList = listBrandId();
		
		BitSet totalBrandBs = new BitSet();
		for (Integer brandId : brandIdList) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_BRAND, brandId + "");
			totalBrandBs.or(bs);
			bs.and(resultBs);
			int n = bs.cardinality();
			if (n != 0) initBrandList.add(new QueryBrand(brandId, n));
		}
		resultBs.and(totalBrandBs);
		
		
		p.setTotalRows(resultBs.cardinality());
		List<Integer> prodIdList = BitSetUtils.bsToPage(resultBs, (p.getPageNumber() - 1) * p.getPageSize(), p.getPageSize());
		List<QueryProduct> prodList = listProduct(prodIdList);
		
		List<QueryBrand> brandList = listBrand(initBrandList);
		QueryPageList queryPageList = new QueryPageList(p, prodList);
		queryPageList.setBrandList(brandList);
		
		return queryPageList;
	}

	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>theme
	public QueryPageList queryTheme(Integer storeId, String date, QueryPage p, Integer tId) {
		
		BitSet resultBs = BitSetUtils.readBitSet(BitSetUtils.PATH_STORE, storeId + "");
		if (tId != null) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_THEME, tId + "");
			resultBs.and(bs);
		}
		
		
		// statistic
		List<QueryTheme> themeList = new ArrayList<QueryTheme>();
		List<Integer> themeIdList = listThemeId();
		
		BitSet totalBrandBs = new BitSet();
		for (Integer themeId : themeIdList) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_THEME, themeId + "");
			totalBrandBs.or(bs);
			bs.and(resultBs);
			int n = bs.cardinality();
			if (n != 0) themeList.add(new QueryTheme(themeId, n));
		}
		resultBs.and(totalBrandBs);
		
		
		p.setTotalRows(resultBs.cardinality());
		List<Integer> prodIdList = BitSetUtils.bsToPage(resultBs, (p.getPageNumber() - 1) * p.getPageSize(), p.getPageSize());
		List<QueryProduct> prodList = listProduct(prodIdList);
		
		List<QueryTheme> promoList = listTheme(themeList);
		QueryPageList queryPageList = new QueryPageList(p, prodList);
		queryPageList.setThemeList(promoList);
		
		return queryPageList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>promo
	public QueryPageList queryPromo(Integer storeId, String date, QueryPage p, Integer gId) {
		
		BitSet resultBs = BitSetUtils.readBitSet(BitSetUtils.PATH_STORE, storeId + "");
		if (gId != null) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_PROMO + "/" + date, gId + "");
			resultBs.and(bs);
		}
		
		
		// statistic
		List<QueryPromo> progList = new ArrayList<QueryPromo>();
		List<Integer> progIdList = listProgId(date);
		
		BitSet totalPromoBs = new BitSet();
		for (Integer progId : progIdList) {	
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_PROMO + "/" + date, progId + "");
			totalPromoBs.or(bs);
			bs.and(resultBs);
			int n = bs.cardinality();
			if (n != 0) progList.add(new QueryPromo(progId, n));
		}
		resultBs.and(totalPromoBs);
		
		
		
		
		p.setTotalRows(resultBs.cardinality());
		List<Integer> prodIdList = BitSetUtils.bsToPage(resultBs, (p.getPageNumber() - 1) * p.getPageSize(), p.getPageSize());
		List<QueryProduct> prodList = listProduct(prodIdList);
		
		List<QueryPromo> promoList = listPromo(progList);
		QueryPageList queryPageList = new QueryPageList(p, prodList);
		queryPageList.setPromoList(promoList);
		
		return queryPageList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
