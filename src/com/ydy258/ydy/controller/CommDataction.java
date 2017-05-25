

package com.ydy258.ydy.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.entity.CommData;
import com.ydy258.ydy.entity.Region;
import com.ydy258.ydy.entity.RetData;
import com.ydy258.ydy.service.ICommDataService;
import com.ydy258.ydy.util.JSONHelper;

@Controller
@RequestMapping("/dictionary/")
public class CommDataction extends BaseFacade  {

	/**
	 * 增加字典
	* @Title: save
	* @param @return 
	* @throws
	* @Description: TODO
	 */
	@Autowired
	private ICommDataService commDataService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/save", method = {RequestMethod.GET, RequestMethod.POST})
	public String save(HttpServletRequest request, HttpServletResponse response,String id,String isLeaf,String parentId,
			String name,String status,String typeDes,String root) {
		try {
			CommData dd = new CommData();
			if(!StringUtils.isBlank(id)){
				dd = commDataService.getById(CommData.class, Long.valueOf(id));
			}
			if(!StringUtils.isBlank(isLeaf)){
				dd.setIsLeaf(Integer.valueOf(isLeaf));
			}
			if(!StringUtils.isBlank(parentId)){
				dd.setParentId(Long.valueOf(parentId));
			}
			if(!StringUtils.isBlank(name)){
				dd.setName(name);
			}
			if(!StringUtils.isBlank(status)){
				dd.setStatus(status);
			}
			if(!StringUtils.isBlank(typeDes)){
				dd.setTypeDesc(typeDes);
			}
			if(!StringUtils.isBlank(root)){
				dd.setRoot(root);
			}
			commDataService.save(dd);
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
	
	/**
	 * 通过ID查询分类字典
	* @Title: query
	* @param @return 
	* @return String
	* @throws
	* @Description: TODO
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
	public String query(HttpServletRequest request, HttpServletResponse response,String id) {
		try {
			if(!StringUtils.isBlank(id)){
				CommData dd= commDataService.getById(CommData.class,Long.valueOf(id));
				JSONHelper.returnInfo(JSONHelper.bean2json(dd,true));
	    		return NONE;
			}
			JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
	
	/**
	 * 查询父节点
	* @Title: queryParent
	* @param @return 
	* @return String
	* @throws
	* @Description: TODO
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryParent", method = {RequestMethod.GET, RequestMethod.POST})
	public String queryParent(HttpServletRequest request, HttpServletResponse response,String id) {
		try {
			List<CommData> dics = commDataService.queryParent();
			CommData dd = new CommData();
			dd.setId(Long.valueOf(-1));
			dd.setName("根结点");
			dics.add(0, dd);
			JSONHelper.returnInfo(JSONHelper.list2json(dics));
    		return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}

	/**
	 * 分布查询
	* @Title: listByPage
	* @param @return 
	* @return String
	* @throws
	* @Description: TODO
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listByPage", method = {RequestMethod.GET, RequestMethod.POST})
	public String listByPage(HttpServletRequest request, HttpServletResponse response,String id) {
		try {
			List<Map> list = new ArrayList<Map>();
			List<CommData> dics = commDataService.queryFirstParent();
			for(CommData dd:dics){
				Map<String,Object> root = new HashMap<String,Object>();
				root.put("id", dd.getId());
				root.put("parentId", dd.getParentId());
				root.put("name", dd.getName());
				root.put("typeDes", dd.getTypeDesc());
				root.put("status", dd.getStatus());
				root.put("expanded", false);
				dics = commDataService.queryByParentId(dd.getId()+"");
				List<Map> chs2 = new ArrayList<Map>();
				for(CommData d:dics){
					Map rdm = new HashMap();
					rdm.put("id",d.getId());
					rdm.put("expanded", false);
					rdm.put("name",d.getName());
					rdm.put("parentId",d.getParentId());
					rdm.put("typeDes", d.getTypeDesc());
					rdm.put("status", d.getStatus());
					rdm.put("leaf", d.getIsLeaf()==Constants.isLeaf.yes.getValue()?true:false);
					
					dics = commDataService.queryByParentId(dd.getId()+"");
					if(dics!=null&&dics.size()>0){
						List<Map> chs3 = new ArrayList<Map>();
						for(CommData dc:dics){
							Map rdmc = new HashMap();
							rdmc.put("id",dc.getId());
							rdmc.put("expanded", true);
							rdmc.put("name",dc.getName());
							rdmc.put("parentId",dc.getParentId());
							rdmc.put("typeDes", dc.getTypeDesc());
							rdmc.put("status", dc.getStatus());
							rdmc.put("leaf", true);
							rdmc.put("leaf", dc.getIsLeaf()==Constants.isLeaf.yes.getValue()?true:false);
							chs3.add(rdmc);
						}
						rdm.put("expanded", true);
						rdm.put("children", chs3);
					}
					chs2.add(rdm);
				}
				root.put("children", chs2);
				list.add(root);
			}
			
			JSONHelper.returnInfo(net.sf.json.JSONArray.fromObject(list));
			//JSONHelper.returnInfo(JSONHelper.bean2json(root));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
    }
	
	
	/**
	 * 字典列表
	* @Title: dictionaryList
	* @param @return 
	* @return String
	* @throws
	* @Description: TODO
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/dictionaryList", method = {RequestMethod.GET, RequestMethod.POST})
	public String dictionaryList(HttpServletRequest request, HttpServletResponse response,String dataRoot) {
	    	try {
		  		if(StringUtils.isBlank(dataRoot)){
		  			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.data_err_code,Constants.data_err_msg));
					return NONE;
		  		}
				List<RetData> list = commDataService.queryDictionary(dataRoot);
				JSONHelper.returnInfo(JSONHelper.successInfo(Constants.success_code,Constants.success_msg));
				return NONE;
			} catch (Exception e) {
				e.printStackTrace();
				JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
				return NONE;
			}
	   }

	
	@RequestMapping(value = "/getProvince", method = { RequestMethod.GET, RequestMethod.POST })
	public String getProvince(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			List<Region> provs = commDataService.getProvinces();
			List<Map<String,String>> ret = new ArrayList<Map<String,String>>();
			for(Region r:provs){
				Map<String,String> m = new HashMap<String,String>();
				m.put("id", r.getRegionId()+"");
				m.put("name", r.getRegionName());
				ret.add(m);
			}
			JSONHelper.returnInfo(net.sf.json.JSONArray.fromObject(ret));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
		
	}
	
	@RequestMapping(value = "/getCity", method = { RequestMethod.GET, RequestMethod.POST })
	public String getCity(HttpServletRequest request, HttpServletResponse response,long provinceId) throws IOException {
		
		try {
			List<Region> cities = commDataService.getCitiesByProvinceId(provinceId);
			List<Map<String,String>> ret = new ArrayList<Map<String,String>>();
			for(Region r:cities){
				Map<String,String> m = new HashMap<String,String>();
				m.put("id", r.getRegionId()+"");
				m.put("name", r.getRegionName());
				ret.add(m);
			}
			JSONHelper.returnInfo(net.sf.json.JSONArray.fromObject(ret));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}
	
	
	@RequestMapping(value = "/getArea", method = { RequestMethod.GET, RequestMethod.POST })
	public String getArea(HttpServletRequest request, HttpServletResponse response,long citiesId) throws IOException {
		try {
			List<Region> areas = commDataService.getAreaByCityId(citiesId);
			List<Map<String,String>> ret = new ArrayList<Map<String,String>>();
			for(Region r:areas){
				Map<String,String> m = new HashMap<String,String>();
				m.put("id", r.getRegionId()+"");
				m.put("name", r.getRegionName());
				ret.add(m);
			}
			JSONHelper.returnInfo(net.sf.json.JSONArray.fromObject(ret));
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			JSONHelper.returnInfo(JSONHelper.failedInfo(Constants.failed_code,Constants.failed_msg));
			return NONE;
		}
	}	
}
