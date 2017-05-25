package com.ydy258.ydy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ydy258.ydy.dao.IBaseDao;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.service.IUserService;

public class UserServiceImpl  extends IBaseServiceImpl implements  IUserService {

	@Autowired
	private IBaseDao baseDaoImpl;
	
	public List query(String userName,String password) throws Exception {
		List list = baseDaoImpl.loadBySQL(" select * from sys_user where user_name='"+userName+"' and password='"+password+"'", null, SysUser.class);
		return list;
	}
	
}
