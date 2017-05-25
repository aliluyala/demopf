

package com.ydy258.ydy.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.Setting;
import com.ydy258.ydy.entity.BusinessRules;
import com.ydy258.ydy.entity.Consignor;
import com.ydy258.ydy.entity.SysPermission;
import com.ydy258.ydy.entity.SysRole;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.service.ISysPermissionService;
import com.ydy258.ydy.service.ISysUserService;
import com.ydy258.ydy.util.JSONHelper;
import com.ydy258.ydy.util.MD5;
import com.ydy258.ydy.util.SettingUtils;


@Controller
@RequestMapping("/sys/user/")
public class SysPermissionAction extends BaseFacade {
	
	@Autowired
	private ISysPermissionService sysPermissionService; 
	
	@Autowired
	private ISysUserService sysUserService;
	

	/**
	 * 
	 * @Title:        userLogin 
	 * @Description:  TODO 用户登录
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @param userName
	 * @param:        @param passWord
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午5:09:42
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/userLogin", method = {RequestMethod.GET, RequestMethod.POST})
	public String userLogin(HttpServletRequest request, HttpServletResponse response,String userName,String passWord) {
		try {
			String agent = request.getHeader("User-Agent");
			passWord = MD5.getInstance().getMD5(passWord);
			SysUser user = sysPermissionService.userLogin(userName, passWord);
			if(null!=user){
				if(!user.getStatus().equals("effective")){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.login_err_code,Constants.login_err_msg));
		    		return NONE;
				}
				if(agent.contains("Dalvik")&&user.getMobileUser()==null){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.login_err_code,Constants.login_err_msg));
		    		return NONE;
				}
				if(agent.contains("Dalvik")&&!user.getMobileUser()){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.login_err_code,Constants.login_err_msg));
		    		return NONE;
				}
				for(SysRole role:user.getRoles()){
					if(role.getPermissions()!=null&&role.getPermissions().size()>0){
						role.buttons.addAll(role.getPermissions());
					}
					/*for(SysPermission permission:role.getPermissions()){
						List<SysPermission> buttons = sysPermissionService.listByParentId(permission.getId());
						if(buttons!=null&&buttons.size()>0){
							role.buttons.addAll(buttons);
						}
					}*/
				}
	    		HttpSession session = request.getSession();
	    		session.setAttribute(Constants.LOGIN_KEY, user);
	    		info("登录成功！");
	    		JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
	    		return NONE;
	    	}
			Truck truck = sysPermissionService.truckUserLogin(userName, passWord);
			if(null!=truck){
				SysRole role = sysUserService.getRoleByName("车主");
				if(role.getPermissions()!=null&&role.getPermissions().size()>0){
					role.buttons.addAll(role.getPermissions());
				}
				SysUser trucks = new SysUser();
				trucks.setId(truck.getId());
				trucks.setRealName(truck.getDriverName());
				trucks.setUserName(truck.getMobile());
				trucks.getRoles().add(role);
				trucks.setDepartmentId(truck.getId());
				trucks.setDepartment("D0001");
				
				HttpSession session = request.getSession();
	    		session.setAttribute(Constants.LOGIN_KEY, trucks);
	    		info("登录成功！");
	    		JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
	    		return NONE;
			}
			Consignor consignor = sysPermissionService.consignorUserLogin(userName, passWord);
			if(null != consignor){
				SysRole role = sysUserService.getRoleByName("货主");
				if(role.getPermissions()!=null&&role.getPermissions().size()>0){
					role.buttons.addAll(role.getPermissions());
				}
				SysUser trucks = new SysUser();
				trucks.setId(consignor.getId());
				trucks.setRealName(consignor.getRealName());
				trucks.setUserName(consignor.getMobile());
				trucks.getRoles().add(role);
				trucks.setDepartmentId(consignor.getId());
				trucks.setDepartment("E0001");
				
				HttpSession session = request.getSession();
	    		session.setAttribute(Constants.LOGIN_KEY, trucks);
	    		info("登录成功！");
	    		JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
	    		return NONE;
			}
			else{
	    		JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.login_err_code,Constants.login_err_msg));
	    		return NONE;
	    	}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }

