package com.ydy258.ydy.dao.impl;

import com.ydy258.ydy.dao.IBaseDao;
import com.ydy258.ydy.entity.SysUserOperation;

public class SysUserLogServiceDao extends BaseDaoImpl implements IBaseDao {
	public void saveLog(String userId,String dataId,String actionName,String actionT) throws Exception{
		SysUserOperation suo = new SysUserOperation();
		suo.setActionName(actionName);
		suo.setUserId(userId);
		suo.setDataId(dataId);
		suo.setActionTranslation(actionT);
		this.save(suo);
	}
}
