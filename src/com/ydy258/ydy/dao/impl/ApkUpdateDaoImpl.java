package com.ydy258.ydy.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ydy258.ydy.dao.IApkUpdateDao;
import com.ydy258.ydy.entity.ApkJarUpdate;
import com.ydy258.ydy.entity.ApkUpdate;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.PageFactory;

@Repository
public class ApkUpdateDaoImpl extends BaseDaoImpl implements IApkUpdateDao {
	
	public Page apkByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select info.* from t_apk_update info where 1=1 ");
		if(!StringUtils.isBlank(args.get("search"))){
			sbsql.append(" and  info.newVersion like :search");
			args.put("search", "%"+args.get("search")+"%");
		}
		sbsql.append(" order by info.created_date desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,ApkUpdate.class, currentPage, pageSize);
    }

	public ApkUpdate getNewApk(String appKey) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select * from t_apk_update where id=(select max(id) from t_apk_update  where app_key='"+appKey+"') ");
		List<ApkUpdate> list = this.loadBySQL(sbsql.toString(), null, ApkUpdate.class);
		if(list==null || list.size()<1){
			return null;
		}
		return list.get(0);
    }
	
	
	public Page apkJarByPage(Map<String,String> args,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select info.* from t_apk_jar_update info where 1=1 ");
		if(!StringUtils.isBlank(args.get("search"))){
			sbsql.append(" and  info.newVersion like :search");
			args.put("search", "%"+args.get("search")+"%");
		}
		sbsql.append(" order by info.created_date desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,ApkUpdate.class, currentPage, pageSize);
    }
	
	public ApkJarUpdate getNewJarApk(String appKey) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select * from t_apk_jar_update where id=(select max(id) from t_apk_jar_update  where app_key='"+appKey+"') ");
		List<ApkJarUpdate> list = this.loadBySQL(sbsql.toString(), null, ApkJarUpdate.class);
		if(list==null || list.size()<1){
			return null;
		}
		return list.get(0);
    }

}
