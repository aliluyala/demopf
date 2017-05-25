package com.ydy258.ydy.dao;

import java.util.List;

import com.ydy258.ydy.entity.BusinessRules;
import com.ydy258.ydy.entity.TCargoTransactionStatements;
import com.ydy258.ydy.entity.TDriverTransactionStatements;
import com.ydy258.ydy.entity.TTransactionLlzfLog;
import com.ydy258.ydy.entity.TTransactionLog;
import com.ydy258.ydy.util.BaseException;


public interface ITallyDao extends IBaseDao {
	/**
	 * 
	 * @Title:        saveLlzfLog 
	 * @Description:  TODO ����֧����־����
	 * @param:        @param log
	 * @param:        @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015��7��27�� ����9:57:25
	 */
	public boolean saveLlzfLog(TTransactionLlzfLog log);
	
	/**
	 * ��ˮ��־
	 * @Title:        saveStatementsLog 
	 * @Description:  TODO
	 * @param:        @param stransationType
	 * @param:        @param log
	 * @param:        @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015��7��27�� ����9:58:09
	 */
	public boolean saveStatementsLog(TTransactionLog log);
	
	/**
	 *  ��������ˮ
	 * @Title:        saveStatements 
	 * @Description:  TODO
	 * @param:        @param statements
	 * @param:        @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015��7��27�� ����9:58:53
	 */
	
	public boolean saveStatements(TCargoTransactionStatements statements);
	
	/**
	 * ��������ˮ
	 * @Title:        saveStatements 
	 * @Description:  TODO
	 * @param:        @param statements
	 * @param:        @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015��7��27�� ����9:59:32
	 */
	
	
	public boolean saveStatements(TDriverTransactionStatements statements);
	
	
	
	
	/**
	 * �޸Ļ����˺��ʽ�
	 * @Title:        updateCargoAcc 
	 * @Description:  TODO
	 * @param:        @param accId
	 * @param:        @param balance
	 * @param:        @return    
	 * @return:       boolean    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015��7��27�� ����10:28:29
	 */
	public boolean updateCargoAcc(Long accId,Double balance);
	
	
	
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
}
