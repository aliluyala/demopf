

package com.ydy258.ydy.controller;


import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.llpay.client.config.PartnerConfig;
import com.llpay.client.config.ServerURLConfig;
import com.llpay.client.utils.LLPayUtil;
import com.llpay.client.vo.OrderInfo;
import com.llpay.client.vo.PaymentInfo;
import com.ydy258.ydy.Constants;
import com.ydy258.ydy.entity.Consignor;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.Truck;
import com.ydy258.ydy.service.IMemberService;
import com.ydy258.ydy.service.IProxyService;
import com.ydy258.ydy.service.ITallyService;
import com.ydy258.ydy.util.DateUtil;
import com.ydy258.ydy.util.JSONHelper;
import com.ydy258.ydy.util.utils;

@Controller
@RequestMapping("/sys/pay/")
public class TallyAction extends BaseFacade  {
	@Autowired
	private IMemberService memberService;
	
	@Autowired
	private IProxyService proxyService;
	
	@Autowired
	private ITallyService tallyService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/toPlainPay", method = {RequestMethod.GET, RequestMethod.POST})
	public String toPlainPay(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("utf-8");
			response.setCharacterEncoding("utf-8");
			
			Date dtOrder = new Date();
	    	String dtstr = DateUtil.getStrByDate(dtOrder,"yyyyMMddHHmmss");
	    	String noOrder = utils.generaterOrderNumber();
	    	String moneyOrder = request.getParameter("money_order");
	    	String nameGoods = request.getParameter("name_goods");
	    	
	    	
	    	String userIdtype = "";
	    	String phone = "";
	    	String registerDate = "";
	        
	    	
			HttpSession session = request.getSession();
			SysUser user = (SysUser)session.getAttribute(Constants.LOGIN_KEY);
			String dep  = user.getDepartment();
			
			short userType = 1;
			Long userId = user.getDepartmentId();//对应用户ID
			if(dep.equals("D0001")){//D0001货主部门编号
				Truck truck = memberService.getById(Truck.class,userId);
				if(truck==null){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
					return NONE;
				}
				registerDate = DateUtil.getStrByDate(truck.getRegisterTime());
	        	phone = truck.getMobile();
	        	userType = 2;//车主类型编号
			}else if(dep.equals("E0001")){//E0001货主部门编号
				Consignor consignor = memberService.getById(Consignor.class,Long.valueOf(userId));
				if(consignor==null){
					JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
					return NONE;
				}
				registerDate = DateUtil.getStrByDate(consignor.getRegisterTime());
	        	phone = consignor.getMobile();
	        	userType = 1;//货主类型编号
			}else{
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
			
			userIdtype = userType+"_"+userId;
	        
			tallyService.saveUserOrder(userId, userType, noOrder, 1);
	        // 创建订单
	        OrderInfo order = createOrder(noOrder,dtstr,moneyOrder,nameGoods,nameGoods);
	        
	        RequestDispatcher dispatcher = null;
	        String paymod = request.getParameter("paymod");
	        if ("plain".equals(paymod))
	        {
	            plainPay(request, order,userIdtype,registerDate,phone);
	            dispatcher = request.getRequestDispatcher("/jsp/llpay/gotoPlainPay.jsp");
	        }
	        dispatcher.forward(request, response);
	        return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	

	/**
     * 普通支付处理
     * @param req
     * @param order
     */
    private void plainPay(HttpServletRequest req, OrderInfo order,String userId,String registerDate,String phone)
    {
        // 构造支付请求对象
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setVersion(PartnerConfig.VERSION);
        paymentInfo.setOid_partner(PartnerConfig.OID_PARTNER);
        paymentInfo.setUser_id(userId);
        paymentInfo.setSign_type(PartnerConfig.SIGN_TYPE);
        paymentInfo.setBusi_partner(PartnerConfig.BUSI_PARTNER);
        paymentInfo.setNo_order(order.getNo_order());
        paymentInfo.setDt_order(order.getDt_order());
        paymentInfo.setName_goods(order.getName_goods());
        paymentInfo.setInfo_order(order.getInfo_order());
        paymentInfo.setMoney_order(order.getMoney_order());
        paymentInfo.setNotify_url(PartnerConfig.NOTIFY_URL);
        paymentInfo.setUrl_return(PartnerConfig.URL_RETURN);
        paymentInfo.setUserreq_ip(LLPayUtil.getIpAddr(req));
        paymentInfo.setUrl_order("");
        paymentInfo.setValid_order("10080");// 单位分钟，可以为空，默认7天
        paymentInfo.setTimestamp(LLPayUtil.getCurrentDateTimeStr());
        paymentInfo.setRisk_item(createRiskItem(registerDate,phone,userId));
        // 加签名
        String sign = LLPayUtil.addSign(JSON.parseObject(JSON
                .toJSONString(paymentInfo)), PartnerConfig.TRADER_PRI_KEY,
                PartnerConfig.MD5_KEY);
        paymentInfo.setSign(sign);

        req.setAttribute("version", paymentInfo.getVersion());
        req.setAttribute("oid_partner", paymentInfo.getOid_partner());
        req.setAttribute("user_id", paymentInfo.getUser_id());
        req.setAttribute("sign_type", paymentInfo.getSign_type());
        req.setAttribute("busi_partner", paymentInfo.getBusi_partner());
        req.setAttribute("no_order", paymentInfo.getNo_order());
        req.setAttribute("dt_order", paymentInfo.getDt_order());
        req.setAttribute("name_goods", paymentInfo.getName_goods());
        req.setAttribute("info_order", paymentInfo.getInfo_order());
        req.setAttribute("money_order", paymentInfo.getMoney_order());
        req.setAttribute("notify_url", paymentInfo.getNotify_url());
        req.setAttribute("url_return", paymentInfo.getUrl_return());
        req.setAttribute("userreq_ip", paymentInfo.getUserreq_ip());
        req.setAttribute("url_order", paymentInfo.getUrl_order());
        req.setAttribute("valid_order", paymentInfo.getValid_order());
        req.setAttribute("timestamp", paymentInfo.getTimestamp());
        req.setAttribute("sign", paymentInfo.getSign());
        req.setAttribute("risk_item", paymentInfo.getRisk_item());
        req.setAttribute("req_url", ServerURLConfig.PAY_URL);

    }

    /**
     * 模拟商户创建订单
     * @param req
     * @return
     */
    private OrderInfo createOrder(HttpServletRequest req)
    {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setNo_order(LLPayUtil.getCurrentDateTimeStr());
        orderInfo.setDt_order(LLPayUtil.getCurrentDateTimeStr());
        orderInfo.setMoney_order(req.getParameter("money_order"));
        orderInfo.setName_goods(req.getParameter("name_goods"));
        orderInfo.setInfo_order("用户购买" + req.getParameter("name_goods"));
        return orderInfo;
    }
    
    /**
     * 模拟商户创建订单
     * @param req
     * @return
     */
    private OrderInfo createOrder(String noOrder,String dtOrder,String moneyOrder,String nameGoods,String infoOrder)
    {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setNo_order(noOrder);
        orderInfo.setDt_order(dtOrder);
        orderInfo.setMoney_order(moneyOrder);
        orderInfo.setName_goods(nameGoods);
        orderInfo.setInfo_order(infoOrder);
        return orderInfo;
    }
    
    /**
     * 根据连连支付风控部门要求的参数进行构造风控参数
     * @return
     */
    private String createRiskItem(String frms_ware_category,String user_info_mercht_userno,String user_info_bind_phone,String user_info_dt_register)
    {
        JSONObject riskItemObj = new JSONObject();
        riskItemObj.put("frms_ware_category", frms_ware_category);
        riskItemObj.put("user_info_mercht_userno", user_info_mercht_userno);
        riskItemObj.put("user_info_bind_phone", user_info_bind_phone);
        riskItemObj.put("user_info_dt_register", user_info_dt_register);
        return riskItemObj.toString();
    }
    
    
    private String createRiskItem(String Date,String phone,String userId) {
        JSONObject mRiskItem = new JSONObject();
        try {
           //mRiskItem.put("user_info_dt_register", Date);
            mRiskItem.put("frms_ware_category", "1014");//
            mRiskItem.put("user_info_mercht_userno", userId);
            mRiskItem.put("user_info_bind_phone", phone);
            mRiskItem.put("user_info_dt_register", Date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mRiskItem.toString();
    }
    
    /**
     * 根据连连支付风控部门要求的参数进行构造风控参数
     * @return
     */
    private String createRiskItem()
    {
        JSONObject riskItemObj = new JSONObject();
        riskItemObj.put("user_info_full_name", "你好");
        riskItemObj.put("frms_ware_category", "1999");
        return riskItemObj.toString();
    }

}
