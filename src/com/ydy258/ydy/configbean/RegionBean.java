package com.ydy258.ydy.configbean;

import java.io.Serializable;

public class RegionBean implements Serializable {	
	private static final long serialVersionUID = 7648863797582939351L;
	private Long id;
	private String province;
	private String city;
	private String area;
	private boolean delete;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
}