package com.ppx.cloud.grant.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mongodb.util.JSON;
import com.ppx.cloud.grant.common.CacheConfig;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.grant.common.LoginAccount;
import com.ppx.cloud.grant.filter.GrantFilterService;

@Service
public class ResourceService {
	
	@Resource(name="configMongoTemplate")
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private GrantFilterService filterService;
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> getResource() {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(0));
		LinkedHashMap<String, Object> resMap = mongoTemplate.findOne(query, LinkedHashMap.class, "grant_resource");		
		
		LoginAccount account = GrantContext.getLoginAccount();
		if (!account.isAdmin()) {
			// 管理员查看所有资源，商户主账号只能查看已经分配的资源
			List<Integer> permitResIdList = filterService.getResIds(account.getMerchantId());		
			Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
			returnMap.put("_id", 0);
			returnMap.put("tree", filterNode((LinkedHashMap<String, Object>)resMap.get("tree"), permitResIdList));
			return returnMap;
		}
		
		return resMap;
	}
	
	public void saveResource(String tree, String removeIds) {
		// 清除过滤器中缓存
		filterService.clearCache(CacheConfig.URI_INDEX_CACHE);
		filterService.clearCache(CacheConfig.MER_BIT_SET_CACHE);
		filterService.clearCache(CacheConfig.MER_RES_CACHE);
		filterService.clearCache(CacheConfig.MENU_OP_CACHE);
				
		if (!StringUtils.isEmpty(removeIds)) {
			String[] resId = removeIds.split(",");	
			Query removeQuery = new Query();
			removeQuery.addCriteria(Criteria.where("_id").in(new ArrayList<String>(Arrays.asList(resId))));
			mongoTemplate.remove(removeQuery, "grant_resource_uri");
		}
		
		Update update = new Update();		
		update.set("tree", JSON.parse(tree));
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(0));
		mongoTemplate.upsert(query, update, "grant_resource");
	}
	
	@SuppressWarnings("rawtypes")
	public Map getUri(Integer resId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(resId));
		return mongoTemplate.findOne(query, Map.class, "grant_resource_uri");
	}	
	
	@SuppressWarnings("rawtypes")
	private int saveToUri(String uri) {
		int index = getUriSeq();
		Update update = new Update();
		update.setOnInsert("index", index);
		
		Query uriQuery = new Query();
		uriQuery.addCriteria(Criteria.where("_id").is(uri));
		Map uriMap = mongoTemplate.findAndModify(uriQuery, update, FindAndModifyOptions.options().upsert(true).returnNew(true),
				Map.class, "grant_uri_index");			
		return (Integer)uriMap.get("index");
	}
	
	@SuppressWarnings("rawtypes")
	private int getUriSeq() {
		Query seqQuery = new Query();
		seqQuery.addCriteria(Criteria.where("_id").is("URI_SEQ"));
		Update update = new Update();
		update.inc("sequence_value", 1);
		Map seqMap = mongoTemplate.findAndModify(seqQuery, update, FindAndModifyOptions.options().upsert(true).returnNew(true)
				,Map.class, "grant_counters");
		int seq = (Integer)seqMap.get("sequence_value");
		return seq;
	}
	
	@SuppressWarnings("rawtypes")
	public Map saveUri(Integer resId, String uri, Integer menuId) {	
		// 清除过滤器中缓存
		filterService.clearCache(CacheConfig.URI_INDEX_CACHE);
		filterService.clearCache(CacheConfig.MER_BIT_SET_CACHE);
		filterService.clearCache(CacheConfig.MER_RES_CACHE);
		filterService.clearCache(CacheConfig.MENU_OP_CACHE);
				
		Update update = new Update();	
		String[] uriArray = uri.split(",");
		List<String> uriList = new ArrayList<String>();
		List<Integer> indexList = new ArrayList<Integer>();
		for (String u : uriArray) {
			uriList.add(u);
			int index = saveToUri(u);
			indexList.add(index);
		}		
		update.addToSet("uri").each(uriList.toArray());
		update.addToSet("uriIndex").each(indexList.toArray());
		if (menuId != null) {
			update.set("pMenuId", menuId);
		}
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(resId));	
		Map map = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().upsert(true).returnNew(true),
				Map.class, "grant_resource_uri");
		return map;
	}
	
	
	public void removeUri(Integer resId, String uri, int uriIndex) {
		// 清除过滤器中缓存
		filterService.clearCache(CacheConfig.URI_INDEX_CACHE);
		filterService.clearCache(CacheConfig.MER_BIT_SET_CACHE);
		filterService.clearCache(CacheConfig.MER_RES_CACHE);
		filterService.clearCache(CacheConfig.MENU_OP_CACHE);
				
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(resId));
		
		if ("-1".equals(uri)) {
			// mongodb数组只有一条数据时，删除整条记录
			mongoTemplate.remove(query, "grant_resource_uri");
		}
		else {
			Update update = new Update();	
			update.pull("uri", uri);
			update.pull("uriIndex", uriIndex);
			mongoTemplate.updateFirst(query, update, "grant_resource_uri");
		}
	}
	
	
	/**
	 * 根据permitResIdList,过滤掉没有分配的资源
	 */
	private Map<String, Object> filterNode(LinkedHashMap<String, Object> treeMap, List<Integer> permitResIdList) {
		
		Map<String, Object> newNode = new LinkedHashMap<String, Object>();
		newNode.put("t", treeMap.get("t"));
		newNode.put("i", treeMap.get("i"));
		newNode.put("id", treeMap.get("id"));
		
		@SuppressWarnings("unchecked")
		List<LinkedHashMap<String, Object>> list = (ArrayList<LinkedHashMap<String, Object>>)treeMap.get("n");
		if (list != null) {
			List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
			for (LinkedHashMap<String, Object> map : list) {
				Map<String, Object> newMap = filterNode(map, permitResIdList);
				Integer tmpId = (Integer)newMap.get("id");
				if (permitResIdList.contains(tmpId)) {					
					newList.add(newMap);
				}
			}
			if (!newList.isEmpty()) {				
				newNode.put("n", newList);
			}
		}
		return newNode;
	}
	
}
