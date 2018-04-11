package com.ppx.cloud.store.product.release;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.store.product.changestatus.ChangeStatus;
import com.ppx.cloud.store.product.release.bean.Product;
import com.ppx.cloud.store.product.release.bean.ProductDetail;
import com.ppx.cloud.store.product.release.bean.ProductExport;
import com.ppx.cloud.store.product.release.bean.ProductImg;
import com.ppx.cloud.store.product.release.bean.Sku;
import com.ppx.cloud.store.promo.program.ProgramIndexService;


@Service
public class ProductService extends MyDaoSupport {
	
	@Autowired
	private ProgramIndexService programIndexService;
	
	public PageList<Product> listProduct(Page page, Product bean) {
		Map<String, Object> map = queryProduct(bean);
		StringBuilder cSql = (StringBuilder)map.get("cSql");
		StringBuilder qSql = (StringBuilder)map.get("qSql");
		MyCriteria c = (MyCriteria)map.get("c");
		
		List<Product> list = queryPage(Product.class, page, cSql, qSql, c.getParaList());
		return new PageList<Product>(list, page);
	}
	
	private Map<String, Object> queryProduct(Product bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		MyCriteria c = createCriteria("and")
			.addAnd("PROD_STATUS = ?", bean.getProdStatus())
			.addAnd("REPO_ID = ?", bean.getRepoId())
			.addAnd("CAT_ID = ?", bean.getCatId())
			.addAnd("MAIN_CAT_ID = ?", bean.getMainCatId())
			.addAnd("BRAND_ID = ?", bean.getBrandId())
			.addAnd("PROD_ID = ?", bean.getProdId())
			.addAnd("PROD_TITLE like ?", "%", bean.getProdTitle(), "%");
		
		String whereSql = " where p.MERCHANT_ID = ?";
		StringBuilder cSql = new StringBuilder("select count(*) from product p").append(whereSql).append(c);
		StringBuilder qSql = new StringBuilder("select p.*, (select concat(min(PRICE), '-', max(PRICE)) from sku where PROD_ID = p.PROD_ID) PROD_PRICE,"
				+ " (select sum(STOCK_NUM) from sku where PROD_ID = p.PROD_ID) PROD_STOCK from product p").append(whereSql).append(c).append(" order by p.PROD_ID desc");
		
		c.addPrePara(merchantId);
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("cSql", cSql);
		returnMap.put("qSql", qSql);
		returnMap.put("c", c);
		
		return returnMap;
	}
	
	public List<Product> exportProduct(Product bean) {
		Map<String, Object> map = queryProduct(bean);
		StringBuilder qSql = (StringBuilder)map.get("qSql");
		MyCriteria c = (MyCriteria)map.get("c");
		
		List<Product> r = (List<Product>)super.getJdbcTemplate().query(qSql.toString(), 
				BeanPropertyRowMapper.newInstance(Product.class), c.getParaList().toArray());
		return r;
	}
	
	public List<ProductExport> exportProductDetail(Product bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		MyCriteria c = createCriteria("and").addAnd("REPO_ID = ?", bean.getRepoId());
		String sql = "select p.*, (select concat(min(PRICE), '-', max(PRICE)) from sku where PROD_ID = p.PROD_ID) PROD_PRICE,"
				+ " (select sum(STOCK_NUM) from sku where PROD_ID = p.PROD_ID) PROD_STOCK, d.BAR_CODE, d.PROD_POSITION, "
				+ " (select group_concat(concat('SKU_ID:', SKU_ID, ';数量:', STOCK_NUM, ';名称:', SKU_NAME, ';价格:', PRICE) order by SKU_PRIO SEPARATOR '<BR>') from sku where PROD_ID = p.PROD_ID) SKU_MSG"
				+ " from product p join product_detail d on p.PROD_ID = d.PROD_ID where p.MERCHANT_ID = ?" + c;
		c.addPrePara(merchantId);
		
		List<ProductExport> r = (List<ProductExport>)super.getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(ProductExport.class), c.getParaList().toArray());
		
