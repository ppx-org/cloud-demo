package com.ppx.cloud.store.merchant.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.grant.service.MerchantService;

@Service
public class CategoryService extends MyDaoSupport {
	
	@Autowired
	private MerchantService merchantService;

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
		return returnList;
	}
	
	public List<Category> displaySubCat() {
		List<Category> catList = listCategory();
		List<Category> returnList = new ArrayList<Category>();
		for (Category category : catList) {
			if (category.getChildren() == null) continue;
			for (Category child : category.getChildren()) {
				Category c = new Category();
				c.setCatId(child.getCatId());
				c.setCatName(category.getCatName() + "-" + child.getCatName());
				returnList.add(c);
			}
		}
		return returnList;
	}
	
	public List<Category> displayAllCat() {
		List<Category> catList = listCategory();
		List<Category> returnList = new ArrayList<Category>();
		for (Category category : catList) {
			Category mainC = new Category();
			mainC.setCatId(category.getCatId());
			mainC.setCatName(category.getCatName());
			returnList.add(mainC);
			
			if (category.getChildren() != null) {
				for (Category child : category.getChildren()) {
					Category childC = new Category();
					childC.setCatId(child.getCatId());
					childC.setCatName(category.getCatName() + "-" + child.getCatName());
					returnList.add(childC);
				}
			}
			
		}
		return returnList;
	}
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Transactional
	public int insertCategory(Category bean) {
		merchantService.lockMerchant();
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		
		// 读取同级别最大catPrio+1
		String prioSql = "select ifnull(max(CAT_PRIO), 0) + 1 PRIO from category where MERCHANT_ID = ? and PARENT_ID = ? and RECORD_STATUS = ?";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, bean.getParentId(), 1);
				
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
		merchantService.lockMerchant();

		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String prioSql = "select min(CAT_PRIO) - 1 PRIO from category where MERCHANT_ID = ? and PARENT_ID"
				+ " = (select PARENT_ID from category where CAT_ID = ?)";
		int prio = getJdbcTemplate().queryForObject(prioSql, Integer.class, merchantId, id);
		
		String updateSql = "update category set CAT_PRIO = ? where CAT_ID = ?";
		
		return getJdbcTemplate().update(updateSql, prio, id);
	}
	
	@Transactional
	public int last(Integer id) {
		merchantService.lockMerchant();

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
		merchantService.lockMerchant();
		
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
		merchantService.lockMerchant();
		
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