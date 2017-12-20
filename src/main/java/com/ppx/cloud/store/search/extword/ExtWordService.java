package com.ppx.cloud.store.search.extword;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.grant.common.GrantContext;


@Service
public class ExtWordService extends MyDaoSupport {
	
	public PageList<ExtWord> listTest(Page page, ExtWord bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		
		MyCriteria c = createCriteria("and").addAnd("EXT_WORD like ?", "%", bean.getExtWord(), "%");
		
		StringBuilder cSql = new StringBuilder("select count(*) from search_ext_word where MERCHANT_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select * from search_ext_word where MERCHANT_ID = ?").append(c);
		c.addPrePara(merchantId);
		
		List<ExtWord> list = queryPage(ExtWord.class, page, cSql, qSql, c.getParaList());
		return new PageList<ExtWord>(list, page);
	}
	
	public int insertExtWord(ExtWord bean) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		bean.setMerchantId(merchantId);
		return insert(bean, "MERCHANT_ID", "EXT_WORD");
	}

	
	public int deleteExtWord(String id) {
		int merchantId = GrantContext.getLoginAccount().getMerchantId();
		return getJdbcTemplate().update("delete from search_ext_word where MERCHANT_ID = ? and EXT_WORD = ?", merchantId, id);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
