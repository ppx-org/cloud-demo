package com.ppx.cloud.store.product.release;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;


@Service
public class ProductService extends MyDaoSupport {
	
	
	public PageList<Product> listProduct(Page page, Product bean) {
		
		MyCriteria c = createCriteria("where")
			.addAnd("PROD_TITLE like ?", "%", bean.getProdTitle(), "%")
			.addAnd("PROD_STATUS = ?", bean.getProdStatus());
		
		StringBuilder cSql = new StringBuilder("select count(*) from product p").append(c);
		StringBuilder qSql = new StringBuilder("select p.* from product p").append(c);
		
		List<Product> list = queryPage(Product.class, page, cSql, qSql, c.getParaList());
		return new PageList<Product>(list, page);
	}
	
	public int insertProduct(Product bean, ProductDetail detail, String prodImgSrc,
			Integer[] stockNum, Float[] price, String[] skuName, String[] skuImgSrc) {
		
		// 第一个SKU
		Sku firstSku = new Sku();
		firstSku.setProdId(-1);
		firstSku.setStockNum(stockNum[0]);
		firstSku.setPrice(price[0]);
		firstSku.setSkuPrio(0);
		if (skuName != null) firstSku.setSkuName(skuName[0]);
		if (skuImgSrc != null) firstSku.setSkuImgSrc(skuImgSrc[0]);
		int r = insert(firstSku);
		int prodId = super.getLastInsertId();
		// 更新SKU.PROD_ID以便跟SKU_ID一样
		getJdbcTemplate().update("update sku set PROD_ID = ? where SKU_ID = ?", prodId, prodId);
		
		// 其它SKU
		List<Object[]> skuArgList = new ArrayList<Object[]>();
		for (int i = 1; i < stockNum.length; i++) {
			Object[] arg = {prodId, stockNum[i], i, price[i], skuName[i], skuImgSrc[i]};
			skuArgList.add(arg);
		}
		if (stockNum.length > 1) {
			String insertSkuSql = "insert into sku(PROD_ID, STOCK_NUM, SKU_PRIO, PRICE, SKU_NAME, SKU_IMG_SRC) values(?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(insertSkuSql, skuArgList);
		}
		
		// product
		bean.setProdId(prodId);
		insert(bean);
		
		// detail
		detail.setProdId(prodId);
		insert(detail);
		
		// img
		String[] imgSrc = prodImgSrc.split(",");
		List<Object[]> imgArgList = new ArrayList<Object[]>();
		for (int i = 1; i < imgSrc.length; i++) {
			Object[] arg = {prodId, i, imgSrc[i]};
			imgArgList.add(arg);
		}
		String imgSql = "insert into product_img(PROD_ID, PROD_IMG_PRIO, PROD_IMG_SRC) values(?,?,?)";
		getJdbcTemplate().batchUpdate(imgSql, imgArgList);
		
		return r;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Product getProduct(int prodId) {
		String sql = "select * from product where PROD_ID = ?";
		Product prod = getJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(Product.class), prodId);
		return prod;
	}
	
	public ProductDetail getProductDetail(int prodId) {
		String sql = "select * from product_detail where PROD_ID = ?";
		ProductDetail detail = getJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(ProductDetail.class), prodId);
		return detail;
	}
	
	
	public List<Sku> listSku(int prodId) {
		String skuSql = "select * from sku where PROD_ID = ? order by SKU_PRIO";
		List<Sku> skuList = getJdbcTemplate().query(skuSql, BeanPropertyRowMapper.newInstance(Sku.class), prodId);
		return skuList;
	}
	
	
	
	
	
	
	
	
	
	// action >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	/*
	 * onShelves和offSheles都需要判断相关活动是否停止，因为需要生成价格索引和搜索索引
	 */
	
	// action 
	public int onShelves(Integer prodId) {
		
		
		String sql = "update product set PROD_STATUS = ? where PROD_ID = ?";
		getJdbcTemplate().update(sql, 2, prodId);
		return 2;
	}
	
	public int offShelves(Integer prodId) {
		String sql = "update product set PROD_STATUS = ? where PROD_ID = ?";
		getJdbcTemplate().update(sql, 3, prodId);
		return 3;
	}
	
	
}
