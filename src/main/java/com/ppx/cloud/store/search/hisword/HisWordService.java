package com.ppx.cloud.store.search.hisword;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;


@Service
public class HisWordService extends MyDaoSupport {
	
	
	public List<HisWord> listHisWord(HisWord hisWord, Page page) {
		System.out.println("xxxxxxxxxout:" + hisWord.getHisWord());
	
		MyCriteria c = createCriteria("and").addAnd("HIS_WORD like ?", hisWord.getHisWord());
		
		StringBuilder cSql = new StringBuilder("select count(*) from search_history_word where STORE_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select * from search_history_word where STORE_ID = ?").append(c).append(" order by CREATED desc");
		
		c.addPrePara(hisWord.getStoreId());
		
		List<HisWord> list = queryPage(HisWord.class, page, cSql, qSql, c.getParaList());
		return list;
	}
	
	
}
