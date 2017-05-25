package com.ydy258.ydy.dao;

import java.util.List;
import java.util.Map;

import com.ydy258.ydy.entity.SysRole;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.util.Page;


public interface ISysUserDao extends IBaseDao {
	public Page roleListByPage(String search,int currentPage,int pageSize) throws Exception;
	
	public Page userListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception;

	public List userList(String search) throws Exception ;
	
	public SysUser queryUserByName(String name) throws Exception;
	
	public List listRoles(String search) throws Exception ;
	
	public List listRolesByIDString(String idString) throws Exception;
	
	public Page sysLogListByPage(String search,int currentPage,int pageSize) throws Exception;
	
	public List listProxy(String search) throws Exception;
	
	public SysRole getRoleByName(String name) throws Exception;
	
	public Page userPayListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception ;
	
	public SysUser queryUserByParentId(String parentId) throws Exception ;
	
	public Page userAccountBook(Long userId,Short userTpye,String filter,int currentPage,int pageSize) throws Exception;

}
