package com.ydy258.ydy.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TTransactionLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_transaction_log", catalog = "hyt")
public class TTransactionLog implements java.io.Serializable {

	// Fields

	private Long id;
	private String userId;
	
	private Date createdDate;
	private String content;

	private String tradeType;
	// Constructors

	@Column(name = "trade_type", nullable = false)
	public String getTradeType() {
		return tradeType;
	}

	
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	/** default constructor */
	public TTransactionLog() {
	}

	/** minimal constructor */
	public TTransactionLog(Long id) {
		this.id = id;
	}

	/** full constructor */
	public TTransactionLog(Long id, String userId, Timestamp createdDate,
			String content) {
		this.id = id;
		this.userId = userId;
		this.createdDate = createdDate;
		this.content = content;
	}

	// Property accessors
	@Id
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

	@Column(name = "created_date", length = 19)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "content")
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}