package com.ydy258.ydy.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SuppressWarnings("unchecked")
public class JSONHelper {

	public static void returnInfo(JSONObject jso){
		HttpServletResponse response =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		response.setContentType("text/json; charset=gb2312");
		try {
			PrintWriter out = response.getWriter();
			out.print(jso);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void returnInfo(HttpServletResponse response,JSONObject jso){
		response.setContentType("text/json; charset=gb2312");
		try {
			PrintWriter out = response.getWriter();
			out.print(jso);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void returnInfo(JSONArray jso){
		HttpServletResponse response =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		response.setContentType("text/json; charset=gb2312");
		try {
			PrintWriter out = response.getWriter();
			out.print(jso);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void returnInfo(JSONObject jso,String ctype){
		HttpServletResponse response =  ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
		response.setContentType(""+ctype+"; charset=gb2312");
		try {
			PrintWriter out = response.getWriter();
			out.print(jso);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//增加�?
	public static JSONObject appCode2json(Object appCode,Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", object);
		map.put("appCode", appCode);
		return JSONObject.fromObject(map);
	}
	
	public static JSONObject appCode2json(Object appCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appCode", appCode);
		return JSONObject.fromObject(map);
	}
	
	public static JSONObject successInfo(Object appCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("appCode", appCode);
		return JSONObject.fromObject(map);
	}
	
	public static JSONObject failedInfo(Object appCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);
		map.put("appCode", appCode);
		return JSONObject.fromObject(map);
	}
	
	
	public static JSONObject successInfo(Object appCode,Object appMsg) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("appCode", appCode);
		map.put("appMsg", appMsg);
		return JSONObject.fromObject(map);
	}
	
	public static JSONObject failedInfo(Object appCode,Object appMsg) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);
		map.put("appCode", appCode);
		map.put("appMsg", appMsg);
		return JSONObject.fromObject(map);
	}
	
	public static JSONObject list2jsonWithAppCode(List list,int appCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appCode", appCode);
		map.put("root", list);
		return JSONObject.fromObject(map);
	}
	
	public static JSONObject list2json(List list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", list.size());
		map.put("root", list);
		
		return JSONObject.fromObject(map);
	}
	
	public static JSONObject bean2json(Object object,boolean success) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("data", object);
		return JSONObject.fromObject(map);
	}
	
	public static JSONObject bean2json(boolean forbidden) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("forbidden", forbidden);
		return JSONObject.fromObject(map);
	}
	
	public static JSONObject bean2json(Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", true);
		map.put("data", object);
		return JSONObject.fromObject(map);
	}
	
	public static JSONObject bean2json(Object obj, String[] exincludes) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("success", true);
		map.put("data", obj);
		
		JsonConfig jc = DateJsonValueProcessor.configJson(exincludes, "yyyy-MM-dd");
		return JSONObject.fromObject(map, jc);
	}
	
	
	public static JSONObject list2json(List list, String total) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", total);
		map.put("root", list);
		map.put("success", true);
		return JSONObject.fromObject(map);
	}
	
	
	public static JSONObject list2json(List list, String total, String[] exincludes, String datePattern) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", total);
		map.put("root", list);
		
		JsonConfig jc = DateJsonValueProcessor.configJson(exincludes, datePattern);
		return JSONObject.fromObject(map, jc);
	}
	
	
	public static JSONObject list2json(List list, String total, String[] exincludes) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", total);
		map.put("root", list);
		
		JsonConfig jc = DateJsonValueProcessor.configJson(exincludes, "yyyy-MM-dd");
		return JSONObject.fromObject(map, jc);
	}
	
	
	public static JSONObject set2json(Set set, String total, String[] exincludes) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCount", total);
		map.put("root", set);
		
		JsonConfig jc = DateJsonValueProcessor.configJson(exincludes, "yyyy-MM-dd");
		return JSONObject.fromObject(map, jc);
	}
	
	
	public static JSONArray list2JsonWithoutRoot(List list, String[] exincludes, String datePattern) {
		JsonConfig jc = DateJsonValueProcessor.configJson(exincludes, datePattern);
		return JSONArray.fromObject(list, jc);
	}
}
