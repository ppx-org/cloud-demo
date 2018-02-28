package com.ppx.cloud.micro.content.product;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;


@Service
public class MProductService extends MyDaoSupport {
	
	public MProduct getProduct(Integer prodId) {		
				
		String sql = "select p.PROD_ID, p.PROD_TITLE, max(s.PRICE) MAX_PRICE, min(s.PRICE) MIN_PRICE, d.PROD_ARGS, d.PROD_DESC,"
			+ " (select group_concat(PROD_IMG_SRC ORDER BY PROD_IMG_PRIO) from product_img where PROD_ID = p.PROD_ID) IMG_SRC_STR,"
			+ " (select ifnull((select INDEX_POLICY from program_index i where i.PROD_ID = p.PROD_ID and curdate() between INDEX_BEGIN and INDEX_END order by INDEX_PRIO desc limit 1), '')) POLICY" 
			+ " from product p join sku s on p.PROD_ID = ? and p.PROD_ID = s.PROD_ID left join product_detail d on p.PROD_ID = d.PROD_ID" 
			+ " order by s.SKU_PRIO";

		MProduct product = getJdbcTemplate().queryForObject(sql, BeanPropertyRowMapper.newInstance(MProduct.class), prodId);
		
		String skuSql = "select * from sku where PROD_ID = ?";
		List<MSku> skuList = getJdbcTemplate().query(skuSql, BeanPropertyRowMapper.newInstance(MSku.class), prodId);
		product.setSkuList(skuList);
		
		return product;
	}
	
	
	
	
	
}
