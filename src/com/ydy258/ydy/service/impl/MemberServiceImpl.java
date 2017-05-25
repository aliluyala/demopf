package com.ydy258.ydy.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydy258.ydy.dao.IMemberDao;
import com.ydy258.ydy.entity.Consignor;
import com.ydy258.ydy.entity.FreightLine;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.entity.Warehouse;
import com.ydy258.ydy.service.IMemberService;
import com.ydy258.ydy.util.Page;

@Service("memberService")
public class MemberServiceImpl extends IBaseServiceImpl implements IMemberService {
		
	@Autowired
	private IMemberDao memberDao;
	
	
	public Page truckUserByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		return memberDao.truckUserByPage(args, currentPage, pageSize);
    }

	public List truckUserStatements(Map<String,String> args) throws Exception {
		return memberDao.truckUserStatements(args);
	}

	public Page consignorUserByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		return memberDao.consignorUserByPage(args, currentPage, pageSize);
    }

	public List consignorUserStatements(Map<String,String> args) throws Exception {
		return memberDao.consignorUserStatements(args);
	}
	
	public Page feebackQuery(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		return memberDao.feebackQuery(args, currentPage, pageSize);
	}
	
	public Truck getByPlateNumber(String plateNumber) throws Exception  {
		return memberDao.getByPlateNumber(plateNumber);
	}
	
	
	public Truck getByMobile(String mobile) throws Exception   {
		return memberDao.getByMobile(mobile);
	}
	
	public void deleteByTruckId(long truckId) throws Exception {
		memberDao.deleteByTruckId(truckId);
	}
	
	public Consignor getbyMobileNo(String mobileNo){
		return memberDao.getbyMobileNo(mobileNo);
	}
	public boolean userIsExists(String mobileNo){
		return memberDao.userIsExists(mobileNo);
	}
	
	public List<FreightLine> consignorCompanyByPage(Map<String,String> args) throws Exception {
		return memberDao.consignorCompanyByPage(args);
	}
	
	
	public List<Warehouse> warehouseByPage(Map<String,String> args) throws Exception {
		return memberDao.warehouseByPage(args);
	}
}
