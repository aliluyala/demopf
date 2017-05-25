package com.ydy258.ydy.util;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 閸欐垿锟介柇顔绘閻ㄥ嫭绁寸拠鏇犫柤鎼达拷
 * 
 * @author lwq
 * 
 */
public class SendmailUtils {

	public static void sendMail(String to,String[] cc,String message)throws MessagingException {
		  JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
 	   
	  	  Properties props = new Properties();
	  	  props.put("mail.smtp.auth", "true");
	  	  senderImpl.setHost("smtp.163.com");
	  	  senderImpl.setUsername("yundeyiadmin");
	  	  senderImpl.setPassword("yundeyi123");
	  	  senderImpl.setJavaMailProperties(props);
	  	  MimeMessage mimeMessge = senderImpl.createMimeMessage();
	  	  MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessge,true,"utf-8");
	  	  mimeMessageHelper.setTo(to);
	  	  mimeMessageHelper.setCc(cc);
	  	  mimeMessageHelper.setFrom("yundeyiadmin@163.com");
	  	  mimeMessageHelper.setSubject("加油充值");
	  	  mimeMessageHelper.setText(message,true);  
	  	  senderImpl.send(mimeMessge);
	}
	
	public static void sendMail(String to,String[] cc,String message,String subject)throws MessagingException {
		  JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
	   
	  	  Properties props = new Properties();
	  	  props.put("mail.smtp.auth", "true");
	  	  senderImpl.setHost("smtp.163.com");
	  	  senderImpl.setUsername("yundeyiadmin");
	  	  senderImpl.setPassword("yundeyi123");
	  	  senderImpl.setJavaMailProperties(props);
	  	  MimeMessage mimeMessge = senderImpl.createMimeMessage();
	  	  MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessge,true,"utf-8");
	  	  mimeMessageHelper.setTo(to);
	  	  mimeMessageHelper.setCc(cc);
	  	  mimeMessageHelper.setFrom("yundeyiadmin@163.com");
	  	  mimeMessageHelper.setSubject(subject);
	  	  mimeMessageHelper.setText(message,true);  
	  	  senderImpl.send(mimeMessge);
	}
	
    public static void main(String[] args) throws MessagingException {
    	sendMail("405753068@qq.com",new String[]{"dzmsuccess@163.com"},"测试");
    	 /* JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
    	   
    	  Properties props = new Properties();
    	  props.put("mail.smtp.auth", "true");
    	  senderImpl.setHost("smtp.163.com");
    	  senderImpl.setUsername("dengzhongx2");
    	  senderImpl.setPassword("19850429");
    	  senderImpl.setJavaMailProperties(props);
    	  MimeMessage mimeMessge = senderImpl.createMimeMessage();
    	  
    	  MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessge,true);
    	    
    	  mimeMessageHelper.setTo("405753068@qq.com");
    	  mimeMessageHelper.setCc("1692677495@qq.com");
    	  mimeMessageHelper.setFrom("dengzhongx2@163.com");
    	  mimeMessageHelper.setSubject("濞ｈ濮為梽鍕濞村鐦�);
    	  mimeMessageHelper.setText("test",true);  
    	  //FileSystemResource img = new FileSystemResource(new File("I:/liang.jpg"));
    	   //mimeMessageHelper.addAttachment(MimeUtility.encodeWord("3M閺嶅嘲鎼ф禒鎾崇氨閺嶅洤鍣柅浣芥彛閸楁洘膩閺夛拷jpg"),img);   
    	  senderImpl.send(mimeMessge);*/
    }
}