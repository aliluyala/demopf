package com.ydy258.ydy.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.ydy258.ydy.entity.Consignor;
import com.ydy258.ydy.entity.FreightLine;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.entity.Warehouse;
import com.ydy258.ydy.util.Page;


public interface IMemberDao extends IBaseDao {
	
	public Page truckUserByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception;

	public List truckUserStatements(Map<String,String> args) throws Exception;
	
	public Page consignorUserByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public List consignorUserStatements(Map<String,String> args) throws Exception;
	
	public Page feebackQuery(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public Truck getByPlateNumber(String plateNumber) throws Exception  ;
	
	public Truck getByMobile(String mobile) throws Exception  ;
	
	public void deleteByTruckId(long truckId) throws Exception   ;
	
	
	public Consignor getbyMobileNo(String mobileNo);
	public boolean userIsExists(String mobileNo);
	
	public List<FreightLine> consignorCompanyByPage(Map<String,String> args) throws Exception ;
	
	public List<Warehouse> warehouseByPage(Map<String,String> args) throws Exception ;
}
