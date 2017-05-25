package com.ydy258.ydy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 第三方商户订单明细表实体类
 * 
 * @author mickeylan, 2016.08.21
 * 
 */
@Entity
@Table(name = "t_third_mall_order_detail")
public class ThirdMallOrderDetail implements Serializable {
	private static final long serialVersionUID = -8217108497069682050L;
	private Long id;
	private String orderNo; // 订单号（流水号）
	private Long goodsId; // 消费商品ID
	private String goodsName;	//商品名称
	private Double goodsNumber; // 消费数量
	private Double price;	//购买时的单价
	private Double amount; // 总价格
	private Double cost; // 成本价
	private Double discount; // 折扣价（消费者实付金额）
	private Integer canUsePoints; // 补贴额（补贴的驿道币）	
	private Integer maxCanUsePoints;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="order_no", length=32, nullable=false)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name="goods_id", nullable=false)
	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	
	@Column(name="goods_name", length=100, nullable=false)
	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	@Column(name="goods_number", nullable=false)
	public Double getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Double goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	
	@Column(name="price", nullable=false)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name="amount", nullable=false)
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Column(name="cost", nullable=false)
	public Double getCost() {
		return cost;
	}
	
	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Column(name="discount", nullable=false)
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	@Column(name="can_use_points", nullable=false)
	public Integer getCanUsePoints() {
		return canUsePoints;
	}

	public void setCanUsePoints(Integer points) {
		this.canUsePoints = points;
	}
	
	public Integer getMaxCanUsePoints() {
		return maxCanUsePoints;
	}
	
	@Column(name="max_can_use_points", nullable=false)
	public void setMaxCanUsePoints(Integer maxCanUsePoints) {
		this.maxCanUsePoints = maxCanUsePoints;
	}
}