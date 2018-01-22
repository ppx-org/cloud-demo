package com.ppx.cloud.search.show.category;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.search.util.BitSetUtils;


@Service
public class MCategoryService extends MyDaoSupport {
	

	
	
	/**
	 * 不同store可能有不同的category
	 * 每项的数量，图片，从搜索引擎中的读取
	 * 
	 * @return
	 */
	public List<MCategory> listCategory() {		
		
		int merchantId = MGrantContext.getWxUser().getMerchantId();
		
		String sql = "select CAT_ID CID, PARENT_ID PID, CAT_NAME CN, CAT_IMG_X X, CAT_IMG_Y Y from category"
				+ " where MERCHANT_ID = ? and RECORD_STATUS = ? order by CAT_PRIO";
		
		List<MCategory> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(MCategory.class), merchantId, 1);	
		
		List<MCategory> returnList = new ArrayList<MCategory>();
		for (MCategory c : list) {
			if (c.getPid() == -1) {
				List<MCategory> listChild = getChildren(list, c.getCid());
				c.setChildren(listChild.size() == 0 ? null : listChild);
				returnList.add(c);
			}
			
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_CAT, c.getCid() + "");
			if (bs != null) {
				c.setN(bs.cardinality());
			}
		}
		return returnList;
	}
	
	private List<MCategory> getChildren(List<MCategory> list, int pid) {
		List<MCategory> returnList = new ArrayList<MCategory>();
		for (MCategory c : list) {
			if (c.getPid() == pid) {
				returnList.add(c);
			}
			
			BitSet bs = BitSetUtils.readBitSet(BitSetUtils.PATH_CAT, c.getCid() + "");
			if (bs != null) {
				c.setN(bs.cardinality());
			}
		}
		return returnList;
	}
	
	
	
	
}
