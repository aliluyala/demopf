package com.ydy258.ydy.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ydy258.ydy.dao.IInsuranceDao;
import com.ydy258.ydy.entity.DdcxwyInsurance;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.PageFactory;

@Repository
public class InsuranceDaoImpl extends BaseDaoImpl implements IInsuranceDao {
	
	
	public Page ddwyinsuranceListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select insurance.* from ddcxwy_insurance insurance where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("proxyName"))){
			sbsql.append(" and  insurance.name like :proxyName");
			args.put("proxyName", "%"+where.get("proxyName")+"%");
		}
		if(!StringUtils.isBlank(where.get("proxyId"))){
			sbsql.append(" and  insurance.proxy_id = :proxyId");
			args.put("proxyId", Long.valueOf(where.get("proxyId")));
		}
		if(!StringUtils.isBlank(where.get("saleUserName"))){
			sbsql.append(" and  insurance.sale_user_name like :saleUserName");
			args.put("saleUserName", "%"+where.get("saleUserName")+"%");
		}
		sbsql.append(" order by insurance.created_date desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,DdcxwyInsurance.class, currentPage, pageSize);
    }
}
