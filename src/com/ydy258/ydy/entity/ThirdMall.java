package com.ydy258.ydy.entity;

/**
 * 第三方商户实体类
 * 2015.11.05 by mickeylan
 */
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "t_third_mall")
public class ThirdMall implements Serializable {
	private static final long serialVersionUID = -902636920240631901L;

	private Long id;
	private String storeName; // 商户名称
	private Integer storeType;	//商户类型
	private String address; // 商户地址
	private Long agencyId = 0L; // 上级代理id
	private Geometry location; // 经纬度
	private String geohash; // geohash值
	private Double ratio; // 分成比例
	private String description; // 商户介绍
	private Date createdDate;
	
	private Double longitude;
	private Double latitude;
	
	private String discountInfo;	//优惠信息
	private Boolean isDelete;	//删除标志
	
	private String phone;
	private Boolean isReal;	//是否真实商家	
	private Integer mobileType = 0;	//手机型号，0-android,1-苹果	

	private Double rank = 5.0;	//店铺评分
	
	private Boolean isFirstRet;//是否首单返
	
	private Double retRadio;//首单返的比例
	
	@Column(name="ret_radio")
	public Double getRetRadio() {
		return retRadio;
	}

	public void setRetRadio(Double retRadio) {
		this.retRadio = retRadio;
	}
	@Column(name="is_first_ret")
	public Boolean getIsFirstRet() {
		return isFirstRet;
	}

	public void setIsFirstRet(Boolean isFirstRet) {
		this.isFirstRet = isFirstRet;
	}
	
	@Column(name="rank")
	public Double getRank() {
		return rank;
	}

	public void setRank(Double rank) {
		this.rank = rank;
	}
	@Column(name="phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Column(name="is_real")
	public Boolean getIsReal() {
		return isReal;
	}

	public void setIsReal(Boolean isReal) {
		this.isReal = isReal;
	}
	
	@Column(name="mobile_type")
	public Integer getMobileType() {
		return mobileType;
	}

	public void setMobileType(Integer mobileType) {
		this.mobileType = mobileType;
	}
	
	@Column()
	public String getDiscountInfo() {
		return discountInfo;
	}

	public void setDiscountInfo(String discountInfo) {
		this.discountInfo = discountInfo;
	}
	@Column()
	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	@Column()
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	@Column()
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	private Long addminId;

	@Column()
	public Long getAddminId() {
		return addminId;
	}

	public void setAddminId(Long addminId) {
		this.addminId = addminId;
	}

	@Column()
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length=200, nullable=false)
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	
	@Column(nullable=false)
	public Integer getStoreType() {
		return storeType;
	}

	public void setStoreType(Integer storeType) {
		this.storeType = storeType;
	}

	@Column(length=200, nullable=false)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(nullable=false)
	public Long getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Long agencyId) {
		this.agencyId = agencyId;
	}

	@Column
	@Type(type = "org.hibernate.spatial.GeometryType")
	public Geometry getLocation() {
		return location;
	}

	public void setLocation(Geometry location) {
		this.location = location;
	}

	@Column(length=20)
	public String getGeohash() {
		return geohash;
	}

	public void setGeohash(String geohash) {
		this.geohash = geohash;
	}

	@Column
	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	@Column(length=200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}