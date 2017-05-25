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
@Table(name = "proxy_statements")
public class ProxyStatements implements java.io.Serializable {

	// Fields
	private Long id;
	private long sysUserId;
	private String sysUserName;
	private Long proxyUserId;
	private String proxyUserName;
	private long memberId;
	private String memberName;
	private short userType;
	private int years;
	private Date createdDate;
	
	private boolean isSuccess;
	
	private int type;
	
	private double balance;
	
	private String orderNo;
	@Column()
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	@Column()
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	@Column()
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Column()
	 public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	@Id @GeneratedValue(strategy=IDENTITY)
	 @Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column()
	public long getSysUserId() {
		return sysUserId;
	}
	public void setSysUserId(long sysUserId) {
		this.sysUserId = sysUserId;
	}
	@Column()
	public String getSysUserName() {
		return sysUserName;
	}
	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}
	@Column()
	public Long getProxyUserId() {
		return proxyUserId;
	}
	public void setProxyUserId(Long proxyUserId) {
		this.proxyUserId = proxyUserId;
	}
	@Column()
	public String getProxyUserName() {
		return proxyUserName;
	}
	public void setProxyUserName(String proxyUserName) {
		this.proxyUserName = proxyUserName;
	}
	@Column()
	public long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	@Column()
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	@Column()
	public short getUserType() {
		return userType;
	}
	public void setUserType(short userType) {
		this.userType = userType;
	}
	@Column()
	public int getYears() {
		return years;
	}
	public void setYears(int years) {
		this.years = years;
	}
	@Column()
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
