package com.ppx.cloud.store.release.prod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.store.merchant.category.Category;
import com.ppx.cloud.store.merchant.category.CategoryService;
import com.ppx.cloud.store.merchant.repo.RepositoryService;


@Controller	
public class ProductController {
	
	@Autowired
	private RepositoryService repoServ;
	
	@Autowired
	private CategoryService catServ;
	
	
	@Autowired
	private ProductService serv;
	
	@GetMapping
	public ModelAndView listProduct() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("listJson", listJson(new Page(), new Product()));
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> listJson(Page page, Product bean) {
		PageList<Product> list = serv.listTest(page, bean);
		return ControllerReturn.ok(list);
	}
	
	
	
	@GetMapping
	public ModelAndView addProduct() {
		ModelAndView mv = new ModelAndView();
		// repo
		mv.addObject("listRepo", repoServ.listRepository());
		mv.addObject("listCat", displaySubCat(catServ.listCategory()));
		
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertProduct(Product bean) {
		int r = serv.insertProduct(bean);
		return ControllerReturn.ok(r);
	}
	
	
	
	
	private List<Category> displaySubCat(List<Category> list) {
		List<Category> returnList = new ArrayList<Category>();
		for (Category category : list) {
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
	
}

