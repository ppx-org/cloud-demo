package com.ppx.cloud.store.merchant.child;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.LimitRecord;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.bean.MerchantAccount;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.grant.common.GrantUtils;

@Service
public class ChildService extends MyDaoSupport {

	
	public PageList<MerchantAccount> listChild(Page page, MerchantAccount child) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		MyCriteria c = createCriteria("and")
				.addAnd("ACCOUNT_ID = ?", child.getAccountId())
				.addAnd("LOGIN_ACCOUNT like ?", "%", child.getLoginAccount(), "%");
		
		String sql = "from merchant_account where MERCHANT_ID = ? and RECORD_STATUS = ? and MERCHANT_ID != ACCOUNT_ID";
		StringBuilder cSql = new StringBuilder("select count(*) ").append(sql).append(c);
		StringBuilder qSql = new StringBuilder("select * ").append(sql).append(c).append(" order by ACCOUNT_ID desc");
		c.addPrePara(merchantId);
		c.addPrePara(1);
		List<MerchantAccount> list = queryPage(MerchantAccount.class, page, cSql, qSql, c.getParaList());

		return new PageList<MerchantAccount>(list, page);
	}
	
//	public List<MerchantAccount> listChildAccount(Page page, MerchantAccount account) {
//		List<Object> paraList = new ArrayList<Object>();
//		paraList.add(GrantContext.getLoginAccount().getMerchantId());
//		
//		MyCriteria c = createCriteria("and")
//			.addAnd("a.ACCOUNT_ID = ?", account.getAccountId())
//			.addAnd("a.LOGIN_ACCOUNT like ?", "%", account.getMerchantName(), "%");
//		
//		StringBuilder cSql = new StringBuilder("select count(*) from merchant_accoun where MERCHANT_ID = ? and MERCHANT_ID != ACCOUNT_ID").append(c);
//		StringBuilder qSql = new StringBuilder("select * from merchant_account a where a.MERCHANT_ID = ? and MERCHANT_ID != ACCOUNT_ID")
//			.append(c).append(" order by a.MERCHANT_ID");
//		
//		paraList.addAll(c.getParaList());
//		return queryPage(MerchantAccount.class, page, cSql, qSql, paraList);
//	}

	@Transactional
	public int insertChild(MerchantAccount bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		MerchantAccount account = new MerchantAccount();
		account.setMerchantId(merchantId);
		account.setLoginAccount(bean.getLoginAccount());
		account.setLoginPassword(GrantUtils.getMD5Password(bean.getLoginPassword()));

		return insert(account, "LOGIN_ACCOUNT");
	}

	public MerchantAccount getChild(Integer id) {
		MerchantAccount bean = getJdbcTemplate().queryForObject("select * from merchant_account where ACCOUNT_ID = ?",
				BeanPropertyRowMapper.newInstance(MerchantAccount.class), id);
		return bean;
	}
	
	
	public int updateAccount(MerchantAccount bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		// 帐号唯一，只能更新自己子帐号的
		return update(bean, new LimitRecord("MERCHANT_ID", merchantId), "LOGIN_ACCOUNT");
	}
	
	public int updatePassword(Integer id, String loginPassword) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		// 只能更新自己子帐号的
		String sql = "update merchant_account set LOGIN_PASSWORD = ? where ACCOUNT_ID = ? and MERCHANT_ID = ?";
		return getJdbcTemplate().update(sql, GrantUtils.getMD5Password(loginPassword), id, merchantId);
	}

	public int deleteMerchant(Integer id) {
		return getJdbcTemplate().update("update merchant set RECORD_STATUS = 0 where MERCHANT_ID = ?", id);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}