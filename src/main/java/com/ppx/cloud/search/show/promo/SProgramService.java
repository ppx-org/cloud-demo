package com.ppx.cloud.search.show.promo;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.util.DateUtils;
import com.ppx.cloud.search.common.Session;
import com.ppx.cloud.search.util.BitSetUtils;


@Service
public class SProgramService extends MyDaoSupport {
	

	public List<SProgram> listProgram(Session s) {
		
		String sql = "select PROG_ID GID, PROG_NAME GN, PROG_IMG_X X, PROG_IMG_Y  Y from program " +
				" where MERCHANT_ID = ? and PROG_STATUS = ? and curdate() between PROG_BEGIN and PROG_END order by PROG_PRIO";
		// 2:进行中
		List<SProgram> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(SProgram.class), s.getmId(), 2);
		// 加上本店的
		String normalPath = BitSetUtils.ORDER_NORMAL;
		BitSet storeBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), normalPath + "/" + BitSetUtils.PATH_STORE, s.getsId());
		
		List<SProgram> returnList = new ArrayList<SProgram>();
		for (SProgram t : list) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), normalPath + "/" + BitSetUtils.PATH_PROMO + "/" + DateUtils.today(), t.getGid() + "");
			bs.and(storeBs);
			if (bs != null && bs.cardinality() != 0) {
				t.setN(bs.cardinality());
				returnList.add(t);
			}
		}
		return returnList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
