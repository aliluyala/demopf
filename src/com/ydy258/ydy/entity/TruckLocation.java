package com.ydy258.ydy.entity;

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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.vividsolutions.jts.geom.Geometry;

@Entity
@Table(name = "t_truck_location")
@SqlResultSetMapping(
		name = "near_truck",
		entities = {
			@EntityResult(
					entityClass = TruckLocation.class,
					fields = {
						@FieldResult(name="id", column="id"),
						@FieldResult(name="truckId", column="truck_id"),
						@FieldResult(name="plateNumber", column="plate_number"),
						@FieldResult(name="driverName", column="driver_name"),
						@FieldResult(name="driverMobile", column="driver_mobile"),
						@FieldResult(name="origin", column="origin"),
						@FieldResult(name="destination", column="destination"),
						@FieldResult(name="truckType", column="truck_type"),
						@FieldResult(name="truckLength", column="truck_length"),						
						@FieldResult(name="loadStatus", column="load_status"),
						@FieldResult(name="sendLocTime", column="send_loc_time"),
						@FieldResult(name="jpushId", column="jpush_id"),
						@FieldResult(name="location", column="location"),	
						@FieldResult(name="address", column="address"),
						@FieldResult(name="geohash", column="geohash"),
						@FieldResult(name="memo", column="memo"),
						@FieldResult(name="remainLoad", column="remain_load")
					}
			)			
		},
		columns = {
				@ColumnResult(name = "distance"),
				@ColumnResult(name = "driver_pic")
		}
)
@NamedNativeQueries({
	@NamedNativeQuery(
			name = "getNearTruck",
			query = "SELECT t.*,ST_Distance(ST_Transform(ST_GeomFromText('POINT(:lng :lat)',4326),"
					+ "26986),ST_Transform(ST_GeomFromWKB(location,4326),26986)) as distance FROM t_truck_location as t " 
					+ "WHERE ST_DWithin(ST_Transform(ST_GeomFromText('POINT(:lng :lat)',4326),26986), "
					+ "ST_Transform(ST_GeomFromWKB(location,4326),26986), 10000) AND POSITION(:dest IN destination)"
					+ "ORDER BY distance",
			resultSetMapping = "near_truck"
	)
})
public class TruckLocation extends com.ydy258.ydy.entity.Entity implements Serializable {
	private static final long serialVersionUID = -1567381642562802715L;

	private Long id;
	private Long truckId;			//司机id	
	private String plateNumber; 	//车牌号
	private String driverName; 	//司机名
	private String driverMobile; 	//司机手机号
	private String origin; 		//出发地
	private String destination; 	//目的地
	private String truckType; 		//货车类型
	private Double truckLength;	//车长	
	private String loadStatus; 	//货车负载情况
	private Date sendLocTime; 		//最近更新位置信息时间
	private String jpushId;		//司机极光id
	private Geometry location; 	//司机位置	
	/** 目前司机所在位置的详细地址 **/
	private String address;
	private String geohash;		//司机位置的geohash值	
	private String memo;			//备注	
	private Double remainLoad;		//剩余载重量	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column()
	public Long getTruckId() {
		return truckId;
	}

	public void setTruckId(Long truckId) {
		this.truckId = truckId;
	}

	@Column(length = 15)
	public String getPlateNumber() {
		return plateNumber==null?"":plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	@Column(length = 50)
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	@Column(length = 15)
	public String getDriverMobile() {
		return driverMobile;
	}

	public void setDriverMobile(String driverMobile) {
		this.driverMobile = driverMobile;
	}

	@Column(length = 30)
	public String getOrigin() {
		return origin==null?"":origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Column(length = 1000)
	public String getDestination() {
		return destination==null?"":destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Column(length = 10)
	public String getTruckType() {
		return truckType;
	}

	public void setTruckType(String truckType) {
		this.truckType = truckType;
	}
	
	@Column(nullable=false)
	public Double getTruckLength() {
		return truckLength;
	}

	public void setTruckLength(Double truckLength) {
		this.truckLength = truckLength;
	}	

	@Column(length = 10)
	public String getLoadStatus() {
		return loadStatus==null?"":loadStatus;
	}

	public void setLoadStatus(String loadStatus) {
		this.loadStatus = loadStatus;
	}
	
	@Column(length = 15)
	public String getJpushId() {
		return jpushId;
	}

	public void setJpushId(String jpushId) {
		this.jpushId = jpushId;
	}
	
	@Column()
	public Date getSendLocTime() {
		return sendLocTime;
	}

	public void setSendLocTime(Date sendLocTime) {
		this.sendLocTime = sendLocTime;
	}
	
	@Column()
	@Type(type = "org.hibernate.spatial.GeometryType")
	public Geometry getLocation() {
		return location;
	}

	public void setLocation(Geometry location) {
		this.location = location;
	}	
	
	@Column(length = 255)
	public String getAddress() {
		return address==null?"":address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(length=20)
	public String getGeohash() {
		return geohash;
	}

	public void setGeohash(String geohash) {
		this.geohash = geohash;
	}
	
	@Column(length=255)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Column()
	public Double getRemainLoad() {
		return remainLoad;
	}

	public void setRemainLoad(Double remainLoad) {
		this.remainLoad = remainLoad;
	}
}