package com.ydy258.ydy.service;

import java.util.List;
import java.util.Map;

import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.entity.TruckCard;
import com.ydy258.ydy.util.Page;


public interface ICardRhService extends IBaseService {
	public Page truckCardRHPriceByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception ;
	
	public Page truckCardRHCNPCByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception ;
	
	public TruckCard truckCardHistory(String cardNumber) throws Exception ;
}
