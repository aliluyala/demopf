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
@Table(name = "t_truck_card")
public class TruckCard implements Serializable {
	private static final long serialVersionUID = -902636920240631901L;
	private Long id;
	private String cardNumber;
	private int cardType;//1：山东速度加油卡，2：山东高速加油卡，3：中石化加油卡
	private int parentType;//加油卡 1，etc:2
	private String cardTypeName;
	private long truckId;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="truck_id")
	public long getTruckId() {
		return truckId;
	}
	public void setTruckId(long truckId) {
		this.truckId = truckId;
	}
	@Column(name="card_type_name")
	public String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
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
}
