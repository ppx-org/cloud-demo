package com.ppx.cloud.store.release.prod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ppx.cloud.common.controller.ControllerReturn;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.store.common.dictionary.Dict;
import com.ppx.cloud.store.common.dictionary.DictBean;
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
		
		mv.addObject("productStatusList", Dict.listProductStatus());
		
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
		mv.addObject("listCat", catServ.displaySubCat());
		
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertProduct(Product bean, @RequestParam Integer[] stockNum, @RequestParam Float[] price,
			String[] skuName, String[] skuImgSrc) {
		System.out.println("..........getRepoId:" + bean.getRepoId());
		System.out.println("..........getCatId:" + bean.getCatId());
		System.out.println("..........getProdTitle:" + bean.getProdTitle());
		
		for (Integer n : stockNum) {
			System.out.println("nnnnnnn:" + n);
		}
		
		for (Float p : price) {
			System.out.println("pppppppp:" + p);
		}
		
		if (skuName != null) {
			for (String name : skuName) {
				System.out.println("skuName:" + name);
			}
		}
		
		if (skuImgSrc != null) {
			for (String src : skuImgSrc) {
				System.out.println("skuImgSrc:" + src);
			}
		}
	
		
		return ControllerReturn.ok(1);
		
		//int r = serv.insertProduct(bean);
		//return ControllerReturn.ok(r);
	}
	

	@GetMapping
	public ModelAndView editProduct(@RequestParam Integer prodId) {
		ModelAndView mv = new ModelAndView();
		// repo
		mv.addObject("listRepo", repoServ.listRepository());
		mv.addObject("listCat", catServ.displaySubCat());
		
		
		mv.addObject("prod", serv.getProduct(prodId));
		mv.addObject("listSku", serv.listSku(prodId));
		
		return mv;
	}

	
	
}

