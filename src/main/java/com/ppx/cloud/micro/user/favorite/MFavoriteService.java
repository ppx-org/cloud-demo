package com.ppx.cloud.micro.user.favorite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.micro.common.WxUser;
import com.ppx.cloud.storecommon.query.bean.MQueryProduct;
import com.ppx.cloud.storecommon.query.service.QueryCommonService;


@Service
public class MFavoriteService extends MyDaoSupport {
	
	@Autowired
	private QueryCommonService queryServ;
	
	public int addProduct(@RequestParam Integer prodId) {
		WxUser u = MGrantContext.getWxUser();
		
		String sql = "insert into user_favorite(OPENID, PROD_ID, STORE_ID) values(?, ?, ?)";
		int r = getJdbcTemplate().update(sql, u.getOpenid(), prodId, u.getStoreId());
		return r;
	}
	
	
	
	public List<MQueryProduct> listProduct() {
		WxUser u = MGrantContext.getWxUser();
		
		String sql = "select PROD_ID from user_favorite where OPENID = ? and STORE_ID = ? order by CREATED desc";
		List<Integer> prodIdList = getJdbcTemplate().queryForList(sql, Integer.class, u.getOpenid(), u.getStoreId());
		
		List<MQueryProduct> list = queryServ.listProduct(prodIdList, u.getStoreId());
		return list;
	}
	
	
	
	
}
