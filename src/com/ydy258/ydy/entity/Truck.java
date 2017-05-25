package com.ydy258.ydy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "t_truck_info")
public class Truck implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3617126330666256082L;
	
	private Long id;	

	/** 锟斤拷锟斤拷司锟斤拷锟斤拷锟斤拷 */
	private String driverName;	

	/** 锟街伙拷锟�*/
	private String mobile;
	/** 锟斤拷锟狡猴拷 */
	private String plateNumber;
	/** 锟斤拷锟斤拷 */
	private String password;
	/** 锟斤拷锟街わ拷锟�*/
	private String idCardNumber;
	/** 锟斤拷锟街わ拷锟狡瑄rl */
	private String idCardPic;
	/** 锟斤拷驶证锟斤拷 */
	private String drivingLicenseNumber;
	/** 锟斤拷驶证锟斤拷片url */
	private String drivingLicensePic;
	/** 锟斤拷驶证锟斤拷 */
	private String registrationNumber;
	
	/** 行驶证照片url */
	private String registrationPic;
	/** 司机照片 */
	private String driverPic;	

	/** 车辆照片 */
	private String truckPic;
	
	/** 出厂年份 */
	private String factoryYear;	

	/** 车长 */
	private Double truckLength;
	
 	
 	private  SysUser proxyUserId;
 	
 	/** 手机硬件ID */
 	private String deviceId;
 	
 	/** 推广柜员ID */
 	private String tellerId;
 	
 	private Boolean isFirstCheck;
 	
 	private Boolean addExpTime;
 	
 	private Long roleId;
 	
 	/**注册时间**/
	private Date registerTime;	
	
	
	private String gsCard;
	private String jyCard;
	private String czCard;
	
	
	/** 手机类型 0－android,1-苹果*/
 	private Integer mobileType = 0;	
 	
 	@Column(nullable=false)
	public Integer getMobileType() {
		return mobileType;
	}

	public void setMobileType(Integer mobileType) {
		this.mobileType = mobileType;
	}
	
	@Column()
	public String getGsCard() {
		return gsCard;
	}

	public void setGsCard(String gsCard) {
		this.gsCard = gsCard;
	}
	@Column()
	public String getJyCard() {
		return jyCard;
	}

	public void setJyCard(String jyCard) {
		this.jyCard = jyCard;
	}
	@Column()
	public String getCzCard() {
		return czCard;
	}

	public void setCzCard(String czCard) {
		this.czCard = czCard;
	}

	@Column()
 	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	@Column()
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

 	@Column()
	public Boolean getAddExpTime() {
		return addExpTime;
	}

	public void setAddExpTime(Boolean addExpTime) {
		this.addExpTime = addExpTime;
	}

	@Column()
 	public Boolean getIsFirstCheck() {
		return isFirstCheck;
	}

	public void setIsFirstCheck(Boolean isFirstCheck) {
		this.isFirstCheck = isFirstCheck;
	}

	private Double specialBalance = 0.0;	
 	@Column()
 	public Double getSpecialBalance() {
		return specialBalance;
	}

	public void setSpecialBalance(Double specialBalance) {
		this.specialBalance = specialBalance;
	}

	@Column()
	public String getFactoryYear() {
		return factoryYear;
	}

	public void setFactoryYear(String factoryYear) {
		this.factoryYear = factoryYear;
	}
	@Column()
	public Double getTruckLength() {
		return truckLength;
	}

	public void setTruckLength(Double truckLength) {
		this.truckLength = truckLength;
	}
	@ManyToOne(targetEntity=SysUser.class, fetch = FetchType.LAZY)
	@JoinColumn(name="proxy_user_id", updatable=false, insertable=false)
	@NotFound(action=NotFoundAction.IGNORE)
	public SysUser getProxyUserId() {
		return proxyUserId;
	}

	public void setProxyUserId(SysUser proxyUserId) {
		this.proxyUserId = proxyUserId;
	}
	@Column()
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	@Column()
	public String getTellerId() {
		return tellerId;
	}

	public void setTellerId(String tellerId) {
		this.tellerId = tellerId;
	}

	@Column(length=200)
	public String getDriverPic() {
		return driverPic;
	}

	public void setDriverPic(String driverPic) {
		this.driverPic = driverPic;
	}
	@Column(length=200)
	public String getTruckPic() {
		return truckPic;
	}

	public void setTruckPic(String truckPic) {
		this.truckPic = truckPic;
	}

	/** 锟斤拷锟斤拷锟铰际э拷艽锟斤拷锟�*/
	private Integer loginFailureCount = 0;

	/** 锟斤拷锟斤拷锟斤拷 */
	private Date lockedDate;

	/** 锟斤拷锟斤拷录锟斤拷锟斤拷 */
	private Date loginDate;

	/** 锟斤拷锟斤拷录IP */
	private String loginIp;
	
	/** 锟角凤拷通锟斤拷锟斤拷锟�*/
	private Boolean isCheck = false;	
	
	private Boolean isLocked = false;	

	
	private String managerType;
	private String brandType;
	private String truckIDCode;
	private String motorCode;
	private String registerDate;
	private String dispCardDate;
	
	/** 积分**/
 	private Long points; 
	
 	
 	/** 服务到期时间 */
 	private Date expireTime;
 	
 	@Column()
 	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	
 	@Column()
	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}
	
	@Column(name="manager_type")
	public String getManagerType() {
		return managerType;
	}

	public void setManagerType(String managerType) {
		this.managerType = managerType;
	}

	@Column(name="brand_type")
	public String getBrandType() {
		return brandType;
	}

	public void setBrandType(String brandType) {
		this.brandType = brandType;
	}

	@Column(name="truckid_code")
	public String getTruckIDCode() {
		return truckIDCode;
	}

	public void setTruckIDCode(String truckIDCode) {
		this.truckIDCode = truckIDCode;
	}

	@Column(name="motor_code")
	public String getMotorCode() {
		return motorCode;
	}

	
	public void setMotorCode(String motorCode) {
		this.motorCode = motorCode;
	}

	@Column(name="register_date")
	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	@Column(name="disp_date")
	public String getDispCardDate() {
		return dispCardDate;
	}

	public void setDispCardDate(String dispCardDate) {
		this.dispCardDate = dispCardDate;
	}

	@Column(name = "is_locked")
	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	private TCommData tcd;
	
	@ManyToOne
	@JoinColumn(name="truck_type")
	public TCommData getTcd() {
		return tcd;
	}

	public void setTcd(TCommData tcd) {
		this.tcd = tcd;
	}

	/** 锟斤拷锟斤拷锟斤拷 */
	private Integer deadWeight;
	/** 锟截伙拷状态状态锟斤拷0锟斤拷锟斤拷锟截★拷1锟斤拷锟斤拷铡锟�锟斤拷锟斤拷锟截ｏ拷 */
	private Integer loadStatus = 0;	
	
	/**锟斤拷锟斤拷锟斤拷锟斤拷ID**/
	private String jpushId;	
	
 	private Double balance;
 	
 	/** 身份认证步骤 */
	private Integer authStep = 0;	
 	
	@Column()
 	public Integer getAuthStep() {
		return authStep;
	}

	public void setAuthStep(Integer authStep) {
		this.authStep = authStep;
	}

	@Column(name = "balance")
 	public Double getBalance() {
 		return balance;
 	}

 	public void setBalance(Double balance) {
 		this.balance = balance;
 	}
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(length = 12, nullable = false)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Column(length = 10, nullable = false)
	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
	@Pattern(regexp = "^[^\\s&\"<>]+$")
	@Column(length = 20, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(length = 19, nullable = false)
	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	@Column(length=200)
	public String getIdCardPic() {
		return idCardPic;
	}

	public void setIdCardPic(String idCardPic) {
		this.idCardPic = idCardPic;
	}

	@Column(length=20)
	public String getDrivingLicenseNumber() {
		return drivingLicenseNumber;
	}

	public void setDrivingLicenseNumber(String drivingLicenseNumber) {
		this.drivingLicenseNumber = drivingLicenseNumber;
	}

	@Column(length=200)
	public String getDrivingLicensePic() {
		return drivingLicensePic;
	}

	public void setDrivingLicensePic(String drivingLicensePic) {
		this.drivingLicensePic = drivingLicensePic;
	}

	@Column(length = 20)
	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	@Column(length = 200)
	public String getRegistrationPic() {
		return registrationPic;
	}

	public void setRegistrationPic(String registrationPic) {
		this.registrationPic = registrationPic;
	}
	
	public Integer getLoginFailureCount() {
		return loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	
	public Boolean getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(Boolean isCheck) {
		this.isCheck = isCheck;
	}
	
	@Column(length=20, nullable=false)
	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
	public Integer getDeadWeight() {
		return deadWeight;
	}

	public void setDeadWeight(Integer deadWeight) {
		this.deadWeight = deadWeight;
	}
	
	public Integer getLoadStatus() {
		return loadStatus;
	}

	public void setLoadStatus(Integer loadStatus) {
		this.loadStatus = loadStatus;
	}
	
	@Column(name="jpush_id")
	public String getJpushId() {
		return jpushId;
	}
	
	public void setJpushId(String jpushId) {
		this.jpushId = jpushId;
	}
	
	public Truck() {		
	}
}