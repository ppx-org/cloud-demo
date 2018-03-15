package com.ppx.cloud.micro.content.home;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.ppx.cloud.common.config.RedisConfig;
import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.demo.common.query.QueryProduct;


@Controller	
public class MHomeController {
	
	@Autowired
	private MHomeService serv;
	
	@Autowired
	private CacheManager cacheManager;
	
	
	public static void main(String[] args) {
		System.out.println("..............go:begin");
		
		try {
			// 非验证连接
			ServerAddress addr = new ServerAddress("139.199.152.193", Integer.parseInt("27017"));
			MongoClient client = new MongoClient(addr);
			SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(client, "redsea");
			MongoDbFactory dbFactory = simpleMongoDbFactory;

			// 去掉_class字段
			MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(simpleMongoDbFactory),
					new MongoMappingContext());
			converter.setTypeMapper(new DefaultMongoTypeMapper(null));
			MongoTemplate mongoTemplate = new MongoTemplate(simpleMongoDbFactory, converter);
			
			mongoTemplate.getCollectionNames();
		} catch (Exception e) {
			System.out.println("..............go:Exception");
			// 用户名 数据库 密码
			MongoCredential credential = MongoCredential.createScramSha1Credential("redsea", "admin", "redsea".toCharArray());
			ServerAddress addr = new ServerAddress("139.199.152.193", Integer.parseInt("27017"));
			MongoClient client = new MongoClient(addr, Arrays.asList(credential));
			SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(client, "redsea");
			MongoDbFactory dbFactory = simpleMongoDbFactory;

			// 去掉_class字段
			MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(simpleMongoDbFactory),
					new MongoMappingContext());
			converter.setTypeMapper(new DefaultMongoTypeMapper(null));
			MongoTemplate mongoTemplate = new MongoTemplate(simpleMongoDbFactory, converter);
			
		
			Set<String> s = mongoTemplate.getCollectionNames();
			
			System.out.println("..............go:002:" + s);
			//e.printStackTrace();
		}
		System.out.println("..............go:end");
	}

	@PostMapping @ResponseBody
	public Map<String, Object> go() {
		
		System.out.println("..............go:begin");
		
		try {
			// 用户名 数据库 密码
			MongoCredential credential = MongoCredential.createScramSha1Credential("redsea", "redsea", "redsea".toCharArray());

			// IP port
			ServerAddress addr = new ServerAddress("192.168.101.186", Integer.parseInt("27017"));
			MongoClient client = new MongoClient(addr);
			SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(client, "redsea");
			
			MongoDbFactory dbFactory = simpleMongoDbFactory;

			// 去掉_class字段
			MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(simpleMongoDbFactory),
					new MongoMappingContext());
			converter.setTypeMapper(new DefaultMongoTypeMapper(null));
			MongoTemplate mongoTemplate = new MongoTemplate(simpleMongoDbFactory, converter);
			
			
			
			
		} catch (Exception e) {
			System.out.println("..............go:Exception");
			e.printStackTrace();
		}
	
		
		
		
		
		
		
		System.out.println("..............go:end");
		
		return ControllerReturn.ok();
	}
	
	
	
	
	@Autowired
	private WebApplicationContext app;
	
	
	@PostMapping @ResponseBody
	public Map<String, Object> test() {
		
		String[] names = app.getBeanDefinitionNames();
		for (String s : names) {
			//System.out.println("beanName:" + s);
		}
		
		StringRedisTemplate stringRedisTemplate = app.getBean(StringRedisTemplate.class);
		
		DataType dataType = stringRedisTemplate.type("listHome::SimpleKey []");
		
		Long expire = stringRedisTemplate.getExpire("listHome::SimpleKey []");
		
		
		String out = stringRedisTemplate.opsForValue().get("listHome::SimpleKey []");
		
		
		
		
		//Set<String> set = stringRedisTemplate.keys("listHome*");
		
		
		System.out.println("vvvvvvvvvvvvvvvv.................:" + out);
		
		
		MongoClientOptions op = new MongoClientOptions.Builder().build();
		System.out.println("xxxxxxxxxxxxxout:" + op.getConnectionsPerHost());
		System.out.println("xxxxxxxxxxxxxout:" + op.getConnectTimeout());
		System.out.println("xxxxxxxxxxxxxout:" + op.getMaxWaitTime());
		
		// Collection<String> c = cacheManager.getCacheNames();
		// RedisCacheManager rCache = (RedisCacheManager) cacheManager;
		
		
		
		
//		Cache cache = cacheManager.getCache("listHome");
//		ObjectMapper om = new ObjectMapper();
//		try {
//			System.out.println("-------------------0:" + om.writer().writeValueAsString(cache.get("SimpleKey []").get()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		
		
		return ControllerReturn.ok();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Cacheable(value = "listHome")
	@PostMapping @ResponseBody
	public Map<String, Object> listHome() {
		List<MSwiper> swiperList = serv.listSwiper();
		List<MLevel> levelList = serv.listLevel();
		MPage page = new MPage();
		List<QueryProduct> prodList = serv.listLevelProd(page);
		
		Map<String, Object> returnMap = ControllerReturn.ok();
		returnMap.put("swiperList", swiperList);
		returnMap.put("levelList", levelList);
		returnMap.put("prodList", prodList);
		returnMap.put("page", page);
		
		return returnMap;
	}
	
	// 更多时调用
	@Cacheable(value = "levelProd", keyGenerator = RedisConfig.WISELY_KEY_GENERATOR)
	@PostMapping @ResponseBody
	public Map<String, Object> listLevelProd(@RequestBody MPage page) {
		List<QueryProduct> list = serv.listLevelProd(page);
		return ControllerReturn.ok(list);
	}
	
	
	
	
}

