package com.ydy258.ydy.entity;

/**
 * 商户柜员实体
 * 2015.11.05 by mickeylan
 */

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_third_mall_teller")
public class ThirdMallTeller implements Serializable {
	private static final long serialVersionUID = 6721198869320312826L;
	private Long id;
	private String tellerId; // 柜员id
	private Long storeId; // 商户ID
	private String tellerName; // 柜员姓名
	private String password; // 登录密码
	private String jpushId; // 登录手机的极光推送ID
	private String deviceId; // 登录手机的设备ID
	private Date lastLogin; // 最后一次登录时间
	private String lastIp; // 登录时的IP
	private Boolean isLock = false;	//锁定标志	
	private Date createdDate;

	@Column()
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length=32,nullable=false)
	public String getTellerId() {
		return tellerId==null?"":tellerId;
	}

	public void setTellerId(String tellerId) {
		this.tellerId = tellerId;
	}

	@Column(nullable=false)
	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	@Column(nullable=false)
	public String getTellerName() {
		return tellerName==null?"":tellerName;
	}

	public void setTellerName(String tellerName) {
		this.tellerName = tellerName;
	}

	@Column(length=32, nullable=false)
	public String getPassword() {
		return password==null?"":password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(length=30, nullable=false)
	public String getJpushId() {
		return jpushId==null?"":jpushId;
	}

	public void setJpushId(String jpushId) {
		this.jpushId = jpushId;
	}

	@Column(length=32)
	public String getDeviceId() {
		return deviceId==null?"":deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column()
	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	@Column(length=16, nullable=false)
	public String getLastIp() {
		return lastIp==null?"":lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}
	
	@Column()
	public Boolean getIsLock() {
		return isLock;
	}

	public void setIsLock(Boolean isLock) {
		this.isLock = isLock;
	}
}