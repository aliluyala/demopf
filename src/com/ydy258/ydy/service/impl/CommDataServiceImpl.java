package com.ydy258.ydy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.ydy258.ydy.dao.ICommDataDao;
import com.ydy258.ydy.entity.CommData;
import com.ydy258.ydy.entity.Region;
import com.ydy258.ydy.entity.RetData;
import com.ydy258.ydy.service.ICommDataService;


@Service("commDataService")
public class CommDataServiceImpl extends IBaseServiceImpl implements ICommDataService {
	
	private final String TRUCK_MASTER_SECRET = "e4f557ac2ae557baad2174d9";
	private final String TRUCK_APPKEY = "658dbc0c8cc16cb132cabda0";
	private final String CONSIGNOR_MASTER_SECRET = "9062a451631d1b0413c2832b";
	private final String CONSIGNOR_APPKEY = "f54b48b451aff6ce731ed68f";
	
	private final String TITLE = "您有新的信息";	
	
	@Autowired
	private ICommDataDao commDataDao;
	
	public List<RetData> queryDictionary(String root) throws Exception{
		List<RetData> rds = new ArrayList<RetData>();
		String sql = " select dd.* from t_comm_data dd where root='"+root+"'";
		List<CommData> dictionaryList1 = commDataDao.loadBySQL(sql, null, CommData.class);
		if(dictionaryList1==null||dictionaryList1.size()>0){
			CommData dd = dictionaryList1.get(0);
			sql = " select dd.* from t_comm_data dd where parent_id="+dd.getId();
			List<CommData> dictionaryList2 =  commDataDao.loadBySQL(sql, null, CommData.class);
			for(CommData d:dictionaryList2){
				RetData rd = new RetData();
				rd.setId(d.getId()+"");
				rd.setName(d.getName());
				sql = " select dd.* from t_comm_data dd where parent_id="+d.getId();
				List<CommData> dictionaryList3 = commDataDao.loadBySQL(sql, null, CommData.class);
				List<RetData> sons = new ArrayList<RetData>();
				for(CommData dl:dictionaryList3){
					RetData son = new RetData();
					son.setId(dl.getId()+"");
					son.setName(dl.getName());
					sons.add(son);
				}
				rd.setSons(sons);
			
				rds.add(rd);
			}
		}
		return rds;
	}

	@Override
	public List<CommData> queryParent() throws Exception {
		// TODO Auto-generated method stub
		return commDataDao.queryParent();
	}

	@Override
	public List<CommData> queryFirstParent() throws Exception {
		// TODO Auto-generated method stub
		return commDataDao.queryFirstParent();
	}

	@Override
	public List<CommData> queryByParentId(String parentId) throws Exception {
		// TODO Auto-generated method stub
		return commDataDao.queryByParentId(parentId);
	}
	
	
	public List<Region> getProvinces() {
		return commDataDao.getProvinces();
	}

	@Override
	public List<Region> getCitiesByProvinceId(Long ProvinceId) {
		// TODO Auto-generated method stub
		return commDataDao.getCitiesByProvinceId(ProvinceId);
	}

	@Override
	public List<Region> getAreaByCityId(Long cityId) {
		return commDataDao.getAreaByCityId(cityId);
	}
	
	public static void main(String[] args){
		CommDataServiceImpl impl = new CommDataServiceImpl();
		//impl.push2Truck("04082b59964", "调试用的", "1");
	}
	
	/**
	 * 发送运单信息给符合条件的司机
	 */
	public String push2Truck(String jpushId, String msg, int authstep,boolean isAuth) {
		JPushClient jpushClient = new JPushClient(TRUCK_MASTER_SECRET, TRUCK_APPKEY, 3);
		try {
			PushPayload payload = buildPushObjectWithExtra(jpushId, msg,authstep,isAuth);
			PushResult result = jpushClient.sendPush(payload);
			if(result.isResultOK())
				return new Long(result.msg_id).toString();
			else
				System.out.println(result.toString());
		}
		catch (Exception e) {
            // Connection error, should retry later
            e.printStackTrace();
		}  
		return null;
	}
	
