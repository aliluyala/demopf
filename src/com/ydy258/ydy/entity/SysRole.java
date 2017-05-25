package com.ydy258.ydy.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * SysRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_role")
public class SysRole implements java.io.Serializable {

	// Fields

	private Long id;
	private String roleName;
	private Date createdDate;
	private String status;
	
	private Set<SysPermission> permissions = new HashSet<SysPermission>();

	public List<SysPermission> buttons = new ArrayList<SysPermission>();
	// Constructors



	@ManyToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
        name="sys_role_permission",
        joinColumns=@JoinColumn(name="role_id"),
        inverseJoinColumns=@JoinColumn(name="permission_id")
    )
	public Set<SysPermission> getPermissions() {
		return permissions;
	}

	
	public void setPermissions(Set<SysPermission> permissions) {
		this.permissions = permissions;
	}

	/** default constructor */
	public SysRole() {
	}

	/** full constructor */
	public SysRole(String roleName, Timestamp createdDate, String status) {
		this.roleName = roleName;
		this.createdDate = createdDate;
		this.status = status;
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

	@Column(name = "role_name")
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "created_date", length = 19)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "status")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}