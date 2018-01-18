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
	
	//private static Policy specialPolicy = new Policy("S:y", "特价¥y");
	//private static Policy changePolicy = new Policy("E:y;C:y", "满¥y立减¥y");

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	static {
		// S:y D:pId,P:y E:y,C:y
		
		// type
		policyTypeList.add(new Policy("Category", "CAT_N"));
		policyTypeList.add(new Policy("Brand", "BRAND_N"));
		policyTypeList.add(new Policy("Product", "PRODUCT_N"));
		policyTypeList.add(new Policy("Special", "SPECIAL_N"));
		policyTypeList.add(new Policy("Change", "CHANG_N"));
		policyTypeList.add(new Policy("Dependence", "Dependence_N"));
		
		// cat
		catPolicyList.add(new Policy("%:d", "%:d"));
		catPolicyList.add(new Policy("E:y,-:y", "E:y,-:y"));
		
		// brand
		brandPolicyList.add(new Policy("%:d", "%:d"));
		brandPolicyList.add(new Policy("E:y,-:y", "E:y,-:y"));
		
		// product
		productPolicyList.add(new Policy("A:y", "A:y"));
		productPolicyList.add(new Policy("%:d", "%:d"));
		productPolicyList.add(new Policy("%:d,2:d", "%:d,2:d"));
		productPolicyList.add(new Policy("%:d,2+:d", "%:d,2+:d"));
		productPolicyList.add(new Policy("E:y,-:y", "E:y,-:y"));
		productPolicyList.add(new Policy("y:Y,n:N", "y:Y,n:N"));
		productPolicyList.add(new Policy("+:y", "+:y"));
		productPolicyList.add(new Policy("B:n,F:n", "B:n,F:n"));
		
		

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