	/**
	 * 推送报价信息给货主
	 * @param jpushId
	 * @param msg
	 * @param billId
	 * @return
	 */
	public String push2Consignor(String jpushId, String msg, int authStep,boolean isAuth) {
		JPushClient jpushClient = new JPushClient(CONSIGNOR_MASTER_SECRET, CONSIGNOR_APPKEY, 3);
		try {
			PushPayload payload = buildPushObjectWithExtra(jpushId, msg, authStep,isAuth);
			PushResult result = jpushClient.sendPush(payload);
			if(result.isResultOK())
				return new Long(result.msg_id).toString();
			else
				System.out.println(result.toString());
		}
		catch (Exception e) {
            e.printStackTrace();
		}  
		return null;
	}
	
	
	/**
	 * 发送运单信息给符合条件的司机
	 */
	public String push2Truck(String jpushId, String msg, int authstep,boolean isAuth, int mobileType) {
		JPushClient jpushClient = new JPushClient(TRUCK_MASTER_SECRET, TRUCK_APPKEY, 3);
		try {
			PushPayload payload = buildPushObjectWithExtra(jpushId, msg,authstep,isAuth,mobileType);
			PushResult result = jpushClient.sendPush(payload);
			if(result.isResultOK())
				return new Long(result.msg_id).toString();
			else
				System.out.println(result.toString());
		}
		catch (Exception e) {
            // Connection error, should retry later
            e.printStackTrace();
		}  
		return null;
	}
	
	/**
	 * 推送报价信息给货主
	 * @param jpushId
	 * @param msg
	 * @param billId
	 * @return
	 */
	public String push2Consignor(String jpushId, String msg, int authStep,boolean isAuth, int mobileType) {
		JPushClient jpushClient = new JPushClient(CONSIGNOR_MASTER_SECRET, CONSIGNOR_APPKEY, 3);
		try {
			PushPayload payload = buildPushObjectWithExtra(jpushId, msg, authStep,isAuth,mobileType);
			PushResult result = jpushClient.sendPush(payload);
			if(result.isResultOK())
				return new Long(result.msg_id).toString();
			else
				System.out.println(result.toString());
		}
		catch (Exception e) {
            e.printStackTrace();
		}  
		return null;
	}
	
	
	/**
	 * 发送运单信息给符合条件的司机
	 */
	public String push2Truck(String jpushId, String msg,Long balanceId, int mobileType) {
		JPushClient jpushClient = new JPushClient(TRUCK_MASTER_SECRET, TRUCK_APPKEY, 3);
		try {
			PushPayload payload = buildPushObjectWithExtra(jpushId, msg,balanceId,mobileType);
			PushResult result = jpushClient.sendPush(payload);
			if(result.isResultOK())
				return new Long(result.msg_id).toString();
			else
				System.out.println(result.toString());
		}
		catch (Exception e) {
            // Connection error, should retry later
            e.printStackTrace();
		}  
		return null;
	}
	
	/**
	 * 推送报价信息给货主
	 * @param jpushId
	 * @param msg
	 * @param billId
	 * @return
	 */
	public String push2Consignor(String jpushId, String msg,Long balanceId, int mobileType) {
		JPushClient jpushClient = new JPushClient(CONSIGNOR_MASTER_SECRET, CONSIGNOR_APPKEY, 3);
		try {
			PushPayload payload = buildPushObjectWithExtra(jpushId, msg,balanceId,mobileType);
			PushResult result = jpushClient.sendPush(payload);
			if(result.isResultOK())
				return new Long(result.msg_id).toString();
			else
				System.out.println(result.toString());
		}
		catch (Exception e) {
            e.printStackTrace();
		}  
		return null;
	}
	
	
	/**
	 * 发送运单信息给符合条件的司机
	 */
	public String push2Truck(String jpushId, String msg, int mobileType) {
		JPushClient jpushClient = new JPushClient(TRUCK_MASTER_SECRET, TRUCK_APPKEY, 3);
		try {
			PushPayload payload = buildPushObjectWithExtra(jpushId, msg,mobileType);
			PushResult result = jpushClient.sendPush(payload);
			if(result.isResultOK())
				return new Long(result.msg_id).toString();
			else
				System.out.println(result.toString());
		}
		catch (Exception e) {
            // Connection error, should retry later
            e.printStackTrace();
		}  
		return null;
	}
	
	/**
	 * 推送报价信息给货主
	 * @param jpushId
	 * @param msg
	 * @param billId
	 * @return
	 */
	public String push2Consignor(String jpushId, String msg, int mobileType) {
		JPushClient jpushClient = new JPushClient(CONSIGNOR_MASTER_SECRET, CONSIGNOR_APPKEY, 3);
		try {
			PushPayload payload = buildPushObjectWithExtra(jpushId, msg,mobileType);
			PushResult result = jpushClient.sendPush(payload);
			if(result.isResultOK())
				return new Long(result.msg_id).toString();
			else
				System.out.println(result.toString());
		}
		catch (Exception e) {
            e.printStackTrace();
		}  
		return null;
	}
	
