package com.ydy258.ydy.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydy258.ydy.dao.IApkUpdateDao;
import com.ydy258.ydy.entity.ApkJarUpdate;
import com.ydy258.ydy.entity.ApkUpdate;
import com.ydy258.ydy.service.IApkUpdateService;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.PageFactory;

@Service("apkUpdateService")
public class ApkUpdateServiceImpl extends IBaseServiceImpl implements IApkUpdateService {
		
	@Autowired
	private IApkUpdateDao apkUpdateDao;
	
	
	public Page apkByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		return apkUpdateDao.apkByPage(args, currentPage, pageSize);
    }
	
	public ApkUpdate getNewApk(String appKey) throws Exception {
		return apkUpdateDao.getNewApk(appKey);
	}

	public Page apkJarByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		return apkUpdateDao.apkJarByPage(args, currentPage, pageSize);
    }
	
	public ApkJarUpdate getNewJarApk(String appKey) throws Exception {
		return apkUpdateDao.getNewJarApk(appKey);
	}
}
