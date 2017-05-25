package com.ydy258.ydy.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ydy258.ydy.entity.TDriverTransactionStatements;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.entity.UserRechargeRequest;
import com.ydy258.ydy.util.BaseException;
import com.ydy258.ydy.util.Page;


public interface IStatisticsService extends IBaseService {
	
	public List newtruckUserStatistics(Map<String,String> args) throws Exception;
	
	public List newConsignorUserStatistics(Map<String,String> args) throws Exception;
	
	public Page companyStatements(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public Page companyAccountBooks(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public Page proxyStatements(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public boolean balanceProxyStatements(long[] tradeId) throws Exception ;
	
	public boolean balanceCompanyStatements(long[] tradeId) throws Exception;
	
	public Page userRechargeRequest(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	
	public boolean balanceUserjyRechargeRequest(long tradeId,long points,double recharge) throws Exception;
	public boolean balanceUsergsRechargeRequest(long tradeId,double specialBalance,double recharge) throws Exception;
	
	
	public Page waybillByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public List newtruckUserStatisticsByMonth(Map<String,String> args) throws Exception;
	
	public List newConsignorUserStatisticsByMonth(Map<String,String> args) throws Exception;
	
	public List newMallUserStatisticsByMonth(Map<String,String> args) throws Exception;
}
