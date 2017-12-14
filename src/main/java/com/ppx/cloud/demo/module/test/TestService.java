package com.ppx.cloud.demo.module.test;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;

/**
 * 
 * @author dengxz
 * @date 2017年11月14日
 */
@Service
public class TestService extends MyDaoSupport {
	
	@Transactional
	public void test() {
		
		System.out.println("------------------begin");
		
		//getJdbcTemplate().queryfor
		
		
		System.out.println("------------------end");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 返回PageList包含List和Page，是为了在使用缓存时存储到Page数据
	 */
	//@Cacheable(value = "testCache", keyGenerator = RedisConfig.WISELY_KEY_GENERATOR)	
	public PageList<TestBean> listTest(Page page, TestBean bean) {
		// 默认排序，后面加上需要从页面传过来的排序的，防止SQL注入
		page.addDefaultOrderName("TEST_ID").addPermitOrderName("TEST_NAME");
		
		// 分页排序查询
		MyCriteria c = createCriteria("where").addAnd("TEST_NAME like ?", "%", bean.getTestName(), "%");
		
		// ---------
		//MyCriteria c = createCriteria("where").addAnd("TEST_NAME like '%" + bean.getTestName() + "%'");
		//MyCriteria c = createCriteria("where").addAnd("TEST_ID = " + bean.getTestName() + "");
		
		
		// 分开两条sql，mysql在count还会执行子查询
		StringBuilder cSql = new StringBuilder("select count(*) from test").append(c);
		StringBuilder qSql = new StringBuilder("select test.*, TEST_ID TEST_NUMBER, TEST_TIME TEST_DATE from test").append(c);
		
		List<TestBean> list = queryPage(TestBean.class, page, cSql, qSql, c.getParaList());
		return new PageList<TestBean>(list, page);
	}
	
	//@CacheEvict(value = "testCache", allEntries=true)
	public int insertTest(TestBean bean) {
		// 后面带不允许重名的字段（该字段需要建索引）
		return insert(bean, "TEST_NAME");
	}
	
	public TestBean getTest(Integer id) {
		TestBean bean = getJdbcTemplate().queryForObject("select * from test where TEST_ID = ?",
				BeanPropertyRowMapper.newInstance(TestBean.class), id);		
		return bean;
	}
	
	//@CacheEvict(value = "testCache", allEntries=true)
	public int updateTest(TestBean bean) {
		return update(bean, "TEST_NAME");
	}
	
	//@CacheEvict(value = "testCache", allEntries=true)
	public int deleteTest(Integer id) {
		return getJdbcTemplate().update("delete from test where TEST_ID = ?", id);
	}
}
