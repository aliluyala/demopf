

package com.ydy258.ydy.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.configbean.Config;
import com.ydy258.ydy.service.IMemberService;
import com.ydy258.ydy.util.JSONHelper;
import com.ydy258.ydy.util.utils;


@Controller
@RequestMapping("/sys/testjedis/")
public class ConfigAction extends BaseFacade  {
	
	@Autowired
	private IMemberService memberService;
	
	@Autowired
	private JedisPool jedisPool;
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/updatePICCInsuranceCargoConfig", method = {RequestMethod.GET, RequestMethod.POST})
	public String updatePICCInsuranceCargoConfig(HttpServletRequest request, HttpServletResponse response,
		     String xhMainGlausesCode,
		     String xhGoodsTypeNo,
		     String xhMainGlauses,
		     String xhRatio,
		     String zhMainGlausesCode,
		     String zhMainGlauses,
		     String zhRatio,
		     String zhGoodsTypeNo,
		     String xhprice,
		     String xhcost,
		     String zhprice,
		     String zhcost
			) {
		try {
			Jedis jedis = jedisPool.getResource();
	        String key = "config:piccInsuranceConfig";
	        byte[] cacheElement = jedis.get(key.getBytes());
	        Map<String,String>  config = null;
	        if (cacheElement != null) {
	        	config = (HashMap<String,String>)utils.unserialize(cacheElement);
	        	if(!StringUtils.isBlank(xhMainGlausesCode)){
	        		config.put("xhMainGlausesCode", xhMainGlausesCode);
	        	}
	        	if(!StringUtils.isBlank(xhGoodsTypeNo)){
	        		config.put("xhGoodsTypeNo", xhGoodsTypeNo);
	        	}
	        	if(!StringUtils.isBlank(xhMainGlauses)){
	        		config.put("xhMainGlauses", xhMainGlauses);
	        	}
	        	if(!StringUtils.isBlank(zhMainGlausesCode)){
	        		config.put("zhMainGlausesCode", zhMainGlausesCode);
	        	}
	        	if(!StringUtils.isBlank(zhMainGlauses)){
	        		config.put("zhMainGlauses", zhMainGlauses);
	        	}
	        	if(!StringUtils.isBlank(zhRatio)){
	        		config.put("zhRatio", zhRatio);
	        	}
	        	if(!StringUtils.isBlank(zhGoodsTypeNo)){
	        		config.put("zhGoodsTypeNo", zhGoodsTypeNo);
	        	}
	        	if(!StringUtils.isBlank(zhprice)){
	        		config.put("zhprice", zhprice);
	        	}
	        	if(!StringUtils.isBlank(zhcost)){
	        		config.put("zhcost", zhcost);
	        	}
	        	if(!StringUtils.isBlank(xhprice)){
	        		config.put("xhprice", xhprice);
	        	}
	        	if(!StringUtils.isBlank(xhcost)){
	        		config.put("xhcost", xhcost);
	        	}
	        }
	        jedis.set(key.getBytes(), utils.serialize(config));
	        JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listPICCInsuranceCargoConfig", method = {RequestMethod.GET, RequestMethod.POST})
	public String listPICCInsuranceCargoConfig(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<Config> retList = new ArrayList<Config>();
			Jedis jedis = jedisPool.getResource();
	        String key = "config:piccInsuranceConfig";
	        byte[] cacheElement = jedis.get(key.getBytes());
	        Map<String,String>  config = null;
	        if (cacheElement != null) {
	        	config = (HashMap<String,String>)utils.unserialize(cacheElement);
	        	
	        	Config configbean = new Config();
	        	configbean.setKey("xhMainGlausesCode");
	        	configbean.setValue(config.get("xhMainGlausesCode"));
	        	retList.add(configbean);
	        	
	        	configbean = new Config();
	        	configbean.setKey("xhGoodsTypeNo");
	        	configbean.setValue(config.get("xhGoodsTypeNo"));
	        	retList.add(configbean);
	        	
	        	configbean = new Config();
	        	configbean.setKey("xhMainGlauses");
	        	configbean.setValue(config.get("xhMainGlauses"));
	        	retList.add(configbean);
	        	
	        	configbean = new Config();
	        	configbean.setKey("xhRatio");
	        	configbean.setValue(config.get("xhRatio"));
	        	retList.add(configbean);
	        	
	        	configbean = new Config();
	        	configbean.setKey("xhprice");
	        	configbean.setValue(config.get("xhprice"));
	        	retList.add(configbean);
	        	
	        	configbean = new Config();
	        	configbean.setKey("xhcost");
	        	configbean.setValue(config.get("xhcost"));
	        	retList.add(configbean);
	        	
	        	configbean = new Config();
	        	configbean.setKey("zhprice");
	        	configbean.setValue(config.get("zhprice"));
	        	retList.add(configbean);
	        	
	        	configbean = new Config();
	        	configbean.setKey("zhcost");
	        	configbean.setValue(config.get("zhcost"));
	        	retList.add(configbean);
	        }
	        JSONHelper.returnInfo(JSONHelper.list2json(retList));
    		return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
}
