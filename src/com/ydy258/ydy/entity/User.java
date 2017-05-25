package com.ydy258.ydy.entity;
import java.io.Serializable;
public class User implements Serializable {
    private Long id;
    private String name;
    //ʡ��getter/setter��
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}