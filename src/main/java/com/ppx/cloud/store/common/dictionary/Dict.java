package com.ppx.cloud.store.common.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dict {
	
	private static Map<Integer, String> progStatusMap = new HashMap<Integer, String>();
	static {
		// 0:删除1:未启动2:进行中3:暂停4:停止  结束
		progStatusMap.put(1, "未启动");
		progStatusMap.put(2, "进行中");
		progStatusMap.put(3, "暂停");
		progStatusMap.put(4, "停止");
	}
	
	private static Map<Integer, String> prodStatusMap = new HashMap<Integer, String>();
	static {
		// 
		prodStatusMap.put(1, "草稿");
		prodStatusMap.put(2, "上架");
		prodStatusMap.put(3, "下架");
	}
	
	private static Map<Integer, String> changeTypeMap = new HashMap<Integer, String>();
	static {
		// 
		changeTypeMap.put(1, "进货");
		changeTypeMap.put(2, "盘盈");
		changeTypeMap.put(3, "盘亏");
		changeTypeMap.put(4, "损坏");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static String getProgStatusDesc(Integer status) {
		return progStatusMap.get(status);
	}

	public static String getProdStatusDesc(Integer status) {
		return prodStatusMap.get(status);
	}
	
	public static String getChangeTypeDesc(Integer type) {
		return changeTypeMap.get(type);
	}
	
	public static List<DictBean> listChangeType() {
		List<DictBean> returnList = new ArrayList<DictBean>();
		Set<Integer> set = changeTypeMap.keySet();
		for (Integer v : set) {
			returnList.add(new DictBean(v, changeTypeMap.get(v)));
		}
		return returnList;
	}
	
	public static List<DictBean> listProdStatus() {
		List<DictBean> returnList = new ArrayList<DictBean>();
		
		Set<Integer> set = prodStatusMap.keySet();
		for (Integer v : set) {
			returnList.add(new DictBean(v, prodStatusMap.get(v)));
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
