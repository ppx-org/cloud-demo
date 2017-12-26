package com.ppx.cloud.search.util;

import java.util.HashMap;
import java.util.Map;

public class SearchDict {
	
	
	private static Map<Integer, String> versionStatusMap = new HashMap<Integer, String>();
	
	static {
		versionStatusMap.put(1, "未处理");
		versionStatusMap.put(2, "已生成");
		versionStatusMap.put(3, "使用中");
	}
	
	public static String getVersionStatusDesc(Integer versionStatus) {
		return versionStatusMap.get(versionStatus);
	}
}
