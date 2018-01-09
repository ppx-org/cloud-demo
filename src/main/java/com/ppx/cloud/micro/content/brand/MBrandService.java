package com.ppx.cloud.micro.content.brand;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;


@Service
public class MBrandService extends MyDaoSupport {
	

	
	
	public List<MBrand> listBrand() {		
		
		int merchantId = MGrantContext.getWxUser().getMerchantId();
		
		String sql = "select BRAND_ID BID, BRAND_NAME BN from brand where MERCHANT_ID = ? and RECORD_STATUS = ? order by BRAND_PRIO";
		
		List<MBrand> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(MBrand.class), merchantId, 1);	
		
		
		return list;
	}
	
	
	
	
	
}
