package com.ydy258.ydy.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TDriverTransactionStatements entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_proxy_transaction_order")
public class TProxyTransactionOrder implements java.io.Serializable {

	// Fields

	private Long id;
	private String orderNo;
	private Long sysUserId;
	private String sysUserName;
	private Long proxyUserId;
	private String proxyUserName;
	private Double balance;
	private Integer tradeType;
	private String remark;
	private Date createdDate;
	@Id @GeneratedValue(strategy=IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	@Column
	public Long getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(Long sysUserId) {
		this.sysUserId = sysUserId;
	}
	@Column
	public String getSysUserName() {
		return sysUserName;
	}
	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}
	@Column
	public Long getProxyUserId() {
		return proxyUserId;
	}
	public void setProxyUserId(Long proxyUserId) {
		this.proxyUserId = proxyUserId;
	}
	@Column
	public String getProxyUserName() {
		return proxyUserName;
	}
	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}
	@Column
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	@Column
	public Integer getTradeType() {
		return tradeType;
	}
	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}
	@Column
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
