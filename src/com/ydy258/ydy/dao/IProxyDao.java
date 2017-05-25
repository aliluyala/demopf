package com.ydy258.ydy.dao;

import java.util.Map;

import com.ydy258.ydy.entity.TInsurance;
import com.ydy258.ydy.util.Page;


public interface IProxyDao extends IBaseDao {
	
	public Page insuranceByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public Page idCardqueryOrderByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public Page proxyConfigListByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	
	public Page insuranceChinaLifeByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception ;
	
	public TInsurance getByParentId(long parentId) throws Exception ;

}
