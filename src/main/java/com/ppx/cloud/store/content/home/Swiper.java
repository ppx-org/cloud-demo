package com.ppx.cloud.store.content.home;

import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.common.jdbc.annotation.Table;


@Table(name="home_swiper")
public class Swiper {
	
	@Id
	private Integer swiperId;
	
	private Integer storeId;
	
	private String swiperImg;
	
	private Integer swiperPrio;

	public Integer getSwiperId() {
		return swiperId;
	}

	public void setSwiperId(Integer swiperId) {
		this.swiperId = swiperId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getSwiperImg() {
		return swiperImg;
	}

	public void setSwiperImg(String swiperImg) {
		this.swiperImg = swiperImg;
	}

	public Integer getSwiperPrio() {
		return swiperPrio;
	}

	public void setSwiperPrio(Integer swiperPrio) {
		this.swiperPrio = swiperPrio;
	}

	
	
}
