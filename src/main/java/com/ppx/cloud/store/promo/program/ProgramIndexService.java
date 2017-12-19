package com.ppx.cloud.store.promo.program;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;

@Service
public class ProgramIndexService extends MyDaoSupport {
	
	
	
	
	
	
	
	
	// 0:删除1:未开始2:进行中3:暂停(4):结束
	
	
	
	@Transactional
	public int start(Integer progId) {
		Program prog = getJdbcTemplate().queryForObject("select * from program where PROG_ID = ?",
				BeanPropertyRowMapper.newInstance(Program.class), progId);	
		
		// 删除
		String deleteSql = "delete from program_index where PROG_ID = ?";
		getJdbcTemplate().update(deleteSql, progId);
		
		// 生成索引
		switch (prog.getPolicyType()) {
			case "Category":startCategory(prog);break;
			case "Brand":startBrand(prog);break;
			case "Product":startProduct(prog);break;
			case "Special":startSpecial(prog);break;
			case "Change":startChange(prog);break;
			case "Dependence":startDependence(prog);break;
			default:return -1;
		}
		
		
		
		return 1;
	}
	
	
	@Transactional
	public int stop(Integer progId) {
	
		return 1;
	}
	
	@Transactional
	public int pause(Integer progId) {
	
		return 1;
	}
	
	
	private int updateStatus(Integer prodId, Integer status) {
		String sql = "update program set RECORD_STATUS = ? where PROD_ID = ?";
		getJdbcTemplate().update(sql, status, prodId);
		return 1;
	}
	
	
	
	
	
	
	
	
		
		
		
		
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private int startCategory(Program prog) {
		String categorySql = "insert into program_index(MERCHANT_ID, PROG_ID, PROD_ID, INDEX_BEGIN, INDEX_END, INDEX_PRIO, INDEX_POLICY, CAT_ID) " + 
			"select ?, pc.PROG_ID, prod.PROD_ID, ?, ?, ?, ?, pc.CAT_ID " +
			"from program_category pc join product prod on pc.PROG_ID = ? and (pc.CAT_ID = prod.CAT_ID or pc.CAT_ID = prod.MAIN_CAT_ID) ";
		int r = getJdbcTemplate().update(categorySql, prog.getMerchantId(), prog.getProgBegin(), prog.getProgEnd(), 
				prog.getProgPrio(), prog.getPolicyArgs(), prog.getProgId());
		return r;
	}
	
	private int startBrand(Program prog) {
		String brandSql = "insert into program_index(MERCHANT_ID, PROG_ID, PROD_ID, INDEX_BEGIN, INDEX_END, INDEX_PRIO, INDEX_POLICY, BRAND_ID) " + 
			"select ?, b.PROG_ID, prod.PROD_ID, ?, ?, ?, ?, b.BRAND_ID " +
			"from program_brand b join product prod on b.PROG_ID = ? and b.BRAND_ID = prod.BRAND_ID ";
		int r = getJdbcTemplate().update(brandSql, prog.getMerchantId(), prog.getProgBegin(), prog.getProgEnd(),
				prog.getProgPrio(), prog.getPolicyArgs(), prog.getProgId());
		return r;
	}
	
	private int startProduct(Program prog) {
		String productSql = "insert into program_index(MERCHANT_ID, PROG_ID, PROD_ID, INDEX_BEGIN, INDEX_END, INDEX_PRIO, INDEX_POLICY) " + 
				"select ?, pp.PROG_ID, pp.PROD_ID, ?, ?, ?, ? " + 
				"from program_product pp join program p on pp.PROG_ID = ? where p.MERCHANT_ID = -1 and RECORD_STATUS = 1";
		int r = getJdbcTemplate().update(productSql, prog.getMerchantId(), prog.getProgBegin(), prog.getProgEnd(),
				prog.getProgPrio(), prog.getPolicyArgs(), prog.getProgId());
		return r;
	}
	
