package com.ppx.cloud.micro.user.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.storecommon.order.bean.OrderItem;
import com.ppx.cloud.storecommon.order.bean.UserOrder;
import com.ppx.cloud.storecommon.page.MPage;
import com.ppx.cloud.storecommon.page.MPageList;
import com.ppx.cloud.storecommon.query.bean.MQueryProduct;


@Service
public class MOrderService extends MyDaoSupport {
	
	
	
	
	public MPageList<UserOrder> listMyOrder(MPage page) {
		String openid = MGrantContext.getWxUser().getOpenid();
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		StringBuilder sql = new StringBuilder("select ORDER_ID, ORDER_TIME, ORDER_STATUS, ORDER_PRICE from user_order where OPENID = ? and STORE_ID = ?");
		
		
		List<UserOrder> orderList = mQueryPage(UserOrder.class, page, sql, openid, storeId);
		
		List<Integer> orderIdList = new ArrayList<Integer>();
		for (UserOrder userOrder : orderList) {
			orderIdList.add(userOrder.getOrderId());
		}
		
		
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("orderIdList", orderIdList);	
		
		String itemSql = "select * from order_item where ORDER_ID in (:orderIdList) order by ITEM_ID";
		
		List<OrderItem> orderItemList = jdbc.query(itemSql, paramMap, BeanPropertyRowMapper.newInstance(OrderItem.class));
		for (OrderItem orderItem : orderItemList) {
			for (UserOrder order : orderList) {
				if (orderItem.getOrderId() == order.getOrderId()) {
					order.addOrderItem(orderItem);
				}
			}
		}
		
		return new MPageList<UserOrder>(orderList, page);
	}
	
	
	
	
	
	
}
