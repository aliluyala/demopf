package com.ydy258.ydy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.Setting;
import com.ydy258.ydy.dao.ITallyDao;
import com.ydy258.ydy.entity.BusinessRules;
import com.ydy258.ydy.entity.Consignor;
import com.ydy258.ydy.entity.ProxyStatements;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.TCargoTransactionStatements;
import com.ydy258.ydy.entity.TCompanyTransactionStatements;
import com.ydy258.ydy.entity.TDriverTransactionStatements;
import com.ydy258.ydy.entity.TProxyTransactionOrder;
import com.ydy258.ydy.entity.TProxyTransactionStatements;
import com.ydy258.ydy.entity.TUserOrder;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.entity.UserRechargeRequest;
import com.ydy258.ydy.service.ICommDataService;
import com.ydy258.ydy.service.ITallyService;
import com.ydy258.ydy.util.BaseException;
import com.ydy258.ydy.util.DateUtil;
import com.ydy258.ydy.util.SettingUtils;
import com.ydy258.ydy.util.utils;

@Service("tallyService")
public class TallyServiceImpl extends IBaseServiceImpl implements ITallyService {
	
	private static Setting setting = SettingUtils.get();
	private static final Logger logger  =  Logger.getLogger("ydytransaction");
	
	@Autowired
	private ITallyDao tallyDao;

