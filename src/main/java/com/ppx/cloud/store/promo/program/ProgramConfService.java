package com.ppx.cloud.store.promo.program;

import java.util.ArrayList;
import java.util.BitSet;
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
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.grant.service.MerchantService;
import com.ppx.cloud.store.promo.program.bean.ProgramBrand;
import com.ppx.cloud.store.promo.program.bean.ProgramCategory;
import com.ppx.cloud.store.promo.program.bean.ProgramSpecial;
import com.ppx.cloud.store.promo.program.bean.ProgramSubject;

@Service
public class ProgramConfService extends MyDaoSupport {
	@Autowired
	private MerchantService merchantService;
	
	public List<ProgramCategory> listProgramCat(Integer progId) {
		String sql = "select * from program_category where PROG_ID = ?";
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
		String sql = "select * from program_brand where PROG_ID = ?";
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// ----------------------------------subject-------------------------------
	
	
	
	public List<ProgramSubject> listProgramSubject(Integer progId) {
		String sql = "select * from program_subject where PROG_ID = ?";
		List<ProgramSubject> list = getJdbcTemplate().query(sql, BeanPropertyRowMapper.newInstance(ProgramSubject.class), progId);
		return list;
	}
	
	@Transactional
	public int insertProgramSubject(ProgramSubject bean) {
		// 加锁
		merchantService.lockMerchant();
		
		// subject已经存在		
		String existsSql = "select count(*) from program_subject p where p.PROG_ID = ? and p.SUBJECT_ID = ?";
		int c = getJdbcTemplate().queryForObject(existsSql, Integer.class, bean.getProgId(), bean.getSubjectId());
		if (c >= 1) return 0;
		
		return insert(bean);
	}
	
	public int deleteProgramSubject(Integer progId, Integer subjectId) {
		return getJdbcTemplate().update("delete from program_subject where PROG_ID = ? and SUBJECT_ID = ?", progId, subjectId);
	}
	
	
	
	
	
	
	// ----------------special-----------------
	public PageList<ProgramSpecial> listProgramSpecial(Page page, ProgramSpecial bean) {
	
		MyCriteria c = createCriteria("and").addAnd("PROD_ID like ?", bean.getProdId());
		
		StringBuilder cSql = new StringBuilder("select count(*) from program_special where PROG_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select * from program_special where PROG_ID = ?").append(c);
		
		c.addPrePara(bean.getProgId());
			
		List<ProgramSpecial> list = queryPage(ProgramSpecial.class, page, cSql, qSql, c.getParaList());
		return new PageList<ProgramSpecial>(list, page);
	}
	
	
	@Transactional
	public int insertProgramSpecial(Integer progId, String prodIdStr, String specialPriceStr) {
		// 加锁
		int merchantId = merchantService.lockMerchant();
		
		String[] prodId = prodIdStr.split(",");
		String[] specialPrice = specialPriceStr.split(",");
		if (prodId.length != specialPrice.length) {
			return -1;
		}
		
		// result bit,1:产品ID不存在product,2:产品ID已经存在program_special
		String insertSql = "insert into import_data(MERCHANT_ID, ID, NUM_1, RESULT) " +
			"select " + merchantId + ", ?, ?, if ((select count(*) from product where PROD_ID = ? and REPO_ID in (select REPO_ID from repository where MERCHANT_ID = " + merchantId + ")) = 1,  0, 1) " +
			"^ if ((select count(*) from program_special where PROD_ID = ? and PROG_ID = " + progId + ") != 1, 0, 2) r";
		List<Object[]> argList = new ArrayList<Object[]>();
		for (int i = 0; i < prodId.length; i++) {
			Object[] arg = {prodId[i], specialPrice[i], prodId[i], prodId[i]};
			argList.add(arg);
		}
		
		// 
		String deleteSql = "delete from import_data where MERCHANT_ID = ?";
		getJdbcTemplate().update(deleteSql, merchantId);
		int r[] = getJdbcTemplate().batchUpdate(insertSql, argList);
		
		
		
		
		return 1;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public int deleteProgramSpecial(Integer progId, Integer subjectId) {
		return getJdbcTemplate().update("delete from program_subject where PROG_ID = ? and SUBJECT_ID = ?", progId, subjectId);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
