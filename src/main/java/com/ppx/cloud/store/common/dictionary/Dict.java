package com.ppx.cloud.store.common.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dict {
	
	private static Map<Integer, String> programStatusMap = new HashMap<Integer, String>();
	static {
		// 0:删除1:未启动2:进行中3:暂停4:停止  结束
		programStatusMap.put(1, "未启动");
		programStatusMap.put(2, "进行中");
		programStatusMap.put(3, "暂停");
		programStatusMap.put(4, "停止");
	}
	
	public static String getProgramStatusDesc(Integer status) {
		return programStatusMap.get(status);
	}

	
	private static Map<Integer, String> productStatusMap = new HashMap<Integer, String>();
	static {
		// 
		productStatusMap.put(1, "-");
		productStatusMap.put(2, "上");
		productStatusMap.put(3, "下");
	}
	
	public static String getProductStatusDesc(Integer status) {
		return productStatusMap.get(status);
	}
	
	
	
	
	
	
	
	
	
	public static List<DictBean> listProductStatus() {
		List<DictBean> returnList = new ArrayList<DictBean>();
		
		Set<Integer> set = productStatusMap.keySet();
		for (Integer v : set) {
			returnList.add(new DictBean(v, productStatusMap.get(v)));
		}
		return returnList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
