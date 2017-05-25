package com.ydy258.ydy.dao;

import java.util.List;
import java.util.Map;

import com.ydy258.ydy.entity.ThirdMall;
import com.ydy258.ydy.entity.ThirdMallOrder;
import com.ydy258.ydy.entity.ThirdMallTeller;
import com.ydy258.ydy.entity.TruckCard;
import com.ydy258.ydy.util.Page;


public interface ICardRhDao extends IBaseDao {
	public Page truckCardRHPriceByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception ;
	
	public Page truckCardRHCNPCByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception ;
	
	public TruckCard truckCardHistory(String cardNumber) throws Exception ;
}
