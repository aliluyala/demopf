package com.ydy258.ydy.dao.impl;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ydy258.ydy.dao.IStatisticsDao;
import com.ydy258.ydy.entity.ProxyStatements;
import com.ydy258.ydy.entity.WaybillInfo;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.PageFactory;
import com.ydy258.ydy.util.utils;

@Repository
public class StatisticsDaoImpl extends BaseDaoImpl implements IStatisticsDao {
	
	
	/**
	 * 统计 天指定天数的每一天的新增用户数
	 */
	public List newtruckUserStatistics(Map<String,String> args) throws Exception {
		if(StringUtils.isBlank(args.get("days"))){
			return null;
		}
		Format f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -Integer.valueOf(args.get("days")));
		String dateString = f.format(c.getTime());
		Map<String,String> sqlagrs = new HashMap<String,String>();
		sqlagrs.put("dateStr", "to_date("+dateString+", 'yyyy-mm-dd')");
		StringBuffer sbsql = new StringBuffer("select to_char(info.register_time,'yyyy-mm-dd') created,count(*) as countuser "
												+ "from t_truck_info info "
												+ "where info.register_time>to_date('"+dateString+"', 'yyyy-mm-dd') ");
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sbsql.append(" and info.proxy_user_id="+args.get("proxyUserId"));
		}
		sbsql.append(" group by to_char(info.register_time,'yyyy-mm-dd') ORDER BY created desc ");
		return this.loadMapBySQL(sbsql.toString());
    }
	
	/**
	 * 统计 天指定天数的每一天的新增用户数
	 */
	public List newConsignorUserStatistics(Map<String,String> args) throws Exception {
		if(StringUtils.isBlank(args.get("days"))){
			return null;
		}
		Format f = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -Integer.valueOf(args.get("days")));
		String dateString = f.format(c.getTime());
		Map<String,String> sqlagrs = new HashMap<String,String>();
		sqlagrs.put("dateStr", "to_date("+dateString+", 'yyyy-mm-dd')");
		StringBuffer sbsql = new StringBuffer("select to_char(info.register_time,'yyyy-mm-dd') created,count(*) as countuser "
				+ "from t_consignor_info info "
				+ "where info.register_time>to_date('"+dateString+"', 'yyyy-mm-dd') ");
		
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sbsql.append(" and info.proxy_user_id="+args.get("proxyUserId"));
		}	
		sbsql.append("group by to_char(info.register_time,'yyyy-mm-dd') ORDER BY created desc");
		return this.loadMapBySQL(sbsql.toString());
    }

	
	/**
	 * 统计 天指定天数的每一天的新增用户数
	 */
	public List newtruckUserStatisticsByMonth(Map<String,String> args) throws Exception {
		if(StringUtils.isBlank(args.get("month"))){
			return null;
		}
		String monthString = args.get("month");//从2014年开始统计
		StringBuffer sbsql = new StringBuffer("select to_char(info.register_time,'yyyy-mm') created,count(*) as countuser "
												+ "from t_truck_info info "
												+ "where info.register_time>to_date('"+monthString+"', 'yyyy-mm') ");
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sbsql.append(" and info.proxy_user_id="+args.get("proxyUserId"));
		}
		sbsql.append(" group by to_char(info.register_time,'yyyy-mm') ORDER BY created desc ");
		return this.loadMapBySQL(sbsql.toString());
    }
	
	/**
	 * 统计 天指定天数的每一天的新增用户数
	 */
	public List newConsignorUserStatisticsByMonth(Map<String,String> args) throws Exception {
		if(StringUtils.isBlank(args.get("month"))){
			return null;
		}
		String monthString = args.get("month");//从2014年开始统计
		StringBuffer sbsql = new StringBuffer("select to_char(info.register_time,'yyyy-mm') created,count(*) as countuser "
				+ "from t_consignor_info info "
				+ "where info.register_time>to_date('"+monthString+"', 'yyyy-mm') ");
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sbsql.append(" and info.proxy_user_id="+args.get("proxyUserId"));
		}	
		if(args.get("userType").equals("1")){
			sbsql.append(" and info.user_type=1");
		}else{
			sbsql.append(" and info.user_type<>1");
		}
		sbsql.append("group by to_char(info.register_time,'yyyy-mm') ORDER BY created desc");
		return this.loadMapBySQL(sbsql.toString());
    }
	
	
	/**
	 * 统计 天指定天数的每一天的新增用户数 (店家)
	 */
	public List newMallUserStatisticsByMonth(Map<String,String> args) throws Exception {
		if(StringUtils.isBlank(args.get("month"))){
			return null;
		}
		String monthString = args.get("month");//从2014年开始统计
		StringBuffer sbsql = new StringBuffer("select to_char(info.created_date,'yyyy-mm') created,count(*) as countuser "
				+ "from t_third_mall info "
				+ "where info.created_date>to_date('"+monthString+"', 'yyyy-mm') ");
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sbsql.append(" and info.agency_id="+args.get("proxyUserId"));
		}	
		sbsql.append("group by to_char(info.created_date,'yyyy-mm') ORDER BY created desc");
		return this.loadMapBySQL(sbsql.toString());
    }
	
	public Page companyStatements(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		Map sqlargs = new HashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("select  ts.id id, ts.account_id userId,order_no orderNo,pay pay,remark remark,user_type userType,pay_points point,ts.created_date date "
				+ "from t_company_transaction_statements ts "
				+ "where 1=1 and pay>0   ");
				
		if(!StringUtils.isBlank(args.get("orderNo"))){
			sb.append("  and order_no like :orderNo ");
			sqlargs.put("orderNo", "%"+args.get("orderNo")+"%");
		}
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sb.append(" and ts.proxy_user_id="+args.get("proxyUserId"));
		}
		
		if(!StringUtils.isBlank(args.get("startTime"))){
			sb.append("  and ts.created_date >=to_date('"+args.get("startTime")+"', 'yyyy-mm-dd')");
			//sqlargs.put("startTime", "to_date("+args.get("startTime")+", 'yyyy-mm-dd')");
		}
		if(!StringUtils.isBlank(args.get("endTime"))){
			sb.append("  and ts.created_date <to_date('"+args.get("endTime")+"', 'yyyy-mm-dd')");
			//sqlargs.put("endTime", "to_date("+args.get("endTime")+", 'yyyy-mm-dd')");
		}
		sb.append(" order by ts.created_date desc");
		return PageFactory.createMapPageBySql(this, sb.toString(), sqlargs, currentPage, pageSize);
	}
	
	public Page companyAccountBooks(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		Map sqlargs = new HashMap<String,String>();
		StringBuffer fy = new StringBuffer();
		StringBuffer tj = new StringBuffer();
		StringBuffer countsql = new StringBuffer();
		fy.append("select  ts.id id,ts.success isSuccess, ts.account_id userId,order_no orderNo,pay pay,remark remark,user_type userType,pay_points point,ts.created_date date,COALESCE(ts.cost,0) costpay,COALESCE(ts.proxy_price,0) proxyPricepay  "
				+ "from t_company_transaction_statements ts "
				+ "where 1=1 ");
		
		if(!StringUtils.isBlank(args.get("orderNo"))){
			fy.append(" and ts.order_no like :orderNo ");
			sqlargs.put("orderNo", "%"+args.get("orderNo")+"%");
		}
		
		if(!StringUtils.isBlank(args.get("tradeType"))){
			fy.append(" and ts.trade_type in("+args.get("tradeType")+")");
		}
		
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			fy.append(" and ts.proxy_user_id="+args.get("proxyUserId"));
		}	
		
		if(!StringUtils.isBlank(args.get("startTime"))){
			fy.append("  and ts.created_date >=to_date('"+args.get("startTime")+"', 'YYYY-MM-DD')");
		}
		if(!StringUtils.isBlank(args.get("endTime"))){
			fy.append("  and ts.created_date <to_date('"+args.get("endTime")+"', 'YYYY-MM-DD')");
		}
		fy.append(" order by id desc ");
		Page page = PageFactory.createMapPageBySql2(this, fy.toString(), sqlargs, currentPage, pageSize);
		
		
		tj.append("select  sum(pay) totalpay1,sum(costpay) totalpay2,sum(proxyPricepay) totalpay3,sum(pay)-sum(proxyPricepay) pay12,sum(proxyPricepay)-sum(costpay) pay13 from (").append(fy).append(") t");
		
		List<Map> ret = this.loadMapBySQL(tj.toString(), sqlargs);
		
		if(ret!=null&&ret.size()>0){
			Map ret_ = new HashMap();
			ret_.put("totalpay1", utils.convert((Double)ret.get(0).get("totalpay1")==null?0.0:(Double)ret.get(0).get("totalpay1")));
			ret_.put("totalpay2", utils.convert((Double)ret.get(0).get("totalpay2")==null?0.0:(Double)ret.get(0).get("totalpay2")));
			ret_.put("totalpay3", utils.convert((Double)ret.get(0).get("totalpay3")==null?0.0:(Double)ret.get(0).get("totalpay3")));
			ret_.put("pay12", utils.convert((Double)ret.get(0).get("pay12")==null?0.0:(Double)ret.get(0).get("pay12")));
			ret_.put("pay13", utils.convert((Double)ret.get(0).get("pay13")==null?0.0:(Double)ret.get(0).get("pay13")));
			page.setOtherMap(ret_);
		}
		return page;
	}
	
	
	public Page proxyStatements(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		Map sqlargs = new HashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("select *  "
				+ "from proxy_statements ts "
				+ "where 1=1  ");
		if(!StringUtils.isBlank(args.get("orderNo"))){
			sb.append("  and order_no like :orderNo ");
			sqlargs.put("orderNo", "%"+args.get("orderNo")+"%");
		}
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sb.append(" and ts.proxy_user_id="+args.get("proxyUserId"));
		}
		if(!StringUtils.isBlank(args.get("type"))){
			sb.append(" and ts.type="+args.get("type"));
		}
		if(!StringUtils.isBlank(args.get("startTime"))){
			sb.append("  and ts.created_date >=to_date('"+args.get("startTime")+"', 'yyyy-mm-dd')");
		}
		if(!StringUtils.isBlank(args.get("endTime"))){
			sb.append("  and ts.created_date <to_date('"+args.get("endTime")+"', 'yyyy-mm-dd')");
		}
		sb.append(" order by ts.created_date desc");
		return PageFactory.createPageBySql(this, sb.toString(), sqlargs, ProxyStatements.class, currentPage, pageSize);
	}
	
	
	public Page userRechargeRequest(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		Map sqlargs = new HashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("select truck.driver_name,truck.mobile,truck.plate_number,ts.*  "
				+ "from t_user_recharge_request ts join t_truck_info truck on ts.user_id=truck.id "
				+ "where 1=1  ");
		if(!StringUtils.isBlank(args.get("orderNo"))){
			sb.append("  and order_no like :orderNo ");
			sqlargs.put("orderNo", "%"+args.get("orderNo")+"%");
		}
		if(!StringUtils.isBlank(args.get("mobile"))){
			sb.append("  and mobile like :mobile ");
			sqlargs.put("mobile", "%"+args.get("mobile")+"%");
		}
		if(!StringUtils.isBlank(args.get("type"))){
			sb.append(" and ts.trade_type="+args.get("type"));
		}
		
		if(!StringUtils.isBlank(args.get("success"))){
			if(args.get("success").equals("true")){
				sb.append(" and ts.is_success="+args.get("success"));
			}else{
				sb.append(" and (ts.is_success="+args.get("success")+" or ts.is_success is null) " );
			}
		}
		
		if(!StringUtils.isBlank(args.get("startTime"))){
			sb.append("  and ts.created_date >=to_date('"+args.get("startTime")+"', 'yyyy-mm-dd')");
		}
		if(!StringUtils.isBlank(args.get("endTime"))){
			sb.append("  and ts.created_date <to_date('"+args.get("endTime")+"', 'yyyy-mm-dd')");
		}
		sb.append(" order by ts.created_date desc");
		return PageFactory.createMapPageBySql2(this, sb.toString(), sqlargs, currentPage, pageSize);
	}
	
	
	public Page waybillByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		Map sqlargs = new HashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("select w.* from t_consignor_info c  join t_waybill_info w on c.id=w.consignor_id where 1=1  ");
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sb.append(" and c.proxy_user_id="+args.get("proxyUserId"));
		}
		if(!StringUtils.isBlank(args.get("content"))){
			sb.append("  and w.origin like :content ");
			sqlargs.put("content", "%"+args.get("content")+"%");
		}
		sb.append(" order by w.id desc");
		return PageFactory.createPageBySql(this, sb.toString(), sqlargs, WaybillInfo.class, currentPage, pageSize);
	}
	
	public Page proxyBalanceStatements(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		Map sqlargs = new HashMap<String,String>();
		StringBuffer sb = new StringBuffer();
		sb.append("select *  "
				+ "from proxy_statements ts "
				+ "where 1=1  ");
		if(!StringUtils.isBlank(args.get("orderNo"))){
			sb.append("  and order_no like :orderNo ");
			sqlargs.put("orderNo", "%"+args.get("orderNo")+"%");
		}
		if(!StringUtils.isBlank(args.get("proxyUserId"))){
			sb.append(" and ts.proxy_user_id="+args.get("proxyUserId"));
		}
		if(!StringUtils.isBlank(args.get("type"))){
			sb.append(" and ts.type="+args.get("type"));
		}
		if(!StringUtils.isBlank(args.get("startTime"))){
			sb.append("  and ts.created_date >=to_date('"+args.get("startTime")+"', 'yyyy-mm-dd')");
		}
		if(!StringUtils.isBlank(args.get("endTime"))){
			sb.append("  and ts.created_date <to_date('"+args.get("endTime")+"', 'yyyy-mm-dd')");
		}
		sb.append(" order by ts.created_date desc");
		return PageFactory.createPageBySql(this, sb.toString(), sqlargs, ProxyStatements.class, currentPage, pageSize);
	}
	
