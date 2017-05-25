package com.ydy258.ydy.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TDriverTransactionStatements entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_user_recharge_request")
public class UserRechargeRequest implements java.io.Serializable {

	// Fields

	private Long id;
	private String orderNo;
	private Long userId;
	private int userType;
	private Double rechargeMuch;
	private Integer tradeType;
	private String remark;
	private Date createdDate;
	private Long payPoint;
	private Long returnPoint;
	private Double specialBalance;
	private Double rechargeTotal;
	@Column
	public Double getRechargeTotal() {
		return rechargeTotal;
	}
	public void setRechargeTotal(Double rechargeTotal) {
		this.rechargeTotal = rechargeTotal;
	}
	private Boolean isSuccess;
	@Column
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	@Column
	public Double getSpecialBalance() {
		return specialBalance;
	}
	public void setSpecialBalance(Double specialBalance) {
		this.specialBalance = specialBalance;
	}
	@Column
	public Long getPayPoint() {
		return payPoint;
	}
	public void setPayPoint(Long payPoint) {
		this.payPoint = payPoint;
	}
	@Column
	public Long getReturnPoint() {
		return returnPoint;
	}
	public void setReturnPoint(Long returnPoint) {
		this.returnPoint = returnPoint;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Column
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	@Column
	public Double getRechargeMuch() {
		return rechargeMuch;
	}
	public void setRechargeMuch(Double rechargeMuch) {
		this.rechargeMuch = rechargeMuch;
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
