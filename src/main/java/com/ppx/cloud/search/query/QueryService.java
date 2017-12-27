package com.ppx.cloud.search.query;

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
import com.ppx.cloud.search.util.BitSetUtils;
import com.ppx.cloud.search.util.WordUtils;


@Service
public class QueryService extends MyDaoSupport {
	
	public List<Integer> findProdId(String w) {
		if (StringUtils.isEmpty(w)) {
			return null;
		}
		
		// title
		String versionName = "V1";
		
		BitSet titleBs = BitSetUtils.readBitSet(versionName, "title", w);
		
		if (titleBs == null && w.length() > 1) {
			// 分词搜索
			BitSet splitBs = new BitSet();
			String[] word = WordUtils.splitWord(w).split(",");
			for (String s : word) {
				BitSet bs = BitSetUtils.readBitSet(versionName, "title", s);
				if (bs != null) {
					splitBs.or(bs);
				}
			}
			titleBs = splitBs;
		}
		
		
		System.out.println("out......:" + titleBs);
		
		
		
		//titleBs.
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		List<Integer> prodIdList = new ArrayList<Integer>();
//		prodIdList.add(1);
//		prodIdList.add(3);
//		prodIdList.add(5);
		
		return prodIdList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public List<QueryProduct> listProduct(List<Integer> prodIdList) {
		
		
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> prodParamMap = new HashMap<String, Object>();
		prodParamMap.put("prodIdList", prodIdList);	
		
		
		// TODO 改成 curdate()
		String prodSql = "select p.PROD_ID, p.PROD_TITLE, s.PRICE, img.SKU_IMG_SRC PROD_IMG_SRC, idx.PROG_ID, idx.POLICY from product p join sku s on p.PROD_ID = s.PROD_ID left join " +
			"(select t.SKU_ID, t.SKU_IMG_SRC from (select * from sku_img order by SKU_IMG_PRIO desc) t group by t.SKU_ID) img on s.SKU_ID = img.SKU_ID left join " +
			"(select t.PROD_ID, t.PROG_ID, t.INDEX_POLICY POLICY from (select * from program_index i where date_format(now(), '%Y-%m-%d') between INDEX_BEGIN and INDEX_END order by INDEX_PRIO desc) t group by t.PROD_ID) idx on p.PROD_ID = idx.PROD_ID " + 
			"where p.PROD_ID in (:prodIdList)";
		
		List<QueryProduct> prodList = jdbc.query(prodSql, prodParamMap, BeanPropertyRowMapper.newInstance(QueryProduct.class));
	
		
		
		

		
		
		return prodList;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
