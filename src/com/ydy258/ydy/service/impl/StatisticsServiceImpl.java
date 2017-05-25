package com.ydy258.ydy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ydy258.ydy.dao.IStatisticsDao;
import com.ydy258.ydy.entity.ProxyStatements;
import com.ydy258.ydy.entity.TCompanyTransactionStatements;
import com.ydy258.ydy.entity.TDriverTransactionStatements;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.entity.UserRechargeRequest;
import com.ydy258.ydy.service.IStatisticsService;
import com.ydy258.ydy.service.ITallyService;
import com.ydy258.ydy.util.BaseException;
import com.ydy258.ydy.util.Page;

@Service("statisticsService")
public class StatisticsServiceImpl extends IBaseServiceImpl implements IStatisticsService {
		
	private static final Logger logger  =  Logger.getLogger("ydytransaction");
	
	@Autowired
	private IStatisticsDao statisticsDao;
	@Autowired
	private ITallyService tallyService;
	
	public List newtruckUserStatistics(Map<String,String> args) throws Exception{
		return statisticsDao.newtruckUserStatistics(args);
	}
	
	public List newConsignorUserStatistics(Map<String,String> args) throws Exception{
		return statisticsDao.newConsignorUserStatistics(args);
	}
	
	public List newtruckUserStatisticsByMonth(Map<String,String> args) throws Exception{
		return statisticsDao.newtruckUserStatisticsByMonth(args);
	}
	
	public List newConsignorUserStatisticsByMonth(Map<String,String> args) throws Exception{
		return statisticsDao.newConsignorUserStatisticsByMonth(args);
	}
	
	public List newMallUserStatisticsByMonth(Map<String,String> args) throws Exception{
		return statisticsDao.newMallUserStatisticsByMonth(args);
	}
	
	public Page companyStatements(Map<String,String> args ,int currentPage,int pageSize) throws Exception{
		return statisticsDao.companyStatements(args,currentPage,pageSize);
	}
	
	public Page companyAccountBooks(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		return statisticsDao.companyAccountBooks(args, currentPage, pageSize);
	}
	
	public Page proxyStatements(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		return statisticsDao.proxyStatements(args, currentPage, pageSize);
	}
	
