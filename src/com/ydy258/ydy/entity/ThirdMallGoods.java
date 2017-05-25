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
 * 第三方商户商品实体
 * @author mickeylan
 * 
 */
@Entity
@Table(name = "t_third_mall_goods")
public class ThirdMallGoods implements Serializable {
	private static final long serialVersionUID = 7741539535421021817L;
	private Long id;
	private Long storeId; // 商户ID
	private String goodsName; // 商品名称
	private Double price; // 单价
	private Double costPrice;	//成本价
	private String units;	//计量单位
	private Integer storedNumber; // 库存数量
	private Double ratio; // 补贴率（每笔交易补贴给用户的驿道币比率）
	private Double returnRatio; // 返现比率（每成功完成一笔交易，按成交额返用户驿道币的比率）(满足条件condition的返点比率)
	private Double usePointRatio;	//每笔交易最多可使用的驿道币数量
	private Double agentRatio;	//每笔交易代理的分成比率（用利润减去补贴的驿道币后计算）
	private Boolean isDelete;	//删除标志	
	
	private Double defaultRatio;
	
	private Double condition;//满多少条件
	
	
	private Double litteRatio; // 返现比率（每成功完成一笔交易，按成交额返用户驿道币的比率）(不满足条件condition的返点比率)
	

	private Double discountOff;//折扣率
	@Column()
	public Double getDiscountOff() {
		return discountOff;
	}

	public void setDiscountOff(Double discountOff) {
		this.discountOff = discountOff;
	}

	@Column(name="litte_ratio")
	public Double getLitteRatio() {
		return litteRatio;
	}

	public void setLitteRatio(Double litteRatio) {
		this.litteRatio = litteRatio;
	}

	private Boolean isPerfect;//是否在我公司增加返点率之类的参数

	private Double specialBalanceRatio;
	private Integer priceFlag = 1;	//是否需要输入商品数量标志，0-不需要,1-需要	

	private Date createdDate;
	
	@Column()
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	
	@Column(name="condition")
	public Double getCondition() {
		return condition==null?0:condition;
	}

	public void setCondition(Double condition) {
		this.condition = condition;
	}
	@Column(name="special_balance_ratio")
	public Double getSpecialBalanceRatio() {
		return specialBalanceRatio;
	}

	public void setSpecialBalanceRatio(Double specialBalanceRatio) {
		this.specialBalanceRatio = specialBalanceRatio;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="is_perfect")
	public Boolean getIsPerfect() {
		return isPerfect;
	}

	public void setIsPerfect(Boolean isPerfect) {
		this.isPerfect = isPerfect;
	}
	@Column(name="default_ratio")
	public Double getDefaultRatio() {
		return defaultRatio;
	}

	public void setDefaultRatio(Double defaultRatio) {
		this.defaultRatio = defaultRatio;
	}
	
	@Column(name="store_id", nullable=false)
	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	@Column(name="goods_name", length=100, nullable=false)
	public String getGoodsName() {
		return goodsName==null?"":goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	@Column(name="price", nullable=false)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Column(name="cost_price", nullable=false)
	public Double getCostPrice() {
		return costPrice==null?0.0:costPrice;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}

	@Column(name="stored_number", nullable=false)
	public Integer getStoredNumber() {
		return storedNumber;
	}
	
	public void setStoredNumber(Integer storedNumber) {
		this.storedNumber = storedNumber;
	}
	
	@Column(name="units", length=10, nullable=false)
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}	

	@Column(name="ratio", nullable=false)
	public Double getRatio() {
		return ratio==null?0.0:ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	@Column(name="return_ratio", nullable=false)
	public Double getReturnRatio() {
		return returnRatio==null?0.0:returnRatio;
	}

	public void setReturnRatio(Double returnRatio) {
		this.returnRatio = returnRatio;
	}
	
	@Column(name="use_point_ratio", nullable=false)
	public Double getUsePointRatio() {
		return usePointRatio==null?0.0:usePointRatio;
	}
	
	public void setUsePointRatio(Double usePointRatio) {
		this.usePointRatio = usePointRatio;
	}

	@Column(name="agent_ratio", nullable=false)
	public Double getAgentRatio() {
		return agentRatio==null?0.0:agentRatio;
	}
	
	public void setAgentRatio(Double agentRatio) {
		this.agentRatio = agentRatio;
	}

	@Column(name="is_delete", nullable = false)
	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	@Column(name="price_flag", nullable = false)
	public Integer getPriceFlag() {
		return priceFlag;
	}

	public void setPriceFlag(Integer priceFlag) {
		this.priceFlag = priceFlag;
	}
}