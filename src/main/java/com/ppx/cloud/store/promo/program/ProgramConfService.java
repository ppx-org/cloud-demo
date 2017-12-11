package com.ppx.cloud.store.promo.program;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.store.promo.program.bean.ProgramCategory;

@Service
public class ProgramConfService extends MyDaoSupport {
	
	
	public List<ProgramCategory> listProgramCat(Integer progId) {
		String sql = "select * from program_category where PROG_ID = ?";
		List<ProgramCategory> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(ProgramCategory.class), progId);
		return list;
	}
	
	public int insertProgramCategory(ProgramCategory bean) {
		
		
		
		return insert(bean);
	}
}
