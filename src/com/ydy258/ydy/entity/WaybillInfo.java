package com.ydy258.ydy.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.FieldResult;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "t_waybill_info")
public class WaybillInfo implements Serializable {	
	private static final long serialVersionUID = 8536964953502494226L;
	
	private Long id;
	private Long consignorId;			//货主ID
	private String consignorName;		//货主姓名
	private String consignorMobile;	//货主手机号
	private String origin;				//出发地
	private String destination;		//目的地
	private String memo;				//备注
	private String truckType;			//运货所需车型
	private Double truckLength;		//车长
	private String goodsType;			//货物类型
	private String jpushId;			//货主极光ID
	private Double fee;				//运费
	private Double bond;				//货主保证金
	private Integer weight;			//重量
	private Integer bulk;				//体积
	//private Long[] sendTruckId;		//已发送司机ID列表
	//private Long[] firedTruckId;		//已报价司机ID列表
	private Date addTime;				//运单生成时间
	//private Geometry location;			//货主位置
	private String geohash;			//货主位置geohash值
	private Integer isDelete;			//删除标志	
	private Integer isOrdered;			//是否最终生成了订单	
	private String orderNo;			//生成的订单号	
	private String sendConsignorId;	//已发送货主ID列表
	private String firedConsignorId;	//已报价货主ID列表


	private Integer payType;	//支付方式（1-余额,2-微信,3-支付宝,4-连连）
	
	private Boolean isPaySuccess=false;//是否支付成功

	@Column(name="pay_success")
	public Boolean getIsPaySuccess() {
		return isPaySuccess;
	}

	public void setIsPaySuccess(Boolean isPaySuccess) {
		this.isPaySuccess = isPaySuccess;
	}

	@Column(name="pay_type")
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="consignor_id")
	public Long getConsignorId() {
		return consignorId;
	}

	public void setConsignorId(Long consignorId) {
		this.consignorId = consignorId;
	}

	@Column(name="consignor_name", length = 50)
	public String getConsignorName() {
		return consignorName;
	}

	public void setConsignorName(String consignorName) {
		this.consignorName = consignorName;
	}

	@Column(name="consignor_mobile", length = 15)
	public String getConsignorMobile() {
		return consignorMobile;
	}

	public void setConsignorMobile(String consignorMobile) {
		this.consignorMobile = consignorMobile;
	}

	@Column(name="origin", length = 30)
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Column(name="destination", length = 30)
	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	@Column(name="weight")
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Column(name="bulk")
	public Integer getBulk() {
		return bulk;
	}

	public void setBulk(Integer bulk) {
		this.bulk = bulk;
	}

	@Column(name="fee")
	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}
	
	@Column(name="bond")
	public Double getBond() {
		return bond;
	}

	public void setBond(Double bond) {
		this.bond = bond;
	}


	@Column(name="add_time")
	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	
	@Column(name="memo", length=255)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Column(name="truck_type", length=20)
	public String getTruckType() {
		return truckType;
	}

	public void setTruckType(String truckType) {
		this.truckType = truckType;
	}
	
	@Column(name="truck_length", nullable=false)
	public Double getTruckLength() {
		return truckLength;
	}

	public void setTruckLength(Double truckLength) {
		this.truckLength = truckLength;
	}
	
	@Column(name="goods_type", nullable=false,length=30)
	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	@Column(name="jpush_id", length=20)
	public String getJpushId() {
		return jpushId;
	}

	public void setJpushId(String jpushId) {
		this.jpushId = jpushId;
	}	
	
	@Column(name="geohash", nullable=false, length=20)
	public String getGeohash() {
		return geohash;
	}

	public void setGeohash(String geohash) {
		this.geohash = geohash;
	}
	
	@Column(name="is_delete", nullable=false)
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	@Column(name="is_ordered", nullable=false)
	public Integer getIsOrdered() {
		return isOrdered;
	}

	public void setIsOrdered(Integer isOrdered) {
		this.isOrdered = isOrdered;
	}
	
	@Column(name="order_no", nullable=false, length=32)
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
	@Column(name="send_consignor_id", length=255)	
	public String getSendConsignorId() {
		return sendConsignorId;
	}

	public void setSendConsignorId(String sendConsignorId) {
		this.sendConsignorId = sendConsignorId;
	}

	@Column(name="fired_consignor_id", length=255)	
	public String getFiredConsignorId() {
		return firedConsignorId;
	}

	public void setFiredConsignorId(String firedConsignorId) {
		this.firedConsignorId = firedConsignorId;
	}
}
