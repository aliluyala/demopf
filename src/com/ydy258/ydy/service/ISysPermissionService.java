package com.ydy258.ydy.service;

import java.util.List;

import com.ydy258.ydy.entity.Consignor;
import com.ydy258.ydy.entity.SysRole;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.Truck;


public interface ISysPermissionService extends IBaseService {
	public void update(SysRole role,String[] newPermission,String[] oldPermission) throws Exception;
	
	public String[] getNewMinus(String[] newPermission,String[] oldPermission);
	
	public String[] getOldMinus(String[] newPermission,String[] oldPermission);
	
	
	public SysUser userLogin(String userName,String password) throws Exception;
	public List queryParent() throws Exception;
	public List listByParentId(Long parentId) throws Exception;
	
	
	public List listSysPermissionByIDString(String idString) throws Exception;
	
	
	public List rolePermissionByRoleID(String roleId) throws Exception;
	
	
	public Truck truckUserLogin(String mobile,String password) throws Exception ;
	
	public Consignor consignorUserLogin(String mobile,String password) throws Exception ;
	
}
