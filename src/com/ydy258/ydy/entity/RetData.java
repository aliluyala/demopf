package com.ydy258.ydy.entity;

import java.util.List;

public class RetData {
	private String id;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<RetData> getSons() {
		return sons;
	}
	public void setSons(List<RetData> sons) {
		this.sons = sons;
	}
	private List<RetData> sons;
}
