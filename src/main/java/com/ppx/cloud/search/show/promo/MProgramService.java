package com.ppx.cloud.search.show.promo;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.util.DateUtils;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.search.util.BitSetUtils;


@Service
public class MProgramService extends MyDaoSupport {
	

	public List<MProgram> listProgram() {		
		
		int merchantId = MGrantContext.getWxUser().getMerchantId();
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		String sql = "select PROG_ID GID, PROG_NAME GN, PROG_IMG_X X, PROG_IMG_Y  Y from program " +
				" where MERCHANT_ID = ? and curdate() between PROG_BEGIN and PROG_END order by PROG_PRIO";
		
		List<MProgram> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(MProgram.class), merchantId);
		// 加上本店的
		String normalPath = BitSetUtils.ORDER_NORMAL + BitSetUtils.getCurrentVersionName();
		BitSet storeBs = BitSetUtils.readBitSet(normalPath + "/" + BitSetUtils.PATH_STORE, storeId + "");
		
		List<MProgram> returnList = new ArrayList<MProgram>();
		for (MProgram t : list) {
			BitSet bs = BitSetUtils.readBitSet(normalPath + "/" + BitSetUtils.PATH_PROMO + "/" + DateUtils.today(), t.getGid() + "");

			bs.and(storeBs);
			if (bs != null && bs.cardinality() != 0) {
				t.setN(bs.cardinality());
				returnList.add(t);
			}
		}
		
		return returnList;
	}
	
	
	
	
	
}
