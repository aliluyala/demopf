package com.ydy258.ydy;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class Setting implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2478720806514567115L;
	/**
	 * �˺�������
	 */
	public enum AccountLockType {

		/** ��Ա */
		member,

		/** ����Ա */
		admin
	}
	
	/**
	 * С��λ��ȷ��ʽ
	 */
	public enum RoundType {

		/** �������� */
		roundHalfUp,

		/** ����ȡ�� */
		roundUp,

		/** ����ȡ�� */
		roundDown
	}
	
	/**
	 * ��֤������
	 */
	public enum CaptchaType {

		/** ��Ա��¼ */
		memberLogin,

		/** ��Աע�� */
		memberRegister,

		/** ��̨��¼ */
		adminLogin,

		/** ��Ʒ���� */
		review,

		/** ��Ʒ��ѯ */
		consultation,

		/** �һ����� */
		findPassword,

		/** �������� */
		resetPassword,

		/** ���� */
		other
	}
	
	/** ������� */
	public static final String CACHE_NAME = "setting";

	/** ����Key */
	public static final Integer CACHE_KEY = 0;
	
	
	
	/** �˺������� */
	private AccountLockType[] accountLockTypes;
	/** ��֤������ */
	private CaptchaType[] captchaTypes;
	
	/** �Զ�����ʱ�� */
	private Integer accountLockTime;
	
	/** �����¼ʧ�������� */
	private Integer accountLockCount;
	
	/** Cookie·�� */
	private String cookiePath;

	/** Cookie������ */
	private String cookieDomain;
	
	/** �Ƿ���վ���� */
	private Boolean isSiteEnabled;
	
	/** �۸�ȷλ�� */
	private Integer priceScale;

	/** �۸�ȷ��ʽ */
	private RoundType priceRoundType;
	
	/** ���ҷ�� */
	private String currencySign;

	/** ���ҵ�λ */
	private String currencyUnit;
	
	/** mongodb �����ַ�б� */
	private String[] mongoHosts;
	
	/** mongodb �û��� */
	private String mongoUserName;	

	/** mongodb ���� */
	private String mongoPassword;
	
	/** mongodb ��ݿ��� */
	private String mongoDBName;
	
	
	private String permission;
	
	
	private long truckregisterpoints;
	
	private long consingorregisterpoints;
	
	
	

	private Integer jyReturnPoint;
	private Integer jyPayPoint;
	private Integer jySpecialBalance;
	private Integer glReturnPoint;
	private Integer glPayPoint;
	private Integer glSpecialBalance;
	
	private Double yearPay;
	
	private Double ddbxcxwy;
	
	private String serviceMail;
	
	public String getServiceMail() {
		return serviceMail;
	}

	public void setServiceMail(String serviceMail) {
		this.serviceMail = serviceMail;
	}
	public Double getDdbxcxwy() {
		return ddbxcxwy;
	}

	public void setDdbxcxwy(Double ddbxcxwy) {
		this.ddbxcxwy = ddbxcxwy;
	}

	public Double getYearPay() {
		return yearPay;
	}

	public void setYearPay(Double yearPay) {
		this.yearPay = yearPay;
	}

	public long getConsingorregisterpoints() {
		return consingorregisterpoints;
	}

	public void setConsingorregisterpoints(long consingorregisterpoints) {
		this.consingorregisterpoints = consingorregisterpoints;
	}
	
	public Integer getJyReturnPoint() {
		return jyReturnPoint;
	}

	public void setJyReturnPoint(Integer jyReturnPoint) {
		this.jyReturnPoint = jyReturnPoint;
	}

	public Integer getJyPayPoint() {
		return jyPayPoint;
	}

	public void setJyPayPoint(Integer jyPayPoint) {
		this.jyPayPoint = jyPayPoint;
	}

	public Integer getJySpecialBalance() {
		return jySpecialBalance;
	}

	public void setJySpecialBalance(Integer jySpecialBalance) {
		this.jySpecialBalance = jySpecialBalance;
	}

	public Integer getGlReturnPoint() {
		return glReturnPoint;
	}

	public void setGlReturnPoint(Integer glReturnPoint) {
		this.glReturnPoint = glReturnPoint;
	}

	public Integer getGlPayPoint() {
		return glPayPoint;
	}

	public void setGlPayPoint(Integer glPayPoint) {
		this.glPayPoint = glPayPoint;
	}

	public Integer getGlSpecialBalance() {
		return glSpecialBalance;
	}

	public void setGlSpecialBalance(Integer glSpecialBalance) {
		this.glSpecialBalance = glSpecialBalance;
	}


	public long getTruckregisterpoints() {
		return truckregisterpoints;
	}

	public void setTruckregisterpoints(long truckregisterpoints) {
		this.truckregisterpoints = truckregisterpoints;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * ��ȡ�˺�������
	 * 
	 * @return �˺�������
	 */
	public AccountLockType[] getAccountLockTypes() {
		return accountLockTypes;
	}
	
	/**
	 * ��ȡ��֤������
	 * 
	 * @return ��֤������
	 */
	public CaptchaType[] getCaptchaTypes() {
		return captchaTypes;
	}
	
	/**
	 * ��ȡ�Զ�����ʱ��
	 * 
	 * @return �Զ�����ʱ��
	 */
	@NotNull
	@Min(0)
	public Integer getAccountLockTime() {
		return accountLockTime;
	}
	
	/**
	 * ��ȡ�����¼ʧ��������
	 * 
	 * @return �����¼ʧ��������
	 */
	@NotNull
	@Min(1)
	public Integer getAccountLockCount() {
		return accountLockCount;
	}
	
	/**
	 * ���������¼ʧ��������
	 * 
	 * @param accountLockCount
	 *            �����¼ʧ��������
	 */
	public void setAccountLockCount(Integer accountLockCount) {
		this.accountLockCount = accountLockCount;
	}
	
	/**
	 * ��ȡCookie·��
	 * 
	 * @return Cookie·��
	 */
	@NotEmpty
	@Length(max = 200)
	public String getCookiePath() {
		return cookiePath;
	}
	
	/**
	 * ����Cookie·��
	 * 
	 * @param cookiePath
	 *            Cookie·��
	 */
	public void setCookiePath(String cookiePath) {
		if (cookiePath != null && !cookiePath.endsWith("/")) {
			cookiePath += "/";
		}
		this.cookiePath = cookiePath;
	}

	/**
	 * ��ȡCookie������
	 * 
	 * @return Cookie������
	 */
	@Length(max = 200)
	public String getCookieDomain() {
		return cookieDomain;
	}

	/**
	 * ����Cookie������
	 * 
	 * @param cookieDomain
	 *            Cookie������
	 */
	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}
	
	/**
	 * ��ȡ�Ƿ���վ����
	 * 
	 * @return �Ƿ���վ����
	 */
	@NotNull
	public Boolean getIsSiteEnabled() {
		return isSiteEnabled;
	}

	/**
	 * �����Ƿ���վ����
	 * 
	 * @param isSiteEnabled
	 *            �Ƿ���վ����
	 */
	public void setIsSiteEnabled(Boolean isSiteEnabled) {
		this.isSiteEnabled = isSiteEnabled;
	}
	
	/**
	 * ��ȡ�۸�ȷλ��
	 * 
	 * @return �۸�ȷλ��
	 */
	@NotNull
	@Min(0)
	@Max(3)
	public Integer getPriceScale() {
		return priceScale;
	}

	/**
	 * ���ü۸�ȷλ��
	 * 
	 * @param priceScale
	 *            �۸�ȷλ��
	 */
	public void setPriceScale(Integer priceScale) {
		this.priceScale = priceScale;
	}	

	/**
	 * ��ȡ�۸�ȷ��ʽ
	 * 
	 * @return �۸�ȷ��ʽ
	 */
	@NotNull
	public RoundType getPriceRoundType() {
		return priceRoundType;
	}	
	
	/**
	 * ���ü۸�ȷ��ʽ
	 * 
	 * @param priceRoundType
	 *            �۸�ȷ��ʽ
	 */
	public void setPriceRoundType(RoundType priceRoundType) {
		this.priceRoundType = priceRoundType;
	}
	
	/**
	 * ��ȡ���ҷ��
	 * 
	 * @return ���ҷ��
	 */
	@NotEmpty
	@Length(max = 200)
	public String getCurrencySign() {
		return currencySign;
	}

	/**
	 * ���û��ҷ��
	 * 
	 * @param currencySign
	 *            ���ҷ��
	 */
	public void setCurrencySign(String currencySign) {
		this.currencySign = currencySign;
	}
	
	/**
	 * ��ȡ���ҵ�λ
	 * 
	 * @return ���ҵ�λ
	 */
	@NotEmpty
	@Length(max = 200)
	public String getCurrencyUnit() {
		return currencyUnit;
	}

	/**
	 * ���û��ҵ�λ
	 * 
	 * @param currencyUnit
	 *            ���ҵ�λ
	 */
	public void setCurrencyUnit(String currencyUnit) {
		this.currencyUnit = currencyUnit;
	}
	
	/**
	 * ���þ���
	 * 
	 * @param amount
	 *            ��ֵ
	 * @return ��ֵ
	 */
	public BigDecimal setScale(BigDecimal amount) {
		if (amount == null) {
			return null;
		}
		int roundingMode;
		if (getPriceRoundType() == RoundType.roundUp) {
			roundingMode = BigDecimal.ROUND_UP;
		} else if (getPriceRoundType() == RoundType.roundDown) {
			roundingMode = BigDecimal.ROUND_DOWN;
		} else {
			roundingMode = BigDecimal.ROUND_HALF_UP;
		}
		return amount.setScale(getPriceScale(), roundingMode);
	}
	
	public String[] getMongoHosts() {
		return mongoHosts;
	}

	public void setMongoHosts(String[] mongoHosts) {
		this.mongoHosts = mongoHosts;
	}
	
	public String getMongoUserName() {
		return mongoUserName;
	}

	public void setMongoUserName(String mongoUserName) {
		this.mongoUserName = mongoUserName;
	}

	public String getMongoPassword() {
		return mongoPassword;
	}

	public void setMongoPassword(String mongoPassword) {
		this.mongoPassword = mongoPassword;
	}

	public String getMongoDBName() {
		return mongoDBName;
	}

	public void setMongoDBName(String mongoDBName) {
		this.mongoDBName = mongoDBName;
	}
}