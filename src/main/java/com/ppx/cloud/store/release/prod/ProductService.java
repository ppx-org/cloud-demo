package com.ppx.cloud.store.release.prod;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;


@Service
public class ProductService extends MyDaoSupport {
	
	
	public PageList<Product> listTest(Page page, Product bean) {
		
		// 分页排序查询
		MyCriteria c = createCriteria("where").addAnd("PROD_TITLE like ?", "%", bean.getProdTitle(), "%");
		
		
		// 分开两条sql，mysql在count还会执行子查询
		StringBuilder cSql = new StringBuilder("select count(*) from product p").append(c);
		StringBuilder qSql = new StringBuilder("select p. from product p").append(c);
		
		List<Product> list = queryPage(Product.class, page, cSql, qSql, c.getParaList());
		return new PageList<Product>(list, page);
	}
	
	public int insertProduct(Product bean) {
		return insert(bean);
	}
	
	
}
