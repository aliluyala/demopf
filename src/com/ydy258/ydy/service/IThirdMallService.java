package com.ydy258.ydy.service;

import java.util.List;
import java.util.Map;

import com.ydy258.ydy.entity.ThirdMall;
import com.ydy258.ydy.entity.ThirdMallOrder;
import com.ydy258.ydy.entity.ThirdMallTeller;
import com.ydy258.ydy.util.Page;

public interface IThirdMallService extends  IBaseService {
	public Page thirdMallListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception;
	public Page thirdMallGoodsListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception;
	public Page thirdMallTellerListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception;
	public Page thirdMallOrderListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception ;
	public ThirdMall getMallByUserId(Long userId) throws Exception;
	public ThirdMallTeller getTellerByTellerNo(String tellerNo,long storeId) throws Exception;
	public ThirdMallTeller getTellerByTellerName(String tellerName) throws Exception;
	public List<Map> thirdMallListByPage(Map<String,String> where) throws Exception;
	public List thirdMallOrderByOrderNo(Map<String,String> where) throws Exception ;
	public ThirdMallOrder thirdMallOrderByOrderNo(String orderNo) throws Exception ;
	public List thirdMallOrderExcel(Map<String,String> where) throws Exception ;
	
	public Page goodsCategoryListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception ;
}
