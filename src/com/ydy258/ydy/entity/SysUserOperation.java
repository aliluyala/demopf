package com.ydy258.ydy.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysUserOperation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_user_operation")
public class SysUserOperation implements java.io.Serializable {

	// Fields

	private Long id;
	private String userId;
	private String actionName;
	private Date createdDate;
	private String actionTranslation;
	private String dataId;

	// Constructors

	/** default constructor */
	public SysUserOperation() {
	}

	/** full constructor */
	public SysUserOperation(String userId, String actionName,
			Date createdDate, String actionTranslation, String dataId) {
		this.userId = userId;
		this.actionName = actionName;
		this.createdDate = createdDate;
		this.actionTranslation = actionTranslation;
		this.dataId = dataId;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "user_id")
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "action_name")
	public String getActionName() {
		return this.actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	@Column(name = "created_date", length = 19)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "action_translation")
	public String getActionTranslation() {
		return this.actionTranslation;
	}

	public void setActionTranslation(String actionTranslation) {
		this.actionTranslation = actionTranslation;
	}

	@Column(name = "data_id")
	public String getDataId() {
		return this.dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

}