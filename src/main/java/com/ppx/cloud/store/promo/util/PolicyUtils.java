package com.ppx.cloud.store.promo.util;

import java.util.ArrayList;
import java.util.List;


/**
 * 第一次查询时生成？启动方案时，更新skuId的状态(或删除)
 * @author dengxz
 * @date 2017年12月7日
 */
public class PolicyUtils {
	private static List<Policy> policyTypeList = new ArrayList<Policy>();
	
	private static List<Policy> catPolicyList = new ArrayList<Policy>();
	private static List<Policy> brandPolicyList = new ArrayList<Policy>();
	private static List<Policy> subjectPolicyList = new ArrayList<Policy>();
	
	private static List<Policy> prodPolicyList = new ArrayList<Policy>();
	
	static {
		// type
		policyTypeList.add(new Policy("CAT", "CAT"));
		policyTypeList.add(new Policy("BRAND", "BRAND"));
		policyTypeList.add(new Policy("SUBJECT", "SUBJECT"));
		
		// cat
		catPolicyList.add(new Policy("%", "%"));
		catPolicyList.add(new Policy("-", "-"));
		
		// brand
		brandPolicyList.add(new Policy("%", "%"));
		brandPolicyList.add(new Policy("-", "-"));
		
		// brand
		subjectPolicyList.add(new Policy("%", "%"));
		subjectPolicyList.add(new Policy("-", "-"));
		
		// prod
		prodPolicyList.add(new Policy("%", "%"));
		prodPolicyList.add(new Policy("%", "%"));
		
		
		
		// $:7 2+%:0.5 2+%:0.7 4-1
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static List<Policy> listPolicyType() {
		return policyTypeList;
	}
	
	public static List<Policy> listCatPolicy() {
		return catPolicyList;
	}
	
	public static List<Policy> listBrandPolicy() {
		return brandPolicyList;
	}
	
	public static List<Policy> listSubjectPolicy() {
		return subjectPolicyList;
	}
	
	public static List<Policy> listProdPolicy() {
		return prodPolicyList;
	}
	
	
	
	
	
	
	
	
	
	
	
}
