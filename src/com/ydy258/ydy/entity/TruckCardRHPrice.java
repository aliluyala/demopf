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
@Table(name = "t_truck_card_price")
public class TruckCardRHPrice implements Serializable {
	private static final long serialVersionUID = -902636920240631901L;
	private Long id;
	private int cardType;
	private int cardParentType;
	
	private String cardTypeName;
	
	private double returnPoint1;
	private double condition;
	private double returnPoint2;
	private double returnSpecial;
	
	private double returnPoint;
	private double special;
	private double radio;
	@Column(name="return_point")
	public double getReturnPoint() {
		return returnPoint;
	}
	public void setReturnPoint(double returnPoint) {
		this.returnPoint = returnPoint;
	}
	@Column(name="special")
	public double getSpecial() {
		return special;
	}
	public void setSpecial(double special) {
		this.special = special;
	}
	@Column(name="radio")
	public double getRadio() {
		return radio;
	}
	public void setRadio(double radio) {
		this.radio = radio;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="card_parent_type")
	public int getCardParentType() {
		return cardParentType;
	}
	public void setCardParentType(int cardParentType) {
		this.cardParentType = cardParentType;
	}
	
	@Column(name="card_type")
	public int getCardType() {
		return cardType;
	}
	public void setCardType(int cardType) {
		this.cardType = cardType;
	}
	@Column(name="card_type_name")
	public String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(String cardTypeName) {
		this.cardTypeName = cardTypeName;
	}
	@Column(name="return_points1")
	public double getReturnPoint1() {
		return returnPoint1;
	}
	public void setReturnPoint1(double returnPoint1) {
		this.returnPoint1 = returnPoint1;
	}
	@Column(name="condition")
	public double getCondition() {
		return condition;
	}
	
	public void setCondition(double condition) {
		this.condition = condition;
	}
	
	@Column(name="return_points2")
	public double getReturnPoint2() {
		return returnPoint2;
	}
	public void setReturnPoint2(double returnPoint2) {
		this.returnPoint2 = returnPoint2;
	}
	
	@Column(name="return_special")
	public double getReturnSpecial() {
		return returnSpecial;
	}
	public void setReturnSpecial(double returnSpecial) {
		this.returnSpecial = returnSpecial;
	}
}
