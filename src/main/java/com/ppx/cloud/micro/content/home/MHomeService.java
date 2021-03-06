package com.ppx.cloud.micro.content.home;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.MPage;
import com.ppx.cloud.demo.common.query.QueryCommonService;
import com.ppx.cloud.demo.common.query.QueryProduct;
import com.ppx.cloud.micro.common.MGrantContext;


@Service
public class MHomeService extends MyDaoSupport {
	
	@Autowired
	private QueryCommonService commonServ;

	
	
	public List<MSwiper> listSwiper() {
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		String sql = "select SWIPER_IMG SRC, SWIPER_URL URL from home_swiper where STORE_ID = ? order by SWIPER_PRIO";
		List<MSwiper> list = getJdbcTemplate().query(sql,  BeanPropertyRowMapper.newInstance(MSwiper.class), storeId);
		
		return list;
	}
	
	public List<MLevel> listLevel() {
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		String levelSql = "select LEVEL_ID, LEVEL_NAME, PROD_ID FIRST_PROD_ID from (select p.PROD_ID, p.LEVEL_ID, l.LEVEL_NAME from home_level_product p" + 
			" join home_level l on p.LEVEL_ID = l.LEVEL_ID and l.STORE_ID = ? " + 
			" order by l.LEVEL_PRIO, p.PROD_PRIO) t group by LEVEL_ID";
		List<MLevel> list = getJdbcTemplate().query(levelSql, BeanPropertyRowMapper.newInstance(MLevel.class), storeId);
		
		return list;
	}
	
	public List<QueryProduct> listLevelProd(MPage page) {
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		StringBuilder sql = new StringBuilder("select p.PROD_ID PID from home_level_product p join home_level l on p.LEVEL_ID = l.LEVEL_ID and l.STORE_ID = ?" + 
			" order by l.LEVEL_PRIO, p.PROD_PRIO");
		List<Integer> prodIdList = mQueryPage(Integer.class, page, sql, storeId);
		List<QueryProduct> prodList = commonServ.listProduct(prodIdList, storeId);
		
		return prodList;
	}
	
	
}
