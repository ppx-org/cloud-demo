package com.ppx.cloud.micro.user.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.common.page.MPageList;
import com.ppx.cloud.demo.common.order.OrderItem;
import com.ppx.cloud.demo.common.order.UserOrder;
import com.ppx.cloud.demo.common.price.bean.SkuIndex;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.micro.user.order.bean.ConfirmOrderItem;
import com.ppx.cloud.micro.user.order.bean.ConfirmOrderPara;
import com.ppx.cloud.micro.user.order.bean.ConfirmReturn;
import com.ppx.cloud.micro.user.order.bean.OverflowSku;


@Service
public class MOrderService extends MyDaoSupport {
	
	
	public MPageList<UserOrder> listMyOrder(MPage page) {
		String openid = MGrantContext.getWxUser().getOpenid();
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		StringBuilder sql = new StringBuilder("select ORDER_ID, ORDER_TIME, ORDER_STATUS, ORDER_PRICE from user_order where OPENID = ? and STORE_ID = ?");
		
		
		List<UserOrder> orderList = mQueryPage(UserOrder.class, page, sql, openid, storeId);
		
		if (orderList.size() == 0) {
			return new MPageList<UserOrder>(orderList, page); 
		}
		
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
	
	
	
	@Transactional
	public ConfirmReturn submitOrder(ConfirmOrderItem comfirmOrderItem, ConfirmOrderPara para) {
		ConfirmReturn confirmReturn = new ConfirmReturn();
		
		String openid = MGrantContext.getWxUser().getOpenid();
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		Map<Integer, Integer> stockMap = new HashMap<Integer, Integer>();
		Integer[] skuId = para.getSkuId();
		Integer[] num = para.getNum();
		for (int i = 0; i < skuId.length; i++) {
			stockMap.put(skuId[i], num[i]);
		}
		
		
		
		// lock >>>>>>>>>>>>>>>>>>>
		NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(getJdbcTemplate());
		Map<String, Object> lockPara = new HashMap<String, Object>();
		lockPara.put("skuIdArray", stockMap.keySet());
		String lockSql = "select SKU_ID, STOCK_NUM from sku where SKU_ID in (:skuIdArray) for update";
		List<Map<String, Object>> stockList = jdbc.queryForList(lockSql, lockPara);
		for (Map<String, Object> map : stockList) {
			Integer id = (Integer)map.get("SKU_ID");
			Integer n = (Integer)map.get("STOCK_NUM");
			if (stockMap.get(id) > n) {
				confirmReturn.addOverflowSku(new OverflowSku(id, n));
			}
		}
		if (confirmReturn.getOverflowList().size() > 0) {
			confirmReturn.setResult(1);
			return confirmReturn;
		}
		
		// user_order
		UserOrder order = new UserOrder();
		order.setOpenid(openid);
		order.setStoreId(storeId);
		order.setOrderTime(new Date());
		order.setOrderStatus(0);
		order.setOrderPrice(comfirmOrderItem.getTotalPrice());
		order.setPayPrice(comfirmOrderItem.getTotalPrice());
		
		super.insert(order);
		int orderId = super.getLastInsertId();
		
		//  order_item
		List<SkuIndex> skuIndexList = comfirmOrderItem.getSkuIndexList();
		
		List<Object[]> itemArgsList = new ArrayList<Object[]>();
		List<Object[]> stockArgsList = new ArrayList<Object[]>();
		List<Object[]> changeArgsList = new ArrayList<Object[]>();
		for (SkuIndex s : skuIndexList) {
			Object[] itemArg = {orderId, s.getSkuId(), s.getProdId(), s.getPrice(), s.getItemPrice(),
					s.getNum(), s.getProdTitle(), s.getSkuName(), s.getSkuImgSrc(), s.getPolicy()};
			itemArgsList.add(itemArg);
			
			Object[] stockArg = {s.getNum(), s.getSkuId()};
			stockArgsList.add(stockArg);
			
			Object[] changeArg = {s.getSkuId(), -s.getNum(), openid, orderId};
			changeArgsList.add(changeArg);
		}
		
		// minus stock
		String stockSql = "update sku set STOCK_NUM = STOCK_NUM - ? where SKU_ID = ?";
		getJdbcTemplate().batchUpdate(stockSql, stockArgsList);
		
		String changeSql = "insert into change_stock(SKU_ID, CHANGE_NUM, CHANGE_TYPE, CREATED, OPENID, ORDER_ID) values(?, ?, 5, now(), ?, ?)";
				getJdbcTemplate().batchUpdate(changeSql, changeArgsList);
		
		
		//  order item
		List<Object[]> argsList = new ArrayList<Object[]>();
		for (SkuIndex s : skuIndexList) {
			Object[] arg = {orderId, s.getSkuId(), s.getProdId(), s.getPrice(), s.getItemPrice(),
					s.getNum(), s.getProdTitle(), s.getSkuName(), s.getSkuImgSrc(), s.getPolicy()};
			argsList.add(arg);
		}
		

		String insertItemSql = "insert into order_item(ORDER_ID, SKU_ID, PROD_ID, ITEM_UNIT_PRICE, ITEM_PRICE, "
				+ "ITEM_NUM, ITEM_TITLE, ITEM_SKU, ITEM_IMG, ITEM_PROMO) values(?,?,?,?,?,"
				+ "?,?,?,?,?)";
		
		getJdbcTemplate().batchUpdate(insertItemSql, argsList);
		
		
		return confirmReturn;
	}
	
	
	
}