//	@SuppressWarnings("unchecked")
//	@RequestMapping(value = "/memberLogin", method = {RequestMethod.GET, RequestMethod.POST})
//	public String memberLogin(HttpServletRequest request, HttpServletResponse response,String mobile,String passWord) {
//		try {
//			Consignor consignor = null;
//			passWord = MD5.getInstance().getMD5(passWord);
//			Truck truck = sysPermissionService.truckLogin(mobile, passWord);
//			if(truck!=null){
//				SysRole role = sysPermissionService.getById(SysRole.class, truck.getRoleId());
//				role.buttons.addAll(role.getPermissions());
//				HttpSession session = request.getSession();
//	    		session.setAttribute(Constants.LOGIN_KEY, truck);
//	    		info("登录成功！");
//	    		JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
//	    		return NONE;
//			}else{
//				consignor = sysPermissionService.consignorLogin(mobile, passWord);
//				if(consignor!=null){
//					SysRole role = sysPermissionService.getById(SysRole.class, consignor.getRoleId());
//					role.buttons.addAll(role.getPermissions());
//					HttpSession session = request.getSession();
//		    		session.setAttribute(Constants.LOGIN_KEY, consignor);
//		    		info("登录成功！");
//		    		JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
//		    		return NONE;
//				}else{
//					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.login_err_code,Constants.login_err_msg));
//		    		return NONE;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
//			return NONE;
//		}
//    }

	
	/**
	 * 
	 * @Title:        logout 
	 * @Description:  TODO 用户注销
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午5:10:48
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();
    		session.removeAttribute(Constants.LOGIN_KEY);
    		JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	/**
	 * 
	 * @Title:        userPermission 
	 * @Description:  TODO 权限列表
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午5:11:32
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/userPermission", method = {RequestMethod.GET, RequestMethod.POST})
	public String userPermission(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
		Set<SysPermission> allPermissions = new HashSet<SysPermission>();
		Set<SysRole> roles = user.getRoles();
		for(SysRole r:roles){
			allPermissions.addAll(r.getPermissions());
		}
		List<Map> ret = new ArrayList<Map>();
		for(SysPermission p:allPermissions){
			if(p.getParentId()==-1){
				Map<String,Object> n = new HashMap<String,Object>();
				n.put("text", p.getPermissionName());
				n.put("mid", p.getId()+"");
				n.put("cls", "folder");
				n.put("leaf", false);
				n.put("expanded", true);
				List<Map> list = this.childrens(allPermissions, p.getId());
				n.put("children", list);
				ret.add(n);
			}
		}
		JSONHelper.returnInfo(JSONArray.fromObject(ret));
		return NONE;
    }
    

	final private Setting setting = SettingUtils.get();
	/**
	 * 
	 * @Title:        userPermission 
	 * @Description:  TODO 权限列表
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午5:11:32
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/existsInUserPermissions", method = {RequestMethod.GET, RequestMethod.POST})
	public String existsInUserPermissions(HttpServletRequest request, HttpServletResponse response,String requestpath) {
		HttpSession session = request.getSession();
		SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
		boolean success = false;
		if(setting.getPermission().indexOf(requestpath)>-1){
			success = true;
		}else{
			first:for(SysRole role:user.getRoles()){
				   sencond:for(SysPermission permission:role.buttons){
					  //System.out.println(requestpath+":---->"+permission.getPermissionUrl());
					  if(!StringUtils.isBlank(permission.getPermissionUrl())&&requestpath.endsWith(permission.getPermissionUrl())){
						  success = true;
						  break first;
					  }
				   }
			   }
		}
		JSONHelper.returnInfo(JSONHelper.bean2json(success));
		return NONE;
    }
	
	/**
	 * 
	 * @Title:        getUser 
	 * @Description:  TODO 得session里的用户信息
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午5:11:56
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getUser", method = {RequestMethod.GET, RequestMethod.POST})
	public String getUser(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
		Map ret = new HashMap();
		ret.put("realName", user.getRealName());
		ret.put("userName", user.getUserName());
		JSONHelper.returnInfo(JSONHelper.bean2json(ret));
		return NONE;
    }
    
    private List<Map> childrens(Set<SysPermission> ps,Long parentId){
    	List<Map> list = new ArrayList<Map>();
    	for(SysPermission p:ps){
    		if(p.getParentId().equals(parentId)){
    			Map<String,Object> n = new HashMap<String,Object>();
				n.put("text", p.getPermissionName());
				n.put("mid", p.getId()+"");
				n.put("cls", "file");
				n.put("leaf", true);
				n.put("url", p.getPermissionUrl());
				n.put("children", new ArrayList<Map>());
				list.add(n);
    		}
    	}
    	return list;
    }
    
    
   /**
    * 
    * @Title:        save 
    * @Description:  TODO 保存权限
    * @param:        @param request
    * @param:        @param response
    * @param:        @return    
    * @return:       String    
    * @throws 
    * @author        Administrator
    * @Date          2015年8月7日 下午5:12:54
    */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/save", method = {RequestMethod.GET, RequestMethod.POST})
	public String save(HttpServletRequest request, HttpServletResponse response,String id,String parentId,String sonId,String name,String status,String url) {
		try {
			SysPermission dd = new SysPermission();
			if(!StringUtils.isBlank(id)){
				dd = sysPermissionService.getById(SysPermission.class, Long.valueOf(id));
			}
			if(!StringUtils.isBlank(id)){
				dd.setId(Long.valueOf(id));
			}
			if(!StringUtils.isBlank(parentId)){
				dd.setParentId(Long.valueOf(parentId));
			}
			if(!StringUtils.isBlank(sonId)){
				dd.setParentId(Long.valueOf(sonId));
			}
			if(!StringUtils.isBlank(name)){
				dd.setPermissionName(name);
			}
			if(!StringUtils.isBlank(status)){
				dd.setStatus(status);
			}
			if(!StringUtils.isBlank(url)){
				dd.setPermissionUrl(url);
			}
			sysPermissionService.save(dd);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
	
	/**
	 * 
	 * @Title:        query 
	 * @Description:  TODO 查询指定ID 的 权限
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午5:13:16
	 */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
	public String query(HttpServletRequest request, HttpServletResponse response,String id) {
		try {
			if(!StringUtils.isBlank(id)){
				SysPermission dd= (SysPermission)sysPermissionService.getById(SysPermission.class, Long.valueOf(id));
				JSONHelper.returnInfo(JSONHelper.bean2json(dd,true));
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
	
    /**
     * 
     * @Title:        queryParent 
     * @Description:  TODO 
     * @param:        @param request
     * @param:        @param response
     * @param:        @return    
     * @return:       String    
     * @throws 
     * @author        Administrator
     * @Date          2015年8月7日 下午5:14:05
     */
    @SuppressWarnings("unchecked")
   	@RequestMapping(value = "/queryParent", method = {RequestMethod.GET, RequestMethod.POST})
   	public String queryParent(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<SysPermission> dics = sysPermissionService.queryParent();
			
			SysPermission dd = new SysPermission();
			dd.setId(Long.valueOf(-1));
			dd.setPermissionName("根结点");
			dics.add(0, dd);
			JSONHelper.returnInfo(JSONHelper.list2json(dics));
    		return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
    
    
    /**
     * 
     * @Title:        queryParent 
     * @Description:  TODO 
     * @param:        @param request
     * @param:        @param response
     * @param:        @return    
     * @return:       String    
     * @throws 
     * @author        Administrator
     * @Date          2015年8月7日 下午5:14:05
     */
    @SuppressWarnings("unchecked")
   	@RequestMapping(value = "/querySon", method = {RequestMethod.GET, RequestMethod.POST})
   	public String querySon(HttpServletRequest request, HttpServletResponse response,Long parentId) {
		try {
			List<SysPermission>  dics = sysPermissionService.listByParentId(parentId);
			JSONHelper.returnInfo(JSONHelper.list2json(dics));
    		return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}

    
    @SuppressWarnings("unchecked")
   	@RequestMapping(value = "/listByPage", method = {RequestMethod.GET, RequestMethod.POST})
   	public String listByPage(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Map> list = new ArrayList<Map>();
			List<SysPermission> dics = sysPermissionService.queryParent();
			for(SysPermission dd:dics){
				Map<String,Object> root = new HashMap<String,Object>();
				root.put("id", dd.getId());
				root.put("parentId", dd.getParentId());
				root.put("name", dd.getPermissionName());
				root.put("status", dd.getStatus());
				root.put("expanded", false);
				
				dics = sysPermissionService.listByParentId(dd.getId());
				
				List<Map> chs2 = new ArrayList<Map>();
				for(SysPermission d:dics){
					Map rdm = new HashMap();
					rdm.put("id",d.getId());
					rdm.put("expanded", false);
					rdm.put("name",d.getPermissionName());
					rdm.put("url",d.getPermissionUrl());
					rdm.put("parentId",d.getParentId());
					rdm.put("status", d.getStatus());
					rdm.put("leaf", false);
					List<Map> chs3 = new ArrayList<Map>();
					dics = sysPermissionService.listByParentId(d.getId());
					for(SysPermission buttion:dics){
						Map b = new HashMap();
						b.put("id",buttion.getId());
						b.put("expanded", false);
						b.put("name",buttion.getPermissionName());
						b.put("url",buttion.getPermissionUrl());
						b.put("parentId",buttion.getParentId());
						b.put("status", buttion.getStatus());
						b.put("leaf", true);
						chs3.add(b);
					}
					rdm.put("children", chs3);
					chs2.add(rdm);
				}
				root.put("children", chs2);
				list.add(root);
			}
			JSONHelper.returnInfo(net.sf.json.JSONArray.fromObject(list));
			//JSONHelper.returnInfo(JSONHelper.bean2json(root));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
    
    @SuppressWarnings("unchecked")
   	@RequestMapping(value = "/listPermission", method = {RequestMethod.GET, RequestMethod.POST})
   	public String listPermission(HttpServletRequest request, HttpServletResponse response,String roleId) {
		try {
			List<Map<String,Object>> userpers = new ArrayList<Map<String,Object>>();
			if(!StringUtils.isBlank(roleId)){
				userpers = sysPermissionService.rolePermissionByRoleID(roleId);
			}
			List<Map> list = new ArrayList<Map>();
			List<SysPermission> dics = sysPermissionService.queryParent();
			for(SysPermission dd:dics){
				Map<String,Object> root = new HashMap<String,Object>();
				root.put("id", dd.getId());
				root.put("parentId", dd.getParentId());
				root.put("name", dd.getPermissionName());
				root.put("status", dd.getStatus());
				root.put("expanded", true);
				root.put("checked", isExit(userpers,dd.getId()));
				dics = sysPermissionService.listByParentId(dd.getId());
				
				List<Map> chs2 = new ArrayList<Map>();
				for(SysPermission d:dics){
					Map rdm = new HashMap();
					rdm.put("id",d.getId());
					rdm.put("expanded", true);
					rdm.put("name",d.getPermissionName());
					rdm.put("url",d.getPermissionUrl());
					rdm.put("parentId",d.getParentId());
					rdm.put("status", d.getStatus());
					rdm.put("checked", isExit(userpers,d.getId()));
					rdm.put("leaf", false);
					List<Map> chs3 = new ArrayList<Map>();
					dics = sysPermissionService.listByParentId(d.getId());
					for(SysPermission buttion:dics){
						Map b = new HashMap();
						b.put("id",buttion.getId());
						b.put("expanded", true);
						b.put("name",buttion.getPermissionName());
						b.put("url",buttion.getPermissionUrl());
						b.put("parentId",buttion.getParentId());
						b.put("status", buttion.getStatus());
						b.put("checked", isExit(userpers,buttion.getId()));
						b.put("leaf", true);
						chs3.add(b);
					}
					rdm.put("children", chs3);
					chs2.add(rdm);
				}
				root.put("children", chs2);
				list.add(root);
			}
			JSONHelper.returnInfo(net.sf.json.JSONArray.fromObject(list));
			//JSONHelper.returnInfo(JSONHelper.bean2json(root));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	private boolean isExit(List<Map<String,Object>> userper,Long id){
		if(userper!=null){
			for(Map<String,Object> up:userper){
				if((up.get("permission_id")+"").equals(id+"")){
					return true;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
   	@RequestMapping(value = "/saveRole", method = {RequestMethod.GET, RequestMethod.POST})
   	public String saveRole(HttpServletRequest request, HttpServletResponse response,String roleId,String name,String status,String newpermissions) {
		try {
			SysRole dd = new SysRole();
			if(!StringUtils.isBlank(roleId)){
				dd = (SysRole)sysPermissionService.getById(SysRole.class, Long.valueOf(roleId));
			}
			if(!StringUtils.isBlank(name)){
				dd.setRoleName(name);
			}
			if(!StringUtils.isBlank(status)){
				dd.setStatus(status);
			}
			dd.setCreatedDate(new Date());
			
			if(!StringUtils.isBlank(newpermissions)){
				List<SysPermission> dics = sysPermissionService.listSysPermissionByIDString(newpermissions);
				dd.getPermissions().clear();
				dd.getPermissions().addAll(dics);
			}else{
				dd.getPermissions().clear();
			}
			sysPermissionService.save(dd);
			info("设置角色！ 角色名称："+dd.getRoleName());
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			error("设置角色失败！系统出错！");
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
    
	
	@SuppressWarnings("unchecked")
   	@RequestMapping(value = "/queryRole", method = {RequestMethod.GET, RequestMethod.POST})
   	public String queryRole(HttpServletRequest request, HttpServletResponse response,String id) {
		try {
			if(!StringUtils.isBlank(id)){
				Map ret = new HashMap();
				SysRole dd= (SysRole)sysPermissionService.getById(SysRole.class, Long.valueOf(id));
				StringBuffer sysroles = new StringBuffer();
				List<Map<String,Object>> userpers = sysPermissionService.rolePermissionByRoleID(id);
				for(Map<String,Object> up:userpers){
					String v = up.get("permission_id").toString();
					sysroles.append(v+",");
				}
				ret.put("syspermissions", sysroles.toString());
				ret.put("sysrole", dd);
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
   	@RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
   	public String test(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<SysUser> page = sysUserService.listProxy(null);
			BusinessRules br = sysUserService.getById(BusinessRules.class, 3740L);
			for(SysUser u:page){
				BusinessRules b = new BusinessRules();
				b.setActionUrl(br.getActionUrl());
				b.setProxyUserId(u.getId());
				b.setDescript(br.getDescript());
				b.setPrice(br.getPrice());
				b.setDiscount(br.getDiscount());
				b.setType(br.getType());
				b.setCost(br.getCost());
				b.setDiscountPrice(br.getDiscountPrice());
				b.setDiscountPoints(br.getDiscountPoints());
				b.setProxyPrice(br.getProxyPrice());
				sysUserService.save(b);
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
   	@RequestMapping(value = "/test2", method = {RequestMethod.GET, RequestMethod.POST})
   	public String test2(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<SysUser> page = sysUserService.listProxy(null);
			BusinessRules br = sysUserService.getById(BusinessRules.class, 3736L);
			BusinessRules br1 = sysUserService.getById(BusinessRules.class, 3737L);
			BusinessRules br2 = sysUserService.getById(BusinessRules.class, 3739L);
			for(SysUser u:page){
				BusinessRules b = new BusinessRules();
				b.setActionUrl(br.getActionUrl());
				b.setProxyUserId(u.getId());
				b.setDescript(br.getDescript());
				b.setPrice(br.getPrice());
				b.setDiscount(br.getDiscount());
				b.setType(br.getType());
				b.setCost(br.getCost());
				b.setDiscountPrice(br.getDiscountPrice());
				b.setDiscountPoints(br.getDiscountPoints());
				b.setProxyPrice(br.getProxyPrice());
				sysUserService.save(b);
				
				BusinessRules b2 = new BusinessRules();
				b2.setActionUrl(br2.getActionUrl());
				b2.setProxyUserId(u.getId());
				b2.setDescript(br2.getDescript());
				b2.setPrice(br2.getPrice());
				b2.setDiscount(br2.getDiscount());
				b2.setType(br2.getType());
				b2.setCost(br2.getCost());
				b2.setDiscountPrice(br2.getDiscountPrice());
				b2.setDiscountPoints(br2.getDiscountPoints());
				b2.setProxyPrice(br2.getProxyPrice());
				sysUserService.save(b2);
				
				BusinessRules b1 = new BusinessRules();
				b1.setActionUrl(br1.getActionUrl());
				b1.setProxyUserId(u.getId());
				b1.setDescript(br1.getDescript());
				b1.setPrice(br1.getPrice());
				b1.setDiscount(br1.getDiscount());
				b1.setType(br1.getType());
				b1.setCost(br1.getCost());
				b1.setDiscountPrice(br1.getDiscountPrice());
				b1.setDiscountPoints(br1.getDiscountPoints());
				b1.setProxyPrice(br1.getProxyPrice());
				sysUserService.save(b1);
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
