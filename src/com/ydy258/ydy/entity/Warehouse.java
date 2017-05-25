package com.ydy258.ydy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_warehouse")
public class Warehouse implements Serializable {
	private static final long serialVersionUID = 7851422753887302289L;
	private Long id;
	private Long userId; // 用户ID
	private String place; // 落货地点
	private String contact; // 联系人
	private String phone; // 联系电话
	private String address; // 详细地址
	private String picPath;

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="pic_path",length=500)
	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	
	@Column(name="user_id", nullable=false)
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name="place", length=64)
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Column(name="contact", length=32)
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Column(name="phone", length=64)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name="address", length=200)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}