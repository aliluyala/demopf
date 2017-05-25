

package com.ydy258.ydy.controller;


import java.net.URLDecoder;
import java.util.ArrayList;
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

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.entity.Entity;
import com.ydy258.ydy.entity.SysRole;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.TCargoTransactionStatements;
import com.ydy258.ydy.entity.TDriverTransactionStatements;
import com.ydy258.ydy.entity.TProxyTransactionStatements;
import com.ydy258.ydy.service.ISysUserService;
import com.ydy258.ydy.service.ITallyService;
import com.ydy258.ydy.util.DateUtil;
import com.ydy258.ydy.util.JSONHelper;
import com.ydy258.ydy.util.MD5;
import com.ydy258.ydy.util.Page;

@Controller
@RequestMapping("/sys/user/")
public class SysUserAction extends BaseFacade  {
	
	@Autowired
	private ISysUserService sysUserService;
	
	@Autowired
	private ITallyService tallyService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/roleListByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String roleListByPage(HttpServletRequest request, HttpServletResponse response,String search,int currentPage) {
		try {
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
			}
			Page page = sysUserService.roleListByPage(search, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((SysRole)sm).getId());
				mo.put("name", ((SysRole)sm).getRoleName());
				mo.put("createTime", ((SysRole)sm).getCreatedDate()==null?"":DateUtil.getStrYMDHMByDate(((SysRole)sm).getCreatedDate()));
				mo.put("status", ((SysRole)sm).getStatus()==null||((SysRole)sm).getStatus().equals(Entity.Status.effective.name())?"有效":"无效");
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
	@RequestMapping(value = "/userListByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String userListByPage(HttpServletRequest request, HttpServletResponse response,String search,int currentPage) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if("A0001".equals(user.getDepartment())){
				where.put("parentId",user.getDepartmentId()+"");
			}
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
				where.put("name",search);
			}
			Page page = sysUserService.userListByPage(where, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((SysUser)sm).getId());
				mo.put("userName", ((SysUser)sm).getUserName());
				mo.put("realName", ((SysUser)sm).getRealName());
				mo.put("empNumber", ((SysUser)sm).getEmpNumber());
				mo.put("department", ((SysUser)sm).getDepartment());
				mo.put("position", ((SysUser)sm).getPosition());
				mo.put("createTime", ((SysUser)sm).getCreatedDate()==null?"":DateUtil.getStrYMDHMByDate(((SysUser)sm).getCreatedDate()));
				mo.put("status", ((SysUser)sm).getStatus()==null||((SysUser)sm).getStatus().equals(Entity.Status.effective.name())?"有效":"无效");
				mo.put("proxyAdmin", ((SysUser)sm).getProxyAdmin()==null||((SysUser)sm).getProxyAdmin()==false?"否":"是");
				mo.put("mobileUser", ((SysUser)sm).getMobileUser()==null||((SysUser)sm).getMobileUser()==false?"否":"是");
				mo.put("balance", ((SysUser)sm).getBalance()==null?0.0:((SysUser)sm).getBalance());
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
	@RequestMapping(value = "/userPayListByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String userPayListByPage(HttpServletRequest request, HttpServletResponse response,int currentPage,String name) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			where.put("sysUserId",user.getId()+"");
			if(!StringUtils.isBlank(name)){
				where.put("search",name);
			}
			Page page = sysUserService.userPayListByPage(where, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((Map)sm).get("id"));
				mo.put("orderNo", ((Map)sm).get("order_no"));
				mo.put("pay", ((Map)sm).get("pay"));
				mo.put("remark", ((Map)sm).get("remark"));
				mo.put("isTurnout", ((Map)sm).get("is_turnout"));
				mo.put("name", ((Map)sm).get("name"));
				mo.put("mobile", ((Map)sm).get("mobile"));
				mo.put("createTime", ((Map)sm).get("created_date")==null?"":DateUtil.getStrYMDHMByDate((Date)(((Map)sm).get("created_date"))));
				mo.put("balance", ((Map)sm).get("balance")==null?0.0:((Map)sm).get("balance"));
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
	@RequestMapping(value = "/userAccountBook", method = {RequestMethod.GET, RequestMethod.POST})
	public String userAccountBook(HttpServletRequest request, HttpServletResponse response,int currentPage) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			where.put("sysUserId",user.getId()+"");
			String dep  = user.getDepartment();
			short userType = 1;
			if(dep.equals("D0001")){
				userType = 2;
			}else if(dep.equals("E0001")){
				userType = 1;
			}else{
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
			Page page = sysUserService.userAccountBook(user.getDepartmentId(), userType, null, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Long id;
				Double pay;
				Double balance;
				Integer tradeType;
				String remark;
				String createdDate;
				Long payPoints;
			 	Long restPoints;
				String orderNo;
				if(Short.valueOf(userType)==Constants.UserType.consignorUser.getType()){
					TCargoTransactionStatements order = (TCargoTransactionStatements)sm;
					id = order.getId();
					pay = order.getPay();
					balance = order.getBalance();
					tradeType = order.getTradeType();
					remark = order.getRemark();
					createdDate = DateUtil.getStrYMDHMSByDate(order.getCreatedDate());
					payPoints = order.getPayPoints();
					restPoints = order.getRestPoints();
					orderNo = order.getOrderNo();
				}else{
					TDriverTransactionStatements order = (TDriverTransactionStatements)sm;
					id = order.getId();
					pay = order.getPay();
					balance = order.getBalance();
					tradeType = order.getTradeType();
					remark = order.getRemark();
					createdDate = DateUtil.getStrYMDHMSByDate(order.getCreatedDate());
					payPoints = order.getPayPoints();
					restPoints = order.getRestPoints();
					orderNo = order.getOrderNo();
				}
				Map mo = new HashMap();
				mo.put("id", id);
				mo.put("orderNo", orderNo);
				mo.put("pay", pay);
				mo.put("remark", remark);
				mo.put("isTurnout",tradeType);
				mo.put("createTime", createdDate);
				mo.put("balance", balance);
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
	@RequestMapping(value = "/addBalance4ProxyUserTally", method = {RequestMethod.GET, RequestMethod.POST})
	public String addBalance4ProxyUserTally(HttpServletRequest request, HttpServletResponse response,long proxyUserId,double balance) {
		try {
			if(balance<=0){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			long sysUserId = user.getId();
			String sysUserName = user.getRealName();
			SysUser sysUser = sysUserService.getById(SysUser.class,proxyUserId);
			if(sysUser.getDepartment().equals("C0001")){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"合作商不能充值;"));
				return NONE;
			}
			if(sysUser==null/*||!sysUser.getProxyAdmin()*/){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			tallyService.addBalance4ProxyUserTally(sysUserId, 
													sysUserName, 
													proxyUserId,
													sysUser.getUserName(), 
													balance);
			info("代理用户"+sysUser.getUserName()+"充值"+balance+"成功！");
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/userList", method = {RequestMethod.GET, RequestMethod.POST})
	public String userList(HttpServletRequest request, HttpServletResponse response,String search) {
		try {
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
			}
			List page = sysUserService.userList(search);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page){
				Map mo = new HashMap();
				mo.put("id", ((SysUser)sm).getId());
				mo.put("userName", ((SysUser)sm).getUserName());
				mo.put("realName", ((SysUser)sm).getRealName());
				mo.put("empNumber", ((SysUser)sm).getEmpNumber());
				mo.put("department", ((SysUser)sm).getDepartment());
				mo.put("position", ((SysUser)sm).getPosition());
				mo.put("createTime", ((SysUser)sm).getCreatedDate()==null?"":DateUtil.getStrYMDHMByDate(((SysUser)sm).getCreatedDate()));
				mo.put("status", ((SysUser)sm).getStatus()==null||((SysUser)sm).getStatus().equals(Entity.Status.effective.name())?"有效":"无效");
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.size()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveUser", method = {RequestMethod.GET, RequestMethod.POST})
	public String saveUser(HttpServletRequest request, HttpServletResponse response,
			String userName,
			String password,
			String realName,
			String empNumber,
			String position,
			String status,
			String newRoles,
			String department,
			Boolean isMobileUser,
			Long proxyId,
			String mobile) {
		try {
			if(StringUtils.isBlank(userName)||
					StringUtils.isBlank(realName)||
					StringUtils.isBlank(position)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			if("A0001".equals(department)&&(proxyId==null)){//如果是代理
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			//加入代理区分
			SysUser  dd = new SysUser();
			dd.setCreatedDate(new Date());
			
			SysUser dd2 = sysUserService.queryUserByName(userName);
			if(dd2!=null){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"用户名已被占用"));
				return NONE;
			}
			if(!StringUtils.isBlank(userName)){
				dd.setUserName(userName);
			}
			if(!StringUtils.isBlank(password)){
				password = MD5.getInstance().getMD5(password);
				dd.setPassword(password);
			}
			if(!StringUtils.isBlank(realName)){
				dd.setRealName(realName);
			}
			if(!StringUtils.isBlank(empNumber)){
				dd.setEmpNumber(empNumber);
			}
			if(!StringUtils.isBlank(department)){
				dd.setDepartment(department);
			}
			if(!StringUtils.isBlank(mobile)){
				dd.setMobile(mobile);
			}
			if(proxyId!=null&&proxyId!=-1L){
				dd.setDepartment("A0001");
				dd.setDepartmentId(proxyId);//把代理的ID设置成部门ID 
				dd.setProxyAdmin(false);
			}else{
				dd.setDepartment("B0001");
				dd.setDepartmentId(proxyId);
			}
			if(!StringUtils.isBlank(position)){
				dd.setPosition(position);
			}
			if(!StringUtils.isBlank(status)){
				dd.setStatus(status);
			}
			if(isMobileUser!=null){
				dd.setMobileUser(isMobileUser);
			}
			if(!StringUtils.isBlank(newRoles)){
				List<SysRole> roles = sysUserService.listRolesByIDString(newRoles);
				dd.getRoles().clear();
				dd.getRoles().addAll(roles);
			}else{
				dd.getRoles().clear();
			}
			sysUserService.save(dd);
			info("设置用户：用户名："+dd.getRealName());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			error("设置用户失败！");
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
    
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editUser", method = {RequestMethod.GET, RequestMethod.POST})
	public String editUser(HttpServletRequest request, HttpServletResponse response,
			String userId,
			String password,
			String realName,
			String empNumber,
			String position,
			String status,
			String newRoles,
			String department,
			Boolean isMobileUser,
			Long proxyId,
			String mobile) {
		try {
			if(StringUtils.isBlank(realName)||StringUtils.isBlank(position)||StringUtils.isBlank(userId)){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			if("A0001".equals(department)&&(proxyId==null)){//如果是代理
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
				return NONE;
			}
			SysUser sysUser = sysUserService.getById(SysUser.class,Long.valueOf(userId));
			if(sysUser.getDepartment().equals("C0001")){
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,"合作商在商家管理里修改!"));
				return NONE;
			}
			//加入代理区分
			SysUser  dd = null;
			//如果ID是编辑
			if(!StringUtils.isBlank(userId)){
				dd = (SysUser)sysUserService.getById(SysUser.class, Long.valueOf(userId));
			}
			if(!StringUtils.isBlank(password)){
				password = MD5.getInstance().getMD5(password);
				dd.setPassword(password);
			}
			if(!StringUtils.isBlank(realName)){
				dd.setRealName(realName);
			}
			if(!StringUtils.isBlank(empNumber)){
				dd.setEmpNumber(empNumber);
			}
			if(proxyId!=null&&proxyId!=-1L){
				dd.setDepartment("A0001");
				dd.setDepartmentId(proxyId);//把代理的ID设置成部门ID
			}else{
				dd.setDepartment("B0001");
				dd.setDepartmentId(proxyId);//把代理的ID设置成部门ID
			}
			if(!StringUtils.isBlank(position)){
				dd.setPosition(position);
			}
			if(!StringUtils.isBlank(status)){
				dd.setStatus(status);
			}
			if(isMobileUser!=null){
				dd.setMobileUser(isMobileUser);
			}
			if(!StringUtils.isBlank(mobile)){
				dd.setMobile(mobile);
			}
			if(!StringUtils.isBlank(newRoles)){
				List<SysRole> roles = sysUserService.listRolesByIDString(newRoles);
				dd.getRoles().clear();
				dd.getRoles().addAll(roles);
			}else{
				dd.getRoles().clear();
			}
			sysUserService.save(dd);
			info("设置用户：用户名："+dd.getRealName());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			error("设置用户失败！");
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/changePassword", method = {RequestMethod.GET, RequestMethod.POST})
	public String changePassword(HttpServletRequest request,
			HttpServletResponse response,
			String srcPassword,
			String password) {
		try {
			if(StringUtils.isBlank(password)||StringUtils.isBlank(srcPassword)){
				JSONHelper.returnInfo(JSONHelper.appCode2json("密码不能为空"));
				return NONE;
			}
			//加入代理区分
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			SysUser dd = (SysUser)sysUserService.getById(SysUser.class, Long.valueOf(user.getId()));
			srcPassword = MD5.getInstance().getMD5(srcPassword);
			if(!srcPassword.equals(dd.getPassword())){
				JSONHelper.returnInfo(JSONHelper.appCode2json("原始密码错误"));
				return NONE;
			}
			password = MD5.getInstance().getMD5(password);
			dd.setPassword(password);
			sysUserService.save(dd);
			info("修改密码：用户名："+dd.getRealName());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			error("修改密码失败！");
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryUser", method = {RequestMethod.GET, RequestMethod.POST})
	public String queryUser(HttpServletRequest request, HttpServletResponse response,String userId) {
		try {
			if(!StringUtils.isBlank(userId)){
				Map ret = new HashMap();
				SysUser dd= sysUserService.getById(SysUser.class, Long.valueOf(userId));
				StringBuffer sysroles = new StringBuffer();
				for(SysRole sr:dd.getRoles()){
					sysroles.append(sr.getId()+",");
				}
				if(!StringUtils.isBlank(sysroles)){
					ret.put("sysroles", sysroles.substring(0, sysroles.length()-1));
				}
				ret.put("sysUser", dd);
				JSONHelper.returnInfo(JSONHelper.bean2json(ret,true));
	    		return NONE;
			}
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
	
	/**
	 * 
	 * @Title:        listRoles 
	 * @Description:  TODO 角色列表
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param search
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月11日 上午11:52:23
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listRoles", method = {RequestMethod.GET, RequestMethod.POST})
	public String listRoles(HttpServletRequest request, HttpServletResponse response,String search) {
		try {
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
			}
			List page = sysUserService.listRoles(search);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page){
				Map mo = new HashMap();
				mo.put("id", ((SysRole)sm).getId());
				mo.put("roleName", ((SysRole)sm).getRoleName());
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.size()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	/**
	 * 
	 * @Title:        listRoles 
	 * @Description:  TODO 角色列表
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param search
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月11日 上午11:52:23
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listProxy", method = {RequestMethod.GET, RequestMethod.POST})
	public String listProxy(HttpServletRequest request, HttpServletResponse response,String search) {
		try {
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
			}
			List page = sysUserService.listProxy(search);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page){
				Map mo = new HashMap();
				mo.put("id", ((SysUser)sm).getId());
				mo.put("proxyName", ((SysUser)sm).getRealName());
				list.add(mo);
			}
			Map mo = new HashMap();
			mo.put("id", -1L);
			mo.put("proxyName", "公司直属");
			list.add(0,mo);
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.size()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	/**
	 * 
	 * @Title:        listRoles 
	 * @Description:  TODO 角色列表
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param search
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月11日 上午11:52:23
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listProxy2", method = {RequestMethod.GET, RequestMethod.POST})
	public String listProxy2(HttpServletRequest request, HttpServletResponse response,String search) {
		try {
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
			}
			List<Map> list = new ArrayList<Map>();
			if(user.getDepartment().equals("A0001")){
				Map mo = new HashMap();
				mo.put("id", user.getId());
				mo.put("proxyName", user.getRealName());
				list.add(mo);
			}else if(user.getDepartment().equals("B0001")){
				List page = sysUserService.listProxy(search);
				for(Object sm:page){
					Map mo = new HashMap();
					mo.put("id", ((SysUser)sm).getId());
					mo.put("proxyName", ((SysUser)sm).getRealName());
					list.add(mo);
				}
			}else{
				SysUser parentUser = sysUserService.getById(SysUser.class, user.getParentId());
				Map mo = new HashMap();
				mo.put("id", parentUser.getId());
				mo.put("proxyName", parentUser.getRealName());
				list.add(mo);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("totalCount","0");
			map.put("root", list);
			Map otherInfo = new HashMap();
			otherInfo.put("userType", user.getDepartment());
			otherInfo.put("userId", list.get(0).get("id"));
			map.put("message", JSONObject.fromObject(otherInfo).toString());
			JSONHelper.returnInfo(JSONObject.fromObject(map));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	/**
	 * 
	 * @Title:        sysLogListByPage 
	 * @Description:  TODO 查询日志
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param search
	 * @param:        @param currentPage
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月11日 上午11:52:08
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/sysLogListByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String sysLogListByPage(HttpServletRequest request, HttpServletResponse response,String search,int currentPage) {
		try {
			if(!StringUtils.isBlank(search)){
				search = URLDecoder.decode(search,"UTF-8"); 
			}
			Page page = sysUserService.sysLogListByPage(search, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((Map)sm).get("id"));
				mo.put("userId", ((Map)sm).get("userid"));
				mo.put("realName", ((Map)sm).get("realname"));
				mo.put("userName", ((Map)sm).get("username"));
				mo.put("createTime", ((Map)sm).get("dated")==null?"":((Map)sm).get("dated").toString());
				mo.put("userIp", ((Map)sm).get("userip"));
				mo.put("logLevel", ((Map)sm).get("loglevel"));
				mo.put("message", ((Map)sm).get("message"));
				mo.put("logger", ((Map)sm).get("logger"));
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
}
