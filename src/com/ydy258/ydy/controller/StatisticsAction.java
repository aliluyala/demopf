

package com.ydy258.ydy.controller;


import java.io.IOException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import redis.clients.jedis.JedisPool;

import com.itextpdf.text.log.SysoCounter;
import com.vividsolutions.jts.geom.Geometry;
import com.ydy258.ydy.Constants;
import com.ydy258.ydy.entity.ProxyStatements;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.entity.TruckLocation;
import com.ydy258.ydy.entity.UserRechargeRequest;
import com.ydy258.ydy.entity.WaybillInfo;
import com.ydy258.ydy.service.IStatisticsService;
import com.ydy258.ydy.service.ITruckLbsService;
import com.ydy258.ydy.util.BaseException;
import com.ydy258.ydy.util.DateUtil;
import com.ydy258.ydy.util.JSONHelper;
import com.ydy258.ydy.util.Page;

@Controller
@RequestMapping("/sys/statistics/")
public class StatisticsAction extends BaseFacade  {
	
	static Map<String,String> provinces = new HashMap<String,String>();
	
	static{
		provinces.put("安徽", "合肥|32.02|116.17");
		provinces.put("北京", "北京市|40.05|115.24");
		provinces.put("福建", "福州|26.55|118.18");
		provinces.put("甘肃", "兰州|36.54|102.51");
		provinces.put("海南", "海口 |20.52|109.20");
		provinces.put("广东", "广州|23.58|112.14");
		provinces.put("广西", "南宁|22.98|107.19");
		provinces.put("贵州", "贵阳|26.85|105.42");
		provinces.put("河北", "石家庄|38.52|113.30");
		provinces.put("河南", "郑州|34.96|112.40");
		provinces.put("黑龙", "哈尔滨|45.94|125.36");
		provinces.put("湖北", "武汉|30.85|113.17");
		provinces.put("湖南", "长沙|28.62|111.59");
		provinces.put("江苏", "南京|32.53|117.46");
		provinces.put("江西", "南昌|28.90|114.55");
		provinces.put("辽宁", "沈阳|41.98|122.25");
		provinces.put("内蒙", "呼和浩特|40.98|110.41");
		provinces.put("宁夏", "银川|38.77|105.16");
		provinces.put("青海", "西宁|36.88|100.48");
		provinces.put("山东", "济南|36.90|116.00");
		provinces.put("山西", "太原|38.04|111.33");
		provinces.put("上海", "上海市|31.64|120.29");
		provinces.put("陕西", "西安|34.67|107.57");
		provinces.put("四川", "成都|30.90|103.04");
		provinces.put("天津", "天津市|39.52|116.12");
		provinces.put("西藏", "拉萨|29.89|90.08");
		provinces.put("新疆", "乌鲁木齐|43.95|86.36");
		provinces.put("云南", "昆明|25.54|101.42");
		provinces.put("浙江", "杭州|30.66|119.10");
		provinces.put("重庆", "重庆市|29.85|105.33");
		provinces.put("吉林", "长春|44.04|124.19");
	}
	@Autowired
	private JedisPool jedisPool;
	
