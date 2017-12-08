package com.ppx.cloud.store.promo.subject;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class SubjectService extends MyDaoSupport {
	
	
	public List<Subject> listSubject() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select * from subject where MERCHANT_ID = ? and RECORD_STATUS = ? order by SUBJECT_PRIO";
		
		List<Subject> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Subject.class), merchantId, 1);
		
		return list;
	}
	
	public int insertSubject(Subject bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String prioSql = "select ifnull(max(SUBJECT_PRIO), 0) + 1 PRIO from subject where MERCHANT_ID = ? and RECORD_STATUS = ?";
		int subjectPrio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, 1);
		
		bean.setSubjectPrio(subjectPrio);
		bean.setMerchantId(merchantId);
		return insert(bean);
	}
	
	public Subject getSubject(Integer id) {
		Subject bean = getJdbcTemplate().queryForObject("select * from subject where SUBJECT_ID = ?",
				BeanPropertyRowMapper.newInstance(Subject.class), id);		
		return bean;
	}
	
	public int updateSubject(Subject bean) {
		return update(bean);
	}
	
	public int deleteSubject(Integer id) {
		return getJdbcTemplate().update("update subject set RECORD_STATUS = ? where SUBJECT_ID = ?", 0, id);
	}
	
	private void lockMerchant() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select 1 from merchant where MERCHANT_ID = ? for update";
		getJdbcTemplate().queryForMap(sql, merchantId);
	}
	
	@Transactional
	public int top(Integer id) {
		lockMerchant();

		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String prioSql = "select min(SUBJECT_PRIO) - 1 PRIO from subject where MERCHANT_ID = ? and RECORD_STATUS = ?";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, 1);
		
		String updateSql = "update subject set SUBJECT_PRIO = ? where SUBJECT_ID = ?";
		return getJdbcTemplate().update(updateSql, prio, id);
	}
	
	
	
	@Transactional
	public int up(Integer id) {
		lockMerchant();
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select SUBJECT_ID, SUBJECT_PRIO from subject where MERCHANT_ID = ? and RECORD_STATUS = ? order by SUBJECT_PRIO";
		List<Subject> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Subject.class), merchantId, 1);
		
		int prio = -1;
		int upId = -1;
		int upPrio = -1;
		for (Subject b : list) {
			
			if (b.getSubjectId() == id) {
				prio = b.getSubjectPrio();
				break;
			}
			upId = b.getSubjectId();
			upPrio = b.getSubjectPrio();
		}
		
		String updateSql = "update subject set SUBJECT_PRIO = ? where SUBJECT_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, upPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, upId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
	
	@Transactional
	public int down(Integer id) {
		lockMerchant();
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select * from subject where MERCHANT_ID = ? and RECORD_STATUS = ? order by SUBJECT_PRIO";
		List<Subject> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Subject.class), merchantId, 1);
		
		int prio = -1;
		int downId = -1;
		int downPrio = -1;
		boolean found = false;
		for (Subject b : list) {
			downId = b.getSubjectId();
			downPrio = b.getSubjectPrio();
			if (found) {
				break;
			}
			if (b.getSubjectId() == id) {
				prio = b.getSubjectPrio();
				found = true;
			}
			
		}
		
		String updateSql = "update subject set SUBJECT_PRIO = ? where SUBJECT_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, downPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, downId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
}
