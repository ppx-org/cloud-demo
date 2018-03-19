package com.ppx.cloud.micro.log;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.ppx.cloud.common.jdbc.MyDaoSupport;
import com.ppx.cloud.micro.common.MGrantContext;
import com.ppx.cloud.micro.common.WxUser;


@Service
public class MLogService extends MyDaoSupport {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public int addPromoEntry(MLogPromo promo) {
		System.out.println("xxxxxxxxout:" + promo);
		WxUser u = MGrantContext.getWxUser();
		promo.setDate(new Date());
		promo.setOpenid(u.getOpenid());
		promo.setPromoCode(u.getPromoCode());
	
		
		mongoTemplate.insert(promo, "data_promo_entry");
		return 1;
	}
	
	
	
	
}
