package com.ydy258.ydy.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * SysUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ddcxwy_insurance")
public class DdcxwyInsurance implements java.io.Serializable {

	// Fields

	private Long id;
	private String orderNo;
	private Integer count;
	private Double balance;
	private Date createdDate;
	private Long proxyId;
	private String proxyName;
	private Long saleUserId;
	private String saleUserName;
	private String xczPath;
	private String bdPath;
	private int status;
	
	private String name;
	private String mobile;
	private String idCard;
	private Date startTime;
	private Integer type;
	
	@Column()
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column()
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column()
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Column()
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	@Column()
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column()
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	@Column()
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	@Column()
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	@Column()
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Column()
	public String getXczPath() {
		return xczPath;
	}
	public void setXczPath(String xczPath) {
		this.xczPath = xczPath;
	}
	@Column()
	public String getBdPath() {
		return bdPath;
	}
	public void setBdPath(String bdPath) {
		this.bdPath = bdPath;
	}
	@Column()
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column()
	public Long getProxyId() {
		return proxyId;
	}
	public void setProxyId(Long proxyId) {
		this.proxyId = proxyId;
	}
	@Column()
	public String getProxyName() {
		return proxyName;
	}
	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}
	@Column()
	public Long getSaleUserId() {
		return saleUserId;
	}
	public void setSaleUserId(Long saleUserId) {
		this.saleUserId = saleUserId;
	}
	@Column()
	public String getSaleUserName() {
		return saleUserName;
	}
	public void setSaleUserName(String saleUserName) {
		this.saleUserName = saleUserName;
	}
}