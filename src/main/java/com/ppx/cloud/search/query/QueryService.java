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
import com.ppx.cloud.search.query.bean.QueryCategory;
import com.ppx.cloud.search.query.bean.QueryPage;
import com.ppx.cloud.search.query.bean.QueryPageList;
import com.ppx.cloud.search.query.bean.QueryProduct;
import com.ppx.cloud.search.util.BitSetUtils;
import com.ppx.cloud.search.util.WordUtils;


@Service
public class QueryService extends MyDaoSupport {
	
	
	public QueryPageList query(String w, QueryPage p, Integer cId) {
		
		Map<String, Object> findMap = findProdId(w, p, cId);
		
		
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
			
			return new QueryPageList(prodList, catList, p);
		}
	}
	
	private Map<String, Object> findProdId(String w, QueryPage p, Integer cId) {
		
		int storeId = 1;
		String versionName = "V1";
		
		if (StringUtils.isEmpty(w)) {
			return null;
		}
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		BitSet resultBs = BitSetUtils.readBitSet(versionName, BitSetUtils.PATH_TITLE, w);
		
		if (resultBs.cardinality() == 0 && w.length() > 1) {
			BitSet splitBs = new BitSet();
			String[] word = WordUtils.splitWord(w).split(",");
			for (String s : word) {
				BitSet bs = BitSetUtils.readBitSet(versionName, BitSetUtils.PATH_TITLE, s);
				splitBs.or(bs);
			}
			resultBs = splitBs;
		}
		
		if (resultBs.cardinality() != 0) {
			BitSet storeBs = BitSetUtils.readBitSet(versionName, BitSetUtils.PATH_STORE, storeId + "");
			resultBs.and(storeBs);
		}
		else {
			return returnMap;
		}
		
		// 查catId
		if (cId != null) {
			BitSet catBs = BitSetUtils.readBitSet(versionName, BitSetUtils.PATH_CAT, cId + "");
			resultBs.and(catBs);
		}
		
		
		
		p.setTotalRows(resultBs.cardinality());
		List<Integer> prodIdList = BitSetUtils.bsToPage(resultBs, (p.getPageNumber() - 1) * p.getPageSize(), p.getPageSize());
		returnMap.put("prodIdList", prodIdList);
		
		
		// cat statistic
		List<QueryCategory> catList = new ArrayList<QueryCategory>();
		List<Integer> catIdList = listCatId(versionName);		
		for (Integer catId : catIdList) {			
			BitSet bs = BitSetUtils.readBitSet(versionName, BitSetUtils.PATH_CAT, catId + "");
			bs.and(resultBs);
			int n = bs.cardinality();
			if (n != 0) catList.add(new QueryCategory(catId, n));
		}
		returnMap.put("catList", catList);
		
		// promo statistic
		List<QueryCategory> progList = new ArrayList<QueryCategory>();
		List<Integer> progIdList = listCatId(versionName);		
		for (Integer progId : progIdList) {			
			BitSet bs = BitSetUtils.readBitSet(versionName, BitSetUtils.PATH_PROMO, progId + "");
			bs.and(resultBs);
			int n = bs.cardinality();
			if (n != 0) progList.add(new QueryCategory(progId, n));
		}
		System.out.println("xxxxxxxxxout:" + progList);
		returnMap.put("progList", progList);
		
		
		
		
		
		
		
		
		
		
		
		
		
		return returnMap;
	}
	
	
	
	
	
	
	
	
	
	
	
	private List<QueryProduct> listProduct(List<Integer> prodIdList) {
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> prodParamMap = new HashMap<String, Object>();
		prodParamMap.put("prodIdList", prodIdList);	
		
		String prodSql = "select p.PROD_ID PID, p.PROD_TITLE T, s.PRICE P, img.SKU_IMG_SRC SRC, idx.PROG_ID GID, idx.POLICY ARG from product p join sku s on p.PROD_ID = s.PROD_ID left join " +
			"(select t.SKU_ID, t.SKU_IMG_SRC from (select * from sku_img order by SKU_IMG_PRIO desc) t group by t.SKU_ID) img on s.SKU_ID = img.SKU_ID left join " +
			"(select t.PROD_ID, t.PROG_ID, t.INDEX_POLICY POLICY from (select * from program_index i where curdate() between INDEX_BEGIN and INDEX_END order by INDEX_PRIO desc) t group by t.PROD_ID) idx on p.PROD_ID = idx.PROD_ID " + 
			"where p.PROD_ID in (:prodIdList)";
		
		List<QueryProduct> prodList = jdbc.query(prodSql, prodParamMap, BeanPropertyRowMapper.newInstance(QueryProduct.class));
		return prodList;
	}

	
	
	private List<QueryCategory> listCategory(List<QueryCategory> catList) {
		//List<Integer> catIdList = new ArrayList<Integer>();
		Map<Integer, Integer> catIdMapNum = new HashMap<Integer, Integer>();
		for (QueryCategory c : catList) {
			//catIdList.add(c.getCatId());
			catIdMapNum.put(c.getCid(), c.getN());
		}
		
		
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> catParamMap = new HashMap<String, Object>();
		catParamMap.put("catIdList", catIdMapNum.keySet());	
		
		String prodSql = "select CAT_ID CID, PARENT_ID PID, CAT_NAME CN from category where CAT_ID in (:catIdList) order by CAT_PRIO";
		
		List<QueryCategory> resultCatList = jdbc.query(prodSql, catParamMap, BeanPropertyRowMapper.newInstance(QueryCategory.class));
		for (QueryCategory c : resultCatList) {
			c.setN(catIdMapNum.get(c.getCid()));
		}
		
		return resultCatList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private List<Integer> listCatId(String versionName) {
		List<Integer> returnList = new ArrayList<Integer>();
		String[] fileName = new File(BitSetUtils.getRealPath(versionName, BitSetUtils.PATH_CAT)).list();
		for (String catIdName : fileName) {		
			catIdName = catIdName.replace("_", "");
			returnList.add(Integer.parseInt(catIdName));
		}
		return returnList;
	}
	
	private List<Integer> listProgId(String versionName) {
		List<Integer> returnList = new ArrayList<Integer>();
		String[] fileName = new File(BitSetUtils.getRealPath(versionName, BitSetUtils.PATH_PROMO)).list();
		for (String catIdName : fileName) {	
			catIdName = catIdName.replace("_", "");
			returnList.add(Integer.parseInt(catIdName));
		}
		return returnList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