	@Autowired
	private IStatisticsService statisticsService;
	@Autowired
	private ITruckLbsService truckLbsService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newtruckUserStatistics", method = {RequestMethod.GET, RequestMethod.POST})
	public String newtruckUserStatistics(HttpServletRequest request, HttpServletResponse response,String days) {
			try {
				Map<String,String> args = new HashMap<String,String>();
				args.put("days", days);
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("A0001".equals(user.getDepartment())){
					args.put("proxyUserId", user.getDepartmentId()+"");
				}
				List<Map> list = statisticsService.newtruckUserStatistics(args);
				Map<String,BigInteger> map = addMap(list);
				Date today = new Date();
				int countuser = 0;
				List<Map> retList = new ArrayList<Map>();
				for(int i=Integer.valueOf(days)-1;i>=0;i--){
					Map ret = new HashMap();
					String date = getNextDay(today,-i);
					BigInteger count = map.get(date);
					if(count!=null){
						ret.put("date", date);
						ret.put("count", count);
						retList.add(ret);
						countuser+=count.intValue();
					}else{
						ret.put("date", date);
						ret.put("count", 0);
						retList.add(ret);
					}
				}
				JSONHelper.returnInfo(JSONHelper.list2json(retList,countuser+""));
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newconsignorUserStatistics", method = {RequestMethod.GET, RequestMethod.POST})
	public String newconsignorUserStatistics(HttpServletRequest request, HttpServletResponse response,String days) {
			try {
				Map<String,String> args = new HashMap<String,String>();
				args.put("days", days);
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("A0001".equals(user.getDepartment())){
					args.put("proxyUserId", user.getDepartmentId()+"");
				}
				List<Map> list = statisticsService.newConsignorUserStatistics(args);
				Map<String,BigInteger> map = addMap(list);
				Date today = new Date();
				int countuser = 0;
				List<Map> retList = new ArrayList<Map>();
				for(int i=Integer.valueOf(days)-1;i>=0;i--){
					Map ret = new HashMap();
					String date = getNextDay(today,-i);
					BigInteger count = map.get(date);
					if(count!=null){
						ret.put("date", date);
						ret.put("count", count);
						retList.add(ret);
						countuser+=count.intValue();
					}else{
						ret.put("date", date);
						ret.put("count", 0);
						retList.add(ret);
					}
				}
				JSONHelper.returnInfo(JSONHelper.list2json(retList,countuser+""));
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newtruckUserStatisticsByMonth", method = {RequestMethod.GET, RequestMethod.POST})
	public String newtruckUserStatisticsByMonth(HttpServletRequest request, HttpServletResponse response,String month) {
			try {
				Map<String,String> args = new HashMap<String,String>();
				args.put("month", month);
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("A0001".equals(user.getDepartment())){
					args.put("proxyUserId", user.getDepartmentId()+"");
				}
				List<Map> list = statisticsService.newtruckUserStatisticsByMonth(args);
				Map<String,BigInteger> map = addMap(list);
				int countuser = 0;
				List<Map> retList = new ArrayList<Map>();
				List<String> months = getMonth(month);
				for(int i=0;i<months.size();i++){
					Map ret = new HashMap();
					BigInteger count = map.get(months.get(i));
					if(count!=null){
						ret.put("month", months.get(i));
						ret.put("count", count);
						retList.add(ret);
						countuser+=count.intValue();
					}else{
						ret.put("month", months.get(i));
						ret.put("count", 0);
						retList.add(ret);
					}
				}
				JSONHelper.returnInfo(JSONHelper.list2json(retList,countuser+""));
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newconsignorUserStatisticsByMonth", method = {RequestMethod.GET, RequestMethod.POST})
	public String newconsignorUserStatisticsByMonth(HttpServletRequest request, HttpServletResponse response,String month,String usreType) {
			try {
				Map<String,String> args = new HashMap<String,String>();
				args.put("month", month);
				args.put("userType", usreType);
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("A0001".equals(user.getDepartment())){
					args.put("proxyUserId", user.getDepartmentId()+"");
				}
				List<Map> list = statisticsService.newConsignorUserStatisticsByMonth(args);
				Map<String,BigInteger> map = addMap(list);
				int countuser = 0;
				List<Map> retList = new ArrayList<Map>();
				List<String> months = getMonth(month);
				for(int i=0;i<months.size();i++){
					Map ret = new HashMap();
					BigInteger count = map.get(months.get(i));
					if(count!=null){
						ret.put("month", months.get(i));
						ret.put("count", count);
						retList.add(ret);
						countuser+=count.intValue();
					}else{
						ret.put("month", months.get(i));
						ret.put("count", 0);
						retList.add(ret);
					}
				}
				JSONHelper.returnInfo(JSONHelper.list2json(retList,countuser+""));
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/newMallUserStatisticsByMonth", method = {RequestMethod.GET, RequestMethod.POST})
	public String newMallUserStatisticsByMonth(HttpServletRequest request, HttpServletResponse response,String month) {
			try {
				Map<String,String> args = new HashMap<String,String>();
				args.put("month", month);
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("A0001".equals(user.getDepartment())){
					args.put("proxyUserId", user.getDepartmentId()+"");
				}
				List<Map> list = statisticsService.newMallUserStatisticsByMonth(args);
				Map<String,BigInteger> map = addMap(list);
				int countuser = 0;
				List<Map> retList = new ArrayList<Map>();
				List<String> months = getMonth(month);
				for(int i=0;i<months.size();i++){
					Map ret = new HashMap();
					BigInteger count = map.get(months.get(i));
					if(count!=null&&count.intValue()<500){   //count.intValue()<500把动加进来除掉
						ret.put("month", months.get(i));
						ret.put("count", count);
						retList.add(ret);
						countuser+=count.intValue();
					}else{
						ret.put("month", months.get(i));
						ret.put("count", 0);
						retList.add(ret);
					}
				}
				System.out.println(retList);
				JSONHelper.returnInfo(JSONHelper.list2json(retList,countuser+""));
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	
	    public static Map<String,BigInteger> addMap(List<Map> list){
	    	Map map = new HashMap();
	    	for(Map m:list){
	    		String created = (String)m.get("created");
	    		BigInteger countuser = (BigInteger)m.get("countuser");
	    		map.put(created, countuser);
	    	}
	    	return map;
	    }
	    
	    public static String getNextDay(Date date,int datec) {		
	    	Calendar calendar = Calendar.getInstance();
	    	calendar.setTime(date);
	    	calendar.add(Calendar.DAY_OF_MONTH, datec);		
	    	date = calendar.getTime();		
	    	return DateUtil.getStrYMDByDate(date);	
	    }
	    public static String getNextMonth(Date date,int datec) {		
	    	Calendar calendar = Calendar.getInstance();
	    	calendar.setTime(date);
	    	calendar.add(Calendar.MONTH, datec);		
	    	date = calendar.getTime();		
	    	return DateUtil.getStrYMByDate(date);	
	    }
	    
	    //得到指定时间开始到现的每一个月分集合
	    public static List<String> getMonth(String  date) {
	    	List<String> months = new ArrayList<String>();
	    	Calendar calendar = Calendar.getInstance();
	    	calendar.setTime(DateUtil.getDateByStr(date));
	    	while(calendar.getTime().before(new Date())){
	    		months.add(DateUtil.getStrYMByDate(calendar.getTime()));
	    		calendar.add(Calendar.MONTH, 1);
	    	}
	    	//System.out.println(months);
	    	return months;	
	    }
	    
	    
	    public static void main(String[] args){
	    	System.out.println(getMonth("2017-01").toString()); 
	    }
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/companyStatements", method = {RequestMethod.GET, RequestMethod.POST})
		public String compantyStatements(HttpServletRequest request, HttpServletResponse response,String orderNo,String startTime,String endTime,int currentPage) {
			try {
				args = new HashMap<String,String>();
				if(!StringUtils.isBlank(orderNo)){
					orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
					args.put("orderNo", orderNo);
				}
				if(!StringUtils.isBlank(startTime)){
					args.put("startTime", startTime);
				}
				if(!StringUtils.isBlank(endTime)){
					endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
					args.put("endTime", endTime);
				}
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("A0001".equals(user.getDepartment())){
					args.put("proxyUserId", user.getDepartmentId()+"");
				}
				Page page = statisticsService.companyStatements(args,currentPage,pageSize);
				List<Map> list = page.getResults();
				List<Map> retlist = new ArrayList<Map>();
				for(Map sm:list){
					Map ret = new HashMap();
					ret.put("id", sm.get("id"));
					ret.put("point", sm.get("point"));
					ret.put("orderNo", sm.get("orderno"));
					ret.put("userType", (Short)sm.get("usertype")==Constants.UserType.TruckUser.getType()?"车主":"货主");
					ret.put("accountId", sm.get("userid"));
					ret.put("pay", sm.get("pay"));
					ret.put("remark", sm.get("remark"));
					ret.put("createdDate", DateUtil.getStrYMDHMSByDate((Date)sm.get("date")));
					retlist.add(ret);
				}
				JSONHelper.returnInfo(JSONHelper.list2json(retlist,page.getTotalCount()+""));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	    
/*	    *//**
		 * 提交空车信息
		 * @param request
		 * @param response
		 * @throws IOException
		 *//*
		@RequestMapping(value="/commitTruckInfo", method = {RequestMethod.GET, RequestMethod.POST})
		public void commitTruckInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
			try {
				List<TruckLocation> list =  truckLbsService.getAllTruck();
				for(TruckLocation t:list){
					String geohash = GeoHash.encode(t.get, longitude).substring(0, AppConst.GEOHASH_LENGTH);
					TruckLocation loc = lbsService.getTruckInfoByTruckId(truckId);
					if(loc != null) {
						//已存在，更新相关信息
						// 记录存在，直接更新				
						Geometry g1 = new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
						loc.setSendLocTime(new Date());
						loc.setOrigin(origin);
						loc.setDestination(destination);
						loc.setLoadStatus(loadStatus);
						loc.setMemo(memo);
						loc.setAddress(currPos);
						loc.setLocation(g1);
						loc.setGeohash(geohash);
						loc.setRemainLoad(remainLoad);
						lbsService.update(loc);
					}
					//更新redis缓存中的货车位置				
					String key = loc.getId() + ":" + loc.getTruckId();
					Jedis jedis = jedisPool.getResource();
					String oldGeoHash = jedis.get(key);
					if(oldGeoHash == null || oldGeoHash.length() == 0) {
						//新车，直接加入缓存
						jedis.set(key, geohash);
						jedis.sadd(geohash, loc.getId().toString());
					}
					else {
						if(!oldGeoHash.equals(geohash)) {
							//两次的geohash值不同，说明车辆已经移动到了另一个geohash区域
							jedis.set(key, geohash);
							jedis.smove(oldGeoHash, geohash, loc.getId().toString());
						}
						else {
							jedis.sadd(geohash, loc.getId().toString());
						}
					}
					
				}
				jedis.close();
			}
			catch(Exception e) {
				logger.error(e.getMessage(), e);
			}

		}	*/
		
	    /*@SuppressWarnings("unchecked")
		@RequestMapping(value = "/getAllTruck", method = {RequestMethod.GET, RequestMethod.POST})
		public String getAllTruck(HttpServletRequest request, HttpServletResponse response, Double longitude, Double latitude) {
			try {
				args = new HashMap<String,String>();
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("A0001".equals(user.getDepartment())){
					args.put("proxyUserId", user.getDepartmentId()+"");
				}
				List<Object[]> list =  (List<Object[]>) truckLbsService.getNearTruckList(null, null, null, longitude, latitude, 1000000, 1000);
				List<Map> retlist = new ArrayList<Map>();
				if(list==null){
					list = new ArrayList<Object[]>();
				}
				for(Object[] t:list){
					TruckLocation tl = (TruckLocation)t[0];
					Map ret = new HashMap();
					ret.put("ll", tl.getLocation().getCoordinate().x);
					ret.put("ln",  tl.getLocation().getCoordinate().y);
					retlist.add(ret);
				}
				JSONHelper.returnInfo(JSONHelper.list2json(retlist));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }*/
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/getAllTruck", method = {RequestMethod.GET, RequestMethod.POST})
		public String getAllTruck(HttpServletRequest request, HttpServletResponse response, Double longitude, Double latitude) {
			try {
				args = new HashMap<String,String>();
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("A0001".equals(user.getDepartment())){
					args.put("proxyUserId", user.getDepartmentId()+"");
				}
				List<TruckLocation> list =  (List<TruckLocation>) truckLbsService.getAllTruck();
				List<Map> retlist = new ArrayList<Map>();
				if(list==null){
					list = new ArrayList<TruckLocation>();
				}
				for(TruckLocation t:list){
					Map ret = new HashMap();
					ret.put("ll", t.getLocation().getCoordinate().x);
					ret.put("ln",  t.getLocation().getCoordinate().y);
					retlist.add(ret);
				}
				JSONHelper.returnInfo(JSONHelper.list2json(retlist));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/getAllProvinceTruck", method = {RequestMethod.GET, RequestMethod.POST})
		public String getAllProvinceTruck(HttpServletRequest request, HttpServletResponse response, Double longitude, Double latitude) {
			try {
				args = new HashMap<String,String>();
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("A0001".equals(user.getDepartment())){
					args.put("proxyUserId", user.getDepartmentId()+"");
				}
				List<Map> list =   truckLbsService.getAllProvinceTruck();
				List<Map> retlist = new ArrayList<Map>();
				if(list==null){
					list = new ArrayList<Map>();
				}
				for(Map<String,Object> t:list){
					if(StringUtils.isBlank((String)t.get("pri"))){
						continue;
					}
					String p = provinces.get(t.get("pri"));
					
					if(p==null){
						continue;
					}
					String ll = p.split("\\|")[2].trim();
					String ln = p.split("\\|")[1].trim();
					
					Map ret = new HashMap();
					ret.put("province", t.get("pri"));
					ret.put("countt",  ((BigInteger)t.get("countt")).intValue()*7);
					ret.put("ll", ll);
					ret.put("ln", ln);
					retlist.add(ret);
				}
				JSONHelper.returnInfo(JSONHelper.list2json(retlist));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	    

	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/proxyStatements", method = {RequestMethod.GET, RequestMethod.POST})
		public String proxyStatements(HttpServletRequest request, HttpServletResponse response,String tradeType,String orderNo,String startTime,String endTime,int currentPage) {
			try {
				args = new HashMap<String,String>();
				if(!StringUtils.isBlank(orderNo)){
					orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
					args.put("orderNo", orderNo);
				}
				if(!StringUtils.isBlank(tradeType)){
					tradeType = URLDecoder.decode(tradeType,"UTF-8"); 
					args.put("type", tradeType);
				}
				if(!StringUtils.isBlank(startTime)){
					args.put("startTime", startTime);
				}
				if(!StringUtils.isBlank(endTime)){
					endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
					args.put("endTime", endTime);
				}
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("A0001".equals(user.getDepartment())){
					args.put("proxyUserId", user.getDepartmentId()+"");
				}
				//  ts.id id, ts.account_id userId,order_no orderNo,pay pay,remark remark,user_type userType,pay_points point,ts.created_date date,ts.cost costpay 
				Page page = statisticsService.proxyStatements(args,currentPage,pageSize);
				List<ProxyStatements> list = page.getResults();
				List<Map> retlist = new ArrayList<Map>();
				for(ProxyStatements sm:list){
					Map ret = new HashMap();
					ret.put("id", sm.getId());
					ret.put("sysUserName", sm.getSysUserName());
					ret.put("orderNo", sm.getOrderNo());
					ret.put("type",sm.getType()==1?"增加服务时间":"代理充值");
					ret.put("proxyUserName",sm.getProxyUserName());
					ret.put("memberName", sm.getMemberName());
					ret.put("isSuccess", sm.isSuccess());
					ret.put("userType", sm.getUserType()==1?"货主":"车主");
					ret.put("createdDate", DateUtil.getStrYMDHMSByDate((Date)sm.getCreatedDate()));
					if(sm.getType()==1){//如果是增加服务时间
						ret.put("count", sm.getYears()+"年");
					}else{
						ret.put("count", convert((Double)sm.getBalance())+"元");
					}
					retlist.add(ret);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("totalCount", page.getTotalCount()+"");
				map.put("root", retlist);
				Map otherInfo = page.getOtherMap();
				otherInfo.put("userType", user.getDepartment());
				map.put("message", JSONObject.fromObject(otherInfo).toString());
				JSONHelper.returnInfo(JSONObject.fromObject(map));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/balanceProxyStatements", method = {RequestMethod.GET, RequestMethod.POST})
		public String balanceProxyStatements(HttpServletRequest request, HttpServletResponse response,long[] tradeId) {
			try {
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("C0001".equals(user.getDepartment())||"A0001".equals(user.getDepartment())){
					JSONHelper.returnInfo(JSONHelper.successInfo(Constants.no_permission_code,Constants.no_permission_msg));
					return NONE;
				}
				statisticsService.balanceProxyStatements(tradeId);
				StringBuffer ids = new StringBuffer();
				for(long id:tradeId){
					ids.append(id+"|");
				}
				info("代理账单结算人："+user.getUserName()+" 结算时间："+DateUtil.getStrYMDHMSByDate(new Date())+" 结算id:"+ids);
				JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	    
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/userjyRechargeRequest", method = {RequestMethod.GET, RequestMethod.POST})
		public String userjyRechargeRequest(HttpServletRequest request, HttpServletResponse response,String orderNo,String startTime,String endTime,Boolean success,int currentPage) {
			try {
				args = new HashMap<String,String>();
				if(!StringUtils.isBlank(orderNo)){
					orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
					args.put("orderNo", orderNo);
				}
				if(success!=null){
					args.put("success", success+"");
				}
				args.put("type", "-2");
				if(!StringUtils.isBlank(startTime)){
					args.put("startTime", startTime);
				}
				if(!StringUtils.isBlank(endTime)){
					endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
					args.put("endTime", endTime);
				}
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if(!"B0001".equals(user.getDepartment())){
					JSONHelper.returnInfo(JSONHelper.successInfo(Constants.no_permission_code,Constants.no_permission_msg));
					return NONE;
				}
				//  ts.id id, ts.account_id userId,order_no orderNo,pay pay,remark remark,user_type userType,pay_points point,ts.created_date date,ts.cost costpay 
				Page page = statisticsService.userRechargeRequest(args, currentPage, pageSize);
				List<Map> list = page.getResults();
				List<Map> retlist = new ArrayList<Map>();
				for(Map sm:list){
					Map ret = new HashMap();
					ret.put("id", sm.get("id"));
					ret.put("realName", sm.get("driver_name"));
					ret.put("mobile", sm.get("mobile"));
					ret.put("plateNumber", sm.get("plate_number"));
					ret.put("orderNo", sm.get("order_no"));
					ret.put("rechargeMuch", sm.get("recharge_much"));
					ret.put("tradeType", sm.get("trade_type"));
					ret.put("createdDate", sm.get("created_date"));
					ret.put("payPoint", sm.get("pay_point"));
					ret.put("returnPoint", sm.get("return_point"));
					ret.put("specialBalance", sm.get("special_balance"));
					ret.put("rechargeTotal", sm.get("recharge_total"));
					ret.put("isSuccess", sm.get("is_success")==null?false:sm.get("is_success"));
					retlist.add(ret);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("totalCount", page.getTotalCount()+"");
				map.put("root", retlist);
				Map otherInfo = page.getOtherMap();
				otherInfo.put("userType", user.getDepartment());
				map.put("message", JSONObject.fromObject(otherInfo).toString());
				map.put("success", true);
				JSONHelper.returnInfo(JSONObject.fromObject(map));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/usergsRechargeRequest", method = {RequestMethod.GET, RequestMethod.POST})
		public String usergsRechargeRequest(HttpServletRequest request, HttpServletResponse response,Boolean success,String orderNo,String startTime,String endTime,int currentPage) {
			try {
				args = new HashMap<String,String>();
				if(!StringUtils.isBlank(orderNo)){
					orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
					args.put("orderNo", orderNo);
				}
				args.put("type", "-1");
				if(success!=null){
					args.put("success", success+"");
				}
				if(!StringUtils.isBlank(startTime)){
					args.put("startTime", startTime);
				}
				if(!StringUtils.isBlank(endTime)){
					endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
					args.put("endTime", endTime);
				}
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if(!"B0001".equals(user.getDepartment())){
					JSONHelper.returnInfo(JSONHelper.successInfo(Constants.no_permission_code,Constants.no_permission_msg));
					return NONE;
				}
				//  ts.id id, ts.account_id userId,order_no orderNo,pay pay,remark remark,user_type userType,pay_points point,ts.created_date date,ts.cost costpay 
				Page page = statisticsService.userRechargeRequest(args, currentPage, pageSize);
				List<Map> list = page.getResults();
				List<Map> retlist = new ArrayList<Map>();
				for(Map sm:list){
					Map ret = new HashMap();
					ret.put("id", sm.get("id"));
					ret.put("realName", sm.get("driver_name"));
					ret.put("mobile", sm.get("mobile"));
					ret.put("plateNumber", sm.get("plate_number"));
					ret.put("orderNo", sm.get("order_no"));
					ret.put("rechargeMuch", sm.get("recharge_much"));
					ret.put("tradeType", sm.get("trade_type"));
					ret.put("createdDate", sm.get("created_date"));
					ret.put("payPoint", sm.get("pay_point"));
					ret.put("returnPoint", sm.get("return_point"));
					ret.put("specialBalance", sm.get("pay_point"));
					ret.put("rechargeTotal", sm.get("recharge_total"));
					ret.put("isSuccess", sm.get("is_success")==null?false:sm.get("is_success"));
					retlist.add(ret);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("totalCount", page.getTotalCount()+"");
				map.put("root", retlist);
				Map otherInfo = page.getOtherMap();
				otherInfo.put("userType", user.getDepartment());
				map.put("message", JSONObject.fromObject(otherInfo).toString());
				map.put("success", true);
				JSONHelper.returnInfo(JSONObject.fromObject(map));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/balanceUserjyRechargeRequest", method = {RequestMethod.GET, RequestMethod.POST})
		public String balanceUserjyRechargeRequest(HttpServletRequest request, HttpServletResponse response,long tradeId,long points,double recharge) {
			try {
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				UserRechargeRequest ps = statisticsService.getById(UserRechargeRequest.class,tradeId);
				boolean success = false;
				if(ps.getIsSuccess()!=null){
					success = ps.getIsSuccess();
				}
				if(ps==null||success){
					throw new BaseException("ID出错！");
				}
				Truck truckInfo = statisticsService.getById(Truck.class,ps.getUserId());
				if(truckInfo==null){
					throw new BaseException("ID出错！");
				}
				double _recharge = 0.0;
				long _points = 0L;
				if(ps.getPayPoint()<=truckInfo.getPoints()){
					_recharge = ps.getRechargeMuch();
					_points = ps.getPayPoint();
				}else{
					_points = truckInfo.getPoints();
					_recharge = ps.getRechargeMuch();
				}
				if(_recharge!=recharge||_points!=points){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
					return NONE;
				}
				statisticsService.balanceUserjyRechargeRequest(tradeId, points, recharge);
				//修改状态
				ps.setIsSuccess(true);
				ps.setRechargeTotal(_recharge+_points);
				statisticsService.save(ps);
				
				info("结算人："+user.getUserName()+" 结算时间："+DateUtil.getStrYMDHMSByDate(new Date())+" 结算id:"+tradeId);
				JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				//System.out.println(e.getMessage());
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,e.getMessage()));
				return NONE;
			}
	    }
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/balanceUsergsRechargeRequest", method = {RequestMethod.GET, RequestMethod.POST})
		public String balanceUsergsRechargeRequest(HttpServletRequest request, HttpServletResponse response,long tradeId,double specialBalance,double recharge) {
			try {
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				UserRechargeRequest ps = statisticsService.getById(UserRechargeRequest.class,tradeId);
				boolean success = false;
				if(ps.getIsSuccess()!=null){
					success = ps.getIsSuccess();
				}
				if(ps==null||success){
					throw new BaseException("ID出错！");
				}
				Truck truckInfo = statisticsService.getById(Truck.class,ps.getUserId());
				if(truckInfo==null){
					throw new BaseException("ID出错！");
				}
				double _recharge = 0.0;
				double _specialBalance = 0L;
				//
				if(ps.getPayPoint()<=truckInfo.getSpecialBalance()){
					_recharge = ps.getRechargeMuch();
					_specialBalance = ps.getPayPoint();
				}else{
					_specialBalance = truckInfo.getSpecialBalance();
					_recharge = ps.getRechargeMuch();
				}
				if(_recharge!=recharge||_specialBalance!=specialBalance){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
					return NONE;
				}
				statisticsService.balanceUsergsRechargeRequest(tradeId, specialBalance, recharge);
				//修改状态
				ps.setIsSuccess(true);
				ps.setRechargeTotal(_recharge+_specialBalance);
				statisticsService.save(ps);
				
				info("结算人："+user.getUserName()+" 结算时间："+DateUtil.getStrYMDHMSByDate(new Date())+" 结算id:"+tradeId);
				JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,e.getMessage()));
				return NONE;
			}
	    }
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/queryjyRechargeRequest", method = {RequestMethod.GET, RequestMethod.POST})
		public String queryjyRechargeRequest(HttpServletRequest request, HttpServletResponse response,long tradeId) {
			try {
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				UserRechargeRequest ps = statisticsService.getById(UserRechargeRequest.class,tradeId);
				if(ps==null){
					throw new BaseException("ID出错！");
				}
				Truck truckInfo = statisticsService.getById(Truck.class,ps.getUserId());
				if(truckInfo==null){
					throw new BaseException("ID出错！");
				}
				double _recharge = 0.0;
				long _points = 0L;
				if(ps.getPayPoint()<=truckInfo.getPoints()){
					_recharge = ps.getRechargeMuch();
					_points = ps.getPayPoint();
				}else{
					_points = truckInfo.getPoints();
					_recharge = ps.getRechargeMuch();
				}
				Map ret = new HashMap();
				ret.put("userPoints", truckInfo.getPoints());
				ret.put("orderNeedPoints", ps.getPayPoint());
				ret.put("id", ps.getId());
				ret.put("orderNo", ps.getOrderNo());
				ret.put("recharge", _recharge);
				ret.put("points", _points);
				ret.put("total", _recharge+_points);
				JSONHelper.returnInfo(JSONHelper.bean2json(ret));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/querygsRechargeRequest", method = {RequestMethod.GET, RequestMethod.POST})
		public String querygsRechargeRequest(HttpServletRequest request, HttpServletResponse response,long tradeId) {
			try {
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				UserRechargeRequest ps = statisticsService.getById(UserRechargeRequest.class,tradeId);
				if(ps==null){
					throw new BaseException("ID出错！");
				}
				Truck truckInfo = statisticsService.getById(Truck.class,ps.getUserId());
				if(truckInfo==null){
					throw new BaseException("ID出错！");
				}
				double _recharge = 0.0;
				double _specialBalance = 0L;
				//
				if(ps.getPayPoint()<=truckInfo.getSpecialBalance()){
					_recharge = ps.getRechargeMuch();
					_specialBalance = ps.getPayPoint();
				}else{
					_specialBalance = truckInfo.getSpecialBalance();
					_recharge = ps.getRechargeMuch();
				}
				Map ret = new HashMap();
				ret.put("userSpecialBalance", truckInfo.getSpecialBalance());
				ret.put("orderNeedSpecialBalance", ps.getPayPoint());
				ret.put("id", ps.getId());
				ret.put("orderNo", ps.getOrderNo());
				ret.put("recharge", _recharge);
				ret.put("specialBalance", _specialBalance);
				ret.put("total", _recharge+_specialBalance);
				JSONHelper.returnInfo(JSONHelper.bean2json(ret));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/balanceCompanyStatements", method = {RequestMethod.GET, RequestMethod.POST})
		public String balanceCompanyStatements(HttpServletRequest request, HttpServletResponse response,long[] tradeId) {
			try {
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("C0001".equals(user.getDepartment())||"A0001".equals(user.getDepartment())){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
					return NONE;
				}
				statisticsService.balanceCompanyStatements(tradeId);
				StringBuffer ids = new StringBuffer();
				for(long id:tradeId){
					ids.append(id+"|");
				}
				info("公司账单结算人："+user.getUserName()+" 结算时间："+DateUtil.getStrYMDHMSByDate(new Date())+" 结算id:"+ids);
				JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/companyAccountBooks", method = {RequestMethod.GET, RequestMethod.POST})
		public String companyAccountBooks(HttpServletRequest request, HttpServletResponse response,String tradeType,String orderNo,String startTime,String endTime,int currentPage) {
			try {
				args = new HashMap<String,String>();
				if(!StringUtils.isBlank(orderNo)){
					orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
					args.put("orderNo", orderNo);
				}
				if(!StringUtils.isBlank(tradeType)){
					tradeType = URLDecoder.decode(tradeType,"UTF-8"); 
					args.put("tradeType", tradeType);
				}
				if(!StringUtils.isBlank(startTime)){
					args.put("startTime", startTime);
				}
				if(!StringUtils.isBlank(endTime)){
					endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
					args.put("endTime", endTime);
				}
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("A0001".equals(user.getDepartment())){
					args.put("proxyUserId", user.getDepartmentId()+"");
				}
				//  ts.id id, ts.account_id userId,order_no orderNo,pay pay,remark remark,user_type userType,pay_points point,ts.created_date date,ts.cost costpay 
				Page page = statisticsService.companyAccountBooks(args,currentPage,pageSize);
				List<Map> list = page.getResults();
				List<Map> retlist = new ArrayList<Map>();
				for(Map sm:list){
					//cts1.id cts1id, cts1.account_id cts1userId,cts1.order_no cts1orderNo,cts1.pay cts1pay,cts1.remark cts1remark,cts1.user_type cts1userType,cts1.pay_points cts1point,cts1.created_date cts1date, "
						//	+ " cts2.id cts2id, cts2.account_id cts2userId,cts2.order_no cts2orderNo,cts2.pay cts2pay,cts2.remark cts2remark,cts2.user_type cts2userType,cts2.pay_points cts2point,cts2.created_date cts2date  "
					Map ret = new HashMap();
					ret.put("id", sm.get("id"));
					ret.put("userId", sm.get("userid"));
					ret.put("orderNo", sm.get("orderno"));
					ret.put("userType", Constants.UserType.getDescript((Short)sm.get("usertype")));
					ret.put("pay", convert((Double)sm.get("pay")));
					ret.put("point", sm.get("point"));
					ret.put("remark", sm.get("remark"));
					ret.put("proxyprice", convert((Double)sm.get("proxypricepay")));
					ret.put("date", DateUtil.getStrYMDHMSByDate((Date)sm.get("date")));
					ret.put("costpay", convert((Double)sm.get("costpay")));
					ret.put("isSuccess",sm.get("issuccess"));
					retlist.add(ret);
				}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("totalCount", page.getTotalCount()+"");
				map.put("root", retlist);
				Map otherInfo = page.getOtherMap();
				otherInfo.put("userType", user.getDepartment());
				map.put("message", JSONObject.fromObject(otherInfo).toString());
				JSONHelper.returnInfo(JSONObject.fromObject(map));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
	    
	    private double convert(double value){   
	        long   l1   =   Math.round(value*100);   //四舍五入   
	        double   ret   =   l1/100.0;               //注意：使用   100.0   而不是   100   
	        return   ret;   
	    }  

	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/waybillList", method = {RequestMethod.GET, RequestMethod.POST})
		public String waybillList(HttpServletRequest request, HttpServletResponse response,String content,int currentPage) {
			try {
				args = new HashMap<String,String>();
				if(!StringUtils.isBlank(content)){
					content = URLDecoder.decode(content,"UTF-8"); 
					args.put("content", content);
				}
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				if("A0001".equals(user.getDepartment())){
					args.put("proxyUserId", user.getDepartmentId()+"");
				}
				Page page = statisticsService.waybillByPage(args, currentPage, pageSize);
				List<Map> list = page.getResults();
				List<Map> retlist = new ArrayList<Map>();
				for(Object sm:list){
					WaybillInfo wi = (WaybillInfo)sm;
					Map ret = new HashMap();
					ret.put("id", wi.getId());
					ret.put("orderNo", wi.getOrderNo());
					ret.put("addTime", DateUtil.getStrYMDHMByDate(wi.getAddTime()));
					ret.put("consignorName", wi.getConsignorName());
					ret.put("consignorMobile", wi.getConsignorMobile());
					ret.put("origin", wi.getOrigin());
					ret.put("destination", wi.getDestination());
					ret.put("truckType", wi.getTruckType());
					ret.put("truckLength", wi.getTruckLength());
					ret.put("goodsType", wi.getGoodsType());
					ret.put("weight", wi.getWeight());
					ret.put("bulk", wi.getBulk());
					ret.put("fee", wi.getFee());
					ret.put("bond",wi.getBond());
					ret.put("payType", wi.getPayType());
					ret.put("memo", wi.getMemo());
					retlist.add(ret);
				}
				JSONHelper.returnInfo(JSONHelper.list2json(retlist,page.getTotalCount()+""));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
}
