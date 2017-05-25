package com.ydy258.ydy.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.dao.IBaseDao;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.service.IBaseService;

public class IBaseServiceImpl implements IBaseService  {
	@Autowired
	private IBaseDao baseDaoImpl;
	
	public static final Logger logger  =  Logger.getLogger("ydysystemlog");
	
	public  void info(String message){
		HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", user.getId());
		map.put("userName", user.getUserName());
		map.put("realName", user.getRealName());
		map.put("message", message);
		logger.info(JSONObject.fromObject(map).toString());
	}
	
	/**
	 * 保存 或修改
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void save(Object entity) {
		// TODO Auto-generated method stub
		baseDaoImpl.save(entity);
	}

	/**
	 * 修改
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void update(Object entity) {
		// TODO Auto-generated method stub
		baseDaoImpl.update(entity);
	}

	/**
	 * 查询实体
	 */
	@Override
	@Transactional(readOnly = true)
	public <T> T getById(Class<T> clazz, Object id) {
		return baseDaoImpl.getById(clazz, id);
	}

	/**
	 * 删除
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public <T> void delete(Class<T> clazz, Long id) throws Exception {
		baseDaoImpl.delete(clazz, id);
		
	}
}