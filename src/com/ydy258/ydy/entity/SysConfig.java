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
@Table(name = "sys_config")
public class SysConfig implements Serializable {

	private Long id;
	
	private boolean isProduction=true;//是不是生产环境
	
	private String chinaLifeEmail;//中国人寿email
	
	private String  ddEmail;//大地email\
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column()
	public String getChinaLifeEmail() {
		return chinaLifeEmail;
	}
	public void setChinaLifeEmail(String chinaLifeEmail) {
		this.chinaLifeEmail = chinaLifeEmail;
	}
	@Column()
	public boolean isProduction() {
		return isProduction;
	}
	public void setProduction(boolean isProduction) {
		this.isProduction = isProduction;
	}
	@Column()
	public String getDdEmail() {
		return ddEmail;
	}
	public void setDdEmail(String ddEmail) {
		this.ddEmail = ddEmail;
	}
	
}