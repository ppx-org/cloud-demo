package com.ppx.cloud.store.promo.program;

import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.store.common.dictionary.Dict;
import com.ppx.cloud.store.promo.statushistory.ProgramStatus;

@Service
public class ProgramIndexService extends MyDaoSupport {
	//private final int INIT_STATUS = 1;
	private final int RUNNING_STATUS = 2;
	private final int PAUSE_STATUS = 3;
	private final int STOP_STATUS = 4;
	
	private int getStatusForUpdate(Integer progId) {
		String sql = "select PROG_STATUS from program where PROG_ID = ? for update";
		return getJdbcTemplate().queryForObject(sql, Integer.class, progId);
	}
	
	
	private int updateStatus(Integer prodId, Integer status) {
		String sql = "update program set PROG_STATUS = ? where PROG_ID = ?";
		return getJdbcTemplate().update(sql, status, prodId);
	}
	
	@Transactional
	public String start(Integer progId) {
		int currentStatus = getStatusForUpdate(progId);
		if (currentStatus == RUNNING_STATUS) return "-1";
		
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
			default:return "-2";
		}
		
		updateStatus(progId, RUNNING_STATUS);
		
		ProgramStatus statusHistory = new ProgramStatus();
		statusHistory.setProgId(progId);
		statusHistory.setHistoryProgStatus(RUNNING_STATUS);
		int creator = GrantContext.getLoginAccount().getAccountId();
		statusHistory.setCreator(creator);
		insert(statusHistory);
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String updateSql = "INSERT INTO search_last_updated(MERCHANT_ID,PROG_LAST_UPDATED) VALUES (?,now()) ON DUPLICATE KEY UPDATE PROG_LAST_UPDATED=now()";
		getJdbcTemplate().update(updateSql, merchantId);
		
		return progId + "," + RUNNING_STATUS + "," + Dict.getProgStatusDesc(RUNNING_STATUS);
	}
	
	@Transactional
	public String pause(Integer progId) {
		if (getStatusForUpdate(progId) != RUNNING_STATUS) return "-1";
		
		updateStatus(progId, PAUSE_STATUS);
		
		ProgramStatus statusHistory = new ProgramStatus();
		statusHistory.setProgId(progId);
		statusHistory.setHistoryProgStatus(PAUSE_STATUS);
		int creator = GrantContext.getLoginAccount().getAccountId();
		statusHistory.setCreator(creator);
		insert(statusHistory);
		return progId + "," + PAUSE_STATUS + "," + Dict.getProgStatusDesc(PAUSE_STATUS);
	}
	
	@Transactional
	public String stop(Integer progId) {
		int currentStatus = getStatusForUpdate(progId);
		if (currentStatus != RUNNING_STATUS && currentStatus != PAUSE_STATUS) return "-1";
		
		// 删除索引
		String deleteSql = "delete from program_index where PROG_ID = ?";
		getJdbcTemplate().update(deleteSql, progId);
		
		updateStatus(progId, STOP_STATUS);
		ProgramStatus statusHistory = new ProgramStatus();
		statusHistory.setProgId(progId);
		statusHistory.setHistoryProgStatus(STOP_STATUS);
		int creator = GrantContext.getLoginAccount().getAccountId();
		statusHistory.setCreator(creator);
		insert(statusHistory);
		
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		String updateSql = "INSERT INTO search_last_updated(MERCHANT_ID,PROG_LAST_UPDATED) VALUES (?,now()) ON DUPLICATE KEY UPDATE PROG_LAST_UPDATED=now()";
		getJdbcTemplate().update(updateSql, merchantId);
		return progId + "," + STOP_STATUS + "," + Dict.getProgStatusDesc(STOP_STATUS);
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
				"from program_product pp join program p on pp.PROG_ID = ? where p.MERCHANT_ID = -1 and PROG_STATUS = 1";
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
				"select ?, c.PROG_ID, c.PROD_ID, ?, ?, ?, concat('E:', p.POLICY_ARGS,',C:', c.CHANGE_PRICE) " + 
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// onShelves时(offShelves时不删除，通过端口状态判断搜索)
	public int addProgramIndex(Integer prodId) {
		String prodSql = "select CAT_ID, MAIN_CAT_ID from product where PROD_ID = ?";
		Map<String, Object> prodMap = getJdbcTemplate().queryForMap(prodSql, prodId);
		Integer catId = (Integer)prodMap.get("CAT_ID");
		Integer mainCatId = (Integer)prodMap.get("MAIN_CAT_ID");
		Integer brandId = (Integer)prodMap.get("BRAND_ID");
		
		String deleteCatSql = "delete from program_index where PROD_ID = ? and (CAT_ID = ? or CAT_ID = ?)";
		getJdbcTemplate().update(deleteCatSql, prodId, catId, mainCatId);
		
		String categorySql = "insert into program_index(MERCHANT_ID, PROG_ID, PROD_ID, INDEX_BEGIN, INDEX_END, INDEX_PRIO, INDEX_POLICY, CAT_ID) " + 
				" select p.MERCHANT_ID, pc.PROG_ID, ?, p.PROG_BEGIN, p.PROG_END, p.PROG_PRIO, p.POLICY_ARGS, pc.CAT_ID " +
				" from program_category pc join program p on (pc.CAT_ID = ? or pc.CAT_ID = ?) and pc.PROG_ID = p.PROG_ID and p.PROG_STATUS = 2 and to_days(p.PROG_END) >= to_days(now()) ";
		getJdbcTemplate().update(categorySql, prodId, catId, mainCatId);
		
		
		if (brandId != null) {
			String deleteBrandSql = "delete from program_index where PROD_ID = ? and BRAND_ID = ?)";
			getJdbcTemplate().update(deleteBrandSql, prodId, brandId);
			
			String brandSql = "insert into program_index(MERCHANT_ID, PROG_ID, PROD_ID, INDEX_BEGIN, INDEX_END, INDEX_PRIO, INDEX_POLICY, BRAND_ID) " + 
					" select p.MERCHANT_ID, b.PROG_ID, ?, p.PROG_BEGIN, p.PROG_END, p.PROG_PRIO, p.POLICY_ARGS " +
					" from program_brand b join program p on b.BRAND_ID = ? and pc.PROG_ID = p.PROG_ID and p.PROG_STATUS = 2 and to_days(p.PROG_END) >= to_days(now()) ";
			 getJdbcTemplate().update(brandSql, prodId, brandId);
		}
		return 1;
	}
	
	

	

	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
