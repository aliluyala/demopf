package com.ydy258.ydy.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

public class utils {
	private static final double EARTH_RADIUS = 6378.137; // 地球半径
	private static Random random = new Random(System.currentTimeMillis());
	
	private static double rad(double d) {
		return d * Math.PI / 180.0; // 计算弧长
	}

	// lng1 第一个点经度，lat1第一点纬度；lng2第二点经度，lat2第二点纬度
	public static double getDistance(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		// s = s * 1000; //换算成米
		return s; // 得到千米数

	}
	/**
     * 生成订单编号
     */
    public static synchronized String generaterOrderNumber() {
        String id = null;
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");        
              
        id = formatter.format(date)  + String.format("%04d", Math.abs(random.nextLong()) % 10000);
        
        return id;
    }
	
	public static String getIP(HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		return ip != null ? ip : "";
	}
	
	/**
     * 序列化java对象
     * 
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    /**
     * 反序列化java对象
     * 
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }	
    
    public static double convert(double value){   
        long   l1   =   Math.round(value*100);   //四舍五入   
        double   ret   =   l1/100.0;               //注意：使用   100.0   而不是   100   
        return   ret;   
    }  
}