package com.ydy258.ydy.service;



public interface ISysTallyService extends IBaseService {
	
	public  boolean sysTally(Double money,Short userType,String orderNo,Long userId,String remark,Integer tradeType,boolean isTurnout);
}
