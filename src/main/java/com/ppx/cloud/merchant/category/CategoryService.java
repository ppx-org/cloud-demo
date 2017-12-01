package com.ppx.cloud.merchant.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.demo.module.TestBean;
import com.ppx.cloud.grant.common.GrantContext;

@Service
public class CategoryService extends MyDaoSupport {

	
	public List<Category> listCategory() {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
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
		
		
		

		return list;
	}

	
	
	
	public int insertCategory(Category bean) {
		return insert(bean);
	}
	
	public int updateCategory(Category bean) {
		return update(bean);
	}
	
	public int deleteCategory(Integer id) {
		// 存在商品不能删除
		return getJdbcTemplate().update("delete from category where CAT_ID = ?", id);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}