package com.ydy258.ydy.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydy258.ydy.dao.ICardRhDao;
import com.ydy258.ydy.entity.TruckCard;
import com.ydy258.ydy.service.ICardRhService;
import com.ydy258.ydy.util.Page;

@Service("cardRhServiceImpl")
public class CardRhServiceImpl extends IBaseServiceImpl implements ICardRhService {
		
	@Autowired
	private ICardRhDao cardRhDao;
	
	public Page truckCardRHPriceByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		return cardRhDao.truckCardRHPriceByPage(where, currentPage, pageSize);
	}
	
	public Page truckCardRHCNPCByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		return cardRhDao.truckCardRHCNPCByPage(where, currentPage, pageSize);
	}
	
	public TruckCard truckCardHistory(String cardNumber) throws Exception {
		return cardRhDao.truckCardHistory(cardNumber);
	}
}
