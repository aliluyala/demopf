package com.ydy258.ydy.entity;

/**
 * 司机卡
 * 
 */
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "t_truck_card_order")
public class TruckCardRHOrder implements Serializable {
	private static final long serialVersionUID = -902636920240631901L;
	private Long id;
	private String orderNo;
	
	private long goodsId;
	private double price;
	private double discount;
	private int points;
	
	private long cardId;
	private String cardNumber;
	private int cardType;//1：山东速度加油卡，2：山东高速加油卡，3：中石化加油卡
	private String cardTypeName;
	
	private int parentType;//加油卡 1，etc:2
	private long truckId;
	private String truckMoblie;
	private String truckName;
	private Integer payType=1;	//支付方式（1-余额,2-微信,3-支付宝,4-连连）
	private Date createDate;
	private Date payDate;
	private Date handleTime;//处理时间
	private String handleSysUser;//处理人登录名
	private String handleErrMsg;//处理失败原因
	
	
	private int status;//1,用户提交，2，支付成功。3处理成功，4,处理失败
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="handle_err_msg")
	public String getHandleErrMsg() {
		return handleErrMsg;
	}
	public void setHandleErrMsg(String handleErrMsg) {
		this.handleErrMsg = handleErrMsg;
	}
	@Column(name="handle_time")
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	@Column(name="handle_sys_user")
	public String getHandleSysUser() {
		return handleSysUser;
	}
	public void setHandleSysUser(String handleSysUser) {
		this.handleSysUser = handleSysUser;
	}
	
	@Column(name="card_type_name")
	public String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
	@Column(name="pay_date")
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	@Column(name="pay_type")
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	@Column(name="parent_type")
	public int getParentType() {
		return parentType;
	}
	public void setParentType(int parentType) {
		this.parentType = parentType;
	}
	@Column(name="card_number")
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	@Column(name="card_type")
	public int getCardType() {
		return cardType;
	}
	public void setCardType(int cardType) {
		this.cardType = cardType;
	}
	
	@Column(name="order_no")
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	@Column(name="goods_id")
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
	
	@Column(name="price")
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	@Column(name="discount")
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	@Column(name="points")
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	@Column(name="card_id")
	public long getCardId() {
		return cardId;
	}
	public void setCardId(long cardId) {
		this.cardId = cardId;
	}
	
	@Column(name="truck_id")
	public long getTruckId() {
		return truckId;
	}
	public void setTruckId(long truckId) {
		this.truckId = truckId;
	}
	
	@Column(name="truck_mobile")
	public String getTruckMoblie() {
		return truckMoblie;
	}
	public void setTruckMoblie(String truckMoblie) {
		this.truckMoblie = truckMoblie;
	}
	@Column(name="truck_name")
	public String getTruckName() {
		return truckName;
	}
	public void setTruckName(String truckName) {
		this.truckName = truckName;
	}
	
	@Column(name="created_date")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name="status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
