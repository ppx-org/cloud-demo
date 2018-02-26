package com.ppx.cloud.store.promo.statushistory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;


@Service
public class ProgramStatusService extends MyDaoSupport {
	
	public PageList<ProgramStatus> listProgramStatus(Page page, Integer progId) {
		
		List<Object> paraList = new ArrayList<Object>();
		paraList.add(progId);
	
		StringBuilder cSql = new StringBuilder("select count(*) from program_status_history h where h.PROG_ID = ?");
		StringBuilder qSql = new StringBuilder("select c.* from program_status_history c where c.PROG_ID = ? order by CREATED desc");
		
		List<ProgramStatus> list = queryPage(ProgramStatus.class, page, cSql, qSql, paraList);
		return new PageList<ProgramStatus>(list, page);
	}
	
	
	public String getProgMsg(Integer progId) {
		String sql = "select ifnull((select concat(PROG_NAME, ':', PROG_STATUS) from program where PROG_ID = ?), '')";
		String msg = getJdbcTemplate().queryForObject(sql, String.class, progId);
		return msg;
	}
	
	
	
}
