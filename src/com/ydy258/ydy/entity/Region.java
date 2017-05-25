package com.ydy258.ydy.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "t_region")
public class Region implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5769228815513264444L;

	public enum EnumRegionType {
		NATION, PROVINCE, CITY, AERA
	}
	
	private Integer parentId;
	private Integer regionId;	
	private String regionName;
	private EnumRegionType regionType;
	private Integer agencyId;
	/** �ϼ����� */
	private Region parent;

	/** �¼����� */
	private Set<Region> children = new HashSet<Region>();
	
	@Id
	@GeneratedValue
	@Column(name="region_id")
	public Integer getRegionId() {
		return regionId;
	}
	
	@Column(name="parent_id", nullable=false)
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}	

	@Column(name="region_name", length=120, nullable=false)
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	@Column(name="region_type", nullable=false)
	@Enumerated(EnumType.ORDINAL)
	public EnumRegionType getRegionType() {
		return regionType;
	}

	public void setRegionType(EnumRegionType regionType) {
		this.regionType = regionType;
	}
	
	@Column(name="agency_id", nullable=false)
	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}	
	/**
	 * ͨ��region_id�����ϼ�����
	 * @return
	 */
	@ManyToOne(targetEntity=Region.class, fetch = FetchType.LAZY)
	@JoinColumn(name="region_id", updatable=false, insertable=false)
	public Region getParent() {
		return parent;
	}

	/**
	 * �����ϼ�����
	 * 
	 * @param parent
	 *            �ϼ�����
	 */
	public void setParent(Region parent) {
		this.parent = parent;
	}

	/**
	 * ��ȡ�¼�����
	 * ͨ��parent_id����ȡ���¼�����
	 * @return �¼�����
	 */
	@OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)	
	public Set<Region> getChildren() {
		return children;
	}

	/**
	 * �����¼�����
	 * 
	 * @param children
	 *            �¼�����
	 */
	public void setChildren(Set<Region> children) {
		this.children = children;
	}
	
	public Region() {		
	}
}