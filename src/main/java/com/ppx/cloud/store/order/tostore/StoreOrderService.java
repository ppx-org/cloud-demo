package com.ppx.cloud.store.order.tostore;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyCriteria;
import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.common.page.Page;
import com.ppx.cloud.common.page.PageList;
import com.ppx.cloud.storecommon.order.bean.UserOrder;

@Service
public class StoreOrderService extends MyDaoSupport {
	
	
	public PageList<UserOrder> listOrder(Page page, UserOrder bean) {
				
		MyCriteria c = createCriteria("and").addAnd("ORDER_ID = ?", bean.getOrderId());
					
		StringBuilder cSql = new StringBuilder("select count(*) from user_order where STORE_ID = ?").append(c);
		StringBuilder qSql = new StringBuilder("select * from user_order where STORE_ID = ?")
				.append(c).append(" order by ORDER_ID desc");
		c.addPrePara(bean.getStoreId());
		
		List<UserOrder> list = queryPage(UserOrder.class, page, cSql, qSql, c.getParaList());
		return new PageList<UserOrder>(list, page);
	}
	
}
