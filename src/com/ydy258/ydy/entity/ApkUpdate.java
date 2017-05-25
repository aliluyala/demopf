package com.ydy258.ydy.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "t_apk_update")
public class ApkUpdate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3617126330666256082L;
	
	private Long id;	
    private boolean update;
	private String newVersion;
	private String apkUrl;
	private String updateLog;
	private double targetSize;
	private String newMd5;
	private Date createdDate;
	
	private boolean forceUpdate;
	
	private int appType;
	
	private String appKey;
	
	private String packages;
	
	
	private String versoinName;
	@Column()
	public String getVersoinName() {
		return versoinName;
	}

	public void setVersoinName(String versoinName) {
		this.versoinName = versoinName;
	}

	@Column()
	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	@Column()
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@Column()
	public int getAppType() {
		return appType;
	}

	public void setAppType(int appType) {
		this.appType = appType;
	}

	@Column()
	public boolean isForceUpdate() {
		return forceUpdate;
	}
	
	public void setForceUpdate(boolean forceUpdate) {
		this.forceUpdate = forceUpdate;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column()
	public boolean isUpdate() {
		return update;
	}
	public void setUpdate(boolean update) {
		this.update = update;
	}
	@Column()
	public String getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
	@Column()
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	@Column()
	public String getUpdateLog() {
		return updateLog;
	}
	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
	@Column()
	public double getTargetSize() {
		return targetSize;
	}
	public void setTargetSize(double targetSize) {
		this.targetSize = targetSize;
	}
	@Column()
	public String getNewMd5() {
		return newMd5;
	}
	public void setNewMd5(String newMd5) {
		this.newMd5 = newMd5;
	}
	@Column()
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}