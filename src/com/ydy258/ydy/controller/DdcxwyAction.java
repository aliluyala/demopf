package com.ydy258.ydy.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.ydy258.ydy.entity.DdcxwyInsurance;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.service.ITallyService;
import com.ydy258.ydy.service.InsuranceService;
import com.ydy258.ydy.util.DateUtil;
import com.ydy258.ydy.util.JSONHelper;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.SettingUtils;
import com.ydy258.ydy.util.utils;



@Controller
@RequestMapping("/sys/insurance/")
public class DdcxwyAction  extends BaseFacade  { 
	
	final private Setting setting = SettingUtils.get();
	
	@Autowired
	private InsuranceService insuranceService;
	
	@Autowired
	private ITallyService tallyService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ddcxwyinsuranceList", method = {RequestMethod.GET, RequestMethod.POST})
	public String ddcxwyinsuranceList(HttpServletRequest request, HttpServletResponse response,
			String proxyName,
			String saleUserName,
			int currentPage) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if("A0001".equals(user.getDepartment())){
				where.put("proxyId",user.getDepartmentId()+"");
			}
			if(!StringUtils.isBlank(proxyName)){
				proxyName = URLDecoder.decode(proxyName,"UTF-8"); 
				where.put("proxyName",proxyName);
			}
			Page page = insuranceService.ddcxwyInsuranceByPage(where, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((DdcxwyInsurance)sm).getId());
				mo.put("orderNo", ((DdcxwyInsurance)sm).getOrderNo());
				mo.put("proxyId", ((DdcxwyInsurance)sm).getProxyId());
				mo.put("proxyName", ((DdcxwyInsurance)sm).getProxyName());
				mo.put("saleUserId", ((DdcxwyInsurance)sm).getSaleUserId());
				//mo.put("saleUserName", ((DdcxwyInsurance)sm).getSaleUserName());
				mo.put("xczPath", ((DdcxwyInsurance)sm).getXczPath());
				mo.put("bdPath", ((DdcxwyInsurance)sm).getBdPath());
				mo.put("createdDate", ((DdcxwyInsurance)sm).getCreatedDate()==null?"":DateUtil.getStrYMDHMByDate(((DdcxwyInsurance)sm).getCreatedDate()));
				mo.put("status", ((DdcxwyInsurance)sm).getStatus());
				mo.put("balance", ((DdcxwyInsurance)sm).getBalance());
				mo.put("name", ((DdcxwyInsurance)sm).getName());
				mo.put("mobile", ((DdcxwyInsurance)sm).getMobile());
				mo.put("idCard", ((DdcxwyInsurance)sm).getIdCard());
				mo.put("type", ((DdcxwyInsurance)sm).getType());
				mo.put("startTime", ((DdcxwyInsurance)sm).getStartTime()==null?"":DateUtil.getStrYMDByDate(((DdcxwyInsurance)sm).getStartTime()));
				mo.put("count", ((DdcxwyInsurance)sm).getCount());
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
	@RequestMapping(value = "/ddcxwyinsuranceListCheck", method = {RequestMethod.GET, RequestMethod.POST})
	public String ddcxwyinsuranceListCheck(HttpServletRequest request, HttpServletResponse response,
			String proxyName,
			String saleUserName,
			int currentPage) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if("A0001".equals(user.getDepartment())){
				where.put("proxyId",user.getDepartmentId()+"");
			}
			if(!StringUtils.isBlank(proxyName)){
				proxyName = URLDecoder.decode(proxyName,"UTF-8"); 
				where.put("proxyName",proxyName);
			}
			if(!StringUtils.isBlank(saleUserName)){
				saleUserName = URLDecoder.decode(saleUserName,"UTF-8"); 
				where.put("saleUserName",saleUserName);
			}
			Page page = insuranceService.ddcxwyInsuranceByPage(where, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((DdcxwyInsurance)sm).getId());
				mo.put("orderNo", ((DdcxwyInsurance)sm).getOrderNo());
				mo.put("proxyId", ((DdcxwyInsurance)sm).getProxyId());
				mo.put("proxyName", ((DdcxwyInsurance)sm).getProxyName());
				mo.put("saleUserId", ((DdcxwyInsurance)sm).getSaleUserId());
				//mo.put("saleUserName", ((DdcxwyInsurance)sm).getSaleUserName());
				mo.put("xczPath", ((DdcxwyInsurance)sm).getXczPath());
				mo.put("bdPath", ((DdcxwyInsurance)sm).getBdPath());
				mo.put("createdDate", ((DdcxwyInsurance)sm).getCreatedDate()==null?"":DateUtil.getStrYMDHMByDate(((DdcxwyInsurance)sm).getCreatedDate()));
				mo.put("status", ((DdcxwyInsurance)sm).getStatus());
				//mo.put("balance", ((DdcxwyInsurance)sm).getBalance());
				mo.put("name", ((DdcxwyInsurance)sm).getName());
				//mo.put("mobile", ((DdcxwyInsurance)sm).getMobile());
				mo.put("idCard", ((DdcxwyInsurance)sm).getIdCard());
				mo.put("type", ((DdcxwyInsurance)sm).getType());
				mo.put("startTime", ((DdcxwyInsurance)sm).getStartTime()==null?"":DateUtil.getStrYMDByDate(((DdcxwyInsurance)sm).getStartTime()));
				mo.put("count", ((DdcxwyInsurance)sm).getCount());
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
	@RequestMapping(value = "/save", method = {RequestMethod.GET, RequestMethod.POST})
	public String save(HttpServletRequest request,HttpServletResponse response,
			int count,
			int type,
			double balance,
			String name,
			String mobile,
			String idCard,
			String startTime
			){
		try {
			double price = 500;
			if(type==2){
				price=700;
			}else if(type==3){
				price=1000;
			}
			//检查金额
			if(price*count!=balance
					||StringUtils.isBlank(name)||
					StringUtils.isBlank(mobile)||
					StringUtils.isBlank(idCard)||
					startTime==null||StringUtils.isBlank(startTime)||
					DateUtil.getDateByStr(startTime).getTime()<new Date().getTime()
					){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			DdcxwyInsurance di = new DdcxwyInsurance();
			di.setBalance(balance);
			di.setCount(count);
			di.setType(type);
			di.setName(name);
			di.setMobile(mobile);
			di.setIdCard(idCard);
			di.setStartTime(DateUtil.getDateByStr(startTime));
			
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
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
					//记录上传过程起始时的时间，用来计算上传时间
					int pre = (int) System.currentTimeMillis();
					//取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());
					if(file != null){
						//取得当前上传文件的文件名称
						String myFileName = file.getOriginalFilename();
						//如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if(myFileName.trim() !=""){
							// 重命名上传后的文件名
							String contentType = file.getContentType();
							if (!contentType.equals("image/jpeg")
									&& !contentType.equals("image/png")
									&& !contentType.equals("application/pdf")) {
								JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"请上传pdf、png、jpg格式文件"));
								return NONE;
							}
							// 取得文件后缀
							String appden = myFileName.substring(myFileName.lastIndexOf("."));
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
							String orderNo = utils.generaterOrderNumber();
							//扣除费用
							tallyService.balance4SysUserTally(
									orderNo,
									user.getId(), 
									user.getUserName(),
									user.getId(), 
									user.getUserName(), 
									102, 
									"购买大地保险车险", 
									-balance, 
									true);
							di.setXczPath(picUrl);
							di.setOrderNo(orderNo);
							di.setCreatedDate(new Date());
							di.setProxyId(user.getDepartmentId());
							di.setProxyName(user.getUserName());
							di.setSaleUserId(user.getId());
							di.setSaleUserName(user.getUserName());
							di.setStatus(Constants.InsuranceStatus.checking.getStatus());
							insuranceService.save(di);
						}
					}
				}
			}
			info("新增大地保险车行无忧 订单号："+di.getOrderNo());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,e.getMessage()));
			return NONE;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/update", method = {RequestMethod.GET, RequestMethod.POST})
	public String update(HttpServletRequest request,HttpServletResponse response,long id){
		try {
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			
			if(!"B0001".equals(user.getDepartment())){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.no_permission_code,Constants.no_permission_msg));
				return NONE;
			}
			DdcxwyInsurance di = insuranceService.getById(DdcxwyInsurance.class, id);
			if(di.getStatus()==Constants.InsuranceStatus.success.getStatus()){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.no_permission_code,Constants.no_permission_msg));
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
						String contentType = file.getContentType();
						if (!contentType.equals("image/jpeg")
								&& !contentType.equals("image/png")
								&& !contentType.equals("application/pdf")) {
							JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"请上传pdf、png、jpg格式文件"));
							return NONE;
						}
						//取得当前上传文件的文件名称
						String myFileName = file.getOriginalFilename();
						//如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if(myFileName.trim() !=""){
							// 重命名上传后的文件名
							// 取得文件后缀
							String appden = myFileName.substring(myFileName
									.lastIndexOf("."));
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
							
							di.setBdPath(picUrl);
							di.setStatus(Constants.InsuranceStatus.success.getStatus());
							insuranceService.save(di);
						}
					}
				}
			}
			info("结算大地保险车行无忧,订单号："+di.getOrderNo());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
	
}
