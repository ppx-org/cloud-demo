package com.ppx.cloud.store.merchant.theme;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.grant.service.MerchantService;


@Service
public class ThemeService extends MyDaoSupport {
	
	@Autowired
	private MerchantService merchantService;
	
	public List<Theme> listTheme(Integer status) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		MyCriteria c = createCriteria("where").addAnd("MERCHANT_ID = ?", merchantId)
				.addAnd("RECORD_STATUS = ?", status);
		
		String sql = "select * from theme" + c + " order by THEME_PRIO";
		
		List<Theme> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Theme.class), c.getParaList().toArray());
		
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
	
	public int restoreTheme(Integer id) {
		
		
		return getJdbcTemplate().update("update theme set RECORD_STATUS = ? where THEME_ID = ?", 1, id);
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
	
	
	public PageList<ThemeProduct> listThemeProduct(Page page, ThemeProduct bean) {
	
		MyCriteria c = createCriteria("and").addAnd("m.PROD_ID = ?", bean.getProdId());
		
		StringBuilder cSql = new StringBuilder("select count(*) from theme_map_prod m where m.THEME_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select m.THEME_ID, m.PROD_ID, d.PROD_TITLE from theme_map_prod m left join product d on m.PROD_ID = d.PROD_ID where THEME_ID = ?").append(c);		
		c.addPrePara(bean.getThemeId());
		
		List<ThemeProduct> list = queryPage(ThemeProduct.class, page, cSql, qSql, c.getParaList());
		return new PageList<ThemeProduct>(list, page);
	}
	
	
	@Transactional
	public String insertThemeProduct(Integer themeId, String prodIdStr) {
		// 加锁
		int merchantId = merchantService.lockMerchant();
		
		String[] prodId = prodIdStr.split(",");
		
		// 清除
		String deleteSql = "delete from import_data where MERCHANT_ID = ?";
		getJdbcTemplate().update(deleteSql, merchantId);
		
		// result bit,1:产品ID不存在product,2:产品ID已经存在theme_map_prod
		String importSql = "insert into import_data(MERCHANT_ID, ROWNUM, INT_1, RESULT) " +
			"select " + merchantId + ", ?, ?, if ((select count(*) from product where PROD_ID = ? and REPO_ID in (select REPO_ID from repository where MERCHANT_ID = " + merchantId + ")) = 1,  0, 1) " +
			"^ if ((select count(*) from theme_map_prod where PROD_ID = ? and THEME_ID = " + themeId + ") != 1, 0, 2) r";
		List<Object[]> argList = new ArrayList<Object[]>();
		for (int i = 0; i < prodId.length; i++) {
			Object[] arg = {i+1, prodId[i], prodId[i], prodId[i]};
			argList.add(arg);
		}
		getJdbcTemplate().batchUpdate(importSql, argList);
		
		
		// 找出不符合条件记录
		String errorSql = "select group_concat(concat(ROWNUM, ':', RESULT)) msg from import_data where MERCHANT_ID = ? and result != ?";
		String msg = getJdbcTemplate().queryForObject(errorSql, String.class, merchantId, 0);
		if (!StringUtils.isEmpty(msg)) {
			return msg;
		}
		else {
			String insertSql = "insert into theme_map_prod(THEME_ID, PROD_ID) select ?, INT_1 from import_data where MERCHANT_ID = ?";
			int r = getJdbcTemplate().update(insertSql, themeId, merchantId);
			return "ok:" + r;
		}
		
	}

	
	public int deleteThemeProduct(Integer themeId, Integer prodId) {
		return getJdbcTemplate().update("delete from theme_map_prod where THEME_ID = ? and PROD_ID = ?", themeId, prodId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
