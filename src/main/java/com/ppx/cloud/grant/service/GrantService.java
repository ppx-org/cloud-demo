package com.ppx.cloud.grant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.grant.bean.Merchant;
import com.ppx.cloud.grant.bean.MerchantAccount;
import com.ppx.cloud.grant.common.CacheConfig;
import com.ppx.cloud.grant.common.GrantContext;
import com.ppx.cloud.grant.filter.GrantFilterService;

@Service
public class GrantService extends MyDaoSupport {
	
	@Autowired
	@Qualifier("configMongoTemplate")
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private GrantFilterService filterService;
	
	public List<Merchant> listMerchant(Page page, Merchant mer) {
		MyCriteria c = createCriteria("where")
			.addAnd("m.MERCHANT_ID = ?", mer.getMerchantId())
			.addAnd("m.MERCHANT_NAME like ?", "%", mer.getMerchantName(), "%");
		
		StringBuilder cSql = new StringBuilder("select count(*) from merchant m left join merchant_account a on m.MERCHANT_ID = a.ACCOUNT_ID").append(c);
		StringBuilder qSql = new StringBuilder("select m.MERCHANT_ID, m.MERCHANT_NAME, a.LOGIN_ACCOUNT " +
			" from merchant m left join merchant_account a on m.MERCHANT_ID = a.ACCOUNT_ID")
			.append(c).append(" order by m.MERCHANT_ID");
		return queryPage(Merchant.class, page, cSql, qSql, c.getParaList());
	}
	
	public List<MerchantAccount> listChildAccount(Page page, MerchantAccount account) {
		List<Object> paraList = new ArrayList<Object>();
		paraList.add(GrantContext.getLoginAccount().getMerchantId());
		
		MyCriteria c = createCriteria("and")
			.addAnd("a.ACCOUNT_ID = ?", account.getAccountId())
			.addAnd("a.LOGIN_ACCOUNT like ?", "%", account.getMerchantName(), "%");
		
		StringBuilder cSql = new StringBuilder("select count(*) from merchant_accoun where MERCHANT_ID = ? and MERCHANT_ID != ACCOUNT_ID").append(c);
		StringBuilder qSql = new StringBuilder("select * from merchant_account a where a.MERCHANT_ID = ? and MERCHANT_ID != ACCOUNT_ID")
			.append(c).append(" order by a.MERCHANT_ID");
		
		paraList.addAll(c.getParaList());
		return queryPage(MerchantAccount.class, page, cSql, qSql, paraList);
	}
	
	
	public Map<?, ?> getAuthorize(Integer accountId) {		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(accountId));
		return mongoTemplate.findOne(query, Map.class, "grant_authorize");
	}
	
	public void saveAuthorize(Integer accountId, String resIds) {
		// 清除过滤器中缓存
		filterService.clearCache(CacheConfig.MER_BIT_SET_CACHE);
		filterService.clearCache(CacheConfig.MER_RES_CACHE);
				
		List<Integer> resIdArray = new ArrayList<Integer>();
		if (!StringUtils.isEmpty(resIds)) {
			String[] s = resIds.split(",");
			for (String resId : s) {
				resIdArray.add(Integer.parseInt(resId));
			}
		}
		
		Update update = new Update();		
		update.set("resIds", resIdArray);
		
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(accountId));
		mongoTemplate.upsert(query, update, "grant_authorize");
	}
	
	

	
	
}