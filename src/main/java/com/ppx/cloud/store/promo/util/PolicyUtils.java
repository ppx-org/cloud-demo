package com.ppx.cloud.store.promo.util;

import java.util.ArrayList;
import java.util.List;


/**
 * 第一次查询时生成？启动方案时，更新skuId的状态(或删除)
 * @author dengxz
 * @date 2017年12月7日
 */
public class PolicyUtils {
	private static List<Policy> policyList = new ArrayList<Policy>();
	
	static {
		policyList.add(new Policy("CAT_DISCOUNT", "CAT-discount"));
		policyList.add(new Policy("BRAND_DISCOUNT", "BRAND-discount"));
		policyList.add(new Policy("SUBJECT_DISCOUNT", "SUBJECT-discount"));
	}
	
	public static List<Policy> listPolicy() {
		return policyList;
	}
}
