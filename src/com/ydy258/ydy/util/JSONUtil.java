package com.ydy258.ydy.util;

import net.sf.json.JSONObject;


@SuppressWarnings("unchecked")
public class JSONUtil {
	
	public String jsonObject(String jsonStr,String args){
		JSONObject jb=new JSONObject();   
		//��json��ʽ���ַ�ת��Ϊjson���󣬲�ȡ�øö���ġ�userName������ֵ  
		String o=(String)jb.fromObject(jsonStr).get(args);  
		return o;
	}
	

}
