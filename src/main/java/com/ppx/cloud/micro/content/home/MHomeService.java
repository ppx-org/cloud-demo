package com.ppx.cloud.micro.content.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.storecommon.page.MQueryPage;
import com.ppx.cloud.storecommon.query.bean.MQueryProduct;
import com.ppx.cloud.storecommon.query.service.QueryCommonService;


@Service
public class MHomeService extends MyDaoSupport {
	
	@Autowired
	private QueryCommonService commonServ;
	
	public List<String> listSwiper() {
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		String sql = "select SWIPER_IMG from home_swiper where STORE_ID = ? order by SWIPER_PRIO";
		List<String> list = getJdbcTemplate().queryForList(sql, String.class, storeId);
		
		return list;
	}
	
	
	public List<MLevel> listLevel() {
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		String levelSql = "select LEVEL_ID, LEVEL_NAME, PROD_ID FIRST_PROD_ID from (select p.PROD_ID, p.LEVEL_ID, l.LEVEL_NAME from home_level_product p " + 
			"join home_level l on p.LEVEL_ID = l.LEVEL_ID and l.STORE_ID = ? " + 
			" order by l.LEVEL_PRIO, p.PROD_PRIO) t group by level_id";
		List<MLevel> list = getJdbcTemplate().query(levelSql, BeanPropertyRowMapper.newInstance(MLevel.class), storeId);
		
		return list;
	}
	
	
	
	public List<MQueryProduct> listLevelProd(MQueryPage page) {
		Integer storeId = MGrantContext.getWxUser().getStoreId();
		StringBuilder sb = new StringBuilder("select p.PROD_ID PID from home_level_product p join home_level l on p.LEVEL_ID = l.LEVEL_ID and l.STORE_ID = ? " + 
			"order by l.LEVEL_PRIO, p.PROD_PRIO");
		
		List<Integer> prodIdList = mQueryPage(Integer.class, page, sb, storeId);
		
		List<MQueryProduct> prodList = commonServ.listProduct(prodIdList, storeId);
		
		
		return prodList;
	}
	
	
	
	
	
	
}
