package com.ppx.cloud.store.promo.util;

import java.util.ArrayList;
import java.util.List;

public class PolicyUtils {
	private static List<Policy> policyList = new ArrayList<Policy>();
	
	static {
		policyList.add(new Policy("CAT_DISCOUNT", "类目-discount"));
	}
	
	public static List<Policy> listPolicy() {
		return policyList;
	}
}
