package com.ppx.cloud.micro.user.favorite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.demo.common.query.QueryCommonService;
import com.ppx.cloud.demo.common.query.QueryProduct;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.micro.common.WxUser;


@Service
public class MFavoriteService extends MyDaoSupport {
	
	@Autowired
	private QueryCommonService queryServ;
	
	public boolean isFavorite(Integer prodId) {
		WxUser u = MGrantContext.getWxUser();
		String sql = "select count(*) from user_favorite where OPENID = ? and PROD_ID = ?";
		int r = getJdbcTemplate().queryForObject(sql, Integer.class, u.getOpenid(), prodId);
		if (r == 1) {
			return true;
		}
		return false;
	}
	

	public int addProduct(Integer prodId) {
		WxUser u = MGrantContext.getWxUser();
		
		String sql = "insert into user_favorite(OPENID, PROD_ID, STORE_ID) values(?, ?, ?)";
		int r = getJdbcTemplate().update(sql, u.getOpenid(), prodId, u.getStoreId());
		return r;
	}
	
	public int removeProduct(Integer prodId) {
		WxUser u = MGrantContext.getWxUser();
		
		String sql = "delete from user_favorite where OPENID = ? and PROD_ID = ?";
		int r = getJdbcTemplate().update(sql, u.getOpenid(), prodId);
		return r;
	}
	
	
	public List<QueryProduct> listProduct() {
		WxUser u = MGrantContext.getWxUser();
		
		String sql = "select PROD_ID from user_favorite where OPENID = ? and STORE_ID = ? order by CREATED desc";
		List<Integer> prodIdList = getJdbcTemplate().queryForList(sql, Integer.class, u.getOpenid(), u.getStoreId());
		
		List<QueryProduct> list = queryServ.listProduct(prodIdList, u.getStoreId());
		return list;
	}
	
	
	
	
	
	
}
