package com.ydy258.ydy.service.impl;

import com.ydy258.ydy.entity.SysUserOperation;
import com.ydy258.ydy.service.ISysUserLogService;

public class SysUserLogServiceImpl extends IBaseServiceImpl implements ISysUserLogService {
	
	public void saveLog(String userId,String dataId,String actionName,String actionT) throws Exception{
		SysUserOperation suo = new SysUserOperation();
		suo.setActionName(actionName);
		suo.setUserId(userId);
		suo.setDataId(dataId);
		suo.setActionTranslation(actionT);
		this.save(suo);
	}

}
