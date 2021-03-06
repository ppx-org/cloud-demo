package com.ppx.cloud.store.product.changestatus;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;


@Service
public class ChangeStatusService extends MyDaoSupport {
	
	public PageList<ChangeStatus> listChangeStatus(Page page, Integer prodId) {
		
		List<Object> paraList = new ArrayList<Object>();
		paraList.add(prodId);
	
		StringBuilder cSql = new StringBuilder("select count(*) from change_status c where c.PROD_ID = ?");
		StringBuilder qSql = new StringBuilder("select c.* from change_status c where c.PROD_ID = ? order by CREATED desc");
		
		List<ChangeStatus> list = queryPage(ChangeStatus.class, page, cSql, qSql, paraList);
		return new PageList<ChangeStatus>(list, page);
	}
	
	
	public String getProdMsg(Integer prodId) {
		String sql = "select ifnull((select PROD_TITLE from product where PROD_ID = ?), '')";
		String msg = getJdbcTemplate().queryForObject(sql, String.class, prodId);
		return msg;
	}
	
	
	
}
