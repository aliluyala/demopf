package com.ydy258.ydy.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydy258.ydy.dao.IThirdMallDao;
import com.ydy258.ydy.entity.ThirdMall;
import com.ydy258.ydy.entity.ThirdMallOrder;
import com.ydy258.ydy.entity.ThirdMallTeller;
import com.ydy258.ydy.service.IThirdMallService;
import com.ydy258.ydy.util.Page;
@Service("thirdMallService")
public class ThirdMallServiceImpl  extends IBaseServiceImpl implements  IThirdMallService {

	@Autowired
	private IThirdMallDao thirdMallDao;
	
	public Page thirdMallListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception{
		return thirdMallDao.thirdMallListByPage(where, currentPage, pageSize);
	}
	
	public List<Map> thirdMallListByPage(Map<String,String> where) throws Exception{
		return thirdMallDao.thirdMallListByPage(where);
	}
	
	public Page thirdMallGoodsListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception{
		return thirdMallDao.thirdMallGoodsListByPage(where, currentPage, pageSize);
	}
	public Page thirdMallTellerListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception{
		return thirdMallDao.thirdMallTellerListByPage(where, currentPage, pageSize);
	}
	public Page thirdMallOrderListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		return thirdMallDao.thirdMallOrderListByPage(where, currentPage, pageSize);
	}
	
	public List thirdMallOrderExcel(Map<String,String> where) throws Exception {
		return thirdMallDao.thirdMallOrderExcel(where);
	}
	
	public List thirdMallOrderByOrderNo(Map<String,String> where) throws Exception {
		return thirdMallDao.thirdMallOrderByOrderNo(where);
	}
	
	public ThirdMallOrder thirdMallOrderByOrderNo(String orderNo) throws Exception {
		return thirdMallDao.thirdMallOrderByOrderNo(orderNo);
	}
	
	public ThirdMall getMallByUserId(Long userId) throws Exception {
		return thirdMallDao.getMallByUserId(userId);
	}
	
	public ThirdMallTeller getTellerByTellerNo(String tellerNo,long storeId) throws Exception {
		return thirdMallDao.getTellerByTellerNo(tellerNo,storeId);
	}
	
	public ThirdMallTeller getTellerByTellerName(String tellerName) throws Exception {
		return thirdMallDao.getTellerByTellerName(tellerName);
	}
	
	
	public Page goodsCategoryListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		return thirdMallDao.goodsCategoryListByPage(where, currentPage, pageSize);
	}
}