	/**
	 * 保险推送
	 * @param jpushId
	 * @param msg
	 * @param billId
	 * @return
	 */
	public String push2Truck(String jpushId, String msg,Long insureId,int insureType, boolean success,int mobileType) {
		JPushClient jpushClient = new JPushClient(TRUCK_MASTER_SECRET, TRUCK_APPKEY, 3);
		try {
			PushPayload payload = buildPushObjectWithExtra(jpushId, msg, insureId,insureType,success,mobileType);
			PushResult result = jpushClient.sendPush(payload);
			if(result.isResultOK())
				return new Long(result.msg_id).toString();
			else
				System.out.println(result.toString());
		}
		catch (Exception e) {
            e.printStackTrace();
		}  
		return null;
	}
	
	
	/**
	 * 保险推送
	 * @param jpushId
	 * @param msg
	 * @param billId
	 * @return
	 */
	public String push2Consignor(String jpushId, String msg,Long insureId,int insureType,  boolean success,int mobileType) {
		JPushClient jpushClient = new JPushClient(CONSIGNOR_MASTER_SECRET, CONSIGNOR_APPKEY, 3);
		try {
			PushPayload payload = buildPushObjectWithExtra(jpushId, msg, insureId,insureType,success,mobileType);
			PushResult result = jpushClient.sendPush(payload);
			if(result.isResultOK())
				return new Long(result.msg_id).toString();
			else
				System.out.println(result.toString());
		}
		catch (Exception e) {
            e.printStackTrace();
		}  
		return null;
	}
	
