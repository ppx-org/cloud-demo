package com.ppx.cloud.micro.user.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.storecommon.price.bean.SkuIndex;
import com.ppx.cloud.storecommon.price.service.PriceCommonService;


@Service
public class MCartService extends MyDaoSupport {
	
	@Autowired
	private PriceCommonService priceServ;
	
	
	public int addSku(@RequestParam Integer skuId) {
		String openid = MGrantContext.getWxUser().getOpenid();
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		String sql = "insert into user_cart(OPENID, SKU_ID, SKU_NUM, STORE_ID) values(?, ?, ?, ?) ON DUPLICATE KEY UPDATE SKU_NUM = SKU_NUM + 1";
		int r = getJdbcTemplate().update(sql, openid, skuId, 1, storeId);
		
		return r;
		
	}
	
	public List<SkuIndex> listSku() {
		String openid = MGrantContext.getWxUser().getOpenid();
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		String sql = "select SKU_ID, SKU_NUM NUM from user_cart where OPENID = ? and STORE_ID = ?";
		List<SkuIndex> skuIndexList = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(SkuIndex.class), openid, storeId);
		
		// 计价
		Map<Integer, SkuIndex> skuIndexMap = new HashMap<Integer, SkuIndex>();
		for (SkuIndex skuIndex : skuIndexList) {
			skuIndexMap.put(skuIndex.getSkuId(), skuIndex);
		}
		Map<Integer, List<SkuIndex>> returnMap = priceServ.countPrice(skuIndexMap);
		
		List<SkuIndex> returnList = returnMap.get(1);
		
		return returnList;
	}
	
	
	
}
