package com.ppx.cloud.store.order.tostore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.demo.common.order.OrderItem;
import com.ppx.cloud.demo.common.order.OrderStatusHistory;
import com.ppx.cloud.demo.common.order.UserOrder;
import com.ppx.cloud.grant.common.GrantContext;

@Service
public class StoreOrderService extends MyDaoSupport {
	
	
	public PageList<UserOrder> listOrder(Page page, UserOrder bean) {
				
		MyCriteria c = createCriteria("and")
			.addAnd("ORDER_ID = ?", bean.getOrderId())
			.addAnd("ORDER_STATUS = ?", bean.getOrderStatus());
					
		StringBuilder cSql = new StringBuilder("select count(*) from user_order where STORE_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select * from user_order where STORE_ID = ?")
				.append(c).append(" order by ORDER_ID desc");
		c.addPrePara(bean.getStoreId());
		
		
		
		List<UserOrder> orderList = queryPage(UserOrder.class, page, cSql, qSql, c.getParaList());
		
		if (orderList.size() > 0) {
			orderList = listOrderItem(orderList);
		}
		
		
		return new PageList<UserOrder>(orderList, page);
	}
	
	
	
	public List<UserOrder> listOrderItem(List<UserOrder> orderList) {
		List<Integer> orderIdList = new ArrayList<Integer>();
		for (UserOrder o : orderList) {
			orderIdList.add(o.getOrderId());
		}
		
		
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("orderIdList", orderIdList);
		
		String itemSql = "select * from order_item where ORDER_ID in (:orderIdList)";
		List<OrderItem> itemList = jdbc.query(itemSql, paramMap, BeanPropertyRowMapper.newInstance(OrderItem.class));
		
		Map<Integer, List<OrderItem>> map = new HashMap<Integer, List<OrderItem>>();
		for (OrderItem item : itemList) {
			if (map.containsKey(item.getOrderId())) {
				List<OrderItem> list = map.get(item.getOrderId());
				list.add(item);
			}
			else {
				List<OrderItem> list = new ArrayList<OrderItem>();
				list.add(item);
				map.put(item.getOrderId(), list);
			}
		}
		
		for (UserOrder o : orderList) {
			o.setListItem(map.get(o.getOrderId()));
		}
		
		return orderList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Transactional
	public int finish(Integer orderId) {
		
		String lockSql = "select ORDER_STATUS from user_order where ORDER_ID = ?";
		int status = getJdbcTemplate().queryForObject(lockSql, Integer.class, orderId);
		// 3:待提货
		if (status != 3) {
			return 0;
		}
		
		String updateSql = "update user_order set ORDER_STATUS = ? where ORDER_ID = ?";
		// 4:交易完成
		int r = getJdbcTemplate().update(updateSql, 4, orderId);
		
		// status history
		OrderStatusHistory his = new OrderStatusHistory();
		int creator = GrantContext.getLoginAccount().getAccountId();
		his.setOrderId(orderId);
		his.setCreator(creator);
		his.setHistoryStatus(4);
		insert(his);
		
		
		return r;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
