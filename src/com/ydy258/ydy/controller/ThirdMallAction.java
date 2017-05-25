

package com.ydy258.ydy.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.ydy258.ydy.AppConst;
import com.ydy258.ydy.Constants;
import com.ydy258.ydy.entity.GoodsCategory;
import com.ydy258.ydy.entity.SysRole;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.ThirdMall;
import com.ydy258.ydy.entity.ThirdMallGoods;
import com.ydy258.ydy.entity.ThirdMallOrder;
import com.ydy258.ydy.entity.ThirdMallOrderDetail;
import com.ydy258.ydy.entity.ThirdMallTeller;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.service.ISysUserService;
import com.ydy258.ydy.service.IThirdMallOrderDetailService;
import com.ydy258.ydy.service.IThirdMallService;
import com.ydy258.ydy.util.DateUtil;
import com.ydy258.ydy.util.ExportExcel;
import com.ydy258.ydy.util.GeoHash;
import com.ydy258.ydy.util.JSONHelper;
import com.ydy258.ydy.util.MD5;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.ThirdOrder;
import com.ydy258.ydy.util.utils;

@Controller
@RequestMapping("/sys/thirdmall/")
public class ThirdMallAction extends BaseFacade  {
	
	@Autowired
	private IThirdMallService thirdMallService;
	
	@Autowired
	private ISysUserService sysUserService;
	

	/** servletContext */
	private ServletContext servletContext;	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/thirdMallListByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String thirdMallListByPage(HttpServletRequest request, HttpServletResponse response,String realName,String userName,String storeName,String userId,int currentPage) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			
			if(!"A0001".equals(user.getDepartment())){
				where.put("agencyId", userId+"");
			}
			
			if("A0001".equals(user.getDepartment())){
				where.put("agencyId", user.getDepartmentId()+"");
			}
			
