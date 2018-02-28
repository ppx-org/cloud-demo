package com.ppx.cloud.store.search.hotword;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class HotWordService extends MyDaoSupport {
	
	
	public List<HotWord> listHotWord(Integer storeId) {
		String sql = "select * from search_hot_word where STORE_ID = ? order by HOT_PRIO";
		List<HotWord> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(HotWord.class), storeId);
		return list;
	}
	
	public int insertHotWord(HotWord bean) {
		String prioSql = "select ifnull(max(HOT_PRIO), 0) + 1 PRIO from search_hot_word where STORE_ID = ?";
		int hotPrio = getJdbcTemplate().queryForObject(prioSql, Integer.class, bean.getStoreId());
		
		bean.setHotPrio(hotPrio);
		return insert(bean);
	}
	
	public HotWord getHotWord(Integer id) {
		HotWord bean = getJdbcTemplate().queryForObject("select * from search_hot_word where HOT_ID = ?",
				BeanPropertyRowMapper.newInstance(HotWord.class), id);		
		return bean;
	}
	
	public int updateHotWord(HotWord bean) {
		return update(bean);
	}
	
	public int deleteHotWord(Integer id) {
		return getJdbcTemplate().update("delete from search_hot_word where HOT_ID = ?", id);
	}
	
	private void lockMerchant() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select 1 from merchant where MERCHANT_ID = ? for update";
		getJdbcTemplate().queryForMap(sql, merchantId);
	}
	
	@Transactional
	public int top(Integer storeId, Integer id) {
		lockMerchant();

		String prioSql = "select min(HOT_PRIO) - 1 PRIO from search_hot_word where STORE_ID = ?";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, storeId);
		
		String updateSql = "update search_hot_word set HOT_PRIO = ? where HOT_ID = ?";
		return getJdbcTemplate().update(updateSql, prio, id);
	}
	
	
	@Transactional
	public int up(Integer storeId, Integer id) {
		lockMerchant();
		
		String sql = "select * from search_hot_word where STORE_ID = ? order by HOT_PRIO";
		List<HotWord> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(HotWord.class), storeId);
		
		int prio = -1;
		int upId = -1;
		int upPrio = -1;
		for (HotWord b : list) {
			if (b.getHotId() == id) {
				prio = b.getHotPrio();
				break;
			}
			upId = b.getHotId();
			upPrio = b.getHotPrio();
		}
		
		String updateSql = "update search_hot_word set HOT_PRIO = ? where HOT_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, upPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, upId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
	
	@Transactional
	public int down(Integer storeId, Integer id) {
		lockMerchant();
		
		String sql = "select * from search_hot_word where STORE_ID = ? order by HOT_PRIO";
		List<HotWord> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(HotWord.class), storeId);
		
		int prio = -1;
		int downId = -1;
		int downPrio = -1;
		boolean found = false;
		for (HotWord b : list) {
			downId = b.getHotId();
			downPrio = b.getHotPrio();
			if (found) {
				break;
			}
			if (b.getHotId() == id) {
				prio = b.getHotPrio();
				found = true;
			}
			
		}
		
		String updateSql = "update search_hot_word set HOT_PRIO = ? where HOT_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, downPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, downId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
}
