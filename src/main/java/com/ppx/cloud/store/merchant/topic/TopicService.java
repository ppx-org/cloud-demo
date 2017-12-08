package com.ppx.cloud.store.merchant.topic;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class TopicService extends MyDaoSupport {
	
	
	public List<Topic> listTopic() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select * from topic where MERCHANT_ID = ? and RECORD_STATUS = ? order by TOPIC_PRIO";
		
		List<Topic> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Topic.class), merchantId, 1);
		
		return list;
	}
	
	public int insertTopic(Topic bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String prioSql = "select ifnull(max(TOPIC_PRIO), 0) + 1 PRIO from topic where MERCHANT_ID = ? and RECORD_STATUS = ?";
		int topicPrio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, 1);
		
		bean.setTopicPrio(topicPrio);
		bean.setMerchantId(merchantId);
		return insert(bean);
	}
	
	public Topic getTopic(Integer id) {
		Topic bean = getJdbcTemplate().queryForObject("select * from topic where TOPIC_ID = ?",
				BeanPropertyRowMapper.newInstance(Topic.class), id);		
		return bean;
	}
	
	public int updateTopic(Topic bean) {
		return update(bean);
	}
	
	public int deleteTopic(Integer id) {
		return getJdbcTemplate().update("update topic set RECORD_STATUS = ? where TOPIC_ID = ?", 0, id);
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
		String prioSql = "select min(TOPIC_PRIO) - 1 PRIO from topic where MERCHANT_ID = ? and RECORD_STATUS = ?";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, 1);
		
		String updateSql = "update topic set TOPIC_PRIO = ? where TOPIC_ID = ?";
		return getJdbcTemplate().update(updateSql, prio, id);
	}
	
	
	
	@Transactional
	public int up(Integer id) {
		lockMerchant();
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select TOPIC_ID, TOPIC_PRIO from topic where MERCHANT_ID = ? and RECORD_STATUS = ? order by TOPIC_PRIO";
		List<Topic> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Topic.class), merchantId, 1);
		
		int prio = -1;
		int upId = -1;
		int upPrio = -1;
		for (Topic b : list) {
			
			if (b.getTopicId() == id) {
				prio = b.getTopicPrio();
				break;
			}
			upId = b.getTopicId();
			upPrio = b.getTopicPrio();
		}
		
		String updateSql = "update topic set TOPIC_PRIO = ? where TOPIC_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, upPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, upId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
	
	@Transactional
	public int down(Integer id) {
		lockMerchant();
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select * from topic where MERCHANT_ID = ? and RECORD_STATUS = ? order by TOPIC_PRIO";
		List<Topic> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Topic.class), merchantId, 1);
		
		int prio = -1;
		int downId = -1;
		int downPrio = -1;
		boolean found = false;
		for (Topic b : list) {
			downId = b.getTopicId();
			downPrio = b.getTopicPrio();
			if (found) {
				break;
			}
			if (b.getTopicId() == id) {
				prio = b.getTopicPrio();
				found = true;
			}
			
		}
		
		String updateSql = "update topic set TOPIC_PRIO = ? where TOPIC_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, downPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, downId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
}
