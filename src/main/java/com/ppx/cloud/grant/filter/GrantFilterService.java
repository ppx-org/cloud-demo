package com.ppx.cloud.grant.filter;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.CacheConfig;


@Service
public class GrantFilterService extends MyDaoSupport {
	
	@Resource(name="configMongoTemplate")
	private MongoTemplate mongoTemplate;
	
	
	@Autowired
	@Qualifier(CacheConfig.LOCAL_CACHE)
	private CacheManager cacheManager;
	
	public void clearCache(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		cache.clear();
	}
	
	@Cacheable(value=CacheConfig.URI_INDEX_CACHE, cacheManager=CacheConfig.LOCAL_CACHE)
	public Integer getIndexFromUri(String uri) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(uri));
		
		Map<?, ?> map = mongoTemplate.findOne(query, Map.class, "grant_uri_index");
		if (map != null) {
			return (Integer)map.get("index");
		}
		return null;
	}
	
	@Cacheable(value=CacheConfig.MER_BIT_SET_CACHE, cacheManager=CacheConfig.LOCAL_CACHE)
	public BitSet getMerchantResBitSet(Integer merchantId) {
		
		BitSet grantBitset = new BitSet();	    
    	List<Integer> resIds = getResIds(merchantId);
    	List<Integer> uriIndexes = getUriIndexes(resIds);
    	for (Integer index : uriIndexes) {
    		grantBitset.set(index);
		}
    	return grantBitset;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Integer> getUriIndexes(List<Integer> resIds) {		
		Query query = new Query();
		
		Criteria criteria = Criteria.where("_id").in(resIds);
		query.addCriteria(criteria);
		
		List<Integer> returnList = new ArrayList<Integer>();
		List<Map> list = mongoTemplate.find(query, Map.class, "grant_resource_uri");
		if (list != null) {
			for (Map map : list) {
				List<Integer> uriList = (List<Integer>)map.get("uriIndex");
				returnList.addAll(uriList);
			}
		}
		return returnList;
	}
	
	@SuppressWarnings("unchecked")
	@Cacheable(value=CacheConfig.MER_RES_CACHE, cacheManager=CacheConfig.LOCAL_CACHE)
	public List<Integer> getResIds(Integer accountId) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(accountId));		
		Map<?, ?> map = mongoTemplate.findOne(query, Map.class, "grant_authorize");
		if (map != null) {
			return (List<Integer>)map.get("resIds");
		}
		else {
			return new ArrayList<Integer>();
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Cacheable(value=CacheConfig.MENU_OP_CACHE, cacheManager=CacheConfig.LOCAL_CACHE)
	public List<Map> getOpUri(String menuUri) {
		
		// 在grant_resource_uri中，如果是操作项将有pMenuId字段
		Query query = new Query();
		query.addCriteria(Criteria.where("uri").in(menuUri));
		
		List<Map> list = mongoTemplate.find(query, Map.class, "grant_resource_uri");		
		List<Integer> menuList = new ArrayList<Integer>();
		for (Map map : list) {
			Integer menuId = (Integer)map.get("_id");
			menuList.add(menuId);
		}
	
		Query opQuery = new Query();
		opQuery.addCriteria(Criteria.where("pMenuId").in(menuList));
		List<Map> opList = mongoTemplate.find(opQuery, Map.class, "grant_resource_uri");
		return opList;
	}
	
}