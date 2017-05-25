

package com.ydy258.ydy.controller;


import java.io.File;
import java.math.BigDecimal;
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
import com.ydy258.ydy.entity.ApkJarUpdate;
import com.ydy258.ydy.entity.ApkUpdate;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.service.IApkUpdateService;
import com.ydy258.ydy.util.ApkInfoTools;
import com.ydy258.ydy.util.DateUtil;
import com.ydy258.ydy.util.JSONHelper;
import com.ydy258.ydy.util.MD5Util;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.SettingUtils;

@Controller
@RequestMapping("/sys/apkupdate/")
public class ApkUpdateAction extends BaseFacade  {
	
	final private Setting setting = SettingUtils.get();
	
	@Autowired
	private IApkUpdateService apkUpdateService;
	
	/** servletContext */
	private ServletContext servletContext;	
	
	@Override
	public void setServletContext(ServletContext ctx) {
		// TODO Auto-generated method stub
		this.servletContext = ctx;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/apkByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String apkByPage(HttpServletRequest request, HttpServletResponse response,String search,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
				args.put("search", search);
			}
			//加入代理区分
			Page page = apkUpdateService.apkByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((ApkUpdate)sm).getId());
				mo.put("apkUrl", ((ApkUpdate)sm).getApkUrl());
				mo.put("appType", ((ApkUpdate)sm).getAppType());
				mo.put("newMd5", ((ApkUpdate)sm).getNewMd5());
				mo.put("targetSize", ((ApkUpdate)sm).getTargetSize());
				mo.put("newVersion", ((ApkUpdate)sm).getVersoinName());
				mo.put("updateLog", ((ApkUpdate)sm).getUpdateLog());
				mo.put("forceUpdate", ((ApkUpdate)sm).isForceUpdate());
				mo.put("createdDate", DateUtil.getStrYMDHMByDate(((ApkUpdate)sm).getCreatedDate()));
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
	@RequestMapping(value = "/upload", method = {RequestMethod.GET, RequestMethod.POST})
	public String upload(HttpServletRequest request,HttpServletResponse response,
			boolean isUpdate,
			String updateLog,
			int appType
			){
		try {
			//检查金额
			if(StringUtils.isBlank(updateLog)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			
			ApkUpdate di = new ApkUpdate();
			di.setCreatedDate(new Date());
			di.setForceUpdate(isUpdate);
			//di.setNewVersion(newVersion);
			di.setUpdateLog(updateLog);
			di.setAppType(appType);
			if(appType==1){
				di.setAppKey("2b638ee31cb2f00401d7ffc8c39d3c4d");
				di.setPackages("com.hyt258.truck");
			}
			if(appType==2){
				di.setAppKey("d4aa166dcec0079801d7ffc8c39d3c4d");
				di.setPackages("com.hyt258.consignor");
			}
			if(appType==3){
				di.setAppKey("71b0923fcbb7cf6b01d7ffc8c39d3c4d");
				di.setPackages("com.hyt258.merchant");
			}
			if(appType==4){
				di.setAppKey("35cebc2bcf3e9e4901d7ffc8c39d3c4d");
				di.setPackages("com.hyt258.manage");
			}
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
						double size = (double)file.getSize()/(double)1048576;
						BigDecimal   b   =   new   BigDecimal(size);    
						size   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
						//取得当前上传文件的文件名称
						String myFileName = file.getOriginalFilename();
						//如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if(myFileName.trim() !=""){
							// 重命名上传后的文件名
							String contentType = file.getContentType();
							if (!contentType.equals("application/octet-stream")) {
								JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"请上传apk格式文件"));
								return NONE;
							}
							// 取得文件后缀
							String appden = myFileName.substring(myFileName.lastIndexOf("."));
							//产生目录名
							String dir = DateUtil.dateToShortCode();
							//产生新文件名
							String serverFileName = UUID.randomUUID().toString();
							String path = ctx.getRealPath("/") + "apkfiles" + File.separator + dir + File.separator;
							File f = new File(path);
							if (!f.exists())
								f.mkdirs();
							String imgName = path + serverFileName + appden;

							// 定义上传路径
							File localFile = new File(imgName);
							file.transferTo(localFile);
							MD5Util getMD5 = new MD5Util();
							String md5 = getMD5.GetFileMD5Code(localFile);
							
							String[] rs =  ApkInfoTools.unZip(localFile.getAbsolutePath());
							di.setNewVersion(rs[2]);
							di.setVersoinName(rs[0]);
							if(!rs[1].equals(di.getPackages())){
								JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,"apk上传错误！"));
								return NONE;
							}
							ApkUpdate check = apkUpdateService.getNewApk(di.getAppKey());
							if(check!=null&&Integer.valueOf(di.getNewVersion())<=Integer.valueOf(check.getNewVersion())){
								JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,"版本号不正确！"));
								return NONE;
							}
							String picUrl = "apkfiles/" + dir + "/" + serverFileName + appden;
							di.setApkUrl(picUrl);
							di.setNewMd5(md5);
							di.setTargetSize(size);
							apkUpdateService.save(di);
						}
					}
				}
			}
			info("上传apk："+di.getNewVersion());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,e.getMessage()));
			return NONE;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/jarApkByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String jarApkByPage(HttpServletRequest request, HttpServletResponse response,String search,int currentPage) {
		try {
			Map args = new HashMap<String,String>();
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
				args.put("search", search);
			}
			//加入代理区分
			Page page = apkUpdateService.apkJarByPage(args, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((ApkUpdate)sm).getId());
				mo.put("apkUrl", ((ApkUpdate)sm).getApkUrl());
				mo.put("appType", ((ApkUpdate)sm).getAppType());
				mo.put("newMd5", ((ApkUpdate)sm).getNewMd5());
				mo.put("targetSize", ((ApkUpdate)sm).getTargetSize());
				mo.put("newVersion", ((ApkUpdate)sm).getNewVersion());
				mo.put("versionName", ((ApkUpdate)sm).getVersoinName());
				mo.put("updateLog", ((ApkUpdate)sm).getUpdateLog());
				mo.put("forceUpdate", ((ApkUpdate)sm).isForceUpdate());
				mo.put("createdDate", DateUtil.getStrYMDHMByDate(((ApkUpdate)sm).getCreatedDate()));
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
	@RequestMapping(value = "/jarUpload", method = {RequestMethod.GET, RequestMethod.POST})
	public String jarUpload(HttpServletRequest request,HttpServletResponse response,
			boolean isUpdate,
			String updateLog,
			int appType,
			String version,
			String versionName
			){
		try {
			if(StringUtils.isBlank(updateLog)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			
			ApkJarUpdate di = new ApkJarUpdate();
			di.setCreatedDate(new Date());
			di.setForceUpdate(isUpdate);
			//di.setNewVersion(newVersion);
			di.setUpdateLog(updateLog);
			di.setAppType(appType);
			if(appType==1){
				di.setAppKey("2b638ee31cb2f00401d7ffc8c39d3c4d");
				di.setPackages("com.hyt258.truck");
			}
			if(appType==2){
				di.setAppKey("d4aa166dcec0079801d7ffc8c39d3c4d");
				di.setPackages("com.hyt258.consignor");
			}
			if(appType==3){
				di.setAppKey("71b0923fcbb7cf6b01d7ffc8c39d3c4d");
				di.setPackages("com.hyt258.merchant");
			}
			if(appType==4){
				di.setAppKey("35cebc2bcf3e9e4901d7ffc8c39d3c4d");
				di.setPackages("com.hyt258.manage");
			}
			di.setNewVersion(version);
			di.setVersoinName(versionName);
			ApkJarUpdate check = apkUpdateService.getNewJarApk(di.getAppKey());
			if(check!=null&&Integer.valueOf(di.getNewVersion())<=Integer.valueOf(check.getNewVersion())){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,"版本号不正确！"));
				return NONE;
			}
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			ServletContext ctx = request.getSession().getServletContext();
			//创建一个通用的多部分解析器     
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(ctx);
			//判断 request 是否有文件上传,即多部分请求    测试git
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
						double size = (double)file.getSize()/(double)1048576;
						BigDecimal   b   =   new   BigDecimal(size);    
						size   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
						//取得当前上传文件的文件名称
						String myFileName = file.getOriginalFilename();
						//如果名称不为“”,说明该文件存在，否则说明该文件不存在
						if(myFileName.trim() !=""){
							// 重命名上传后的文件名
							String contentType = file.getContentType();
							if (!contentType.equals("application/octet-stream")) {
								JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"请上传apk格式文件"));
								return NONE;
							}
							// 取得文件后缀
							String appden = myFileName.substring(myFileName.lastIndexOf("."));
							//产生目录名
							String dir = DateUtil.dateToShortCode();
							//产生新文件名
							String serverFileName = UUID.randomUUID().toString();
							String path = ctx.getRealPath("/") + "apkfiles" + File.separator + dir + File.separator;
							File f = new File(path);
							if (!f.exists())
								f.mkdirs();
							String imgName = path + serverFileName + appden;

							// 定义上传路径
							File localFile = new File(imgName);
							file.transferTo(localFile);
							MD5Util getMD5 = new MD5Util();
							String md5 = getMD5.GetFileMD5Code(localFile);
							
							
							String picUrl = "apkfiles/" + dir + "/" + serverFileName + appden;
							di.setApkUrl(picUrl);
							di.setNewMd5(md5);
							di.setTargetSize(size);
							apkUpdateService.save(di);
						}
					}
				}
			}
			info("上传apk："+di.getNewVersion());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,e.getMessage()));
			return NONE;
		}
	}
}
