package com.ppx.cloud.search.create;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.search.util.BitSetUtils;
import com.ppx.cloud.search.util.WordUtils;

@Repository
public class SearchCreateService extends MyDaoSupport {
	
	
	@Transactional
	public int init() {		
		String sql = "select prodId, prodTitle from product";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql);
		
		String delSql = "delete from search_prop_word";
		getJdbcTemplate().update(delSql);
		
		String insertSql = "insert into search_prop_word(prodId, searchWords, merchantId) values(?, ?, ?)";		
		for (Map<String, Object> map : list) {
			int prodId = (Integer)map.get("prodId");
			String prodTitle = (String)map.get("prodTitle");			
			String r = WordUtils.splitWord(prodTitle);
			getJdbcTemplate().update(insertSql, prodId, r, 1);
		}
		return 1;
	}
	
	/**
	 * 商户对应每个store的索引
	 * @param merchantId
	 * @return
	 */
	public int createStore(int merchantId) {
		String path = "F:/store/search/" + merchantId + "/store/";
		
		File pathFile = new File(path);
		if (!pathFile.exists()) {
			if (!pathFile.mkdirs()) return -1;
		}
		
		// 删除原来的索引
		File[] f = pathFile.listFiles();
		for (File file : f) {
			file.delete();
		}
		
		String sql = "select storeId from store where merchantId = ?";
		List<Integer> storeIdList = getJdbcTemplate().queryForList(sql, Integer.class, merchantId);
		
		String prodSql = "select prodId from product where repoId in (select repoId from repo_map_store where storeId = ?)";
		for (Integer storeId : storeIdList) {
			BitSet storeBs = new BitSet();
			List<Integer> prodIdList = getJdbcTemplate().queryForList(prodSql, Integer.class, storeId);
			for (Integer prodId : prodIdList) {
				storeBs.set(prodId);
			}
			BitSetUtils.writeBitSet(path, storeId + "", storeBs);
		}
		
		// 创建fast和normal索引
		String fastSql = "select prodId from product where repoId in (select repoId from repo where storeId = ?)";
		
		for (Integer storeId : storeIdList) {
			BitSet fastBs = new BitSet();
			List<Integer> prodIdList = getJdbcTemplate().queryForList(fastSql, Integer.class, storeId);			
			for (Integer prodId : prodIdList) {
				fastBs.set(prodId);
			}
			BitSetUtils.writeBitSet(path, "fast" + storeId, fastBs);
		}
		
		
		return 1;
	}
	
	/**
	 * 创建标题的索引
	 * @param merchantId
	 * @return
	 */
	public int createTitle(int merchantId) {
		String path = "F:/store/search/" + merchantId + "/title/";
		
		File pathFile = new File(path);
		if (!pathFile.exists()) {
			if (!pathFile.mkdirs()) return -1;
		}
		
		// 删除原来的索引
		File[] f = pathFile.listFiles();
		for (File file : f) {
			file.delete();
		}
		
		Map<String, BitSet> workMap = new HashMap<String, BitSet>();		
		String sql = "select * from search_prop_word where merchantId = ?";
		List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, merchantId);
		for (Map<String, Object> map : list) {
			int searchProdId = (Integer)map.get("prodId");
			String searchWords = (String)map.get("searchWords");
			
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
	
	public int createCat(int merchantId) {
		String path = "F:/store/search/" + merchantId + "/cat/";
		
		// 创建目录
		File pathFile = new File(path);
		if (!pathFile.exists()) {
			if (!pathFile.mkdirs()) return -1;
		}
		
		// 删除原来的索引
		File[] f = pathFile.listFiles();
		for (File file : f) {
			file.delete();
		}
		
		// 创建子类索引
		String subCatSql = "select s.catId from category_sub s, category c where s.catId = c.catId and c.catStatus = 0 " +
				"and s.mainCatId in(select m.catId from category_main m, category c where m.catId = c.catId and c.catStatus = 0 and m.merchantId = ?)";
		List<Integer> catIdList =  getJdbcTemplate().queryForList(subCatSql, Integer.class, merchantId);
		String prodSql = "select prodId from product where catId = ?"; 
		for (Integer catId : catIdList) {
			List<Integer> prodIdList =  getJdbcTemplate().queryForList(prodSql, Integer.class, catId);
			BitSet bs = new BitSet();
			for (Integer prodId : prodIdList) {
				bs.set(prodId);
			}
			BitSetUtils.writeBitSet(path, catId + "", bs);
		}
		
		// 创建父类索引
		String mainCatSql = "select m.catId, (select group_concat(catId) from category_sub where mainCatId = m.catId) subCatIds from category_main m, " +
				" category c where m.catId = c.catId and c.catStatus = 0 and m.merchantId = ?";
		List<Map<String, Object>> mainCatIdList =  getJdbcTemplate().queryForList(mainCatSql, merchantId);
		
		BitSet allBs = null; // 全类目索引 文件名称为"0"
		for (Map<String, Object> map : mainCatIdList) {
			int mainCatId = (Integer)map.get("catId");
			String subIds = (String)map.get("subCatIds");
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
				if (allBs == null) allBs = mainBs;
				else allBs.or(mainBs);
			}
		}
		BitSetUtils.writeBitSet(path, "0", allBs);
		
		return 1;
	}
	
	public int createPromo(int merchantId) {
		
		String path = "F:/store/search/" + merchantId + "/promo/";
		
		// 创建目录
		File pathFile = new File(path);
		if (!pathFile.exists()) {
			if (!pathFile.mkdirs()) return -1;
		}
		
		// 删除原来的索引(两层)
		File[] f = pathFile.listFiles();
		for (File file : f) {
			if (file.isFile()) file.delete();
			if (file.isDirectory()) {
				File[] subFileArray = file.listFiles();
				for (File subF : subFileArray) {subF.delete();}
			}
			file.delete();
		}
		
		// 以promoId来建立目录，当前时间点只有一个promoId
		String promoIdSql = "select promoId from promo where merchantId = ? and promoType = 0 and promoTo >= curdate()";
		List<Integer> promoIdList = getJdbcTemplate().queryForList(promoIdSql, Integer.class, merchantId);
		for (Integer promoId : promoIdList) {
			// 创建文件夹
			String promoIdPath = path + promoId + "/";
			File pathFileFile = new File(promoIdPath);
			if (!pathFileFile.mkdir()) return -1;
			
			
			
			// "m元系列", "t:13,m:?" // 1,2,3,5,10
			String promoSql = "select prodId from product p where exists(select 1 from promo_sku p, sku s where p.skuId = s.skuId"
					+ " and p.skuPriceRule like ? and p.promoId = ? and s.prodId = p.prodId)";
			List<String> ruleList =  new ArrayList<String>();//PriceDef.listSearchItem();
			for (String r : ruleList) {
				List<Integer> prodIdList = getJdbcTemplate().queryForList(promoSql, Integer.class, r + "%", promoId);
				BitSet bs = new BitSet();
				for (Integer prodId : prodIdList) {
					bs.set(prodId);
				}
				String fileName = r.replaceAll(":", "").replaceAll(",", "");
				if (bs.cardinality() != 0) 	BitSetUtils.writeBitSet(promoIdPath, fileName, bs);
			}
			
			// 全部促销商品，文件名为0
			String allPromoSql = "select prodId from product p where exists(select 1 from promo_sku p, sku s where p.skuId = s.skuId"
					+ " and p.promoId = ? and s.prodId = p.prodId)";
			List<Integer> prodIdList = getJdbcTemplate().queryForList(allPromoSql, Integer.class, promoId);
			BitSet bs = new BitSet();
			for (Integer prodId : prodIdList) {
				bs.set(prodId);
			}
			if (bs.cardinality() != 0) 	BitSetUtils.writeBitSet(promoIdPath, "0", bs);
		}
		
		return 1;
	}
	
	
	
	public int createSubject(int merchantId) {		
		String path = "F:/store/search/" + merchantId + "/subject/";
		
		// 创建目录
		File pathFile = new File(path);
		if (!pathFile.exists()) {
			if (!pathFile.mkdirs()) return -1;
		}
		
		// 删除原来的索引
		File[] f = pathFile.listFiles();
		for (File file : f) {
			file.delete();
		}
		
		BitSet allBs = null; // 全主题索引 文件名称为"0"
		String subjectSql =  "select subjectId from prod_subject where merchantId = ?";	
		List<Integer> subjectList =  getJdbcTemplate().queryForList(subjectSql, Integer.class, merchantId);
		
		for (Integer subjectId : subjectList) {
			BitSet bs = new BitSet();
			String prodSql = "select prodId from prod_map_subject where subjectId = ?";		
			List<Integer> prodList =  getJdbcTemplate().queryForList(prodSql, Integer.class, subjectId);
			for (Integer prodId : prodList) {
				bs.set(prodId);
			}
			if (bs.cardinality() != 0) 	{
				BitSetUtils.writeBitSet(path, subjectId + "", bs);
				if (allBs == null) allBs = bs;
				else allBs.or(bs);
			}
		}
		
		BitSetUtils.writeBitSet(path, "0", allBs);
		return 1;
	}
	
	
	public int createBrand(int merchantId) {		
		String path = "F:/store/search/" + merchantId + "/brand/";
		
		// 创建目录
		File pathFile = new File(path);
		if (!pathFile.exists()) {
			if (!pathFile.mkdirs()) return -1;
		}
		
		// 删除原来的索引
		File[] f = pathFile.listFiles();
		for (File file : f) {
			file.delete();
		}
		
		BitSet allBs = null; // 全品牌索引 文件名称为"0"
		String brandSql =  "select brandId from prod_brand where merchantId = ?";	
		List<Integer> brandList =  getJdbcTemplate().queryForList(brandSql, Integer.class, merchantId);
		for (Integer brandId : brandList) {
			BitSet bs = new BitSet();
			String prodSql = "select prodId from product where brandId = ?";		
			List<Integer> prodList =  getJdbcTemplate().queryForList(prodSql, Integer.class, brandId);
			for (Integer prodId : prodList) {
				bs.set(prodId);
			}
			if (bs.cardinality() != 0) {
				BitSetUtils.writeBitSet(path, brandId + "", bs);
				if (allBs == null) allBs = bs;
				else allBs.or(bs);
			}
		}
		BitSetUtils.writeBitSet(path, "0", allBs);
		return 1;
	}
	
	
	public int createDisable(int merchantId) {
		String disablePath = "F:/store/search/" + merchantId + "/disable/";
		// 创建目录
		File pathFile = new File(disablePath);
		if (!pathFile.exists()) {
			if (!pathFile.mkdirs()) return -1;
		}
		
		// 删除原来的索引
		File[] f = pathFile.listFiles();
		for (File file : f) {
			file.delete();
		}
		
		String sql = "select p.prodId from product p, repo r where p.repoId = r.repoId and r.merchantId = ? and p.prodStatus < 1";
		BitSet disableBs = new BitSet();
		List<Integer> prodIdList = getJdbcTemplate().queryForList(sql, Integer.class, merchantId);
		for (Integer prodId : prodIdList) {
			disableBs.set(prodId);
		}
		
		BitSetUtils.writeBitSet(disablePath, "disable", disableBs);
		
		return 1;
	}
	
	public boolean addDisable(int merchantId, int prodId) {
		String disablePath = "F:/store/search/" + merchantId + "/disable/";
		BitSet disableBs =  BitSetUtils.readBitSet(disablePath, "disable");
		if (disableBs != null) disableBs.set(prodId);
		BitSetUtils.writeBitSet(disablePath, "disable", disableBs);
		return true;
	}
	
	public boolean removeDisable(int merchantId, int prodId) {
		String disablePath = "F:/store/search/" + merchantId + "/disable/";
		BitSet disableBs =  BitSetUtils.readBitSet(disablePath, "disable");
		if (disableBs != null) disableBs.clear(prodId);
		BitSetUtils.writeBitSet(disablePath, "disable", disableBs);
		return true;
	}
	
}
