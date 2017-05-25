package com.ydy258.ydy.dao;

import java.util.List;

import com.ydy258.ydy.entity.ThirdMallOrderDetail;

public interface IThirdMallOrderDetailDao extends IBaseDao {
	List<ThirdMallOrderDetail> getByOrderNo(String orderNo);
}
