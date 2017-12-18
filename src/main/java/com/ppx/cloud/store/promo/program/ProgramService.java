package com.ppx.cloud.store.promo.program;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;

@Service
public class ProgramService extends MyDaoSupport {
	
	
	public PageList<Program> listProgram(Page page, Program bean) {
		
		// 分页排序查询
		MyCriteria c = createCriteria("where")
				.addAnd("POLICY_TYPE like ?", bean.getPolicyType())
				.addAnd("PROG_NAME like ?", "%", bean.getProgName(), "%");
		
		StringBuilder cSql = new StringBuilder("select count(*) from program where RECORD_STATUS = ?").append(c);
		StringBuilder qSql = new StringBuilder("select * from program where RECORD_STATUS = ?").append(c);
		c.addPrePara(1);
		
		List<Program> list = queryPage(Program.class, page, cSql, qSql, c.getParaList());
		return new PageList<Program>(list, page);
	}
	
	public int insertProgram(Program bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		bean.setMerchantId(merchantId);
		return insert(bean);
	}
	
	public Program getProgram(Integer id) {
		Program bean = getJdbcTemplate().queryForObject("select * from program where PROG_ID = ?",
				BeanPropertyRowMapper.newInstance(Program.class), id);		
		return bean;
	}
	
	public int updateProgram(Program bean) {
		return update(bean);
	}
	
	public int deleteProgram(Integer id) {
		return getJdbcTemplate().update("update program set RECORD_STATUS = ? where PROG_ID = ?", 0, id);
	}
}
