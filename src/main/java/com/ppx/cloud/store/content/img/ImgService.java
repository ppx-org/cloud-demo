package com.ppx.cloud.store.content.img;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class ImgService extends MyDaoSupport {
	
	
	public List<Img> listImg() {
		//getJdbcTemplate().update("delete from img where MERCHANT_ID = -1");
		
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();		
		String countSql = "select count(*) from home_img where MERCHANT_ID = ?";
		int c = getJdbcTemplate().queryForObject(countSql, Integer.class, merchantId);
		if (c == 0) {
			String insertSql = "insert into home_img(MERCHANT_ID, IMG_TYPE, IMG_PRIO) values(?, ?, ?)";
			List<Object[]> argList = new ArrayList<Object[]>();
			String[] type = {"cat", "brand", "theme", "promo"};
			for (int i = 0; i < type.length; i++) {
				Object[] obj = {merchantId, type[i], i};
				argList.add(obj);
			}
			getJdbcTemplate().batchUpdate(insertSql, argList);
		}
		String sql = "select * from home_img where MERCHANT_ID = ? order by IMG_PRIO";
		List<Img> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Img.class), merchantId);
		return list;
	}
	
	public int updateImgSrc(String type, String src) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "update home_img set IMG_SRC = ? where MERCHANT_ID = ? and IMG_TYPE = ?";
		int r = getJdbcTemplate().update(sql, src, merchantId, type);
		return 1;
	}
	
	
	public String getImgSrc(String type) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select IMG_SRC from home_img where MERCHANT_ID = ? and IMG_TYPE = ?";
		return getJdbcTemplate().queryForObject(sql, String.class, merchantId, type);
	}
	
}
