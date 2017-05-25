package com.ydy258.ydy.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.ydy258.ydy.AppConst;
import com.ydy258.ydy.StringUtil;
import com.ydy258.ydy.dao.ITruckLocationDao;
import com.ydy258.ydy.entity.TruckLocation;
import com.ydy258.ydy.util.GeoHash;

@Repository
public class TruckLocationDaoImpl extends BaseDaoImpl implements ITruckLocationDao {	
	private Logger logger = Logger.getLogger(TruckLocationDaoImpl.class);
	
	@Autowired
	private JedisPool jedisPool;	
	
	/**
	 * 返回指定范围、指定目的地、指定车型的货车列表
	 */	
	public List<?> getNearTruckList(String destination, String truckType, 
			Double truckLength, Double longitude, Double latitude, Integer radius, Integer num) {
		if(longitude == 0 && latitude == 0)
			return null;
		new GeoHash();
		//计算经纬度的geohash值
		String geohash = GeoHash.encode(latitude, longitude).substring(0, AppConst.GEOHASH_LENGTH);
		String[] near9 = GeoHash.getGeoHashExpand(geohash);
		
		//从redis缓存中读取指定区域的所有车辆ID
		Jedis jedis = jedisPool.getResource();
		Object[] ids = null;
		try {
			ids = jedis.sunion(near9).toArray();		
		}
		finally {
			jedis.close();
		}
		
		if(ids == null || ids.length == 0)
			return null;
		
		/*Long[] lids = new Long[ids.length];
		for(int i=0; i<ids.length; i++) {
			lids[i] = Long.parseLong(ids[i].toString());
		}*/
		String sids = StringUtils.join(ids, ",");
		StringBuilder sb = new StringBuilder();
				
		//double delta = radius / (100 * 1000);		
		/*sb.append("SELECT * from (SELECT t1.*,ST_Distance(ST_Transform(ST_GeomFromText('POINT(%f %f)',4326),2343),");
		sb.append("ST_Transform(ST_GeomFromWKB(location,4326),2343)) as distance FROM ");
		sb.append("(SELECT * FROM t_truck_location WHERE ");
		sb.append("ST_Within(location, ST_MakeEnvelope(%f,%f,%f,%f))) as t1) as t2");
		sb.append(" WHERE send_loc_time>now()-interval '1 hour' AND distance<:radius");*/
		
		sb.append("SELECT loc.*,ST_Distance(ST_Transform(ST_GeomFromText('POINT(%f %f)',4326),2343),");
		sb.append("ST_Transform(ST_GeomFromWKB(location,4326),2343)) as distance,t.driver_pic FROM ");
		sb.append("t_truck_location AS loc,t_truck_info AS t WHERE (loc.id IN (%s)) ");	
		//sb.append(" AND (send_loc_time>now()-interval '1 hour')");
		sb.append(" AND ST_Distance(ST_Transform(ST_GeomFromText('POINT(%f %f)',4326),2343),");
		sb.append("ST_Transform(ST_GeomFromWKB(location,4326),2343)) < :radius AND loc.truck_id=t.id");
		
		if(destination != null && destination.length() > 0) {			
			//sb.append(" AND POSITION(:dest IN loc.destination) <> 0");
			String s = StringUtil.genSearchPlaceNameConditionSQL("destination", destination);
			sb.append(" AND (" + s + ")");
		}
		
		if(truckType != null && truckType.length() > 0)
			sb.append(" AND loc.truck_type=:truckType");
		
		if(truckLength != null && truckLength > 0.0)
			sb.append(" AND loc.truck_length=:truckLength");
		
		sb.append(" ORDER BY distance");
		
		String sql = sb.toString();
		sql = String.format(sql, longitude, latitude, sids, longitude, latitude);
		logger.debug("[TruckLocationDaoImpl]getNearTruckList, sql=" + sql);
		Query query = entityManager.createNativeQuery(sql, "near_truck");		
		//query.setParameter("lng", longitude);
		//query.setParameter("lat", latitude);
		query.setParameter("radius", radius);
		//query.setParameter("ids", Arrays.asList(lids));
		
		//if(destination != null && destination.length() > 0)
		//	query.setParameter("dest", destination);
		
		if(truckType != null && truckType.length() > 0)
			query.setParameter("truckType", truckType);		
		
		if(truckLength != null && truckLength > 0.0)
			query.setParameter("truckLength", truckLength);
		
		query.setMaxResults(num);				
		return query.getResultList();				
	}
	/**
	 * 根据truckId取得周边指定货车的信息
	 * 用于货主版查询周边货车功能
	 */
	public TruckLocation getTruckInfoByTruckId(Long truckId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<TruckLocation> criteriaQuery = cb.createQuery(TruckLocation.class);
		Root<TruckLocation> loc = criteriaQuery.from(TruckLocation.class);		
		criteriaQuery.select(loc);
		//ListJoin<WaybillOrder, WaybillOrderLog> lj = order.join(order.getModel().getList("logs", WaybillOrderLog.class));
		//Predicate plj = criteriaBuilder.equal(lj.get("orderNo").as(String.class), orderNo);
		criteriaQuery.where(cb.equal(loc.get("truckId"), truckId));
		
		List<TruckLocation> list = entityManager.createQuery(criteriaQuery).setMaxResults(1).getResultList();
		if(list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}
	/**
	 * 根据订单号查找运货货车目前所在的位置
	 * @param orderNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TruckLocation getOrderLocation(String orderNo) {
		String sql = "SELECT * FROM t_truck_location WHERE truck_id=(SELECT truck_id FROM t_waybill_order WHERE order_no=:orderNo)";
		Query query = entityManager.createNativeQuery(sql, TruckLocation.class);
		List<TruckLocation> geos = query.setParameter("orderNo", orderNo).setMaxResults(1).getResultList();
		if(geos.size() > 0)
			return geos.get(0);
		else
			return null;		
	}
	
	
	/**
	 * 根据订单号查找运货货车目前所在的位置
	 * @param orderNo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TruckLocation> getAllTruck() {
		String sql = "SELECT * FROM t_truck_location";
		Query query = entityManager.createNativeQuery(sql, TruckLocation.class);
		List<TruckLocation> geos = query.getResultList();
		return geos;	
	}
	
	@SuppressWarnings("unchecked")
	public List<Map> getAllProvinceTruck() throws Exception {
		String sql = "select substring(address from 1 for 2) pri ,count(*) countt from t_truck_location  group by pri";
		List<Map> list = this.loadMapBySQL(sql);
		return list;	
	}
	
	/**
	 * 返回所有一小时内有更新过位置的车辆信息，仅读取id,truck_id,geohash三个字段
	 * @return
	 */
	public List<Tuple> getNewestTruck() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Tuple> query = cb.createTupleQuery();
		Root<TruckLocation> loc = query.from(TruckLocation.class);
		query.multiselect(loc.get("id").as(Long.class).alias("id"), 
				loc.get("truckId").as(Long.class).alias("truckId"),
				loc.get("geohash").as(String.class).alias("geohash"));
		Date d = DateUtils.addHours(new Date(), -1);
		query.where(cb.greaterThan(loc.get("sendLocTime").as(Date.class), d));
		List<Tuple> results = entityManager.createQuery(query).getResultList();
		return results;
	}
	/**
	 * 根据条件搜索货车列表
	 */	
	public List<?> queryTruckList(HashMap<String, Object> conds, Double longitude, Double latitude, Long firstId, Long lastId, Integer num) {
		if(longitude == 0 && latitude == 0)
			return null;
		
		if(conds == null)
			return null;		
		
		StringBuilder sb = new StringBuilder();		
		
		sb.append("SELECT loc.*,ST_Distance(ST_Transform(ST_GeomFromText('POINT(%f %f)',4326),2343),");
		sb.append("ST_Transform(ST_GeomFromWKB(location,4326),2343)) as distance,t.driver_pic FROM ");
		sb.append("t_truck_location AS loc,t_truck_info AS t WHERE loc.truck_id=t.id");		
		
		Iterator<String> keys = conds.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			if(key.equals("origin")) {
				String s = StringUtil.genSearchPlaceNameConditionSQL("origin", (String)conds.get(key));
				String s2 = StringUtil.genSearchPlaceNameConditionSQL("address", (String)conds.get(key));
				sb.append(" AND ((" + s + ") OR (" + s2 + "))");
			}
			if(key.equals("destination")) {
				String s = StringUtil.genSearchPlaceNameConditionSQL("destination", (String)conds.get(key));
				sb.append(" AND (" + s + ")");
			}
			if(key.equals("truckType")) {				
				sb.append(String.format(" AND loc.truck_type='%s'", conds.get(key)));
			}			
			if(key.equals("truckLength")) {				
				sb.append(String.format(" AND loc.truck_length=%s", conds.get(key)));
			}	
			if(key.equals("plateNumber")) {
				sb.append(String.format(" AND (POSITION('%s' IN loc.plate_number)<>0)", conds.get(key)));
			}
			if(key.equals("driverName")) {
				sb.append(String.format(" AND (POSITION('%s' IN loc.driver_name)<>0)", conds.get(key)));
			}
			if(key.equals("driverMobile")) {
				sb.append(String.format(" AND (POSITION('%s' IN loc.driver_mobile)<>0)", conds.get(key)));
			}
		}		
		if(firstId > 0)
			sb.append(" AND loc.id>" + firstId);	//下拉刷新
		else if(lastId > 0)
			sb.append(" AND loc.id<" + lastId);		//上拉刷新
		
		sb.append(" ORDER BY loc.id DESC");
		
		String sql = sb.toString();
		sql = String.format(sql, longitude, latitude);
		logger.debug("[TruckLocationDaoImpl]queryTruckList, sql=" + sql);
		Query query = entityManager.createNativeQuery(sql, "near_truck");			
		
		query.setMaxResults(num);				
		return query.getResultList();				
	}
}