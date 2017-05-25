package com.ydy258.ydy.controller;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.util.utils;




public class BaseFacade  implements ServletContextAware {

	public static final Logger logger  =  Logger.getLogger("ydysystemlog");
	
	public static final String NONE = null; 
	
	public static final int pageSize = 40; 
	
	public static final String start = "sysuser.";
	
	protected Map<String, String> args = new Hashtable<String, String>();
	
	/** servletContext */
	private ServletContext servletContext;
	
	@Autowired
	private JedisPool jedisPool;
	
	@Override
	public void setServletContext(ServletContext ctx) {
		// TODO Auto-generated method stub
		this.servletContext = ctx;
	}
	
	
	public static void info(String message){
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
	
	public static void error(String message){
		HttpServletRequest request =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", user.getId());
		map.put("userName", user.getUserName());
		map.put("message", message);
		map.put("realName", user.getRealName());
		logger.error(JSONObject.fromObject(map).toString());
	}
	
	
	 /**
     */
    public  void putSession1(HttpServletRequest request,Object object){
    	Cookie cookies[] = request.getCookies();
	    if (cookies!=null){
	        for(int i=0;i<cookies.length;i++){
	        	Cookie cookie = cookies[i];
	        	if ("Hm_lpvt_3c8ecbfa472e76b0340d7a701a04197e".equals(cookie.getName())){
	        		Jedis jedis = jedisPool.getResource();
	        		jedis.set((start+cookie.getValue()).getBytes(), utils.serialize(object));
	        		//jedis.pexpire((start+cookie.getValue()).getBytes(), 1000*60*30L);
	        	}
	        }
	    }
    }
    
    /**
     */
    public  Object getSession1(HttpServletRequest request){  
    	Object o = null;
    	Cookie cookies[] = request.getCookies();
	    if (cookies!=null){
	        for(int i=0;i<cookies.length;i++){
	        	Cookie cookie = cookies[i];
	        	if ("Hm_lpvt_3c8ecbfa472e76b0340d7a701a04197e".equals(cookie.getName())){
	        		Jedis jedis = jedisPool.getResource();
	        		byte[] cacheElement = jedis.get((start+cookie.getValue()).getBytes());
	        		o = utils.unserialize(cacheElement);
	        	}
	        }
	    }
	    return o;
    }
    
    /**
     */
    public  void clearSession1(HttpServletRequest request){  
    	Object o = null;
    	Cookie cookies[] = request.getCookies();
	    if (cookies!=null){
	        for(int i=0;i<cookies.length;i++){
	        	Cookie cookie = cookies[i];
	        	if ("Hm_lpvt_3c8ecbfa472e76b0340d7a701a04197e".equals(cookie.getName())){
	        		Jedis jedis = jedisPool.getResource();
	        		jedis.del((start+cookie.getValue()).getBytes());
	        	}
	        }
	    }
    }
}
