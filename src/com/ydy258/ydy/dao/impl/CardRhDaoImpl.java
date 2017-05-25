package com.ydy258.ydy.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ydy258.ydy.dao.ICardRhDao;
import com.ydy258.ydy.entity.TruckCard;
import com.ydy258.ydy.entity.TruckCardRHOrder;
import com.ydy258.ydy.entity.TruckCardRHPrice;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.PageFactory;

@Repository
public class CardRhDaoImpl extends BaseDaoImpl implements ICardRhDao {
	public Page truckCardRHPriceByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select price.* from t_truck_card_price price where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("cardTypeName"))){
			sbsql.append(" and  price.card_type_name like :cardTypeName");
			args.put("cardTypeName", "%"+where.get("cardTypeName")+"%");
		}
		sbsql.append(" order by price.id desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,TruckCardRHPrice.class, currentPage, pageSize);
    }
	
	public Page truckCardRHCNPCByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select corder.* from t_truck_card_order corder where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("cardType"))){
			sbsql.append(" and  corder.card_type = :cardType");
			args.put("cardType", Integer.valueOf(where.get("cardType")));
		}
		if(!StringUtils.isBlank(where.get("status"))){
			sbsql.append(" and  corder.status = :status");
			args.put("status", Integer.valueOf(where.get("status")));
		}
		if(!StringUtils.isBlank(where.get("truckName"))){
			sbsql.append(" and  corder.truck_name like :truckName");
			args.put("truckName", "%"+where.get("truckName")+"%");
		}
		if(!StringUtils.isBlank(where.get("orderNo"))){
			sbsql.append(" and  corder.order_no like :orderNo");
			args.put("orderNo", "%"+where.get("orderNo")+"%");
		}
		if(!StringUtils.isBlank(where.get("truckMobile"))){
			sbsql.append(" and  corder.truck_mobile like :truckMobile");
			args.put("truckMobile", "%"+where.get("truckMobile")+"%");
		}
		sbsql.append(" order by corder.id desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,TruckCardRHOrder.class, currentPage, pageSize);
    }
	
	public TruckCard truckCardHistory(String cardNumber) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select * from t_truck_card where card_number='"+cardNumber+"'");
		List<TruckCard> list = this.loadBySQL(sbsql.toString(), null, TruckCard.class);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
    }
}
