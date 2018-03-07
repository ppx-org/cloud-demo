package com.ppx.cloud.search.show.brand;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.search.util.BitSetUtils;


@Service
public class MBrandService extends MyDaoSupport {
	
	
	public List<MBrand> listBrand() {		
		
		int merchantId = MGrantContext.getWxUser().getMerchantId();
		int storeId = MGrantContext.getWxUser().getStoreId();
		
		String sql = "select BRAND_ID BID, BRAND_NAME BN, BRAND_IMG_X X, BRAND_IMG_Y Y from brand where MERCHANT_ID = ? and RECORD_STATUS = ? order by BRAND_PRIO";
		
		List<MBrand> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(MBrand.class), merchantId, 1);	
		
		// 加上本店的
		String normalPath = BitSetUtils.ORDER_NORMAL + BitSetUtils.getCurrentVersionName();
		BitSet storeBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentVersionName(), normalPath + "/" + BitSetUtils.PATH_STORE, storeId + "");
		
		List<MBrand> returnList = new ArrayList<MBrand>();
		
		for (MBrand b : list) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.getCurrentVersionName(), normalPath + "/" + BitSetUtils.PATH_CAT, b.getBid() + "");
			bs.and(storeBs);
			if (bs != null && bs.cardinality() != 0) {
				b.setN(bs.cardinality());
				returnList.add(b);
			}
		}
		
		return returnList;
	}
	
	
	
	
	
}
