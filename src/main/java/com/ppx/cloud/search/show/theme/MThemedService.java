package com.ppx.cloud.search.show.theme;

import java.util.BitSet;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.search.show.brand.MBrand;
import com.ppx.cloud.search.util.BitSetUtils;


@Service
public class MThemedService extends MyDaoSupport {
	

	
	
	public List<MTheme> listTheme() {		
		
		int merchantId = MGrantContext.getWxUser().getMerchantId();
		
		String sql = "select THEME_ID TID, THEME_NAME TN, THEME_IMG_X X, THEME_IMG_Y Y from theme where MERCHANT_ID = ? and RECORD_STATUS = ? order by THEME_PRIO";
		
		List<MTheme> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(MTheme.class), merchantId, 1);	
		
		for (MTheme t : list) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_CAT, t.getTid() + "");
			if (bs != null) {
				t.setN(bs.cardinality());
			}
		}
		
		return list;
	}
	
	
	
	
	
}