	@Autowired
	private ICommDataService commDataService;
	/**
	 * 保存user-Order 映射
	 * @Title:        saveUserOrder 
	 * @Description:  TODO
	 * @param:        @param userId 用户ID
	 * @param:        @param userType 用户类型 1，货主 2，车主
	 * @param:        @param noOrder 唯一订单号
	 * @param:        @param status 状态
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月18日 上午10:45:06
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean saveUserOrder(Long userId,Short userType,String noOrder,int rechargeType) throws Exception{
		if(userId==null||userType==null||noOrder==null){
			throw new BaseException("参数错误");
		}
		if(userType!=Constants.UserType.consignorUser.getType()&&
				userType!=Constants.UserType.TruckUser.getType()){
			throw new BaseException("参数错误");
		}
		if(rechargeType!=-1&&rechargeType!=-2&&rechargeType!=1){
			throw new BaseException("参数错误");
		}
		TUserOrder uo = new TUserOrder();
		uo.setCreatedTime(new Date());
		uo.setStatus(Constants.OrderUserStatus.sendStatus.getStatus());
		uo.setNoOrder(noOrder);
		uo.setUserType(userType);
		uo.setUserId(userId);
		uo.setRechargeType(rechargeType);
		this.save(uo);
		return true;
	}
	/**
	 * 记账功能
	 * userId 用户ID
	 * userType 用户类型 1，货主 （Constants.UserType.consignorUser.getType()）2，车主（Constants.UserType.TruckUser.getType()）
	 * money:交易数 正数是账号上加，负数是扣去 0:表示不变
	 * payPoints : 交易积分数   正数是账号上加，负数是扣去 0:表示不变
	 * tradeType 交易类型（Constants.TradeType.recharge.getType()）
	 * tradeTypeDescript 交易描述  Constants.TradeType.recharge.getZHName()
	 * orderNo : 订单编号
	 * isTurnout : 相对于用户  支出为true,收入为false
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean tally(Long userId,
						Short userType,
						Double money,
						Long payPoints,
						Integer tradeType,
						String tradeTypeDescript,
						String orderNo,
						boolean isTurnout) throws Exception {
		Map log = new HashMap();
		log.put("info", "记账开始！");
		logger.info(new JSONObject(log).toString());
		//记录参数
		Map args = new HashMap();
		args.put("userId", userId);
		args.put("userType", userType);
		args.put("money", money);
		args.put("payPoints", payPoints);
		args.put("tradeType", tradeType);
		args.put("orderNo", orderNo);
		args.put("isTurnout", isTurnout);
		args.put("tradeTypeDescript", tradeTypeDescript);
		
		logger.info(new JSONObject(args).toString());
		
		//验证参数
		if(userId==null||
				userType==null||
				tradeType==null||
				StringUtils.isBlank(tradeTypeDescript)||
				StringUtils.isBlank(orderNo)){
			throw new BaseException("必要参数为空！");
		}
		if(userType!=Constants.UserType.consignorUser.getType()&&
				userType!=Constants.UserType.TruckUser.getType()){
			throw new BaseException("用户类型参数出错！");
		}
		
		//如果是货主
		if(userType==Constants.UserType.consignorUser.getType()){
			//修改用户余额
			Consignor consignor = tallyDao.getById(Consignor.class,userId);
			consignor.setBalance((consignor.getBalance()!=null?consignor.getBalance():0.0)+money);
			consignor.setPoints((consignor.getPoints()!=null?consignor.getPoints():0L)+payPoints);
			if(consignor.getBalance()<0||consignor.getPoints()<0){
				throw new BaseException("全额或都积分不够！");
			}
			this.save(consignor);
			//记流水
			TCargoTransactionStatements tcts = new TCargoTransactionStatements();
			tcts.setCreatedDate(new Date());
			tcts.setAccountId(consignor.getId());
			tcts.setPay(money);
			tcts.setBalance(consignor.getBalance());
			tcts.setOrderNo(orderNo);
			tcts.setPayPoints(payPoints);
			tcts.setRestPoints(consignor.getPoints());
			tcts.setIsTurnout(isTurnout);
			tcts.setTradeType(tradeType);
			tcts.setRemark(tradeTypeDescript);
			this.save(tcts);
			String sendMsg = "收支明细";
			//推送信息给用户
			commDataService.push2Consignor(consignor.getJpushId(), sendMsg, tcts.getId(),Integer.valueOf(consignor.getMobileType()));

		}
		//如果是车主
		if(userType==Constants.UserType.TruckUser.getType()){
			//修改用户余额
			Truck truckInfo = tallyDao.getById(Truck.class,userId);
			truckInfo.setBalance((truckInfo.getBalance()!=null?truckInfo.getBalance():0.0)+money);
			truckInfo.setPoints((truckInfo.getPoints()!=null?truckInfo.getPoints():0L)+payPoints);
			if(truckInfo.getBalance()<0||truckInfo.getPoints()<0){
				throw new BaseException("全额或都积分不够！");
			}
			this.save(truckInfo);
			//记流水
			TDriverTransactionStatements tdts = new TDriverTransactionStatements();
			tdts.setCreatedDate(new Date());
			tdts.setAccountId(truckInfo.getId());
			tdts.setPay(money);
			tdts.setBalance(truckInfo.getBalance());
			
			tdts.setPayPoints(payPoints);
			tdts.setRestPoints(truckInfo.getPoints());
			tdts.setOrderNo(orderNo);
			tdts.setIsTurnout(isTurnout);
			tdts.setTradeType(tradeType);
			tdts.setRemark(tradeTypeDescript);
			this.save(tdts);
			String sendMsg = "收支明细";
			//推送信息给用户
			commDataService.push2Truck(truckInfo.getJpushId(), sendMsg, tdts.getId(),Integer.valueOf(truckInfo.getMobileType()));

		}
		//记录公司流水
		if(tradeType!=Constants.TradeType.recharge.getType()){
			TCompanyTransactionStatements tcts = new TCompanyTransactionStatements();
			tcts.setCreatedDate(new Date());
			tcts.setAccountId(userId);
			tcts.setUserType(userType);
			tcts.setPay(-money);
			tcts.setOrderNo(orderNo);
			tcts.setIsTurnout(!isTurnout);//对于公司账单为 收入
			tcts.setTradeType(tradeType);
			tcts.setRemark(tradeTypeDescript);
			this.save(tcts);
		}
		log = new HashMap();
		log.put("info", "记账成功！");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	
	
	/**
	 * 记账功能  (计算成本与利润)
	 * userId 用户ID
	 * userType 用户类型 1，货主 （Constants.UserType.consignorUser.getType()）2，车主（Constants.UserType.TruckUser.getType()）
	 * money:交易数 正数是账号上加，负数是扣去 0:表示不变
	 * 成本:交易数 正数是账号上加，负数是扣去 0:表示不变
	 * payPoints : 交易积分数   正数是账号上加，负数是扣去 0:表示不变
	 * tradeType 交易类型（Constants.TradeType.recharge.getType()）
	 * tradeTypeDescript 交易描述  Constants.TradeType.recharge.getZHName()
	 * orderNo : 订单编号
	 * isTurnout : 相对于用户  支出为true,收入为false
	 */                  
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean tally(Long userId,Short userType,Double money,Double cost,Long payPoints,Integer tradeType,String tradeTypeDescript,String orderNo,boolean isTurnout) throws Exception {
		
		Map log = new HashMap();
		log.put("info", "记账开始！");
		logger.info(new JSONObject(log).toString());
		//记录参数
		Map args = new HashMap();
		args.put("userId", userId);
		args.put("userType", userType);
		args.put("money", money);
		args.put("cost", cost);
		args.put("payPoints", payPoints);
		args.put("tradeType", tradeType);
		args.put("orderNo", orderNo);
		args.put("isTurnout", isTurnout);
		args.put("tradeTypeDescript", tradeTypeDescript);
		
		logger.info(new JSONObject(args).toString());
		
		//验证参数
		if(userId==null||userType==null||tradeType==null||StringUtils.isBlank(tradeTypeDescript)||StringUtils.isBlank(orderNo)){
			throw new BaseException("必要参数为空！");
		}
		if(userType!=Constants.UserType.consignorUser.getType()&&userType!=Constants.UserType.TruckUser.getType()){
			throw new BaseException("用户类型参数出错！");
		}
		
		//如果是货主
		if(userType==Constants.UserType.consignorUser.getType()){
			//修改用户余额
			Consignor consignor = tallyDao.getById(Consignor.class,userId);
			consignor.setBalance((consignor.getBalance()!=null?consignor.getBalance():0.0)+money);
			consignor.setPoints((consignor.getPoints()!=null?consignor.getPoints():0L)+payPoints);
			if(consignor.getBalance()<0||consignor.getPoints()<0){
				throw new BaseException("全额或都积分不够！");
			}
			this.save(consignor);
			//记流水
			TCargoTransactionStatements tcts = new TCargoTransactionStatements();
			tcts.setCreatedDate(new Date());
			tcts.setAccountId(consignor.getId());
			tcts.setPay(money);
			tcts.setBalance(consignor.getBalance());
			tcts.setOrderNo(orderNo);
			tcts.setPayPoints(payPoints);
			tcts.setRestPoints(consignor.getPoints());
			tcts.setIsTurnout(isTurnout);
			tcts.setTradeType(tradeType);
			tcts.setRemark(tradeTypeDescript);
			this.save(tcts);
			String sendMsg = "收支明细";
			//推送信息给用户
			commDataService.push2Consignor(consignor.getJpushId(), sendMsg, tcts.getId(),Integer.valueOf(consignor.getMobileType()));

		}
		//如果是车主
		if(userType==Constants.UserType.TruckUser.getType()){
			//修改用户余额
			Truck truckInfo = tallyDao.getById(Truck.class,userId);
			truckInfo.setBalance((truckInfo.getBalance()!=null?truckInfo.getBalance():0.0)+money);
			truckInfo.setPoints((truckInfo.getPoints()!=null?truckInfo.getPoints():0L)+payPoints);
			if(truckInfo.getBalance()<0||truckInfo.getPoints()<0){
				throw new BaseException("全额或都积分不够！");
			}
			this.save(truckInfo);
			//记流水
			TDriverTransactionStatements tdts = new TDriverTransactionStatements();
			tdts.setCreatedDate(new Date());
			tdts.setAccountId(truckInfo.getId());
			tdts.setPay(money);
			tdts.setBalance(truckInfo.getBalance());
			
			tdts.setPayPoints(payPoints);
			tdts.setRestPoints(truckInfo.getPoints());
			tdts.setOrderNo(orderNo);
			tdts.setIsTurnout(isTurnout);
			tdts.setTradeType(tradeType);
			tdts.setRemark(tradeTypeDescript);
			this.save(tdts);
			String sendMsg = "收支明细";
			//推送信息给用户
			commDataService.push2Truck(truckInfo.getJpushId(), sendMsg, tdts.getId(),Integer.valueOf(truckInfo.getMobileType()));

		}
		
		//记录公司流水
		if(tradeType!=Constants.TradeType.recharge.getType()){
			TCompanyTransactionStatements tcts = new TCompanyTransactionStatements();
			tcts.setCreatedDate(new Date());
			tcts.setAccountId(userId);
			tcts.setUserType(userType);
			tcts.setPay(-money);
			tcts.setCost(cost);
			tcts.setProfit(-cost-money);//利润计算
			tcts.setOrderNo(orderNo);
			tcts.setIsTurnout(!isTurnout);//对于公司账单为 收入
			tcts.setTradeType(tradeType);
			tcts.setRemark(tradeTypeDescript);
			this.save(tcts);
		}
				
		log = new HashMap();
		log.put("info", "记账成功！");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	
	
	/**
	 * 记账功能  (计算成本与利润)
	 * userId 用户ID
	 * userType 用户类型 1，货主 （Constants.UserType.consignorUser.getType()）2，车主（Constants.UserType.TruckUser.getType()）
	 * money:交易数 正数是账号上加，负数是扣去 0:表示不变
	 * 成本:交易数 正数是账号上加，负数是扣去 0:表示不变
	 * payPoints : 交易积分数   正数是账号上加，负数是扣去 0:表示不变
	 * tradeType 交易类型（Constants.TradeType.recharge.getType()）
	 * tradeTypeDescript 交易描述  Constants.TradeType.recharge.getZHName()
	 * orderNo : 订单编号
	 * isTurnout : 相对于用户  支出为true,收入为false
	 */                  
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean tally(
			Long userId, //用户ID
			Short userType,//用户类型
			Double money,//支付钱数
			Double cost,//公司价格
			Long proxyUserId,//代理ID 填谁 就将记账到哪 个代理下
			Double proxyPrice,//代理价格
			Long payPoints,//支付点数
			Integer tradeType,//交易类型
			String tradeTypeDescript,//交易描述
			String orderNo,//订单号
			boolean isTurnout//是否是支出
						) throws Exception {
		
		Map log = new HashMap();
		log.put("info", "记账开始！");
		logger.info(new JSONObject(log).toString());
		//记录参数
		Map args = new HashMap();
		args.put("userId", userId);
		args.put("userType", userType);
		args.put("money", money);
		args.put("cost", cost);
		args.put("payPoints", payPoints);
		args.put("tradeType", tradeType);
		args.put("proxyUserId", proxyUserId);
		args.put("proxyPrice", proxyPrice);
		args.put("orderNo", orderNo);
		args.put("isTurnout", isTurnout);
		args.put("tradeTypeDescript", tradeTypeDescript);
		
		logger.info(new JSONObject(args).toString());
		
		//验证参数
		if(userId==null||userType==null||tradeType==null||StringUtils.isBlank(tradeTypeDescript)||StringUtils.isBlank(orderNo)){
			throw new BaseException("必要参数为空！");
		}
		if(userType!=Constants.UserType.consignorUser.getType()&&userType!=Constants.UserType.TruckUser.getType()){
			throw new BaseException("用户类型参数出错！");
		}
		
		//如果是货主
		if(userType==Constants.UserType.consignorUser.getType()){
			//修改用户余额
			Consignor consignor = tallyDao.getById(Consignor.class,userId);
			consignor.setBalance((consignor.getBalance()!=null?consignor.getBalance():0.0)+money);
			consignor.setPoints((consignor.getPoints()!=null?consignor.getPoints():0L)+payPoints);
			if(consignor.getBalance()<0||consignor.getPoints()<0){
				throw new BaseException("全额或都积分不够！");
			}
			this.save(consignor);
			//记流水
			TCargoTransactionStatements tcts = new TCargoTransactionStatements();
			tcts.setCreatedDate(new Date());
			tcts.setAccountId(consignor.getId());
			tcts.setPay(money);
			tcts.setBalance(consignor.getBalance());
			tcts.setOrderNo(orderNo);
			tcts.setPayPoints(payPoints);
			tcts.setRestPoints(consignor.getPoints());
			tcts.setProxyPrice(proxyPrice);
			tcts.setProxyUserId(proxyUserId);
			tcts.setIsTurnout(isTurnout);
			tcts.setTradeType(tradeType);
			tcts.setRemark(tradeTypeDescript);
			this.save(tcts);
			String sendMsg = "收支明细";
			//推送信息给用户
			commDataService.push2Consignor(consignor.getJpushId(), sendMsg, tcts.getId(),Integer.valueOf(consignor.getMobileType()));

		}
		//如果是车主
		if(userType==Constants.UserType.TruckUser.getType()){
			//修改用户余额
			Truck truckInfo = tallyDao.getById(Truck.class,userId);
			truckInfo.setBalance((truckInfo.getBalance()!=null?truckInfo.getBalance():0.0)+money);
			truckInfo.setPoints((truckInfo.getPoints()!=null?truckInfo.getPoints():0L)+payPoints);
			if(truckInfo.getBalance()<0||truckInfo.getPoints()<0){
				throw new BaseException("全额或都积分不够！");
			}
			this.save(truckInfo);
			//记流水
			TDriverTransactionStatements tdts = new TDriverTransactionStatements();
			tdts.setCreatedDate(new Date());
			tdts.setAccountId(truckInfo.getId());
			tdts.setPay(money);
			tdts.setBalance(truckInfo.getBalance());
			
			tdts.setPayPoints(payPoints);
			tdts.setRestPoints(truckInfo.getPoints());
			tdts.setProxyPrice(proxyPrice);
			tdts.setProxyUserId(proxyUserId);
			tdts.setOrderNo(orderNo);
			tdts.setIsTurnout(isTurnout);
			tdts.setTradeType(tradeType);
			tdts.setRemark(tradeTypeDescript);
			this.save(tdts);
			String sendMsg = "收支明细";
			//推送信息给用户
			commDataService.push2Truck(truckInfo.getJpushId(), sendMsg, tdts.getId(),Integer.valueOf(truckInfo.getMobileType()));

		}
		
		//记录公司流水
		if(tradeType!=Constants.TradeType.recharge.getType()){
			TCompanyTransactionStatements tcts = new TCompanyTransactionStatements();
			tcts.setCreatedDate(new Date());
			tcts.setAccountId(userId);
			tcts.setUserType(userType);
			tcts.setProxyPrice(proxyPrice);
			tcts.setProxyUserId(proxyUserId);
			tcts.setPay(-money);
			tcts.setCost(cost);
			tcts.setProfit(-cost-money);//利润计算
			tcts.setOrderNo(orderNo);
			tcts.setIsTurnout(!isTurnout);//对于公司账单为 收入
			tcts.setTradeType(tradeType);
			tcts.setRemark(tradeTypeDescript);
			this.save(tcts);
		}
				
		log = new HashMap();
		log.put("info", "记账成功！");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	
	
	/**
	 *                  
	 * @Title:        addExperTimeTally 后台用户给会员增加服务年限
	 * @Description:  TODO
	 * @param:        @param sysUserId 操作的ID
	 * @param:        @param sysUserName 操作的用户名称
	 * @param:        @param proxyUserId 代理ID
	 * @param:        @param proxyUserName 代理名称
	 * @param:        @param memberId 会员ID
	 * @param:        @param memberName
	 * @param:        @param userType
	 * @param:        @param years
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月23日 下午3:22:53
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean addExperTimeTally(
								long sysUserId,
								String sysUserName,
								Long proxyUserId,
								String proxyUserName,
								long memberId,
								String memberName,
								short userType,
								int years) throws Exception {
		String orderNo = utils.generaterOrderNumber();
		Map log = new HashMap();
		log.put("info", "记账开始！");
		log.put("method", "addExperTimeTally");
		logger.info(new JSONObject(log).toString());
		//记录参数
		Map args = new HashMap();
		args.put("sysUserId", sysUserId);
		args.put("sysUserName", sysUserName);
		args.put("proxyUserId", proxyUserId);
		args.put("proxyUserName", proxyUserName);
		args.put("memberId", memberId);
		args.put("memberName", memberName);
		args.put("userType", userType);
		args.put("years", years);
		args.put("orderNo", orderNo);
		log.put("method", "addExperTimeTally");
		logger.info(new JSONObject(args).toString());
		//检查参数
		if(userType!=Constants.UserType.consignorUser.getType()&&
				userType!=Constants.UserType.TruckUser.getType()){
			throw new BaseException("用户类型参数出错！");
		}
		SysUser sysUser = this.getById(SysUser.class,sysUserId);
		if(sysUser==null){
			throw new BaseException("用户ID出错！");
		}
		//扣代理的费用
		double balance_ = sysUser.getBalance()==null?0.0:sysUser.getBalance();
		sysUser.setBalance(balance_-setting.getYearPay());
		if(sysUser.getBalance()<0){
			throw new BaseException("余额不足！");
		}
		this.save(sysUser);
		//记账
		ProxyStatements ps = new ProxyStatements();
		ps.setType(1);//服务时间是2
		ps.setUserType(userType);
		ps.setCreatedDate(new Date());
		ps.setMemberId(memberId);
		ps.setMemberName(memberName);
		ps.setSysUserId(sysUserId);
		ps.setSysUserName(sysUserName);
		ps.setProxyUserId(proxyUserId);
		ps.setProxyUserName(proxyUserName);
		ps.setOrderNo(orderNo);
		ps.setYears(years);
		this.save(ps);
		//记代理流水
		TProxyTransactionStatements tcts1 = new TProxyTransactionStatements();
		tcts1.setCreatedDate(new Date());
		tcts1.setAccountId(sysUser.getId());
		tcts1.setUserId(memberId);
		tcts1.setUserType(userType);
		tcts1.setBalance(sysUser.getBalance());
		tcts1.setPay(-setting.getYearPay());
		tcts1.setOrderNo(orderNo);
		tcts1.setIsTurnout(true);
		tcts1.setTradeType(Constants.TradeType.proxy4User.getType());
		tcts1.setRemark(Constants.TradeType.proxy4User.getZHName());
		this.save(tcts1);
		//增加服务年限
		//如果是货主
		if(userType==Constants.UserType.consignorUser.getType()){
			long payPoints = 0L;
			
			Consignor consignor = this.getById(Consignor.class,memberId);
			if(consignor==null){
				throw new BaseException("用户ID出错！");
			}
			if(consignor.getAddExpTime()==null||consignor.getAddExpTime()==false){
				payPoints = 0;//送0
			}
			consignor.setExpireTime(DateUtil.addMonth(consignor.getExpireTime(), years*12));
			consignor.setPoints((consignor.getPoints()!=null?consignor.getPoints():0L)+payPoints);
			consignor.setAddExpTime(true);
			this.save(consignor);
			//记录流水
			TCargoTransactionStatements tcts = new TCargoTransactionStatements();
			tcts.setCreatedDate(new Date());
			tcts.setAccountId(consignor.getId());
			tcts.setPay(0.0);
			tcts.setBalance(consignor.getBalance());
			tcts.setRestPoints(consignor.getPoints());
			tcts.setOrderNo(orderNo);
			tcts.setPayPoints(payPoints);
			tcts.setIsTurnout(false);
			tcts.setTradeType(Constants.TradeType.experTime.getType());
			tcts.setRemark(Constants.TradeType.experTime.getZHName());
		}
		//如果是车主
		if(userType==Constants.UserType.TruckUser.getType()){
			long payPoints = 0L;
			Truck truck = this.getById(Truck.class,memberId);
			if(truck==null){
				throw new BaseException("用户ID出错！");
			}
			if(truck.getAddExpTime()==null||truck.getAddExpTime()==false){
				payPoints = 0;//送0
			}
			truck.setExpireTime(DateUtil.addMonth(truck.getExpireTime(), years*12));
			truck.setPoints((truck.getPoints()!=null?truck.getPoints():0L)+payPoints);
			truck.setAddExpTime(true);
			this.save(truck);
			//记录流水
			TDriverTransactionStatements tdts = new TDriverTransactionStatements();
			tdts.setCreatedDate(new Date());
			tdts.setAccountId(truck.getId());
			tdts.setPay(0.0);
			tdts.setBalance(truck.getBalance());
			tdts.setRestPoints(truck.getPoints());
			tdts.setOrderNo(orderNo);
			tdts.setPayPoints(payPoints);
			tdts.setIsTurnout(false);
			tdts.setTradeType(Constants.TradeType.experTime.getType());
			tdts.setRemark(Constants.TradeType.experTime.getZHName());
			this.save(tdts);
		}
		log = new HashMap();
		log.put("info", "记账成功！");
		log.put("method", "addExperTimeTally");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	
	/**
	 *                  
	 * @Title:        addExperTimeTally 后台用户给会员增加服务年限
	 * @Description:  TODO
	 * @param:        @param sysUserId 操作的ID
	 * @param:        @param sysUserName 操作的用户名称
	 * @param:        @param proxyUserId 代理ID
	 * @param:        @param proxyUserName 代理名称
	 * @param:        @param memberId 会员ID
	 * @param:        @param memberName
	 * @param:        @param userType
	 * @param:        @param years
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月23日 下午3:22:53
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean addPointsTally(
								long memberId,
								short userType,
								long points) throws Exception {
		String orderNo = utils.generaterOrderNumber();
		Map log = new HashMap();
		log.put("info", "记账开始！");
		log.put("method", "addPointsTally");
		logger.info(new JSONObject(log).toString());
		//记录参数
		Map args = new HashMap();
		args.put("memberId", memberId);
		args.put("userType", userType);
		args.put("orderNo", orderNo);
		log.put("method", "addPointsTally");
		logger.info(new JSONObject(args).toString());
		//检查参数
		if(userType!=Constants.UserType.consignorUser.getType()&&
				userType!=Constants.UserType.TruckUser.getType()||points<0){
			throw new BaseException("用户类型参数出错！");
		}
		
		//如果是货主
		if(userType==Constants.UserType.consignorUser.getType()){
			Consignor consignor = this.getById(Consignor.class,memberId);
			if(consignor==null){
				throw new BaseException("用户ID出错！");
			}
			//充值
			long points_ = consignor.getPoints()==null?0L:consignor.getPoints();
			consignor.setPoints(points_+points);
			this.save(consignor);
			//记流水
			TCargoTransactionStatements tcts = new TCargoTransactionStatements();
			tcts.setCreatedDate(new Date());
			tcts.setAccountId(consignor.getId());
			tcts.setPay(0.0);
			tcts.setBalance(consignor.getBalance());
			tcts.setRestPoints(consignor.getPoints());
			tcts.setOrderNo(orderNo);
			tcts.setPayPoints(points);
			tcts.setIsTurnout(false);
			tcts.setTradeType(20);
			tcts.setRemark("注册赠送！");
			this.save(tcts);
		}
		//如果是车主
		if(userType==Constants.UserType.TruckUser.getType()){
			Truck truck = this.getById(Truck.class,memberId);
			if(truck==null){
				throw new BaseException("用户ID出错！");
			}
			long points_ = truck.getPoints()==null?0L:truck.getPoints();
			truck.setPoints(points_+points);
			this.save(truck);
			
			TDriverTransactionStatements tdts = new TDriverTransactionStatements();
			tdts.setCreatedDate(new Date());
			tdts.setAccountId(truck.getId());
			tdts.setPay(0.0);
			tdts.setBalance(truck.getBalance());
			tdts.setRestPoints(truck.getPoints());
			tdts.setOrderNo(orderNo);
			tdts.setPayPoints(points);
			tdts.setIsTurnout(false);
			tdts.setTradeType(20);
			tdts.setRemark("注册赠送！");
			this.save(tdts);
		}
		log = new HashMap();
		log.put("info", "记账成功！");
		log.put("method", "addPointsTally");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	
	/**
	 *                  
	 * @Title:        用用户币
	 * @Description:  TODO
	 * @param:        @param sysUserId 操作的ID
	 * @param:        @param sysUserName 操作的用户名称
	 * @param:        @param proxyUserId 代理ID
	 * @param:        @param proxyUserName 代理名称
	 * @param:        @param memberId 会员ID
	 * @param:        @param memberName
	 * @param:        @param userType
	 * @param:        @param years
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月23日 下午3:22:53
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean addPointsTally(
								long memberId,
								short userType,
								long points,
								String descript) throws Exception {
		String orderNo = utils.generaterOrderNumber();
		Map log = new HashMap();
		log.put("info", "记账开始！");
		log.put("method", "addPointsTally");
		logger.info(new JSONObject(log).toString());
		//记录参数
		Map args = new HashMap();
		args.put("memberId", memberId);
		args.put("userType", userType);
		args.put("orderNo", orderNo);
		log.put("method", "addPointsTally");
		logger.info(new JSONObject(args).toString());
		//检查参数
		if(userType!=Constants.UserType.consignorUser.getType()&&
				userType!=Constants.UserType.TruckUser.getType()||points<0){
			throw new BaseException("用户类型参数出错！");
		}
		
		//如果是货主
		if(userType==Constants.UserType.consignorUser.getType()){
			Consignor consignor = this.getById(Consignor.class,memberId);
			if(consignor==null){
				throw new BaseException("用户ID出错！");
			}
			//充值
			long points_ = consignor.getPoints()==null?0L:consignor.getPoints();
			consignor.setPoints(points_+points);
			this.save(consignor);
			//记流水
			TCargoTransactionStatements tcts = new TCargoTransactionStatements();
			tcts.setCreatedDate(new Date());
			tcts.setAccountId(consignor.getId());
			tcts.setPay(0.0);
			tcts.setBalance(consignor.getBalance());
			tcts.setRestPoints(consignor.getPoints());
			tcts.setOrderNo(orderNo);
			tcts.setPayPoints(points);
			tcts.setIsTurnout(false);
			tcts.setTradeType(20);
			tcts.setRemark(descript);
			this.save(tcts);
		}
		//如果是车主
		if(userType==Constants.UserType.TruckUser.getType()){
			Truck truck = this.getById(Truck.class,memberId);
			if(truck==null){
				throw new BaseException("用户ID出错！");
			}
			long points_ = truck.getPoints()==null?0L:truck.getPoints();
			truck.setPoints(points_+points);
			this.save(truck);
			
			TDriverTransactionStatements tdts = new TDriverTransactionStatements();
			tdts.setCreatedDate(new Date());
			tdts.setAccountId(truck.getId());
			tdts.setPay(0.0);
			tdts.setBalance(truck.getBalance());
			tdts.setRestPoints(truck.getPoints());
			tdts.setOrderNo(orderNo);
			tdts.setPayPoints(points);
			tdts.setIsTurnout(false);
			tdts.setTradeType(20);
			tdts.setRemark(descript);
			this.save(tdts);
		}
		log = new HashMap();
		log.put("info", "记账成功！");
		log.put("method", "addPointsTally");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	
	/**
	 *                  
	 * @Title:        addExperTimeTally 后台用户给会员增加服务年限
	 * @Description:  TODO
	 * @param:        @param sysUserId 操作的ID
	 * @param:        @param sysUserName 操作的用户名称
	 * @param:        @param proxyUserId 代理ID
	 * @param:        @param proxyUserName 代理名称
	 * @param:        @param memberId 会员ID
	 * @param:        @param memberName
	 * @param:        @param userType
	 * @param:        @param years
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月23日 下午3:22:53
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean addBalanceTally(
								long sysUserId,
								String sysUserName,
								Long proxyUserId,
								String proxyUserName,
								long memberId,
								String memberName,
								short userType,
								double balance) throws Exception {
		String sendMsg = "收支明细";
		String orderNo = utils.generaterOrderNumber();
		Map log = new HashMap();
		log.put("info", "记账开始！");
		log.put("method", "addBalanceTally");
		logger.info(new JSONObject(log).toString());
		//记录参数
		Map args = new HashMap();
		args.put("sysUserId", sysUserId);
		args.put("sysUserName", sysUserName);
		args.put("proxyUserId", proxyUserId);
		args.put("proxyUserName", proxyUserName);
		args.put("memberId", memberId);
		args.put("memberName", memberName);
		args.put("userType", userType);
		args.put("balance", balance);
		args.put("orderNo", orderNo);
		log.put("method", "addBalanceTally");
		logger.info(new JSONObject(args).toString());
		//检查参数
		if(userType!=Constants.UserType.consignorUser.getType()&&
				userType!=Constants.UserType.TruckUser.getType()||balance<0){
			throw new BaseException("用户类型参数出错！");
		}
		SysUser sysUser = this.getById(SysUser.class,sysUserId);
		if(sysUser==null){
			throw new BaseException("用户ID出错！");
		}
		//扣代理的费用
		double proxyBalance = sysUser.getBalance()==null?0.0:sysUser.getBalance();
		sysUser.setBalance(proxyBalance-balance);
		if(sysUser.getBalance()<0){
			throw new BaseException("余额不足！不能充值。");
		}
		this.save(sysUser);
		//记账
		ProxyStatements ps = new ProxyStatements();
		ps.setType(2);//充值是2
		ps.setUserType(userType);
		ps.setCreatedDate(new Date());
		ps.setMemberId(memberId);
		ps.setMemberName(memberName);
		ps.setSysUserId(sysUserId);
		ps.setSysUserName(sysUserName);
		ps.setProxyUserId(proxyUserId);
		ps.setProxyUserName(proxyUserName);
		ps.setBalance(balance);
		ps.setOrderNo(orderNo);
		this.save(ps);
		
		//记代理流水
		TProxyTransactionStatements tcts1 = new TProxyTransactionStatements();
		tcts1.setCreatedDate(new Date());
		tcts1.setAccountId(sysUser.getId());
		tcts1.setUserId(memberId);
		tcts1.setUserType(userType);
		tcts1.setBalance(sysUser.getBalance());
		tcts1.setPay(-balance);
		tcts1.setOrderNo(orderNo);
		tcts1.setIsTurnout(true);
		tcts1.setTradeType(Constants.TradeType.proxy4User.getType());
		tcts1.setRemark(Constants.TradeType.proxy4User.getZHName());
		this.save(tcts1);
		//如果是货主
		if(userType==Constants.UserType.consignorUser.getType()){
			Consignor consignor = this.getById(Consignor.class,memberId);
			if(consignor==null){
				throw new BaseException("用户ID出错！");
			}
			//充值
			double balance_ = consignor.getBalance()==null?0.0:consignor.getBalance();
			consignor.setBalance(balance_+balance);
			this.save(consignor);
			//记流水
			TCargoTransactionStatements tcts = new TCargoTransactionStatements();
			tcts.setCreatedDate(new Date());
			tcts.setAccountId(consignor.getId());
			tcts.setPay(balance);
			tcts.setBalance(consignor.getBalance());
			tcts.setRestPoints(consignor.getPoints());
			tcts.setOrderNo(orderNo);
			tcts.setPayPoints(0L);
			tcts.setIsTurnout(false);
			tcts.setTradeType(Constants.TradeType.proxyRecharge.getType());
			tcts.setRemark(Constants.TradeType.proxyRecharge.getZHName());
			this.save(tcts);
			//推送信息给用户
			commDataService.push2Consignor(consignor.getJpushId(), sendMsg, tcts.getId(),Integer.valueOf(consignor.getMobileType()));
		}
		//如果是车主
		if(userType==Constants.UserType.TruckUser.getType()){
			Truck truck = this.getById(Truck.class,memberId);
			if(truck==null){
				throw new BaseException("用户ID出错！");
			}
			double balance_ = truck.getBalance()==null?0.0:truck.getBalance();
			truck.setBalance(balance_+balance);
			this.save(truck);
			
			TDriverTransactionStatements tdts = new TDriverTransactionStatements();
			tdts.setCreatedDate(new Date());
			tdts.setAccountId(truck.getId());
			tdts.setPay(balance);
			tdts.setBalance(truck.getBalance());
			tdts.setRestPoints(truck.getPoints());
			tdts.setOrderNo(orderNo);
			tdts.setPayPoints(0L);
			tdts.setIsTurnout(false);
			tdts.setTradeType(Constants.TradeType.proxyRecharge.getType());
			tdts.setRemark(Constants.TradeType.proxyRecharge.getZHName());
			this.save(tdts);
			//推送信息给用户
			commDataService.push2Truck(truck.getJpushId(), sendMsg, tdts.getId(),Integer.valueOf(truck.getMobileType()));
		}
		log = new HashMap();
		log.put("info", "记账成功！");
		log.put("method", "addBalanceTally");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	
	
	/**
	 *                  
	 * @Title:        addExperTimeTally 后台用户给会员增加服务年限
	 * @Description:  TODO
	 * @param:        @param sysUserId 操作的ID
	 * @param:        @param sysUserName 操作的用户名称
	 * @param:        @param proxyUserId 代理ID
	 * @param:        @param proxyUserName 代理名称
	 * @param:        @param memberId 会员ID
	 * @param:        @param memberName
	 * @param:        @param userType
	 * @param:        @param years
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月23日 下午3:22:53
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean minusBalanceTally(
								long sysUserId,
								String sysUserName,
								long memberId,
								String memberName,
								short userType,
								double balance) throws Exception {
		String sendMsg = "收支明细";
		String orderNo = utils.generaterOrderNumber();
		Map log = new HashMap();
		log.put("info", "记账开始！");
		log.put("method", "addBalanceTally");
		logger.info(new JSONObject(log).toString());
		//记录参数
		Map args = new HashMap();
		args.put("sysUserId", sysUserId);
		args.put("sysUserName", sysUserName);
		args.put("memberId", memberId);
		args.put("memberName", memberName);
		args.put("userType", userType);
		args.put("balance", balance);
		args.put("orderNo", orderNo);
		log.put("method", "minusBalanceTally");
		logger.info(new JSONObject(args).toString());
		//检查参数
		if(userType!=Constants.UserType.consignorUser.getType()&&
				userType!=Constants.UserType.TruckUser.getType()||balance<=0){
			throw new BaseException("用户类型参数出错！");
		}
		SysUser sysUser = this.getById(SysUser.class,sysUserId);
		if(sysUser==null){
			throw new BaseException("用户ID出错！");
		}
		//如果是货主
		if(userType==Constants.UserType.consignorUser.getType()){
			Consignor consignor = this.getById(Consignor.class,memberId);
			if(consignor==null){
				throw new BaseException("用户ID出错！");
			}
			//充值
			double balance_ = consignor.getBalance()==null?0.0:consignor.getBalance();
			consignor.setBalance(balance_-balance);
			if(consignor.getBalance()<0){
				throw new BaseException("用户余额不够！");
			}
			this.save(consignor);
			//记流水
			TCargoTransactionStatements tcts = new TCargoTransactionStatements();
			tcts.setCreatedDate(new Date());
			tcts.setAccountId(consignor.getId());
			tcts.setPay(-balance);
			tcts.setBalance(consignor.getBalance());
			tcts.setRestPoints(consignor.getPoints());
			tcts.setOrderNo(orderNo);
			tcts.setPayPoints(0L);
			tcts.setIsTurnout(false);
			tcts.setTradeType(Constants.TradeType.refund.getType());
			tcts.setRemark(Constants.TradeType.refund.getZHName());
			this.save(tcts);
			//推送信息给用户
			commDataService.push2Consignor(consignor.getJpushId(), sendMsg, tcts.getId(),Integer.valueOf(consignor.getMobileType()));
		}
		//如果是车主
		if(userType==Constants.UserType.TruckUser.getType()){
			Truck truck = this.getById(Truck.class,memberId);
			if(truck==null){
				throw new BaseException("用户ID出错！");
			}
			double balance_ = truck.getBalance()==null?0.0:truck.getBalance();
			truck.setBalance(balance_-balance);
			if(truck.getBalance()<0){
				throw new BaseException("用户余额不够！");
			}
			this.save(truck);
			
			TDriverTransactionStatements tdts = new TDriverTransactionStatements();
			tdts.setCreatedDate(new Date());
			tdts.setAccountId(truck.getId());
			tdts.setPay(-balance);
			tdts.setBalance(truck.getBalance());
			tdts.setRestPoints(truck.getPoints());
			tdts.setOrderNo(orderNo);
			tdts.setPayPoints(0L);
			tdts.setIsTurnout(false);
			tdts.setTradeType(Constants.TradeType.refund.getType());
			tdts.setRemark(Constants.TradeType.refund.getZHName());
			this.save(tdts);
			//推送信息给用户
			commDataService.push2Truck(truck.getJpushId(), sendMsg, tdts.getId(),Integer.valueOf(truck.getMobileType()));
		}
		log = new HashMap();
		log.put("info", "记账成功！");
		log.put("method", "minusBalanceTally");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	/**
	 * 结算
	 * @Title:        addSpecialBalance 
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
	public boolean addSpecialBalance(long userId,double specialBalance,String orderNo,String desc,int tradeType,boolean isTurnOut) throws Exception {
		Map log = new HashMap();
		log.put("info", "记账开始！");
		log.put("method", "addSpecialBalance");
		logger.info(new JSONObject(log).toString());
		
		Truck truckInfo = this.getById(Truck.class,userId);
		if(truckInfo==null){
			throw new BaseException("userID出错！");
		}
		truckInfo.setSpecialBalance((truckInfo.getSpecialBalance()!=null?truckInfo.getSpecialBalance():0L)+specialBalance);
		
		if(truckInfo.getSpecialBalance()<0){
			throw new BaseException("专项费不够！");
		}
		this.save(truckInfo);
		//记流水
		TDriverTransactionStatements tdts = new TDriverTransactionStatements();
		tdts.setCreatedDate(new Date());
		tdts.setAccountId(truckInfo.getId());
		tdts.setPay(0.0);
		tdts.setBalance(truckInfo.getBalance());
		tdts.setPayPoints(0L);
		tdts.setRestPoints(truckInfo.getPoints());
		tdts.setSpecialBalance(specialBalance);
		tdts.setOrderNo(orderNo);
		tdts.setIsTurnout(isTurnOut);
		tdts.setTradeType(tradeType);
		tdts.setRemark(desc);
		this.save(tdts);
		
		log = new HashMap();
		log.put("info", "记账成功！");
		log.put("method", "addSpecialBalance");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	
	/**
	 *                  
	 * @Title:        addBalance4ProxyUserTally 给代理充值
	 * @Description:  TODO
	 * @param:        @param sysUserId 操作的ID
	 * @param:        @param sysUserName 操作的用户名称
	 * @param:        @param proxyUserId 代理ID
	 * @param:        @param proxyUserName 代理名称
	 * @param:        @param memberId 会员ID
	 * @param:        @param memberName
	 * @param:        @param userType
	 * @param:        @param years
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月23日 下午3:22:53
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean addBalance4ProxyUserTally(
								long sysUserId,
								String sysUserName,
								Long proxyUserId,
								String proxyUserName,
								double balance) throws Exception {
		String orderNo = utils.generaterOrderNumber();
		Map log = new HashMap();
		log.put("info", "记账开始！");
		log.put("method", "addBalance4ProxyUserTally");
		logger.info(new JSONObject(log).toString());
		//记录参数
		Map args = new HashMap();
		args.put("sysUserId", sysUserId);
		args.put("sysUserName", sysUserName);
		args.put("proxyUserId", proxyUserId);
		args.put("proxyUserName", proxyUserName);
		args.put("balance", balance);
		args.put("orderNo", orderNo);
		log.put("method", "addBalance4ProxyUserTally");
		logger.info(new JSONObject(args).toString());
		//记账
		TProxyTransactionOrder ps = new TProxyTransactionOrder();
		ps.setSysUserId(sysUserId);
		ps.setSysUserName(sysUserName);
		ps.setProxyUserId(proxyUserId);
		ps.setProxyUserName(proxyUserName);
		ps.setBalance(balance);
		ps.setOrderNo(orderNo);
		ps.setTradeType(Constants.TradeType.proxyRenew.getType());
		ps.setRemark(Constants.TradeType.proxyRenew.getZHName());
		ps.setCreatedDate(new Date());
		this.save(ps);
		
		//如果是货主
		SysUser sysUser = this.getById(SysUser.class,proxyUserId);
		if(sysUser==null){
			throw new BaseException("用户ID出错！");
		}
		//充值
		double balance_ = sysUser.getBalance()==null?0.0:sysUser.getBalance();
		sysUser.setBalance(balance_+balance);
		if(sysUser.getBalance()<0){
			throw new BaseException("余额不足！不能充值。");
		}
		this.save(sysUser);
		//记流水
		TProxyTransactionStatements tcts = new TProxyTransactionStatements();
		tcts.setCreatedDate(new Date());
		tcts.setAccountId(sysUser.getId());
		tcts.setUserId(sysUserId);
		tcts.setUserType(Constants.UserType.sysUser.getType());
		tcts.setBalance(sysUser.getBalance());
		tcts.setOrderNo(orderNo);
		tcts.setPay(balance);
		tcts.setIsTurnout(false);
		tcts.setTradeType(Constants.TradeType.proxyRecharge.getType());
		tcts.setRemark(Constants.TradeType.proxyRecharge.getZHName());
		this.save(tcts);
		
		log = new HashMap();
		log.put("info", "记账成功！");
		log.put("method", "addBalance4ProxyUserTally");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	
	
	/**
	 * 
	 * @Title:        balance4SysUserTally 
	 * @Description:  TODO
	 * @param:        @param operationSysUserId
	 * @param:        @param operationSysUserName
	 * @param:        @param sysUserId
	 * @param:        @param sysUserName
	 * @param:        @param tradeType
	 * @param:        @param remark
	 * @param:        @param balance
	 * @param:        @param isTurnOut
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2016年1月14日 上午10:28:54
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean balance4SysUserTally(
								String orderNo,
								long operationSysUserId,
								String operationSysUserName,
								long sysUserId,
								String sysUserName,
								int tradeType,
								String remark,
								double balance,
								boolean isTurnOut) throws Exception {
		Map log = new HashMap();
		log.put("info", "记账开始！");
		log.put("method", "balance4SysUserTally");
		logger.info(new JSONObject(log).toString());
		//记录参数
		Map args = new HashMap();
		args.put("operationSysUserId", operationSysUserId);
		args.put("operationSysUserName", operationSysUserName);
		args.put("sysUserId", sysUserId);
		args.put("sysUserName", sysUserName);
		args.put("balance", balance);
		args.put("orderNo", orderNo);
		args.put("tradeType", tradeType);
		args.put("remark", remark);
		args.put("isTurnOut", isTurnOut);
		log.put("method", "balance4SysUserTally");
		logger.info(new JSONObject(args).toString());
		//记账
		TProxyTransactionOrder ps = new TProxyTransactionOrder();
		ps.setSysUserId(operationSysUserId);
		ps.setSysUserName(operationSysUserName);
		ps.setProxyUserId(sysUserId);
		ps.setProxyUserName(sysUserName);
		ps.setBalance(balance);
		ps.setOrderNo(orderNo);
		ps.setTradeType(tradeType);
		ps.setRemark(remark);
		ps.setCreatedDate(new Date());
		this.save(ps);
		
		//如果是货主
		SysUser sysUser = this.getById(SysUser.class,sysUserId);
		if(sysUser==null){
			throw new BaseException("用户ID出错！");
		}
		//充值
		double balance_ = sysUser.getBalance()==null?0.0:sysUser.getBalance();
		sysUser.setBalance(balance_+balance);
		if(sysUser.getBalance()<0){
			throw new BaseException("余额不足！");
		}
		this.save(sysUser);
		//记流水
		TProxyTransactionStatements tcts = new TProxyTransactionStatements();
		tcts.setCreatedDate(new Date());
		tcts.setAccountId(sysUser.getId());
		tcts.setBalance(sysUser.getBalance());
		tcts.setUserId(operationSysUserId);
		tcts.setUserType(Constants.UserType.sysUser.getType());
		tcts.setOrderNo(orderNo);
		tcts.setPay(balance);
		tcts.setIsTurnout(isTurnOut);
		tcts.setTradeType(tradeType);
		tcts.setRemark(remark);
		this.save(tcts);
		
		log = new HashMap();
		log.put("info", "记账成功！");
		log.put("method", "balance4SysUserTally");
		logger.info(new JSONObject(log).toString());
		return true;
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={RuntimeException.class, Exception.class})
	public boolean addRechargeTally(
								long sysUserId,
								String sysUserName,
								Long proxyUserId,
								String proxyUserName,
								long memberId,
								int rechargeType,
								double recharge) throws Exception {
		String orderNo = utils.generaterOrderNumber();
		Map log = new HashMap();
		log.put("info", "记账开始！");
		log.put("method", "addRechargeTally");
		logger.info(new JSONObject(log).toString());
		//记录参数
		Map args = new HashMap();
		args.put("sysUserId", sysUserId);
		args.put("sysUserName", sysUserName);
		args.put("proxyUserId", proxyUserId);
		args.put("proxyUserName", proxyUserName);
		args.put("memberId", memberId);
		args.put("jyRecharge", recharge);
		args.put("orderNo", orderNo);
		log.put("method", "addRechargeTally");
		logger.info(new JSONObject(args).toString());
		//检查参数
		if(recharge<0||(rechargeType!=-2&&rechargeType!=-1)){
			throw new BaseException("用户类型参数出错！");
		}
		//修改用户余额
		Truck truckInfo = tallyDao.getById(Truck.class, memberId);
		truckInfo.setBalance(truckInfo.getBalance()+recharge);
		tallyDao.save(truckInfo);
		//记流水15274093108 
		TDriverTransactionStatements tdts = new TDriverTransactionStatements();
		tdts.setCreatedDate(new Date());
		tdts.setAccountId(truckInfo.getId());
		tdts.setPay(recharge);
		tdts.setBalance(truckInfo.getBalance());
		tdts.setRestPoints(truckInfo.getPoints());
		tdts.setOrderNo(orderNo);
		tdts.setPayPoints(0L);
		tdts.setIsTurnout(false);
		if(rechargeType==Constants.TradeType.recharge.getType()){
			tdts.setTradeType(Constants.TradeType.recharge.getType());
			tdts.setRemark(Constants.TradeType.recharge.getZHName());
		}else if(rechargeType==Constants.TradeType.jyrecharge.getType()){
			tdts.setTradeType(Constants.TradeType.jyrecharge.getType());
			tdts.setRemark(Constants.TradeType.jyrecharge.getZHName());
		}else if(rechargeType==Constants.TradeType.glrecharge.getType()){
			tdts.setTradeType(Constants.TradeType.glrecharge.getType());
			tdts.setRemark(Constants.TradeType.glrecharge.getZHName());
		}else{
			throw new BaseException("支付类型不正确！");
		}
		tallyDao.save(tdts);
		//如果是加油费充值
		if(rechargeType==Constants.TradeType.jyrecharge.getType()){
			UserRechargeRequest ur = new UserRechargeRequest();
			ur.setCreatedDate(new Date());
			ur.setOrderNo(orderNo);
			ur.setRechargeMuch(recharge);
			ur.setUserType(2);
			ur.setTradeType(tdts.getTradeType());
			ur.setUserId(tdts.getAccountId());
			ur.setRemark(tdts.getRemark());
			ur.setPayPoint((long)((setting.getJyPayPoint()*recharge)/100));
			ur.setReturnPoint((long)((setting.getJyReturnPoint()*recharge)/100));
			ur.setSpecialBalance((setting.getJySpecialBalance()*recharge/100));
			tallyDao.save(ur);
		}
		//如果是过路费充值
		if(rechargeType==Constants.TradeType.glrecharge.getType()){
			UserRechargeRequest ur = new UserRechargeRequest();
			ur.setCreatedDate(new Date());
			ur.setOrderNo(orderNo);
			ur.setRechargeMuch(recharge);
			ur.setUserType(2);
			ur.setTradeType(tdts.getTradeType());
			ur.setUserId(tdts.getAccountId());
			ur.setRemark(tdts.getRemark());
			ur.setPayPoint((long)((setting.getGlPayPoint()*recharge)/100));
			ur.setReturnPoint((long)((setting.getGlReturnPoint()*recharge)/100));
			ur.setSpecialBalance((setting.getGlSpecialBalance()*recharge/100));
			tallyDao.save(ur);
		}
		log = new HashMap();
		log.put("info", "记账成功！");
		log.put("method", "addRechargeTally");
		logger.info(new JSONObject(log).toString());
		String sendMsg = "收支明细";
		//推送信息给用户
		commDataService.push2Truck(truckInfo.getJpushId(), sendMsg, tdts.getId(),Integer.valueOf(truckInfo.getMobileType()));
		return true;
	}
	/**
	 * 
	 * @Title:        getBusinessRulesByActionUrl 
	 * @Description:  TODO   查询 商业规则
	 * @param:        @param actionUrl 商业url 
	 * @param:        @param type 类型
	 * @param:        @return
	 * @param:        @throws BaseException    
	 * @return:       BusinessRules    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年10月5日 下午2:14:07
	 */
	public BusinessRules getBusinessRulesByActionUrl(String actionUrl,Integer type) throws BaseException{
		return tallyDao.getBusinessRulesByActionUrl(actionUrl, type);
	}
	
	/**
	 * 
	 * @Title:        getBusinessRulesByActionUrl 
	 * @Description:  TODO 查询 商业规则
	 * @param:        @param actionUrl  商业url
	 * @param:        @return
	 * @param:        @throws BaseException    
	 * @return:       BusinessRules    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年10月5日 下午2:14:14
	 */
	public BusinessRules getBusinessRulesByActionUrl(String actionUrl) throws BaseException{
		return tallyDao.getBusinessRulesByActionUrl(actionUrl);
	}
	
	
	public List userAccountBook(Long userId,Short userTpye,long firstId,long lastId,int num,String filter) throws BaseException{
		return tallyDao.userAccountBook(userId, userTpye, firstId, lastId, num,filter);
	}
	
	public List userAccountBook(Long userId,Short userTpye,long firstId,long lastId,int num) throws BaseException{
		return tallyDao.userAccountBook(userId, userTpye, firstId, lastId, num);
	}
}
