package com.ppx.cloud.micro.user.cart;

import java.util.Date;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.util.DateUtils;
import com.ppx.cloud.micro.common.MGrantContext;


@Service
public class MCartService extends MyDaoSupport {
	
	public int addSku(@RequestParam Integer skuId) {
		String openid = MGrantContext.getWxUser().getOpenid();
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		String sql = "insert into user_cart(OPENID, SKU_ID, SKU_NUM, STORE_ID) values(?, ?, ?, ?) ON DUPLICATE KEY UPDATE SKU_NUM = SKU_NUM + 1";
		int r = getJdbcTemplate().update(sql, openid, skuId, 1, storeId);
		
		return r;
		
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> listSku() {
		
		
		return null;
	}
	
	
	
}
