package com.ppx.cloud.store.content.level;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class LevelService extends MyDaoSupport {
	
	
	public List<Level> listLevel(Integer storeId) {
		String sql = "select * from home_level where STORE_ID = ? order by LEVEL_PRIO";
		
		List<Level> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Level.class), storeId);
		
		return list;
	}
	
	public int insertLevel(Level bean) {
		lockMerchant();
		
		String prioSql = "select ifnull(max(LEVEL_PRIO), 0) + 1 PRIO from home_level where STORE_ID = ?";
		int levelPrio = getJdbcTemplate().queryForObject(prioSql, Integer.class, bean.getStoreId());
		
		bean.setLevelPrio(levelPrio);
		return insert(bean);
	}
	
	public Level getLevel(Integer levelId) {
		Level bean = getJdbcTemplate().queryForObject("select * from home_level where LEVEL_ID = ?",
				BeanPropertyRowMapper.newInstance(Level.class), levelId);		
		return bean;
	}
	
	public int deleteLevel(Integer id) {
		return getJdbcTemplate().update("delete from home_level where LEVEL_ID = ?", id);
	}
	
	private void lockMerchant() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select 1 from merchant where MERCHANT_ID = ? for update";
		getJdbcTemplate().queryForMap(sql, merchantId);
	}
	
	@Transactional
	public int top(Integer storeId, Integer id) {
		lockMerchant();

		String prioSql = "select min(LEVEL_PRIO) - 1 PRIO from home_level where STORE_ID = ?";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, storeId);
		
		String updateSql = "update home_level set LEVEL_PRIO = ? where LEVEL_ID = ?";
		return getJdbcTemplate().update(updateSql, prio, id);
	}
	
	
	
	@Transactional
	public int up(Integer storeId, Integer id) {
		lockMerchant();
		
		String sql = "select * from home_level where STORE_ID = ? order by LEVEL_PRIO";
		List<Level> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Level.class), storeId);
		
		int prio = -1;
		int upId = -1;
		int upPrio = -1;
		for (Level b : list) {
			
			if (b.getLevelId() == id) {
				prio = b.getLevelPrio();
				break;
			}
			upId = b.getLevelId();
			upPrio = b.getLevelPrio();
		}
		
		String updateSql = "update home_level set LEVEL_PRIO = ? where LEVEL_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, upPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, upId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
	
	@Transactional
	public int down(Integer storeId, Integer id) {
		lockMerchant();
		
		String sql = "select * from home_level where STORE_ID = ? order by LEVEL_PRIO";
		List<Level> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Level.class), storeId);
		
		int prio = -1;
		int downId = -1;
		int downPrio = -1;
		boolean found = false;
		for (Level b : list) {
			downId = b.getLevelId();
			downPrio = b.getLevelPrio();
			if (found) {
				break;
			}
			if (b.getLevelId() == id) {
				prio = b.getLevelPrio();
				found = true;
			}
			
		}
		
		String updateSql = "update home_level set LEVEL_PRIO = ? where LEVEL_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, downPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, downId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// /////////////////
	public List<LevelProd> listLevelProd(Integer levelId) {
		String sql = "select p.PROD_ID, p.PROD_TITLE from product p join home_level_product l where p.PROD_ID = l.PROD_ID and l.LEVEL_ID = ? order by l.PROD_PRIO";
		
		List<LevelProd> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(LevelProd.class), levelId);
		
		return list;
	}
	
	
	public int insertLevelProd(LevelProd bean) {
		lockMerchant();
		
		// 同一store，不同重复prodId,
		String testSql = "select concat((select count(*) from product where PROD_ID = ?), " + 
			" (select count(*) from home_level_product where LEVEL_ID in (select LEVEL_ID from home_level where STORE_ID = (select STORE_ID from home_level where LEVEL_ID = ?)) and PROD_ID = ?)) TEST_INFO";
		String testInfo = getJdbcTemplate().queryForObject(testSql, String.class, bean.getProdId(), bean.getLevelId(), bean.getProdId());
		if (testInfo.startsWith("0")) {
			// 不存在prod
			return 0;
		}
		else if (testInfo.endsWith("1")) {
			// 已经存在
			return -1;
		}
		
		
		String prioSql = "select ifnull(max(PROD_PRIO), 0) + 1 PRIO from home_level_product where LEVEL_ID = ?";
		int prodPrio = getJdbcTemplate().queryForObject(prioSql, Integer.class, bean.getLevelId());
		
		String sql = "insert into home_level_product(LEVEL_ID, PROD_ID, PROD_PRIO) values(?, ?, ?)";
		
		int r = getJdbcTemplate().update(sql, bean.getLevelId(), bean.getProdId(), prodPrio);
		
		
		return r;
	}
	
	
	public int deleteLevelProd(Integer levelId, Integer id) {
		return getJdbcTemplate().update("delete from home_level_product where LEVEL_ID = ? and PROD_ID = ?", levelId, id);
	}
	
	
	@Transactional
	public int prodTop(Integer levelId, Integer id) {
		lockMerchant();

		String prioSql = "select min(PROD_PRIO) - 1 PRIO from home_level_product where LEVEL_ID = ?";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, levelId);
		
		String updateSql = "update home_level_product set PROD_PRIO = ? where LEVEL_ID = ? and PROD_ID = ?";
		return getJdbcTemplate().update(updateSql, prio, levelId, id);
	}
	
	
	
	@Transactional
	public int prodUp(Integer levelId, Integer id) {
		lockMerchant();
		
		String sql = "select * from home_level_product where LEVEL_ID = ? order by PROD_PRIO";
		List<LevelProd> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(LevelProd.class), levelId);
		
		int prio = -1;
		int upId = -1;
		int upPrio = -1;
		for (LevelProd b : list) {
			
			if (b.getProdId() == id) {
				prio = b.getProdPrio();
				break;
			}
			upId = b.getProdId();
			upPrio = b.getProdPrio();
		}
		
		String updateSql = "update home_level_product set PROD_PRIO = ? where LEVEL_ID = ? and PROD_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, upPrio, levelId, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, levelId, upId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
	
	@Transactional
	public int prodDown(Integer levelId, Integer id) {
		lockMerchant();
		
		String sql = "select * from home_level_product where LEVEL_ID = ? order by PROD_PRIO";
		List<LevelProd> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(LevelProd.class), levelId);
		
		int prio = -1;
		int downId = -1;
		int downPrio = -1;
		boolean found = false;
		for (LevelProd b : list) {
			downId = b.getProdId();
			downPrio = b.getProdPrio();
			if (found) {
				break;
			}
			if (b.getProdId() == id) {
				prio = b.getProdPrio();
				found = true;
			}
			
		}
		
		String updateSql = "update home_level_product set PROD_PRIO = ? where LEVEL_ID = ? and PROD_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, downPrio, levelId, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, levelId, downId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
