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
@Table(name = "t_proxy_transaction_statements")
public class TProxyTransactionStatements implements java.io.Serializable {

	// Fields

	private Long id;
	private Long accountId;
	private Double balance;
	private Integer tradeType;
	private String remark;
	private Date createdDate;
 	private String orderNo;
 	private Boolean isTurnout = false;	
 	private Double pay;
 	private Long userId;//用户ID\
 	private Short userType;//用户类型
 	private String userName;//操作用户的名称
 	private String moblie;//电话号码
 	@Column
 	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Column
	public Short getUserType() {
		return userType;
	}
	public void setUserType(Short userType) {
		this.userType = userType;
	}
	@Column
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column
	public String getMoblie() {
		return moblie;
	}
	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}
	@Column
 	public Double getPay() {
		return pay;
	}
	public void setPay(Double pay) {
		this.pay = pay;
	}
	@Column(name = "is_turnout")
 	public Boolean getIsTurnout() {
		return isTurnout;
	}
	public void setIsTurnout(Boolean isTurnout) {
		this.isTurnout = isTurnout;
	}
	// Constructors
 	@Column(name = "order_no")
 	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	// Constructors

	/** default constructor */
	public TProxyTransactionStatements() {
	}

	/** minimal constructor */
	public TProxyTransactionStatements(Long id, Long accountId,
			Double balance, Integer tradeType) {
		this.id = id;
		this.accountId = accountId;
		this.balance = balance;
		this.tradeType = tradeType;
	}


	// Property accessors
	 @Id @GeneratedValue(strategy=IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "account_id", nullable = false)
	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}



	@Column(name = "balance", nullable = false, precision = 22, scale = 0)
	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Column(name = "trade_type", nullable = false)
	public Integer getTradeType() {
		return this.tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "created_date", length = 19)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
