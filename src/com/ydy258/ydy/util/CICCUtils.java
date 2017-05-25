package com.ydy258.ydy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.ydy258.ydy.Constants;

public class CICCUtils {
	
	
	
	public static final String KEY_STORE_TYPE_JKS = "JKS";
	public static final String KEY_STORE_TYPE_P12 = "JKS";
	public static final String SCHEME_HTTPS = "https";
    public static final int HTTPS_PORT = 8912;
    
    /*public static final String HTTPS_URL_DEL = "https://ywtest.ccic-net.com.cn:8912/cargo-general/Endorsement";
    public static final String HTTPS_URL_SUB = "https://ywtest.ccic-net.com.cn:8912/cargo-general/SubmitUnderwrite";
    public static final String KEY_STORE_CLIENT_PATH = "F:/cicckeys/cicckey.jks";
    public static final String KEY_STORE_TRUST_PATH = "F:/cicckeys/service.jks";*/
    
    public static final String HTTPS_URL_DEL = "https://service.ccic-net.com.cn:8888/cargo-general/Endorsement";
    public static final String HTTPS_URL_SUB = "https://service.ccic-net.com.cn:8888/cargo-general/SubmitUnderwrite";
    public static final String KEY_STORE_CLIENT_PATH = CICCUtils.class.getClassLoader().getResource("").getPath().toString()+"config"+File.separator+"proc.jks";
    public static final String KEY_STORE_TRUST_PATH = CICCUtils.class.getClassLoader().getResource("").getPath().toString()+"config"+File.separator+"pros.jks";

    public static final String KEY_STORE_PASSWORD = "123456";
    public static final String KEY_STORE_TRUST_PASSWORD = "123456";
    
    
    public static Map<String,String> message = new HashMap<String,String>();
    static {
    	message.put("访问规则引擎系统超时", "大地保险处理超时");
    	message.put("客户资料信息身份证号码不正确或者不符合规定！", "身份证号码不正确或者不符合规定");
    }
    
