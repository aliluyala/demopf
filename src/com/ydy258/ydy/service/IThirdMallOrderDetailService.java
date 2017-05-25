package com.ydy258.ydy.service;

import java.util.List;

import com.ydy258.ydy.entity.ThirdMallOrderDetail;

public interface IThirdMallOrderDetailService extends IBaseService {
	List<ThirdMallOrderDetail> getByOrderNo(String orderNo);
}
