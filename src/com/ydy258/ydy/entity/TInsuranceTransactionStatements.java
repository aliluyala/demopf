package com.ydy258.ydy.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TDriverTransactionStatements entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_insurance_transaction_statements", catalog = "hyt")
public class TInsuranceTransactionStatements implements java.io.Serializable {

	// Fields

	private Long id;
	private Long accountId;
	private Double pay;
	private Double deposit;
	private Double balance;
	private Integer tradeType;
	private String remark;
	private Date createdDate;

	// Property accessors
	@Id
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

	@Column(name = "pay", precision = 22, scale = 0)
	public Double getPay() {
		return this.pay;
	}

	public void setPay(Double pay) {
		this.pay = pay;
	}

	@Column(name = "deposit", precision = 22, scale = 0)
	public Double getDeposit() {
		return this.deposit;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
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