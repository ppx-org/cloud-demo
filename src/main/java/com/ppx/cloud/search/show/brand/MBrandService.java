package com.ppx.cloud.search.show.brand;

import java.util.BitSet;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.search.show.category.MCategory;
import com.ppx.cloud.search.util.BitSetUtils;


@Service
public class MBrandService extends MyDaoSupport {
	

	
	
	public List<MBrand> listBrand() {		
		
		int merchantId = MGrantContext.getWxUser().getMerchantId();
		
		String sql = "select BRAND_ID BID, BRAND_NAME BN, BRAND_IMG_X X, BRAND_IMG_Y Y from brand where MERCHANT_ID = ? and RECORD_STATUS = ? order by BRAND_PRIO";
		
		List<MBrand> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(MBrand.class), merchantId, 1);	
		
		for (MBrand b : list) {
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_CAT, b.getBid() + "");
			if (bs != null) {
				b.setN(bs.cardinality());
			}
		}
		
		return list;
	}
	
	
	
	
	
}
