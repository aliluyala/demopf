package com.ydy258.ydy.dao.impl;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.dao.ICommDataDao;
import com.ydy258.ydy.entity.CommData;
import com.ydy258.ydy.entity.Region;

@Repository
public class CommDataDaoImpl extends BaseDaoImpl implements ICommDataDao {
	
	
	public List<CommData> queryParent() throws Exception {
		String sql = " select dd.* from t_comm_data dd where dd.is_leaf="+Constants.isLeaf.no.getValue();
		return this.loadBySQL(sql, null, CommData.class);
	}
	
	
	public List<CommData> queryFirstParent() throws Exception {
		String sql = " select dd.* from t_comm_data dd where dd.parent_id=-1";
		return this.loadBySQL(sql, null, CommData.class);
	}

	public List<CommData> queryByParentId(String parentId) throws Exception {
		String sql = " select dd.* from t_comm_data dd where dd.parent_id="+parentId+" order by id";
		return this.loadBySQL(sql, null, CommData.class);
	}


	
	@Override
	public List<Region> getProvinces() {
		// TODO Auto-generated method stub		
		String jpql = "select region from Region as region where region.parentId=1";
		TypedQuery<Region> query = entityManager.createQuery(jpql, Region.class).setFlushMode(FlushModeType.COMMIT);		
		return query.getResultList();
	}

	@Override
	public List<Region> getCitiesByProvinceId(Long ProvinceId) {
		// TODO Auto-generated method stub
		String jpql = "select region from Region as region where region.parentId="+ProvinceId;
		TypedQuery<Region> query = entityManager.createQuery(jpql, Region.class).setFlushMode(FlushModeType.COMMIT);		
		return query.getResultList();
	}

	@Override
	public List<Region> getAreaByCityId(Long cityId) {
		// TODO Auto-generated method stub
		String jpql = "select region from Region as region where region.parentId="+cityId;
		TypedQuery<Region> query = entityManager.createQuery(jpql, Region.class).setFlushMode(FlushModeType.COMMIT);		
		return query.getResultList();
	}
}
