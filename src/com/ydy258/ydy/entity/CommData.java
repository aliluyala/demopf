package com.ydy258.ydy.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * DataDictionary entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_comm_data")
public class CommData{

	// Fields

	private Long id;
	private Long parentId;
	private String name;
	private String status;

	private String root;
	
	private int isLeaf;
	@Column(name = "is_leaf")
	public int getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(int isLeaf) {
		this.isLeaf = isLeaf;
	}

	@Column(name = "root")
	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	private String typeDesc;
	// Constructors

	@Column(name = "descript")
	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	/** default constructor */
	public CommData() {
	}

	/** full constructor */
	public CommData(Long parentId, String name, String status) {
		this.parentId = parentId;
		this.name = name;
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

	@Column(name = "parent_id")
	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "data_name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "status")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}