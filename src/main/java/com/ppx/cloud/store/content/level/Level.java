package com.ppx.cloud.store.content.level;

import com.ppx.cloud.common.jdbc.annotation.Id;
import com.ppx.cloud.common.jdbc.annotation.Table;


@Table(name="home_level")
public class Level {
	
	@Id
	private Integer levelId;
	
	private Integer storeId;
	
	private String levelName;
	
	private Integer LevelPrio;

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public Integer getLevelPrio() {
		return LevelPrio;
	}

	public void setLevelPrio(Integer levelPrio) {
		LevelPrio = levelPrio;
	}

	
	
}
