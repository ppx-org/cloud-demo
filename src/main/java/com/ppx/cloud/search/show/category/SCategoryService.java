package com.ppx.cloud.search.show.category;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.search.common.Session;
import com.ppx.cloud.search.util.BitSetUtils;


@Service
public class SCategoryService extends MyDaoSupport {
	

	
	
	/**
	 * 不同store可能有不同的category
	 * 每项的数量，图片，从搜索引擎中的读取
	 * 
	 * @return
	 */
	public List<SCategory> listCategory(Session s) {
		
		String sql = "select CAT_ID CID, PARENT_ID PID, CAT_NAME CN, CAT_IMG_X X, CAT_IMG_Y Y from category"
				+ " where MERCHANT_ID = ? and RECORD_STATUS = ? order by CAT_PRIO";
		
		List<SCategory> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(SCategory.class), s.getmId(), 1);	
		
		// 加上本店的
		String normalPath = BitSetUtils.ORDER_NORMAL;
		BitSet storeBs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), normalPath + "/" + BitSetUtils.PATH_STORE, s.getsId());
		
		List<SCategory> returnList = new ArrayList<SCategory>();
		for (SCategory c : list) {
			if (c.getPid() == -1) {
				BitSet bs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), normalPath + "/" + BitSetUtils.PATH_CAT, c.getCid());
				bs.and(storeBs);
				if (bs != null && bs.cardinality() != 0) {
					c.setN(bs.cardinality());
					
					List<SCategory> listChild = getChildren(list, c.getCid(), storeBs);
					c.setChildren(listChild.size() == 0 ? null : listChild);
					returnList.add(c);
				}
			}
		}
		return returnList;
	}
	
	private List<SCategory> getChildren(List<SCategory> list, int pid, BitSet storeBs) {
		List<SCategory> returnList = new ArrayList<SCategory>();
		for (SCategory c : list) {
			if (c.getPid() == pid) {
				String normalPath = BitSetUtils.ORDER_NORMAL;
				BitSet bs = BitSetUtils.readBitSet(BitSetUtils.getCurrentV(), normalPath + "/" + BitSetUtils.PATH_CAT, c.getCid());
				bs.and(storeBs);
				if (bs != null && bs.cardinality() != 0) {
					c.setN(bs.cardinality());
					returnList.add(c);
				}
			}
		}
		return returnList;
	}
	
	
	
	
}
