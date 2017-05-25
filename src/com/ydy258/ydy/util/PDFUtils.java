package com.ydy258.ydy.util;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
public class PDFUtils {
	public static void pdfreader(String template,String descPath,Map<String,String> param) throws Exception{
		PdfReader reader = new PdfReader(template);
		//3 根据表单生成一个新的pdf
		PdfStamper ps = new PdfStamper(reader,new FileOutputStream(descPath));
		//4 获取pdf表单
		AcroFields s = ps.getAcroFields();
		//5给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
		BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		s.addSubstitutionFont(bf);
		//6遍历pdf表单表格，同时给表格赋值
		Map fieldMap = s.getFields();
		Set set = fieldMap.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()){
			Entry entry = (Entry) iterator.next();
			String key = (String)entry.getKey();
			if(param.get(key)!=null){
				s.setField(key, ""+param.get(key));
			}
		}
		ps.setFormFlattening(true); // 这句不能少
		ps.close();
		reader.close();
	}
	
	public static void main(String [] args){
	//1 准备要填充的数据
	Map paraMap = new HashMap();
	paraMap.put("name", "邓钟鸣");
	paraMap.put("order", "PYD1234354566567567647");
	paraMap.put("phone", "18508495859");
	paraMap.put("goodName", "大米");
	paraMap.put("fph", "PYD1234");
	paraMap.put("truckInfo", "大卡车");
	paraMap.put("startAddr", "东莞");
	paraMap.put("endAddr", "湖南");
	paraMap.put("jinge", "10000");
	paraMap.put("startTime", "2014-01-01");
	paraMap.put("year", "2015");
	paraMap.put("month", "02");
	paraMap.put("day", "10");
	try {
	//2 读入pdf表单
	PdfReader reader = new PdfReader("D:\\template.pdf");
	//3 根据表单生成一个新的pdf
	PdfStamper ps = new PdfStamper(reader,new FileOutputStream("D:\\my_blspb.PDF"));
	//4 获取pdf表单
	AcroFields s = ps.getAcroFields();
	//5给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
	BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	//BaseFont bf = BaseFont.createFont("C:/WINDOWS/Fonts/SIMSUN.TTC,1",BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	s.addSubstitutionFont(bf);
	//6遍历pdf表单表格，同时给表格赋值
	Map fieldMap = s.getFields();
	Set set = fieldMap.entrySet();
	Iterator iterator = set.iterator();
	while(iterator.hasNext()){
	Entry entry = (Entry) iterator.next();
	String key = (String)entry.getKey();
	if(paraMap.get(key)!=null){
		s.setField(key, ""+paraMap.get(key));
	}
	}
	ps.setFormFlattening(true); // 这句不能少
	ps.close();
	reader.close();
	} catch (IOException e) {
	// TODO 自动生成的 catch 块
	e.printStackTrace();
	} catch (DocumentException e) {
	// TODO 自动生成的 catch 块
	e.printStackTrace();
	} 
	}
}