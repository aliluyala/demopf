package com.ydy258.ydy.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ydy258.ydy.dao.IThirdMallDao;
import com.ydy258.ydy.entity.GoodsCategory;
import com.ydy258.ydy.entity.ThirdMall;
import com.ydy258.ydy.entity.ThirdMallGoods;
import com.ydy258.ydy.entity.ThirdMallOrder;
import com.ydy258.ydy.entity.ThirdMallTeller;
import com.ydy258.ydy.util.DateUtil;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.PageFactory;

@Repository
public class ThirdMallDaoImpl extends BaseDaoImpl implements IThirdMallDao {
	/*@Table(name = "t_third_mall")
	public class ThirdMall implements Serializable {
		private static final long serialVersionUID = -902636920240631901L;

		private Long id;
		private String storeName; // 商户名称
		private Integer storeType;	//商户类型
		private String address; // 商户地址
		private Long agencyId = 0L; // 上级代理id
		private Geometry location; // 经纬度
		private String geohash; // geohash值
		private Double ratio; // 分成比例
		private String description; // 商户介绍
*/
	public Page thirdMallListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select mall.ret_radio,mall.is_first_ret,mall.id,mall.store_name,mall.store_type,mall.address,mall.description,mall.created_date,users.real_name,users.user_name,mall.phone,mall.is_real from sys_user users join t_third_mall mall on users.id=mall.addmin_id where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("storeName"))){
			sbsql.append(" and  mall.store_name like :storeName");
			args.put("storeName", "%"+where.get("storeName")+"%");
		}
		if(!StringUtils.isBlank(where.get("realName"))){
			sbsql.append(" and  users.real_name like :realName");
			args.put("realName", "%"+where.get("realName")+"%");
		}
		if(!StringUtils.isBlank(where.get("agencyId"))){
			sbsql.append(" and  mall.agency_id = :agencyId");
			args.put("agencyId", Long.valueOf(where.get("agencyId")));
		}
		if(!StringUtils.isBlank(where.get("storeType"))){
			sbsql.append(" and  mall.store_type = :storeType");
			args.put("storeType", where.get("storeType"));
		}
		sbsql.append(" order by mall.created_date desc");
		return PageFactory.createMapPageBySql(this, sbsql.toString(), args, currentPage, pageSize);
    }
	
	public List thirdMallListByPage(Map<String,String> where) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select mall.id,mall.store_name,mall.addmin_id from sys_user users join t_third_mall mall on users.id=mall.addmin_id  where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("agencyId"))){
			sbsql.append(" and  mall.agency_id = :agencyId");
			args.put("agencyId", Long.valueOf(where.get("agencyId")));
		}
		sbsql.append(" order by mall.created_date desc");
		return this.loadMapBySQL(sbsql.toString(), args);
    }
	
	
	public ThirdMall getMallByUserId(Long userId) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select mall.* from t_third_mall mall where 1=1 and addmin_id="+userId);
		List l = this.loadBySQL(sbsql.toString(), null, ThirdMall.class);
		if(l!=null&&l.size()>0){
			return (ThirdMall) l.get(0);
		}
		return null;
    }
	
	public ThirdMallTeller getTellerByTellerNo(String tellerNo,long storeId) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select teller.* from t_third_mall_teller teller where 1=1 and teller_id='"+tellerNo+"' and store_id="+storeId);
		List l = this.loadBySQL(sbsql.toString(), null, ThirdMallTeller.class);
		if(l!=null&&l.size()>0){
			return (ThirdMallTeller) l.get(0);
		}
		return null;
    }
	
	
	public ThirdMallTeller getTellerByTellerName(String tellerName) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select teller.* from t_third_mall_teller teller where 1=1 and teller_name='"+tellerName+"'");
		List l = this.loadBySQL(sbsql.toString(), null, ThirdMallTeller.class);
		if(l!=null&&l.size()>0){
			return (ThirdMallTeller) l.get(0);
		}
		return null;
    }
	
	/*
	@Table(name = "t_third_mall_goods")
	public class ThirdMallGoods implements Serializable {
		private static final long serialVersionUID = 7741539535421021817L;
		private Long id;
		private Long storeId; // 商户ID
		private String goodsName; // 商品名称
		private Double price; // 单价
		private Double costPrice;	//成本价
		private String units;	//计量单位
		private Integer storedNumber; // 库存数量
		private Double ratio; // 补贴率
		private Double returnRatio; // 返现比率
*/
	public Page thirdMallGoodsListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select ThirdMallGoods.* from t_third_mall_goods ThirdMallGoods left join  t_third_mall ThirdMall on ThirdMallGoods.store_id=ThirdMall.id  where 1=1 and ThirdMallGoods.is_delete=false ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("goodsName"))){
			sbsql.append(" and  ThirdMallGoods.goods_name like :goodsName");
			args.put("goodsName", "%"+where.get("goodsName")+"%");
		}
		if(!StringUtils.isBlank(where.get("storeId"))){
			sbsql.append(" and  ThirdMallGoods.store_id = :storeId");
			args.put("storeId", Long.valueOf(where.get("storeId")));
		}
		if(!StringUtils.isBlank(where.get("proxyId"))){
			sbsql.append(" and  ThirdMall.agency_id = :proxyId");
			args.put("proxyId", Long.valueOf(where.get("proxyId")));
		}
		sbsql.append(" order by ThirdMallGoods.id desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args, ThirdMallGoods.class,currentPage, pageSize);
    }
	
	/*@Table(name = "t_third_mall_teller")
	public class ThirdMallTeller implements Serializable {
		private static final long serialVersionUID = 6721198869320312826L;
		private Long id;
		private String tellerId; // 柜员id
		private Long storeId; // 商户ID
		private String tellerName; // 柜员姓名
		private String password; // 登录密码
		private String jpushId; // 登录手机的极光推送ID
		private String deviceId; // 登录手机的设备ID
		private Date lastLogin; // 最后一次登录时间
		private String lastIp; // 登录时的IP
		private Boolean isLock = false;	//锁定标志	
*/
	public Page thirdMallTellerListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select ThirdMallTeller.* from t_third_mall_teller ThirdMallTeller left join t_third_mall ThirdMall on ThirdMallTeller.store_id=ThirdMall.id where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("tellerName"))){
			sbsql.append(" and  ThirdMallTeller.teller_name like :tellerName");
			args.put("tellerName", "%"+where.get("tellerName")+"%");
		}
		if(!StringUtils.isBlank(where.get("storeId"))){
			sbsql.append(" and  ThirdMallTeller.store_id = :storeId");
			args.put("storeId", Long.valueOf(where.get("storeId")));
		}
		if(!StringUtils.isBlank(where.get("proxyId"))){
			sbsql.append(" and  ThirdMall.agency_id = :proxyId");
			args.put("proxyId", Long.valueOf(where.get("proxyId")));
		}
		sbsql.append(" order by ThirdMallTeller.created_date desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,ThirdMallTeller.class, currentPage, pageSize);
    }
	
	
	public Page goodsCategoryListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select cate.* from t_goods_category cate  where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("categoryName"))){
			sbsql.append(" and  cate.cate_name like :cateName");
			args.put("cateName", "%"+where.get("categoryName")+"%");
		}
		sbsql.append(" order by cate.id desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,GoodsCategory.class, currentPage, pageSize);
    }
	
	
	public Page goodsCategoryListByStoreId(Map<String,String> where) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select cate.* from t_goods_category cate  where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("storeId"))){
			sbsql.append(" and  cate.mall_id = :storeId");
			args.put("storeId", where.get("storeId"));
		}
		sbsql.append(" order by cate.id desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,GoodsCategory.class, 1, 1);
    }
	
	/*@Table(name = "t_third_mall_order")
	public class ThirdMallOrder implements Serializable {
		private static final long serialVersionUID = 1217185864952224325L;
		private Long id;
		private String orderNo; // 订单号（流水号）
		private Long storeId; // 商户ID
		private String tellerId;	//柜员号
		private Long agencyId;	//代理ID
		private String customName; // 消费者姓名
		private Long goodsId; // 消费商品ID
		private Double goodsNumber; // 消费数量
		private Double amount; // 总价格
		private Double cost;	//成本价
		private Double discount; // 折扣价（消费者实付金额）
		private Integer points; // 补贴额（补贴的驿道币）
		private Date addTime; // 下单时间
		private Integer orderStatus; // 订单状态（待付款、成交、取消，...）
		private Date payTime; // 支付时间
*/		
	public Page thirdMallOrderListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select orders.*,teller.teller_name from t_third_mall_teller teller right join t_third_mall_order orders on teller.teller_id=orders.teller_id  where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("tellerName"))){
			sbsql.append(" and  teller.teller_name like :tellerName");
			args.put("tellerName", "%"+where.get("tellerName")+"%");
		}
		if(!StringUtils.isBlank(where.get("customName"))){
			sbsql.append(" and  teller.custom_name like :customName");
			args.put("customName", "%"+where.get("customName")+"%");
		}
		if(!StringUtils.isBlank(where.get("agencyId"))){
			sbsql.append(" and  orders.agency_id = :agencyId");
			args.put("agencyId", Long.valueOf(where.get("agencyId")));
		}
		if(!StringUtils.isBlank(where.get("storeId"))){
			sbsql.append(" and  orders.store_id = :storeId");
			args.put("storeId", Long.valueOf(where.get("storeId")));
		}
		if(!StringUtils.isBlank(where.get("orderStatus"))){
			sbsql.append(" and  orders.order_status = :orderStatus");
			args.put("orderStatus", Integer.valueOf(where.get("orderStatus")));
		}
		if(!StringUtils.isBlank(where.get("orderNo"))){
			sbsql.append(" and  orders.order_no like :orderNo");
			args.put("orderNo", "%"+where.get("orderNo")+"%");
		}
		if(!StringUtils.isBlank(where.get("date"))){
			sbsql.append(" and  orders.pay_time >= :payTime1");
			args.put("payTime1", DateUtil.getDateByStr(where.get("date")+" 00:00:00"));
			sbsql.append(" and  orders.pay_time <= :payTime2");
			args.put("payTime2",  DateUtil.getDateByStr(where.get("date")+" 23:59:59"));
		}
		sbsql.append(" order by id desc");
		return PageFactory.createMapPageBySql(this, sbsql.toString(), args,currentPage, pageSize);
    }
	
	public List thirdMallOrderExcel(Map<String,String> where) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select orders.*,teller.teller_name from t_third_mall_teller teller right join t_third_mall_order orders on teller.teller_id=orders.teller_id  where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("tellerName"))){
			sbsql.append(" and  teller.teller_name like :tellerName");
			args.put("tellerName", "%"+where.get("tellerName")+"%");
		}
		if(!StringUtils.isBlank(where.get("customName"))){
			sbsql.append(" and  teller.custom_name like :customName");
			args.put("customName", "%"+where.get("customName")+"%");
		}
		if(!StringUtils.isBlank(where.get("agencyId"))){
			sbsql.append(" and  orders.agency_id = :agencyId");
			args.put("agencyId", Long.valueOf(where.get("agencyId")));
		}
		if(!StringUtils.isBlank(where.get("storeId"))){
			sbsql.append(" and  orders.store_id = :storeId");
			args.put("storeId", Long.valueOf(where.get("storeId")));
		}
		if(!StringUtils.isBlank(where.get("orderNo"))){
			sbsql.append(" and  orders.order_no like :orderNo");
			args.put("orderNo", "%"+where.get("orderNo")+"%");
		}
		if(!StringUtils.isBlank(where.get("orderStatus"))){
			sbsql.append(" and  orders.order_status = :orderStatus");
			args.put("orderStatus", Integer.valueOf(where.get("orderStatus")));
		}
		if(!StringUtils.isBlank(where.get("date"))){
			sbsql.append(" and  orders.pay_time >= :payTime1");
			args.put("payTime1", DateUtil.getDateByStr(where.get("date")+" 00:00:00"));
			sbsql.append(" and  orders.pay_time <= :payTime2");
			args.put("payTime2",  DateUtil.getDateByStr(where.get("date")+" 23:59:59"));
		}
		sbsql.append(" order by id desc");
		return this.loadMapBySQL(sbsql.toString(), args);
    }
	
	public List thirdMallOrderByOrderNo(Map<String,String> where) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select orders.*,teller.teller_name from t_third_mall_teller teller right join t_third_mall_order orders on teller.teller_id=orders.teller_id  where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("orderNo"))){
			sbsql.append(" and  orders.order_no = :orderNo");
			args.put("orderNo", where.get("orderNo"));
		}
		sbsql.append(" order by id desc");
		return this.loadByJPQL(sbsql.toString(), args);
    }
	
	public ThirdMallOrder thirdMallOrderByOrderNo(String orderNo) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select * from t_third_mall_order orders  where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(orderNo)){
			sbsql.append(" and  orders.order_no = :orderNo");
			args.put("orderNo", orderNo);
		}
		List<ThirdMallOrder> list = this.loadBySQL(sbsql.toString(), args, ThirdMallOrder.class);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
    }
}
