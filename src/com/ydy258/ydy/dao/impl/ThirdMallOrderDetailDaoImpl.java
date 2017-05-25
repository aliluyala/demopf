package com.ydy258.ydy.dao.impl;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import com.ydy258.ydy.dao.IThirdMallOrderDetailDao;
import com.ydy258.ydy.entity.ThirdMallOrderDetail;

@Repository
public class ThirdMallOrderDetailDaoImpl extends BaseDaoImpl implements IThirdMallOrderDetailDao {

	@Override
	/**
	 * 返回指定订单的商品列表
	 */
	public List<ThirdMallOrderDetail> getByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		if(orderNo == null)
			return null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ThirdMallOrderDetail> criteriaQuery = criteriaBuilder.createQuery(ThirdMallOrderDetail.class);
		Root<ThirdMallOrderDetail> order = criteriaQuery.from(ThirdMallOrderDetail.class);		
		criteriaQuery.select(order);		
		criteriaQuery.where(criteriaBuilder.equal(order.get("orderNo").as(String.class), orderNo));
		
		return entityManager.createQuery(criteriaQuery).getResultList();		
	}
}