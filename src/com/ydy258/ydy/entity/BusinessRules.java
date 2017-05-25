package com.ydy258.ydy.entity;

/**
 * ҵ�����ʵ����
 */
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

@Entity
@Table(name = "t_business_rules")
@NamedNativeQueries({
	@NamedNativeQuery(
			name = "getByActionName",
			query = "SELECT * FROM t_business_rules WHERE POSITION(action_url in :action)<>0"
	)
})
public class BusinessRules implements Serializable {
	private static final long serialVersionUID = -7677744660219859688L;
	private Long id;
	private String actionUrl;//url
	private String descript;//����
	private Double price;//ԭ��
	private Double discount;//�ۿ���
	private Integer type = 1;//����
	private Double cost;//�ɱ�
	private Double discountPrice;//�ۺ�۸�

	private Long discountPoints;//�ۺ���
	
	
	private Long proxyUserId;//
	
	private Double proxyPrice;
	
	
	private Double truckPrice;//价格比例
	
	private Double consignorPrice;//价格比例
	
	private Double truckDiscountPoint;//价格比例
	
	private Double consignorDiscountPoint;//价格比例
	@Column(name="truck_discount_point")
	public Double getTruckDiscountPoint() {
		return truckDiscountPoint;
	}

	public void setTruckDiscountPoint(Double truckDiscountPoint) {
		this.truckDiscountPoint = truckDiscountPoint;
	}
	
	@Column(name="consignor_discount_point")
	public Double getConsignorDiscountPoint() {
		return consignorDiscountPoint;
	}

	public void setConsignorDiscountPoint(Double consignorDiscountPoint) {
		this.consignorDiscountPoint = consignorDiscountPoint;
	}

	@Column(name="truck_price")
	public Double getTruckPrice() {
		return truckPrice;
	}

	public void setTruckPrice(Double truckPrice) {
		this.truckPrice = truckPrice;
	}

	@Column(name="consignor_price")
	public Double getConsignorPrice() {
		return consignorPrice;
	}

	public void setConsignorPrice(Double consignorPrice) {
		this.consignorPrice = consignorPrice;
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

	@Column(name="discount_price")
	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	@Column(name="discount_points")
	public Long getDiscountPoints() {
		return discountPoints;
	}

	public void setDiscountPoints(Long discountPoints) {
		this.discountPoints = discountPoints;
	}

	@Column(name="cost")
	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@Column(name="type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable=false, length=50)
	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	@Column(nullable=false, length=100)
	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	@Column(nullable=false)
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(nullable=false)
	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}
}
