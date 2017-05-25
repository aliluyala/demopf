package com.ydy258.ydy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 第三方商户订单实体类
 * 
 * @author mickeylan
 * 
 */
@Entity
@Table(name = "t_third_mall_order")
public class ThirdMallOrder implements Serializable {
	private static final long serialVersionUID = 1217185864952224325L;
	private Long id;
	private String orderNo; // 订单号（流水号）
	private Long storeId; // 商户ID
	private String tellerId;	//柜员号
	private Long agencyId;	//代理ID
	private Long customId;	//消费者ID
	private String customName; // 消费者姓名
	private Long goodsId; // 消费商品ID
	private Double goodsNumber; // 消费数量
	private Double amount; // 总价格
	private Double cost;	//成本价
	private Double discount; // 折扣价（消费者实付金额）
	private Integer points; // 补贴额（补贴的驿道币）
	private Double agentProfit;	//本笔订单分给代理的利润((总价－成本价－抵扣的驿道币) * 代理利润率)
	private Date addTime; // 下单时间
	private Integer orderStatus; // 订单状态（待付款、成交、取消，...）
	private Date payTime; // 支付时间
	private Double rank1 = 0.0;	//服务态度评分
	private Double rank2 = 0.0;	//油品质量评分
	private Double rank3 = 0.0;	//油站环境评分    
	private String comment = "";	//评论	
	private Integer payType;	//支付方式（1-余额,2-微信,3-支付宝,4-连连）	
	private Boolean isPaySuccess=false;//是否支付成功
	
	private Integer returnPoints=0;//是否支付成功
	
	private Double special; // 返的专项费
	
	
	private Boolean isBill;//是否要发票
	
	//发票类
	private String receiveName;//收件人名称
	private String receiveMobile;//收件人电话
	private String receiveAdress;//收件人地址
	private String receiveAdressDtl;//收件 人地址详情
	
	private String billType;//发票类型 1：普通 2：增值
	private String billName;//抬头
	private String billRecognition;//纳税人识别号
	private String billAddress;//公司地址
	private String billAddressDtl;//公司地址详情
	private String billPhone;//公司电话
	private String bank;//开户银行
	private String bankAcc;//银行账号
	
	private Double mailCharge;
	private Double taxCharge;
	
	private Integer billStatus;//是否要发票
	@Column()
	public Boolean getIsBill() {
		return isBill;
	}

	public void setIsBill(Boolean isBill) {
		this.isBill = isBill;
	}

	public Integer getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}

	@Column()
	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	@Column()
	public String getReceiveMobile() {
		return receiveMobile;
	}

	public void setReceiveMobile(String receiveMobile) {
		this.receiveMobile = receiveMobile;
	}

	@Column()
	public String getReceiveAdress() {
		return receiveAdress;
	}

	public void setReceiveAdress(String receiveAdress) {
		this.receiveAdress = receiveAdress;
	}

	@Column()
	public String getReceiveAdressDtl() {
		return receiveAdressDtl;
	}

	public void setReceiveAdressDtl(String receiveAdressDtl) {
		this.receiveAdressDtl = receiveAdressDtl;
	}

	@Column()
	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	@Column()
	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	@Column()
	public String getBillRecognition() {
		return billRecognition;
	}

	public void setBillRecognition(String billRecognition) {
		this.billRecognition = billRecognition;
	}

	@Column()
	public String getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(String billAddress) {
		this.billAddress = billAddress;
	}

	@Column()
	public String getBillAddressDtl() {
		return billAddressDtl;
	}

	public void setBillAddressDtl(String billAddressDtl) {
		this.billAddressDtl = billAddressDtl;
	}

	@Column()
	public String getBillPhone() {
		return billPhone;
	}

	public void setBillPhone(String billPhone) {
		this.billPhone = billPhone;
	}

	@Column()
	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	@Column()
	public String getBankAcc() {
		return bankAcc;
	}

	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}

	@Column()
	public Double getMailCharge() {
		return mailCharge;
	}

	public void setMailCharge(Double mailCharge) {
		this.mailCharge = mailCharge;
	}

	@Column()
	public Double getTaxCharge() {
		return taxCharge;
	}

	public void setTaxCharge(Double taxCharge) {
		this.taxCharge = taxCharge;
	}

	@Column(name="special")
	public Double getSpecial() {
		return special;
	}

	public void setSpecial(Double special) {
		this.special = special;
	}
	@Column(name="pay_time")
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="return_points")
	public Integer getReturnPoints() {
		return returnPoints;
	}

	public void setReturnPoints(Integer returnPoints) {
		this.returnPoints = returnPoints;
	}

	@Column(name="pay_success")
	public Boolean getIsPaySuccess() {
		return isPaySuccess;
	}

	public void setIsPaySuccess(Boolean isPaySuccess) {
		this.isPaySuccess = isPaySuccess;
	}
	@Column(name="order_no", length=32, nullable=false)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name="store_id", length=8, nullable=false)
	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	
	@Column(name="teller_id", nullable=false)
	public String getTellerId() {
		return tellerId;
	}

	public void setTellerId(String tellerId) {
		this.tellerId = tellerId;
	}
	
	@Column(name="agency_id", nullable=false)
	public Long getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Long agencyId) {
		this.agencyId = agencyId;
	}

	@Column(name="custom_id", nullable=false)
	public Long getCustomId() {
		return customId;
	}

	public void setCustomId(Long customId) {
		this.customId = customId;
	}

	@Column(name="custom_name", length=30,nullable=false)
	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Column(name="goods_id")
	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	@Column(name="goods_number")
	public Double getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Double goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	@Column(name="amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Column(name="cost")
	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Column(name="discount")
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	@Column(name="agent_profit")
	public Double getAgentProfit() {
		return agentProfit;
	}

	public void setAgentProfit(Double agentProfit) {
		this.agentProfit = agentProfit;
	}

	@Column(name="points", nullable=false)
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	@Column(name="add_time", nullable=false)
	public Date getAddTime() {
		return addTime;
	}
	
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	@Column(name="order_status", nullable=false)
	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	
	@Column(name="rank1", nullable=false)
	public Double getRank1() {
		return rank1;
	}

	public void setRank1(Double rank1) {
		this.rank1 = rank1;
	}

	@Column(name="rank2", nullable=false)
	public Double getRank2() {
		return rank2;
	}

	public void setRank2(Double rank2) {
		this.rank2 = rank2;
	}

	@Column(name="rank3", nullable=false)
	public Double getRank3() {
		return rank3;
	}

	public void setRank3(Double rank3) {
		this.rank3 = rank3;
	}

	@Column(name="comment", length=200)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}	
	
	@Column(name="pay_type")
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}
}