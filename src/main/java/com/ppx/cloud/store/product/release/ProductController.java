package com.ppx.cloud.store.product.release;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
import com.ppx.cloud.store.product.release.bean.ProductExport;


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
		
		mv.addObject("listRepo", repoServ.displayRepository());
		mv.addObject("listCat", catServ.displayAllCat());
		mv.addObject("listBrand", brandServ.displayBrand());
		
		mv.addObject("productStatusList", Dict.listProdStatus());
		
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
	public Map<String, Object> insertProduct(Product prod, ProductDetail detail, @RequestParam String prodImgSrc,
			@RequestParam Integer[] stockNum, @RequestParam Float[] price, String[] skuName, @RequestParam String[] skuImgSrc) {
		if (stockNum.length != price.length || stockNum.length != skuImgSrc.length) {
			ControllerReturn.ok(-1);
		}
		
		int r = serv.insertProduct(prod, detail, prodImgSrc, stockNum, price, skuName, skuImgSrc);
		return ControllerReturn.ok(r);
	}

	@GetMapping
	public ModelAndView editProduct(@RequestParam(required=true) Integer prodId, @RequestParam(defaultValue="0") Integer editType) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("editType", editType);
		// <select
		mv.addObject("listRepo", repoServ.displayRepository());
		mv.addObject("listCat", catServ.displaySubCat());
		mv.addObject("listBrand", brandServ.displayBrand());
		
		
		// stockChange
		mv.addObject("listChangeType", Dict.listChangeStockType());
		
		
		Product prod = serv.getProduct(prodId);
		if (prod.getSkuDesc() == null) {
			// 页面根据skuDesc是不是为""来判断是否有多个sku
			prod.setSkuDesc("");
		}
		mv.addObject("prod", prod);
		
		ProductDetail detail = serv.getProductDetail(prodId);
		mv.addObject("detail", detail);
		mv.addObject("listSku", serv.listSku(prodId));
		mv.addObject("listImg", serv.listProductImg(prodId));
		
		// 产品参数
		List<Map<String, String>> prodArsList = new ArrayList<Map<String, String>>();
		if (!StringUtils.isEmpty(detail.getProdArgs())) {
			String[] line = detail.getProdArgs().split("@");
			for (String str : line) {
				String name = str.split("\\|")[0];
				String value = "";
				if (str.split("\\|").length == 2) {
					value = str.split("\\|")[1];
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", name);
				map.put("value", value);
				prodArsList.add(map);
			}
		}
		int argsLen = prodArsList.size();
		for (int i = 0; i < 10 - argsLen; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", "");
			map.put("value", "");
			prodArsList.add(map);
		}
		mv.addObject("prodArsList", prodArsList);
		
		
		return mv;
	}

	@PostMapping @ResponseBody
	public Map<String, Object> updateProduct(Product prod, ProductDetail detail, @RequestParam String prodImgSrc,
			@RequestParam Integer[] skuId, @RequestParam Integer[] stockNum, @RequestParam Float[] price,
			String[] skuName, @RequestParam String[] skuImgSrc) {
		if (skuId.length != stockNum.length || skuId.length != price.length || skuId.length != skuImgSrc.length) {
			ControllerReturn.ok(-1);
		}
		
		int r = serv.updateProduct(prod, detail, prodImgSrc, skuId, stockNum, price, skuName, skuImgSrc);
		return ControllerReturn.ok(r);
	}
	
	
	
	// >>>>>>>>>>>>>>>>>>>>action
	@PostMapping @ResponseBody
	public Map<String, Object> onShelves(@RequestParam Integer prodId) {
		int r = serv.onShelves(prodId);
		String statusDesc = Dict.getProdStatusDesc(r);
		return ControllerReturn.ok(r, statusDesc);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> offShelves(@RequestParam Integer prodId) {
		int r = serv.offShelves(prodId);
		String statusDesc = Dict.getProdStatusDesc(r);
		return ControllerReturn.ok(r, statusDesc);
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> exportProduct(Product bean) {
		List<Product> list = serv.exportProduct(bean);
		return ControllerReturn.ok(list);
	}
	
	
	
	// >>>>>>>>>>>>>>>>>>>>>>>>export product detail
	
	@GetMapping
	public ModelAndView viewExportDetail() {
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("listRepo", repoServ.displayRepository());
		mv.addObject("listCat", catServ.displayAllCat());
		mv.addObject("listBrand", brandServ.displayBrand());
		
		mv.addObject("productStatusList", Dict.listProdStatus());
		
		
		return mv;
	}
	
	@PostMapping @ResponseBody
	public Map<String, Object> exportProductDetail(Product bean) {
		List<ProductExport> list = serv.exportProductDetail(bean);
		return ControllerReturn.ok(list);
	}
}

