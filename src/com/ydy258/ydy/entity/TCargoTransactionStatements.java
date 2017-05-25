package com.ydy258.ydy.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TCargoTransactionStatements entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_cargo_transaction_statements")
public class TCargoTransactionStatements implements java.io.Serializable {

	// Fields

	private Long id;
	private Long accountId;
	private Double pay;
	private Double deposit;
	private Double balance;
	private Integer tradeType;
	private String remark;
	private Date createdDate;
	
	private Double proxyPrice;
	private Long proxyUserId;
	
	private Integer payType=1;
	@Column(name="pay_type")
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	@Column()
	public Double getProxyPrice() {
		return proxyPrice;
	}

	public void setProxyPrice(Double proxyPrice) {
		this.proxyPrice = proxyPrice;
	}

	@Column()
	public Long getProxyUserId() {
		return proxyUserId;
	}

	public void setProxyUserId(Long proxyUserId) {
		this.proxyUserId = proxyUserId;
	}

	/** ���**/
 	private Long payPoints=0L;
 	/**ssʣ�»��**/
 	private Long restPoints=0L;
	
 	private String orderNo;
	// Constructors
 	private Boolean isTurnout = false;	
 	
 	@Column(name = "is_turnout")
 	public Boolean getIsTurnout() {
		return isTurnout;
	}

	public void setIsTurnout(Boolean isTurnout) {
		this.isTurnout = isTurnout;
	}

	@Column(name = "order_no")
 	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name = "rest_points")
 	public Long getRestPoints() {
		return restPoints;
	}

	public void setRestPoints(Long restPoints) {
		this.restPoints = restPoints;
	}

	@Column(name = "pay_points")
	public Long getPayPoints() {
		return payPoints;
	}

	public void setPayPoints(Long payPoints) {
		this.payPoints = payPoints;
	}

	/** default constructor */
	public TCargoTransactionStatements() {
	}

	/** minimal constructor */
	public TCargoTransactionStatements(Long id, Long accountId, Double balance,
			Integer tradeType) {
		this.id = id;
		this.accountId = accountId;
		this.balance = balance;
		this.tradeType = tradeType;
	}

	/** full constructor */
	public TCargoTransactionStatements(Long id, Long accountId, Double pay,
			Double deposit, Double balance, Integer tradeType, String remark,
			Date createdDate) {
		this.id = id;
		this.accountId = accountId;
		this.pay = pay;
		this.deposit = deposit;
		this.balance = balance;
		this.tradeType = tradeType;
		this.remark = remark;
		this.createdDate = createdDate;
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
