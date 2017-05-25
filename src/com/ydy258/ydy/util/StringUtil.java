package com.ydy258.ydy.util;

import java.util.HashMap;
import java.util.Iterator;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringUtil {
	public static boolean isNotEmpty(String str){
		if(str==null || "".equals(str)){
			return false;
		}
		return true;
	}
	public static boolean isEmpty(String str){
		if(str==null || "".equals(str)){
			return true;
		}
		return false;
	}
	
	public static String objectToString(Object object){
		if(object!=null){
			return object.toString();
		}else{
			return "";
		}
		
	}
	
	public static void main(String[] args) {
        String str = "哈，什么？？";
        String str2 = str.replace('，', ',') ;
        System.out.println(str2);
}
	
	/**
     * 半角转全角
     * @param input String.
     * @return 全角字符串.
     */
    public static String toSBC(String input) {
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == ' ') {
                 c[i] = '\u3000';
               } else if (c[i] < '\177') {
                 c[i] = (char) (c[i] + 65248);

               }
             }
             return new String(c);
    }

    /**
     * 全角转半角
     * @param input String.
     * @return 半角字符串
     */
    public static String toDBC(String input) {
        

             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == '\u3000') {
                 c[i] = ' ';
               } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                 c[i] = (char) (c[i] - 65248);

               }
             }
        String returnString = new String(c);
        
             return returnString;
    }
    
    
    public static HashMap<String, String> toHashMap(String object)  
	   {  
			JSONObject object1 = JSONObject.parseObject(object);
			ObjectMapper mapper = new ObjectMapper();  
	   
	    	HashMap<String, String> data = new HashMap<String, String>();  
	       // 将json字符串转换成jsonObject  
	    	Iterator it = object1.keySet().iterator();  
	       // 遍历jsonObject数据，添加到Map对象  
	       while (it.hasNext())  
		       {  
		           String key = String.valueOf(it.next());  
		           String value = (String) object1.get(key);  
		           data.put(key, value);  
		       }  
	       return data;  
	   }  
}
