package com.ydy258.ydy.entity;

import java.util.Date;
import java.util.HashSet;
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
 * SysUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "sys_user")
public class SysUser implements java.io.Serializable {

	// Fields

	private Long id;
	private String userName;
	private String password;
	private String realName;
	private String empNumber;
	private String department;
	private String position;
	private Date createdDate;
	private String status;
	private Set<SysRole> roles = new HashSet<SysRole>();
	private Long parentId;
	
	private Long departmentId;
	
	
	private Boolean proxyAdmin;
	
	
	private Double balance=0.0;
	
	private String mobile;
	
	@Column()
	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	@Column()
	public Double getBalance() {
		return balance;
	}


	public void setBalance(Double balance) {
		this.balance = balance;
	}


	@Column()
	public Boolean getProxyAdmin() {
		return proxyAdmin;
	}


	public void setProxyAdmin(Boolean proxyAdmin) {
		this.proxyAdmin = proxyAdmin;
	}

	private Boolean mobileUser=false;
	
	@Column()
	public Boolean getMobileUser() {
		
		return mobileUser;
	}


	public void setMobileUser(Boolean mobileUser) {
		this.mobileUser = mobileUser;
	}
	
	@Column()
	public Long getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	// Constructors
	@Column()
	 public Long getParentId() {
		return parentId;
	}


	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}


	@ManyToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
     @JoinTable(
         name="sys_user_role",
         joinColumns=@JoinColumn(name="user_id"),
         inverseJoinColumns=@JoinColumn(name="role_id")
     )
	public Set<SysRole> getRoles() {
		return roles;
	}

	
	public void setRoles(Set<SysRole> roles) {
		this.roles = roles;
	}

	/** default constructor */
	public SysUser() {
	}

	/** full constructor */
	public SysUser(String userName, String password, String realName,
			String empNumber, String department, String position,
			Date createdDate, String status) {
		this.userName = userName;
		this.password = password;
		this.realName = realName;
		this.empNumber = empNumber;
		this.department = department;
		this.position = position;
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

	@Column(name = "user_name")
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "password")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "real_name")
	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Column(name = "emp_number")
	public String getEmpNumber() {
		return this.empNumber;
	}

	public void setEmpNumber(String empNumber) {
		this.empNumber = empNumber;
	}

	@Column(name = "department")
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "position")
	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
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