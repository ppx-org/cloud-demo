package com.ppx.cloud.store.common.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dict {

	private static Map<Integer, String> prodStatusMap = new HashMap<Integer, String>();
	static {
		// 
		prodStatusMap.put(1, "草稿");
		prodStatusMap.put(2, "上架");
		prodStatusMap.put(3, "下架");
	}
	
	private static Map<Integer, String> changeStockTypeMap = new HashMap<Integer, String>();
	static {
		// 
		changeStockTypeMap.put(1, "进货");
		changeStockTypeMap.put(2, "盘盈");
		changeStockTypeMap.put(3, "盘亏");
		changeStockTypeMap.put(4, "损坏");
		changeStockTypeMap.put(5, "下单");
	}
	
	private static Map<Integer, String> progStatusMap = new HashMap<Integer, String>();
	static {
		// 0:删除1:未启动2:进行中3:暂停4:停止  结束
		progStatusMap.put(1, "未启动");
		progStatusMap.put(2, "进行中");
		progStatusMap.put(3, "暂停");
		progStatusMap.put(4, "停止");
	}
	
	private static Map<Integer, String> orderStatusMap = new HashMap<Integer, String>();
	static {
		orderStatusMap.put(1, "待付款");
		orderStatusMap.put(2, "待配货");
		orderStatusMap.put(3, "待提货");
		orderStatusMap.put(4, "交易完成");
		orderStatusMap.put(5, "交易取消");
	}
	
	private static Map<Integer, String> orderItemStatusMap = new HashMap<Integer, String>();
	static {
		orderItemStatusMap.put(1, "未配送");
		orderItemStatusMap.put(2, "已锁定");
		orderItemStatusMap.put(3, "已配置");
		orderItemStatusMap.put(4, "已配送");
	}
	
	public static String getOrderItemStatusDesc(Integer status) {
		return orderItemStatusMap.get(status);
	}
	
	public static List<DictBean> listOrderItemStatus() {
		List<DictBean> returnList = new ArrayList<DictBean>();
		
		Set<Integer> set = orderItemStatusMap.keySet();
		for (Integer v : set) {
			returnList.add(new DictBean(v, orderItemStatusMap.get(v)));
		}
		return returnList;
	}
	
	
	public static String getOrderStatusDesc(Integer status) {
		return orderStatusMap.get(status);
	}
	
	public static List<DictBean> listOrderStatus() {
		List<DictBean> returnList = new ArrayList<DictBean>();
		
		Set<Integer> set = orderStatusMap.keySet();
		for (Integer v : set) {
			returnList.add(new DictBean(v, orderStatusMap.get(v)));
		}
		return returnList;
	}
	
	
	public static String getProdStatusDesc(Integer status) {
		return prodStatusMap.get(status);
	}
	
	public static List<DictBean> listProdStatus() {
		List<DictBean> returnList = new ArrayList<DictBean>();
		
		Set<Integer> set = prodStatusMap.keySet();
		for (Integer v : set) {
			returnList.add(new DictBean(v, prodStatusMap.get(v)));
		}
		return returnList;
	}
	
	
	public static String getProgStatusDesc(Integer status) {
		return progStatusMap.get(status);
	}
	
	
	public static String getChangeStockTypeDesc(Integer type) {
		return changeStockTypeMap.get(type);
	}
	
	public static List<DictBean> listChangeStockType() {
		List<DictBean> returnList = new ArrayList<DictBean>();
		Set<Integer> set = changeStockTypeMap.keySet();
		for (Integer v : set) {
			returnList.add(new DictBean(v, changeStockTypeMap.get(v)));
		}
		return returnList;
	}
	
	
	
	public static List<DictBean> listImgX() {
		List<DictBean> returnList = new ArrayList<DictBean>();
		for (int i = 0; i < 5; i++) {
			returnList.add(new DictBean(i, i + ""));
		}
		return returnList;
	}
	
	public static List<DictBean> listImgY() {
		List<DictBean> returnList = new ArrayList<DictBean>();
		for (int i = 0; i < 10; i++) {
			returnList.add(new DictBean(i, i + ""));
		}
		return returnList;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}

