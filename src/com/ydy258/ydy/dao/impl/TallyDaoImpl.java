package com.ydy258.ydy.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.dao.ITallyDao;
import com.ydy258.ydy.entity.BusinessRules;
import com.ydy258.ydy.entity.TCargoTransactionStatements;
import com.ydy258.ydy.entity.TDriverTransactionStatements;
import com.ydy258.ydy.entity.TTransactionLlzfLog;
import com.ydy258.ydy.entity.TTransactionLog;
import com.ydy258.ydy.util.BaseException;

@Repository
public class TallyDaoImpl extends BaseDaoImpl implements ITallyDao {
	
	private static final Logger logger  =  Logger.getLogger(TallyDaoImpl.class );
	
	//ע��ʵ�������
	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * ��������֧����־
	 */
	@Override
	public boolean saveLlzfLog(TTransactionLlzfLog log) {
		this.save(log);
		return true;
	}

	/**
	 * ���潻����־
	 */
	@Override
	public boolean saveStatementsLog(TTransactionLog log) {
		return false;
	}

	/**
	 * �������������ˮ
	 */
	@Override
	public boolean saveStatements(TCargoTransactionStatements statements) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * ���泵��������ˮ
	 */
	@Override
	public boolean saveStatements(TDriverTransactionStatements statements) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * ���������˺�
	 */

	/**
	 * ���������˺�
	 */

	/**
	 * �޸Ļ����˺��ʽ�
	 */
	@Override
	public boolean updateCargoAcc(Long accId, Double balance) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	
	
	
	/**
	 * ͨ�� actionUrl �� ��Ʒ���� ��ѯ ��ҵ����
	 */
	public BusinessRules getBusinessRulesByActionUrl(String actionUrl,Integer type) throws BaseException{
		String sql = "select * from t_business_rules where action_url=:actionUrl and type=:type";
		javax.persistence.Query query = entityManager.createNativeQuery(sql,BusinessRules.class).setParameter("actionUrl", actionUrl).setParameter("type", type).setFlushMode(FlushModeType.COMMIT);
		List<BusinessRules> list = query.getResultList();
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * ͨ�� actionUrl ��ѯ��ҵ����
	 */
	public BusinessRules getBusinessRulesByActionUrl(String actionUrl) throws BaseException{
		return this.getBusinessRulesByActionUrl(actionUrl, 1);
	}

	
	/**
	 * ͨ�� actionUrl �� ��Ʒ���� ��ѯ ��ҵ����
	 */
	public List userAccountBook(Long userId,Short userTpye,long firstId,long lastId,int num,String filter) throws BaseException{
		if(userId == null || userId == 0L || (userTpye!=Constants.UserType.TruckUser.getType() && userTpye!=Constants.UserType.consignorUser.getType()))
			return null;
		if(num == 0)
			num = 20;
		StringBuilder sb = new StringBuilder();
		
		if(userTpye==Constants.UserType.consignorUser.getType()){
			sb.append("SELECT * FROM t_cargo_transaction_statements WHERE 1=1 and account_id="+userId+"  and pay!=0");
		}
		
		if(userTpye==Constants.UserType.TruckUser.getType()){
			sb.append("SELECT * FROM t_driver_transaction_statements WHERE 1=1 and account_id="+userId+"  and pay!=0");
		}
		
		if("1".equals(filter)){//�����֧��
			sb.append(" and is_turnout ");
		}
		if("2".equals(filter)){//�����֧��
			sb.append(" and is_turnout=false ");
		}
		if(firstId > 0)
			sb.append(" AND id>" + firstId);	//����ˢ��
		else if(lastId > 0)
			sb.append(" AND id<" + lastId);		//����ˢ��
		sb.append(" ORDER BY id DESC");
		
		String sql = sb.toString();
		if(userTpye==Constants.UserType.consignorUser.getType()){
			TypedQuery<TCargoTransactionStatements> query = (TypedQuery<TCargoTransactionStatements>)entityManager.createNativeQuery(sql, TCargoTransactionStatements.class);
			return query.setMaxResults(num).getResultList();
		}
		if(userTpye==Constants.UserType.TruckUser.getType()){
			TypedQuery<TDriverTransactionStatements> query = (TypedQuery<TDriverTransactionStatements>)entityManager.createNativeQuery(sql, TDriverTransactionStatements.class);
			return query.setMaxResults(num).getResultList();
		}
		return null;		
	}

	/**
	 * ͨ�� actionUrl �� ��Ʒ���� ��ѯ ��ҵ����
	 */
	public List userAccountBook(Long userId,Short userTpye,long firstId,long lastId,int num) throws BaseException{
		if(userId == null || userId == 0L || (userTpye!=Constants.UserType.TruckUser.getType() && userTpye!=Constants.UserType.consignorUser.getType()))
			return null;
		if(num == 0)
			num = 20;
		StringBuilder sb = new StringBuilder();
		
		if(userTpye==Constants.UserType.consignorUser.getType()){
			sb.append("SELECT * FROM t_cargo_transaction_statements WHERE 1=1 and account_id="+userId+"  and pay_points!=0");
		}
		
		if(userTpye==Constants.UserType.TruckUser.getType()){
			sb.append("SELECT * FROM t_driver_transaction_statements WHERE 1=1 and account_id="+userId+"  and pay_points!=0");
		}
		
		if(firstId > 0)
			sb.append(" AND id>" + firstId);	//����ˢ��
		else if(lastId > 0)
			sb.append(" AND id<" + lastId);		//����ˢ��
		sb.append(" ORDER BY id DESC");
		
		String sql = sb.toString();
		if(userTpye==Constants.UserType.consignorUser.getType()){
			TypedQuery<TCargoTransactionStatements> query = (TypedQuery<TCargoTransactionStatements>)entityManager.createNativeQuery(sql, TCargoTransactionStatements.class);
			return query.setMaxResults(num).getResultList();
		}
		if(userTpye==Constants.UserType.TruckUser.getType()){
			TypedQuery<TDriverTransactionStatements> query = (TypedQuery<TDriverTransactionStatements>)entityManager.createNativeQuery(sql, TDriverTransactionStatements.class);
			return query.setMaxResults(num).getResultList();
		}
		return null;		
	}


}
