package com.ydy258.ydy.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import com.ydy258.ydy.entity.TruckLocation;

public interface ITruckLocationDao extends IBaseDao {
	List<?> getNearTruckList(String destination, String truckType, Double truckLength, 
			Double longitude, Double latitude, Integer radius, Integer num);
	TruckLocation getTruckInfoByTruckId(Long truckId);
	TruckLocation getOrderLocation(String orderNo);
	List<Tuple> getNewestTruck();
	public List<TruckLocation> getAllTruck();
	List<?> queryTruckList(HashMap<String, Object> conds, Double longitude, Double latitude, Long firstId, Long lastId, Integer num);
	
	public List<Map> getAllProvinceTruck() throws Exception ;
}