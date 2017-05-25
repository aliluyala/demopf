package com.ydy258.ydy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ydy258.ydy.configbean.RegionBean;

public class StringUtil {
	static Logger logger = Logger.getLogger(StringUtil.class);
	private static JsonGenerator jsonGenerator = null;
    private static ObjectMapper objectMapper = null;	
	
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
	
	
	public static String getIdKey(){
		return new Date().getTime()+""+new Random(10000).nextLong();
	}
	
	public static String readStringXML(String xmlString,String key){
		String xmlkeystart = "<"+key+">";
		String xmlkeyend = "</"+key+">";
		String first = xmlString.split(xmlkeystart)[1];
		String returnValue = first.split(xmlkeyend)[0];
		return returnValue;
	}
	
	public static void main(String[] args){
		long test = 1L;
		System.out.println(objectToString(test));
	}
	
	public static String dateToShortCode1() {
		Date date = new Date();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = simpledateformat.format(date);
		return s;
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
	/**
	 * 根据字符串生成地名搜索SQL条件语句
	 * @param fieldName - 待搜索的字段名
	 * @param placeName - 地名	
	 * @return
	 */
	public static String genSearchPlaceNameConditionSQL(String fieldName, String placeName) {
		if(fieldName == null || placeName == null)
			return "";
		
		if(placeName.length() == 0)
			return "";
		
		String sql = "";		
		
		try {
			if(placeName.indexOf("{") >= 0 && placeName.indexOf("}") >= 0) {
				//json格式的数据
				objectMapper = new ObjectMapper();
				try {
					jsonGenerator = objectMapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
					RegionBean[] arrRegionBean = objectMapper.readValue(placeName, RegionBean[].class);
					if(arrRegionBean != null && arrRegionBean.length > 0) {	
						ArrayList<String> conditions = new ArrayList<String>();
						for(int i=0; i<arrRegionBean.length; i++) {
							RegionBean rb = arrRegionBean[i];
							sql = "";
							if((rb.getProvince() != null && rb.getProvince().length() > 0) && 
								(rb.getCity() != null && rb.getCity().length() > 0) && 
								(rb.getArea() != null && rb.getArea().length() > 0)) {
								//省、市、县三级都有的情况，按县搜索	
								sql = String.format("(POSITION('%s' IN %s)<>0)", rb.getArea(), fieldName);
							}
							else if((rb.getProvince() != null && rb.getProvince().length() > 0) && 
									(rb.getCity() != null && rb.getCity().length() > 0) && 
									(rb.getArea() != null && rb.getArea().length() == 0)) {
								//省、市二级，按市搜索
								sql = String.format("(POSITION('%s' IN %s)<>0)", rb.getCity(), fieldName);
							}
							else if((rb.getProvince() != null && rb.getProvince().length() > 0) && 
									(rb.getCity() != null && rb.getCity().length() == 0) && 
									(rb.getArea() != null && rb.getArea().length() == 0)) {
								//只有省的情况
								sql = String.format("(POSITION('%s' IN %s)<>0)", rb.getProvince(), fieldName);
							}
							if(sql.length() > 0) {
								conditions.add(sql);
							}							
						}						
						sql = StringUtils.join(conditions," OR ");
					}
				}
				catch(Exception e) {
					logger.error(e.getMessage(), e);
				}
				finally {
					if (jsonGenerator != null) {
			            jsonGenerator.flush();
			        }
			        if (!jsonGenerator.isClosed()) {
			            jsonGenerator.close();
			        }
			        jsonGenerator = null;
			        objectMapper = null;
				}
			}
			else {
				sql = String.format("POSITION('%s' IN %s)<>0", placeName, fieldName);
			}		
		}
		catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.debug("[StringUtil]return conditions=" + sql);
		return sql;
	}
}