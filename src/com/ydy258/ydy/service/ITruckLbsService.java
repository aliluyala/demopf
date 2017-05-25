package com.ydy258.ydy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ydy258.ydy.entity.TruckLocation;

public interface ITruckLbsService extends IBaseService {
	List<?> getNearTruckList(String destination, String truckType, Double truckLength, Double longitude, 
			Double latitude, Integer radius, Integer num);
	public List<TruckLocation> getAllTruck();
	List<?> queryTruckList(HashMap<String, Object> conds, Double longitude, Double latitude, Long firstId, Long lastId, Integer num);
	
	public List<Map> getAllProvinceTruck() throws Exception ;
}