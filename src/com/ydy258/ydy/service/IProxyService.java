package com.ydy258.ydy.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

import com.ydy258.ydy.entity.TInsurance;
import com.ydy258.ydy.util.Page;


public interface IProxyService extends IBaseService {
	
	public Page insuranceByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public Page idCardqueryOrderByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	public Page proxyConfigListByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception;
	
	
	
	public  String rbXMLPara(String username,String BusinessLogic,String retkey,Map<String,String> args) throws Exception;
    
    public  String getKey(Map<String,String> args,String url,String pwd) throws Exception;
    
    
    public String doPost(String url,String data) throws Exception;
    
    
    public Page insuranceChinaLifeByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception ;
    
    public TInsurance getByParentId(long parentId) throws Exception ;
}
