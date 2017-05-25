

package com.ydy258.ydy.controller;


import java.io.File;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
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

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.Setting;
import com.ydy258.ydy.entity.Consignor;
import com.ydy258.ydy.entity.Feedback;
import com.ydy258.ydy.entity.FreightLine;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.TCargoTransactionStatements;
import com.ydy258.ydy.entity.TDriverTransactionStatements;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.entity.Warehouse;
import com.ydy258.ydy.service.ICommDataService;
import com.ydy258.ydy.service.IMemberService;
import com.ydy258.ydy.service.ISysUserService;
import com.ydy258.ydy.service.ITallyService;
import com.ydy258.ydy.util.DateUtil;
import com.ydy258.ydy.util.JSONHelper;
import com.ydy258.ydy.util.MD5;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.ParameterUtils;
import com.ydy258.ydy.util.SettingUtils;
import com.ydy258.ydy.util.StringUtil;

@Controller
@RequestMapping("/sys/member/")
public class MemberAction extends BaseFacade  {
	
	final private Setting setting = SettingUtils.get();
	
	@Autowired
	private IMemberService memberService;
	
	@Autowired
	private ICommDataService commDataService;
	
	
	@Autowired
	private ITallyService tallyService;
	
	@Autowired
	private ISysUserService sysUserService;
	
	
	/** servletContext */
	private ServletContext servletContext;	
	
