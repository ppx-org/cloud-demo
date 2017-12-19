package com.ppx.cloud.store.merchant.theme;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class ThemeService extends MyDaoSupport {
	
	
	public List<Theme> listTheme() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select * from theme where MERCHANT_ID = ? and RECORD_STATUS = ? order by THEME_PRIO";
		
		List<Theme> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Theme.class), merchantId, 1);
		
		return list;
	}
	
	public int insertTheme(Theme bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String prioSql = "select ifnull(max(THEME_PRIO), 0) + 1 PRIO from theme where MERCHANT_ID = ? and RECORD_STATUS = ?";
		int themePrio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, 1);
		
		bean.setThemePrio(themePrio);
		bean.setMerchantId(merchantId);
		return insert(bean);
	}
	
	public Theme getTheme(Integer id) {
		Theme bean = getJdbcTemplate().queryForObject("select * from theme where THEME_ID = ?",
				BeanPropertyRowMapper.newInstance(Theme.class), id);		
		return bean;
	}
	
	public int updateTheme(Theme bean) {
		return update(bean);
	}
	
	public int deleteTheme(Integer id) {
		return getJdbcTemplate().update("update theme set RECORD_STATUS = ? where THEME_ID = ?", 0, id);
	}
	
	private void lockMerchant() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select 1 from merchant where MERCHANT_ID = ? for update";
		getJdbcTemplate().queryForMap(sql, merchantId);
	}
	
	@Transactional
	public int top(Integer id) {
		lockMerchant();

		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String prioSql = "select min(THEME_PRIO) - 1 PRIO from theme where MERCHANT_ID = ? and RECORD_STATUS = ?";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, 1);
		
		String updateSql = "update theme set THEME_PRIO = ? where THEME_ID = ?";
		return getJdbcTemplate().update(updateSql, prio, id);
	}
	
	
	
	@Transactional
	public int up(Integer id) {
		lockMerchant();
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select THEME_ID, THEME_PRIO from theme where MERCHANT_ID = ? and RECORD_STATUS = ? order by THEME_PRIO";
		List<Theme> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Theme.class), merchantId, 1);
		
		int prio = -1;
		int upId = -1;
		int upPrio = -1;
		for (Theme b : list) {
			
			if (b.getThemeId() == id) {
				prio = b.getThemePrio();
				break;
			}
			upId = b.getThemeId();
			upPrio = b.getThemePrio();
		}
		
		String updateSql = "update theme set THEME_PRIO = ? where THEME_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, upPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, upId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
	
	@Transactional
	public int down(Integer id) {
		lockMerchant();
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select * from theme where MERCHANT_ID = ? and RECORD_STATUS = ? order by THEME_PRIO";
		List<Theme> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Theme.class), merchantId, 1);
		
		int prio = -1;
		int downId = -1;
		int downPrio = -1;
		boolean found = false;
		for (Theme b : list) {
			downId = b.getThemeId();
			downPrio = b.getThemePrio();
			if (found) {
				break;
			}
			if (b.getThemeId() == id) {
				prio = b.getThemePrio();
				found = true;
			}
			
		}
		
		String updateSql = "update theme set THEME_PRIO = ? where THEME_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, downPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, downId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
}
