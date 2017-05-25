package com.ydy258.ydy.service;

import java.util.List;

import com.ydy258.ydy.entity.BusinessRules;
import com.ydy258.ydy.util.BaseException;

public interface ITallyService extends IBaseService {
	
	
	public boolean saveUserOrder(Long userId,Short userType,String noOrder,int rechargeType) throws Exception;
	/**\
	 * 
	 * @Title:        tally 
	 * ���˹���
	 * userId �û�ID
	 * userType �û����� 1������ ��Constants.UserType.consignorUser.getType()��2��������Constants.UserType.TruckUser.getType()��
	 * money:������ �������˺��ϼӣ������ǿ�ȥ 0:��ʾ����
	 * payPoints : ���׻����   �������˺��ϼӣ������ǿ�ȥ 0:��ʾ����
	 * tradeType �������ͣ�Constants.TradeType.recharge.getType()��
	 * tradeTypeDescript ��������  Constants.TradeType.recharge.getZHName()
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015��8��28�� ����2:02:52
	 */
	public boolean tally(Long userId,Short userType,Double money,Long payPoints,Integer tradeType,String tradeTypeDescript,String orderNo,boolean isTurnout) throws Exception;
	
	public boolean tally(Long userId,Short userType,Double money,Double cost,Long payPoints,Integer tradeType,String tradeTypeDescript,String orderNo,boolean isTurnout) throws Exception;
	
	
	/**
	 * 
	 * @Title:        getBusinessRulesByActionUrl 
	 * @Description:  TODO   ��ѯ ��ҵ����
	 * @param:        @param actionUrl ��ҵurl 
	 * @param:        @param type ����
	 * @param:        @return
	 * @param:        @throws BaseException    
	 * @return:       BusinessRules    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015��10��5�� ����2:14:07
	 */
	public BusinessRules getBusinessRulesByActionUrl(String actionUrl,Integer type) throws BaseException;
	
	/**
	 * 
	 * @Title:        getBusinessRulesByActionUrl 
	 * @Description:  TODO ��ѯ ��ҵ����
	 * @param:        @param actionUrl  ��ҵurl
	 * @param:        @return
	 * @param:        @throws BaseException    
	 * @return:       BusinessRules    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015��10��5�� ����2:14:14
	 */
	public BusinessRules getBusinessRulesByActionUrl(String actionUrl) throws BaseException;
	
	
	public List userAccountBook(Long userId,Short userTpye,long firstId,long lastId,int num,String filter) throws BaseException;
	
	public List userAccountBook(Long userId,Short userTpye,long firstId,long lastId,int num) throws BaseException;
	
	public boolean tally(
			Long userId, //用户ID
			Short userType,//用户类型
			Double money,//支付钱数
			Double cost,//公司价格
			Long proxyUserId,//代理ID
			Double proxyPrice,//代理价格
			Long payPoints,//支付点数
			Integer tradeType,//交易类型
			String tradeTypeDescript,//交易描述
			String orderNo,//订单号
			boolean isTurnout//是否是支出
			) throws Exception;
	
	public boolean addExperTimeTally(
			long sysUserId,
			String sysUserName,
			Long proxyUserId,
			String proxyUserName,
			long memberId,
			String memberName,
			short userType,
			int years) throws Exception;
	
	public boolean addBalanceTally(
			long sysUserId,
			String sysUserName,
			Long proxyUserId,
			String proxyUserName,
			long memberId,
			String memberName,
			short userType,
			double balance) throws Exception;
	
	
	public boolean addPointsTally(
			long memberId,
			short userType,
			long points) throws Exception;
	
	public boolean addRechargeTally(
			long sysUserId,
			String sysUserName,
			Long proxyUserId,
			String proxyUserName,
			long memberId,
			int rechargeType,
			double recharge) throws Exception;
	
	
	public boolean addBalance4ProxyUserTally(
			long sysUserId,
			String sysUserName,
			Long proxyUserId,
			String proxyUserName,
			double balance) throws Exception ;
	
	public boolean balance4SysUserTally(
			String orderNo,
			long operationSysUserId,
			String operationSysUserName,
			long sysUserId,
			String sysUserName,
			int tradeType,
			String remark,
			double balance,
			boolean isTurnOut) throws Exception;
	
	public boolean minusBalanceTally(
			long sysUserId,
			String sysUserName,
			long memberId,
			String memberName,
			short userType,
			double balance) throws Exception;
	
	public boolean addPointsTally(
			long memberId,
			short userType,
			long points,
			String descript) throws Exception ;
}
