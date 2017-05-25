package com.ydy258.ydy.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.Setting;
import com.ydy258.ydy.dao.IProxyDao;
import com.ydy258.ydy.entity.TInsurance;
import com.ydy258.ydy.service.IProxyService;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.SettingUtils;

@Service("proxyService")
public class ProxyServiceImpl extends IBaseServiceImpl implements IProxyService {
		
	@Autowired
	private IProxyDao proxyDao;
	
	
	private static final String xmlPolicyNo = "PolicyNo";
	private static final String xmlPolicyNoLong = "PolicyNoLong";
	private static final String xmlChangeNo = "ChangeNo";
	private static final String xmlChangeNoLong = "ChangeNoLong";
	private static final String xmlErrorMsg = "ErrorMsg";
	private static final String xmlStatus = "Status";
	
	private static final String separator = "/";
	
	String partnerID = "常氏物流";
	String BusinessLogic = "AddFreightPolicyWeb";
	String pwd = "gxcswlxml";
    String urlmd5 = "http://www.e-picc.com.cn/ecargo/xmlDeal!md5InterfaceAction.action";
    String urldata = "http://www.epicc.com.cn/ecargo/xmlDeal!xmlDealAction.action";
	
	public Page insuranceByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		return proxyDao.insuranceByPage(args, currentPage, pageSize);
	}
	
	public Page insuranceChinaLifeByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		return proxyDao.insuranceChinaLifeByPage(args, currentPage, pageSize);
	}
	
	public Page idCardqueryOrderByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		return proxyDao.idCardqueryOrderByPage(args, currentPage, pageSize);
	}
	
	
	public Page proxyConfigListByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception{
		return proxyDao.proxyConfigListByPage(args, currentPage, pageSize);
	}
	
	
	public static void main(String[] args){
    	try {
			
    		System.out.println("fafdasf|123".split("\\|")[1]);
    		ProxyServiceImpl insu = new ProxyServiceImpl();
			Map<String,String> parammer = new LinkedHashMap<String,String>();
			parammer.put("BusinessLogic", "CancelFreightPolicy");
			parammer.put("PolicyNo", "TYDL201545010600E00782");
			String key = insu.getKey(parammer,insu.urlmd5,insu.pwd);
			
			System.out.println("key: "+key);
			
			parammer = new LinkedHashMap<String,String>();
			
			//parammer.put("username", partnerID);
			//parammer.put("BusinessLogic", "QueryFreightPolicy");
			parammer.put("PolicyNo", "TYDL201545010600E00782");
			
			String retstr = insu.rbXMLPara(insu.partnerID, "CancelFreightPolicy", key, parammer);
			System.out.println("reqestdata: "+retstr);
			
			//锟斤拷锟斤拷锟斤拷锟�
			String ret = insu.doPost(insu.urldata, retstr);
			
			System.out.println("return: "+ret);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public  String rbXMLPara(String username,String BusinessLogic,String retkey,Map<String,String> args) throws Exception
    {
    	StringBuffer sb = new StringBuffer();
    	sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>");
        sb.append("<PolicyList username=\"" + username + "\">"); //*
        sb.append("<Policy BusinessLogic=\"" + BusinessLogic + "\">"); //*
        for(Iterator<String> it=args.keySet().iterator();it.hasNext();){
        	String key = it.next();
        	String value = args.get(key);
        	sb.append("<"+key+">"+value+"</"+key+">"); //锟斤拷锟阶猴拷*
        }
        sb.append("<MD5Key>" + retkey + "</MD5Key>"); //锟斤拷锟斤拷品锟街凤拷锟斤拷(锟轿硷拷锟街典定锟斤拷)
        sb.append("</Policy>");
        sb.append("</PolicyList>");
        return sb.toString();
	}
    
    public  String getKey(Map<String,String> args,String url,String pwd) throws Exception{
        StringBuffer sb = new StringBuffer();
        for(Iterator<String> it=args.keySet().iterator();it.hasNext();){
        	String key = it.next();
        	String value = args.get(key);
        	sb.append(value);
        }
        //String unpartnerID_Temp = logic + mainGlausesCode + goodsTypeNo + departureDate +effDate+ insuredAmount + ratio;
        String unpartXml = "<?xml version='1.0' encoding='GBK'?>";
        unpartXml += "<MD5><String>" + sb.toString() + "</String><md5key>" + pwd + "</md5key></MD5>";
        String unpartnerID = getKey(doPost(url,unpartXml));
        return unpartnerID;
    }
    
    private   String getKey(String xml){
		String str1 = xml.split("<md5value>")[1];
		String str2 = str1.split("</md5value>")[0];
		return str2;
	}
    
    public String doPost(String url,String data) throws Exception {
        //data=new String(data.getBytes(),"gbk");
        URL localURL = new URL(url);
        URLConnection connection = localURL.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
        
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", "gbk");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length()));
        
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            outputStream = httpURLConnection.getOutputStream();
            //outputStreamWriter = new OutputStreamWriter(outputStream);
            
            //outputStreamWriter.write(data.toString());
            outputStream.write(data.getBytes("gbk"));
            outputStream.flush();
            //outputStreamWriter.write(data.toString());
            //outputStreamWriter.flush();
            
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success,"
                		+ " Response code is " + httpURLConnection.getResponseCode());
            }
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
            //System.out.println(resultBuffer);
        } finally {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return resultBuffer.toString();
    }
    
    
    public TInsurance getByParentId(long parentId) throws Exception {
    	return proxyDao.getByParentId(parentId);
    }
    final private Setting setting = SettingUtils.get();
    
    public void cancleAcc(TInsurance ins){
    	ins.setStatus(Constants.InsuranceStatus.cancle.getStatus());
		this.save(ins);
    	/*//发信息给审核员
		String to = setting.getServiceMail();//邮箱
		String[] cc = new String[]{setting.getServiceMail()};
		String sub = "人寿意外险审核";
		String msg = "您有一个人身意外险要审核，订单号为:"+tci.getOrderNo();
		SendmailUtils.sendMail(to, cc, msg,sub);*/
    }
}
