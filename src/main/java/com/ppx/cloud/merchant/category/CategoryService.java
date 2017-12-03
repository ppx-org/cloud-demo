package com.ppx.cloud.merchant.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.demo.module.TestBean;
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
		
		String sql = "select * from category where MERCHANT_ID = ? and RECORD_STATUS = ? order by CAT_PRIO, CAT_ID"; 
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

	
	
	
	public int insertCategory(Category bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		bean.setMerchantId(merchantId);
		bean.setCatPrio(0);
		return insert(bean);
	}
	
	public int updateCategory(Category bean) {
		return update(bean);
	}
	
	public int deleteCategory(Integer id) {
		return getJdbcTemplate().update("update category set RECORD_STATUS = ? where CAT_ID = ?", 0, id);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}