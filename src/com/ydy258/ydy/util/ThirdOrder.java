package com.ydy258.ydy.util;

import java.util.Date;
import java.util.Map;

public class ThirdOrder
{
	private String orderNo; // 订单号（流水号）
	private String customName; // 消费者姓名
	private String tellerName; // 消费者姓名
	private String payType; // 消费者姓名
	private String amount; // 总价格
	private String discount; // 总价格
	private String points; // 总价格
	private String addTime; // 下单时间
	private String payTime; // 支付时间
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getCustomName() {
		return customName;
	}
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	public String getTellerName() {
		return tellerName;
	}
	public void setTellerName(String tellerName) {
		this.tellerName = tellerName;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
}