package com.ydy258.ydy.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydy258.ydy.dao.IInsuranceDao;
import com.ydy258.ydy.service.InsuranceService;
import com.ydy258.ydy.util.Page;

@Service("insuranceService")
public class InsuranceServiceImpl extends IBaseServiceImpl implements InsuranceService {
		
	@Autowired
	private IInsuranceDao insuranceDao;
	
	public Page ddcxwyInsuranceByPage(Map where,int currentPage,int pageSize) throws Exception {
		return insuranceDao.ddwyinsuranceListByPage(where, currentPage, pageSize);
    }
	
}
