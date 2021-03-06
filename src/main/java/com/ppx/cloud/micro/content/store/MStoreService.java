package com.ppx.cloud.micro.content.store;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;

@Service
public class MStoreService extends MyDaoSupport {

	public List<MStore> listStore() {
		int merchantId = MGrantContext.getWxUser().getMerId();
		
		String sql = "select s.STORE_ID ID, s.STORE_NAME NAME, s.STORE_NO NO, r.REPO_ADDRESS ADDR,"
				+ " s.STORE_LNG LNG, s.STORE_LAT LAT, s.STORE_PHONE PHONE, s.STORE_IMG IMG from store s left join repository r"
				+ " on s.STORE_ID = r.REPO_ID where s.MERCHANT_ID = ? and s.RECORD_STATUS = ? order by s.STORE_ID desc";
		List<MStore> list = getJdbcTemplate().query(sql,  BeanPropertyRowMapper.newInstance(MStore.class), merchantId, 1);
		
		return list;
	}
	
	
	public MStore getStore() {
		int storeId = MGrantContext.getWxUser().getStoreId();
		String sql = "select s.STORE_ID ID, s.STORE_NAME NAME, s.STORE_NO NO, r.REPO_ADDRESS ADDR,"
				+ " s.STORE_LNG LNG, s.STORE_LAT LAT, s.STORE_PHONE PHONE, s.STORE_IMG IMG from store s left join repository r"
				+ " on s.STORE_ID = r.REPO_ID where s.STORE_ID = ?";
		MStore store = getJdbcTemplate().queryForObject(sql,  BeanPropertyRowMapper.newInstance(MStore.class), storeId);
		return store;
	}
	
	
	public MStore getStore(Integer storeId) {
		String sql = "select s.STORE_ID ID, s.STORE_NAME NAME, s.STORE_NO NO, r.REPO_ADDRESS ADDR,"
				+ " s.STORE_LNG LNG, s.STORE_LAT LAT, s.STORE_PHONE PHONE, s.STORE_IMG IMG from store s left join repository r"
				+ " on s.STORE_ID = r.REPO_ID where s.STORE_ID = ?";
		MStore store = getJdbcTemplate().queryForObject(sql,  BeanPropertyRowMapper.newInstance(MStore.class), storeId);
		return store;
	}
	


	
	
	
	

	
	
	
	
	
	
	
	
}
