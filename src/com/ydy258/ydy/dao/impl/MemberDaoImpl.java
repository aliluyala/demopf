package com.ydy258.ydy.dao.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ydy258.ydy.dao.IMemberDao;
import com.ydy258.ydy.entity.Consignor;
import com.ydy258.ydy.entity.Feedback;
import com.ydy258.ydy.entity.FreightLine;
import com.ydy258.ydy.entity.TCargoTransactionStatements;
import com.ydy258.ydy.entity.TDriverTransactionStatements;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.entity.TruckLocation;
import com.ydy258.ydy.entity.Warehouse;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.PageFactory;

@Repository
public class MemberDaoImpl extends BaseDaoImpl implements IMemberDao {
	
	public Page truckUserByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select info.* from t_truck_info info where 1=1 ");
		if(!StringUtils.isBlank(args.get("search"))){
			sbsql.append(" and  info.driver_name like :search");
			args.put("search", "%"+args.get("search")+"%");
		}
		if(!StringUtils.isBlank(args.get("phone"))){
			sbsql.append(" and  info.mobile like :phone");
			args.put("phone", "%"+args.get("phone")+"%");
		}
		if(!StringUtils.isBlank(args.get("code"))){
			sbsql.append(" and  info.teller_id like :code");
			args.put("code", "%"+args.get("code")+"%");
		}
		if(!StringUtils.isBlank(args.get("checkStatus"))){
			sbsql.append(" and  info.is_check='"+args.get("checkStatus")+"' ");
			args.remove("checkStatus");
		}
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sbsql.append(" and  info.proxy_user_id='"+args.get("proxyUserId")+"' ");
			args.remove("proxyUserId");
		}
		if(!StringUtils.isBlank(args.get("date"))){
			sbsql.append(" and  to_char(info.register_time,'yyyy-mm-dd') = :date");
			args.put("date", args.get("date"));
		}
		sbsql.append(" order by info.id desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,Truck.class, currentPage, pageSize);
    }
	
	public List truckUserStatements(Map<String,String> args) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select statements.* from t_driver_transaction_statements statements where 1=1 ");
		if(!StringUtils.isBlank(args.get("truckId"))){
			sbsql.append(" and  statements.account_id='"+args.get("truckId")+"' ");
			args.remove("truckId");
		}
		sbsql.append(" order by statements.created_date desc");
		return this.loadBySQL(sbsql.toString(), args, TDriverTransactionStatements.class);
    }
	
	
	
	public Page consignorUserByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select info.* from t_consignor_info info where 1=1 ");
		if(!StringUtils.isBlank(args.get("search"))){
			sbsql.append(" and  info.real_name like :search");
			args.put("search", "%"+args.get("search")+"%");
		}
		if(!StringUtils.isBlank(args.get("phone"))){
			sbsql.append(" and  info.mobile like :phone");
			args.put("phone", "%"+args.get("phone")+"%");
		}
		if(!StringUtils.isBlank(args.get("code"))){
			sbsql.append(" and  info.teller_id like :code");
			args.put("code", "%"+args.get("code")+"%");
		}
		if(!StringUtils.isBlank(args.get("checkStatus"))){
			sbsql.append(" and  info.is_auth='"+args.get("checkStatus")+"' ");
			args.remove("checkStatus");
		}
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sbsql.append(" and  info.proxy_user_id='"+args.get("proxyUserId")+"' ");
			args.remove("proxyUserId");
		}
		if(!StringUtils.isBlank(args.get("userType"))){
			sbsql.append(" and  info.user_type='"+args.get("userType")+"' ");
			args.remove("userType");
		}else{
			sbsql.append(" and  (info.user_type='0' or info.user_type is null) ");
			args.remove("userType");
		}
		if(!StringUtils.isBlank(args.get("date"))){
			sbsql.append(" and  to_char(info.register_time,'yyyy-mm-dd') = :date");
			args.put("date", args.get("date"));
		}
		sbsql.append(" order by info.id desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,Consignor.class, currentPage, pageSize);
    }
	
	/**
	 * 专线
	 */
	public List<FreightLine> consignorCompanyByPage(Map<String,String> args) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select info.* from t_freight_line info where 1=1 ");
		if(!StringUtils.isBlank(args.get("userId"))){
			sbsql.append(" and  info.user_id='"+args.get("userId")+"' ");
			args.remove("userId");
		}
		sbsql.append(" order by info.id desc");
		return this.loadBySQL(sbsql.toString(), args, FreightLine.class);
    }
	
	/**
	 * 落货
	 */
	public List<Warehouse> warehouseByPage(Map<String,String> args) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select info.* from t_warehouse info where 1=1 ");
		if(!StringUtils.isBlank(args.get("userId"))){
			sbsql.append(" and  info.user_id='"+args.get("userId")+"' ");
			args.remove("userId");
		}
		sbsql.append(" order by info.id desc");
		return this.loadBySQL(sbsql.toString(), args, Warehouse.class);
    }
	
	public List consignorUserStatements(Map<String,String> args) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select statements.* from t_cargo_transaction_statements statements where 1=1 ");
		if(!StringUtils.isBlank(args.get("consignorId"))){
			sbsql.append(" and  statements.account_id='"+args.get("consignorId")+"' ");
			args.remove("consignorId");
		}
		sbsql.append(" order by statements.created_date desc");
		return this.loadBySQL(sbsql.toString(), args, TCargoTransactionStatements.class);
    }
	
	
	public Page feebackQuery(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select info.* from t_feedback info where 1=1 ");
		if(!StringUtils.isBlank(args.get("content"))){
			sbsql.append(" and  info.content like :content");
			args.put("content", "%"+args.get("content")+"%");
		}
		if(!StringUtils.isBlank(args.get("mobile"))){
			sbsql.append(" and  info.mobile like :mobile");
			args.put("mobile", "%"+args.get("mobile")+"%");
		}
		sbsql.append(" order by info.add_time desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,Feedback.class, currentPage, pageSize);
    }

	public Truck getByPlateNumber(String mobile) throws Exception  {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(mobile))
			return null;
		String jpql = "select truck from Truck as truck where truck.mobile=:mobile";
		TypedQuery<Truck> query = entityManager.createQuery(jpql, Truck.class).setFlushMode(FlushModeType.COMMIT);		
		List<Truck> l = query.setParameter("mobile", mobile).getResultList();		
		if(l != null && l.size() > 0)
			return l.get(0);		
		else
			return null;
	}	
	
	/**
	 * 检查指定的手机号是否已经注册
	 * @param mobile
	 * @return
	 */
	public Truck getByMobile(String mobile) throws Exception   {
		if(StringUtils.isEmpty(mobile))
			return null;		
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Truck> query = cb.createQuery(Truck.class);
		Root<Truck> truck = query.from(Truck.class);
		query.select(truck);
		query.where(cb.equal(truck.get("mobile").as(String.class), mobile));
		
		List<Truck> l = entityManager.createQuery(query).getResultList();
		if(l != null && l.size() > 0)
			return l.get(0);
		else
			return null;		
	}
	
	
	
	public Consignor getbyMobileNo(String mobileNo) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(mobileNo))
			return null;
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Consignor> query = cb.createQuery(Consignor.class);
		Root<Consignor> consignor = query.from(Consignor.class);
		query.select(consignor);
		query.where(cb.equal(consignor.get("mobile").as(String.class), mobileNo));
		
		List<Consignor> l = entityManager.createQuery(query).getResultList();
		if(l != null && l.size() > 0)
			return l.get(0);
		else
			return null;		
	}
	//���ָ���ֻ�ŵ��û��Ƿ����
	public boolean userIsExists(String mobileNo) {
		// TODO Auto-generated method stub		
		if(StringUtils.isEmpty(mobileNo))
			return false;
		String jpql = "select count(*) from Consignor as consignor where consignor.mobile=:mobileNo";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("mobileNo", mobileNo).getSingleResult();
		return count > 0;
	}
	
	public void deleteByTruckId(long truckId) throws Exception   {
		StringBuffer sql = new StringBuffer("select t.* from t_truck_location t where t.truck_id="+truckId);
		List<TruckLocation> list = this.loadBySQL(sql.toString(), null, TruckLocation.class);
		if(list!=null&&list.size()>0){
			this.delete(list.get(0));
		}
	}
}
