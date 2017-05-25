package com.ydy258.ydy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_cargo_user")
public class CargoUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7975432264219454906L;
	

	private Long id;
	private String mobile;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(length = 12, nullable = false)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}