/*	public Page companyAccountBooks(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		Map sqlargs = new HashMap<String,String>();
		StringBuffer fy = new StringBuffer();
		StringBuffer tj = new StringBuffer();
		StringBuffer countsql = new StringBuffer();
		fy.append("select   cts1.id cts1id, cts1.account_id cts1userId,cts1.order_no cts1orderNo,cts1.pay cts1pay,cts1.remark cts1remark,cts1.user_type cts1userType,cts1.pay_points cts1point,cts1.created_date cts1date,"
				+ "cts2.id cts2id, cts2.account_id cts2userId,cts2.order_no cts2orderNo,cts2.pay cts2pay,cts2.remark cts2remark,cts2.user_type cts2userType,cts2.pay_points cts2point,cts2.created_date cts2date  "
				+ " from (select * from t_company_transaction_statements where is_turnout=false) cts1 left join (select * from t_company_transaction_statements where is_turnout=true) cts2 on cts1.order_no=cts2.order_no where 1=1 and cts1.trade_type <>4");
		
		if(!StringUtils.isBlank(args.get("orderNo"))){
			fy.append(" and cts1.order_no like :orderNo ");
			sqlargs.put("orderNo", "%"+args.get("orderNo")+"%");
		}
		if(!StringUtils.isBlank(args.get("startTime"))){
			fy.append("  and cts1.created_date >to_date('"+args.get("startTime")+"', 'yyyy-mm-dd')");
			//sqlargs.put("startTime", "to_date("+args.get("startTime")+", 'yyyy-mm-dd')");
		}
		if(!StringUtils.isBlank(args.get("endTime"))){
			fy.append("  and cts1.created_date <to_date('"+args.get("endTime")+"', 'yyyy-mm-dd')");
			//sqlargs.put("endTime", "to_date("+args.get("endTime")+", 'yyyy-mm-dd')");
		}
		
		fy.append(" union all ");
		
		fy.append("select   cts1.id cts1id, cts1.account_id cts1userId,cts1.order_no cts1orderNo,cts1.pay cts1pay,cts1.remark cts1remark,cts1.user_type cts1userType,cts1.pay_points cts1point,cts1.created_date cts1date,"
				+ "cts2.id cts2id, cts2.account_id cts2userId,cts2.order_no cts2orderNo,cts2.pay cts2pay,cts2.remark cts2remark,cts2.user_type cts2userType,cts2.pay_points cts2point,cts2.created_date cts2date  "
				+ " from (select * from t_company_transaction_statements where is_turnout=false) cts1 left join (select * from t_company_transaction_statements where is_turnout=true) cts2  on cts1.order_no=cts2.order_no and cts1.account_id=cts2.account_id where 1=1 and cts1.trade_type =4 ");
		
		if(!StringUtils.isBlank(args.get("orderNo"))){
			fy.append(" and cts1.order_no like :orderNo ");
			sqlargs.put("orderNo", "%"+args.get("orderNo")+"%");
		}
		
		if(!StringUtils.isBlank(args.get("startTime"))){
			fy.append("  and cts1.created_date >to_date('"+args.get("startTime")+"', 'yyyy-mm-dd')");
			//sqlargs.put("startTime", "to_date("+args.get("startTime")+", 'yyyy-mm-dd')");
		}
		if(!StringUtils.isBlank(args.get("endTime"))){
			fy.append("  and cts1.created_date <to_date('"+args.get("endTime")+"', 'yyyy-mm-dd')");
			//sqlargs.put("endTime", "to_date("+args.get("endTime")+", 'yyyy-mm-dd')");
		}
		
		Page page = PageFactory.createMapPageBySql2(this, fy.toString(), sqlargs, currentPage, pageSize);
		
		
		tj.append("select  sum(cts1pay) totalpay1,sum(cts2pay) totalpay2,sum(cts1pay)+sum(cts2pay) pay12 from (").append(fy).append(") t");
		List<Map> ret = this.loadMapBySQL(tj.toString(), sqlargs);
		if(ret!=null&&ret.size()>0){
			page.setOtherMap(ret.get(0));
		}
		return page;
	}*/
	
	
}
