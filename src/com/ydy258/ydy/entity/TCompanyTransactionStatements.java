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
@Table(name = "t_company_transaction_statements")
public class TCompanyTransactionStatements implements java.io.Serializable {

	// Fields

	private Long id;
	private Long accountId;
	private Double pay;
	private Double deposit;
	private Integer tradeType;
	private String remark;
	private Date createdDate;

	/** ���**/
 	private Long payPoints;
 	/**ssʣ�»��**/
 	private Long restPoints;
 	
 	private String orderNo;
 	
	private Short userType;
	
	private Boolean isTurnout = false;	
	
	private Double cost = 0.0;	//�ɱ�
	
	private Double  profit = 0.0;//����
 	
	private Double proxyPrice;
	private Long proxyUserId;
	private boolean isSuccess;
	
	private Integer payType=1;
	@Column(name="pay_type")
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	@Column()
	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
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

	@Column(name = "cost")
 	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Column(name = "profit")
	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	@Column(name = "is_turnout")
 	public Boolean getIsTurnout() {
		return isTurnout;
	}

	public void setIsTurnout(Boolean isTurnout) {
		this.isTurnout = isTurnout;
	}

	@Column(name = "user_type")
	public Short getUserType() {
		return userType;
	}

	public void setUserType(Short userType) {
		this.userType = userType;
	}

	// Constructors
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
	// Constructors

	/** default constructor */
	public TCompanyTransactionStatements() {
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
