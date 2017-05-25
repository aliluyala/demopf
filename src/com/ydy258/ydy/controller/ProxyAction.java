

package com.ydy258.ydy.controller;


import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.Setting;
import com.ydy258.ydy.StringUtil;
import com.ydy258.ydy.entity.BusinessRules;
import com.ydy258.ydy.entity.Consignor;
import com.ydy258.ydy.entity.IDCardQueryOrder;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.TInsurance;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.service.ICommDataService;
import com.ydy258.ydy.service.IProxyService;
import com.ydy258.ydy.service.ITallyService;
import com.ydy258.ydy.util.CICCUtils;
import com.ydy258.ydy.util.DateUtil;
import com.ydy258.ydy.util.JSONHelper;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.ParameterUtils;
import com.ydy258.ydy.util.SendmailUtils;
import com.ydy258.ydy.util.SettingUtils;

@Controller
@RequestMapping("/sys/proxy/")
public class ProxyAction extends BaseFacade  {
	
	@Autowired
	private ICommDataService commDataService;
	
	@Autowired
	private IProxyService proxyService;
	
	@Autowired
	private ITallyService tallyService;
	
	final private Setting setting = SettingUtils.get();
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insuranceByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String insuranceByPage(HttpServletRequest request, HttpServletResponse response,String orderNo,String status,String startTime,String endTime,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
				args.put("orderNo", orderNo);
			}
			if(!StringUtils.isBlank(status)){
				orderNo = URLDecoder.decode(status,"UTF-8"); 
				args.put("status", status);
			}
			if(!StringUtils.isBlank(startTime)){
				startTime = URLDecoder.decode(startTime,"UTF-8"); 
				args.put("startTime", startTime);
			}
			if(!StringUtils.isBlank(endTime)){
				endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
				args.put("endTime", endTime);
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			args.put("company", Constants.InsuranceCompany.PICC.getType()+"");
			if("A0001".equals(user.getDepartment())){
				args.put("proxyUserId", user.getDepartmentId()+"");
			}
			if("D0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.TruckUser.getType()+"");
			}
			if("E0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.consignorUser.getType()+"");
			}
			Page page = proxyService.insuranceByPage(args, currentPage,  pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				TInsurance c = (TInsurance)sm;
				mo.put("id", c.getId());
				mo.put("orderNo", c.getOrderNo());
				mo.put("policyNo", c.getPolicyNo());
				if("B0001".equals(user.getDepartment())){
					mo.put("ratio", c.getRatio());
				}
				mo.put("price", c.getPrice());
				mo.put("recognizeePhone", c.getRecognizeePhone());
				mo.put("userType", Constants.UserType.getDescript(Short.valueOf(c.getUserType())));
				mo.put("goodsName", c.getGoodsName());
				mo.put("insuredAmount", c.getInsuredAmount());
				mo.put("fromLoc", c.getFromLoc());
				mo.put("toLoc", c.getToLoc());
				mo.put("policyPath", c.getPolicyPath());
				mo.put("company", Constants.InsuranceCompany.getEmunByStatus(c.getCompany()).getScript() );
				mo.put("status", Constants.InsuranceStatus.getEmunByStatus(c.getStatus()).getScript());
				mo.put("created_date", c.getCreated_date()==null?"":DateUtil.getStrYMDHMByDate(c.getCreated_date()));
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
	@RequestMapping(value = "/insuranceChinaLifeByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String insuranceChinaLifeByPage(HttpServletRequest request, HttpServletResponse response,String orderNo,String status,String startTime,String endTime,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
				args.put("orderNo", orderNo);
			}
			if(!StringUtils.isBlank(status)){
				orderNo = URLDecoder.decode(status,"UTF-8"); 
				args.put("status", status);
			}
			if(!StringUtils.isBlank(startTime)){
				startTime = URLDecoder.decode(startTime,"UTF-8"); 
				args.put("startTime", startTime);
			}
			if(!StringUtils.isBlank(endTime)){
				endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
				args.put("endTime", endTime);
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			args.put("company", Constants.InsuranceCompany.chinaLife.getType()+"");
			if("A0001".equals(user.getDepartment())){
				args.put("proxyUserId", user.getDepartmentId()+"");
			}
			if("D0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.TruckUser.getType()+"");
			}
			if("E0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.consignorUser.getType()+"");
			}
			Page page = proxyService.insuranceChinaLifeByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				TInsurance c = (TInsurance)sm;
				mo.put("id", c.getId());
				mo.put("orderNo", c.getOrderNo());
				mo.put("policyNo", c.getPolicyNo());
				mo.put("recognizeePhone", c.getRecognizeePhone());
				mo.put("goodsName", c.getGoodsName());
				mo.put("fromLoc", c.getFromLoc());
				mo.put("toLoc", c.getToLoc());
				mo.put("weigthOrCount", c.getWeigthOrCount());
				mo.put("packQty", c.getPackQty());
				mo.put("cargoNo", c.getCargoNo());
				mo.put("goodsTypeNo", c.getGoodsTypeNo());
				mo.put("mainGlausesCode", c.getMainGlausesCode().equals("PH001")?"普货":c.getMainGlausesCode().equals("XH001")?"鲜活":"易碎");
				mo.put("insuredAmount", c.getInsuredAmount());
				mo.put("transportType", c.getTransportType());
				mo.put("truckWeight", c.getTruckWeight());
				mo.put("truckType", c.getTruckType());
				mo.put("transportNo", c.getTransportNo());
				mo.put("departureDate", c.getDepartureDate());
				mo.put("effDate", c.getEffDate());
				mo.put("recognizeeName", c.getRecognizeeName());
				mo.put("recognizeePhone", c.getRecognizeePhone());
				mo.put("cardNo", c.getCardNo());
				mo.put("policyPath", c.getPolicyPath());
				mo.put("policyNo", c.getPolicyNoLong());
				mo.put("company", Constants.InsuranceCompany.getEmunByStatus(c.getCompany()).getScript() );
				mo.put("status", Constants.InsuranceStatus.getEmunByStatus(c.getStatus()).getScript());
				mo.put("created_date", c.getCreated_date()==null?"":DateUtil.getStrYMDHMByDate(c.getCreated_date()));
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
	@RequestMapping(value = "/insuranceCheckChinaLifeByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String insuranceCheckChinaLifeByPage(HttpServletRequest request, HttpServletResponse response,String orderNo,String status,String startTime,String endTime,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
				args.put("orderNo", orderNo);
			}
			if(!StringUtils.isBlank(status)){
				orderNo = URLDecoder.decode(status,"UTF-8"); 
				args.put("status", status);
			}
			if(!StringUtils.isBlank(startTime)){
				startTime = URLDecoder.decode(startTime,"UTF-8"); 
				args.put("startTime", startTime);
			}
			if(!StringUtils.isBlank(endTime)){
				endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
				args.put("endTime", endTime);
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			args.put("company", Constants.InsuranceCompany.chinaLife.getType()+"");
			if("A0001".equals(user.getDepartment())){
				args.put("proxyUserId", user.getDepartmentId()+"");
			}
			if("D0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.TruckUser.getType()+"");
			}
			if("E0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.consignorUser.getType()+"");
			}
			Page page = proxyService.insuranceChinaLifeByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				TInsurance c = (TInsurance)sm;
				mo.put("id", c.getId());
				mo.put("orderNo", c.getOrderNo());
				mo.put("policyNo", c.getPolicyNo());
				mo.put("recognizeePhone", c.getRecognizeePhone());
				mo.put("goodsName", c.getGoodsName());
				mo.put("fromLoc", c.getFromLoc());
				mo.put("toLoc", c.getToLoc());
				mo.put("weigthOrCount", c.getWeigthOrCount());
				mo.put("packQty", c.getPackQty());
				mo.put("cargoNo", c.getCargoNo());
				mo.put("goodsTypeNo", c.getGoodsTypeNo());
				mo.put("mainGlausesCode", c.getMainGlausesCode().equals("PH001")?"普货":c.getMainGlausesCode().equals("XH001")?"鲜活":"易碎");
				mo.put("insuredAmount", c.getInsuredAmount());
				mo.put("transportType", c.getTransportType());
				mo.put("truckType", c.getTruckType());
				mo.put("truckWeight", c.getTruckWeight());
				mo.put("transportNo", c.getTransportNo());
				mo.put("departureDate", c.getDepartureDate());
				mo.put("effDate", c.getEffDate());
				mo.put("recognizeeName", c.getRecognizeeName());
				mo.put("cardNo", c.getCardNo());
				mo.put("policyPath", c.getPolicyPath());
				mo.put("policyNo", c.getPolicyNoLong());
				mo.put("company", Constants.InsuranceCompany.getEmunByStatus(c.getCompany()).getScript() );
				mo.put("status", Constants.InsuranceStatus.getEmunByStatus(c.getStatus()).getScript());
				mo.put("created_date", c.getCreated_date()==null?"":DateUtil.getStrYMDHMByDate(c.getCreated_date()));
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
	@RequestMapping(value = "/checkChinaLife", method = {RequestMethod.GET, RequestMethod.POST})
	public String checkChinaLife(HttpServletRequest request,HttpServletResponse response,long id,String policy){
		try {
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(StringUtils.isBlank(policy)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			TInsurance di = proxyService.getById(TInsurance.class, id);
			if(di.getStatus()!=Constants.InsuranceStatus.checking.getStatus()){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"不是审核状态,不能审核"));
				return NONE;
			}
			ServletContext ctx = request.getSession().getServletContext();
			//创建一个通用的多部分解析器
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(ctx);
			//判断 request 是否有文件上传,即多部分请求
			if(multipartResolver.isMultipart(request)){
				//转换成多部分request  
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
				//取得request中的所有文件名
				Iterator<String> iter = multiRequest.getFileNames();
				while(iter.hasNext()){
					//取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if(file != null){
						//取得当前上传文件的文件名称
						String myFileName = file.getOriginalFilename();
						//如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if(myFileName.trim() !=""){
							// 重命名上传后的文件名
							// 取得文件后缀
							String appden = myFileName.substring(myFileName.lastIndexOf("."));
							if(!appden.equals(".pdf")){
								JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"请上传pdf文件"));
								return NONE;
							}
							//产生目录名
							String dir = DateUtil.dateToShortCode();
							//产生新文件名
							String serverFileName = UUID.randomUUID().toString();
							String path = ctx.getRealPath("/") + "uploadfiles" + File.separator + dir + File.separator;
							File f = new File(path);
							if (!f.exists())
								f.mkdirs();
							String imgName = path + serverFileName + appden;

							// 定义上传路径
							File localFile = new File(imgName);
							file.transferTo(localFile);
							
							String picUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							
							di.setPolicyPath(picUrl);
							di.setPolicyNoLong(policy);
							di.setStatus(Constants.InsuranceStatus.success.getStatus());
							proxyService.save(di);
						}
					}
				}
			}
			info("结算人寿货运险,订单号："+di.getOrderNo());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/accidentInsuranceChinaLifeByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String accidentInsuranceChinaLifeByPage(HttpServletRequest request, HttpServletResponse response,String orderNo,String status,String startTime,String endTime,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
				args.put("orderNo", orderNo);
			}
			if(!StringUtils.isBlank(status)){
				orderNo = URLDecoder.decode(status,"UTF-8"); 
				args.put("status", status);
			}
			if(!StringUtils.isBlank(startTime)){
				startTime = URLDecoder.decode(startTime,"UTF-8"); 
				args.put("startTime", startTime);
			}
			if(!StringUtils.isBlank(endTime)){
				endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
				args.put("endTime", endTime);
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			args.put("company", Constants.InsuranceCompany.accidentChinaLife.getType()+"");
			if("A0001".equals(user.getDepartment())){
				args.put("proxyUserId", user.getDepartmentId()+"");
			}
			if("D0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.TruckUser.getType()+"");
			}
			if("E0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.consignorUser.getType()+"");
			}
			Page page = proxyService.insuranceChinaLifeByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				TInsurance c = (TInsurance)sm;
				mo.put("id", c.getId());
				mo.put("orderNo", c.getOrderNo());
				mo.put("policyNo", c.getPolicyNo());
				mo.put("recognizeePhone", c.getRecognizeePhone());
				mo.put("insuredAmount", c.getInsuredAmount());
				mo.put("departureDate", c.getDepartureDate());
				mo.put("effDate", c.getEffDate());
				mo.put("recognizeeName", c.getRecognizeeName());
				mo.put("recognizeePhone", c.getRecognizeePhone());
				mo.put("cardNo", c.getCardNo());
				mo.put("policyPath", c.getPolicyPath());
				mo.put("policyNo", c.getPolicyNoLong());
				mo.put("endDate", c.getEndDate());
				mo.put("days", c.getDays());
				
				mo.put("fromLoc", c.getFromLoc());
				mo.put("toLoc", c.getToLoc());
				mo.put("registerNo", c.getRegisterNo());
				
				mo.put("personsCount", c.getPersonsCount());
				mo.put("company", Constants.InsuranceCompany.getEmunByStatus(c.getCompany()).getScript() );
				mo.put("status", Constants.InsuranceStatus.getEmunByStatus(c.getStatus()).getScript());
				mo.put("created_date", c.getCreated_date()==null?"":DateUtil.getStrYMDHMByDate(c.getCreated_date()));
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
	@RequestMapping(value = "/accidentInsuranceCheckChinaLifeByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String accidentInsuranceCheckChinaLifeByPage(HttpServletRequest request, HttpServletResponse response,String orderNo,String status,String startTime,String endTime,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
				args.put("orderNo", orderNo);
			}
			if(!StringUtils.isBlank(status)){
				orderNo = URLDecoder.decode(status,"UTF-8"); 
				args.put("status", status);
			}
			if(!StringUtils.isBlank(startTime)){
				startTime = URLDecoder.decode(startTime,"UTF-8"); 
				args.put("startTime", startTime);
			}
			if(!StringUtils.isBlank(endTime)){
				endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
				args.put("endTime", endTime);
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			args.put("company", Constants.InsuranceCompany.accidentChinaLife.getType()+"");
			if("A0001".equals(user.getDepartment())){
				args.put("proxyUserId", user.getDepartmentId()+"");
			}
			if("D0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.TruckUser.getType()+"");
			}
			if("E0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.consignorUser.getType()+"");
			}
			Page page = proxyService.insuranceChinaLifeByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				TInsurance c = (TInsurance)sm;
				mo.put("id", c.getId());
				mo.put("orderNo", c.getOrderNo());
				mo.put("policyNo", c.getPolicyNo());
				mo.put("recognizeePhone", c.getRecognizeePhone());
				mo.put("insuredAmount", c.getInsuredAmount());
				mo.put("departureDate", c.getDepartureDate());
				mo.put("effDate", c.getEffDate());
				mo.put("recognizeeName", c.getRecognizeeName());
				mo.put("cardNo", c.getCardNo());
				mo.put("policyPath", c.getPolicyPath());
				mo.put("policyNo", c.getPolicyNoLong());
				mo.put("endDate", c.getEndDate());
				mo.put("days", c.getDays());
				
				mo.put("fromLoc", c.getFromLoc());
				mo.put("toLoc", c.getToLoc());
				mo.put("registerNo", c.getRegisterNo());
				mo.put("personsCount", c.getPersonsCount());
				mo.put("company", Constants.InsuranceCompany.getEmunByStatus(c.getCompany()).getScript() );
				mo.put("status", Constants.InsuranceStatus.getEmunByStatus(c.getStatus()).getScript());
				mo.put("created_date", c.getCreated_date()==null?"":DateUtil.getStrYMDHMByDate(c.getCreated_date()));
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
	@RequestMapping(value = "/accidentCheckChinaLife", method = {RequestMethod.GET, RequestMethod.POST})
	public String accidentCheckChinaLife(HttpServletRequest request,HttpServletResponse response,long id,String policy,boolean checked,String tips){
		try {
			String jpushId = "";
			String msg = "";
			Long insureId = null;
			int insureType = -1;
			int osType = -1;
			boolean success = false;
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			
			TInsurance di = proxyService.getById(TInsurance.class, id);
			if(di.getStatus()!=Constants.InsuranceStatus.checking.getStatus()){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"不是审核状态,不能审核"));
				return NONE;
			}
			di.setPolicyPath("");
			if(checked){//审核成功
				if(StringUtils.isBlank(policy)){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
					return NONE;
				}
				di.setPolicyNoLong(policy);
				di.setStatus(Constants.InsuranceStatus.success.getStatus());
				msg = "您好！你的购买的保险已通过审核,保单号为:"+policy;
				proxyService.save(di);
				success = true;
				info("人寿货运险承保,订单号："+di.getOrderNo());
			}else{//审校不成功
				if("1".equals(tips)){
					policy = "资料不全";
					msg = "您好！你的购买的保险已拒保,拒保原因:"+policy;
				}else if("2".equals(tips)){
					policy = "资料不合法";
					msg = "您好！你的购买的保险已拒保,拒保原因:"+policy;
				}else{
					policy = "其它原因";
					msg = "您好！你的购买的保险已拒保,拒保原因:"+policy;
				}
				di.setPolicyNoLong(policy);
				di.setStatus(Constants.InsuranceStatus.refuce.getStatus());
				
				proxyService.save(di);
				success = false;
				//退款
				tallyService.tally(di.getUserId(), 
						Short.valueOf(di.getUserType()),
						di.getPrice(), 
						-di.getCost(),
						di.getProxyUserId(),
						-di.getProxyPrice(), 
						di.getPoints(), 
						Constants.TradeType.turnOutInsurance.getType(),
						"保险单退款",
						di.getOrderNo(),
						false);
				info("人寿货运险拒保,订单号："+di.getOrderNo());
			}
			
			
//			ServletContext ctx = request.getSession().getServletContext();
//			//创建一个通用的多部分解析器
//			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(ctx);
//			//判断 request 是否有文件上传,即多部分请求
//			if(multipartResolver.isMultipart(request)){
//				//转换成多部分request  
//				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
//				//取得request中的所有文件名
//				Iterator<String> iter = multiRequest.getFileNames();
//				while(iter.hasNext()){
//					//取得上传文件
//					MultipartFile file = multiRequest.getFile(iter.next());
//					if(file != null){
//						//取得当前上传文件的文件名称
//						String myFileName = file.getOriginalFilename();
//						//如果名称不为“”,说明该文件存在，否则说明该文件不存在
//						if(myFileName.trim() !=""){
//							// 重命名上传后的文件名
//							// 取得文件后缀
//							String appden = myFileName.substring(myFileName.lastIndexOf("."));
//							if(!appden.equals(".pdf")){
//								JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"请上传pdf文件"));
//								return NONE;
//							}
//							//产生目录名
//							String dir = DateUtil.dateToShortCode();
//							//产生新文件名
//							String serverFileName = UUID.randomUUID().toString();
//							String path = ctx.getRealPath("/") + "uploadfiles" + File.separator + dir + File.separator;
//							File f = new File(path);
//							if (!f.exists())
//								f.mkdirs();
//							String imgName = path + serverFileName + appden;
//
//							// 定义上传路径
//							File localFile = new File(imgName);
//							file.transferTo(localFile);
//							
//							String picUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
//							
//							di.setPolicyPath(picUrl);
//							di.setPolicyNoLong(policy);
//							di.setStatus(Constants.InsuranceStatus.success.getStatus());
//							proxyService.save(di);
//						}
//					}
//				}
//			}
			if(Short.valueOf(di.getUserType())==Constants.UserType.TruckUser.getType()){
				Truck truck = commDataService.getById(Truck.class, di.getUserId());
				jpushId = truck.getJpushId();
				osType = Integer.valueOf(truck.getMobileType());
				String ret = commDataService.push2Truck(jpushId, msg, di.getId(),di.getCompany(),success,osType);
			}else{
				Consignor consignor = commDataService.getById(Consignor.class, di.getUserId());
				jpushId = consignor.getJpushId();
				osType = Integer.valueOf(consignor.getMobileType());
				String ret = commDataService.push2Consignor(jpushId, msg, di.getId(),di.getCompany(),success,osType);
			}
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
	
	/**
	 * 车主无忧险列表
	 * @Title:        truckInsuranceByPage 
	 * @Description:  TODO
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param orderNo
	 * @param:        @param status
	 * @param:        @param startTime
	 * @param:        @param endTime
	 * @param:        @param currentPage
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2017年5月10日 上午10:06:31
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/truckInsuranceByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String truckInsuranceByPage(HttpServletRequest request, HttpServletResponse response,String orderNo,String status,String startTime,String endTime,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
				args.put("orderNo", orderNo);
			}
			if(!StringUtils.isBlank(status)){
				orderNo = URLDecoder.decode(status,"UTF-8"); 
				args.put("status", status);
			}
			if(!StringUtils.isBlank(startTime)){
				startTime = URLDecoder.decode(startTime,"UTF-8"); 
				args.put("startTime", startTime);
			}
			if(!StringUtils.isBlank(endTime)){
				endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
				args.put("endTime", endTime);
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			args.put("company", Constants.InsuranceCompany.ddTruckInsurance.getType()+"");
			if("A0001".equals(user.getDepartment())){
				args.put("proxyUserId", user.getDepartmentId()+"");
			}
			if("D0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.TruckUser.getType()+"");
			}
			if("E0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.consignorUser.getType()+"");
			}
			Page page = proxyService.insuranceChinaLifeByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				TInsurance c = (TInsurance)sm;
				mo.put("id", c.getId());
				mo.put("orderNo", c.getOrderNo());
				mo.put("policyNo", c.getPolicyNo());
				mo.put("recognizeePhone", c.getRecognizeePhone());
				mo.put("insuredAmount", c.getInsuredAmount());
				mo.put("departureDate", c.getDepartureDate());
				mo.put("effDate", c.getEffDate());
				mo.put("recognizeeName", c.getRecognizeeName());
				mo.put("cardNo", c.getCardNo());
				mo.put("policyPath", c.getPolicyPath());
				mo.put("policyNo", c.getPolicyNoLong());
				mo.put("endDate", c.getEndDate());
				mo.put("days", c.getDays());
				mo.put("personsCount", c.getPersonsCount());
				mo.put("registerNo", c.getRegisterNo());
				mo.put("personsCount", c.getPersonsCount());
				mo.put("lisencePath", c.getLisencePath());
				System.out.println("------"+c.getLisencePath());
				mo.put("company", Constants.InsuranceCompany.getEmunByStatus(c.getCompany()).getScript() );
				mo.put("status", Constants.InsuranceStatus.getEmunByStatus(c.getStatus()).getScript());
				mo.put("created_date", c.getCreated_date()==null?"":DateUtil.getStrYMDHMByDate(c.getCreated_date()));
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
	@RequestMapping(value = "/checkTruckUInsurance", method = {RequestMethod.GET, RequestMethod.POST})
	public String checkTruckUInsurance(HttpServletRequest request,HttpServletResponse response,long id,String policy,boolean checked,String tips){
		try {
			String jpushId = "";
			String msg = "";
			Long insureId = null;
			int insureType = -1;
			int osType = -1;
			boolean success = false;
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			
			TInsurance di = proxyService.getById(TInsurance.class, id);
			if(di.getStatus()!=Constants.InsuranceStatus.checking.getStatus()){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"不是审核状态,不能审核"));
				return NONE;
			}
			di.setPolicyPath("");
			if(checked){//审核成功
				if(StringUtils.isBlank(policy)){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
					return NONE;
				}
				di.setPolicyNoLong(policy);
				di.setStatus(Constants.InsuranceStatus.success.getStatus());
				msg = "您好！你的购买的保险已通过审核,保单号为:"+policy;
				proxyService.save(di);
				success = true;
				info("人寿货运险承保,订单号："+di.getOrderNo());
			}else{//审校不成功
				if("1".equals(tips)){
					policy = "资料不全";
					msg = "您好！你的购买的保险已拒保,拒保原因:"+policy;
				}else if("2".equals(tips)){
					policy = "资料不合法";
					msg = "您好！你的购买的保险已拒保,拒保原因:"+policy;
				}else{
					policy = "其它原因";
					msg = "您好！你的购买的保险已拒保,拒保原因:"+policy;
				}
				di.setPolicyNoLong(policy);
				di.setStatus(Constants.InsuranceStatus.refuce.getStatus());
				
				proxyService.save(di);
				success = false;
				//退款
				tallyService.tally(di.getUserId(), 
						Short.valueOf(di.getUserType()),
						di.getPrice(), 
						-di.getCost(),
						di.getProxyUserId(),
						-di.getProxyPrice(), 
						di.getPoints(), 
						Constants.TradeType.turnOutInsurance.getType(),
						"保险单退款",
						di.getOrderNo(),
						false);
				info("人寿货运险拒保,订单号："+di.getOrderNo());
			}
			
			
//			ServletContext ctx = request.getSession().getServletContext();
//			//创建一个通用的多部分解析器
//			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(ctx);
//			//判断 request 是否有文件上传,即多部分请求
//			if(multipartResolver.isMultipart(request)){
//				//转换成多部分request  
//				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
//				//取得request中的所有文件名
//				Iterator<String> iter = multiRequest.getFileNames();
//				while(iter.hasNext()){
//					//取得上传文件
//					MultipartFile file = multiRequest.getFile(iter.next());
//					if(file != null){
//						//取得当前上传文件的文件名称
//						String myFileName = file.getOriginalFilename();
//						//如果名称不为“”,说明该文件存在，否则说明该文件不存在
//						if(myFileName.trim() !=""){
//							// 重命名上传后的文件名
//							// 取得文件后缀
//							String appden = myFileName.substring(myFileName.lastIndexOf("."));
//							if(!appden.equals(".pdf")){
//								JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"请上传pdf文件"));
//								return NONE;
//							}
//							//产生目录名
//							String dir = DateUtil.dateToShortCode();
//							//产生新文件名
//							String serverFileName = UUID.randomUUID().toString();
//							String path = ctx.getRealPath("/") + "uploadfiles" + File.separator + dir + File.separator;
//							File f = new File(path);
//							if (!f.exists())
//								f.mkdirs();
//							String imgName = path + serverFileName + appden;
//
//							// 定义上传路径
//							File localFile = new File(imgName);
//							file.transferTo(localFile);
//							
//							String picUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
//							
//							di.setPolicyPath(picUrl);
//							di.setPolicyNoLong(policy);
//							di.setStatus(Constants.InsuranceStatus.success.getStatus());
//							proxyService.save(di);
//						}
//					}
//				}
//			}
			if(Short.valueOf(di.getUserType())==Constants.UserType.TruckUser.getType()){
				Truck truck = commDataService.getById(Truck.class, di.getUserId());
				jpushId = truck.getJpushId();
				osType = Integer.valueOf(truck.getMobileType());
				String ret = commDataService.push2Truck(jpushId, msg, di.getId(),di.getCompany(),success,osType);
			}else{
				Consignor consignor = commDataService.getById(Consignor.class, di.getUserId());
				jpushId = consignor.getJpushId();
				osType = Integer.valueOf(consignor.getMobileType());
				String ret = commDataService.push2Consignor(jpushId, msg, di.getId(),di.getCompany(),success,osType);
			}
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/insuranceDDByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String insuranceDDByPage(HttpServletRequest request, HttpServletResponse response,String orderNo,String status,String startTime,String endTime,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
				args.put("orderNo", orderNo);
			}
			if(!StringUtils.isBlank(status)){
				orderNo = URLDecoder.decode(status,"UTF-8"); 
				args.put("status", status);
			}
			if(!StringUtils.isBlank(startTime)){
				startTime = URLDecoder.decode(startTime,"UTF-8"); 
				args.put("startTime", startTime);
			}
			if(!StringUtils.isBlank(endTime)){
				endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
				args.put("endTime", endTime);
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			args.put("company", Constants.InsuranceCompany.dd.getType()+"");
			if("A0001".equals(user.getDepartment())){
				args.put("proxyUserId", user.getDepartmentId()+"");
			}
			if("D0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.TruckUser.getType()+"");
			}
			if("E0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.consignorUser.getType()+"");
			}
			Page page = proxyService.insuranceChinaLifeByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				TInsurance c = (TInsurance)sm;
				mo.put("id", c.getId());
				mo.put("orderNo", c.getOrderNo());
				mo.put("policyNo", c.getPolicyNo());
				mo.put("recognizeePhone", c.getRecognizeePhone());
				mo.put("goodsName", c.getGoodsName());
				mo.put("fromLoc", c.getFromLoc());
				mo.put("toLoc", c.getToLoc());
				mo.put("weigthOrCount", c.getWeigthOrCount());
				mo.put("packQty", c.getPackQty());
				mo.put("cargoNo", c.getCargoNo());
				mo.put("goodsTypeNo", c.getGoodsTypeNo());
				mo.put("mainGlausesCode", c.getGoodsTypeNo().equals("XH001")?"鲜活":"综合");
				mo.put("insuredAmount", c.getInsuredAmount());
				mo.put("transportType", c.getTransportType());
				mo.put("truckWeight", c.getTruckWeight());
				mo.put("truckType", c.getTruckType());
				mo.put("transportNo", c.getTransportNo());
				mo.put("departureDate", c.getDepartureDate());
				mo.put("effDate", c.getEffDate());
				mo.put("recognizeeName", c.getRecognizeeName());
				mo.put("recognizeePhone", c.getRecognizeePhone());
				mo.put("cardNo", c.getCardNo());
				mo.put("policyPath", c.getPolicyPath());
				mo.put("policyNo", c.getPolicyNoLong());
				mo.put("tPolicyNo", c.getPolicyNo());
				mo.put("company", Constants.InsuranceCompany.getEmunByStatus(c.getCompany()).getScript() );
				mo.put("status", Constants.InsuranceStatus.getEmunByStatus(c.getStatus()).getScript());
				mo.put("created_date", c.getCreated_date()==null?"":DateUtil.getStrYMDHMByDate(c.getCreated_date()));
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
	@RequestMapping(value = "/insuranceCheckDDByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String insuranceCheckDDByPage(HttpServletRequest request, HttpServletResponse response,String orderNo,String status,String startTime,String endTime,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
				args.put("orderNo", orderNo);
			}
			if(!StringUtils.isBlank(status)){
				orderNo = URLDecoder.decode(status,"UTF-8"); 
				args.put("status", status);
			}
			if(!StringUtils.isBlank(startTime)){
				startTime = URLDecoder.decode(startTime,"UTF-8"); 
				args.put("startTime", startTime);
			}
			if(!StringUtils.isBlank(endTime)){
				endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
				args.put("endTime", endTime);
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			args.put("company", Constants.InsuranceCompany.dd.getType()+"");
			if("A0001".equals(user.getDepartment())){
				args.put("proxyUserId", user.getDepartmentId()+"");
			}
			if("D0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.TruckUser.getType()+"");
			}
			if("E0001".equals(user.getDepartment())){
				args.put("userId", user.getDepartmentId()+"");
				args.put("userType", Constants.UserType.consignorUser.getType()+"");
			}
			Page page = proxyService.insuranceChinaLifeByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				TInsurance c = (TInsurance)sm;
				mo.put("id", c.getId());
				mo.put("orderNo", c.getOrderNo());
				mo.put("policyNo", c.getPolicyNo());
				mo.put("recognizeePhone", c.getRecognizeePhone());
				mo.put("goodsName", c.getGoodsName());
				mo.put("fromLoc", c.getFromLoc());
				mo.put("toLoc", c.getToLoc());
				mo.put("weigthOrCount", c.getWeigthOrCount());
				mo.put("packQty", c.getPackQty());
				mo.put("cargoNo", c.getCargoNo());
				mo.put("goodsTypeNo", c.getGoodsTypeNo());
				mo.put("mainGlausesCode",  c.getMainGlausesCode().equals("XH001")?"鲜活":"综合");
				mo.put("insuredAmount", c.getInsuredAmount());
				mo.put("transportType", c.getTransportType());
				mo.put("truckType", c.getTruckType());
				mo.put("truckWeight", c.getTruckWeight());
				mo.put("transportNo", c.getTransportNo());
				mo.put("departureDate", c.getDepartureDate());
				mo.put("effDate", c.getEffDate());
				mo.put("recognizeeName", c.getRecognizeeName());
				mo.put("cardNo", c.getCardNo());
				mo.put("policyPath", c.getPolicyPath());
				mo.put("policyNo", c.getPolicyNoLong());
				mo.put("tPolicyNo", c.getPolicyNo());
				mo.put("company", Constants.InsuranceCompany.getEmunByStatus(c.getCompany()).getScript() );
				mo.put("status", Constants.InsuranceStatus.getEmunByStatus(c.getStatus()).getScript());
				mo.put("created_date", c.getCreated_date()==null?"":DateUtil.getStrYMDHMByDate(c.getCreated_date()));
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
	@RequestMapping(value = "/checkDD", method = {RequestMethod.GET, RequestMethod.POST})
	public String checkDD(HttpServletRequest request,HttpServletResponse response,long id,String policy,boolean checked,String tips){
		try {
			
			String jpushId = "";
			String msg = "";
			Long insureId = null;
			int insureType = -1;
			int osType = -1;
			boolean success = false;
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			
			TInsurance di = proxyService.getById(TInsurance.class, id);
			if(di.getStatus()!=Constants.InsuranceStatus.checking.getStatus()){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"不是审核状态,不能审核"));
				return NONE;
			}
			if(checked){
				if(StringUtils.isBlank(policy)){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
					return NONE;
				}
				ServletContext ctx = request.getSession().getServletContext();
				//创建一个通用的多部分解析器
				CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(ctx);
				//判断 request 是否有文件上传,即多部分请求
				if(multipartResolver.isMultipart(request)){
					//转换成多部分request  
					MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
					//取得request中的所有文件名
					Iterator<String> iter = multiRequest.getFileNames();
					if(!iter.hasNext()){
						JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
						return NONE;
					}
					while(iter.hasNext()){
						//取得上传文件
						MultipartFile file = multiRequest.getFile(iter.next());
						if(file != null){
							//取得当前上传文件的文件名称
							String myFileName = file.getOriginalFilename();
							//如果名称不为“”,说明该文件存在，否则说明该文件不存在
							if(myFileName.trim() !=""){
								// 重命名上传后的文件名
								// 取得文件后缀
								String appden = myFileName.substring(myFileName.lastIndexOf("."));
								if(!appden.equals(".pdf")){
									JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"请上传pdf文件"));
									return NONE;
								}
								//产生目录名
								String dir = DateUtil.dateToShortCode();
								//产生新文件名
								String serverFileName = UUID.randomUUID().toString();
								String path = ctx.getRealPath("/") + "uploadfiles" + File.separator + dir + File.separator;
								File f = new File(path);
								if (!f.exists())
									f.mkdirs();
								String imgName = path + serverFileName + appden;

								// 定义上传路径
								File localFile = new File(imgName);
								file.transferTo(localFile);
								
								String picUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
								
								di.setPolicyPath(picUrl);
								di.setPolicyNoLong(policy);
								di.setStatus(Constants.InsuranceStatus.success.getStatus());
								proxyService.save(di);
								success = true;
								msg = "您好！你的购买的保险已通过审核,保单号为:"+policy;
								info("大地货运险承保,订单号："+di.getOrderNo());
							}
						}
					}
				}
			}else{
				if("1".equals(tips)){
					policy = "资料不全";
					msg = "您好！你的购买的保险已拒保,拒保原因:"+policy;
				}else if("2".equals(tips)){
					policy = "资料不合法";
					msg = "您好！你的购买的保险已拒保,拒保原因:"+policy;
				}else{
					policy = "其它原因";
					msg = "您好！你的购买的保险已拒保,拒保原因:"+policy;
				}
				di.setPolicyNoLong(policy);
				di.setStatus(Constants.InsuranceStatus.refuce.getStatus());
				proxyService.save(di);
				success = false;
				//退款
				tallyService.tally(di.getUserId(), 
						Short.valueOf(di.getUserType()),
						di.getPrice(), 
						-di.getCost(),
						di.getProxyUserId(),
						-di.getProxyPrice(), 
						di.getPoints(), 
						Constants.TradeType.turnOutInsurance.getType(),
						"保险单退款",
						di.getOrderNo(),
						false);
				info("大地货运险拒保,订单号："+di.getOrderNo());
			}
			//推送
			if(Short.valueOf(di.getUserType())==Constants.UserType.TruckUser.getType()){
				Truck truck = commDataService.getById(Truck.class, di.getUserId());
				jpushId = truck.getJpushId();
				osType = Integer.valueOf(truck.getMobileType());
				String ret = commDataService.push2Truck(jpushId, msg, di.getId(),di.getCompany(),success,osType);
			}else{
				Consignor consignor = commDataService.getById(Consignor.class, di.getUserId());
				jpushId = consignor.getJpushId();
				osType = Integer.valueOf(consignor.getMobileType());
				String ret = commDataService.push2Consignor(jpushId, msg, di.getId(),di.getCompany(),success,osType);
			}
			
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryInsurance", method = {RequestMethod.GET, RequestMethod.POST})
	public String queryInsurance(HttpServletRequest request, HttpServletResponse response,long id) {
		try {
			TInsurance insurance = proxyService.getById(TInsurance.class, id);
			if(insurance==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			JSONHelper.returnInfo(JSONHelper.bean2json(insurance));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/modifyInsurance", method = {RequestMethod.GET, RequestMethod.POST})
	public String modifyInsurance(HttpServletRequest request, HttpServletResponse response,
			long id,
			String typeCode,//货运类型
			String recognizeeName, 
			String packQty,
			String goodsName,
			String fromLoc,
			String toLoc,
			String departureDate,
			int insuredAmount, 
			String recognizeePhone,
			String weigthOrCount,
			String registerNo,
			String cardNo,
			String transport
			) {
		try {
			if (StringUtils.isBlank(typeCode)
					|| StringUtils.isBlank(recognizeeName)
					|| StringUtils.isBlank(packQty)
					|| StringUtils.isBlank(goodsName)
					|| StringUtils.isBlank(fromLoc)
					|| StringUtils.isBlank(toLoc)
					|| StringUtils.isBlank(departureDate)
					|| StringUtils.isBlank(recognizeePhone)
					|| StringUtils.isBlank(weigthOrCount)
					||StringUtils.isBlank(registerNo)) {
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			TInsurance insurance = proxyService.getById(TInsurance.class, id);
			if(insurance==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			JSONHelper.returnInfo(JSONHelper.bean2json(insurance));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cancleInsurance", method = {RequestMethod.GET, RequestMethod.POST})
	public String cancleInsurance(HttpServletRequest request, HttpServletResponse response,
			long id
			) {
		try {
			String xmlErrorMsg = "ErrorMsg";
			String xmlStatus = "Status";
			String partnerID = "常氏物流";
			String BusinessLogic = "CancelFreightPolicy";
			String pwd = "gxcswlxml";
		    String urlmd5 = "http://www.epicc.com.cn/ecargo/xmlDeal!md5InterfaceAction.action";
		    String urldata = "http://www.epicc.com.cn/ecargo/xmlDeal!xmlDealAction.action";
		  //不是公司内部员工不能操作
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			/*if(!"B0001".equals(user.getDepartment())){
				JSONHelper.returnInfo(JSONHelper.successInfo(Constants.no_permission_code,Constants.no_permission_msg));
				return NONE;
			}*/
			TInsurance insurance = proxyService.getById(TInsurance.class, id);
			if(insurance==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			if(insurance.getStatus()!=Constants.InsuranceStatus.success.getStatus()){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			if(DateUtil.getDateByStr(insurance.getDepartureDate()).getTime()<new Date().getTime()){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"启运时间已过，不能撤销！"));
				return NONE;
			}
			//取消赠送保险
			TInsurance ins = proxyService.getByParentId(insurance.getId());
			if(ins!=null){
				if(ins.getStatus()==Constants.InsuranceStatus.checking.getStatus()){
					ins.setStatus(Constants.InsuranceStatus.cancle.getStatus());
					proxyService.save(ins);
				}else if(ins.getStatus()==Constants.InsuranceStatus.success.getStatus()){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"赠送的人身意外险审核已通过，不能撤销！"));
					return NONE;
				}
			}
			Map<String,String> parammer = new LinkedHashMap<String,String>();
			parammer.put("BusinessLogic", BusinessLogic);
			parammer.put("PolicyNo", insurance.getPolicyNo());
			String key = proxyService.getKey(parammer,urlmd5,pwd);
			
			System.out.println("key: "+key);
			parammer = new LinkedHashMap<String,String>();
			parammer.put("PolicyNo", insurance.getPolicyNo());
			
			String retstr = proxyService.rbXMLPara(partnerID, BusinessLogic, key, parammer);
			System.out.println("reqestdata: "+retstr);
			
			String ret = proxyService.doPost(urldata, retstr);
			String status = StringUtil.readStringXML(ret, xmlStatus);
			info("撤消保险单，单号："+insurance.getPolicyNo());
			if(status==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			//如果撤单成功
			if("8".equals(status.trim())){
				insurance.setStatus(Constants.InsuranceStatus.cancle.getStatus());
				insurance.setRetInfo(StringUtil.readStringXML(ret, xmlErrorMsg));
				proxyService.save(insurance);
				//退款
				tallyService.tally(insurance.getUserId(), 
						Short.valueOf(insurance.getUserType()),
						insurance.getPrice(), 
						-insurance.getCost(),
						insurance.getProxyUserId(),
						-insurance.getProxyPrice(), 
						insurance.getPoints(), 
						Constants.TradeType.turnOutInsurance.getType(),
						"保险单退款",
						insurance.getOrderNo(),
						false);
				info("撤单成功，单号："+insurance.getPolicyNo());
			}
			if("0".equals(status.trim())){
				info("撤单失败，单号："+insurance.getPolicyNo());
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"撤单失败:人保系统出错"));
				return NONE;
			}
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cancleChinaLifeInsurance", method = {RequestMethod.GET, RequestMethod.POST})
	public String cancleChinaLifeInsurance(HttpServletRequest request, HttpServletResponse response,
			long id
			) {
		try {
		  //不是公司内部员工不能操作
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			TInsurance insurance = proxyService.getById(TInsurance.class, id);
			if(insurance==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			if(insurance.getStatus()!=Constants.InsuranceStatus.checking.getStatus()){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Date now = new Date();
			if(DateUtil.getDateByStr(insurance.getDepartureDate()).before(now)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"启运时间已过，不能撤销！"));
				return NONE;
			}
			//退款
			tallyService.tally(insurance.getUserId(), 
					Short.valueOf(insurance.getUserType()),
					insurance.getPrice(), 
					-insurance.getCost(),
					insurance.getProxyUserId(),
					-insurance.getProxyPrice(), 
					insurance.getPoints(), 
					Constants.TradeType.turnOutInsurance.getType(),
					"保险单退款",
					insurance.getOrderNo(),
					false);
			insurance.setStatus(Constants.InsuranceStatus.cancle.getStatus());
			proxyService.save(insurance);
			info("撤单成功，单号："+insurance.getPolicyNo());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cancleAccidentChinaLifeInsurance", method = {RequestMethod.GET, RequestMethod.POST})
	public String cancleAccidentChinaLifeInsurance(HttpServletRequest request, HttpServletResponse response,
			long id
			) {
		try {
		  //不是公司内部员工不能操作
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			TInsurance insurance = proxyService.getById(TInsurance.class, id);
			if(insurance==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			if(insurance.getStatus()!=Constants.InsuranceStatus.checking.getStatus()){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			insurance.setStatus(Constants.InsuranceStatus.cancle.getStatus());
			proxyService.save(insurance);
			//退款
			tallyService.tally(insurance.getUserId(), 
					Short.valueOf(insurance.getUserType()),
					insurance.getPrice(), 
					-insurance.getCost(),
					insurance.getProxyUserId(),
					-insurance.getProxyPrice(), 
					insurance.getPoints(), 
					Constants.TradeType.turnOutInsurance.getType(),
					"保险单退款",
					insurance.getOrderNo(),
					false);
			info("撤单成功，单号："+insurance.getPolicyNo());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cancleDDInsurance", method = {RequestMethod.GET, RequestMethod.POST})
	public String cancleDDInsurance(HttpServletRequest request, HttpServletResponse response,
			long id
			) {
		try {
		  //不是公司内部员工不能操作
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			TInsurance insurance = proxyService.getById(TInsurance.class, id);
			if(insurance==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			if(insurance.getStatus()!=Constants.InsuranceStatus.success.getStatus()){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			
			Date now = new Date();
			Date onehour = new Date(now.getTime());
			if(DateUtil.getDateByStr(insurance.getDepartureDate()).before(onehour)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"启运时间已过，不能撤销！"));
				return NONE;
			}
			//取消赠送保险
			TInsurance ins = proxyService.getByParentId(insurance.getId());
			if(ins!=null){
				if(ins.getStatus()==Constants.InsuranceStatus.checking.getStatus()){
					ins.setStatus(Constants.InsuranceStatus.cancle.getStatus());
					proxyService.save(ins);
				}else if(ins.getStatus()==Constants.InsuranceStatus.success.getStatus()){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"赠送的人身意外险审核已通过，不能撤销！"));
					return NONE;
				}
			}
			String dDate = insurance.getDepartureDate();
			Date _dDate = DateUtil.getDateByStr(dDate);
			String date = DateUtil.getStrYMDByDate(_dDate);
			String hour = DateUtil.getStrHHByDate(_dDate);
			String insuranceInfo = CICCUtils.deleteStr(insurance.getPolicyNoLong(),date,hour);
			String ciccRet = CICCUtils.sendInsurance(insuranceInfo, CICCUtils.HTTPS_URL_DEL);
			String s = CICCUtils.getStatus(ciccRet);//投单号
			if(s==null){
				String errMessage = CICCUtils.getReturnMessage(ciccRet);
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,errMessage));
				return NONE;
			}else{
				//再提交一次核保
				String endorseNo = CICCUtils.getEndorseNo(ciccRet);
				String subStr = CICCUtils.SubmitStr(endorseNo);
				ciccRet = CICCUtils.sendInsurance(subStr, CICCUtils.HTTPS_URL_SUB);
				s = CICCUtils.getStatus(ciccRet);//投单号
				if(s==null){
					String errMessage = CICCUtils.getReturnMessage(ciccRet);
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,errMessage));
					return NONE;
				}else{
					//退款
					tallyService.tally(insurance.getUserId(), 
							Short.valueOf(insurance.getUserType()),
							insurance.getPrice(), 
							-insurance.getCost(),
							insurance.getProxyUserId(),
							-insurance.getProxyPrice(), 
							insurance.getPoints(), 
							Constants.TradeType.turnOutInsurance.getType(),
							"保险单退款",
							insurance.getOrderNo(),
							false);
					insurance.setStatus(Constants.InsuranceStatus.cancle.getStatus());
					proxyService.save(insurance);
				}
			}
			info("撤单成功，单号："+insurance.getPolicyNo());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/idCardqueryOrderByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String idCardqueryOrderByPage(HttpServletRequest request, HttpServletResponse response,String orderNo,String status,String startTime,String endTime,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
				args.put("orderNo", orderNo);
			}
			if(!StringUtils.isBlank(status)){
				orderNo = URLDecoder.decode(status,"UTF-8"); 
				args.put("status", status);
			}
			if(!StringUtils.isBlank(startTime)){
				startTime = URLDecoder.decode(startTime,"UTF-8"); 
				args.put("startTime", startTime);
			}
			if(!StringUtils.isBlank(endTime)){
				endTime = DateUtil.getStrYMDByDate(DateUtil.addDay(DateUtil.getDateByStr(endTime), 1));
				args.put("endTime", endTime);
			}
			
			Page page = proxyService.idCardqueryOrderByPage(args, currentPage,  pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				IDCardQueryOrder c = (IDCardQueryOrder)sm;
				mo.put("id", c.getId());
				mo.put("orderNo", c.getOrderNo());
				mo.put("price", c.getPrice());
				mo.put("userType", Constants.UserType.getDescript(Short.valueOf(c.getUserType())));
				mo.put("status", Constants.GoodStatus.getEmunByStatus(c.getStatus()).getScript());
				mo.put("created_date", c.getCreated_date()==null?"":DateUtil.getStrYMDHMByDate(c.getCreated_date()));
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
	
	/*@Autowired
	private ISysTallyService sysTallyService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/pay", method = {RequestMethod.GET, RequestMethod.POST})
	public String pay(HttpServletRequest request, HttpServletResponse response,String insuranceId) {
		try {
			if(StringUtils.isBlank(insuranceId)){
				JSONHelper.returnInfo(JSONHelper.bean2json(Constants.NECESSARY_ARGS_BLANK_ERR));
				return NONE;
			}
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			
			TInsurance insurance = proxyService.getById(TInsurance.class,Long.valueOf(insuranceId));
			if(insurance!=null){
				sysTallyService.sysTally(insurance.getPrice(), Constants.UserType.sysUser.getType(), insurance.getOrderNo(), 
						user.getId(), Constants.TradeType.payCost.getZHName(), Constants.TradeType.payCost.getType(), true);
			}
			proxyService.save(insurance);
			JSONHelper.returnInfo(JSONHelper.appCode2json(Constants.OPERATION_SUCCESS));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.appCode2json(Constants.OPERATION_FAILED));
			return NONE;
		}
    }*/
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/proxyConfigListByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String proxyConfigListByPage(HttpServletRequest request, HttpServletResponse response,String search,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
				args.put("search", search);
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if("A0001".equals(user.getDepartment())){
				args.put("proxyUserId", user.getDepartmentId()+"");
			}
			Page page = proxyService.proxyConfigListByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((Map)sm).get("id"));
				mo.put("path", ((Map)sm).get("action_url"));//访问路径
				mo.put("descript", ((Map)sm).get("descript"));//商品名称
				mo.put("price", ((Map)sm).get("price"));//折后价
				mo.put("type", ((Map)sm).get("type"));//商品类型
				mo.put("discount", ((Map)sm).get("discount"));//折扣率
				mo.put("discountPrice", ((Map)sm).get("discount_price"));//折后价
				mo.put("discountPoint", ((Map)sm).get("discount_points"));//折后所需要积分
				mo.put("proxyPrice",  ((Map)sm).get("proxy_price"));//成本价
				mo.put("proxyName", ((Map)sm).get("user_name"));
				if("A0001".equals(user.getDepartment())){
					mo.put("cost", ((Map)sm).get("cost"));//成本价
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
	@RequestMapping(value = "/proxyConfigSave", method = {RequestMethod.GET, RequestMethod.POST})
	public String proxyConfigSave(HttpServletRequest request, HttpServletResponse response,
			long configId,
			double truckPrice,
			double consignorPrice,
			double truckDiscountPoint,
			double consignorDiscountPoint
			) {
		try {
			BusinessRules dd = proxyService.getById(BusinessRules.class, Long.valueOf(configId));
			dd.setTruckPrice(truckPrice);
			dd.setConsignorPrice(consignorPrice);
			dd.setTruckDiscountPoint(truckDiscountPoint);
			dd.setConsignorDiscountPoint(consignorDiscountPoint);
			proxyService.save(dd);
			info("设置价格：用户名："+JSONObject.fromObject(dd));
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			error("设置价格失败！");
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
    
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryConfig", method = {RequestMethod.GET, RequestMethod.POST})
	public String queryConfig(HttpServletRequest request, HttpServletResponse response,String configId) {
		try {
			if(!StringUtils.isBlank(configId)){
				Map ret = new HashMap();
				BusinessRules dd= proxyService.getById(BusinessRules.class, Long.valueOf(configId));
				//加入代理区分
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				ret.put("business", dd);
				ret.put("user", user);
				JSONHelper.returnInfo(JSONHelper.bean2json(ret,true));
	    		return NONE;
			}
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/truckNum", method = {RequestMethod.GET, RequestMethod.POST})
	public String truckNum(HttpServletRequest request, HttpServletResponse response,String date) {
		try {
			if(!StringUtils.isBlank(date)){
				Thread.sleep(2000);
				Map ret = new HashMap();
				ret.put("count", 12);
				ret.put("other", 88);
				JSONHelper.returnInfo(JSONHelper.bean2json(ret,true));
	    		return NONE;
			}
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
	

}
