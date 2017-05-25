package com.ydy258.ydy.dao;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ydy258.ydy.util.Page;


public interface IStatisticsDao extends IBaseDao {
	

	public List newtruckUserStatistics(Map<String,String> args) throws Exception;
	
	public List newConsignorUserStatistics(Map<String,String> args) throws Exception;
	
	public Page companyStatements(Map<String,String> args ,int currentPage,int pageSize) throws Exception;
	
	public Page companyAccountBooks(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public Page proxyStatements(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public Page userRechargeRequest(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public Page waybillByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	
	public List newtruckUserStatisticsByMonth(Map<String,String> args) throws Exception;
	public List newConsignorUserStatisticsByMonth(Map<String,String> args) throws Exception;
	public List newMallUserStatisticsByMonth(Map<String,String> args) throws Exception ;
}
