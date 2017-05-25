package com.ydy258.ydy.service;

import java.util.List;
import java.util.Map;

import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.util.Page;


public interface InsuranceService extends IBaseService {
	public Page ddcxwyInsuranceByPage(Map where,int currentPage,int pageSize) throws Exception;
}
