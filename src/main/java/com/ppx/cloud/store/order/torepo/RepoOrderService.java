package com.ppx.cloud.store.order.torepo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.storecommon.order.bean.OrderItem;

@Service
public class RepoOrderService extends MyDaoSupport {
	
	
	public PageList<OrderItem> listOrderItemByRepo(Page page, Integer repoId, OrderItem bean) {
				
		MyCriteria c = createCriteria("and").addAnd("ITEM_ID = ?", bean.getItemId());
					
		StringBuilder cSql = new StringBuilder("select count(*) from order_item i join product p on i.PROD_ID = p.PROD_ID and p.REPO_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select i.* from order_item i join product p on i.PROD_ID = p.PROD_ID and p.REPO_ID = ?")
				.append(c).append(" order by i.ORDER_ID desc, i.ITEM_ID desc");
		c.addPrePara(repoId);
		
		List<OrderItem> list = queryPage(OrderItem.class, page, cSql, qSql, c.getParaList());
		return new PageList<OrderItem>(list, page);
	}
	
}
