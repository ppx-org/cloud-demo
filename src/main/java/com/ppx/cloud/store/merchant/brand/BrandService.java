package com.ppx.cloud.store.merchant.brand;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class BrandService extends MyDaoSupport {
	
	
	public List<Brand> listBrand(Integer status) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		MyCriteria c = createCriteria("where").addAnd("MERCHANT_ID = ?", merchantId)
				.addAnd("RECORD_STATUS = ?", status);
		
		String sql = "select * from brand" + c + " order by BRAND_PRIO";
		
		List<Brand> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Brand.class), c.getParaList().toArray());
		
		return list;
	}
	
	public int insertBrand(Brand bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String prioSql = "select ifnull(max(BRAND_PRIO), 0) + 1 PRIO from brand where MERCHANT_ID = ? and RECORD_STATUS = ?";
		int brandPrio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, 1);
		
		bean.setBrandPrio(brandPrio);
		bean.setMerchantId(merchantId);
		return insert(bean);
	}
	
	public Brand getBrand(Integer id) {
		Brand bean = getJdbcTemplate().queryForObject("select * from brand where BRAND_ID = ?",
				BeanPropertyRowMapper.newInstance(Brand.class), id);		
		return bean;
	}
	
	public int updateBrand(Brand bean) {
		return update(bean);
	}
	
	public int deleteBrand(Integer id) {
		return getJdbcTemplate().update("update brand set RECORD_STATUS = ? where BRAND_ID = ?", 0, id);
	}
	
	public int restoreBrand(Integer id) {
		return getJdbcTemplate().update("update brand set RECORD_STATUS = ? where BRAND_ID = ?", 1, id);
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
		String prioSql = "select min(BRAND_PRIO) - 1 PRIO from brand where MERCHANT_ID = ? and RECORD_STATUS = ?";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, 1);
		
		String updateSql = "update brand set BRAND_PRIO = ? where BRAND_ID = ?";
		return getJdbcTemplate().update(updateSql, prio, id);
	}
	
	
	
	@Transactional
	public int up(Integer id) {
		lockMerchant();
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select BRAND_ID, BRAND_PRIO from brand where MERCHANT_ID = ? and RECORD_STATUS = ? order by BRAND_PRIO";
		List<Brand> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Brand.class), merchantId, 1);
		
		int prio = -1;
		int upId = -1;
		int upPrio = -1;
		for (Brand b : list) {
			
			if (b.getBrandId() == id) {
				prio = b.getBrandPrio();
				break;
			}
			upId = b.getBrandId();
			upPrio = b.getBrandPrio();
		}
		
		String updateSql = "update brand set BRAND_PRIO = ? where BRAND_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, upPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, upId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
	
	@Transactional
	public int down(Integer id) {
		lockMerchant();
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select * from brand where MERCHANT_ID = ? and RECORD_STATUS = ? order by BRAND_PRIO";
		List<Brand> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Brand.class), merchantId, 1);
		
		int prio = -1;
		int downId = -1;
		int downPrio = -1;
		boolean found = false;
		for (Brand b : list) {
			downId = b.getBrandId();
			downPrio = b.getBrandPrio();
			if (found) {
				break;
			}
			if (b.getBrandId() == id) {
				prio = b.getBrandPrio();
				found = true;
			}
			
		}
		
		String updateSql = "update brand set BRAND_PRIO = ? where BRAND_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, downPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, downId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
}
