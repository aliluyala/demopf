package com.ydy258.ydy.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ydy258.ydy.dao.IProxyDao;
import com.ydy258.ydy.entity.IDCardQueryOrder;
import com.ydy258.ydy.entity.TInsurance;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.PageFactory;

@Repository
public class ProxyDaoImpl extends BaseDaoImpl implements IProxyDao {
	
	public Page insuranceByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select info.* from t_cargo_insurance info where 1=1 ");
		Map sqlargs = new HashMap<String,String>();
		if(!StringUtils.isBlank(args.get("orderNo"))){
			sbsql.append(" and  info.order_no like :orderNo");
			sqlargs.put("orderNo", "%"+args.get("orderNo")+"%");
		}
		if(!StringUtils.isBlank(args.get("company"))){
			sbsql.append(" and  info.company = "+args.get("company")+"");
		}
		if(!StringUtils.isBlank(args.get("status"))){
			sbsql.append(" and  info.status = "+args.get("status")+"");
		}
		if(!StringUtils.isBlank(args.get("userId"))){
			sbsql.append(" and  info.user_id = '"+args.get("userId")+"'");
		}
		if(!StringUtils.isBlank(args.get("userType"))){
			sbsql.append(" and  info.user_type = '"+args.get("userType")+"'");
		}
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sbsql.append(" and  info.proxy_user_id='"+args.get("proxyUserId")+"' ");
		}
		if(!StringUtils.isBlank(args.get("startTime"))){
			sbsql.append(" and  info.created_date >=to_date('"+args.get("startTime")+"', 'yyyy-mm-dd')");
		}
		if(!StringUtils.isBlank(args.get("endTime"))){
			sbsql.append(" and  info.created_date <=to_date('"+args.get("endTime")+"', 'yyyy-mm-dd')");
		}
		sbsql.append(" order by info.created_date desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), sqlargs,TInsurance.class, currentPage, pageSize);
    }
	
	public Page insuranceChinaLifeByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select info.* from t_cargo_insurance info where 1=1 and status<>-1 ");
		Map sqlargs = new HashMap<String,String>();
		if(!StringUtils.isBlank(args.get("orderNo"))){
			sbsql.append(" and  info.order_no like :orderNo");
			sqlargs.put("orderNo", "%"+args.get("orderNo")+"%");
		}
		if(!StringUtils.isBlank(args.get("status"))){
			sbsql.append(" and  info.status = "+args.get("status")+"");
		}
		
		if(!StringUtils.isBlank(args.get("company"))){
			sbsql.append(" and  info.company = "+args.get("company")+"");
		}
		
		if(!StringUtils.isBlank(args.get("userId"))){
			sbsql.append(" and  info.user_id = '"+args.get("userId")+"'");
		}
		if(!StringUtils.isBlank(args.get("userType"))){
			sbsql.append(" and  info.user_type = '"+args.get("userType")+"'");
		}
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sbsql.append(" and  info.proxy_user_id='"+args.get("proxyUserId")+"' ");
		}
		if(!StringUtils.isBlank(args.get("startTime"))){
			sbsql.append(" and  info.created_date >=to_date('"+args.get("startTime")+"', 'yyyy-mm-dd')");
		}
		if(!StringUtils.isBlank(args.get("endTime"))){
			sbsql.append(" and  info.created_date <=to_date('"+args.get("endTime")+"', 'yyyy-mm-dd')");
		}
		sbsql.append(" order by info.created_date desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), sqlargs,TInsurance.class, currentPage, pageSize);
    }
    
	public Page idCardqueryOrderByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select info.* from t_idcardquery_order info where 1=1 ");
		Map sqlargs = new HashMap<String,String>();
		if(!StringUtils.isBlank(args.get("orderNo"))){
			sbsql.append(" and  info.order_no like :orderNo");
			sqlargs.put("orderNo", "%"+args.get("orderNo")+"%");
		}
		if(!StringUtils.isBlank(args.get("status"))){
			sbsql.append(" and  info.status = "+args.get("status")+"");
		}
		if(!StringUtils.isBlank(args.get("startTime"))){
			sbsql.append(" and  info.created_date >=to_date('"+args.get("startTime")+"', 'yyyy-mm-dd')");
		}
		if(!StringUtils.isBlank(args.get("endTime"))){
			sbsql.append(" and  info.created_date <=to_date('"+args.get("endTime")+"', 'yyyy-mm-dd')");
		}
		sbsql.append(" order by info.created_date desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), sqlargs,IDCardQueryOrder.class, currentPage, pageSize);
    }
	
	
	public Page proxyConfigListByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		Map data = new HashMap();
		StringBuffer sbsql = new StringBuffer(" select config.*,u.real_name user_name from t_business_rules config join Sys_user u on config.proxy_user_id=u.id where 1=1 ");
		if(!StringUtils.isBlank(args.get("search"))){
			sbsql.append(" and  user_name like :descript");
			data.put("descript", "%"+args.get("search")+"%");
		}
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sbsql.append(" and  config.proxy_user_id='"+args.get("proxyUserId")+"' ");
		}
		sbsql.append(" order by config.id desc");
		return PageFactory.createMapPageBySql(this, sbsql.toString(), data, currentPage, pageSize);
    }
	
	public TInsurance getByParentId(long parentId) throws Exception {
		String jpql = "select insurance from TInsurance as insurance where insurance.parentId=:parentId";
		TypedQuery<TInsurance> query = entityManager.createQuery(jpql, TInsurance.class).setFlushMode(FlushModeType.COMMIT);		
		List<TInsurance> l = query.setParameter("parentId", parentId).getResultList();		
		if(l != null && l.size() > 0)
			return l.get(0);		
		else
			return null;
    }
	
}
