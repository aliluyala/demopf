package com.ydy258.ydy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysPermission entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_permission")
public class SysPermission implements java.io.Serializable {

	// Fields

	private Long id;
	private String permissionName;
	private String permissionUrl;
	private Long parentId;
	private String status;
	private String isLeaf;

	// Constructors

	/** default constructor */
	public SysPermission() {
	}

	/** full constructor */
	public SysPermission(String permissionName, String permissionUrl,
			Long parentId, String status, String isLeaf) {
		this.permissionName = permissionName;
		this.permissionUrl = permissionUrl;
		this.parentId = parentId;
		this.status = status;
		this.isLeaf = isLeaf;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "permission_name")
	public String getPermissionName() {
		return this.permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	@Column(name = "permission_url")
	public String getPermissionUrl() {
		return this.permissionUrl;
	}

	public void setPermissionUrl(String permissionUrl) {
		this.permissionUrl = permissionUrl;
	}

	@Column(name = "parent_id")
	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "status")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "is_leaf")
	public String getIsLeaf() {
		return this.isLeaf;
	}

	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

}