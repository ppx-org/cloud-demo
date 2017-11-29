package com.ppx.cloud.grant.service;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.bean.Merchant;
import com.ppx.cloud.grant.bean.MerchantAccount;
import com.ppx.cloud.grant.common.GrantUtils;

@Service
public class MerchantService extends MyDaoSupport {

	public PageList<Merchant> listMerchant(Page page, Merchant mer) {
		MyCriteria c = createCriteria("and").addAnd("m.MERCHANT_NAME like ?", "%", mer.getMerchantName(), "%");
		
		String sql = "from merchant m left join merchant_account a on m.MERCHANT_ID = a.ACCOUNT_ID where m.RECORD_STATUS = ?";
		StringBuilder cSql = new StringBuilder("select count(*) ").append(sql).append(c);
		StringBuilder qSql = new StringBuilder("select m.*, a.LOGIN_ACCOUNT ").append(sql).append(c).append(" order by m.MERCHANT_ID");
		c.addPrePara(1);
		List<Merchant> list = queryPage(Merchant.class, page, cSql, qSql, c.getParaList());

		return new PageList<Merchant>(list, page);
	}

	@Transactional
	public int insertMerchant(Merchant bean) {
		MerchantAccount account = new MerchantAccount();
		account.setMerchantId(-1);
		account.setLoginAccount(bean.getLoginAccount());
		String pw = GrantUtils.getMD5Password(bean.getLoginPassword());
		account.setLoginPassword(pw);
		int accountR = insert(account, "LOGIN_ACCOUNT");
		if (accountR == 0) {
			// 帐号存在
			return -1;
		}
		
		int merchantId = getLastInsertId();
		bean.setMerchantId(merchantId);
		int merchantR = insert(bean, "MERCHANT_NAME");
		if (merchantR == 0) {
			// 名称存在
			return -2;
		}
		
		getJdbcTemplate().update("update merchant_account set merchant_id = ? where account_id = ?", merchantId, merchantId);
		
		return 1;
	}

	public Merchant getMerchant(Integer id) {
		Merchant bean = getJdbcTemplate().queryForObject("select * from merchant where MERCHANT_ID = ?",
				BeanPropertyRowMapper.newInstance(Merchant.class), id);
		return bean;
	}
	
	public MerchantAccount getMerchantAccount(Integer id) {
		MerchantAccount bean = getJdbcTemplate().queryForObject("select * from merchant_account where ACCOUNT_ID = ?",
				BeanPropertyRowMapper.newInstance(MerchantAccount.class), id);
		return bean;
	}

	public int updateMerchant(Merchant bean) {
		return update(bean, "MERCHANT_NAME");
	}
	
	public int updateMerchantAccount(MerchantAccount bean) {
		return update(bean, "LOGIN_ACCOUNT");
	}
	
	public int updateMerchantPassword(Integer id, String merchantPassword) {
		String sql = "update merchant_account set LOGIN_PASSWORD = ? where ACCOUNT_ID = ?";
		return getJdbcTemplate().update(sql, GrantUtils.getMD5Password(merchantPassword), id);
	}

	public int deleteMerchant(Integer id) {
		return getJdbcTemplate().update("update merchant set RECORD_STATUS = 0 where MERCHANT_ID = ?", id);
	}

	
	
	
	
	
	
	
	
	
	
}