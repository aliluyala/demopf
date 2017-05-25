

package com.ydy258.ydy.controller;


import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.Setting;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.entity.TruckCard;
import com.ydy258.ydy.entity.TruckCardRHOrder;
import com.ydy258.ydy.entity.TruckCardRHPrice;
import com.ydy258.ydy.service.ICardRhService;
import com.ydy258.ydy.service.ICommDataService;
import com.ydy258.ydy.service.IMemberService;
import com.ydy258.ydy.service.ISysUserService;
import com.ydy258.ydy.service.ITallyService;
import com.ydy258.ydy.util.DateUtil;
import com.ydy258.ydy.util.JSONHelper;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.SettingUtils;

@Controller
@RequestMapping("/sys/card/")
public class CardRhAction extends BaseFacade  {
	
	final private Setting setting = SettingUtils.get();
	
	@Autowired
	private IMemberService memberService;
	
	@Autowired
	private ICommDataService commDataService;
	
	
	@Autowired
	private ITallyService tallyService;
	
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ICardRhService cardRhServiceImpl;
	
	
	/** servletContext */
	private ServletContext servletContext;	
	
	@Override
	public void setServletContext(ServletContext ctx) {
		// TODO Auto-generated method stub
		this.servletContext = ctx;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/truckCardRHPriceByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String truckCardRHPriceByPage(HttpServletRequest request, HttpServletResponse response,String tellerName,String proxyId,String storeId,int currentPage) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			if(!StringUtils.isBlank(tellerName)){
				tellerName = URLDecoder.decode(tellerName,"UTF-8"); 
				where.put("tellerName", tellerName);
			}
			Page page = cardRhServiceImpl.truckCardRHPriceByPage(where, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((TruckCardRHPrice)sm).getId());
				mo.put("cardParentType", ((TruckCardRHPrice)sm).getCardParentType());
				mo.put("cardTypeName", ((TruckCardRHPrice)sm).getCardTypeName());
				mo.put("condition", ((TruckCardRHPrice)sm).getCondition());
				mo.put("radio", ((TruckCardRHPrice)sm).getRadio());
				mo.put("special", ((TruckCardRHPrice)sm).getReturnSpecial());
				mo.put("condition", ((TruckCardRHPrice)sm).getCondition());
				mo.put("returnPoint1", ((TruckCardRHPrice)sm).getReturnPoint1());
				mo.put("returnPoint2", ((TruckCardRHPrice)sm).getReturnPoint2());
				mo.put("returnSpecial", ((TruckCardRHPrice)sm).getReturnSpecial());
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.getTotalCount()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/truckCardRHPriceSave", method = {RequestMethod.GET, RequestMethod.POST})
	public String truckCardRHPriceSave(HttpServletRequest request, HttpServletResponse response,
			String cardTypeName,
			int cardType,
			double radio,//直减率
			double special,//专项补贴率
			double condition,
			double returnPoint1,
			double returnPoint2) {
		try {
			TruckCardRHPrice tcp = new TruckCardRHPrice();
			tcp.setCardTypeName(cardTypeName);
			tcp.setCardParentType(1);//1:油卡
			tcp.setCardType(cardType);
			tcp.setCondition(condition);
			tcp.setReturnPoint1(returnPoint1);
			tcp.setReturnPoint2(returnPoint2);
			tcp.setReturnSpecial(special);
			tcp.setRadio(radio);
			cardRhServiceImpl.save(tcp);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/truckCardRHPriceEdit", method = {RequestMethod.GET, RequestMethod.POST})
	public String truckCardRHPriceEdit(HttpServletRequest request, HttpServletResponse response,
			long id,
			String cardTypeName,
			double radio,//直减率
			double special,//专项补贴率
			int cardType,
			double condition,
			double returnPoint1,
			double returnPoint2) {
		try {
			TruckCardRHPrice tcp = cardRhServiceImpl.getById(TruckCardRHPrice.class, id);
			tcp.setCardTypeName(cardTypeName);
			tcp.setCardParentType(1);//1:油卡
			tcp.setCardType(cardType);
			tcp.setRadio(radio);
			tcp.setCondition(condition);
			tcp.setReturnPoint1(returnPoint1);
			tcp.setReturnPoint2(returnPoint2);
			tcp.setReturnSpecial(special);
			cardRhServiceImpl.save(tcp);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/truckCardRHPriceQuery", method = {RequestMethod.GET, RequestMethod.POST})
	public String truckCardRHPriceQuery(HttpServletRequest request, HttpServletResponse response,
			long id
			) {
		try {
			TruckCardRHPrice tcp = cardRhServiceImpl.getById(TruckCardRHPrice.class, id);
			JSONHelper.returnInfo(JSONHelper.bean2json(tcp, true));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	//中国石化
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/truckCardRHCNPCByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String truckCardRHCNPCByPage(HttpServletRequest request, HttpServletResponse response,
			String truckName,
			String truckMobile,
			String status,
			int currentPage) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			if(!StringUtils.isBlank(truckName)){
				truckName = URLDecoder.decode(truckName,"UTF-8"); 
				where.put("truckName", truckName);
			}
			if(!StringUtils.isBlank(truckMobile)){
				truckMobile = URLDecoder.decode(truckMobile,"UTF-8"); 
				where.put("truckMobile", truckMobile);
			}
			if(!StringUtils.isBlank(status)){
				status = URLDecoder.decode(status,"UTF-8"); 
				where.put("status", status);
			}
			
			where.put("cardType", 3+"");//3：中国石化加油卡
			Page page = cardRhServiceImpl.truckCardRHCNPCByPage(where, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((TruckCardRHOrder)sm).getId());
				mo.put("orderNo", ((TruckCardRHOrder)sm).getOrderNo());
				mo.put("price", ((TruckCardRHOrder)sm).getPrice());
				mo.put("cardNumber", ((TruckCardRHOrder)sm).getCardNumber());
				mo.put("cardTypeName", ((TruckCardRHOrder)sm).getCardTypeName());
				mo.put("truckMoblie", ((TruckCardRHOrder)sm).getTruckMoblie());
				mo.put("truckName", ((TruckCardRHOrder)sm).getTruckName());
				mo.put("createDate", DateUtil.getStrYMDHMSByDate(((TruckCardRHOrder)sm).getCreateDate()));
				mo.put("payDate", DateUtil.getStrYMDHMSByDate(((TruckCardRHOrder)sm).getPayDate()));
				mo.put("payType", ((TruckCardRHOrder)sm).getPayType());
				mo.put("status", ((TruckCardRHOrder)sm).getStatus());
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.getTotalCount()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	//山东高速加油卡
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/truckCardSDGSCByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String truckCardSDGSCByPage(HttpServletRequest request, HttpServletResponse response,
			String orderNo,
			String truckName,
			String truckMobile,
			String status,
			int currentPage) {
		try {
			Map<String,String> where = new HashMap<String,String>();
			if(!StringUtils.isBlank(orderNo)){
				orderNo = URLDecoder.decode(orderNo,"UTF-8"); 
				where.put("orderNo", orderNo);
			}
			if(!StringUtils.isBlank(truckName)){
				truckName = URLDecoder.decode(truckName,"UTF-8"); 
				where.put("truckName", truckName);
			}
			if(!StringUtils.isBlank(truckMobile)){
				truckMobile = URLDecoder.decode(truckMobile,"UTF-8"); 
				where.put("truckMobile", truckMobile);
			}
			if(!StringUtils.isBlank(status)){
				status = URLDecoder.decode(status,"UTF-8"); 
				where.put("status", status);
			}
			where.put("cardType", 2+"");//2：山东高速加油卡
			Page page = cardRhServiceImpl.truckCardRHCNPCByPage(where, currentPage, pageSize);
			List<Map> list = new ArrayList<Map>();
			for(Object sm:page.getResults()){
				Map mo = new HashMap();
				mo.put("id", ((TruckCardRHOrder)sm).getId());
				mo.put("orderNo", ((TruckCardRHOrder)sm).getOrderNo());
				mo.put("price", ((TruckCardRHOrder)sm).getPrice());
				mo.put("cardNumber", ((TruckCardRHOrder)sm).getCardNumber());
				mo.put("cardTypeName", ((TruckCardRHOrder)sm).getCardTypeName());
				mo.put("truckMoblie", ((TruckCardRHOrder)sm).getTruckMoblie());
				mo.put("truckName", ((TruckCardRHOrder)sm).getTruckName());
				mo.put("createDate", DateUtil.getStrYMDHMSByDate(((TruckCardRHOrder)sm).getCreateDate()));
				mo.put("payDate", DateUtil.getStrYMDHMSByDate(((TruckCardRHOrder)sm).getPayDate()));
				mo.put("payType", ((TruckCardRHOrder)sm).getPayType());
				mo.put("status", ((TruckCardRHOrder)sm).getStatus());
				list.add(mo);
			}
			JSONHelper.returnInfo(JSONHelper.list2json(list,page.getTotalCount()+""));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	//山东高速充值
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/truckCardRHSDGSEdit", method = {RequestMethod.GET, RequestMethod.POST})
	public String truckCardRHSDGSEdit(HttpServletRequest request, HttpServletResponse response,
			long id,
			int status,
			String handleErrMsg
			) {
		try {
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			
			TruckCardRHOrder tco = cardRhServiceImpl.getById(TruckCardRHOrder.class, id);
			Truck truck = cardRhServiceImpl.getById(Truck.class, tco.getTruckId());
			tco.setStatus(status);
			tco.setHandleTime(new Date());
			tco.setHandleSysUser(user.getUserName());
			if(status==4){
				tco.setHandleErrMsg(handleErrMsg);
			}
			cardRhServiceImpl.save(tco);
			if(status==3){//3:成功
				TruckCard tc1 = cardRhServiceImpl.truckCardHistory(tco.getCardNumber());
				if(tc1==null){
					TruckCard tc = new TruckCard();
					tc.setTruckId(tco.getTruckId());
					tc.setCardType(tco.getCardType());
					tc.setCardNumber(tco.getCardNumber());
					tc.setCardTypeName(tco.getCardTypeName());
					tc.setParentType(tco.getParentType());
					cardRhServiceImpl.save(tc);
				}
			}
			if(status==3){
				String sendMsg = "油卡充值成功，订单号："+tco.getOrderNo();
				commDataService.push2Truck(truck.getJpushId(), sendMsg,Integer.valueOf(truck.getMobileType()));
				info("油卡充值成功");
			}else{
				//退款
				tallyService.tally(tco.getTruckId(),
						Constants.UserType.TruckUser.getType(),
						tco.getDiscount(),
						Long.valueOf(tco.getPoints()),
						Constants.TradeType.cardRH.getType(), 
						Constants.TradeType.cardRH.getZHName(), 
						tco.getOrderNo(), false);
				String sendMsg = "油卡充值失败("+handleErrMsg+")，订单号："+tco.getOrderNo();
				commDataService.push2Truck(truck.getJpushId(), sendMsg,Integer.valueOf(truck.getMobileType()));
				info("油卡充值失败");
			}
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	//山东高速充值
		@SuppressWarnings("unchecked")
		@RequestMapping(value = "/truckCardRHCNPCEdit", method = {RequestMethod.GET, RequestMethod.POST})
		public String truckCardRHCNPCEdit(HttpServletRequest request, HttpServletResponse response,
				long id,
				int status,
				String handleErrMsg
				) {
			try {
				HttpSession session = request.getSession();
				SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
				
				TruckCardRHOrder tco = cardRhServiceImpl.getById(TruckCardRHOrder.class, id);
				Truck truck = cardRhServiceImpl.getById(Truck.class, tco.getTruckId());
				tco.setStatus(status);
				tco.setHandleTime(new Date());
				tco.setHandleSysUser(user.getUserName());
				if(status==4){
					tco.setHandleErrMsg(handleErrMsg);
				}
				cardRhServiceImpl.save(tco);
				if(status==3){//3:成功
					TruckCard tc1 = cardRhServiceImpl.truckCardHistory(tco.getCardNumber());
					if(tc1==null){
						TruckCard tc = new TruckCard();
						tc.setTruckId(tco.getTruckId());
						tc.setCardType(tco.getCardType());
						tc.setCardNumber(tco.getCardNumber());
						tc.setCardTypeName(tco.getCardTypeName());
						tc.setParentType(tco.getParentType());
						cardRhServiceImpl.save(tc);
						
					}
				}
				if(status==3){
					String sendMsg = "油卡充值成功，订单号："+tco.getOrderNo();
					commDataService.push2Truck(truck.getJpushId(), sendMsg,Integer.valueOf(truck.getMobileType()));
					info("油卡充值成功");
				}else{
					//退款
					tallyService.tally(tco.getTruckId(),
							Constants.UserType.TruckUser.getType(),
							tco.getDiscount(),
							Long.valueOf(tco.getPoints()),
							Constants.TradeType.cardRH.getType(), 
							Constants.TradeType.cardRH.getZHName(), 
							tco.getOrderNo(), false);
					String sendMsg = "油卡充值失败("+handleErrMsg+")，订单号："+tco.getOrderNo();
					commDataService.push2Truck(truck.getJpushId(), sendMsg,Integer.valueOf(truck.getMobileType()));
					info("油卡充值失败");
				}
				JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	    }
}