	private int startSpecial(Program prog) {
		String specialSql = "insert into program_index(MERCHANT_ID, PROG_ID, PROD_ID, INDEX_BEGIN, INDEX_END, INDEX_PRIO, INDEX_POLICY) " + 
				"select ?, s.PROG_ID, s.PROD_ID, ?, ?, ?, concat('S:', s.SPECIAL_PRICE) " + 
				"from program_special s where p.PROD_ID = ?";
		int r = getJdbcTemplate().update(specialSql, prog.getMerchantId(), prog.getProgBegin(), prog.getProgEnd(),
				prog.getProgPrio(), prog.getProgId());
		return r;
	}
	
	private int startChange(Program prog) {
		String changeSql = "insert into program_index(MERCHANT_ID, PROG_ID, PROD_ID, INDEX_BEGIN, INDEX_END, INDEX_PRIO, INDEX_POLICY) " + 
				"select ?, c.PROG_ID, c.PROD_ID, ?, ?, ?, concat('E:', p.POLICY_ARGS,',S:', c.CHANGE_PRICE) " + 
				"from program_change c join program p on c.PROG_ID = p.PROG_ID and p.PROG_ID = ?";
		int r = getJdbcTemplate().update(changeSql, prog.getMerchantId(), prog.getProgBegin(), prog.getProgEnd(),
				prog.getProgPrio(), prog.getProgId());
		return r;
	}
	
	private int startDependence(Program prog) {
		String dependenceSql = "insert into program_index(MERCHANT_ID, PROG_ID, PROD_ID, INDEX_BEGIN, INDEX_END, INDEX_PRIO, INDEX_POLICY) " + 
				"select ?, d.PROG_ID, d.PROD_ID, ?, ?, ?, concat('D:', d.DEPEND_RPOD_ID, ',P:', d.DEPEND_PRICE) " +
				"from program_dependence d where d.PROG_ID = ?";
		int r = getJdbcTemplate().update(dependenceSql, prog.getMerchantId(), prog.getProgBegin(), prog.getProgEnd(),
				prog.getProgPrio(), prog.getProgId());
		return r;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 
	public int addProgramIndex(Integer prodId, Integer catId, Integer mainCatId, Integer brandId) {
		
		createCategoryIndex(prodId, catId, mainCatId);
		if (!StringUtils.isEmpty(brandId)) {
			createBrandIndex(prodId, brandId);
		}
		return 1;
	}
	
	
	
	
	public int editCategoryIndex(Integer prodId, Integer oldCatId, Integer oldMainCatId, Integer catId, Integer mainCatId) {
		// delete
		String deleteSql = "delete from program_index where PROD_ID = ? and (CAT_ID = ? or CAT_ID = ?)";
		
		
		
		return 1;
	}
	
	public int editBrandIndex(Integer prodId, Integer oldBrandId, Integer brandId) {
		// delete
		String deleteSql = "delete from program_index where PROD_ID = ? and BRAND_ID = ?";
		
		createBrandIndex(prodId, brandId);
		
		
		return 1;
	}
	

	
	
	
	
	private int createCategoryIndex(Integer prodId, Integer catId, Integer mainCatId) {
		String categorySql = "insert into program_index(MERCHANT_ID, PROG_ID, PROD_ID, INDEX_BEGIN, INDEX_END, INDEX_PRIO, INDEX_POLICY) " + 
				" select p.MERCHANT_ID, pc.PROG_ID, ?, p.PROG_BEGIN, p.PROG_END, p.PROG_PRIO, p.POLICY_ARGS " +
				" from program_category pc join program p on (pc.CAT_ID = ? or pc.MAIN_CAT_ID = ?) and pc.PROG_ID = p.PROG_ID and p.RECORD_STATUS = 2 and to_days(p.PROD_END) >= to_days(now()) ";
		
		return 1;
	}
	
	
	
	
	
	private int createBrandIndex(Integer prodId, Integer brandId) {
		String brandSql = "insert into program_index(MERCHANT_ID, PROG_ID, PROD_ID, INDEX_BEGIN, INDEX_END, INDEX_PRIO, INDEX_POLICY) " + 
				" select p.MERCHANT_ID, b.PROG_ID, ?, p.PROG_BEGIN, p.PROG_END, p.PROG_PRIO, p.POLICY_ARGS " +
				" from program_brand b join program p on b.BRAND_ID = ? and pc.PROG_ID = p.PROG_ID and p.RECORD_STATUS = 2 and to_days(p.PROD_END) >= to_days(now()) ";
		return 1;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
