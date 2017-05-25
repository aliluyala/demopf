package com.ydy258.ydy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 货运专线实体类
 * 
 * @author mickeylan
 * 
 */
@Entity
@Table(name = "t_freight_line")
public class FreightLine implements Serializable {
	private static final long serialVersionUID = -7193802036670300100L;
	private Long id;
	private Long userId;
	private String origin;
	private String destination;
	private String phone;	//联系电话	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="user_id", nullable=false)
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Column(name="origin", length=64)
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Column(name="destination", length=64)
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	@Column(name="phone", length=255)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
