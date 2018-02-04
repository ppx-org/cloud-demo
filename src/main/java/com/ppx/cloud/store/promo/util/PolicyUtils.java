package com.ppx.cloud.store.promo.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 

N:3,Y:10(10元3件)
+1(加一元多一件)
B:4,F:1(买4免1)
%:0.5(5折)
%:0.9,2:0.8(单件9折，第二件8折)
%:0.9,2+:0.8(单件9折，第二件及以上8折)
1:0.9,2:0.8(第一件9折，任两件8折)
1:0.9,2+:0.8(第一件9折，任两件及以上8折)
-:10,-3(满10元立减3元)
* 价低者免，价低者折


 * @author dengxz
 * @date 2017年12月11日
 */
public class PolicyUtils {
	
	
	
	private static List<Policy> policyTypeList = new ArrayList<Policy>();
	
	private static List<Policy> catPolicyList = new ArrayList<Policy>();
	private static List<Policy> brandPolicyList = new ArrayList<Policy>();
	private static List<Policy> productPolicyList = new ArrayList<Policy>();


	static {
		
		// type
		policyTypeList.add(new Policy("Category", "分类促销"));
		policyTypeList.add(new Policy("Brand", "品牌促销"));
		policyTypeList.add(new Policy("Product", "商品促销"));
		policyTypeList.add(new Policy("Special", "商品特价"));
		policyTypeList.add(new Policy("Change", "商品换购"));
		policyTypeList.add(new Policy("Dependence", "组合定价"));
		
		// cat
		catPolicyList.add(new Policy("%:d", "n折"));
		catPolicyList.add(new Policy("E:y,-:y", "满n元立减m元"));
		
		// brand
		brandPolicyList.add(new Policy("%:d", "n折"));
		brandPolicyList.add(new Policy("E:y,-:y", "满n元立减m元"));
		
		// product
		productPolicyList.add(new Policy("%:d", "n折"));
		productPolicyList.add(new Policy("%:d,2:d", "单件n折，第二件m折"));
		productPolicyList.add(new Policy("%:d,2+:d", "第一件n折，任两件及以上m折"));
		productPolicyList.add(new Policy("A:y", "n元均价"));
		productPolicyList.add(new Policy("E:y,-:y", "满n元立减m元"));
		productPolicyList.add(new Policy("y:Y,n:N", "n元m件"));
		productPolicyList.add(new Policy("+:y", "加n元多一件"));
		productPolicyList.add(new Policy("B:n,F:n", "买n免m"));
		

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
	
	public static List<Policy> listProductPolicy() {
		return productPolicyList;
	}


	
}