	/**
	 * 结算
	 * @Title:        BalanceTally 
	 * @Description:  TODO
	 * @param:        @param tradeId
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月25日 下午1:03:42
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean balanceProxyStatements(long[] tradeId) throws Exception {
		if(tradeId.length<1){
			return true;
		}
		for(long id:tradeId){
			ProxyStatements ps = this.getById(ProxyStatements.class,id);
			if(ps==null){
				throw new BaseException("ID出错！");
			}
			ps.setSuccess(true);
			statisticsDao.save(ps);
		}
		return true;
	}
	
	
	/**
	 * 结算
	 * @Title:        BalanceTally 
	 * @Description:  TODO
	 * @param:        @param tradeId
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月25日 下午1:03:42
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean balanceUserjyRechargeRequest(long tradeId,long points,double recharge) throws Exception {
		Map log = new HashMap();
		log.put("info", "记账开始！");
		log.put("method", "balanceUserjyRechargeRequest");
		logger.info(new JSONObject(log).toString());
		
		UserRechargeRequest ps = this.getById(UserRechargeRequest.class,tradeId);
		if(ps==null){
			throw new BaseException("ID出错！");
		}
		Truck truckInfo = this.getById(Truck.class,ps.getUserId());
		if(truckInfo==null){
			throw new BaseException("ID出错！");
		}
		//扣费//扣积分
		truckInfo.setBalance((truckInfo.getBalance()!=null?truckInfo.getBalance():0.0)-recharge);
		truckInfo.setPoints((truckInfo.getPoints()!=null?truckInfo.getPoints():0L)-points);
		if(truckInfo.getBalance()<0||truckInfo.getPoints()<0){
			throw new BaseException("全额或都积分不够！");
		}
		//返积分
		truckInfo.setPoints((truckInfo.getPoints()!=null?truckInfo.getPoints():0L)+ps.getReturnPoint());
		if(truckInfo.getBalance()<0||truckInfo.getPoints()<0){
			throw new BaseException("全额或都积分不够！");
		}
		//返过程专项费用
		double _balance = truckInfo.getSpecialBalance()==null?0.0:truckInfo.getSpecialBalance();
		truckInfo.setSpecialBalance(_balance+ps.getSpecialBalance());
		
		this.save(truckInfo);
		//记流水
		TDriverTransactionStatements tdts = new TDriverTransactionStatements();
		tdts.setCreatedDate(new Date());
		tdts.setAccountId(truckInfo.getId());
		tdts.setPay(-recharge);
		tdts.setBalance(truckInfo.getBalance());
		tdts.setPayPoints(-points);
		tdts.setRestPoints(truckInfo.getPoints());
		tdts.setOrderNo(ps.getOrderNo());
		tdts.setIsTurnout(true);
		tdts.setTradeType(-2);
		tdts.setRemark("油卡充值扣费");
		this.save(tdts);
		//反积分
		TDriverTransactionStatements tdts2 = new TDriverTransactionStatements();
		tdts2.setCreatedDate(new Date());
		tdts2.setAccountId(truckInfo.getId());
		tdts2.setPay(0.0);
		tdts2.setBalance(truckInfo.getBalance());
		tdts2.setPayPoints(ps.getReturnPoint());
		tdts2.setSpecialBalance(ps.getSpecialBalance());
		tdts2.setRestPoints(truckInfo.getPoints());
		tdts2.setOrderNo(ps.getOrderNo());
		tdts2.setIsTurnout(false);
		tdts2.setTradeType(-2);
		tdts2.setRemark("油卡充值返点");
		this.save(tdts2);
		
		log = new HashMap();
		log.put("info", "记账成功！");
		log.put("method", "balanceUserjyRechargeRequest");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	
	/**
	 * 结算
	 * @Title:        BalanceTally 
	 * @Description:  TODO
	 * @param:        @param tradeId
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月25日 下午1:03:42
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean balanceUsergsRechargeRequest(long tradeId,double specialBalance,double recharge) throws Exception {
		Map log = new HashMap();
		log.put("info", "记账开始！");
		log.put("method", "balanceUsergsRechargeRequest");
		logger.info(new JSONObject(log).toString());
		
		UserRechargeRequest ps = this.getById(UserRechargeRequest.class,tradeId);
		if(ps==null){
			throw new BaseException("ID出错！");
		}
		Truck truckInfo = this.getById(Truck.class,ps.getUserId());
		if(truckInfo==null){
			throw new BaseException("ID出错！");
		}
		//扣费//扣积分
		truckInfo.setBalance((truckInfo.getBalance()!=null?truckInfo.getBalance():0.0)-recharge);
		truckInfo.setSpecialBalance((truckInfo.getSpecialBalance()!=null?truckInfo.getSpecialBalance():0L)-specialBalance);
		if(truckInfo.getBalance()<0||truckInfo.getSpecialBalance()<0){
			throw new BaseException("全额或都专项费不够！");
		}
		this.save(truckInfo);
		//记流水
		TDriverTransactionStatements tdts = new TDriverTransactionStatements();
		tdts.setCreatedDate(new Date());
		tdts.setAccountId(truckInfo.getId());
		tdts.setPay(-recharge);
		tdts.setBalance(truckInfo.getBalance());
		tdts.setPayPoints(0L);
		tdts.setRestPoints(truckInfo.getPoints());
		tdts.setSpecialBalance(-specialBalance);
		tdts.setOrderNo(ps.getOrderNo());
		tdts.setIsTurnout(true);
		tdts.setTradeType(-1);
		tdts.setRemark("高速充值扣费");
		this.save(tdts);
		
		log = new HashMap();
		log.put("info", "记账成功！");
		log.put("method", "balanceUsergsRechargeRequest");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean balanceCompanyStatements(long[] tradeId) throws Exception {
		if(tradeId.length<1){
			return true;
		}
		for(long id:tradeId){
			TCompanyTransactionStatements ps = this.getById(TCompanyTransactionStatements.class,id);
			if(ps==null){
				throw new BaseException("ID出错！");
			}
			ps.setSuccess(true);
			statisticsDao.save(ps);
		}
		return true;
	}
	
	
	public Page userRechargeRequest(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		return statisticsDao.userRechargeRequest(args, currentPage, pageSize);
	}
	
	public Page waybillByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		return statisticsDao.waybillByPage(args, currentPage, pageSize);
	}
}
