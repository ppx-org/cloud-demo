package com.ppx.cloud.store.merchant.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyCriteria;
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
	
	public List<Category> listCategory(Integer status) {		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		MyCriteria c = createCriteria("where").addAnd("MERCHANT_ID = ?", merchantId)
				.addAnd("RECORD_STATUS = ?", status);
		
		String sql = "select CAT_ID, PARENT_ID, CAT_NAME, CAT_IMG_X, CAT_IMG_Y, RECORD_STATUS from category" + c + " order by CAT_PRIO"; 
		
		
		
		List<Category> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(Category.class), c.getParaList().toArray());	
		
		
		
		List<Category> returnList = new ArrayList<Category>();
		for (Category cat : list) {
			if (cat.getParentId() == -1) {
				List<Category> listChild = getChildren(list, cat.getCatId());
				cat.setChildren(listChild.size() == 0 ? null : listChild);
				returnList.add(cat);
			}
		}
		return returnList;
	}
	
	public List<Category> displaySubCat() {
		List<Category> catList = listCategory(1);
		List<Category> returnList = new ArrayList<Category>();
		for (Category category : catList) {
			if (category.getChildren() == null) continue;
			for (Category child : category.getChildren()) {
				Category c = new Category();
				c.setCatId(child.getCatId());
				c.setCatName(category.getCatName() + "-" + child.getCatName());
				c.setParentId(child.getParentId());
				returnList.add(c);
			}
		}
		return returnList;
	}
	
	public List<Category> displayAllCat() {
		List<Category> catList = listCategory(1);
		List<Category> returnList = new ArrayList<Category>();
		for (Category category : catList) {
			Category mainC = new Category();
			mainC.setCatId(category.getCatId());
			mainC.setCatName(category.getCatName());
			mainC.setParentId(category.getParentId());
			returnList.add(mainC);
			
			if (category.getChildren() != null) {
				for (Category child : category.getChildren()) {
					Category childC = new Category();
					childC.setCatId(child.getCatId());
					childC.setCatName(category.getCatName() + "-" + child.getCatName());
					childC.setParentId(child.getParentId());
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
		int r = getJdbcTemplate().update("update category set RECORD_STATUS = ? where CAT_ID = ?", 0, id);
		// 子类也删除
		getJdbcTemplate().update("update category set RECORD_STATUS = ? where PARENT_ID = ?", 0, id);
		
		return r;
	}
	
	@Transactional
	public int restoreCategory(Integer id) {
		int r = getJdbcTemplate().update("update category set RECORD_STATUS = ? where CAT_ID = ?", 1, id);
		// 子类也恢复
		getJdbcTemplate().update("update category set RECORD_STATUS = ? where PARENT_ID = ?", 1, id);
		
		return r;
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