	@Override
	public void setServletContext(ServletContext ctx) {
		// TODO Auto-generated method stub
		this.servletContext = ctx;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/truckUserByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String truckUserByPage(HttpServletRequest request, HttpServletResponse response,String search,String checkStatus,String date,String phone,String code,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
				args.put("search", search);
			}
			if(!StringUtils.isBlank(phone)){
				phone = URLDecoder.decode(phone,"UTF-8"); 
				args.put("phone", phone);
			}
			if(!StringUtils.isBlank(code)){
				code = URLDecoder.decode(code,"UTF-8"); 
				args.put("code", code);
			}
			if(!StringUtils.isBlank(checkStatus)){
				args.put("checkStatus", checkStatus);
			}
			if(!StringUtils.isBlank(date)){
				args.put("date", date);
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if("A0001".equals(user.getDepartment())){
				args.put("proxyUserId", user.getDepartmentId()+"");
				//args.put("tellerId", value);
			}
			Page page = memberService.truckUserByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((Truck)sm).getId());
				mo.put("driverName", ((Truck)sm).getDriverName());
				mo.put("mobile", ((Truck)sm).getMobile());
				mo.put("plateNumber", ((Truck)sm).getPlateNumber());
				mo.put("idCardNumber", ((Truck)sm).getIdCardNumber());
				mo.put("drivingLicenseNumber", ((Truck)sm).getDrivingLicenseNumber());
				mo.put("drivingLicensePic", ((Truck)sm).getDrivingLicensePic());
				mo.put("registrationNumber", ((Truck)sm).getRegistrationNumber());
				mo.put("registrationPic", ((Truck)sm).getRegistrationPic());
				mo.put("lockedDate", ((Truck)sm).getLockedDate()==null?"":DateUtil.getStrYMDHMByDate(((Truck)sm).getLockedDate()));
				mo.put("loginDate", ((Truck)sm).getLoginDate()==null?"":DateUtil.getStrYMDHMByDate(((Truck)sm).getLoginDate()));
				mo.put("loginIp", ((Truck)sm).getLoginIp());
				mo.put("isCheck", ((Truck)sm).getIsCheck());
				mo.put("isLocked", ((Truck)sm).getIsLocked());
				mo.put("truckType", ((Truck)sm).getTcd().getDataName());
				mo.put("deadWeight", ((Truck)sm).getDeadWeight());
				mo.put("balance", ((Truck)sm).getBalance());
				mo.put("loadStatus", ((Truck)sm).getLoadStatus());
				mo.put("authStep", ((Truck)sm).getAuthStep());
				mo.put("points", ((Truck)sm).getPoints());
				mo.put("gsCard", ((Truck)sm).getGsCard());
				mo.put("jyCard", ((Truck)sm).getJyCard());
				mo.put("czCard", ((Truck)sm).getCzCard());
				mo.put("tellerId", ((Truck)sm).getTellerId());
				mo.put("proxy", ((Truck)sm).getProxyUserId()==null?"":((Truck)sm).getProxyUserId().getUserName());
				mo.put("expireTime",((Truck)sm).getExpireTime()==null?"":DateUtil.getStrYMDHMByDate(((Truck)sm).getExpireTime()));
				mo.put("registerTime",((Truck)sm).getRegisterTime()==null?"":DateUtil.getStrYMDHMByDate(((Truck)sm).getRegisterTime()));
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.getTotalCount()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/findTruckInfo", method = {RequestMethod.GET, RequestMethod.POST})
	public String findTruckInfo(HttpServletRequest request, HttpServletResponse response,String truckId) {
		try {
			if(StringUtils.isBlank(truckId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Truck truck = memberService.getById(Truck.class,Long.valueOf(truckId));
			JSONHelper.returnInfo(JSONHelper.bean2json(truck));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/trucklocked", method = {RequestMethod.GET, RequestMethod.POST})
	public String unlocked(HttpServletRequest request, HttpServletResponse response,String truckId) {
		try {
			if(StringUtils.isBlank(truckId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Truck truck = memberService.getById(Truck.class,Long.valueOf(truckId));
			if(truck.getIsLocked()){
				truck.setIsLocked(false);
			}else{
				truck.setIsLocked(true);
			}
			memberService.save(truck);
			info("锁定车主"+truck.getMobile());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkedTruckUser", method = {RequestMethod.GET, RequestMethod.POST})
	public String checkedUser(HttpServletRequest request, HttpServletResponse response,
			String truckId,
			String idCardNumber,
			String brandType,
			String truckIDCode,
			String motorCode,
			String registerDate,
			String dispCardDate,
			String registrationNumber,
			String drivingLicenseNumber,
			String checked,
			String tips,
			String gsCard,
			String jyCard,
			String czCard) {
		try {
			String sendMsg = "";
			if(StringUtils.isBlank(truckId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Truck truck = memberService.getById(Truck.class,Long.valueOf(truckId));
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(!Boolean.valueOf(checked)&&!user.getDepartment().equals("B0001")&&truck.getAuthStep()==3){//只有公司内部人员才有权限
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,"不是待审核状态不能审核"));
				return NONE;
			}
			if(!StringUtils.isBlank(idCardNumber)){
				truck.setIdCardNumber(idCardNumber);
			}
			if(!StringUtils.isBlank(brandType)){
				truck.setBrandType(brandType);
			}
			if(!StringUtils.isBlank(motorCode)){
				truck.setMotorCode(motorCode);
			}
			if(!StringUtils.isBlank(truckIDCode)){
				truck.setTruckIDCode(truckIDCode);
			}
			if(!StringUtils.isBlank(registerDate)){
				truck.setRegisterDate(registerDate);
			}
			if(!StringUtils.isBlank(dispCardDate)){
				truck.setDispCardDate(dispCardDate);
			}
			if(!StringUtils.isBlank(drivingLicenseNumber)){
				truck.setDrivingLicenseNumber(drivingLicenseNumber);
			}
			if(!StringUtils.isBlank(registrationNumber)){
				truck.setRegistrationNumber(registrationNumber);
			}
			if(!StringUtils.isBlank(checked)){
				truck.setIsCheck(Boolean.valueOf(checked));
			}
			if(!StringUtils.isBlank(gsCard)){
				truck.setGsCard(gsCard);
			}
			if(!StringUtils.isBlank(jyCard)){
				truck.setJyCard(jyCard);
			}
			if(!StringUtils.isBlank(czCard)){
				truck.setCzCard(czCard);
			}
			String idCardPic = null;			//身份证照片
			String registrationPic = null;		//行驶证照片
			String drivingLicensePic = null;	//驾驶证照片
			String driverPic = null;			//司机本人近照
			String truckPic = null;				//货车照片
			
			String idCardPicUrl = "";			//身份证照片url
			String registrationPicUrl = "";		//行驶证照片url
			String drivingLicensePicUrl = "";	//驾照照片url
			String driverPicUrl = "";			//司机照片url
			String truckPicUrl = "";			//车辆照片url
			
			
			idCardPic = ParameterUtils.getString(request, "idCardPic");
			registrationPic = ParameterUtils.getString(request, "registrationPic");
			drivingLicensePic = ParameterUtils.getString(request, "drivingLicensePic");
			driverPic = ParameterUtils.getString(request, "driverPic");
			truckPic = ParameterUtils.getString(request, "truckPic");
				
			// 创建一个通用的多部分解析器
			//ServletContext ctx = request.getSession().getServletContext();
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 保存上传的图片
				// 取得request中的所有文件名
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());

					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					String contentType = file.getContentType();
					if (contentType.equals("image/pjpeg")
							|| contentType.equals("image/gif")
							|| contentType.equals("image/jpeg")
							|| contentType.equals("image/png")
							|| contentType.equals("application/octet-stream")) {
						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (myFileName.trim() != "") {									
							// 重命名上传后的文件名
							// 取得文件后缀
							String appden = myFileName.substring(myFileName.lastIndexOf("."));
							//产生目录名
							String dir = DateUtil.dateToShortCode();
							//产生新文件名
							String serverFileName = UUID.randomUUID().toString();
							String path = servletContext.getRealPath("/").replace("ydypf", "hyt") + "uploadfiles" + File.separator + dir + File.separator;
							File f = new File(path);
							if (!f.exists())
								f.mkdirs();
							String imgName = path + serverFileName + appden;

							// 定义上传路径
							File localFile = new File(imgName);
							file.transferTo(localFile);
							logger.debug(localFile);
							
							if(myFileName.equals(idCardPic))									
								idCardPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(myFileName.equals(registrationPic))
								registrationPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(myFileName.equals(drivingLicensePic))
								drivingLicensePicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(myFileName.equals(driverPic))
								driverPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(myFileName.equals(truckPic))
								truckPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
						}								
					} 
				} // end while
				//更新数据
				if(!StringUtils.isBlank(idCardPicUrl)){
					truck.setIdCardPic(idCardPicUrl);
				}
				if(!StringUtils.isBlank(registrationPicUrl)){
					truck.setRegistrationPic(registrationPicUrl);
				}
				if(!StringUtils.isBlank(drivingLicensePicUrl)){
					truck.setDrivingLicensePic(drivingLicensePicUrl);
				}
				if(!StringUtils.isBlank(driverPicUrl)){
					truck.setDriverPic(driverPicUrl);
				}
				if(!StringUtils.isBlank(truckPicUrl)){
					truck.setTruckPic(truckPicUrl);	
				}
			}
			if(Boolean.valueOf(checked)){
				if(truck.getIsFirstCheck()==null||truck.getIsFirstCheck()==false){
//					//平稳货主账号到sys_user
//					SysUser sysuser = new SysUser();
//					sysuser.setUserName(truck.getMobile());
//					sysuser.setPassword(truck.getPassword());
//					sysuser.setDepartment("D0001");
//					sysuser.setDepartmentId(truck.getId());
//					sysuser.setCreatedDate(truck.getRegisterTime());
//					sysuser.setPosition("车主");
//					sysuser.setRealName(truck.getDriverName());
//					sysuser.setStatus("effective");
//					//设定角色
//					//SysRole role = memberService.getById(SysRole.class, 2436L);
//					SysRole role = sysUserService.getRoleByName("车主");
//					Set<SysRole> roles = new HashSet<SysRole>();
//					roles.add(role);
//					sysuser.setRoles(roles);
//					memberService.save(sysuser);
					truck.setIsFirstCheck(true);
					if(setting.getTruckregisterpoints()>0){
						tallyService.addPointsTally(truck.getId(), Constants.UserType.TruckUser.getType(), setting.getTruckregisterpoints());
					}
				}
				truck.setAuthStep(3);//0：注册状态，1，提交基本资料，2提交基本资料，3审核成功状态
				sendMsg = "尊敬的用户，恭喜您已经通过我们的审核";
			}else{
				//删除位置信息
				memberService.deleteByTruckId(truck.getId());
				truck.setAuthStep(0);//0：注册状态，1，提交基本资料，2提交基本资料，3审核成功状态
				if("1".equals(tips)){
					sendMsg = "尊敬的用户，您上传的资料不全，不能通过我们的审核";
				}else if("2".equals(tips)){
					sendMsg = "尊敬的用户，您上传的资料不清晰，不能通过我们的审核";
				}else{
					sendMsg = "尊敬的用户，您上传的资料不正确，不能通过我们的审核";
				}
			}
			memberService.save(truck);
			
			commDataService.push2Truck(truck.getJpushId(), sendMsg,truck.getAuthStep(),Boolean.valueOf(checked),Integer.valueOf(truck.getMobileType()));
			info("审核车主"+truck.getMobile());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			info("审核车主失败 ID"+truckId);
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/modifyTruckUser", method = {RequestMethod.GET, RequestMethod.POST})
	public String modifyTruckUser(HttpServletRequest request, HttpServletResponse response,
			String truckId
			) {
		try {
			if(StringUtils.isBlank(truckId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Truck truck = memberService.getById(Truck.class,Long.valueOf(truckId));
			String idCardPic = null;			//身份证照片
			String registrationPic = null;		//行驶证照片
			String drivingLicensePic = null;	//驾驶证照片
			String driverPic = null;			//司机本人近照
			String truckPic = null;				//货车照片
			
			String idCardPicUrl = "";			//身份证照片url
			String registrationPicUrl = "";		//行驶证照片url
			String drivingLicensePicUrl = "";	//驾照照片url
			String driverPicUrl = "";			//司机照片url
			String truckPicUrl = "";			//车辆照片url
			
			
			idCardPic = ParameterUtils.getString(request, "idCardPic");
			registrationPic = ParameterUtils.getString(request, "registrationPic");
			drivingLicensePic = ParameterUtils.getString(request, "drivingLicensePic");
			driverPic = ParameterUtils.getString(request, "driverPic");
			truckPic = ParameterUtils.getString(request, "truckPic");
				
			// 创建一个通用的多部分解析器
			//ServletContext ctx = request.getSession().getServletContext();
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 保存上传的图片
				// 取得request中的所有文件名
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());

					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					String contentType = file.getContentType();
					if (contentType.equals("image/pjpeg")
							|| contentType.equals("image/gif")
							|| contentType.equals("image/jpeg")
							|| contentType.equals("image/png")
							|| contentType.equals("application/octet-stream")) {
						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (myFileName.trim() != "") {									
							// 重命名上传后的文件名
							// 取得文件后缀
							String appden = myFileName.substring(myFileName.lastIndexOf("."));
							//产生目录名
							String dir = DateUtil.dateToShortCode();
							//产生新文件名
							String serverFileName = UUID.randomUUID().toString();
							String path = servletContext.getRealPath("/").replace("ydypf", "hyt") + "uploadfiles" + File.separator + dir + File.separator;
							File f = new File(path);
							if (!f.exists())
								f.mkdirs();
							String imgName = path + serverFileName + appden;

							// 定义上传路径
							File localFile = new File(imgName);
							file.transferTo(localFile);
							logger.debug(localFile);
							
							if(myFileName.equals(idCardPic))									
								idCardPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(myFileName.equals(registrationPic))
								registrationPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(myFileName.equals(drivingLicensePic))
								drivingLicensePicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(myFileName.equals(driverPic))
								driverPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(myFileName.equals(truckPic))
								truckPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
						}								
					} 
				} // end while
				//更新数据
				if(!StringUtils.isBlank(idCardPicUrl)){
					truck.setIdCardPic(idCardPicUrl);
				}
				if(!StringUtils.isBlank(registrationPicUrl)){
					truck.setRegistrationPic(registrationPicUrl);
				}
				if(!StringUtils.isBlank(drivingLicensePicUrl)){
					truck.setDrivingLicensePic(drivingLicensePicUrl);
				}
				if(!StringUtils.isBlank(driverPicUrl)){
					truck.setDriverPic(driverPicUrl);
				}
				if(!StringUtils.isBlank(truckPicUrl)){
					truck.setTruckPic(truckPicUrl);	
				}
			}
			memberService.save(truck);
			info("修改车主"+truck.getMobile());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			info("修改车主 ID"+truckId);
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/sendMsg2Consignor", method = {RequestMethod.GET, RequestMethod.POST})
	public String sendMsg2Consignor(HttpServletRequest request, HttpServletResponse response,
			String truckId,
			String checked,
			String msg) {
		try {
			if(StringUtils.isBlank(truckId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Consignor consignor = memberService.getById(Consignor.class,Long.valueOf(truckId));
			//发信息 提醒用户审核完成
			commDataService.push2Consignor(consignor.getJpushId(), msg, 1,Boolean.valueOf(checked));
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/sendMsg2Truck", method = {RequestMethod.GET, RequestMethod.POST})
	public String sendMsg2Truck(HttpServletRequest request, HttpServletResponse response,
			String truckId,
			String checked,
			String msg) {
		try {
			if(StringUtils.isBlank(truckId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Truck truck = memberService.getById(Truck.class,Long.valueOf(truckId));
			//发信息 提醒用户审核完成
			commDataService.push2Truck(truck.getJpushId(), msg, 2,Boolean.valueOf(checked));
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editTruckUser", method = {RequestMethod.GET, RequestMethod.POST})
	public String editUser(HttpServletRequest request, HttpServletResponse response,
			String truckId,
			String driverName,
			String mobile,
			String plateNumber,
			String drivingLicenseNumber
			) {
		try {
			if(StringUtils.isBlank(truckId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Truck t = memberService.getByPlateNumber(plateNumber);
			if(t!=null&&!truckId.equals(t.getId()+"")){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"车牌号不能重复"));
				return NONE;
			}
			
			Truck t1 = memberService.getByMobile(mobile);
			if(t1!=null&&!truckId.equals(t1.getId()+"")){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"手机号不能重复"));
				return NONE;
			}
			Truck truck = memberService.getById(Truck.class,Long.valueOf(truckId));
			if(!StringUtils.isBlank(driverName)){
				truck.setDriverName(driverName);
			}
			if(!StringUtils.isBlank(mobile)){
				truck.setMobile(mobile);
			}
			if(!StringUtils.isBlank(plateNumber)){
				truck.setPlateNumber(plateNumber);
			}
			if(!StringUtils.isBlank(drivingLicenseNumber)){
				truck.setDrivingLicenseNumber(drivingLicenseNumber);
			}
			memberService.save(truck);
			info("编辑车主"+truck.getMobile());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/struckStatements", method = {RequestMethod.GET, RequestMethod.POST})
	public String statements(HttpServletRequest request, HttpServletResponse response,String truckId) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(truckId)){
				args.put("truckId", truckId);
			}
			List<TDriverTransactionStatements> list = memberService.truckUserStatements(args);
			List<Map> retlist = new ArrayList<Map>();
			for(TDriverTransactionStatements sm:list){
				Map ret = new HashMap();
				ret.put("id", sm.getId());
				ret.put("accountId", sm.getAccountId());
				ret.put("pay", sm.getPay());
				ret.put("payType",sm.getPayType()==null||sm.getPayType()==1?"余额":"微信");
				ret.put("deposit", sm.getDeposit());
				ret.put("balance", sm.getBalance());
				ret.put("tradeType", sm.getTradeType());
				ret.put("points", sm.getPayPoints());
				ret.put("restPoints", sm.getRestPoints());
				ret.put("remark", sm.getRemark());
				ret.put("createdDate", DateUtil.getStrYMDHMSByDate(sm.getCreatedDate()));
				retlist.add(ret);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(retlist));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/consignorCompanyByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String consignorCompanyByPage(HttpServletRequest request, HttpServletResponse response,String search,String checkStatus,String date,String phone,String code,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
				args.put("search", search);
			}
			if(!StringUtils.isBlank(phone)){
				phone = URLDecoder.decode(phone,"UTF-8"); 
				args.put("phone", phone);
			}
			if(!StringUtils.isBlank(code)){
				code = URLDecoder.decode(code,"UTF-8"); 
				args.put("code", code);
			}
			if(!StringUtils.isBlank(checkStatus)){
				args.put("checkStatus", checkStatus);
			}
			if(!StringUtils.isBlank(date)){
				args.put("date", date);
			}
			args.put("userType", "1");
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if("A0001".equals(user.getDepartment())){
				args.put("proxyUserId", user.getDepartmentId()+"");
			}
			
			Page page = memberService.consignorUserByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				Consignor c = (Consignor)sm;
				mo.put("id", c.getId());
				mo.put("realName", c.getRealName());
				mo.put("mobile", c.getMobile());
				mo.put("idCard", c.getIdCard());
				mo.put("isLocked", c.getIsLocked());
				mo.put("consignorType", c.getCargoType());
				mo.put("loginDate", c.getLoginDate()==null?"":DateUtil.getStrYMDHMByDate(c.getLoginDate()));
				mo.put("loginIp", c.getLoginIp());
				mo.put("cities", c.getCities());
				mo.put("address", c.getAddress());
				//保留两位小数
				double   f   =   c.getBalance();  
				BigDecimal   b   =   new   BigDecimal(f);  
				double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
				
				mo.put("balance", f1);
				mo.put("isAuth", c.getIsAuth());
				mo.put("fax", c.getFax());
				mo.put("companyName", c.getCompanyName());
				mo.put("registerTime", c.getRegisterTime()==null?"":DateUtil.getStrYMDHMByDate(c.getRegisterTime()));
				mo.put("isPerfect", c.getIsPerfect());
				mo.put("authStep", c.getAuthStep());
				mo.put("points", c.getPoints());
				mo.put("tellerId", c.getTellerId());
				mo.put("proxy", c.getProxyUserId()==null?"":c.getProxyUserId().getUserName());
				mo.put("expireTime",c.getExpireTime()==null?"":DateUtil.getStrYMDHMByDate(c.getExpireTime()));
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.getTotalCount()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/freightLineList", method = {RequestMethod.GET, RequestMethod.POST})
	public String freightLineList(HttpServletRequest request, HttpServletResponse response,String userId) {
		try {
			Map args = new HashMap<String,String>();
			args.put("userId", userId);
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			List<FreightLine> page = memberService.consignorCompanyByPage(args);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page){
				Map mo = new HashMap();
				FreightLine c = (FreightLine)sm;
				mo.put("id", c.getId());
				mo.put("userId", c.getUserId());
				mo.put("src", c.getOrigin());
				mo.put("phone", c.getPhone());
				mo.put("desc", c.getDestination());
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/warehouseList", method = {RequestMethod.GET, RequestMethod.POST})
	public String warehouseList(HttpServletRequest request, HttpServletResponse response,String userId) {
		try {
			Map args = new HashMap<String,String>();
			args.put("userId", userId);
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			List<Warehouse> page = memberService.warehouseByPage(args);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page){
				Map mo = new HashMap();
				Warehouse c = (Warehouse)sm;
				mo.put("id", c.getId());
				mo.put("userId", c.getUserId());
				mo.put("place", c.getPlace());
				mo.put("contact", c.getContact());
				mo.put("phone", c.getPhone());
				mo.put("address", c.getAddress());
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addFreightLine", method = {RequestMethod.GET, RequestMethod.POST})
	public String addFreightLine(HttpServletRequest request, HttpServletResponse response,long userId,String parentId,String sonId,String parentId1,String sonId1,String phone) {
		try {
			sonId1 = sonId1.replace("，", ",");
			sonId1 = sonId1.replace(" ", "");
			phone = phone.replace(" ", "");
			phone = phone.replace("，", ",");
			sonId = sonId.replace("，", ",");
			sonId = sonId.replace(" ", "");
			FreightLine f = new FreightLine();
			f.setUserId(userId);
			f.setDestination(sonId1);
			f.setPhone(phone);
			f.setOrigin(sonId);
			memberService.save(f);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addWarehouse", method = {RequestMethod.GET, RequestMethod.POST})
	public String addWarehouse(HttpServletRequest request, HttpServletResponse response,long userId, String sonId,String contact,String phone,String address) {
		try {
			sonId = sonId.replace("，", ",");
			sonId = sonId.replace(" ", "");
			Warehouse f = new Warehouse();
			f.setUserId(userId);
			f.setAddress(address);
			f.setContact(contact);
			f.setPhone(phone);
			f.setPlace(sonId);
			String pic1 = "pic1";			
			String pic2 = "pic2";			
			String pic3 = "pic3";			
			String pic4 = "pic4";			
			String pic5 = "pic5";			
				// 创建一个通用的多部分解析器
				//ServletContext ctx = request.getSession().getServletContext();
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);
			Map sbpic = new HashMap();
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 保存上传的图片
				// 取得request中的所有文件名
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 取得上传文件
					CommonsMultipartFile  file = (CommonsMultipartFile)multiRequest.getFile(iter.next());
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					String contentType = file.getContentType();
					String fName = file.getFileItem().getFieldName();
					if (contentType.equals("image/pjpeg")
							|| contentType.equals("image/gif")
							|| contentType.equals("image/jpeg")
							|| contentType.equals("image/png")
							|| contentType.equals("application/octet-stream")) {
						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (myFileName.trim() != "") {									
							// 重命名上传后的文件名
							// 取得文件后缀
							String appden = myFileName.substring(myFileName.lastIndexOf("."));
							//产生目录名
							String dir = DateUtil.dateToShortCode();
							//产生新文件名
							String serverFileName = UUID.randomUUID().toString();
							String path = servletContext.getRealPath("/").replace("ydypf", "hyt") + "uploadfiles" + File.separator + dir + File.separator;
							File fn = new File(path);
							if (!fn.exists())
								fn.mkdirs();
							String imgName = path + serverFileName + appden;

							logger.debug(imgName);
							// 定义上传路径
							File localFile = new File(imgName);
							file.transferTo(localFile);
							if(fName.equals(pic1))
								sbpic.put(pic1, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic2))
								sbpic.put(pic2, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic3))
								sbpic.put(pic3, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic4))
								sbpic.put(pic4, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic5))
								sbpic.put(pic5, "uploadfiles/" + dir + "/" + serverFileName + appden);
							
						}								
					}
				} // end while
				if(!StringUtils.isBlank(sbpic.toString())){
					f.setPicPath(JSONObject.fromObject(sbpic).toString());
				}
			}
			memberService.save(f);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/findHouseInfo", method = {RequestMethod.GET, RequestMethod.POST})
	public String findHouseInfo(HttpServletRequest request, HttpServletResponse response,String houseId) {
		try {
			if(StringUtils.isBlank(houseId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Warehouse consignor = memberService.getById(Warehouse.class,Long.valueOf(houseId));
			JSONHelper.returnInfo(JSONHelper.bean2json(consignor));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editWarehouse", method = {RequestMethod.GET, RequestMethod.POST})
	public String editWarehouse(HttpServletRequest request, HttpServletResponse response,long houseId,long userId, String sonId,String contact,String phone,String address) {
		try {
			Warehouse f = memberService.getById(Warehouse.class, houseId);
			sonId = sonId.replace("，", ",");
			sonId = sonId.replace(" ", "");
			f.setUserId(userId);
			f.setAddress(address);
			f.setContact(contact);
			f.setPhone(phone);
			f.setPlace(sonId);
			String pic1 = "pic1";			
			String pic2 = "pic2";			
			String pic3 = "pic3";			
			String pic4 = "pic4";			
			String pic5 = "pic5";			
				// 创建一个通用的多部分解析器
				//ServletContext ctx = request.getSession().getServletContext();
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);
			Map sbpic = new HashMap();
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 保存上传的图片
				// 取得request中的所有文件名
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 取得上传文件
					CommonsMultipartFile  file = (CommonsMultipartFile)multiRequest.getFile(iter.next());
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					String contentType = file.getContentType();
					String fName = file.getFileItem().getFieldName();
					if (contentType.equals("image/pjpeg")
							|| contentType.equals("image/gif")
							|| contentType.equals("image/jpeg")
							|| contentType.equals("image/png")
							|| contentType.equals("application/octet-stream")) {
						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (myFileName.trim() != "") {									
							// 重命名上传后的文件名
							// 取得文件后缀
							String appden = myFileName.substring(myFileName.lastIndexOf("."));
							//产生目录名
							String dir = DateUtil.dateToShortCode();
							//产生新文件名
							String serverFileName = UUID.randomUUID().toString();
							String path = servletContext.getRealPath("/").replace("ydypf", "hyt") + "uploadfiles" + File.separator + dir + File.separator;
							File fn = new File(path);
							if (!fn.exists())
								fn.mkdirs();
							String imgName = path + serverFileName + appden;

							logger.debug(imgName);
							// 定义上传路径
							File localFile = new File(imgName);
							file.transferTo(localFile);
							if(fName.equals(pic1))
								sbpic.put(pic1, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic2))
								sbpic.put(pic2, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic3))
								sbpic.put(pic3, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic4))
								sbpic.put(pic4, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic5))
								sbpic.put(pic5, "uploadfiles/" + dir + "/" + serverFileName + appden);
						}								
					}
				} // end while
				if(!StringUtils.isBlank(sbpic.toString())){
					f.setPicPath(JSONObject.fromObject(sbpic).toString());
				}
			}
			memberService.save(f);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editFreightLine", method = {RequestMethod.GET, RequestMethod.POST})
	public String editFreightLine(HttpServletRequest request, HttpServletResponse response,long id,String src,String desc) {
		try {
			FreightLine f = memberService.getById(FreightLine.class, id);
			f.setDestination(desc);
			f.setOrigin(src);
			memberService.save(f);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteFreightLine", method = {RequestMethod.GET, RequestMethod.POST})
	public String editFreightLine(HttpServletRequest request, HttpServletResponse response,long id) {
		try {
			memberService.delete(FreightLine.class, id);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteWarehouse", method = {RequestMethod.GET, RequestMethod.POST})
	public String deleteWarehouse(HttpServletRequest request, HttpServletResponse response,long id) {
		try {
			memberService.delete(Warehouse.class, id);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }

	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/consignorUserByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String consignorUserByPage(HttpServletRequest request, HttpServletResponse response,String search,String checkStatus,String date,String phone,String code,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
				args.put("search", search);
			}
			if(!StringUtils.isBlank(phone)){
				phone = URLDecoder.decode(phone,"UTF-8"); 
				args.put("phone", phone);
			}
			if(!StringUtils.isBlank(code)){
				code = URLDecoder.decode(code,"UTF-8"); 
				args.put("code", code);
			}
			if(!StringUtils.isBlank(checkStatus)){
				args.put("checkStatus", checkStatus);
			}
			if(!StringUtils.isBlank(date)){
				args.put("date", date);
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if("A0001".equals(user.getDepartment())){
				args.put("proxyUserId", user.getDepartmentId()+"");
			}
			
			Page page = memberService.consignorUserByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				Consignor c = (Consignor)sm;
				mo.put("id", c.getId());
				mo.put("realName", c.getRealName());
				mo.put("mobile", c.getMobile());
				mo.put("idCard", c.getIdCard());
				mo.put("isLocked", c.getIsLocked());
				mo.put("consignorType", c.getCargoType());
				mo.put("loginDate", c.getLoginDate()==null?"":DateUtil.getStrYMDHMByDate(c.getLoginDate()));
				mo.put("loginIp", c.getLoginIp());
				mo.put("cities", c.getCities());
				mo.put("address", c.getAddress());
				//保留两位小数
				double   f   =   c.getBalance();  
				BigDecimal   b   =   new   BigDecimal(f);  
				double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();  
				
				mo.put("balance", f1);
				mo.put("isAuth", c.getIsAuth());
				mo.put("fax", c.getFax());
				mo.put("registerTime", c.getRegisterTime()==null?"":DateUtil.getStrYMDHMByDate(c.getRegisterTime()));
				mo.put("isPerfect", c.getIsPerfect());
				mo.put("authStep", c.getAuthStep());
				mo.put("points", c.getPoints());
				mo.put("tellerId", c.getTellerId());
				mo.put("proxy", c.getProxyUserId()==null?"":c.getProxyUserId().getUserName());
				mo.put("expireTime",c.getExpireTime()==null?"":DateUtil.getStrYMDHMByDate(c.getExpireTime()));
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.getTotalCount()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/findConsignorInfo", method = {RequestMethod.GET, RequestMethod.POST})
	public String findConsignorInfo(HttpServletRequest request, HttpServletResponse response,String consignorId) {
		try {
			if(StringUtils.isBlank(consignorId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Consignor consignor = memberService.getById(Consignor.class,Long.valueOf(consignorId));
			JSONHelper.returnInfo(JSONHelper.bean2json(consignor));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/consignorlocked", method = {RequestMethod.GET, RequestMethod.POST})
	public String consignorlocked(HttpServletRequest request, HttpServletResponse response,String consignorId) {
		try {
			if(StringUtils.isBlank(consignorId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Consignor consignor = memberService.getById(Consignor.class,Long.valueOf(consignorId));
			if(consignor.getIsLocked()){
				consignor.setIsLocked(false);
			}else{
				consignor.setIsLocked(true);
			}
			memberService.save(consignor);
			info("锁定货主:"+consignor.getMobile());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addExpireTime4Consignor", method = {RequestMethod.GET, RequestMethod.POST})
	public String addExpireTime4Consignor(HttpServletRequest request, HttpServletResponse response,long consignorId,int years) {
		try {
			if(years<1){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			long sysUserId = user.getId();
			String sysUserName = user.getRealName();
			Long proxyUserId = null;
			String proxyUserName = null;
			if("A0001".equals(user.getDepartment())){
				proxyUserId = user.getDepartmentId();
				proxyUserName = user.getRealName();
			}
			Consignor consignor = memberService.getById(Consignor.class,consignorId);
			//查看 是否是他本人名下的用户，如果不是是不执行的
			if(consignor==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			tallyService.addExperTimeTally(sysUserId, 
					sysUserName, 
					proxyUserId,
					proxyUserName,
					consignorId,
					consignor.getRealName(),
					Constants.UserType.consignorUser.getType(),
					years);
			info("货主"+consignor.getMobile()+"增加服务"+years+"成功！");
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addExpireTime4Truck", method = {RequestMethod.GET, RequestMethod.POST})
	public String addExpireTime4Truck(HttpServletRequest request, HttpServletResponse response,long driverId,int years) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(years<=0||"C0001".equals(user.getDepartment())){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			long sysUserId = user.getId();
			String sysUserName = user.getRealName();
			Long proxyUserId = null;
			String proxyUserName = null;
			if("A0001".equals(user.getDepartment())){
				proxyUserId = user.getDepartmentId();
				proxyUserName = user.getRealName();
			}
			Truck truck = memberService.getById(Truck.class,driverId);
			//查看 是否是他本人名下的用户，如果不是是不执行的
			if(truck==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			tallyService.addExperTimeTally(sysUserId, 
					sysUserName, 
					proxyUserId,
					proxyUserName,
					driverId,
					truck.getDriverName(),
					Constants.UserType.TruckUser.getType(),
					years);
			info("车主"+truck.getMobile()+"增加服务"+years+"成功！");
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,e.getMessage()));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addjyRecharge4Truck", method = {RequestMethod.GET, RequestMethod.POST})
	public String addjyRecharge4Truck(HttpServletRequest request, HttpServletResponse response,long driverId,double recharge) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(recharge<=0){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			long sysUserId = user.getId();
			String sysUserName = user.getRealName();
			Long proxyUserId = null;
			String proxyUserName = null;
			Truck truck = memberService.getById(Truck.class,driverId);
			//查看 是否是他本人名下的用户，如果不是是不执行的
			if(truck==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			tallyService.addRechargeTally(sysUserId, 
					sysUserName, 
					proxyUserId,
					proxyUserName,
					driverId,
					Constants.TradeType.jyrecharge.getType(),
					recharge);
			info("车主"+truck.getMobile()+"生成充油卡订单"+recharge+"成功！");
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addgsRecharge4Truck", method = {RequestMethod.GET, RequestMethod.POST})
	public String addgsRecharge4Truck(HttpServletRequest request, HttpServletResponse response,long driverId,double recharge) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(recharge<=0){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			long sysUserId = user.getId();
			String sysUserName = user.getRealName();
			Long proxyUserId = null;
			String proxyUserName = null;
			Truck truck = memberService.getById(Truck.class,driverId);
			//查看 是否是他本人名下的用户，如果不是是不执行的
			if(truck==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			tallyService.addRechargeTally(sysUserId, 
					sysUserName, 
					proxyUserId,
					proxyUserName,
					driverId,
					Constants.TradeType.glrecharge.getType(),
					recharge);
			info("车主"+truck.getMobile()+"生成高速充值订单"+recharge+"成功！");
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	/**
	 * 充值货主充值
	 * @Title:        addBalance4Consignor 
	 * @Description:  TODO
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param consignorId
	 * @param:        @param balance
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月25日 上午9:10:38
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addBalance4Consignor", method = {RequestMethod.GET, RequestMethod.POST})
	public String addBalance4Consignor(HttpServletRequest request, HttpServletResponse response,long consignorId,double balance) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(balance<=0||"C0001".equals(user.getDepartment())){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			long sysUserId = user.getId();
			String sysUserName = user.getRealName();
			Long proxyUserId = null;
			String proxyUserName = null;
			if("A0001".equals(user.getDepartment())){
				proxyUserId = user.getDepartmentId();
				proxyUserName = user.getRealName();
			}
			Consignor consignor = memberService.getById(Consignor.class,consignorId);
			//查看 是否是他本人名下的用户，如果不是是不执行的
			if(consignor==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			tallyService.addBalanceTally(sysUserId, 
					sysUserName, 
					proxyUserId,
					proxyUserName,
					consignorId,
					consignor.getRealName(),
					Constants.UserType.consignorUser.getType(),
					balance);
			info("货主"+consignor.getMobile()+"充值"+balance+"成功！");
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,e.getMessage()));
			return NONE;
		}
    }
	
	/**
	 * 车主充值
	 * @Title:        addBalance4Truck 
	 * @Description:  TODO
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param driverId
	 * @param:        @param balance
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月25日 上午9:11:15
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addBalance4Truck", method = {RequestMethod.GET, RequestMethod.POST})
	public String addBalance4Truck(HttpServletRequest request, HttpServletResponse response,long driverId,double balance) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(balance<=0||"C0001".equals(user.getDepartment())){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			long sysUserId = user.getId();
			String sysUserName = user.getRealName();
			Long proxyUserId = null;
			String proxyUserName = null;
			if("A0001".equals(user.getDepartment())){
				proxyUserId = user.getDepartmentId();
				proxyUserName = user.getRealName();
			}
			Truck truck = memberService.getById(Truck.class,driverId);
			//查看 是否是他本人名下的用户，如果不是是不执行的
			if(truck==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			tallyService.addBalanceTally(sysUserId, 
					sysUserName, 
					proxyUserId,
					proxyUserName,
					driverId,
					truck.getDriverName(),
					Constants.UserType.TruckUser.getType(),
					balance);
			info("车主"+truck.getMobile()+"充值"+balance+"成功！");
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,e.getMessage()));
			return NONE;
		}
    }
	
	/**
	 * 车主退款
	 * @Title:        addBalance4Truck 
	 * @Description:  TODO
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param driverId
	 * @param:        @param balance
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月25日 上午9:11:15
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/minusBalanceTally", method = {RequestMethod.GET, RequestMethod.POST})
	public String minusBalanceTally(HttpServletRequest request, HttpServletResponse response,long driverId,double balance) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(balance<=0){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			long sysUserId = user.getId();
			String sysUserName = user.getRealName();
			Truck truck = memberService.getById(Truck.class,driverId);
			if(truck==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			if(truck.getBalance()<balance){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"用户余额不够"));
				return NONE;
			}
			tallyService.minusBalanceTally(sysUserId, 
					sysUserName, 
					driverId,
					truck.getDriverName(),
					Constants.UserType.TruckUser.getType(),
					balance);
			info("车主"+truck.getMobile()+"退款"+balance+"成功！");
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,e.getMessage()));
			return NONE;
		}
    }
	
	
	/**
	 * 货主退款
	 * @Title:        addBalance4Truck 
	 * @Description:  TODO
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param driverId
	 * @param:        @param balance
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年11月25日 上午9:11:15
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/minusBalanceTallyForConsingor", method = {RequestMethod.GET, RequestMethod.POST})
	public String minusBalanceTallyForConsingor(HttpServletRequest request, HttpServletResponse response,long consignorId,double balance) {
		try {
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(balance<=0){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			long sysUserId = user.getId();
			String sysUserName = user.getRealName();
			Consignor consignor = memberService.getById(Consignor.class,consignorId);
			if(consignor==null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			if(consignor.getBalance()<balance){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"用户余额不够"));
				return NONE;
			}
			tallyService.minusBalanceTally(sysUserId, 
					sysUserName, 
					consignorId,
					consignor.getRealName(),
					Constants.UserType.consignorUser.getType(),
					balance);
			info("货主"+consignor.getMobile()+"退款"+balance+"成功！");
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,e.getMessage()));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/checkedConsignorUser", method = {RequestMethod.GET, RequestMethod.POST})
	public String checkedConsignorUser(HttpServletRequest request, HttpServletResponse response,
			String consignorId,
			String idCard,
			String checked,
			String tips) {
		try {
			String sendMsg = "";
			if(StringUtils.isBlank(consignorId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			Consignor consignor = memberService.getById(Consignor.class,Long.valueOf(consignorId));
			if(!Boolean.valueOf(checked)&&!user.getDepartment().equals("B0001")&&consignor.getAuthStep()==3){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"不是待审核状态不能审核"));
				return NONE;
			}
			if(!StringUtils.isBlank(idCard)){
				consignor.setIdCard(idCard);
			}
			if(!StringUtils.isBlank(checked)){
				consignor.setIsAuth(Boolean.valueOf(checked));
			}
			
			String idCardPic = null;			//身份证照片
			String businessLicensePic = null;	//营业执照照片
			String taxRegistrationPic = null;	//税务登记证照片
			String avartPic = null;				//货主本人照片
			String officePic = null;			//货主办公场地照片
			
			String idCardPicUrl = "";			//身份证照片url
			String businessLicensePicUrl = "";	//营业执照照片url
			String taxRegistrationPicUrl = "";	//税务登记证照片url
			String avartPicUrl = "";			//货主照片url
			String officePicUrl = "";			//货主办公场地照片url
			idCardPic = ParameterUtils.getString(request, "idCardPic");
			businessLicensePic = ParameterUtils.getString(request, "businessLicensePic");
			taxRegistrationPic = ParameterUtils.getString(request, "taxRegistrationPic");
			avartPic = ParameterUtils.getString(request, "avartPic");
			officePic = ParameterUtils.getString(request, "officePic");
				CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);
				// 判断 request 是否有文件上传,即多部分请求
				if (multipartResolver.isMultipart(request)) {
					// 保存上传的图片
					// 取得request中的所有文件名
					MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
					Iterator<String> iter = multiRequest.getFileNames();
					while (iter.hasNext()) {
						// 取得上传文件
						MultipartFile file = multiRequest.getFile(iter.next());
	
						// 取得当前上传文件的文件名称
						String myFileName = file.getOriginalFilename();
						String contentType = file.getContentType();
						if (contentType.equals("image/pjpeg")
								|| contentType.equals("image/gif")
								|| contentType.equals("image/jpeg")
								|| contentType.equals("image/png")
								|| contentType.equals("application/octet-stream")) {
							// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
							if (myFileName.trim() != "") {									
								// 重命名上传后的文件名
								// 取得文件后缀
								String appden = myFileName.substring(myFileName.lastIndexOf("."));
								//产生目录名
								String dir = DateUtil.dateToShortCode();
								//产生新文件名
								String serverFileName = UUID.randomUUID().toString();
								String path = servletContext.getRealPath("/").replace("ydypf", "hyt") + "uploadfiles" + File.separator + dir + File.separator;
								File f = new File(path);
								if (!f.exists())
									f.mkdirs();
								String imgName = path + serverFileName + appden;
	
								logger.debug(imgName);
								// 定义上传路径
								File localFile = new File(imgName);
								file.transferTo(localFile);
								
								if(myFileName.equals(avartPic))
									avartPicUrl  = "uploadfiles/" + dir + "/" + serverFileName + appden;
								if(myFileName.equals(idCardPic))									
									idCardPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
								else if(myFileName.equals(businessLicensePic))
									businessLicensePicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
								else if(myFileName.equals(taxRegistrationPic))
									taxRegistrationPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;									
								else if(myFileName.equals(officePic))
									officePicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							}								
						}
					} // end while
					if(!StringUtils.isBlank(idCardPicUrl)){
						consignor.setIdCardPic(idCardPicUrl);
					}
					if(!StringUtils.isBlank(businessLicensePicUrl)){
						consignor.setBusinessLicensePic(businessLicensePicUrl);
					}
					if(!StringUtils.isBlank(taxRegistrationPicUrl)){
						consignor.setTaxRegistrationPic(taxRegistrationPicUrl);
					}
					if(!StringUtils.isBlank(avartPicUrl)){
						consignor.setAvartar(avartPicUrl);
					}
					if(!StringUtils.isBlank(officePicUrl)){
						consignor.setOfficePic(officePicUrl);		
					}
				}
			if(Boolean.valueOf(checked)){
				if(consignor.getIsFirstCheck()==null||consignor.getIsFirstCheck()==false){
					//设定角色
					consignor.setIsFirstCheck(true);
					if(setting.getConsingorregisterpoints()>0){
						tallyService.addPointsTally(consignor.getId(), Constants.UserType.consignorUser.getType(), setting.getConsingorregisterpoints());
					}
				}
				consignor.setAuthStep(3);//0：注册状态，1，提交基本资料，2提交基本资料，3审核成功状态
				sendMsg = "尊敬的用户，恭喜您已经通过我们的审核";
			}else{
				consignor.setAuthStep(0);//0：注册状态，1，提交基本资料，2提交基本资料，3审核成功状态
				if("1".equals(tips)){
					sendMsg = "尊敬的用户，您上传的资料不全，不能通过我们的审核";
				}else if("2".equals(tips)){
					sendMsg = "尊敬的用户，您上传的资料不清晰，不能通过我们的审核";
				}else{
					sendMsg = "尊敬的用户，您上传的资料不正确，不能通过我们的审核";
				}
			}
			memberService.save(consignor);
			String ret = commDataService.push2Consignor(consignor.getJpushId(), sendMsg, consignor.getAuthStep(),Boolean.valueOf(checked),Integer.valueOf(consignor.getMobileType()));
			info("审核货主："+consignor.getMobile());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			info("审核货主失败 ID："+consignorId);
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addPoints4Truck", method = {RequestMethod.GET, RequestMethod.POST})
	public String addPoints4Truck(HttpServletRequest request, HttpServletResponse response,
			String truckId,
			int points) {
		try {
			if(StringUtils.isBlank(truckId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			Truck truck = memberService.getById(Truck.class,Long.valueOf(truckId));
			tallyService.addPointsTally(truck.getId(), Constants.UserType.TruckUser.getType(), points,"赠送驿道币");
			info("充币："+points+","+truck.getMobile());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			info("充币：："+truckId);
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveConsignorUser", method = {RequestMethod.GET, RequestMethod.POST})
	public String saveConsignorUser(HttpServletRequest request, HttpServletResponse response,
			String mobile,
			String password,
			String userName,
			String idCard,
			String companyName,
			String companyAddr,
			String descript,
			String bussDescript
			) {
		try {
			if(StringUtils.isBlank(mobile)||
					StringUtils.isBlank(companyName)||
					StringUtils.isBlank(companyAddr)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"参数出错"));
				return NONE;
			}
			if(memberService.userIsExists(mobile)||memberService.getByPlateNumber(mobile)!=null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"手机号已经注册。"));
				return NONE;
			}
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			Date d = new Date();
			Consignor consignor = new Consignor();
			
			consignor.setUserType(1);
			consignor.setRealName(userName);
			consignor.setIdCard(idCard);
			consignor.setCompanyName(companyName);
			consignor.setAddress(companyAddr);					
			consignor.setAuthStep(0);//0：注册状态，1，提交基本资料，2提交基本资料，3审核成功状态
			consignor.setDescript(descript);
			password = StringUtils.isBlank(password)?"123456":password;
			consignor.setPassword(MD5.getInstance().getMD5(password));
			
			consignor.setBalance(0.0);
			consignor.setPoints(0L);
			consignor.setTellerId("00");
			consignor.setBussDescript(bussDescript);
			consignor.setIsLocked(false);
			consignor.setDeviceId("");
			consignor.setJpushId("");
			consignor.setLoginFailureCount(0);
			consignor.setIsAuth(false);
			consignor.setMobile(mobile);
			consignor.setRegisterTime(d);
			consignor.setTaxRegistrationPic("");
			consignor.setPaymentPassword(MD5.getInstance().getMD5(mobile.substring(5)));
			
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(d);
			rightNow.add(Calendar.MONTH, 1);
			consignor.setExpireTime(rightNow.getTime());
			consignor.setProxyUserId(user);
			
			String idCardPic = "idCardPic";			//身份证照片
			String businessLicensePic = "businessLicensePic";	//营业执照照片
			String taxRegistrationPic = "taxRegistrationPic";	//税务登记证照片
			String avartPic = "avartPic";				//货主本人照片
			String officePic = "officePic";			//货主办公场地照片
			String pic1 = "pic1";			
			String pic2 = "pic2";			
			String pic3 = "pic3";			
			String pic4 = "pic4";			
			String pic5 = "pic5";			
			
			String idCardPicUrl = "";			//身份证照片url
			String businessLicensePicUrl = "";	//营业执照照片url
			String taxRegistrationPicUrl = "";	//税务登记证照片url
			String avartPicUrl = "";			//货主照片url
			String officePicUrl = "";			//货主办公场地照片url
			
				// 创建一个通用的多部分解析器
				//ServletContext ctx = request.getSession().getServletContext();
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);
			
			
			Map sbpic = new HashMap();
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 保存上传的图片
				// 取得request中的所有文件名
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 取得上传文件
					CommonsMultipartFile  file = (CommonsMultipartFile)multiRequest.getFile(iter.next());

					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					String contentType = file.getContentType();
					String fName = file.getFileItem().getFieldName();
					if (contentType.equals("image/pjpeg")
							|| contentType.equals("image/gif")
							|| contentType.equals("image/jpeg")
							|| contentType.equals("image/png")
							|| contentType.equals("application/octet-stream")) {
						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (myFileName.trim() != "") {									
							// 重命名上传后的文件名
							// 取得文件后缀
							String appden = myFileName.substring(myFileName.lastIndexOf("."));
							//产生目录名
							String dir = DateUtil.dateToShortCode();
							//产生新文件名
							String serverFileName = UUID.randomUUID().toString();
							String path = servletContext.getRealPath("/").replace("ydypf", "hyt") + "uploadfiles" + File.separator + dir + File.separator;
							File f = new File(path);
							if (!f.exists())
								f.mkdirs();
							String imgName = path + serverFileName + appden;

							logger.debug(imgName);
							// 定义上传路径
							File localFile = new File(imgName);
							file.transferTo(localFile);
							
							if(fName.equals(avartPic))
								avartPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							if(fName.equals(idCardPic))									
								idCardPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(fName.equals(businessLicensePic))
								businessLicensePicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(fName.equals(taxRegistrationPic))
								taxRegistrationPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;									
							else if(fName.equals(officePic))
								officePicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							
							
							else if(fName.equals(pic1))
								sbpic.put(pic1, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic2))
								sbpic.put(pic2, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic3))
								sbpic.put(pic3, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic4))
								sbpic.put(pic4, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic5))
								sbpic.put(pic5, "uploadfiles/" + dir + "/" + serverFileName + appden);
							
						}								
					}
				} // end while
				
				if(!StringUtils.isBlank(idCardPicUrl)){
					consignor.setIdCardPic(idCardPicUrl);
				}
				if(!StringUtils.isBlank(businessLicensePicUrl)){
					consignor.setBusinessLicensePic(businessLicensePicUrl);
				}
				if(!StringUtils.isBlank(avartPicUrl)){
					consignor.setAvartar(avartPicUrl);
				}
				if(!StringUtils.isBlank(officePicUrl)){
					consignor.setOfficePic(officePicUrl);		
				}
				if(!StringUtils.isBlank(sbpic.toString())){
					consignor.setTaxRegistrationPic(JSONObject.fromObject(sbpic).toString());
				}
			}
			if(!StringUtils.isBlank(consignor.getOfficePic())&&
					!StringUtils.isBlank(consignor.getAvartar())&&
					!StringUtils.isBlank(consignor.getIdCardPic())&&
					!StringUtils.isBlank(consignor.getBusinessLicensePic())&&
					!StringUtils.isBlank(consignor.getIdCard())){
				consignor.setAuthStep(3);//0：注册状态，1，提交基本资料，2提交基本资料，3审核成功状态
				consignor.setIsAuth(true);
			}
			memberService.save(consignor);
			info("新增货主ID："+consignor.getMobile());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editCompanyConsignorUser", method = {RequestMethod.GET, RequestMethod.POST})
	public String editCompanyConsignorUser(HttpServletRequest request, HttpServletResponse response,
			long consignorId,
			String mobile,
			String password,
			String userName,
			String idCard,
			String companyName,
			String companyAddr,
			String descript,
			String bussDescript
			) {
		try {
			if(StringUtils.isBlank(mobile)||
					StringUtils.isBlank(companyName)||
					StringUtils.isBlank(companyAddr)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"参数出错"));
				return NONE;
			}
			Consignor consignor = memberService.getById(Consignor.class, consignorId);
			if(!consignor.getMobile().equals(mobile)){
				if(memberService.userIsExists(mobile)||memberService.getByPlateNumber(mobile)!=null){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"手机号已经注册。"));
					return NONE;
				}
				consignor.setMobile(mobile);
			}
			if(!StringUtils.isBlank(password)&&!password.equals("如果不修改请不要填写")){
				consignor.setPassword(MD5.getInstance().getMD5(password));
			}
			if(!userName.equals(consignor.getRealName())){
				consignor.setRealName(userName);
			}
			if(!idCard.equals(consignor.getIdCard())){
				consignor.setIdCard(idCard);
			}
			if(!companyName.equals(consignor.getCompanyName())){
				consignor.setCompanyName(companyName);
			}
			if(!companyAddr.equals(consignor.getAddress())){
				consignor.setAddress(companyAddr);
			}
			if(!bussDescript.equals(consignor.getBussDescript())){
				consignor.setBussDescript(bussDescript);
			}
			descript = descript==null?"":descript;
			if(!descript.equals(consignor.getDescript())){
				consignor.setDescript(descript);
			}
			
			String idCardPic = "idCardPic";			//身份证照片
			String businessLicensePic = "businessLicensePic";	//营业执照照片
			String taxRegistrationPic = "taxRegistrationPic";	//税务登记证照片
			String avartPic = "avartPic";				//货主本人照片
			String officePic = "officePic";			//货主办公场地照片
			
			String idCardPicUrl = "";			//身份证照片url
			String businessLicensePicUrl = "";	//营业执照照片url
			String taxRegistrationPicUrl = "";	//税务登记证照片url
			String avartPicUrl = "";			//货主照片url
			String officePicUrl = "";			//货主办公场地照片url
			
			String pic1 = "pic1";			
			String pic2 = "pic2";			
			String pic3 = "pic3";			
			String pic4 = "pic4";			
			String pic5 = "pic5";			
			Map sbpic = new HashMap();
			if(!StringUtils.isBlank(consignor.getTaxRegistrationPic())){
				sbpic = StringUtil.toHashMap(consignor.getTaxRegistrationPic());
			}
			
			// 判断 request 是否有文件上传,即多部分请求
				// 创建一个通用的多部分解析器
				//ServletContext ctx = request.getSession().getServletContext();
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 保存上传的图片
				// 取得request中的所有文件名
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 取得上传文件
					CommonsMultipartFile  file = (CommonsMultipartFile)multiRequest.getFile(iter.next());

					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					String contentType = file.getContentType();
					String fName = file.getFileItem().getFieldName();
					if (contentType.equals("image/pjpeg")
							|| contentType.equals("image/gif")
							|| contentType.equals("image/jpeg")
							|| contentType.equals("image/png")
							|| contentType.equals("application/octet-stream")) {
						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (myFileName.trim() != "") {									
							// 重命名上传后的文件名
							// 取得文件后缀
							String appden = myFileName.substring(myFileName.lastIndexOf("."));
							//产生目录名
							String dir = DateUtil.dateToShortCode();
							//产生新文件名
							String serverFileName = UUID.randomUUID().toString();
							String path = servletContext.getRealPath("/").replace("ydypf", "hyt") + "uploadfiles" + File.separator + dir + File.separator;
							File f = new File(path);
							if (!f.exists())
								f.mkdirs();
							String imgName = path + serverFileName + appden;

							logger.debug(imgName);
							// 定义上传路径
							File localFile = new File(imgName);
							file.transferTo(localFile);
							
							if(fName.equals(avartPic))
								avartPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							if(fName.equals(idCardPic))									
								idCardPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(fName.equals(businessLicensePic))
								businessLicensePicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(fName.equals(taxRegistrationPic))
								taxRegistrationPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;									
							else if(fName.equals(officePic))
								officePicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							
							
							else if(fName.equals(pic1))
								sbpic.put(pic1, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic2))
								sbpic.put(pic2, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic3))
								sbpic.put(pic3, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic4))
								sbpic.put(pic4, "uploadfiles/" + dir + "/" + serverFileName + appden);
							else if(fName.equals(pic5))
								sbpic.put(pic5, "uploadfiles/" + dir + "/" + serverFileName + appden);
						}								
					}
				} // end while
				
				if(!StringUtils.isBlank(idCardPicUrl)){
					consignor.setIdCardPic(idCardPicUrl);
				}
				if(!StringUtils.isBlank(businessLicensePicUrl)){
					consignor.setBusinessLicensePic(businessLicensePicUrl);
				}
				if(!StringUtils.isBlank(sbpic.toString())){
					consignor.setTaxRegistrationPic(JSONObject.fromObject(sbpic).toString());
				}
				if(!StringUtils.isBlank(avartPicUrl)){
					consignor.setAvartar(avartPicUrl);
				}
				if(!StringUtils.isBlank(officePicUrl)){
					consignor.setOfficePic(officePicUrl);		
				}
			}
			if(!StringUtils.isBlank(consignor.getOfficePic())&&
					!StringUtils.isBlank(consignor.getAvartar())&&
					!StringUtils.isBlank(consignor.getIdCardPic())&&
					!StringUtils.isBlank(consignor.getBusinessLicensePic())&&
					!StringUtils.isBlank(consignor.getIdCard())){
				consignor.setAuthStep(3);//0：注册状态，1，提交基本资料，2提交基本资料，3审核成功状态
				consignor.setIsAuth(true);
			}
			
			memberService.save(consignor);
			info("修改货主ID："+consignor.getMobile());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/modifyConsignorUser", method = {RequestMethod.GET, RequestMethod.POST})
	public String modifyConsignorUser(HttpServletRequest request, HttpServletResponse response,
			String consignorId) {
		try {
			String sendMsg = "";
			if(StringUtils.isBlank(consignorId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Consignor consignor = memberService.getById(Consignor.class,Long.valueOf(consignorId));
			
			String idCardPic = null;			//身份证照片
			String businessLicensePic = null;	//营业执照照片
			String taxRegistrationPic = null;	//税务登记证照片
			String avartPic = null;				//货主本人照片
			String officePic = null;			//货主办公场地照片
			
			String idCardPicUrl = "";			//身份证照片url
			String businessLicensePicUrl = "";	//营业执照照片url
			String taxRegistrationPicUrl = "";	//税务登记证照片url
			String avartPicUrl = "";			//货主照片url
			String officePicUrl = "";			//货主办公场地照片url
			idCardPic = ParameterUtils.getString(request, "idCardPic");
			businessLicensePic = ParameterUtils.getString(request, "businessLicensePic");
			taxRegistrationPic = ParameterUtils.getString(request, "taxRegistrationPic");
			avartPic = ParameterUtils.getString(request, "avartPic");
			officePic = ParameterUtils.getString(request, "officePic");
			
			// 创建一个通用的多部分解析器
			//ServletContext ctx = request.getSession().getServletContext();
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(servletContext);
			// 判断 request 是否有文件上传,即多部分请求
			if (multipartResolver.isMultipart(request)) {
				// 保存上传的图片
				// 取得request中的所有文件名
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
				Iterator<String> iter = multiRequest.getFileNames();
				while (iter.hasNext()) {
					// 取得上传文件
					MultipartFile file = multiRequest.getFile(iter.next());

					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					String contentType = file.getContentType();
					if (contentType.equals("image/pjpeg")
							|| contentType.equals("image/gif")
							|| contentType.equals("image/jpeg")
							|| contentType.equals("image/png")
							|| contentType.equals("application/octet-stream")) {
						// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if (myFileName.trim() != "") {									
							// 重命名上传后的文件名
							// 取得文件后缀
							String appden = myFileName.substring(myFileName.lastIndexOf("."));
							//产生目录名
							String dir = DateUtil.dateToShortCode();
							//产生新文件名
							String serverFileName = UUID.randomUUID().toString();
							String path = servletContext.getRealPath("/").replace("ydypf", "hyt") + "uploadfiles" + File.separator + dir + File.separator;
							File f = new File(path);
							if (!f.exists())
								f.mkdirs();
							String imgName = path + serverFileName + appden;

							logger.debug(imgName);
							// 定义上传路径
							File localFile = new File(imgName);
							file.transferTo(localFile);
							
							if(myFileName.equals(avartPic))
								avartPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							if(myFileName.equals(idCardPic))									
								idCardPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(myFileName.equals(businessLicensePic))
								businessLicensePicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
							else if(myFileName.equals(taxRegistrationPic))
								taxRegistrationPicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;									
							else if(myFileName.equals(officePic))
								officePicUrl = "uploadfiles/" + dir + "/" + serverFileName + appden;
						}								
					}
				} // end while
				if(!StringUtils.isBlank(idCardPicUrl)){
					consignor.setIdCardPic(idCardPicUrl);
				}
				if(!StringUtils.isBlank(businessLicensePicUrl)){
					consignor.setBusinessLicensePic(businessLicensePicUrl);
				}
				if(!StringUtils.isBlank(taxRegistrationPicUrl)){
					consignor.setTaxRegistrationPic(taxRegistrationPicUrl);
				}
				if(!StringUtils.isBlank(avartPicUrl)){
					consignor.setAvartar(avartPicUrl);
				}
				if(!StringUtils.isBlank(officePicUrl)){
					consignor.setOfficePic(officePicUrl);		
				}
			}
			memberService.save(consignor);
			info("修改货主ID："+consignor.getMobile());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			info("修改货主失败 ID："+consignorId);
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editConsignorUser", method = {RequestMethod.GET, RequestMethod.POST})
	public String editConsignorUser(HttpServletRequest request, HttpServletResponse response,
			String consignorId,
			String realName,
			String mobile,
			String idCard
			) {
		try {
			if(StringUtils.isBlank(consignorId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			Consignor consignor = memberService.getById(Consignor.class,Long.valueOf(consignorId));
			
			Consignor t1 = memberService.getbyMobileNo(mobile);
			if(t1!=null&&!consignorId.equals(t1.getId()+"")){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"手机号不能重复"));
				return NONE;
			}
			if(!StringUtils.isBlank(realName)){
				consignor.setRealName(realName);
			}
			if(!StringUtils.isBlank(mobile)){
				consignor.setMobile(mobile);
			}
			if(!StringUtils.isBlank(idCard)){
				consignor.setIdCard(idCard);
			}
			memberService.save(consignor);
			info("编辑货主："+consignor.getMobile());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/consignorStatements", method = {RequestMethod.GET, RequestMethod.POST})
	public String consignorStatements(HttpServletRequest request, HttpServletResponse response,String consignorId) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(consignorId)){
				args.put("consignorId", consignorId);
			}
			List<TCargoTransactionStatements> list = memberService.consignorUserStatements(args);
			List<Map> retlist = new ArrayList<Map>();
			for(TCargoTransactionStatements sm:list){
				Map ret = new HashMap();
				ret.put("id", sm.getId());
				ret.put("accountId", sm.getAccountId());
				ret.put("pay", sm.getPay());
				ret.put("payType",sm.getPayType()==null||sm.getPayType()==1?"余额":"微信");
				ret.put("deposit", sm.getDeposit());
				ret.put("balance", sm.getBalance());
				ret.put("points", sm.getPayPoints());
				ret.put("tradeType", sm.getTradeType());
				ret.put("remark", sm.getRemark());
				ret.put("createdDate", DateUtil.getStrYMDHMSByDate(sm.getCreatedDate()));
				retlist.add(ret);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(retlist));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/feedbackByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String feedbackByPage(HttpServletRequest request, HttpServletResponse response,String content,String mobile,String date,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(content)){
				content = URLDecoder.decode(content,"UTF-8"); 
				args.put("content", content);
			}
			if(!StringUtils.isBlank(mobile)){
				mobile = URLDecoder.decode(mobile,"UTF-8"); 
				args.put("mobile", mobile);
			}
			Page page = memberService.feebackQuery(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				Feedback c = (Feedback)sm;
				mo.put("id", c.getId());
				mo.put("mobile", c.getMobile());
				mo.put("content", c.getContent());
				mo.put("createdDate", DateUtil.getStrYMDHMSByDate(c.getAddTime()));
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.getTotalCount()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
}
