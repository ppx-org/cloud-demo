package com.ppx.cloud.store.order.torepo;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.storecommon.order.bean.OrderItem;

@Service
public class RepoOrderService extends MyDaoSupport {
	
	
	public PageList<OrderItem> listOrderItemByRepo(Page page, Integer repoId, OrderItem bean) {
				
		MyCriteria c = createCriteria("where")
				.addAnd("o.STORE_ID = ?", bean.getStoreId())
				.addAnd("i.ITEM_ID = ?", bean.getItemId())
				.addAnd("o.ORDER_ID = ?", bean.getOrderId());
		
		// 1:未配送
		if (bean.getItemStatus() != null && bean.getItemStatus() == 1) {
			c.addAnd("s.ITEM_STATUS is null");
		}
		else {
			c.addAnd("s.ITEM_STATUS = ?", bean.getItemStatus());
		}
				
					
		StringBuilder cSql = new StringBuilder("select count(*) from order_item i join product p on i.PROD_ID = p.PROD_ID and p.REPO_ID = ?"
				+ " join user_order o on i.ORDER_ID = o.ORDER_ID and o.ORDER_STATUS = ?"
				+ " left join order_item_status s on i.ITEM_ID = s.ITEM_ID ").append(c);
		StringBuilder qSql = new StringBuilder("select i.*, o.STORE_ID, ifnull(s.ITEM_STATUS, 1) ITEM_STATUS from order_item i join product p on i.PROD_ID = p.PROD_ID and p.REPO_ID = ?" 
				+ " join user_order o on i.ORDER_ID = o.ORDER_ID and o.ORDER_STATUS = ?"
				+ " left join order_item_status s on i.ITEM_ID = s.ITEM_ID ").append(c).append(" order by i.ORDER_ID desc, i.ITEM_ID desc");
		c.addPrePara(repoId);
		// 2:已付款
		c.addPrePara(2);
		
		List<OrderItem> list = queryPage(OrderItem.class, page, cSql, qSql, c.getParaList());
		return new PageList<OrderItem>(list, page);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	@Transactional
	public int lockItem(Integer itemId) {
		int operator = GrantContext.getLoginAccount().getAccountId();
		String sql = "insert into order_item_status(ITEM_ID, ITEM_STATUS, LOCK_TIME, LOCK_OPERATOR)"
				+ "select ?, 2, now(), ? from dual where not exists (select 1 from order_item_status where ITEM_ID = ?)";
		int r = getJdbcTemplate().update(sql, itemId, operator, itemId);
		return r;
	}
	
	@Transactional
	public int configItem(Integer itemId) {
		
		String lockSql = "select ITEM_STATUS from order_item_status where ITEM_ID = ?";
		int status = getJdbcTemplate().queryForObject(lockSql, Integer.class, itemId);
		if (status != 2) {
			return 0;
		}
		
		int operator = GrantContext.getLoginAccount().getAccountId();
		String updateSql = "update order_item_status set ITEM_STATUS = ?, CONFIG_TIME = now(), CONFIG_OPERATOR = ? where ITEM_ID = ?";
		int r = getJdbcTemplate().update(updateSql, 3, operator, itemId);
		return r;
	}
	
	
	
	
	
	
	
	
	
	
	

}
