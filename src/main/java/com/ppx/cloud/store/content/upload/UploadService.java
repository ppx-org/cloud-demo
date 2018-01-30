package com.ppx.cloud.store.content.upload;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class UploadService extends MyDaoSupport {
	
	
	public List<Img> listImg() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();		
		String countSql = "select count(*) from img where MERCHANT_ID = ?";
		int c = getJdbcTemplate().queryForObject(countSql, Integer.class, merchantId);
		if (c == 0) {
			String insertSql = "insert into img(MERCHANT_ID, IMG_TYPE) values(?, ?)";
			List<Object[]> argList = new ArrayList<Object[]>();
			String[] type = {"cat", "brand", "theme", "promo"};
			for (String t : type) {
				Object[] obj = {merchantId, t};
				argList.add(obj);
			}
			getJdbcTemplate().batchUpdate(insertSql, argList);
		}
		String sql = "select * from img where MERCHANT_ID = ?";
		List<Img> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Img.class), merchantId);
		return list;
	}
	
}
