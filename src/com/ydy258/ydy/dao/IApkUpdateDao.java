package com.ydy258.ydy.dao;

import java.util.Map;

import com.ydy258.ydy.entity.ApkJarUpdate;
import com.ydy258.ydy.entity.ApkUpdate;
import com.ydy258.ydy.util.Page;


public interface IApkUpdateDao extends IBaseDao {
	
	public Page apkByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public ApkUpdate getNewApk(String appKey) throws Exception ;
	
	public ApkJarUpdate getNewJarApk(String appKey) throws Exception ;
	
	public Page apkJarByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception;

}
