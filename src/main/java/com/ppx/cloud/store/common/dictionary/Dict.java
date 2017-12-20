package com.ppx.cloud.store.common.dictionary;

import java.util.HashMap;
import java.util.Map;

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


}
