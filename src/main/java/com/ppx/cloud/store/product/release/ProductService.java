package com.ppx.cloud.store.product.release;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.store.product.release.bean.Product;
import com.ppx.cloud.store.product.release.bean.ProductDetail;
import com.ppx.cloud.store.product.release.bean.ProductImg;
import com.ppx.cloud.store.product.release.bean.Sku;


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
	
	@Transactional
	public int insertProduct(Product prod, ProductDetail detail, String prodImgSrc,
			Integer[] stockNum, Float[] price, String[] skuName, String[] skuImgSrc) {
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		// 第一个SKU
		Sku firstSku = new Sku();
		firstSku.setProdId(-1);
		firstSku.setStockNum(stockNum[0]);
		firstSku.setPrice(price[0]);
		firstSku.setSkuPrio(0);
		if (skuName != null && skuName.length > 0) firstSku.setSkuName(skuName[0]);
		if (skuImgSrc != null && skuImgSrc.length > 0) firstSku.setSkuImgSrc(skuImgSrc[0]);
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
		if (!skuArgList.isEmpty()) {
			String insertSkuSql = "insert into sku(PROD_ID, STOCK_NUM, SKU_PRIO, PRICE, SKU_NAME, SKU_IMG_SRC) values(?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(insertSkuSql, skuArgList);
		}
		
		// product
		prod.setProdId(prodId);
		prod.setMerchantId(merchantId);
		insert(prod);
		
		// detail
		detail.setProdId(prodId);
		insert(detail);
		
		// img
		String[] imgSrc = prodImgSrc.split(",");
		List<Object[]> imgArgList = new ArrayList<Object[]>();
		for (int i = 0; i < imgSrc.length; i++) {
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
	
	public List<ProductImg> listProductImg(int prodId) {
		String imgSql = "select * from product_img where PROD_ID = ? order by PROD_IMG_PRIO";
		List<ProductImg> imgList = getJdbcTemplate().query(imgSql, BeanPropertyRowMapper.newInstance(ProductImg.class), prodId);
		return imgList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 数量不能直接改
	@Transactional
	public int updateProduct(Product prod, ProductDetail detail, String prodImgSrc,
			Integer[] skuId, Integer[] stockNum, Float[] price, String[] skuName, String[] skuImgSrc) {
		
		// skuId=-1表示新增 其它表示修改
		List<Object[]> newSkuArgList = new ArrayList<Object[]>(); 
		for (int i = 0; i < skuId.length; i++) {
			if (skuId[i] == -1) {
				Object[] arg = {prod.getProdId(), stockNum[i], i, price[i], skuName[i], skuImgSrc[i]};
				newSkuArgList.add(arg);
			}
			else {
				Sku updateSku = new Sku();
				updateSku.setSkuId(skuId[i]);
				updateSku.setSkuName(skuName[i]);
				updateSku.setSkuPrio(i);
				updateSku.setSkuImgSrc(skuImgSrc[i]);
				update(updateSku);
			}
		}
		if (!newSkuArgList.isEmpty()) {
			String insertSkuSql = "insert into sku(PROD_ID, STOCK_NUM, SKU_PRIO, PRICE, SKU_NAME, SKU_IMG_SRC) values(?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(insertSkuSql, newSkuArgList);
		}
		
		
		// product
		int r = update(prod);
		
		// detail
		update(detail);
		
		// img
		getJdbcTemplate().update("delete from product_img where PROD_ID = ?", prod.getProdId());
		String[] imgSrc = prodImgSrc.split(",");
		List<Object[]> imgArgList = new ArrayList<Object[]>();
		for (int i = 0; i < imgSrc.length; i++) {
			Object[] arg = {prod.getProdId(), i, imgSrc[i]};
			imgArgList.add(arg);
		}
		String imgSql = "insert into product_img(PROD_ID, PROD_IMG_PRIO, PROD_IMG_SRC) values(?,?,?)";
		getJdbcTemplate().batchUpdate(imgSql, imgArgList);
		
		
		
		return r;
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
