package com.ppx.cloud.grant.common;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.MemoryUnit;




@Configuration
@EnableCaching
public class CacheConfig {
	
	public static final String LOCAL_CACHE = "localCache";
	
	public static final String URI_INDEX_CACHE = "URI_INDEX_CACHE";
	
	public static final String MER_BIT_SET_CACHE = "MER_BIT_SET_CACHE";
	
	public static final String MER_RES_CACHE = "MER_RES_CACHE";
	
	public static final String MENU_OP_CACHE = "MENU_OP_CACHE";
	
	

	@Bean(name=LOCAL_CACHE)
    public CacheManager cacheManager() {		
		
		net.sf.ehcache.config.Configuration conf = new net.sf.ehcache.config.Configuration();
		CacheConfiguration uriIndexCache = new CacheConfiguration().name(URI_INDEX_CACHE);
		uriIndexCache.maxBytesLocalHeap(100, MemoryUnit.MEGABYTES);
		conf.addCache(uriIndexCache);
		
		CacheConfiguration merBitSetCache = new CacheConfiguration().name(MER_BIT_SET_CACHE);
		merBitSetCache.maxBytesLocalHeap(100, MemoryUnit.MEGABYTES);
		conf.addCache(merBitSetCache);
		
		CacheConfiguration merResCache = new CacheConfiguration().name(MER_RES_CACHE);
		merResCache.maxBytesLocalHeap(100, MemoryUnit.MEGABYTES);
		conf.addCache(merResCache);
		
		CacheConfiguration menuOpCache = new CacheConfiguration().name(MENU_OP_CACHE);
		menuOpCache.maxBytesLocalHeap(100, MemoryUnit.MEGABYTES);
		conf.addCache(menuOpCache);
		
		return new EhCacheCacheManager(new net.sf.ehcache.CacheManager(conf));
    }	
	

	
	
}
