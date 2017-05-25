package com.ydy258.ydy.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TCommBankAccount entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_transaction_llzf_log", catalog = "hyt")
public class TTransactionLlzfLog implements java.io.Serializable {

	// Fields

	private Long id;
	private String oidPartner;
	private String signType;
	private String sign;
	private String dtOrder;
	private String noOrder;
	private String oidPaybill;
	private String moneyOrder;
	private String resultPay;
	private Date settleDate;
	private String infoOrder;
	private String payType;
	private String bankCode;
	private String noAgree;
	private String idType;
	private String idNo;
	private String acctName;
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

	@Column(name = "oid_partner", nullable = false, length = 18)
	public String getOidPartner() {
		return this.oidPartner;
	}

	public void setOidPartner(String oidPartner) {
		this.oidPartner = oidPartner;
	}

	@Column(name = "sign_type", nullable = false)
	public String getSignType() {
		return this.signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	@Column(name = "sign", nullable = false)
	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Column(name = "dt_order", nullable = false)
	public String getDtOrder() {
		return this.dtOrder;
	}

	public void setDtOrder(String dtOrder) {
		this.dtOrder = dtOrder;
	}

	@Column(name = "no_order", nullable = false)
	public String getNoOrder() {
		return this.noOrder;
	}

	public void setNoOrder(String noOrder) {
		this.noOrder = noOrder;
	}

	@Column(name = "oid_paybill", nullable = false)
	public String getOidPaybill() {
		return this.oidPaybill;
	}

	public void setOidPaybill(String oidPaybill) {
		this.oidPaybill = oidPaybill;
	}

	@Column(name = "money_order", nullable = false)
	public String getMoneyOrder() {
		return this.moneyOrder;
	}

	public void setMoneyOrder(String moneyOrder) {
		this.moneyOrder = moneyOrder;
	}

	@Column(name = "result_pay", nullable = false)
	public String getResultPay() {
		return this.resultPay;
	}

	public void setResultPay(String resultPay) {
		this.resultPay = resultPay;
	}

	@Column(name = "settle_date", length = 19)
	public Date getSettleDate() {
		return this.settleDate;
	}

	public void setSettleDate(Date settleDate) {
		this.settleDate = settleDate;
	}

	@Column(name = "info_order")
	public String getInfoOrder() {
		return this.infoOrder;
	}

	public void setInfoOrder(String infoOrder) {
		this.infoOrder = infoOrder;
	}

	@Column(name = "pay_type")
	public String getPayType() {
		return this.payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@Column(name = "bank_code")
	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Column(name = "no_agree")
	public String getNoAgree() {
		return this.noAgree;
	}

	public void setNoAgree(String noAgree) {
		this.noAgree = noAgree;
	}

	@Column(name = "id_type")
	public String getIdType() {
		return this.idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	@Column(name = "id_no")
	public String getIdNo() {
		return this.idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	@Column(name = "acct_name")
	public String getAcctName() {
		return this.acctName;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	@Column(name = "created_date", length = 19)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}