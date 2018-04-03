package com.ppx.cloud.micro.user.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.demo.common.price.bean.SkuIndex;
import com.ppx.cloud.demo.common.price.service.PriceCommonService;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.micro.common.WxUser;


@Service
public class MCartService extends MyDaoSupport {
	
	@Autowired
	private PriceCommonService priceServ;
	
	public int addSku(@RequestParam Integer skuId) {
		WxUser u = MGrantContext.getWxUser();
		
		String sql = "insert into user_cart(OPENID, SKU_ID, SKU_NUM, STORE_ID) values(?, ?, ?, ?)"
				+ " ON DUPLICATE KEY UPDATE SKU_NUM = SKU_NUM + 1";
		int r = getJdbcTemplate().update(sql, u.getOpenid(), skuId, 1, u.getStoreId());
		return r;
	}
	
	public int editSkuNum(Integer skuId, Integer num) {
		WxUser u = MGrantContext.getWxUser();
		String sql = "update user_cart set SKU_NUM = ? where OPENID = ? and SKU_ID = ?";
		int r = getJdbcTemplate().update(sql, num, u.getOpenid(), skuId);
		return r;
	}
	
	public List<SkuIndex> listSku() {
		WxUser u = MGrantContext.getWxUser();
		
		String sql = "select SKU_ID, SKU_NUM NUM from user_cart where OPENID = ? and STORE_ID = ?";
		List<SkuIndex> skuIndexList = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(SkuIndex.class), u.getOpenid(), u.getStoreId());
		
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
