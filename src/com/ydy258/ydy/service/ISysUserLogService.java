package com.ydy258.ydy.service;



public interface ISysUserLogService extends IBaseService {
	public void saveLog(String userId,String dataId,String actionName,String actionT) throws Exception;
}
