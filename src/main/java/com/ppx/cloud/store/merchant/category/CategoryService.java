package com.ppx.cloud.store.merchant.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;

@Service
public class CategoryService extends MyDaoSupport {

	private List<Category> getChildren(List<Category> list, int parentId) {
		List<Category> returnList = new ArrayList<Category>();
		for (Category c : list) {
			if (c.getParentId() == parentId) {
				returnList.add(c);
			}
		}
		return returnList;
	}
	
	public List<Category> listCategory() {
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		String sql = "select CAT_ID, PARENT_ID, CAT_NAME from category where MERCHANT_ID = ? and RECORD_STATUS = ? order by CAT_PRIO"; 
		List<Category> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Category.class), merchantId, 1);	
		
		List<Category> returnList = new ArrayList<Category>();
		for (Category c : list) {
			if (c.getParentId() == -1) {
				List<Category> listChild = getChildren(list, c.getCatId());
				c.setChildren(listChild.size() == 0 ? null : listChild);
				returnList.add(c);
			}
		}
		
		
		
		
		/*
		List<Category> list = new ArrayList<Category>();
		
		Category c = new Category();
		c.setCatId(0);
		c.setParentId(-1);
		c.setCatName("\"AAA'");
		c.setCatPrio(0);
		
		
		
		List<Category> childList = new ArrayList<Category>();
		Category c1 = new Category();
		c1.setCatId(1);
		c1.setParentId(0);
		c1.setCatName("AAA_C1");
		c1.setCatPrio(1);
		childList.add(c1);
		
		Category c2 = new Category();
		c2.setCatId(2);
		c2.setParentId(0);
		c2.setCatName("AAA_C2");
		c2.setCatPrio(2);
		childList.add(c2);
		
		c.setChildren(childList);
		list.add(c);
		
		
		Category c3 = new Category();
		c3.setCatId(3);
		c3.setParentId(-1);
		c3.setCatName("BBB");
		c3.setCatPrio(2);
		list.add(c3);
		
		
		Category c4 = new Category();
		c4.setCatId(4);
		c4.setParentId(-1);
		c4.setCatName("BBB_2");
		c4.setCatPrio(2);
		list.add(c4);
		*/
		
		

		return returnList;
	}

	
	private void lockMerchant() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String sql = "select 1 from merchant where MERCHANT_ID = ? for update";
		getJdbcTemplate().queryForMap(sql, merchantId);
	}
	
	@Transactional
	public int insertCategory(Category bean) {
		lockMerchant();
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		
		// 读取同级别最大catPrio+1
		String prioSql = "select ifnull(max(CAT_PRIO), 0) + 1 PRIO from category where MERCHANT_ID = ? and PARENT_ID = ?";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, bean.getParentId());
				
		bean.setMerchantId(merchantId);
		bean.setCatPrio(0);
		bean.setCatPrio(prio);
		return insert(bean);
	}
	
	public int updateCategory(Category bean) {
		return update(bean);
	}
	
	@Transactional
	public int deleteCategory(Integer id) {
		
		return getJdbcTemplate().update("update category set RECORD_STATUS = ? where CAT_ID = ?", 0, id);
	}
	
	@Transactional
	public int top(Integer id) {
		lockMerchant();

		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String prioSql = "select min(CAT_PRIO) - 1 PRIO from category where MERCHANT_ID = ? and PARENT_ID"
				+ " = (select PARENT_ID from category where CAT_ID = ?)";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, id);
		
		String updateSql = "update category set CAT_PRIO = ? where CAT_ID = ?";
		
		return getJdbcTemplate().update(updateSql, prio, id);
	}
	
	@Transactional
	public int last(Integer id) {
		lockMerchant();

		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String prioSql = "select max(CAT_PRIO) + 1 PRIO from category where MERCHANT_ID = ? and PARENT_ID"
				+ " = (select PARENT_ID from category where CAT_ID = ?)";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, id);
		
		String updateSql = "update category set CAT_PRIO = ? where CAT_ID = ?";
		
		return getJdbcTemplate().update(updateSql, prio, id);
	}
	
	@Transactional
	public int up(Integer id) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		lockMerchant();
		
		String sql = "select CAT_ID, CAT_PRIO from category where MERCHANT_ID = ? and PARENT_ID = (select PARENT_ID from category where CAT_ID = ?)"
				+ " and RECORD_STATUS = ? order by CAT_PRIO";
		List<Category> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Category.class), merchantId, id, 1);
		
		int prio = -1;
		int upId = -1;
		int upPrio = -1;
		for (Category c : list) {
			
			if (c.getCatId() == id) {
				prio = c.getCatPrio();
				break;
			}
			upId = c.getCatId();
			upPrio = c.getCatPrio();
		}
		
		String updateSql = "update category set CAT_PRIO = ? where CAT_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, upPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, upId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
	
	
	@Transactional
	public int down(Integer id) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		lockMerchant();
		
		String sql = "select * from category where MERCHANT_ID = ? and PARENT_ID = (select PARENT_ID from category where CAT_ID = ?)"
				+ " and RECORD_STATUS = ? order by CAT_PRIO";
		List<Category> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Category.class), merchantId, id, 1);
		
		int prio = -1;
		int downId = -1;
		int downPrio = -1;
		boolean found = false;
		for (Category c : list) {
			downId = c.getCatId();
			downPrio = c.getCatPrio();
			if (found) {
				break;
			}
			if (c.getCatId() == id) {
				prio = c.getCatPrio();
				found = true;
			}
			
		}
		
		String updateSql = "update category set CAT_PRIO = ? where CAT_ID = ?";
		int r1 = getJdbcTemplate().update(updateSql, downPrio, id);
		int r2 = getJdbcTemplate().update(updateSql, prio, downId);
		
		return r1 == 1 && r2 == 1 ? 1 : 0;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}