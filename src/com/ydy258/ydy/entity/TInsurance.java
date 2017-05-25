package com.ydy258.ydy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_cargo_insurance")
public class TInsurance implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3617126330666256082L;

	private Long id;

	private Long userId;
	
	private String policy;
	private String policyNo;
	private String primaryNo;
	private String insurance;
	private String mainGlauses;
	private String mainGlausesCode;
	private String additive;
	private String additiveNo;
	private String holderName;
	private String holderAddr;
	private String recognizeeName;
	private String recognizeeAddr;
	private String invRefNo;
	private String packQty;
	private String goodsName;
	private String goodsTypeNo;
	public String getGoodsTypeNo() {
		return goodsTypeNo;
	}
	public void setGoodsTypeNo(String goodsTypeNo) {
		this.goodsTypeNo = goodsTypeNo;
	}
	private String transportType;
	private String transport;
	private String transportNo;
	private String fromLoc;
	private String viaLoc;
	private String toLoc;
	private String deductible;
	private String departureDate;
	private String effDate;
	private String currencyID;
	private String insuredAmount;
	private String ratio;
	private String specialAnnounce;
	private String endCurrencyID;
	
	private Double price;
	
	private String recognizeePhone;
	
	private String userType;
	
	private String orderNo;
	
	private String policyPath;
	
	private String retInfo;
	
	private Double cost;
	
	private Long points;
	private Boolean isDiscount;
	private Double weight;
	private String cardNo;
	private String weigthOrCount;
	private Double srcPrice;
	private Long proxyUserId;
	private Double proxyPrice;
	
	
	private String truckType;
	private String truckWeight;
	private String cargoNo;
	
	private String policyNoLong;
	
	
	private Integer days;
	private String endDate;
	
	private Integer personsCount;
	
	private String registerNo;
	
	private Long parentId;
	
	private String parentOrderNo;
	
	private String lisencePath;
	
	@Column(name="lisence_path")
	public String getLisencePath() {
		return lisencePath;
	}
	public void setLisencePath(String lisencePath) {
		this.lisencePath = lisencePath;
	}
	@Column()
	public String getParentOrderNo() {
		return parentOrderNo;
	}
	public void setParentOrderNo(String parentOrderNo) {
		this.parentOrderNo = parentOrderNo;
	}
	@Column(name="parent_id")
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	@Column(name="register_no")
	public String getRegisterNo() {
		return registerNo;
	}
	public void setRegisterNo(String registerNo) {
		this.registerNo = registerNo;
	}
	
	@Column()
	public Integer getPersonsCount() {
		return personsCount;
	}
	public void setPersonsCount(Integer personsCount) {
		this.personsCount = personsCount;
	}
	@Column()
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	@Column()
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	@Column(name = "policy_no_long")
	public String getPolicyNoLong() {
		return policyNoLong;
	}
	public void setPolicyNoLong(String policyNoLong) {
		this.policyNoLong = policyNoLong;
	}
	
	@Column()
	public String getTruckType() {
		return truckType;
	}
	public void setTruckType(String truckType) {
		this.truckType = truckType;
	}
	@Column()
	public String getTruckWeight() {
		return truckWeight;
	}
	public void setTruckWeight(String truckWeight) {
		this.truckWeight = truckWeight;
	}
	@Column()
	public String getCargoNo() {
		return cargoNo;
	}
	public void setCargoNo(String cargoNo) {
		this.cargoNo = cargoNo;
	}
	@Column()
	public Long getPoints() {
		return points;
	}
	public void setPoints(Long points) {
		this.points = points;
	}
	@Column()
	public Boolean isDiscount() {
		return isDiscount;
	}
	public void setDiscount(Boolean isDiscount) {
		this.isDiscount = isDiscount;
	}
	@Column()
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	@Column()
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	@Column()
	public String getWeigthOrCount() {
		return weigthOrCount;
	}
	public void setWeigthOrCount(String weigthOrCount) {
		this.weigthOrCount = weigthOrCount;
	}
	@Column()
	public Double getSrcPrice() {
		return srcPrice;
	}
	public void setSrcPrice(Double srcPrice) {
		this.srcPrice = srcPrice;
	}
	@Column()
	public Long getProxyUserId() {
		return proxyUserId;
	}
	public void setProxyUserId(Long proxyUserId) {
		this.proxyUserId = proxyUserId;
	}
	@Column()
	public Double getProxyPrice() {
		return proxyPrice;
	}
	public void setProxyPrice(Double proxyPrice) {
		this.proxyPrice = proxyPrice;
	}
	@Column()
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	@Column(name = "ret_info")
	public String getRetInfo() {
		return retInfo;
	}
	public void setRetInfo(String retInfo) {
		this.retInfo = retInfo;
	}
	@Column()
	public String getPolicyPath() {
		return policyPath;
	}
	public void setPolicyPath(String policyPath) {
		this.policyPath = policyPath;
	}
	@Column(name = "order_no")
	public String getOrderNo() {
		return orderNo;
	}
	
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	@Column(name = "policyNo")
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	@Column(name = "userType")
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	@Column(name = "recognizeePhone")
	public String getRecognizeePhone() {
		return recognizeePhone;
	}
	public void setRecognizeePhone(String recognizeePhone) {
		this.recognizeePhone = recognizeePhone;
	}
	@Column(name = "price")
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "userId")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Column(name = "policy")
	public String getPolicy() {
		return policy;
	}
	public void setPolicy(String policy) {
		this.policy = policy;
	}
	@Column(name = "primaryNo")
	public String getPrimaryNo() {
		return primaryNo;
	}
	public void setPrimaryNo(String primaryNo) {
		this.primaryNo = primaryNo;
	}
	@Column(name = "insurance")
	public String getInsurance() {
		return insurance;
	}
	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}
	@Column(name = "mainGlauses")
	public String getMainGlauses() {
		return mainGlauses;
	}
	public void setMainGlauses(String mainGlauses) {
		this.mainGlauses = mainGlauses;
	}
	@Column(name = "mainGlausesCode")
	public String getMainGlausesCode() {
		return mainGlausesCode;
	}
	public void setMainGlausesCode(String mainGlausesCode) {
		this.mainGlausesCode = mainGlausesCode;
	}
	@Column(name = "additive")
	public String getAdditive() {
		return additive;
	}
	public void setAdditive(String additive) {
		this.additive = additive;
	}
	@Column(name = "additiveNo")
	public String getAdditiveNo() {
		return additiveNo;
	}
	public void setAdditiveNo(String additiveNo) {
		this.additiveNo = additiveNo;
	}
	@Column(name = "holderName")
	public String getHolderName() {
		return holderName;
	}
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
	@Column(name = "holderAddr")
	public String getHolderAddr() {
		return holderAddr;
	}
	public void setHolderAddr(String holderAddr) {
		this.holderAddr = holderAddr;
	}
	@Column(name = "recognizeeName")
	public String getRecognizeeName() {
		return recognizeeName;
	}
	public void setRecognizeeName(String recognizeeName) {
		this.recognizeeName = recognizeeName;
	}
	@Column(name = "recognizeeAddr")
	public String getRecognizeeAddr() {
		return recognizeeAddr;
	}
	public void setRecognizeeAddr(String recognizeeAddr) {
		this.recognizeeAddr = recognizeeAddr;
	}
	@Column(name = "invRefNo")
	public String getInvRefNo() {
		return invRefNo;
	}
	public void setInvRefNo(String invRefNo) {
		this.invRefNo = invRefNo;
	}
	@Column(name = "packQty")
	public String getPackQty() {
		return packQty;
	}
	public void setPackQty(String packQty) {
		this.packQty = packQty;
	}
	@Column(name = "goodsName")
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	@Column(name = "transportType")
	public String getTransportType() {
		return transportType;
	}
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}
	@Column(name = "transport")
	public String getTransport() {
		return transport;
	}
	public void setTransport(String transport) {
		this.transport = transport;
	}
	@Column(name = "transportNo")
	public String getTransportNo() {
		return transportNo;
	}
	public void setTransportNo(String transportNo) {
		this.transportNo = transportNo;
	}
	@Column(name = "fromLoc")
	public String getFromLoc() {
		return fromLoc;
	}
	public void setFromLoc(String fromLoc) {
		this.fromLoc = fromLoc;
	}
	@Column(name = "viaLoc")
	public String getViaLoc() {
		return viaLoc;
	}
	public void setViaLoc(String viaLoc) {
		this.viaLoc = viaLoc;
	}
	@Column(name = "toLoc")
	public String getToLoc() {
		return toLoc;
	}
	public void setToLoc(String toLoc) {
		this.toLoc = toLoc;
	}
	@Column(name = "deductible")
	public String getDeductible() {
		return deductible;
	}
	public void setDeductible(String deductible) {
		this.deductible = deductible;
	}
	@Column(name = "departureDate")
	public String getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}
	@Column(name = "effDate")
	public String getEffDate() {
		return effDate;
	}
	public void setEffDate(String effDate) {
		this.effDate = effDate;
	}
	@Column(name = "currency_id")
	public String getCurrencyID() {
		return currencyID;
	}
	public void setCurrencyID(String currencyID) {
		this.currencyID = currencyID;
	}
	@Column(name = "insuredAmount")
	public String getInsuredAmount() {
		return insuredAmount;
	}
	public void setInsuredAmount(String insuredAmount) {
		this.insuredAmount = insuredAmount;
	}
	@Column(name = "ratio")
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	@Column(name = "specialAnnounce")
	public String getSpecialAnnounce() {
		return specialAnnounce;
	}
	public void setSpecialAnnounce(String specialAnnounce) {
		this.specialAnnounce = specialAnnounce;
	}
	@Column(name = "endCurrencyID")
	public String getEndCurrencyID() {
		return endCurrencyID;
	}
	public void setEndCurrencyID(String endCurrencyID) {
		this.endCurrencyID = endCurrencyID;
	}
	@Column(name = "company")
	public Integer getCompany() {
		return company;
	}
	
	public void setCompany(Integer company) {
		this.company = company;
	}
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "created_date")
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	private Integer company;
	private Integer status;
	private Date created_date;

}
