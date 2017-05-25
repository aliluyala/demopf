package com.ydy258.ydy.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * TCommData entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_comm_data")

public class TCommData  implements java.io.Serializable {


    // Fields    

     private Long id;
     private String dataName;
     private String desc;
     private Long parentId;
     private String root;
     private Long isLeaf;


    // Constructors

    /** default constructor */
    public TCommData() {
    }

	/** minimal constructor */
    public TCommData(String dataName) {
        this.dataName = dataName;
    }
    

   
    // Property accessors
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="id", unique=true, nullable=false)

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @Column(name="data_name", nullable=false)

    public String getDataName() {
        return this.dataName;
    }
    
    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
    
    @Column(name="descript")

    public String getDesc() {
        return this.desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    @Column(name="parent_id")

    public Long getParentId() {
        return this.parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    @Column(name="root")

    public String getRoot() {
        return this.root;
    }
    
    public void setRoot(String root) {
        this.root = root;
    }
    
    @Column(name="is_leaf")

    public Long getIsLeaf() {
        return this.isLeaf;
    }
    
    public void setIsLeaf(Long isLeaf) {
        this.isLeaf = isLeaf;
    }
   

}