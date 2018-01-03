package com.ppx.cloud.store.release.prod;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;


@Service
public class ProductService extends MyDaoSupport {
	
	
	public PageList<Product> listProduct(Page page, Product bean) {
		
		MyCriteria c = createCriteria("where")
			.addAnd("PROD_TITLE like ?", "%", bean.getProdTitle(), "%")
			.addAnd("RECORD_STATUS = ?", bean.getRecordStatus());
		
		StringBuilder cSql = new StringBuilder("select count(*) from product p").append(c);
		StringBuilder qSql = new StringBuilder("select p.* from product p").append(c);
		
		List<Product> list = queryPage(Product.class, page, cSql, qSql, c.getParaList());
		return new PageList<Product>(list, page);
	}
	
	public int insertProduct(Product bean) {
		return insert(bean);
	}
	
	
	
	
	
	
	
	public Product getProduct(int prodId) {
		String sql = "select * from product where PROD_ID = ?";
		Product prod = getJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(Product.class), prodId);
		return prod;
	}
	
	
	public List<Sku> listSku(int prodId) {
		String imgSql = "select * from sku_img i where exists(select 1 from sku s where s.SKU_ID = i.SKU_ID and s.PROD_ID = 1) order by SKU_ID, SKU_IMG_PRIO";
		List<SkuImg> imgList = getJdbcTemplate().query(imgSql, BeanPropertyRowMapper.newInstance(SkuImg.class), prodId);
		
		String skuSql = "select * from sku where PROD_ID = ? order by SKU_PRIO";
		List<Sku> skuList = getJdbcTemplate().query(skuSql, BeanPropertyRowMapper.newInstance(Sku.class), prodId);
		
		for (Sku sku : skuList) {
			List<SkuImg> skuImgList = listSkuImg(imgList, sku.getSkuId());
			sku.setSkuImgList(skuImgList);
		}
		
		return skuList;
	}
	
	private List<SkuImg> listSkuImg(List<SkuImg> imgList, int skuId) {
		List<SkuImg> returnList = new ArrayList<SkuImg>();
		for (SkuImg skuImg : imgList) {
			if (skuImg.getSkuId() == skuId) {
				returnList.add(skuImg);
			}
		}
		return returnList;
	}
	
	
	
	
	
	
	
	
	
	
	// action 
	public int onShelves(Integer prodId) {
		String sql = "update product set RECORD_STATUS = ? where PROD_ID = ?";
		getJdbcTemplate().update(sql, 2, prodId);
		return 2;
	}
	
	public int offShelves(Integer prodId) {
		String sql = "update product set RECORD_STATUS = ? where PROD_ID = ?";
		getJdbcTemplate().update(sql, 3, prodId);
		return 3;
	}
	
	
}
