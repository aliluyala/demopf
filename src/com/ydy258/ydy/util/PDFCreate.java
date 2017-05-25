package com.ydy258.ydy.util;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ydy258.ydy.entity.ThirdMall;
import com.ydy258.ydy.entity.ThirdMallOrder;
import com.ydy258.ydy.entity.ThirdMallOrderDetail;
	
public class PDFCreate {
	/** 
	  * 生成一个PDF，with图片 
	  */  
	 public static void main(String[] args) {  
	  System.out.println("createPDFWithChinese...........");  
	  try {  
	   // 1.建立Document实例  
	   Document document = new Document();  
	   // 2.建立一个书写器与Document对象关联，通过书写器将文档写入磁盘  
	   PdfWriter.getInstance(document,  
	     new FileOutputStream("D:\\test.pdf"));  
	   // 3.打开文档  
	   document.open();  
	   // 4.向文档中添加内容  
	   // a)添加一个图片  
	   // b)添加一个段落  
	   document.add(new Paragraph("iText HelloWorld"));  
	   // c)添加一个块  
	   document.add(new Chunk("Text is underline", FontFactory.getFont(  
	     FontFactory.HELVETICA_BOLD, 12, Font.UNDERLINE)));  
	   // d)添加中文,需要引入iTextAsian.jar  
	   // BaseFont bfChi = BaseFont.createFont("STSong-Light",  
	   // "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);  
	   BaseFont bfChi = BaseFont.createFont("STSong-Light",  
	     "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);  
	   Font fontChi = new Font(bfChi, 12, Font.NORMAL);  
	   document.add(new Paragraph("中文测试", fontChi));  
	   // e)添加一个表格  
	   // 表格内部格式和html中的格式差不多  
	   PdfPTable table = new PdfPTable(4);  
	   table.setWidthPercentage(100);  
	   table.setWidthPercentage(100);  
	   table.addCell(new Paragraph("学号", fontChi));  
	   PdfPCell cell = new PdfPCell(new Paragraph("00000001", fontChi));  
	   cell.setColspan(3);  
	   table.addCell(cell);  
	   table.addCell(new Paragraph("姓名", fontChi));  
	   table.addCell(new Paragraph("张三", fontChi));  
	   table.addCell(new Paragraph("总成绩", fontChi));  
	   table.addCell(new Paragraph("160", fontChi));  
	   table.addCell(new Paragraph("学号", fontChi));  
	   PdfPCell cell2 = new PdfPCell(new Paragraph("00000002", fontChi));  
	   cell2.setColspan(3);  
	   table.addCell(cell2);  
	   table.addCell(new Paragraph("姓名", fontChi));  
	   table.addCell(new Paragraph("李四", fontChi));  
	   table.addCell(new Paragraph("总成绩", fontChi));  
	   table.addCell(new Paragraph("167", fontChi));  
	   document.add(table);  
	   // 5.关闭文档  
	   document.close();  
	  } catch (FileNotFoundException e) {  
	   // TODO Auto-generated catch block  
	   e.printStackTrace();  
	  } catch (DocumentException e) {  
	   // TODO Auto-generated catch block  
	   e.printStackTrace();  
	  } catch (MalformedURLException e) {  
	   // TODO Auto-generated catch block  
	   e.printStackTrace();  
	  } catch (IOException e) {  
	   // TODO Auto-generated catch block  
	   e.printStackTrace();  
	  }  
	 }  
	 
	 /** 
	  * 生成一个PDF，with图片 
	  */  
	 public  static void createPTF(ThirdMall t,ThirdMallOrder order,java.util.List<ThirdMallOrderDetail> details,String basePath) {  
	  System.out.println("createPDFWithChinese...........");  
	  try {  
		  float len = 200;
		  if(details!=null&&details.size()>0){
			  len += 10*details.size();
		  }
		  Rectangle pageSize = new Rectangle(60, len);     // pageSize.setBackgroundColor(new Color(0xFF, 0xFF, 0xDE));
	   // 1.建立Document实例  
	   Document document = new Document(pageSize,10,15,10,15);  
	   // 2.建立一个书写器与Document对象关联，通过书写器将文档写入磁盘  
	   PdfWriter.getInstance(document,  
	     new FileOutputStream(basePath));  
	   // 3.打开文档  
	   document.open(); 
	   // 4.向文档中添加内容  
	   // a)添加一个图片  
	   // b)添加一个段落  
	  // document.add(new Paragraph("地址：山东高速服务区京台路泰安分公司"));  
	   // c)添加一个块  
	   //document.add(new Chunk("Text is underline", FontFactory.getFont(  
	    // FontFactory.HELVETICA_BOLD, 12, Font.UNDERLINE)));  
	   // d)添加中文,需要引入iTextAsian.jar  
	   // BaseFont bfChi = BaseFont.createFont("STSong-Light",  
	   // "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);  
	   BaseFont bfChi = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);  
	   Font fontChi = new Font(bfChi, 2, Font.NORMAL);  
	   Font fontChi2 = new Font(bfChi, 3, Font.NORMAL);  
	   Paragraph t1 = new Paragraph("运的易消费凭证", fontChi2);
	   t1.setAlignment(Element.ALIGN_CENTER);
	   document.add(t1);  
	   document.add(new Paragraph("商户名："+t.getStoreName()+"", fontChi));  
	   document.add(new Paragraph("柜员号："+order.getTellerId(), fontChi));  
	   document.add(new Paragraph("订单号："+order.getOrderNo()+"", fontChi)); 
	   document.add(new Paragraph("支付账号："+order.getCustomName()+"", fontChi)); 
	   document.add(new Paragraph("交易时间："+DateUtil.getStrYMDHMByDate(order.getAddTime())+"", fontChi)); 
	   document.add(new Paragraph("\n", fontChi));
	   //Image jpg = Image.getInstance("E:/qrcode_for_gh_0f439949a651_1280.png");
	   
	   //jpg.setAlignment(Image.ALIGN_CENTER);
	   
	   //document.add(jpg);
	   // e)添加一个表格  
	   // 表格内部格式和html中的格式差不多  
	   int count = 0;
	   double tatal = 0;
	   PdfPTable table = new PdfPTable(4);  
	   table.setWidthPercentage(100); 
	   table.setHorizontalAlignment(Element.ALIGN_LEFT);  
	   table.addCell(new Paragraph("商品名称", fontChi));  
	   table.addCell(new Paragraph("单价", fontChi));  
	   table.addCell(new Paragraph("数量", fontChi));  
	   table.addCell(new Paragraph("金额", fontChi));
	   for(ThirdMallOrderDetail od: details){
		   count+=od.getGoodsNumber();
		   tatal+=od.getAmount();
		   table.addCell(new Paragraph(od.getGoodsName(), fontChi));  
		   table.addCell(new Paragraph(od.getPrice()+"", fontChi));  
		   table.addCell(new Paragraph(od.getGoodsNumber()+"", fontChi));  
		   table.addCell(new Paragraph(od.getAmount()+"", fontChi));
	   }
	   table.addCell(new Paragraph("合计", fontChi));
	   table.addCell(new Paragraph("----", fontChi));
	   table.addCell(new Paragraph(count+"", fontChi));
	   table.addCell(new Paragraph(tatal+"", fontChi));
	   document.add(table);  
	   //document.add(new Paragraph("\n", fontChi));
	   document.add(new Paragraph("运的易客服电话：400-675-6568", fontChi));
	   // 5.关闭文档  
	   document.close();  
	  } catch (FileNotFoundException e) {  
	   // TODO Auto-generated catch block  
	   e.printStackTrace();  
	  } catch (DocumentException e) {  
	   // TODO Auto-generated catch block  
	   e.printStackTrace();  
	  } catch (MalformedURLException e) {  
	   // TODO Auto-generated catch block  
	   e.printStackTrace();  
	  } catch (IOException e) {  
	   // TODO Auto-generated catch block  
	   e.printStackTrace();  
	  }  
	 }  
	 /** 
	  * 根据html生成pdf没有试过,不知道行不 
	  */  
	 public static void fromHtmlToPdf() {  
	  try {  
	   Document document = new Document();  
	   StyleSheet st = new StyleSheet();  
	   st.loadTagStyle("body", "leading", "16,0");  
	   PdfWriter.getInstance(document, new FileOutputStream(  
	     "d:\\html2.pdf"));  
	   document.open();  
	   java.util.List<Element> p = HTMLWorker.parseToList(new FileReader(  
	     "d:\\to_pdf.htm"), st);  
	   for (int k = 0; k < p.size(); ++k)  
	    document.add((Element) p.get(k));  
	   document.close();  
	  } catch (Exception e) {  
	   e.printStackTrace();  
	  }  
	 }  
}

