package com.ydy258.ydy.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * TDriverComment entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_driver_comment"
    ,catalog="hyt"
)

public class TDriverComment  implements java.io.Serializable {


    // Fields    

     private Long id;
     private Short score;
     private String comment;
     private Long driverUserId;
     private Timestamp createdTime;
     private String reason;


    // Constructors

    /** default constructor */
    public TDriverComment() {
    }

	/** minimal constructor */
    public TDriverComment(Long id) {
        this.id = id;
    }
    
    /** full constructor */
    public TDriverComment(Long id, Short score, String comment, Long driverUserId, Timestamp createdTime, String reason) {
        this.id = id;
        this.score = score;
        this.comment = comment;
        this.driverUserId = driverUserId;
        this.createdTime = createdTime;
        this.reason = reason;
    }

   
    // Property accessors
    @Id 
    
    @Column(name="id", unique=true, nullable=false)

    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @Column(name="score")

    public Short getScore() {
        return this.score;
    }
    
    public void setScore(Short score) {
        this.score = score;
    }
    
    @Column(name="comment")

    public String getComment() {
        return this.comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    @Column(name="driver_user_id")

    public Long getDriverUserId() {
        return this.driverUserId;
    }
    
    public void setDriverUserId(Long driverUserId) {
        this.driverUserId = driverUserId;
    }
    
    @Column(name="created_time", length=19)

    public Timestamp getCreatedTime() {
        return this.createdTime;
    }
    
    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
    
    @Column(name="reason")

    public String getReason() {
        return this.reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
   








}