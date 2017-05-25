package com.ydy258.ydy.dao;

import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import com.ydy258.ydy.entity.CommData;
import com.ydy258.ydy.entity.Region;


public interface ICommDataDao extends IBaseDao {
	
	public List<CommData> queryParent() throws Exception;
	
	
	public List<CommData> queryFirstParent() throws Exception;

	public List<CommData> queryByParentId(String parentId) throws Exception;
	
	public List<Region> getProvinces();

	public List<Region> getCitiesByProvinceId(Long ProvinceId);

	public List<Region> getAreaByCityId(Long cityId);
}
