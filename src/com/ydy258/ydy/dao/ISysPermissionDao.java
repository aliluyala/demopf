package com.ydy258.ydy.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ydy258.ydy.entity.Consignor;
import com.ydy258.ydy.entity.SysRole;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.Truck;


public interface ISysPermissionDao extends IBaseDao {
	
	public void addPermission(SysRole role,String[] newPermission);
	
	public void delPermission(SysRole role,String[] oldPermission);
	
	public SysUser userLogin(String userName,String password) throws Exception;
	public List queryParent() throws Exception;
	public List listByParentId(long parentId) throws Exception;
	
	
	public List listSysPermissionByIDString(String idString) throws Exception;
	
	
	public List rolePermissionByRoleID(String roleId) throws Exception;
	
	
	public Truck truckLogin(String mobile,String password) throws Exception;
	
	public Consignor consignorLogin(String mobile,String password) throws Exception ;
	
	
	
	
	
}