		return r;
	}
	
	
	@Transactional
	public int insertProduct(Product prod, ProductDetail detail, String prodImgSrc,
			Integer[] stockNum, Float[] price, String[] skuName, String[] skuImgSrc) {
		
		int creator = GrantContext.getLoginAccount().getAccountId();
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
		
		// >>>>>>>>>>>>>>>>>>>>>>历史变量记录
		// change_stock
		String insertChangeStock = "insert into change_stock(SKU_ID, CHANGE_NUM, CHANGE_TYPE, CREATOR) select SKU_ID, STOCK_NUM, 1, ? from sku where PROD_ID = ?";
		getJdbcTemplate().update(insertChangeStock, creator, prodId);
		
		// change_price
		String insertChangePrice = "insert into change_price(SKU_ID, CHANGE_PRICE, CREATOR) select SKU_ID, PRICE, ? from sku where PROD_ID = ?";
		getJdbcTemplate().update(insertChangePrice, creator, prodId);
		
		// product
		prod.setProdId(prodId);
		prod.setMerchantId(merchantId);
		// img 第一张为主图
		prod.setMainImgSrc(prodImgSrc.split(",")[0]);
		insert(prod);
		
		// detail
		detail.setProdId(prodId);
		detail.setCreator(creator);
		insert(detail);
		
		// img 第一张为主图
		String[] imgSrc = prodImgSrc.split(",");
		if (imgSrc.length > 1) {
			List<Object[]> imgArgList = new ArrayList<Object[]>();
			for (int i = 1; i < imgSrc.length; i++) {
				Object[] arg = {prodId, i, imgSrc[i]};
				imgArgList.add(arg);
			}
			String imgSql = "insert into product_img(PROD_ID, PROD_IMG_PRIO, PROD_IMG_SRC) values(?,?,?)";
			getJdbcTemplate().batchUpdate(imgSql, imgArgList);
		}
		
		
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

	// 数量和价格不能直接改
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
				updateSku.setSkuName(skuName.length == 0 ? null : skuName[i]);
				updateSku.setSkuPrio(i);
				updateSku.setSkuImgSrc(skuImgSrc[i]);
				update(updateSku);
			}
		}
		if (!newSkuArgList.isEmpty()) {
			String insertSkuSql = "insert into sku(PROD_ID, STOCK_NUM, SKU_PRIO, PRICE, SKU_NAME, SKU_IMG_SRC) values(?,?,?,?,?,?)";
			getJdbcTemplate().batchUpdate(insertSkuSql, newSkuArgList);
		}
		
		// img 第一张为主图
		prod.setMainImgSrc(prodImgSrc.split(",")[0]);
		// product
		int r = update(prod);
		
		// detail
		update(detail);
		
		// img 第一张为主图
		getJdbcTemplate().update("delete from product_img where PROD_ID = ?", prod.getProdId());
		String[] imgSrc = prodImgSrc.split(",");
		if (imgSrc.length > 1) {
			List<Object[]> imgArgList = new ArrayList<Object[]>();
			for (int i = 1; i < imgSrc.length; i++) {
				Object[] arg = {prod.getProdId(), i, imgSrc[i]};
				imgArgList.add(arg);
			}
			String imgSql = "insert into product_img(PROD_ID, PROD_IMG_PRIO, PROD_IMG_SRC) values(?,?,?)";
			getJdbcTemplate().batchUpdate(imgSql, imgArgList);
		}
		
		return r;
	}

	
	// action >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	/*
	 * onShelves和offSheles都需要判断相关活动是否停止，因为需要生成价格索引和搜索索引
	 */
	
	// 2:上架
	@Transactional
	public int onShelves(Integer prodId) {
		programIndexService.addProgramIndex(prodId);
		
		ChangeStatus changeStatus = new ChangeStatus();
		changeStatus.setProdId(prodId);
		changeStatus.setChangeStatus(2);
		int creator = GrantContext.getLoginAccount().getAccountId();
		changeStatus.setCreator(creator);
		insert(changeStatus);
		
		String sql = "update product set PROD_STATUS = ? where PROD_ID = ?";
		getJdbcTemplate().update(sql, 2, prodId);
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String updateSql = "INSERT INTO search_last_updated(MERCHANT_ID,PROD_LAST_UPDATED) VALUES (?,now()) ON DUPLICATE KEY UPDATE PROD_LAST_UPDATED=now()";
		getJdbcTemplate().update(updateSql, merchantId);
		
		return 2;
	}
	
	// 3:下架
	@Transactional
	public int offShelves(Integer prodId) {
		
		ChangeStatus changeStatus = new ChangeStatus();
		changeStatus.setProdId(prodId);
		changeStatus.setChangeStatus(3);
		int creator = GrantContext.getLoginAccount().getAccountId();
		changeStatus.setCreator(creator);
		insert(changeStatus);
		
		String sql = "update product set PROD_STATUS = ? where PROD_ID = ?";
		getJdbcTemplate().update(sql, 3, prodId);
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String updateSql = "INSERT INTO search_last_updated(MERCHANT_ID,PROD_LAST_UPDATED) VALUES (?,now()) ON DUPLICATE KEY UPDATE PROD_LAST_UPDATED=now()";
		getJdbcTemplate().update(updateSql, merchantId);
		
		return 3;
	}
	
	
}
