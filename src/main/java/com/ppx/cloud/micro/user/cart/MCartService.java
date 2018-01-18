package com.ppx.cloud.micro.user.cart;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.storecommon.price.bean.SkuIndex;


@Service
public class MCartService extends MyDaoSupport {
	
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
		
		
		return skuIndexList;
	}
	
	
	
}
