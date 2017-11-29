package com.ppx.cloud.grant.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.grant.common.LoginAccount;
import com.ppx.cloud.grant.filter.GrantFilterService;

@Service
public class MenuService {
	
	@Resource(name="configMongoTemplate")
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private GrantFilterService filterService;
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getMenu() {
		Query query = new Query();
		Criteria criteria = Criteria.where("_id").is(0);
		query.addCriteria(criteria);
		
		Map map =  mongoTemplate.findOne(query, Map.class, "grant_resource");
		if (map == null) return null;
		
		// --------------------请取uri数据到menu
		List<Map> uriList = mongoTemplate.findAll(Map.class, "grant_resource_uri");
		Map<Integer, String> uriMap = new HashMap<Integer, String>();
		for (Map m : uriList) {
			Integer _id = (Integer)m.get("_id");
			// 取第一个当菜单链接
			String uri = (String)((List)m.get("uri")).get(0);
			uriMap.put(_id, uri);
		}
		
		
		// 读取资源树
		LinkedHashMap<String, Object> treeMap = (LinkedHashMap<String, Object>)map.get("tree"); 
		
		// 允许的菜单
		LoginAccount account = GrantContext.getLoginAccount();
		
		int accountId = account.getAccountId();
		List<Integer> permitResIdList = filterService.getResIds(accountId);
		
		Map<String, Object> test = null;		
		if (!account.isMainAccont()) {
			// 不是主帐号则判断是否权限是否主帐号权限	
			List<Integer> mainPermitResIdList = filterService.getResIds(account.getMerchantId());
			test = filterNode(treeMap, uriMap, permitResIdList, mainPermitResIdList);
		}
		else {
			test = filterNode(treeMap, uriMap, permitResIdList, null);
		}
		
		return (List<Map<String, Object>>)test.get("n");
	}
	
	/**
	 * 当子账号登录时，显示的菜单必须是主账号拥有的
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> filterNode(LinkedHashMap<String, Object> treeMap, 
			Map<Integer, String> uriMap, List<Integer> permitResIdList, List<Integer> mainPermitResIdList) {
		
		Map<String, Object> newNode = new LinkedHashMap<String, Object>();
		String t = (String)treeMap.get("t");
		Integer i = (Integer)treeMap.get("i");
		Integer id = (Integer)treeMap.get("id");
		newNode.put("t", t);
		newNode.put("i", i);
		newNode.put("id", id);
		// 1:菜单
		if (i == 1) {
			newNode.put("uri", uriMap.get(id));
		}
		
		List<LinkedHashMap<String, Object>> list = (ArrayList<LinkedHashMap<String, Object>>)treeMap.get("n");
		if (list == null) {
			return newNode;
		}
		
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		for (LinkedHashMap<String, Object> map : list) {
			Map<String, Object> newMap = filterNode(map, uriMap, permitResIdList, mainPermitResIdList);
			
			Integer tmpId = (Integer)newMap.get("id");
			if (mainPermitResIdList == null) {
				// 2:操作
				if ((Integer)newMap.get("i") != 2 && permitResIdList.contains(tmpId)) {	
					newList.add(newMap);
				}
			}
			else {
				// 2:操作
				if ((Integer)newMap.get("i") != 2 && permitResIdList.contains(tmpId) && mainPermitResIdList.contains(tmpId)) {	
					newList.add(newMap);
				}
			}
		}
		// 如果空不加
		if (!newList.isEmpty()) {				
			newNode.put("n", newList);
		}
		return newNode;
	}

}
