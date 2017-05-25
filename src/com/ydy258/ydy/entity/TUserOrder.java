package com.ydy258.ydy.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * TCargoUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_user_order"
)

public class TUserOrder  implements java.io.Serializable {

    // Fields    
     private Long id;
	 private Long userId;
     private Short userType;
     private String noOrder;
     private Short status;
     private Date createdTime;
     
     private Integer rechargeType;
     
     @Column(name="recharge_type")
     public Integer getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(Integer rechargeType) {
		this.rechargeType = rechargeType;
	}

	@Id @GeneratedValue(strategy=IDENTITY)
     @Column(name="id", unique=true, nullable=false)
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
	@Column(name="user_type")
	public Short getUserType() {
		return userType;
	}
	public void setUserType(Short userType) {
		this.userType = userType;
	}
	 @Column(name="no_order")
	public String getNoOrder() {
		return noOrder;
	}
	public void setNoOrder(String noOrder) {
		this.noOrder = noOrder;
	}
	 @Column(name="status")
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	@Column(name="created_time", length=19)
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}



}
