package com.ppx.cloud.store.product.release;

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
import com.ppx.cloud.store.merchant.brand.BrandService;
import com.ppx.cloud.store.merchant.category.CategoryService;
import com.ppx.cloud.store.merchant.repo.RepositoryService;
import com.ppx.cloud.store.product.release.bean.Product;
import com.ppx.cloud.store.product.release.bean.ProductDetail;


@Controller	
public class ProductController {
	
	@Autowired
	private RepositoryService repoServ;
	
	@Autowired
	private CategoryService catServ;
	
	@Autowired
	private BrandService brandServ;
	
	
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
		PageList<Product> list = serv.listProduct(page, bean);
		return ControllerReturn.ok(list);
	}
	
	
	@GetMapping
	public ModelAndView addProduct() {
		ModelAndView mv = new ModelAndView();
		// <select
		mv.addObject("listRepo", repoServ.displayRepository());
		mv.addObject("listCat", catServ.displaySubCat());
		mv.addObject("listBrand", brandServ.displayBrand());
		
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> insertProduct(Product bean, ProductDetail detail, @RequestParam String prodImgSrc,
			@RequestParam Integer[] stockNum, @RequestParam Float[] price, String[] skuName, @RequestParam String[] skuImgSrc) {
		if (stockNum.length != price.length) {
			ControllerReturn.ok(-1);
		}
		
		int r = serv.insertProduct(bean, detail, prodImgSrc, stockNum, price, skuName, skuImgSrc);
		return ControllerReturn.ok(r);
	}
	

	@GetMapping
	public ModelAndView editProduct(@RequestParam Integer prodId) {
		ModelAndView mv = new ModelAndView();
		// <select
		mv.addObject("listRepo", repoServ.displayRepository());
		mv.addObject("listCat", catServ.displaySubCat());
		mv.addObject("listBrand", brandServ.displayBrand());
		
		Product prod = serv.getProduct(prodId);
		if (prod.getSkuDesc() == null) {
			// 页面根据skuDesc是不是为""来判断是否有多个sku
			prod.setSkuDesc("");
		}
		mv.addObject("prod", prod);
		
		mv.addObject("detail", serv.getProductDetail(prodId));
		mv.addObject("listSku", serv.listSku(prodId));
		
		return mv;
	}

	
	
	
	// >>>>>>>>>>>>>>>>>>>>action
	@PostMapping @ResponseBody
	public Map<String, Object> onShelves(@RequestParam Integer prodId) {
		int r = serv.onShelves(prodId);
		String statusDesc = Dict.getProductStatusDesc(r);
		return ControllerReturn.ok(r, statusDesc);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> offShelves(@RequestParam Integer prodId) {
		int r = serv.offShelves(prodId);
		String statusDesc = Dict.getProductStatusDesc(r);
		return ControllerReturn.ok(r, statusDesc);
	}
	
	
	
	
	
	
}

