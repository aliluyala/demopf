package com.ydy258.ydy.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ydy258.ydy.dao.ITruckLocationDao;
import com.ydy258.ydy.entity.TruckLocation;
import com.ydy258.ydy.service.ITruckLbsService;

@Service("truckLbsService")
public class TruckLbsServiceImpl implements ITruckLbsService {
	private Logger logger = Logger.getLogger(TruckLbsServiceImpl.class);
	
	@Autowired
	private ITruckLocationDao truckLocationDao;
	
	/**
	 * 取得指定经纬度周边货车列表（货主服务）
	 * @param longitude -- 中心点经度
	 * @param latitude -- 中心半纬度
	 * @param radius -- 半径(以米为单位)
	 * @param num -- 返回数量
	 */	
	@Override
	@Transactional(readOnly = true)	
	//@Cacheable
	public List<?> getNearTruckList(String destination, String truckType, Double truckLength, 
			Double longitude, Double latitude, Integer radius, Integer num) {
		return truckLocationDao.getNearTruckList(destination, truckType, truckLength, longitude, latitude, radius, num);
	}


	public List<Map> getAllProvinceTruck() throws Exception {
		return truckLocationDao.getAllProvinceTruck();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<?> queryTruckList(HashMap<String, Object> conds,
			Double longitude, Double latitude, Long firstId, Long lastId, Integer num) {
		// TODO Auto-generated method stub
		return truckLocationDao.queryTruckList(conds, longitude, latitude, firstId, lastId, num);
	}

	public List<TruckLocation> getAllTruck(){
		return truckLocationDao.getAllTruck();
	}

	@Override
	public void save(Object entity) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void update(Object entity) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public <T> T getById(Class<T> clazz, Object id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <T> void delete(Class<T> clazz, Long id) throws Exception {
		// TODO Auto-generated method stub
		
	}	
}