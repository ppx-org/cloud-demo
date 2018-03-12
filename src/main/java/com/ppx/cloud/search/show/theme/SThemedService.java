package com.ppx.cloud.search.show.theme;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.search.common.Session;
import com.ppx.cloud.search.util.BitSetUtils;


@Service
public class SThemedService extends MyDaoSupport {
	
	public List<STheme> listTheme(Session s) {		
		
		String sql = "select THEME_ID TID, THEME_NAME TN, THEME_IMG_X X, THEME_IMG_Y Y "
				+ "from theme where MERCHANT_ID = ? and RECORD_STATUS = ? order by THEME_PRIO";
		
		List<STheme> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(STheme.class), s.getmId(), 1);
		// 加上本店的
		String normalPath = BitSetUtils.ORDER_NORMAL;
		BitSet storeBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), normalPath + "/" + BitSetUtils.PATH_STORE, s.getsId());
		
		List<STheme> returnList = new ArrayList<STheme>();
		for (STheme t : list) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), normalPath + "/" + BitSetUtils.PATH_CAT, t.getTid());
			bs.and(storeBs);
			if (bs != null && bs.cardinality() != 0) {
				t.setN(bs.cardinality());
				returnList.add(t);
			}
		}
		
		return returnList;
	}
	
	
	
	
	
}
