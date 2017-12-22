package com.ppx.cloud.store.promo.valuation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppx.cloud.common.jdbc.MyDaoSupport;


@Service
public class ValuationService extends MyDaoSupport {

	
	public int count(Map<Integer, SkuIndex> skuIndexMap) {
		
		
	
		
		String sSql = "select SKU_ID, PROD_ID, PRICE from sku where SKU_ID in (:skuIdList)";
	

		List<Integer> skuIdList = new ArrayList<Integer>();
		for (Integer skuId : skuIndexMap.keySet()) {
			skuIdList.add(skuId);
		}
		
		
		Map<String, Object> skuParamMap = new HashMap<String, Object>();
		skuParamMap.put("skuIdList", skuIdList);		
		
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		List<SkuIndex> skuList = jdbc.query(sSql, skuParamMap, BeanPropertyRowMapper.newInstance(SkuIndex.class));
		
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
		Map<String, Object> prodParamMap = new HashMap<String, Object>();
		prodParamMap.put("prodIdList", prodIdList);	
		String indexSql = "select (select SKU_ID from sku where PROD_ID = t.PROD_ID) SKU_ID, t.PROD_ID, t.PROG_ID, t.INDEX_POLICY POLICY from"
				+ " (select * from program_index where PROD_ID in (:prodIdList) order by INDEX_PRIO desc) t group by t.PROD_ID";
		List<SkuIndex> indexList = jdbc.query(indexSql, prodParamMap, BeanPropertyRowMapper.newInstance(SkuIndex.class));
		for (SkuIndex index : indexList) {
			SkuIndex newIndex = skuIndexMap.get(index.getSkuId());
			newIndex.setProgId(index.getProgId());
			newIndex.setPolicy(index.getPolicy());
		}
		
		
		
		
		// valuation >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		
		Map<Integer, Integer> groupNum = new HashMap<Integer, Integer>();
		Map<Integer, Float> enoughYen = new HashMap<Integer, Float>();
		Set<Integer> prodId = new HashSet<Integer>();
		
		skuIndexMap.values().forEach(item -> {
			if (item.getProgId() == null) return;
			groupNum.put(item.getProgId(), groupNum.get(item.getProgId()) == null ? 0 : groupNum.get(item.getProgId()) +  item.getNum());
			enoughYen.put(item.getProgId(), enoughYen.get(item.getProgId()) == null ? 0 : 
				enoughYen.get(item.getProgId()) + item.getPrice() * item.getNum());
			prodId.add(item.getProdId());
		});
		
		// 排序 按progId分组，同级价格低在下面，为了价低者折，价低者减
		List<SkuIndex> skuIndexList = new ArrayList<SkuIndex>();
		skuIndexList.addAll(skuIndexMap.values());
		skuIndexList.sort(new SkuIndexComparator());
		
		int count2discount = 0;
		int countMoreAdd = 0;
		int countBuyFree = 0;
		
		for (SkuIndex index : skuIndexList) {
			String poli = index.getPolicy();
			String poli_1 = "";
			String poli_2 = "";
			int gN = groupNum.get(index.getProgId());
			float eYen = enoughYen.get(index.getProgId());
			if (poli != null) {
				String[] item = poli.split(",");
				poli_1 = item[0];
				if (item.length == 2) {
					poli_1 = item[1];
				}
			}
			
			if (poli == null) {
				index.setItemPrice(index.getPrice() * index.getNum());
			}
			else if (poli.indexOf("S") == 0) {
				float p = Float.parseFloat(poli_1.split(":")[1]);
				index.setItemPrice(p * index.getNum());
			}
			else if (poli.indexOf("A") == 0) {
				float p = Float.parseFloat(poli_1.split(":")[1]);
				index.setItemPrice(p * index.getNum());
			}
			else if (poli.startsWith("%")) {
				if ("".equals(poli_2) || gN == 1) {
					float d = Float.parseFloat(poli_1.split(":")[1]);
					index.setItemPrice(index.getPrice() * index.getNum() * d);
				}
				else if (poli_2.startsWith("2+")) {
					float d = Float.parseFloat(poli_2.split(":")[1]);
					index.setItemPrice(index.getPrice() * index.getNum() * d);
				}
				else if (poli_2.startsWith("2")) {
					float batch1D = Float.parseFloat(poli_1.split(":")[1]);
					float batch2D = Float.parseFloat(poli_2.split(":")[1]);
					int batch1N = (int)Math.floor(gN/2) - count2discount;
					count2discount += index.getNum();
					if (batch1N >= index.getNum()) {
						index.setItemPrice(index.getPrice() * index.getNum());
					}
					else if (batch1N <= 0) {
						index.setItemPrice(index.getPrice() * batch2D);
					}
					else {
						index.setItemPrice(index.getPrice() * batch1N * batch1D + index.getPrice() * (index.getNum() - batch1N) * batch2D);
					}
				}
			}
			else if (poli.startsWith("+")) {
				if (gN == 1) {
					index.setItemPrice(index.getPrice());
				}
				else {
					float batch1Y = Float.parseFloat(poli_1.split(":")[1]);
					int batch1N = (int)Math.floor(gN/2) - countMoreAdd;
					countMoreAdd += index.getNum();
					if (batch1N >= index.getNum()) {
						index.setItemPrice(index.getPrice() * index.getNum());
					}
					else if (batch1N <= 0) {
						index.setItemPrice(batch1Y * index.getNum());
					}
					else {
						index.setItemPrice(index.getPrice() * batch1N + batch1Y * (index.getNum() - batch1N));
					}
				}
			}
			else if (poli_1.endsWith("Y")) {
				float y = Float.parseFloat(poli_1.split(":")[0]);
				int n = Integer.parseInt(poli_2.split(":")[0]);
				if (n < gN) {
					float avgP = y / n;
					index.setItemPrice(avgP * index.getNum());
				}
				else {
					index.setItemPrice(index.getPrice() * index.getNum());
				}
			}
			else if (poli_1.startsWith("E") && poli_2.startsWith("-")) {
				float e = Float.parseFloat(poli_1.split(":")[1]);
				int y = Integer.parseInt(poli_2.split(":")[1]);
				
				if (eYen >= e) {
					float avgMinus = (float)Math.floor(eYen/e) * y / gN;
					index.setItemPrice((index.getPrice() - avgMinus) * index.getNum());
				}
			}
			else if (poli_1.startsWith("B")) {
				int buyN = Integer.parseInt(poli_1.split(":")[1]);
				int freeN = Integer.parseInt(poli_2.split(":")[1]);
				
				int gFreeN = (int)Math.floor(gN/buyN) * freeN;
				
				int batch1N = gN - gFreeN - countBuyFree;
				countBuyFree += index.getNum();
				if (buyN > gN) {
					index.setItemPrice(index.getPrice() * index.getNum());
				}
				else if (batch1N <= 0) {
					index.setItemPrice(0f);
				}
				else {
					if (batch1N >= index.getNum()) {
						index.setItemPrice(index.getPrice() * index.getNum());
					}
					else {
						index.setItemPrice(index.getPrice() * batch1N);
					}
				}
			}
			else if (poli_1.startsWith("D")) {
				int dependProdId = Integer.parseInt(poli_1.split(":")[1]);
				float dependPrice = Float.parseFloat(poli_2.split(":")[1]);
				if (prodId.contains(dependProdId)) {
					index.setItemPrice(dependPrice * index.getNum());
				}
				else {
					index.setItemPrice(index.getPrice() * index.getNum());
				}
			}
		};
		
		
		
		try {
			String s = new ObjectMapper().writeValueAsString(skuIndexList);
			System.out.println("xxxxxx:" + s);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
		
		
		
		
		
		
		
		
		// print
//		skuIndexMap.values().forEach(item ->{
//			System.out.println(item.getPrice());
//		});
		
		
	
		
		
		return 1;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
