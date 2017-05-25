package com.ydy258.ydy.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydy258.ydy.dao.ISysUserDao;
import com.ydy258.ydy.entity.SysRole;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.service.ISysUserService;
import com.ydy258.ydy.util.Page;

@Service("sysUserService")
public class SysUserServiceImpl extends IBaseServiceImpl implements ISysUserService {
		
	@Autowired
	private ISysUserDao sysUserDao;
	
	public Page roleListByPage(String search,int currentPage,int pageSize) throws Exception {
		return sysUserDao.roleListByPage(search, currentPage, pageSize);
    }
	
	public Page userListByPage(Map where,int currentPage,int pageSize) throws Exception {
		return sysUserDao.userListByPage(where, currentPage, pageSize);
    }

	public Page userPayListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		return sysUserDao.userPayListByPage(where, currentPage, pageSize);
	}
	
	public Page userAccountBook(Long userId,Short userTpye,String filter,int currentPage,int pageSize) throws Exception{
		return sysUserDao.userAccountBook(userId, userTpye, filter, currentPage, pageSize);
	}
	
	public List userList(String search) throws Exception {
		return sysUserDao.userList(search);
    }
	
	public List listRoles(String search) throws Exception {
		return sysUserDao.listRoles(search);
    }
	
	
	public SysUser queryUserByName(String name) throws Exception{
		return sysUserDao.queryUserByName(name);
	}
	
	public List listRolesByIDString(String idString) throws Exception {
		return sysUserDao.listRolesByIDString(idString);
    }

	@Override
	public Page sysLogListByPage(String search, int currentPage, int pageSize)
			throws Exception {
		return sysUserDao.sysLogListByPage(search, currentPage, pageSize);
	}


	public List listProxy(String search) throws Exception {
		return sysUserDao.listProxy(search);
	}
	
	public SysUser queryUserByParentId(String parentId) throws Exception {
		return sysUserDao.queryUserByParentId(parentId);
	}
	
	public SysRole getRoleByName(String name) throws Exception {
		return sysUserDao.getRoleByName(name);
	}
}
