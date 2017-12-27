package com.ppx.cloud.search.query;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.search.query.bean.QueryPage;
import com.ppx.cloud.search.query.bean.QueryPageList;
import com.ppx.cloud.search.query.bean.QueryProduct;
import com.ppx.cloud.search.util.BitSetUtils;
import com.ppx.cloud.search.util.WordUtils;


@Service
public class QueryService extends MyDaoSupport {
	
	
	public QueryPageList query(String w) {
		
		
		QueryPage p = new QueryPage();
		List<Integer> prodIdList = findProdId(w, p);
		List<QueryProduct> prodList = listProduct(prodIdList);
		
		return new QueryPageList(prodList, p);
	}
	
	private List<Integer> findProdId(String w, QueryPage p) {
		if (StringUtils.isEmpty(w)) {
			return null;
		}
		
		// TODO 自己的st
		// title
		String versionName = "V1";
		
		BitSet titleBs = BitSetUtils.readBitSet(versionName, "title", w);
		
		if (titleBs == null && w.length() > 1) {
			// split
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
	
		
		
		p.setTotalRows(titleBs.cardinality());
		List<Integer> prodIdList = BitSetUtils.bsToPage(titleBs, (p.getPageNumber() - 1) * p.getPageSize(), p.getPageSize());
		
		
		
		
		
		System.out.println("out......list:" + prodIdList);
		System.out.println("out......size:" + p.getTotalRows());
		
		
	
		
		
		
		
		
		return prodIdList;
	}
	
	
	
	
	
	
	
	
	
	
	
	private List<QueryProduct> listProduct(List<Integer> prodIdList) {
		
		
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
