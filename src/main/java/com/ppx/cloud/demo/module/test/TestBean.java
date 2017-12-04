package com.ppx.cloud.demo.module.test;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.common.jdbc.annotation.Table;
import com.ppx.cloud.common.util.DateUtils;


@Table(name="test")
public class TestBean {
	
	@Id
	private Integer testId;
	
	private String testName;
	
	private Integer testNumber;
	
	@JsonFormat(pattern=DateUtils.DATE_PATTERN)
	private Date testDate;
	
	private Date testTime;
	
	private String testDesc;

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Integer getTestNumber() {
		return testNumber;
	}

	public void setTestNumber(Integer testNumber) {
		this.testNumber = testNumber;
	}

	public Date getTestTime() {
		return testTime;
	}

	public void setTestTime(Date testTime) {
		this.testTime = testTime;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}
	
	

}