			if(!StringUtils.isBlank(realName)){
				realName = URLDecoder.decode(realName,"UTF-8"); 
				where.put("realName", realName);
			}
			if(!StringUtils.isBlank(userName)){
				realName = URLDecoder.decode(userName,"UTF-8"); 
				where.put("userName", userName);
			}
			if(!StringUtils.isBlank(storeName)){
				storeName = URLDecoder.decode(storeName,"UTF-8"); 
				where.put("storeName", storeName);
			}
			Page page = thirdMallService.thirdMallListByPage(where, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((Map)sm).get("id"));
				mo.put("realName", ((Map)sm).get("real_name"));
				mo.put("userName", ((Map)sm).get("user_name"));
				mo.put("storeName", ((Map)sm).get("store_name"));
				mo.put("storeType", ((Map)sm).get("store_type"));
				mo.put("address", ((Map)sm).get("address"));
				mo.put("agencyId", ((Map)sm).get("agency_id"));
				mo.put("description", ((Map)sm).get("description"));
				mo.put("phone", ((Map)sm).get("phone"));
				mo.put("real", ((Map)sm).get("is_real"));
				mo.put("isFirstRet", ((Map)sm).get("is_first_ret")==null?false:((Map)sm).get("is_first_ret"));
				mo.put("retRadio", ((Map)sm).get("ret_radio")==null?1:((Map)sm).get("ret_radio"));
				if(!(Boolean)mo.get("isFirstRet")){
					mo.put("retRadio", "---");
				}
				mo.put("createdDate", DateUtil.getStrYMDHMByDate((Date)((Map)sm).get("created_date")));
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.getTotalCount()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/thirdMallListByPage2", method = {RequestMethod.GET, RequestMethod.POST})
	public String thirdMallListByPage2(HttpServletRequest request, HttpServletResponse response,String userId) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			List<Map> list = new ArrayList<Map>();
			if(!user.getDepartment().equals("C0001")){
				where.put("agencyId", userId+"");
				List<Map> page = thirdMallService.thirdMallListByPage(where);
				for(Map sm:page){
					Map mo = new HashMap();
					mo.put("id", sm.get("id"));
					mo.put("storeName",sm.get("store_name"));
					list.add(mo);
				}
			}else{
				where.put("agencyId", userId+"");
				List<Map> page = thirdMallService.thirdMallListByPage(where);
				for(Map sm:page){
					if(((java.math.BigInteger)sm.get("addmin_id")+"").equals(user.getId()+"")){
						Map mo = new HashMap();
						mo.put("id", sm.get("id"));
						mo.put("storeName",sm.get("store_name"));
						list.add(mo);
						break;
					}
				}
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addMall", method = {RequestMethod.GET, RequestMethod.POST})
	public String addMall(HttpServletRequest request, 
			HttpServletResponse response,
			String realName,
			String password,
			String userName,
			String storeName,
			int storeType,
			String description,
			String address,
			double longitude,
			double latitude,
			Long mallId,
			boolean isReal,
			String phone,
			boolean isFirstRet,
			Double retRadio) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(!"A0001".equals(user.getDepartment())&&mallId==null){//如果不代理又是新增
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.no_permission_code,Constants.no_permission_msg));
				return NONE;
			}
			ThirdMall t = null;
			SysUser users = null;
			if(mallId!=null){
				t = (ThirdMall)thirdMallService.getById(ThirdMall.class, mallId);
				users = thirdMallService.getById(SysUser.class, t.getAddminId());
			}
			//如果上新增
			if(mallId==null){
				SysUser u = sysUserService.queryUserByName(userName);
				if(u!=null){
					JSONHelper.returnInfo(JSONHelper.appCode2json("用户名已经使用"));
					return NONE;
				}
			}
			if(mallId==null){
				users = new SysUser();
				users.setCreatedDate(new Date());
				users.setUserName(userName);
				users.setStatus("effective");
				users.setPosition("运的易合作商");
				List<SysRole> roles = sysUserService.listRolesByIDString("72");
				users.getRoles().clear();
				users.getRoles().addAll(roles);
				users.setParentId(user.getId());
				users.setDepartment("C0001");
				users.setEmpNumber("C0001");
			}
			if(!StringUtils.isBlank(password)){
				password = MD5.getInstance().getMD5(password);
				users.setPassword(password);
			}
			if(!StringUtils.isBlank(realName)){
				users.setRealName(realName);
			}
			thirdMallService.save(users);
			
			String geohash = GeoHash.encode(latitude, longitude).substring(0, AppConst.GEOHASH_LENGTH);
			Geometry g1 = new GeometryFactory().createPoint(new Coordinate(longitude, latitude));
			if(t==null){
				t = new ThirdMall();
				t.setCreatedDate(new Date());
				t.setAgencyId(user.getId());
				t.setAddminId(users.getId());
			}
			t.setLongitude(longitude);
			t.setLatitude(latitude);
			t.setGeohash(geohash);
			t.setLocation(g1);
			t.setDiscountInfo("");
			t.setIsDelete(false);
			t.setStoreName(storeName);
			t.setStoreType(storeType);
			t.setAddress(address);
			t.setDescription(description);
			t.setIsReal(isReal);
			t.setPhone(phone);
			t.setRank(0.0);
			t.setIsFirstRet(isFirstRet);
			t.setRetRadio(retRadio);
			thirdMallService.save(t);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/delMall", method = {RequestMethod.GET, RequestMethod.POST})
	public String delMall(HttpServletRequest request, 
			HttpServletResponse response,
			long mallId) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(!"A0001".equals(user.getDepartment())){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.no_permission_code,Constants.no_permission_msg));
				return NONE;
			}
			ThirdMall dd = (ThirdMall)thirdMallService.getById(ThirdMall.class, mallId);
			if(dd==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			SysUser users = (SysUser)thirdMallService.getById(SysUser.class,dd.getAddminId());
			if(users==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			//如果不是自己的商家 不能删除
			if(users.getParentId()!=user.getId()){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			thirdMallService.delete(SysUser.class,dd.getAddminId());
			thirdMallService.delete(ThirdMall.class,mallId);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryMall", method = {RequestMethod.GET, RequestMethod.POST})
	public String queryMall(HttpServletRequest request, 
			HttpServletResponse response,
			Long mallId) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			
			ThirdMall t = null;
			SysUser users = null;
			if(mallId!=null){
				t = (ThirdMall)thirdMallService.getById(ThirdMall.class, mallId);
			}
			SysUser admin = thirdMallService.getById(SysUser.class, t.getAddminId());
			Map ret = new HashMap();
			ret.put("id", t.getId());
			ret.put("realName", admin.getRealName());
			ret.put("userName", admin.getUserName());
			ret.put("storeName", t.getStoreName());
			ret.put("storeType", t.getStoreType());
			ret.put("address", t.getAddress());
			ret.put("agencyId", t.getAgencyId());
			ret.put("latitude", t.getLatitude());
			ret.put("longitude", t.getLongitude());
			ret.put("description", t.getDescription());
			ret.put("phone", t.getPhone());
			ret.put("real", t.getIsReal());
			ret.put("isFirstRet", t.getIsFirstRet()==null?false:t.getIsFirstRet());
			ret.put("retRadio", t.getRetRadio()==null?1.0:t.getRetRadio());
			JSONHelper.returnInfo(JSONHelper.bean2json(ret,true));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/thirdMallGoodsListByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String thirdMallGoodsListByPage(HttpServletRequest request, HttpServletResponse response,String goodsName,String proxyId,String storeId,int currentPage) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			if(!StringUtils.isBlank(goodsName)){
				goodsName = URLDecoder.decode(goodsName,"UTF-8"); 
				where.put("goodsName", goodsName);
			}
			if(!StringUtils.isBlank(proxyId)&&NumberUtils.isNumber(proxyId)){
				proxyId = URLDecoder.decode(proxyId,"UTF-8"); 
				where.put("proxyId", proxyId);
			}
			if(!StringUtils.isBlank(storeId)&&NumberUtils.isNumber(storeId)){
				storeId = URLDecoder.decode(storeId,"UTF-8"); 
				where.put("storeId", storeId);
			}
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(user.getDepartment().equals("C0001")){
				ThirdMall mall = thirdMallService.getMallByUserId(user.getId());
				where.put("storeId",mall.getId()+"");
			}
			Page page = thirdMallService.thirdMallGoodsListByPage(where, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((ThirdMallGoods)sm).getId());
				mo.put("storeId", ((ThirdMallGoods)sm).getStoreId());
				mo.put("storeNumber", ((ThirdMallGoods)sm).getStoredNumber());
				mo.put("goodsName", ((ThirdMallGoods)sm).getGoodsName());
				mo.put("units", ((ThirdMallGoods)sm).getUnits());
				mo.put("price", ((ThirdMallGoods)sm).getPrice());
				mo.put("units", ((ThirdMallGoods)sm).getUnits());
				mo.put("priceFlag", ((ThirdMallGoods)sm).getPriceFlag()==1?"计件计算":"输入计算");
				mo.put("createdDate", ((ThirdMallGoods)sm).getCreatedDate()==null?"":DateUtil.getStrYMDHMByDate(((ThirdMallGoods)sm).getCreatedDate()));
				if(user.getDepartment().equals("B0001")){
					mo.put("costPrice", ((ThirdMallGoods)sm).getCostPrice());
					mo.put("ratio", ((ThirdMallGoods)sm).getRatio());
					mo.put("returnRatio", ((ThirdMallGoods)sm).getReturnRatio());
					mo.put("rtnRatio", ((ThirdMallGoods)sm).getReturnRatio());
					mo.put("condition", ((ThirdMallGoods)sm).getCondition());
					mo.put("rtnRatio1",((ThirdMallGoods)sm).getLitteRatio());
					mo.put("specialRatio", ((ThirdMallGoods)sm).getSpecialBalanceRatio());
					mo.put("discountOff", ((ThirdMallGoods)sm).getDiscountOff());
				}
				list.add(mo);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("totalCount", page.getTotalCount()+"");
			map.put("root", list);
			Map otherInfo = new HashMap();
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
	@RequestMapping(value = "/addGoods", method = {RequestMethod.GET, RequestMethod.POST})
	public String addGoods(HttpServletRequest request, 
			HttpServletResponse response,
			String goodsName,
			Double price,
			String units,
			String description,
			int storedNumber,
			Double ratio,
			Double rtnRatio,
			Double condition,
			Double rtnRatio1,
			Double specialRatio,
			Long goodId,
			int priceFlag,
			Double discountOff
			) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(!"C0001".equals(user.getDepartment())&&!"B0001".equals(user.getDepartment())){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.no_permission_code,Constants.no_permission_msg));
				return NONE;
			}
			if("B0001".equals(user.getDepartment())){//
				if(goodId!=null){
					ThirdMallGoods t1 = (ThirdMallGoods)thirdMallService.getById(ThirdMallGoods.class, goodId);
					t1.setRatio(ratio);
					t1.setReturnRatio(rtnRatio);
					t1.setCondition(condition);
					t1.setLitteRatio(rtnRatio1);
					t1.setSpecialBalanceRatio(specialRatio);
					//t.setCostPrice(costPrice);
					//t1.setIsPerfect(true);
					t1.setDiscountOff(discountOff);
					thirdMallService.save(t1);
				}else{
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.no_permission_code,Constants.no_permission_msg));
					return NONE;
				}
			}
			ThirdMallGoods t =new ThirdMallGoods();
			t.setIsDelete(false);
			t.setCreatedDate(new Date());
			if(goodId!=null){
				t = (ThirdMallGoods)thirdMallService.getById(ThirdMallGoods.class, goodId);
			}
			//查找该用户的store
			ThirdMall mall = thirdMallService.getMallByUserId(user.getId());
			
			if(goodId==null){
				t.setStoreId(mall.getId());
			}
			t.setStoredNumber(storedNumber);
			t.setUnits(units);
			t.setPrice(price);
			t.setGoodsName(goodsName);
			t.setPriceFlag(priceFlag);
			/*if(goodId!=null){
				t.setCostPrice(utils.convert(price*t.getDefaultRatio()));
			}
			if(user.getDepartment().equals("B0001")){
				//t.setReturnRatio(returnRatio);
				t.setRatio(ratio);
				//t.setCostPrice(costPrice);
				t.setIsPerfect(true);
				//t.setDefaultRatio(costPrice/price);
			}*/
			thirdMallService.save(t);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }

	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryMallGoods", method = {RequestMethod.GET, RequestMethod.POST})
	public String queryMallGoods(HttpServletRequest request, 
			HttpServletResponse response,
			Long mallGoodsId) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			
			ThirdMallGoods t = new ThirdMallGoods();
			if(mallGoodsId!=null){
				t = (ThirdMallGoods)thirdMallService.getById(ThirdMallGoods.class, mallGoodsId);
			}
			
			Map mo = new HashMap();
			mo.put("id", t.getId());
			mo.put("storeId", t.getStoreId());
			mo.put("storedNumber",t.getStoredNumber());
			mo.put("goodsName", t.getGoodsName());
			mo.put("units", t.getUnits());
			mo.put("price",t.getPrice());
			mo.put("priceFlag",t.getPriceFlag());
			mo.put("units", t.getUnits());
			mo.put("createdDate", (t.getCreatedDate()==null?"":DateUtil.getStrYMDHMByDate(t.getCreatedDate())));
			if(user.getDepartment().equals("B0001")){
				mo.put("costPrice", t.getCostPrice());
				mo.put("ratio", t.getRatio());
				mo.put("rtnRatio", t.getReturnRatio());
				mo.put("condition", t.getCondition());
				mo.put("rtnRatio1", t.getLitteRatio());
				mo.put("specialRatio", t.getSpecialBalanceRatio());
				mo.put("discountOff", t.getDiscountOff());
				//mo.put("returnRatio",t.getReturnRatio());
			}
			mo.put("userType", user.getDepartment());
			JSONHelper.returnInfo(JSONHelper.bean2json(mo,true));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/delGoods", method = {RequestMethod.GET, RequestMethod.POST})
	public String delGoods(HttpServletRequest request, 
			HttpServletResponse response,
			long mallGoodId) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(!"C0001".equals(user.getDepartment())){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.no_permission_code,Constants.no_permission_msg));
				return NONE;
			}
			ThirdMallGoods t = (ThirdMallGoods)thirdMallService.getById(ThirdMallGoods.class, mallGoodId);
			t.setIsDelete(true);
			thirdMallService.save(t);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/thirdMallTellerListByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String thirdMallTellerListByPage(HttpServletRequest request, HttpServletResponse response,String tellerName,String proxyId,String storeId,int currentPage) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			if(!StringUtils.isBlank(tellerName)){
				tellerName = URLDecoder.decode(tellerName,"UTF-8"); 
				where.put("tellerName", tellerName);
			}
			if(!StringUtils.isBlank(storeId)&&NumberUtils.isNumber(storeId)){
				storeId = URLDecoder.decode(storeId,"UTF-8");
				where.put("storeId", storeId);
			}
			if(!StringUtils.isBlank(proxyId)&&NumberUtils.isNumber(proxyId)){
				proxyId = URLDecoder.decode(proxyId,"UTF-8");
				where.put("proxyId", proxyId);
			}
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			
			if(user.getDepartment().equals("C0001")){
				ThirdMall mall = thirdMallService.getMallByUserId(user.getId());
				where.put("storeId",mall.getId()+"");
			}
			
			Page page = thirdMallService.thirdMallTellerListByPage(where, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((ThirdMallTeller)sm).getId());
				mo.put("storeId", ((ThirdMallTeller)sm).getStoreId());
				mo.put("tellerId", ((ThirdMallTeller)sm).getTellerId());
				mo.put("tellerName", ((ThirdMallTeller)sm).getTellerName());
				mo.put("lastLogin", ((ThirdMallTeller)sm).getLastLogin()==null?"":DateUtil.getStrYMDHMByDate(((ThirdMallTeller)sm).getLastLogin()));
				mo.put("isLock", ((ThirdMallTeller)sm).getIsLock());
				mo.put("createdDate", ((ThirdMallTeller)sm).getCreatedDate()==null?"":DateUtil.getStrYMDHMByDate(((ThirdMallTeller)sm).getCreatedDate()));
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.getTotalCount()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addTeller", method = {RequestMethod.GET, RequestMethod.POST})
	public String addTeller(HttpServletRequest request, 
			HttpServletResponse response,
			String tellerId,
			String tellerName,
			String password,
			Boolean isLock,
			Long id
			) {
		try {
			ThirdMallTeller t = null;
			if(StringUtils.isBlank(tellerName)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			if(id==null){
				if(StringUtils.isBlank(password)){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
					return NONE;
				}
			}
			//如果是新增
			if(id==null){
				t = new ThirdMallTeller();
				t.setCreatedDate(new Date());
			}
			//如果是编辑
			if(id!=null){
				t = (ThirdMallTeller)thirdMallService.getById(ThirdMallTeller.class, id);
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(!"C0001".equals(user.getDepartment())){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.no_permission_code,Constants.no_permission_msg));
				return NONE;
			}
			//查找该用户的store
			ThirdMall mall = thirdMallService.getMallByUserId(user.getId());
			if(id==null){
				ThirdMallTeller teller = thirdMallService.getTellerByTellerNo(tellerId, mall.getId());
				//数据重复
				if(teller!=null){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
					return NONE;
				}
			}
			if(id==null){
				t.setTellerId(tellerId);
			}
			if(!StringUtils.isBlank(password)){
				password = MD5.getInstance().getMD5(password);
				t.setPassword(password);
			}
			t.setTellerName(tellerName);
			t.setIsLock(isLock);
			t.setStoreId(mall.getId());
			thirdMallService.save(t);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/lockTeller", method = {RequestMethod.GET, RequestMethod.POST})
	public String lockTeller(HttpServletRequest request, 
			HttpServletResponse response,
			long tellerId) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(!"C0001".equals(user.getDepartment())){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.no_permission_code,Constants.no_permission_msg));
				return NONE;
			}
			ThirdMallTeller teller = thirdMallService.getById(ThirdMallTeller.class, tellerId);
			if(teller.getIsLock()){
				teller.setIsLock(false);
			}else{
				teller.setIsLock(true);
			}
			thirdMallService.save(teller);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryMallTeller", method = {RequestMethod.GET, RequestMethod.POST})
	public String queryMallTeller(HttpServletRequest request, 
			HttpServletResponse response,
			Long tellerId) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			
			ThirdMallTeller t = null;
			if(tellerId!=null){
				t = (ThirdMallTeller)thirdMallService.getById(ThirdMallTeller.class, tellerId);
			}
			JSONHelper.returnInfo(JSONHelper.bean2json(t,true));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/thirdMallOrderExcel", method = {RequestMethod.GET, RequestMethod.POST})
	public String thirdMallOrderExcel(HttpServletRequest request, HttpServletResponse response,
			String tellerName,
			String customName,
			String storeId,
			String proxyId,
			String date,
			String orderNo,
			Integer orderStatus) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			if(!StringUtils.isBlank(tellerName)){
				tellerName = URLDecoder.decode(tellerName,"UTF-8");
				where.put("tellerName", tellerName);
			}
			if(!StringUtils.isBlank(storeId)&&NumberUtils.isNumber(storeId)){
				storeId = URLDecoder.decode(storeId,"UTF-8");
				where.put("storeId", storeId);
			}
			if(!StringUtils.isBlank(proxyId)&&NumberUtils.isNumber(proxyId)){
				proxyId = URLDecoder.decode(proxyId,"UTF-8");
				where.put("agencyId", proxyId);
			}
			if(!StringUtils.isBlank(customName)){
				customName = URLDecoder.decode(customName,"UTF-8");
				where.put("tellerName", customName);
			}
			if(!StringUtils.isBlank(date)){
				date = URLDecoder.decode(date,"UTF-8");
				where.put("date", date);
			}
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8");
				where.put("orderNo", orderNo);
			}
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			//如果是代理
			if("A0001".equals(user.getDepartment())){
				where.put("agencyId", user.getDepartmentId()+"");
			}
			if("C0001".equals(user.getDepartment())){
				//查找该用户的store
				ThirdMall mall = thirdMallService.getMallByUserId(user.getId());
				where.put("storeId", mall.getId()+"");
			}
			ExportExcel<ThirdOrder> ex = new ExportExcel<ThirdOrder>();
			String[] headers =
			{ "订单号", "客户名称", "业务员名称", "支付类型", "总额","支付额","支出驿道币","订单提交时间","订单支付时间"};
			List<ThirdOrder> dataset = new ArrayList<ThirdOrder>();
			List page = thirdMallService.thirdMallOrderExcel(where);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page){
				ThirdOrder to = new ThirdOrder();
				//to.setId(((Map)sm).get("id")+"");
				to.setAddTime((DateUtil.getStrYMDHMByDate((Date)((Map)sm).get("add_time"))));
				to.setAmount(((Map)sm).get("amount")+"");
				to.setCustomName(((Map)sm).get("custom_name")+"");
				to.setDiscount(((Map)sm).get("discount")+"");
				to.setOrderNo(((Map)sm).get("order_no")+"");
				to.setPayTime((DateUtil.getStrYMDHMByDate((Date)((Map)sm).get("pay_time"))));
				to.setPayType((((Map)sm).get("pay_type")+"").equals("1")?"余额支付":"微信支付");
				to.setPoints(((Map)sm).get("points")+"");
				to.setTellerName(((Map)sm).get("teller_name")+"");
				dataset.add(to);
			}
			
			String appden = ".xls";
			//产生目录名
			String dir = DateUtil.dateToShortCode();
			//产生新文件名
			String serverFileName = UUID.randomUUID().toString();
			String path = session.getServletContext().getRealPath("/") + "thirdOrderExcel" + File.separator + dir + File.separator;
			File f = new File(path);
			if (!f.exists())
				f.mkdirs();
			String fileName = path + serverFileName + appden;
			// 定义上传路径
			File localFile = new File(fileName);
			OutputStream out = new FileOutputStream(fileName);
			ex.exportExcel(headers, dataset, out);
			out.close();

			/*
			//下载.
			File file = new File(fileName);// path是根据日志路径和文件名拼接出来的
		    String filename = file.getName();// 获取日志文件名称
		    InputStream fis = new BufferedInputStream(new FileInputStream(fileName));
		    byte[] buffer = new byte[fis.available()];
		    fis.read(buffer);
		    fis.close();
		    response.reset();
		    // 先去掉文件名称中的空格,然后转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
		    response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.replaceAll(" ", "").getBytes("utf-8"),"iso8859-1"));
		    response.addHeader("Content-Length", "" + file.length());
		    OutputStream os = new BufferedOutputStream(response.getOutputStream());
		    response.setContentType("application/octet-stream");
		    os.write(buffer);// 输出文件
		    os.flush();
		    os.close();*/
			System.out.println("thirdOrderExcel" + File.separator + dir + File.separator+serverFileName + appden);
			JSONHelper.returnInfo(JSONHelper.bean2json("thirdOrderExcel/"+ dir + "/"+serverFileName + appden));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/thirdMallOrderListByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String thirdMallOrderListByPage(HttpServletRequest request, HttpServletResponse response,
			String tellerName,
			String customName,
			String storeId,
			String proxyId,
			String date,
			String orderNo,
			Integer orderStatus,
			int currentPage) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			if(!StringUtils.isBlank(tellerName)){
				tellerName = URLDecoder.decode(tellerName,"UTF-8");
				where.put("tellerName", tellerName);
			}
			if(!StringUtils.isBlank(storeId)&&NumberUtils.isNumber(storeId)){
				storeId = URLDecoder.decode(storeId,"UTF-8");
				where.put("storeId", storeId);
			}
			if(!StringUtils.isBlank(proxyId)&&NumberUtils.isNumber(proxyId)){
				proxyId = URLDecoder.decode(proxyId,"UTF-8");
				where.put("agencyId", proxyId);
			}
			if(!StringUtils.isBlank(customName)){
				customName = URLDecoder.decode(customName,"UTF-8");
				where.put("tellerName", customName);
			}
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8");
				where.put("orderNo", orderNo);
			}
			if(!StringUtils.isBlank(date)){
				date = URLDecoder.decode(date,"UTF-8");
				where.put("date", date);
			}
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			//如果是代理
			if("A0001".equals(user.getDepartment())){
				where.put("agencyId", user.getDepartmentId()+"");
			}
			if("C0001".equals(user.getDepartment())){
				//查找该用户的store
				ThirdMall mall = thirdMallService.getMallByUserId(user.getId());
				where.put("storeId", mall.getId()+"");
			}
			Page page = thirdMallService.thirdMallOrderListByPage(where, currentPage, pageSize);
			
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				
				mo.put("id", ((Map)sm).get("id"));
				mo.put("receiveName", ((Map)sm).get("receive_name"));
				mo.put("receiveMobile", ((Map)sm).get("receive_mobile"));
				mo.put("receiveAdress", ((Map)sm).get("receive_adress"));
				mo.put("receiveAdressDtl", ((Map)sm).get("receive_adress_dtl"));
				mo.put("billType", ((Map)sm).get("bill_type"));
				mo.put("billName", ((Map)sm).get("bill_name"));
				mo.put("billRecognition", ((Map)sm).get("bill_recognition"));
				mo.put("billAddress", ((Map)sm).get("bill_address"));
				mo.put("billAddressDtl", ((Map)sm).get("bill_address_dtl"));
				mo.put("billPhone", ((Map)sm).get("bill_phone"));
				mo.put("bank", ((Map)sm).get("bank"));
				mo.put("bankAcc", ((Map)sm).get("bank_acc"));
				mo.put("mailCharge", ((Map)sm).get("mail_charge"));
				mo.put("taxCharge", ((Map)sm).get("tax_charge"));
				mo.put("billStatus", ((Map)sm).get("bill_status"));
				
				mo.put("orderNo", ((Map)sm).get("order_no"));
				mo.put("customName", ((Map)sm).get("custom_name"));
				//mo.put("goodsNumber", utils.convert((Double)((Map)sm).get("goods_number")));
				mo.put("amount",  utils.convert((Double)((Map)sm).get("amount")));
				mo.put("addTime", (DateUtil.getStrYMDHMByDate((Date)((Map)sm).get("add_time"))));
				mo.put("orderStatus", ((Map)sm).get("order_status"));
				mo.put("payTime", (DateUtil.getStrYMDHMByDate((Date)((Map)sm).get("pay_time"))));
				mo.put("tellerName", ((Map)sm).get("teller_name"));
				mo.put("goodsName", ((Map)sm).get("goods_name"));
				mo.put("payType", ((Map)sm).get("pay_type"));
				mo.put("discount", ((Map)sm).get("discount"));
				mo.put("points", ((Map)sm).get("points"));
				if("B0001".equals(user.getDepartment())){
					mo.put("cost",  utils.convert((Double)((Map)sm).get("cost")));
					mo.put("discount", ((Map)sm).get("discount"));
					mo.put("points", ((Map)sm).get("points"));
				}
				list.add(mo);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("totalCount", page.getTotalCount()+"");
			map.put("root", list);
			Map otherInfo = new HashMap();
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
	
	@Autowired
	private IThirdMallOrderDetailService orderDetailService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/thirdMallOrderQuery", method = {RequestMethod.GET, RequestMethod.POST})
	public String thirdMallOrderQuery(HttpServletRequest request, HttpServletResponse response,
			String orderNo) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			if(!StringUtils.isBlank(orderNo)){
			}
			ThirdMallOrder order = thirdMallService.thirdMallOrderByOrderNo(orderNo);
			List<ThirdMallOrderDetail> details = orderDetailService.getByOrderNo(orderNo);
			ThirdMall t = (ThirdMall)thirdMallService.getById(ThirdMall.class, order.getStoreId());
			Truck truck = thirdMallService.getById(Truck.class, order.getCustomId());
			Map ret = new HashMap();
			List dtls = new ArrayList();
			double count = 0.0;
			double total = 0.0;
			for(ThirdMallOrderDetail od: details){
				   count+=od.getGoodsNumber();
				   total+=od.getAmount();
				   Map d = new HashMap();
				   d.put("goodName", od.getGoodsName());
				   d.put("price", od.getPrice());
				   d.put("number", od.getGoodsNumber());
				   d.put("ant",od.getAmount());
				   dtls.add(d);
			}
			ret.put("total", total);
			ret.put("count", count);
			ret.put("list", dtls);
			ret.put("storeName", t.getStoreName());
			ret.put("payTime", DateUtil.getStrYMDHMByDate(order.getPayTime()));
			ret.put("tellerId", order.getTellerId());
			ret.put("orderNo", order.getOrderNo());
			ret.put("mobile", truck.getMobile());
			JSONHelper.returnInfo(JSONHelper.bean2json(ret));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/thirdMallOrderModify", method = {RequestMethod.GET, RequestMethod.POST})
	public String thirdMallOrderModify(HttpServletRequest request, HttpServletResponse response,
			long orderId) {
		try {
			ThirdMallOrder order = thirdMallService.getById(ThirdMallOrder.class, orderId);
			if(order.getBillStatus()!=2){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,"不是等待发开票状态，不能开发票"));
				return NONE;
			}
			order.setBillStatus(3);//3：开过发票
			thirdMallService.save(order);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	/*@SuppressWarnings("unchecked")
	@RequestMapping(value = "/thirdMallOrderQuery", method = {RequestMethod.GET, RequestMethod.POST})
	public String thirdMallOrderQuery(HttpServletRequest request, HttpServletResponse response,
			String orderNo) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8");
				where.put("tellerName", orderNo);
			}
			List list = thirdMallService.thirdMallOrderByOrderNo(where);
			if(list==null||list.size()>0){
				Map sm = (Map)list.get(0);
				Map mo = new HashMap();
				mo.put("id", ((Map)sm).get("id"));
				mo.put("orderNo", ((Map)sm).get("order_no"));
				mo.put("customName", ((Map)sm).get("custom_name"));
				mo.put("amount",  utils.convert((Double)((Map)sm).get("amount")));
				mo.put("addTime", (DateUtil.getStrYMDHMByDate((Date)((Map)sm).get("add_time"))));
				mo.put("orderStatus", ((Map)sm).get("order_status"));
				mo.put("payTime", (DateUtil.getStrYMDHMByDate((Date)((Map)sm).get("pay_time"))));
				mo.put("tellerName", ((Map)sm).get("teller_name"));
				mo.put("goodsName", ((Map)sm).get("goods_name"));
				mo.put("payType", ((Map)sm).get("pay_type"));
				mo.put("discount", ((Map)sm).get("discount"));
				mo.put("points", ((Map)sm).get("points"));
			}
			
			String separator = "/";
			String path = request.getSession().getServletContext().getRealPath("/");
			String templatePath = path+"template"+separator+"template.pdf";
			String fileName = utils.generaterOrderNumber()+".pdf";
			String dir = "desc"+separator+DateUtil.getStrYMDByDate(new Date());
			String descPath = path+dir;
			File file = new File(descPath);
			if(!file.exists()){
				file.mkdirs();
			}
			String retPath = "";
			ThirdMallOrder order = thirdMallService.thirdMallOrderByOrderNo(orderNo);
			List<ThirdMallOrderDetail> details = orderDetailService.getByOrderNo(orderNo);
			ThirdMall t = (ThirdMall)thirdMallService.getById(ThirdMall.class, order.getStoreId());
			if(order.getOrderPath()==null){
				PDFCreate.createPTF(t,order, details, descPath+separator+fileName);
				order.setOrderPath(dir+separator+fileName);
				thirdMallService.save(order);
				retPath = order.getOrderPath();
			}else{
				PDFCreate.createPTF(t,order, details, descPath+separator+fileName);
				order.setOrderPath(dir+separator+fileName);
				thirdMallService.save(order);
				retPath = order.getOrderPath();
			}
			
			JSONHelper.returnInfo(JSONHelper.bean2json(retPath));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }*/
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/goodsCategoryListByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String goodsCategoryListByPage(HttpServletRequest request, HttpServletResponse response,String categoryName,int currentPage) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			if(!StringUtils.isBlank(categoryName)){
				categoryName = URLDecoder.decode(categoryName,"UTF-8"); 
				where.put("categoryName", categoryName);
			}
			Page page = thirdMallService.goodsCategoryListByPage(where, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((GoodsCategory)sm).getId());
				mo.put("cateName", ((GoodsCategory)sm).getCateName());
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.getTotalCount()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/goodsCategoryListByStoreId", method = {RequestMethod.GET, RequestMethod.POST})
	public String goodsCategoryListByStoreId(HttpServletRequest request, HttpServletResponse response,long storeId) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			where.put("storeId", storeId+"");
			Page page = thirdMallService.goodsCategoryListByPage(where, 1, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((GoodsCategory)sm).getId());
				mo.put("cateName", ((GoodsCategory)sm).getCateName());
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.getTotalCount()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addCategory", method = {RequestMethod.GET, RequestMethod.POST})
	public String addCategory(HttpServletRequest request, 
			HttpServletResponse response,
			String cateName
			) {
		try {
			ThirdMallTeller t = null;
			if(StringUtils.isBlank(cateName)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			GoodsCategory cate = new GoodsCategory();
			cate.setCateName(cateName);
			
			thirdMallService.save(cate);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteCategory", method = {RequestMethod.GET, RequestMethod.POST})
	public String deleteCategory(HttpServletRequest request, 
			HttpServletResponse response,
			long cateId) {
		try {
			GoodsCategory cate = thirdMallService.getById(GoodsCategory.class, cateId);
			if(cate!=null)
				thirdMallService.delete(GoodsCategory.class, cateId);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
}
