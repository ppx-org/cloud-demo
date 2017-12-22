package com.ppx.cloud.store.promo.valuation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;


@Service
public class ValuationService extends MyDaoSupport {

	
	public int count(Map<Integer, SkuIndex> skuIndexMap) {
		
		
	
		
		String sSql = "select SKU_ID, PROD_ID, PRICE from sku where SKU_ID in (:skuIdList)";
	

		List<Integer> skuIdList = new ArrayList<Integer>();
		for (Integer skuId : skuIndexMap.keySet()) {
			skuIdList.add(skuId);
		}
		
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("skuIdList", skuIdList);		
		
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		List<SkuIndex> skuList = jdbc.query(sSql, paramMap, BeanPropertyRowMapper.newInstance(SkuIndex.class));
		
		if (skuList.size() != skuIndexMap.size()) {
			// skuId不存在
			return -1;
		}
		
		List<Integer> prodIdList = new ArrayList<Integer>();
		for (SkuIndex index : skuList) {
			SkuIndex newIndex = skuIndexMap.get(index.getSkuId());
			newIndex.setProdId(index.getProdId());
			newIndex.setPrice(index.getPrice());
			prodIdList.add(index.getProdId());
		}
		
		// index >>>>>>>>>>>>>>>>>>>>>>>
		String indexSql = "select (select SKU_ID from sku where PROD_ID = t.PROD_ID) SKU_ID, t.PROD_ID, t.PROG_ID, t.INDEX_POLICY POLICY from"
				+ " (select * from program_index where PROD_ID in (:prodIdList) order by INDEX_PRIO desc) t group by t.PROD_ID";
		List<SkuIndex> indexList = jdbc.query(indexSql, paramMap, BeanPropertyRowMapper.newInstance(SkuIndex.class));
		for (SkuIndex index : indexList) {
			SkuIndex newIndex = skuIndexMap.get(index.getSkuId());
			newIndex.setProgId(index.getProgId());
			newIndex.setPolicy(index.getPolicy());
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// print
		skuIndexMap.values().forEach(item ->{
			System.out.println(item.getPrice());
		});
		
		
	
		
		
		return 1;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
