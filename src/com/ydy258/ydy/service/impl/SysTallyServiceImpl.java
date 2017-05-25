package com.ydy258.ydy.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydy258.ydy.dao.ISysTallyDao;
import com.ydy258.ydy.entity.TCompanyTransactionStatements;
import com.ydy258.ydy.service.ISysTallyService;

@Service("sysTallyService")
public class SysTallyServiceImpl extends IBaseServiceImpl implements ISysTallyService {
		
	private static final Logger logger  =  Logger.getLogger("ydytransaction");
	
	@Autowired
	private ISysTallyDao sysTallyDao;
	
	/**
	 * 系统
	 */
	public  boolean sysTally(Double money,Short userType,String orderNo,Long userId,String remark,Integer tradeType,boolean isTurnout){
		Map log = new HashMap();
		log.put("info", "记账开始！");
		info(new JSONObject(log).toString());
		//记录参数
		log = new HashMap();
		log.put("money", money);
		log.put("tradeType", tradeType);
		log.put("orderNo", orderNo);
		log.put("isTurnout", isTurnout);
		log.put("tradeTypeDescript", remark);
		info(new JSONObject(log).toString());
		
		TCompanyTransactionStatements tcts = new TCompanyTransactionStatements();
		tcts.setCreatedDate(new Date());
		tcts.setAccountId(userId);
		tcts.setUserType(userType);
		tcts.setPay(money);
		tcts.setOrderNo(orderNo);
		tcts.setIsTurnout(isTurnout);//对于公司账单为 收入
		tcts.setTradeType(tradeType);
		tcts.setRemark(remark);
		sysTallyDao.save(tcts);
		
		log = new HashMap();
		log.put("info", "记账成功！");
		info(new JSONObject(log).toString());
		return true;
	}
}
