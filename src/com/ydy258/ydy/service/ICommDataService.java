package com.ydy258.ydy.service;

import java.util.HashMap;
import java.util.List;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.Notification;

import com.ydy258.ydy.entity.CommData;
import com.ydy258.ydy.entity.Region;
import com.ydy258.ydy.entity.RetData;

public interface ICommDataService extends IBaseService {
	public List<CommData> queryParent() throws Exception;
	
	
	public List<CommData> queryFirstParent() throws Exception;

	public List<CommData> queryByParentId(String parentId) throws Exception;
	
	public List<RetData> queryDictionary(String root) throws Exception;
	
	public List<Region> getProvinces();

	public List<Region> getCitiesByProvinceId(Long ProvinceId);

	public List<Region> getAreaByCityId(Long cityId);
	
	public String push2Truck(String jpushId, String msg, int authstep,boolean isAuth) ;
	
	public String push2Consignor(String jpushId, String msg, int authstep,boolean isAuth);
	
	/**
	 * 发送运单信息给符合条件的司机
	 */
	public String push2Truck(String jpushId, String msg, int authstep,boolean isAuth, int mobileType);
	
	/**
	 * 推送报价信息给货主
	 * @param jpushId
	 * @param msg
	 * @param billId
	 * @return
	 */
	public String push2Consignor(String jpushId, String msg, int authStep,boolean isAuth, int mobileType);
	
	public String push2Consignor(String jpushId, String msg,Long insureId,int insureType, boolean success, int mobileType);
	public String push2Truck(String jpushId, String msg,Long insureId,int insureType, boolean success, int mobileType);
	
	public String push2Truck(String jpushId, String msg,Long balanceId, int mobileType);
	
	public String push2Truck(String jpushId, String msg, int mobileType);
	
	/**
	 * 推送报价信息给货主
	 * @param jpushId
	 * @param msg
	 * @param billId
	 * @return
	 */
	public String push2Consignor(String jpushId, String msg, int mobileType) ;
	
	/**
	 * 推送报价信息给货主
	 * @param jpushId
	 * @param msg
	 * @param billId
	 * @return
	 */
	public String push2Consignor(String jpushId, String msg,Long balanceId, int mobileType);
}