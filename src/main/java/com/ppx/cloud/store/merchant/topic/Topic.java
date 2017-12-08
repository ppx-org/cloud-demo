package com.ppx.cloud.store.merchant.topic;

import com.ppx.cloud.common.jdbc.annotation.Id;

public class Topic {

	@Id
	private Integer topicId;

	private Integer merchantId;

	private String topicName;

	private Integer topicPrio;

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public Integer getTopicPrio() {
		return topicPrio;
	}

	public void setTopicPrio(Integer topicPrio) {
		this.topicPrio = topicPrio;
	}

}
