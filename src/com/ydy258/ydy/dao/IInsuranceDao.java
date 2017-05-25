package com.ydy258.ydy.dao;

import java.util.List;
import java.util.Map;

import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.util.Page;


public interface IInsuranceDao extends IBaseDao {
	public Page ddwyinsuranceListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception;


}
