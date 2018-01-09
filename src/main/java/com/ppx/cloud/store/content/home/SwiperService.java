package com.ppx.cloud.store.content.home;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class SwiperService extends MyDaoSupport {
	
	
	public List<Swiper> listSwiper(Integer storeId) {
		String sql = "select * from home_swiper where STORE_ID = ? order by SWIPER_PRIO";
		
		List<Swiper> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Swiper.class), storeId);
		
		return list;
	}
	
	public int insertSwiper(Swiper bean) {
		String prioSql = "select ifnull(max(SWIPER_PRIO), 0) + 1 PRIO from home_swiper where STORE_ID = ?";
		int swiperPrio = getJdbcTemplate().queryForObject(prioSql, Integer.class, bean.getStoreId());
		
		bean.setSwiperPrio(swiperPrio);
		return insert(bean);
	}
	
	public Swiper getSwiper(Integer id) {
		Swiper bean = getJdbcTemplate().queryForObject("select * from home_swiper where SWIPER_ID = ?",
				BeanPropertyRowMapper.newInstance(Swiper.class), id);		
		return bean;
	}
	
	public int deleteSwiper(Integer id) {
		return getJdbcTemplate().update("delete from home_swiper where SWIPER_ID = ?", id);
	}
	
	private void lockMerchant() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select 1 from merchant where MERCHANT_ID = ? for update";
		getJdbcTemplate().queryForMap(sql, merchantId);
	}
	
	@Transactional
	public int top(Integer storeId, Integer id) {
		lockMerchant();

		String prioSql = "select min(SWIPER_PRIO) - 1 PRIO from home_swiper where STORE_ID = ?";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, storeId);
		
		String updateSql = "update home_swiper set SWIPER_PRIO = ? where SWIPER_ID = ?";
		return getJdbcTemplate().update(updateSql, prio, id);
	}
	
	
	
	@Transactional
	public int up(Integer storeId, Integer id) {
		lockMerchant();
		
		String sql = "select * from home_swiper where STORE_ID = ? order by SWIPER_PRIO";
		List<Swiper> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Swiper.class), storeId);
		
		int prio = -1;
		int upId = -1;
		int upPrio = -1;
		for (Swiper b : list) {
			
			if (b.getSwiperId() == id) {
				prio = b.getSwiperPrio();
				break;
			}
			upId = b.getSwiperId();
			upPrio = b.getSwiperPrio();
		}
		
		String updateSql = "update home_swiper set SWIPER_PRIO = ? where SWIPER_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, upPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, upId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
	
	@Transactional
	public int down(Integer storeId, Integer id) {
		lockMerchant();
		
		String sql = "select * from home_swiper where STORE_ID = ? order by SWIPER_PRIO";
		List<Swiper> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Swiper.class), storeId);
		
		int prio = -1;
		int downId = -1;
		int downPrio = -1;
		boolean found = false;
		for (Swiper b : list) {
			downId = b.getSwiperId();
			downPrio = b.getSwiperPrio();
			if (found) {
				break;
			}
			if (b.getSwiperId() == id) {
				prio = b.getSwiperPrio();
				found = true;
			}
			
		}
		
		String updateSql = "update home_swiper set SWIPER_PRIO = ? where SWIPER_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, downPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, downId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
}
