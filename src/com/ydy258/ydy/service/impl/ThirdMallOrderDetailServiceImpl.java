package com.ydy258.ydy.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ydy258.ydy.dao.IThirdMallOrderDetailDao;
import com.ydy258.ydy.entity.ThirdMallOrderDetail;
import com.ydy258.ydy.service.IThirdMallOrderDetailService;

@Service("thirdMallOrderDetailService")
public class ThirdMallOrderDetailServiceImpl implements IThirdMallOrderDetailService {	
	Logger logger = Logger.getLogger(ThirdMallOrderDetailServiceImpl.class);

	@Autowired
	private IThirdMallOrderDetailDao orderDetailDao;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(Object entity) {
		// TODO Auto-generated method stub
		orderDetailDao.save(entity);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void update(Object entity) {
		// TODO Auto-generated method stub
		orderDetailDao.update(entity);
	}

	@Override
	public <T> T getById(Class<T> clazz, Object id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> void delete(Class<T> clazz, Long id) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ThirdMallOrderDetail> getByOrderNo(
			String orderNo) {
		return orderDetailDao.getByOrderNo(orderNo);
	}

}