	public static String sendInsurance(String insuranceInfo,String HTTPS_URL) throws Exception{
		StringBuffer sb = new StringBuffer();
		HttpClient httpClient = new DefaultHttpClient();
		try{
            KeyStore keyStore  = KeyStore.getInstance(KEY_STORE_TYPE_P12);
            KeyStore trustStore  = KeyStore.getInstance(KEY_STORE_TYPE_JKS);
            InputStream ksIn = new FileInputStream(KEY_STORE_CLIENT_PATH);
            InputStream tsIn = new FileInputStream(new File(KEY_STORE_TRUST_PATH));
            try {
                keyStore.load(ksIn, KEY_STORE_PASSWORD.toCharArray());
                trustStore.load(tsIn, KEY_STORE_TRUST_PASSWORD.toCharArray());
            } finally {
                try { ksIn.close(); } catch (Exception ignore) {}
                try { tsIn.close(); } catch (Exception ignore) {}
            }
            SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore, KEY_STORE_PASSWORD, trustStore);
            Scheme sch = new Scheme(SCHEME_HTTPS, HTTPS_PORT, socketFactory);
            httpClient.getConnectionManager().getSchemeRegistry().register(sch);
            HttpPost post = new HttpPost(HTTPS_URL);
            
            StringEntity entitys = new StringEntity(insuranceInfo,"utf8");
            // Set XML entity
            post.setEntity(entitys);
            // Set content type of request header
            post.setHeader("Content-Type", "text/xml;charset=utf8");
          // Execute request and get the response
            
            
            System.out.println("executing request" + post.getRequestLine());
            HttpResponse resp = httpClient.execute(post);
            HttpEntity entity = resp.getEntity();
            System.out.println(resp.getStatusLine());
            if (entity != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(),"utf8"));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    sb.append(text);
                }
                bufferedReader.close();
            }
            EntityUtils.consume(entity);
		} finally {
            httpClient.getConnectionManager().shutdown();
        }
		return sb.toString();
	}

	
	public static String getProposalNo(String xml){
		xml = xml.split("<status>")[1];
		String status = xml.split("</status>")[0];
		if(!status.endsWith("0")){
			return null;
		}
		xml = xml.split("<proposalNo>")[1];
		String proposalNo = xml.split("</proposalNo>")[0];
		return proposalNo;
	}
	
	/** 大地保险 常见错误 
	 * <?xml version="1.0" encoding="utf-8"?>
		<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
		  <soap:Body>
		    <ProposalGenerateResponse xmlns="http://proposalgenerate.general.gwservice.ccic.com/">
		      <responseHead>
		        <status>2</status>
		        <returnCode>05001</returnCode>
		        <returnMessage>投保单生成失败！投保单保存异常:访问规则引擎系统超时，请与技术支持联系或稍候再试!</returnMessage>
		      </responseHead>
		    </ProposalGenerateResponse>
		  </soap:Body>
		</soap:Envelope>
	 * @Title:        getReturnMessage 
	 * @Description:  TODO
	 * @param:        @param xml
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2016年12月5日 上午10:30:36
	 */
	public static String getReturnMessage(String xml){
		xml = xml.split("<returnMessage>")[1];
		String message = xml.split("</returnMessage>")[0];
		return message;
	}
	/**
	 * 
	 * @Title:        错误信息转换 
	 * @Description:  TODO
	 * @param:        @param xml
	 * @param:        @return    
	 * @return:       String    
	 * @throws 
	 * @author        Administrator
	 * @Date          2016年12月5日 上午10:36:10
	 */
	public static String getChangeMessage(String xml){
		String changeMasseage = "大地保险系统出错";
		String ciccMessage = getReturnMessage(xml);
		for(String key:message.keySet()){
			if(ciccMessage.indexOf(key)>-1){
				changeMasseage = message.get(key);
				break;
			}
		}
		return changeMasseage;
	}
	
	public static String getSumPremium(String xml){
		xml = xml.split("<status>")[1];
		String status = xml.split("</status>")[0];
		if(!status.endsWith("0")){
			return null;
		}
		xml = xml.split("<sumPremium>")[1];
		String proposalNo = xml.split("</sumPremium>")[0];
		return proposalNo;
	}
	
	public static String getPolicyNo(String xml){
		xml = xml.split("<status>")[1];
		String status = xml.split("</status>")[0];
		if(!status.endsWith("0")){
			return null;
		}
		xml = xml.split("<policyNo>")[1];
		String policyNo = xml.split("</policyNo>")[0];
		return policyNo;
	}
	
	public static String getEPolicyURL(String xml){
		xml = xml.split("<status>")[1];
		String status = xml.split("</status>")[0];
		if(!status.endsWith("0")){
			return null;
		}
		xml = xml.split("<ePolicyURL>")[1];
		String policyNo = xml.split("</ePolicyURL>")[0];
		return policyNo;
	}
	
	public static String getEndorseNo(String xml){
		xml = xml.split("<status>")[1];
		String status = xml.split("</status>")[0];
		if(!status.endsWith("0")){
			return null;
		}
		xml = xml.split("<endorseNo>")[1];
		String endorseNo = xml.split("</endorseNo>")[0];
		return endorseNo;
	}
	
	public static String getStatus(String xml){
		xml = xml.split("<status>")[1];
		String status = xml.split("</status>")[0];
		if(!status.endsWith("0")){
			return null;
		}
		return status;
	}
	
	
	/**
     * 读取request流
     * @param req
     * @return
     * @author guoyx
     */
    public static String readReqStr(HttpServletRequest request)
    {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try
        {
            reader = new BufferedReader(new InputStreamReader(request
                    .getInputStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (null != reader)
                {
                    reader.close();
                }
            } catch (IOException e)
            {

            }
        }
        return sb.toString();
    }
	
	
	//撤单
		public static String deleteStr(String policyNo,String validDate,String validHour){
			String subStr =
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:end=\"http://endorsement.general.gwservice.ccic.com/\">"+
					    	"   <soapenv:Header/>                                                    "+
					    	"   <soapenv:Body>                                                       "+
					    	"      <end:EndorsementRequest>                                          "+
					    	"         <end:requestHead>                                              "+
					    	"            <end:channelCode>YDG</end:channelCode>                        "+
									    	"<end:channelCode>000013</end:channelCode>                     "+
									        "<end:channelName>昆明国网</end:channelName>                     "+
									        "<end:channelComCode>000013</end:channelComCode>               "+
									        "<end:channelComName>昆明国网</end:channelComName>               "+
									        "<end:channelProductCode>YDG</end:channelProductCode>       "+
									        "<end:operator>1</end:operator>                           "+
									        "<end:trxCode>001</end:trxCode>                             "+
									        "<end:channelSeqNo>123</end:channelSeqNo>                   "+
									        "<end:trxDate>2013-11-25</end:trxDate>                             "+
									        "<end:regionCode>1</end:regionCode>                       "+
									        "<end:makeChannel>gateway</end:makeChannel>                     "+
									        "<end:passWord>1</end:passWord>                           "+
					    	"         </end:requestHead>                                             "+
					    	"         <end:requestBody>                                              "+
					    	"            <end:riskCode>YDG</end:riskCode>                              "+
					    	"            <end:main>                                                  "+
					    	"                <end:endorseType>19</end:endorseType>"+
					    	"				<end:policyNo>{policyNo}</end:policyNo>                           "+
					   						"<end:validDate>{validDate}</end:validDate>"+
					   						"<end:validHour>{validHour}</end:validHour>"+ 
					    	"            </end:main>                                                 "+
					    	"         </end:requestBody>                                             "+
					    	"      </end:EndorsementRequest>                                         "+
					    	"   </soapenv:Body>                                                      "+
					    	"</soapenv:Envelope>";
			return subStr.replace("{policyNo}", policyNo).replace("{validDate}", validDate).replace("{validHour}", validHour);
		}
		
		
		
		//itemCode A02:鲜活  A13:普货
		public static String queryStr(String certiNo){
			String subStr =
					"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:und=\"http://underwritestatusinquiry.general.gwservice.ccic.com/\">"+
					    	   "<soapenv:Header/>                                                 "+
					    	   "<soapenv:Body>                                                    "+
					    	      "<und:UndwrStatusInquiryRequest>                                  "+
					    	         "<und:requestHead>                                           "+
					    	            "<und:channelCode>000013</und:channelCode>                     "+
					    	            "<und:channelName>昆明国网</und:channelName>                     "+
					    	            "<und:channelComCode>41010000</und:channelComCode>               "+
					    	            "<und:channelComName>昆明国网</und:channelComName>               "+
					    	            "<und:channelProductCode>YDG</und:channelProductCode>       "+
					    	            "<und:operator>1</und:operator>                           "+
					    	            "<und:trxCode>001</und:trxCode>                             "+
					    	            "<und:channelSeqNo>123</und:channelSeqNo>                   "+
					    	            "<und:trxDate>2013-11-25</und:trxDate>                             "+
					    	            "<und:regionCode>1</und:regionCode>                       "+
					    	            "<und:makeChannel>gateway</und:makeChannel>                     "+
					    	            "<und:passWord>1</und:passWord>                           "+
					    	         "</und:requestHead>                                          "+
					    	         "<und:requestBody>                                           "+
					    	            "<und:certiType>T</und:certiType>                         "+
					    	            "<und:certiNo>{certiNo}</und:certiNo>                             "+
					    	            "<und:riskCode>YDG</und:riskCode>                           "+
					    	         "</und:requestBody>                                          "+
					    	      "</und:UndwrStatusInquiryRequest>                                 "+
					    	   "</soapenv:Body>                                                   "+
					    	"</soapenv:Envelope>";;
			return subStr.replace("{certiNo}", certiNo);
		}
		
		
		//itemCode A02:鲜活  A13:普货
				public static String SubmitStr(String certiNo){
					String subStr =
							"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:und=\"http://underwrite.general.gwservice.ccic.com/\">"+
							    	   "<soapenv:Header/>                                                 "+
							    	   "<soapenv:Body>                                                    "+
							    	      "<und:SubmitUnderwriteRequest>                                  "+
							    	         "<und:requestHead>                                           "+
							    	            "<und:channelCode>000013</und:channelCode>                     "+
							    	            "<und:channelName>昆明国网</und:channelName>                     "+
							    	            "<und:channelComCode>41010000</und:channelComCode>               "+
							    	            "<und:channelComName>昆明国网</und:channelComName>               "+
							    	            "<und:channelProductCode>YDG</und:channelProductCode>       "+
							    	            "<und:operator>1</und:operator>                           "+
							    	            "<und:trxCode>001</und:trxCode>                             "+
							    	            "<und:channelSeqNo>123</und:channelSeqNo>                   "+
							    	            "<und:trxDate>2013-11-25</und:trxDate>                             "+
							    	            "<und:regionCode>1</und:regionCode>                       "+
							    	            "<und:makeChannel>gateway</und:makeChannel>                     "+
							    	            "<und:passWord>1</und:passWord>                           "+
							    	         "</und:requestHead>                                          "+
							    	         "<und:requestBody>                                           "+
							    	            "<und:certiType>E</und:certiType>                         "+
							    	            "<und:certiNo>{certiNo}</und:certiNo>                             "+
							    	            "<und:riskCode>YDG</und:riskCode>                           "+
							    	         "</und:requestBody>                                          "+
							    	      "</und:SubmitUnderwriteRequest>                                 "+
							    	   "</soapenv:Body>                                                   "+
							    	"</soapenv:Envelope>";
					return subStr.replace("{certiNo}", certiNo);
				}
				
				
				public static void main(String[] args){
					try {
						String dDate = "2017-02-20 23:36";
						Date _dDate = DateUtil.getDateByStr(dDate);
						String date = DateUtil.getStrYMDByDate(_dDate);
						String hour = DateUtil.getStrHHByDate(_dDate);
						String insuranceInfo = CICCUtils.deleteStr("PYDG201741010100000001",date,hour);
						String ciccRet = CICCUtils.sendInsurance(insuranceInfo, CICCUtils.HTTPS_URL_DEL);
						String s = CICCUtils.getStatus(ciccRet);//投单号
						if(s==null){
							String errMessage = CICCUtils.getReturnMessage(ciccRet);
							System.out.println(errMessage);
							//再提交一次核保
							String endorseNo = "EYDG201741010145000001";
							String subStr = CICCUtils.SubmitStr(endorseNo);
							ciccRet = CICCUtils.sendInsurance(subStr, CICCUtils.HTTPS_URL_SUB);
							s = CICCUtils.getStatus(ciccRet);//投单号
							if(s==null){
								errMessage = CICCUtils.getReturnMessage(ciccRet);
								System.out.println(errMessage);
							}
						}else{
							//再提交一次核保
							String endorseNo = "EYDG201741010145000013";
							String subStr = CICCUtils.SubmitStr(endorseNo);
							ciccRet = CICCUtils.sendInsurance(subStr, CICCUtils.HTTPS_URL_SUB);
							s = CICCUtils.getStatus(ciccRet);//投单号
							if(s==null){
								String errMessage = CICCUtils.getReturnMessage(ciccRet);
								System.out.println(errMessage);
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
}                                                                                                                                                     