	//收支推送
		private PushPayload buildPushObjectWithExtra(String jpushId, String msg ,int mobileType) {
			HashMap<String, String> extra = new HashMap<String, String>();
			extra.put("action","cardRhJpush");	
			PushPayload pushPayload = null;
			if(mobileType == 1) {
				pushPayload = PushPayload.newBuilder()
						.setPlatform(Platform.ios())
						.setAudience(Audience.newBuilder()
								.addAudienceTarget(AudienceTarget.registrationId(jpushId))
								.build())							
								.setNotification(									
										Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
												.setAlert(TITLE)
												.setSound("happy.caf")											
												.autoBadge()
												.setContentAvailable(true)
												.addExtras(extra).build()).build())
								.setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
				//苹果系统
								/*pushPayload = PushPayload.newBuilder()
						.setPlatform(Platform.ios())
						.setAudience(Audience.newBuilder()
								.addAudienceTarget(AudienceTarget.registrationId(jpushId))
								.build())
								.setNotification(									
										Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
												.setAlert(TITLE)
												.setSound("happy.caf")
												//.setBadge(1)
												.autoBadge()
												.addExtras(extra).build()).build()).build();*/					
			}
			else {
				//android系统
				pushPayload = PushPayload.newBuilder()
						.setPlatform(Platform.android())
						.setAudience(Audience.newBuilder()
								.addAudienceTarget(AudienceTarget.registrationId(jpushId))
								.build())
								.setNotification(Notification.android(TITLE, msg, extra))						
						.build();
			}		
			return pushPayload; 
		}
	
	private PushPayload buildPushObjectWithExtra(String jpushId, String msg,int authStep,boolean isAuth) {
		HashMap<String, String> extra = new HashMap<String, String>();
		/*Map object = new HashMap();
		object.put("authStep", authStep);
		object.put("isAuth", isAuth);*/
		System.out.println(isAuth+"&"+authStep);
		extra.put("action",isAuth+"&"+authStep);
		return PushPayload.newBuilder()
				.setPlatform(Platform.android_ios())
				.setAudience(Audience.newBuilder()
						.addAudienceTarget(AudienceTarget.registrationId(jpushId))
						.build())
				.setNotification(Notification.android(TITLE, msg, extra))						
				.build();
	}
	//用户审核推送
	private PushPayload buildPushObjectWithExtra(String jpushId, String msg ,int authStep,boolean isAuth, int mobileType) {
		HashMap<String, String> extra = new HashMap<String, String>();
		extra.put("action",isAuth+"&"+authStep);	
		
		PushPayload pushPayload = null;
		if(mobileType == 1) {
			pushPayload = PushPayload.newBuilder()
					.setPlatform(Platform.ios())
					.setAudience(Audience.newBuilder()
							.addAudienceTarget(AudienceTarget.registrationId(jpushId))
							.build())							
							.setNotification(									
									Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
											.setAlert(TITLE)
											.setSound("happy.caf")											
											.autoBadge()
											.setContentAvailable(true)
											.addExtras(extra).build()).build())
							.setOptions(Options.newBuilder().setApnsProduction(true).build()).build();	
			//苹果系统
							/*pushPayload = PushPayload.newBuilder()
					.setPlatform(Platform.ios())
					.setAudience(Audience.newBuilder()
							.addAudienceTarget(AudienceTarget.registrationId(jpushId))
							.build())
							.setNotification(									
									Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
											.setAlert(TITLE)
											.setSound("happy.caf")
											//.setBadge(1)
											.autoBadge()
											.addExtras(extra).build()).build()).build();	*/			
		}
		else {
			//android系统
			pushPayload = PushPayload.newBuilder()
					.setPlatform(Platform.android())
					.setAudience(Audience.newBuilder()
							.addAudienceTarget(AudienceTarget.registrationId(jpushId))
							.build())
							.setNotification(Notification.android(TITLE, msg, extra))						
					.build();
		}		
		return pushPayload; 
	}
	//保险推送
	private PushPayload buildPushObjectWithExtra(String jpushId, String msg ,Long insureId,int insureType, boolean success,int mobileType) {
		HashMap<String, String> extra = new HashMap<String, String>();
		extra.put("action","insure");	
		extra.put("insureId",insureId+"");	
		extra.put("insureType",insureType+"");	
		extra.put("success",success+"");	
		PushPayload pushPayload = null;
		if(mobileType == 1) {
			pushPayload = PushPayload.newBuilder()
					.setPlatform(Platform.ios())
					.setAudience(Audience.newBuilder()
							.addAudienceTarget(AudienceTarget.registrationId(jpushId))
							.build())							
							.setNotification(									
									Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
											.setAlert(TITLE)
											.setSound("happy.caf")											
											.autoBadge()
											.setContentAvailable(true)
											.addExtras(extra).build()).build())
							.setOptions(Options.newBuilder().setApnsProduction(true).build()).build();	
			//苹果系统
							/*pushPayload = PushPayload.newBuilder()
					.setPlatform(Platform.ios())
					.setAudience(Audience.newBuilder()
							.addAudienceTarget(AudienceTarget.registrationId(jpushId))
							.build())
							.setNotification(									
									Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
											.setAlert(TITLE)
											.setSound("happy.caf")
											//.setBadge(1)
											.autoBadge()
											.addExtras(extra).build()).build()).build();		*/		
		}
		else {
			//android系统
			pushPayload = PushPayload.newBuilder()
					.setPlatform(Platform.android())
					.setAudience(Audience.newBuilder()
							.addAudienceTarget(AudienceTarget.registrationId(jpushId))
							.build())
							.setNotification(Notification.android(TITLE, msg, extra))						
					.build();
		}		
		return pushPayload; 
	}

	//收支推送
	private PushPayload buildPushObjectWithExtra(String jpushId, String msg ,Long balanceId,  int mobileType) {
		HashMap<String, String> extra = new HashMap<String, String>();
		extra.put("action","balance");	
		extra.put("balanceId",balanceId+"");	
		PushPayload pushPayload = null;
		if(mobileType == 1) {
			pushPayload = PushPayload.newBuilder()
					.setPlatform(Platform.ios())
					.setAudience(Audience.newBuilder()
							.addAudienceTarget(AudienceTarget.registrationId(jpushId))
							.build())							
							.setNotification(									
									Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
											.setAlert(TITLE)
											.setSound("happy.caf")											
											.autoBadge()
											.setContentAvailable(true)
											.addExtras(extra).build()).build())
							.setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
			//苹果系统
							/*pushPayload = PushPayload.newBuilder()
					.setPlatform(Platform.ios())
					.setAudience(Audience.newBuilder()
							.addAudienceTarget(AudienceTarget.registrationId(jpushId))
							.build())
							.setNotification(									
									Notification.newBuilder().addPlatformNotification(IosNotification.newBuilder()
											.setAlert(TITLE)
											.setSound("happy.caf")
											//.setBadge(1)
											.autoBadge()
											.addExtras(extra).build()).build()).build();*/					
		}
		else {
			//android系统
			pushPayload = PushPayload.newBuilder()
					.setPlatform(Platform.android())
					.setAudience(Audience.newBuilder()
							.addAudienceTarget(AudienceTarget.registrationId(jpushId))
							.build())
							.setNotification(Notification.android(TITLE, msg, extra))						
					.build();
		}		
		return pushPayload; 
	}
}
