package com.ppx.cloud.search.show.theme;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.search.util.BitSetUtils;


@Service
public class MThemedService extends MyDaoSupport {
	

	
	
	public List<MTheme> listTheme() {		
		
		int merchantId = MGrantContext.getWxUser().getMerchantId();
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		String sql = "select THEME_ID TID, THEME_NAME TN, THEME_IMG_X X, THEME_IMG_Y Y from theme where MERCHANT_ID = ? and RECORD_STATUS = ? order by THEME_PRIO";
		
		List<MTheme> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(MTheme.class), merchantId, 1);
		// 加上本店的
		String normalPath = BitSetUtils.ORDER_NORMAL + BitSetUtils.getCurrentVersionName();
		BitSet storeBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentVersionName(), normalPath + "/" + BitSetUtils.PATH_STORE, storeId + "");
		
		List<MTheme> returnList = new ArrayList<MTheme>();
		for (MTheme t : list) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.getCurrentVersionName(), normalPath + "/" + BitSetUtils.PATH_CAT, t.getTid() + "");
			bs.and(storeBs);
			if (bs != null && bs.cardinality() != 0) {
				t.setN(bs.cardinality());
				returnList.add(t);
			}
		}
		
		return returnList;
	}
	
	
	
	
	
}
