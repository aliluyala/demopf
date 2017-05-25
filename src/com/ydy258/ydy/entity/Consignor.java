package com.ydy258.ydy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "t_consignor_info")
public class Consignor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7975432264219454906L;
	
	public enum ConsignorType  {
		PERSONAL, BUSSINESS
	}

	private Long id;
	private String mobile;
	private String password;
	private String realName;
	private String idCard="";
	private String avartar;
	private Boolean isLocked;
	/** 车主类型 */
	private ConsignorType consignorType = ConsignorType.PERSONAL;
	/** 连续登录失败次数 */
	private Integer loginFailureCount;

	/** 锁定日期 */
	private Date lockedDate;

	/** 最后登录日期 */
	private Date loginDate;

	/** 最后登录IP */
	private String loginIp;
	
	/** 设备ID*/
	private String deviceId;
	/**极光推送ID**/
	private String jpushId;
	/**业务城市**/
	private String cities;
	/**业务货物类型**/
	private String cargoType;
	/**所在地址**/
	private String address;
	
	/**余额**/
	private Double balance = 0.0;
	
	/**传真**/
	private String fax;
	
	/**是否认证**/
	private Boolean isAuth;
	/**资粮是否完善**/
	private Boolean isPerfect;
	
	/** 锁定日期 */
	private Date registerTime;

	private String idCardPic="";	//身份证照片
	private String businessLicensePic="";	//营业执照照片
	private String taxRegistrationPic="";	//税务登记证照片
	private String officePic="";	//办公场所照片
	
	
	/** 身份认证步骤 */
	private Integer authStep = 0;	
	
	/** 积分**/
 	private Long points; 
	
 	
 	/** 服务到期时间 */
 	private Date expireTime;
 	
 	
 	private SysUser proxyUserId;	
 	
 	/** 推广柜员ID */
 	private String tellerId;
 	
 	private Boolean addExpTime;
 	
 	private Boolean isFirstCheck;
 	
 	private Long roleId;

 	/** 手机类型 0－android,1-苹果*/
 	private Integer mobileType = 0;
 	
 	private String companyName;	//公司名	
 	private Integer userType = 0;	//用户类型，0-个人货主,1-运输公司	
 	
 	
 	/** 支付密码 */
	private String paymentPassword;
	
	private String descript="";
	
	
	private String bussDescript;//业务描述
	

	@Column(name="buss_descript",  columnDefinition="TEXT")
	public String getBussDescript() {
		return bussDescript;
	}

	public void setBussDescript(String bussDescript) {
		this.bussDescript = bussDescript;
	}

	@Column(name="descript", columnDefinition="TEXT", nullable=true)
	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	@Column(name="payment_password", nullable=false, length=32)
	public String getPaymentPassword() {
		return paymentPassword;
	}

	public void setPaymentPassword(String paymentPassword) {
		this.paymentPassword = paymentPassword;
	}
 	
 	@Column(name="company_name", length=255)
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name="user_type")
	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	@Column(nullable=false)
	public Integer getMobileType() {
		return mobileType;
	}

	public void setMobileType(Integer mobileType) {
		this.mobileType = mobileType;
	}
 	@Column()
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column()
 	public Boolean getIsFirstCheck() {
		return isFirstCheck;
	}

	public void setIsFirstCheck(Boolean isFirstCheck) {
		this.isFirstCheck = isFirstCheck;
	}
 	@Column()
 	public Boolean getAddExpTime() {
		return addExpTime;
	}

	public void setAddExpTime(Boolean addExpTime) {
		this.addExpTime = addExpTime;
	}

	@ManyToOne(targetEntity=SysUser.class, fetch = FetchType.LAZY)
	@JoinColumn(name="proxy_user_id"/*,updatable=false, insertable=false*/)
	@NotFound(action=NotFoundAction.IGNORE)
 	public SysUser getProxyUserId() {
		return proxyUserId;
	}

	public void setProxyUserId(SysUser proxyUserId) {
		this.proxyUserId = proxyUserId;
	}
	@Column()
	public String getTellerId() {
		return tellerId;
	}

	public void setTellerId(String tellerId) {
		this.tellerId = tellerId;
	}

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

	@Column()
	public Integer getAuthStep() {
		return authStep;
	}

	public void setAuthStep(Integer authStep) {
		this.authStep = authStep;
	}

	@Column()
	public String getIdCardPic() {
		return idCardPic;
	}

	public void setIdCardPic(String idCardPic) {
		this.idCardPic = idCardPic;
	}
	@Column()
	public String getBusinessLicensePic() {
		return businessLicensePic;
	}

	public void setBusinessLicensePic(String businessLicensePic) {
		this.businessLicensePic = businessLicensePic;
	}
	@Column(length=500)
	public String getTaxRegistrationPic() {
		return taxRegistrationPic;
	}

	public void setTaxRegistrationPic(String taxRegistrationPic) {
		this.taxRegistrationPic = taxRegistrationPic;
	}

	public String getOfficePic() {
		return officePic;
	}

	public void setOfficePic(String officePic) {
		this.officePic = officePic;
	}

	@Column(name = "register_time")
	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	@Column(name = "balance")
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	@Column()
	public Boolean getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Boolean isAuth) {
		this.isAuth = isAuth;
	}
	@Column()
	public Boolean getIsPerfect() {
		return isPerfect;
	}

	public void setIsPerfect(Boolean isPerfect) {
		this.isPerfect = isPerfect;
	}

	@Column(name="fax")
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name="address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name="cities")
	public String getCities() {
		return cities;
	}

	public void setCities(String cities) {
		this.cities = cities;
	}
	
	@Column(name="cargo_type")
	public String getCargoType() {
		return cargoType;
	}

	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}

	@Column(name="device_id")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name="jpush_id")
	public String getJpushId() {
		return jpushId;
	}

	public void setJpushId(String jpushId) {
		this.jpushId = jpushId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// 手机号
	@Column(length = 12, nullable = false)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	// 密码
	@Pattern(regexp = "^[^\\s&\"<>]+$")
	@Column(length = 20, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// 姓名
	@Column(name = "real_name", length = 32, nullable = false)
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	// 身份证号码
	@Column(length = 19)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	// 头像
	@Column(name = "arvartar_url", length = 550)
	public String getAvartar() {
		return avartar;
	}

	public void setAvartar(String avartar) {
		this.avartar = avartar;
	}

	@Column()
	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}
	@Column(nullable=false)
	public Integer getLoginFailureCount() {
		return loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}
	@Column
	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}
	@Column
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	@Column(length=16)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	@Column(name="consignor_type")
	@Enumerated(EnumType.ORDINAL)
	public ConsignorType getConsignorType() {
		return consignorType;
	}

	public void setConsignorType(ConsignorType consignorType) {
		this.consignorType = consignorType;
	}
}