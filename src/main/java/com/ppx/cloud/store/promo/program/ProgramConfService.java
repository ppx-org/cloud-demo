package com.ppx.cloud.store.promo.program;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.service.MerchantService;
import com.ppx.cloud.store.promo.program.bean.ProgramBrand;
import com.ppx.cloud.store.promo.program.bean.ProgramCategory;
import com.ppx.cloud.store.promo.program.bean.ProgramChange;
import com.ppx.cloud.store.promo.program.bean.ProgramDependence;
import com.ppx.cloud.store.promo.program.bean.ProgramSpecial;
import com.ppx.cloud.store.promo.program.bean.ProgramProduct;

@Service
public class ProgramConfService extends MyDaoSupport {
	@Autowired
	private MerchantService merchantService;
	
	public List<ProgramCategory> listProgramCat(Integer progId) {
		String sql = "select pc.*, c.CAT_NAME from program_category pc left join category c on pc.CAT_ID = c.CAT_ID where pc.PROG_ID = ?";
		List<ProgramCategory> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(ProgramCategory.class), progId);
		return list;
	}
	
	@Transactional
	public int insertProgramCategory(ProgramCategory bean) {
		// 加锁
		merchantService.lockMerchant();
		
		// catId已经存在，或catId的子类或父类已经存在		
		String existsSql = "select count(*) from program_category p where p.PROG_ID = ? and " + 
			" exists (select 1 from category c where (c.CAT_ID = ? or c.PARENT_ID = ?) and (p.CAT_ID = c.CAT_ID or p.CAT_ID = c.PARENT_ID))";
		int c = getJdbcTemplate().queryForObject(existsSql, Integer.class, bean.getProgId(), bean.getCatId(), bean.getCatId());
		if (c >= 1) return 0;
		
		return insert(bean);
	}
	
	public int deleteProgramCategory(Integer progId, Integer catId) {
		return getJdbcTemplate().update("delete from program_category where PROG_ID = ? and CAT_ID = ?", progId, catId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// ---------------------------------------brand------------------------------------
	
	public List<ProgramBrand> listProgramBrand(Integer progId) {
		String sql = "select pb.*, b.BRAND_NAME from program_brand pb left join brand b on pb.BRAND_ID = b.BRAND_ID where pb.PROG_ID = ?";
		List<ProgramBrand> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(ProgramBrand.class), progId);
		return list;
	}
	
	@Transactional
	public int insertProgramBrand(ProgramBrand bean) {
		// 加锁
		merchantService.lockMerchant();
		
		// brandId已经存在		
		String existsSql = "select count(*) from program_brand p where p.PROG_ID = ? and p.BRAND_ID = ?";
		int c = getJdbcTemplate().queryForObject(existsSql, Integer.class, bean.getProgId(), bean.getBrandId());
		if (c >= 1) return 0;
		
		return insert(bean);
	}
	
	public int deleteProgramBrand(Integer progId, Integer brandId) {
		return getJdbcTemplate().update("delete from program_brand where PROG_ID = ? and BRAND_ID = ?", progId, brandId);
	}
	
	
	
	// ----------------------------------product-------------------------------
	public PageList<ProgramProduct> listProgramProduct(Page page, ProgramProduct bean) {
		
		MyCriteria c = createCriteria("and").addAnd("pp.PROD_ID like ?", bean.getProdId());
		
		StringBuilder cSql = new StringBuilder("select count(*) from program_product pp where pp.PROG_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select pp.*, p.PROD_TITLE from program_product pp left join product p on pp.PROD_ID = p.PROD_ID where pp.PROG_ID = ?").append(c);
		
		c.addPrePara(bean.getProgId());
			
		List<ProgramProduct> list = queryPage(ProgramProduct.class, page, cSql, qSql, c.getParaList());
		return new PageList<ProgramProduct>(list, page);
	}
	
	@Transactional
	public String insertProgramProduct(Integer progId, String prodIdStr) {
		// 加锁
		int merchantId = merchantService.lockMerchant();
		
		String[] prodId = prodIdStr.split(",");
		
		// result bit,1:产品ID不存在product,2:产品ID已经存在program_product
		String importSql = "insert into import_data(MERCHANT_ID, ROWNUM, INT_1, RESULT) " +
			"select " + merchantId + ", ?, ?, if ((select count(*) from product where PROD_ID = ? and MERCHANT_ID = " + merchantId + ") = 1, 0, 1) " +
			"^ if ((select count(*) from program_product where PROD_ID = ? and PROG_ID = " + progId + ") != 1, 0, 2) r";
		List<Object[]> argList = new ArrayList<Object[]>();
		for (int i = 0; i < prodId.length; i++) {
			Object[] arg = {i+1, prodId[i], prodId[i], prodId[i]};
			argList.add(arg);
		}
		
		// 清除
		String deleteSql = "delete from import_data where MERCHANT_ID = ?";
		getJdbcTemplate().update(deleteSql, merchantId);
		getJdbcTemplate().batchUpdate(importSql, argList);
		
		
		// 找出不符合条件记录
		String errorSql = "select group_concat(concat(ROWNUM, ':', RESULT)) msg from import_data where MERCHANT_ID = ? and result != ?";
		String msg = getJdbcTemplate().queryForObject(errorSql, String.class, merchantId, 0);
		if (!StringUtils.isEmpty(msg)) {
			return msg;
		}
		else {
			String insertSql = "insert into program_product(PROG_ID, PROD_ID) select ?, INT_1 from import_data where MERCHANT_ID = ?";
			int r = getJdbcTemplate().update(insertSql, progId, merchantId);
			return "ok:" + r;
		}
	}
	
	public int deleteProgramProduct(Integer progId, Integer prodId) {
		return getJdbcTemplate().update("delete from program_product where PROG_ID = ? and PROD_ID = ?", progId, prodId);
	}
	
	
	
	
	
	
	// ----------------special-----------------
	public PageList<ProgramSpecial> listProgramSpecial(Page page, ProgramSpecial bean) {
	
		MyCriteria c = createCriteria("and").addAnd("s.PROD_ID like ?", bean.getProdId());
		
		StringBuilder cSql = new StringBuilder("select count(*) from program_special s where s.PROG_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select * from program_special s left join product p on s.PROD_ID = p.PROD_ID where s.PROG_ID = ?").append(c);
		
		c.addPrePara(bean.getProgId());
			
		List<ProgramSpecial> list = queryPage(ProgramSpecial.class, page, cSql, qSql, c.getParaList());
		return new PageList<ProgramSpecial>(list, page);
	}
	
	@Transactional
	public String insertProgramSpecial(Integer progId, String prodIdStr, String specialPriceStr) {
		// 加锁
		int merchantId = merchantService.lockMerchant();
		
		String[] prodId = prodIdStr.split(",");
		String[] specialPrice = specialPriceStr.split(",");
		if (prodId.length != specialPrice.length) {
			return "-1";
		}
		
		// result bit,1:产品ID不存在product,2:产品ID已经存在program_special
		String importSql = "insert into import_data(MERCHANT_ID, ROWNUM, INT_1, NUM_1, RESULT) " +
			"select " + merchantId + ", ?, ?, ?, if ((select count(*) from product where PROD_ID = ? and MERCHANT_ID = " + merchantId + ") = 1, 0, 1) " +
			"^ if ((select count(*) from program_special where PROD_ID = ? and PROG_ID = " + progId + ") != 1, 0, 2) r";
		List<Object[]> argList = new ArrayList<Object[]>();
		for (int i = 0; i < prodId.length; i++) {
			Object[] arg = {i+1, prodId[i], specialPrice[i], prodId[i], prodId[i]};
			argList.add(arg);
		}
		
		// 清除
		String deleteSql = "delete from import_data where MERCHANT_ID = ?";
		getJdbcTemplate().update(deleteSql, merchantId);
		getJdbcTemplate().batchUpdate(importSql, argList);
		
		
		// 找出不符合条件记录
		String errorSql = "select group_concat(concat(ROWNUM, ':', RESULT)) msg from import_data where MERCHANT_ID = ? and result != ?";
		String msg = getJdbcTemplate().queryForObject(errorSql, String.class, merchantId, 0);
		if (!StringUtils.isEmpty(msg)) {
			return msg;
		}
		else {
			String insertSql = "insert into program_special(PROG_ID, PROD_ID, SPECIAL_PRICE) select ?, INT_1, NUM_1 from import_data where MERCHANT_ID = ?";
			int r = getJdbcTemplate().update(insertSql, progId, merchantId);
			return "ok:" + r;
		}
		
	}

	
	public int deleteProgramSpecial(Integer progId, Integer prodId) {
		return getJdbcTemplate().update("delete from program_special where PROG_ID = ? and PROD_ID = ?", progId, prodId);
	}
	
	
	
	

	// ----------------dependence-----------------
	public PageList<ProgramDependence> listProgramDependence(Page page, ProgramDependence bean) {
	
		MyCriteria c = createCriteria("and").addAnd("d.PROD_ID like ?", bean.getProdId());
		
		StringBuilder cSql = new StringBuilder("select count(*) from program_dependence d where d.PROG_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select d.*, p.PROD_TITLE from program_dependence d left join product p on d.PROD_ID = p.PROD_ID where d.PROG_ID = ?").append(c);
		
		c.addPrePara(bean.getProgId());
			
		List<ProgramDependence> list = queryPage(ProgramDependence.class, page, cSql, qSql, c.getParaList());
		return new PageList<ProgramDependence>(list, page);
	}
	
	@Transactional
	public String insertProgramDependence(Integer progId, String prodIdStr, String dependProdIdStr, String dependPriceStr) {
		// 加锁
		int merchantId = merchantService.lockMerchant();
		
		String[] prodId = prodIdStr.split(",");
		String[] dependProdId = dependProdIdStr.split(",");
		String[] dependPrice = dependPriceStr.split(",");
		
		if (prodId.length != dependProdId.length || prodId.length != dependPrice.length) {
			return "-1";
		}
		
		// result bit,1:产品ID不存在product,2:产品ID已经存在program_special
		String importSql = "insert into import_data(MERCHANT_ID, ROWNUM, INT_1, INT_2, NUM_1, RESULT) " +
			"select " + merchantId + ", ?, ?, ?, ?, if ((select count(*) from product where PROD_ID = ? and MERCHANT_ID = " + merchantId + ") = 1, 0, 1) " +
			"^ if ((select count(*) from program_dependence where PROD_ID = ? and PROG_ID = " + progId + ") != 1, 0, 2) " +
			"^ if ((select count(*) from product where PROD_ID = ? and  MERCHANT_ID = " + merchantId + ") = 1, 0, 4) ";
		List<Object[]> argList = new ArrayList<Object[]>();
		for (int i = 0; i < prodId.length; i++) {
			Object[] arg = {i+1, prodId[i], dependProdId[i], dependPrice[i], prodId[i], prodId[i], dependProdId[i]};
			argList.add(arg);
		}
		
		// 清除
		String deleteSql = "delete from import_data where MERCHANT_ID = ?";
		getJdbcTemplate().update(deleteSql, merchantId);
		getJdbcTemplate().batchUpdate(importSql, argList);
		
		
		// 找出不符合条件记录
		String errorSql = "select group_concat(concat(ROWNUM, ':', RESULT)) msg from import_data where MERCHANT_ID = ? and result != ?";
		String msg = getJdbcTemplate().queryForObject(errorSql, String.class, merchantId, 0);
		if (!StringUtils.isEmpty(msg)) {
			return msg;
		}
		else {
			String insertSql = "insert into program_dependence(PROG_ID, PROD_ID, DEPEND_RPOD_ID, DEPEND_PRICE) select ?, INT_1, INT_2, NUM_1 from import_data where MERCHANT_ID = ?";
			int r = getJdbcTemplate().update(insertSql, progId, merchantId);
			return "ok:" + r;
		}
		
	}
	
	public int deleteProgramDependence(Integer progId, Integer prodId) {
		return getJdbcTemplate().update("delete from program_dependence where PROG_ID = ? and PROD_ID = ?", progId, prodId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	// ----------------change-----------------
	public PageList<ProgramChange> listProgramChange(Page page, ProgramChange bean) {
	
		MyCriteria c = createCriteria("and").addAnd("c.PROD_ID like ?", bean.getProdId());
		
		StringBuilder cSql = new StringBuilder("select count(*) from program_change c where c.PROG_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select c.*, p.PROD_TITLE from program_change c left join product p on c.PROD_ID = p.PROD_ID where c.PROG_ID = ?").append(c);
		
		c.addPrePara(bean.getProgId());
			
		List<ProgramChange> list = queryPage(ProgramChange.class, page, cSql, qSql, c.getParaList());
		return new PageList<ProgramChange>(list, page);
	}
	
	@Transactional
	public String insertProgramChange(Integer progId, String prodIdStr, String changePriceStr) {
		// 加锁
		int merchantId = merchantService.lockMerchant();
		
		String[] prodId = prodIdStr.split(",");
		String[] changePrice = changePriceStr.split(",");
		if (prodId.length != changePrice.length) {
			return "-1";
		}
		
		// result bit,1:产品ID不存在product,2:产品ID已经存在program_change
		String importSql = "insert into import_data(MERCHANT_ID, ROWNUM, INT_1, NUM_1, RESULT) " +
			"select " + merchantId + ", ?, ?, ?, if ((select count(*) from product where PROD_ID = ? and MERCHANT_ID = " + merchantId + ") = 1, 0, 1) " +
			"^ if ((select count(*) from program_change where PROD_ID = ? and PROG_ID = " + progId + ") != 1, 0, 2) r";
		List<Object[]> argList = new ArrayList<Object[]>();
		for (int i = 0; i < prodId.length; i++) {
			Object[] arg = {i+1, prodId[i], changePrice[i], prodId[i], prodId[i]};
			argList.add(arg);
		}
		
		// 清除
		String deleteSql = "delete from import_data where MERCHANT_ID = ?";
		getJdbcTemplate().update(deleteSql, merchantId);
		getJdbcTemplate().batchUpdate(importSql, argList);
		
		
		// 找出不符合条件记录
		String errorSql = "select group_concat(concat(ROWNUM, ':', RESULT)) msg from import_data where MERCHANT_ID = ? and result != ?";
		String msg = getJdbcTemplate().queryForObject(errorSql, String.class, merchantId, 0);
		if (!StringUtils.isEmpty(msg)) {
			return msg;
		}
		else {
			String insertSql = "insert into program_change(PROG_ID, PROD_ID, CHANGE_PRICE) select ?, INT_1, NUM_1 from import_data where MERCHANT_ID = ?";
			int r = getJdbcTemplate().update(insertSql, progId, merchantId);
			return "ok:" + r;
		}
		
	}

	
	public int deleteProgramChange(Integer progId, Integer prodId) {
		return getJdbcTemplate().update("delete from program_change where PROG_ID = ? and PROD_ID = ?", progId, prodId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
