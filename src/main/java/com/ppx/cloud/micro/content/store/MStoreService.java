package com.ppx.cloud.micro.content.store;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.micro.common.MGrantContext;

@Service
public class MStoreService extends MyDaoSupport {

	public List<MStore> listStore(Page page, MStore bean) {
		int merchantId = MGrantContext.getWxUser().getMerchantId();
		
		String sql = "select s.STORE_ID, s.STORE_NAME, s.STORE_NO, r.REPO_ADDRESS STORE_ADDRESS,  "
				+ " r.STORE_LNG, r.STORE_LAT  from store s left join repository r "
				+ "on s.STORE_ID = r.REPO_ID where s.MERCHANT_ID = ? and s.RECORD_STATUS = ? order by s.STORE_ID desc";
		
		List<MStore> list = getJdbcTemplate().query(sql,  BeanPropertyRowMapper.newInstance(MStore.class), merchantId, 1);
		return list;
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// >>>>>>>>>>>>其它地方调用
	public List<MStore> listStore() {
		int merchantId = -1;//GrantContext.getLoginAccount().getMerchantId();
	
		String sql = "select STORE_ID, STORE_NAME from store where MERCHANT_ID = ? order by STORE_ID desc";
		
		List<MStore> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(MStore.class), merchantId);
		
		return list;
	}
	
	
	
}
