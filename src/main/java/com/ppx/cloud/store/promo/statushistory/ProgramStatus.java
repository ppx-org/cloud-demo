package com.ppx.cloud.store.promo.statushistory;

import java.util.Date;

import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.common.jdbc.annotation.Table;
import com.ppx.cloud.store.common.dictionary.Dict;

@Table(name="program_status_history")
public class ProgramStatus {
	
	@Id
	private Integer historyProgStatusId;
	
	private Integer progId;
	
	private Integer historyProgStatus;
	
	private Date created;
	
	private Integer creator;

	public Integer getHistoryProgStatusId() {
		return historyProgStatusId;
	}

	public void setHistoryProgStatusId(Integer historyProgStatusId) {
		this.historyProgStatusId = historyProgStatusId;
	}

	public Integer getProgId() {
		return progId;
	}

	public void setProgId(Integer progId) {
		this.progId = progId;
	}

	public Integer getHistoryProgStatus() {
		return historyProgStatus;
	}

	public void setHistoryProgStatus(Integer historyProgStatus) {
		this.historyProgStatus = historyProgStatus;
	}
	
	public String getHistoryProgStatusDesc() {
		return Dict.getProgStatusDesc(historyProgStatus);
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

}
	
	
	

