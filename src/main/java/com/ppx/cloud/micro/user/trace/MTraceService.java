package com.ppx.cloud.micro.user.trace;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.demo.common.query.QueryCommonService;
import com.ppx.cloud.demo.common.query.QueryProduct;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.micro.common.WxUser;


@Service
public class MTraceService extends MyDaoSupport {
	
	@Autowired
	private QueryCommonService queryServ;

	

	public int addProduct(Integer prodId) {
		// TODO 存在的更新时间
		WxUser u = MGrantContext.getWxUser();
		
		String sql = "insert into user_trace(OPENID, PROD_ID, STORE_ID) values(?, ?, ?)";
		int r = getJdbcTemplate().update(sql, u.getOpenid(), prodId, u.getStoreId());
		return r;
	}
	
	public int removeProduct(Integer prodId) {
		WxUser u = MGrantContext.getWxUser();
		
		String sql = "delete from user_trace where OPENID = ? and PROD_ID = ?";
		int r = getJdbcTemplate().update(sql, u.getOpenid(), prodId);
		return r;
	}
	
	
	public List<QueryProduct> listProduct(MPage page) {
		WxUser u = MGrantContext.getWxUser();
		
		StringBuilder sql = new StringBuilder("select PROD_ID from user_trace where OPENID = ? and STORE_ID = ? order by CREATED desc");
		
		List<Integer> prodIdList = mQueryPage(Integer.class, page, sql, u.getOpenid(), u.getStoreId());
		
		List<QueryProduct> list = queryServ.listProduct(prodIdList, u.getStoreId());
		return list;
	}
	
	
	
	
	
	
}
