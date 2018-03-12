package com.ppx.cloud.search.show.brand;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.search.common.Session;
import com.ppx.cloud.search.util.BitSetUtils;


@Service
public class SBrandService extends MyDaoSupport {
	
	
	public List<SBrand> listBrand(Session s) {
		
		String sql = "select BRAND_ID BID, BRAND_NAME BN, BRAND_IMG_X X, BRAND_IMG_Y Y from brand where MERCHANT_ID = ? and RECORD_STATUS = ? order by BRAND_PRIO";
		
		List<SBrand> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(SBrand.class), s.getmId(), 1);	
		
		// 加上本店的
		String normalPath = BitSetUtils.ORDER_NORMAL;
		BitSet storeBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), normalPath + "/" + BitSetUtils.PATH_STORE, s.getsId());
		
		List<SBrand> returnList = new ArrayList<SBrand>();
		
		for (SBrand b : list) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), normalPath + "/" + BitSetUtils.PATH_CAT, b.getBid());
			bs.and(storeBs);
			if (bs != null && bs.cardinality() != 0) {
				b.setN(bs.cardinality());
				returnList.add(b);
			}
		}
		
		return returnList;
	}
	
	
	
	
	
}
