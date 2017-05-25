package com.ydy258.ydy.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.ydy258.ydy.Constants;
import com.ydy258.ydy.dao.ISysUserDao;
import com.ydy258.ydy.entity.SysRole;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.TCargoTransactionStatements;
import com.ydy258.ydy.entity.TDriverTransactionStatements;
import com.ydy258.ydy.entity.TProxyTransactionStatements;
import com.ydy258.ydy.util.Page;
import com.ydy258.ydy.util.PageFactory;

@Repository
public class SysUserDaoImpl extends BaseDaoImpl implements ISysUserDao {
	
	
	public Page roleListByPage(String search,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select sysuser.* from sys_role sysuser where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(search)){
			sbsql.append(" and  sysuser.role_name like :content");
			args.put("content", "%"+search+"%");
		}
		sbsql.append(" order by sysuser.created_date desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,SysRole.class, currentPage, pageSize);
    }
	
	public SysRole getRoleByName(String name) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select sysuser.* from sys_role sysuser where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(name)){
			sbsql.append(" and  sysuser.role_name like :content");
			args.put("content", name);
		}
		List<SysRole> l = this.loadBySQL(sbsql.toString(), args, SysRole.class);
		if(l!=null&&l.size()>0){
			return l.get(0);
		}
		return null;
    }
	
	public Page userListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select sysuser.* from sys_user sysuser where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(where.get("name"))){
			sbsql.append(" and  sysuser.real_name like :content");
			args.put("content", "%"+where.get("name")+"%");
		}
		if(!StringUtils.isBlank(where.get("proxyId"))){
			sbsql.append(" and  sysuser.parent_id = :parentId");
			args.put("parentId", "%"+where.get("proxyId")+"%");
		}
		sbsql.append(" order by sysuser.created_date desc");
		return PageFactory.createPageBySql(this, sbsql.toString(), args,SysUser.class, currentPage, pageSize);
    }
	
	
	
	public Page userPayListByPage(Map<String,String> where,int currentPage,int pageSize) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select s.*,c.mobile,c.name from t_proxy_transaction_statements s left join ( (select id ,1 as type,mobile,real_name as name from t_consignor_info )  union (select id ,2 as type,mobile ,driver_name as name from t_truck_info ) union (select id ,3 as type,user_name as mobile ,real_name as name from sys_user )) c on s.user_id=c.id and c.type=s.user_type where 1=1 ");
		Map args = new HashMap();
		
		if(!StringUtils.isBlank(where.get("sysUserId"))){
			sbsql.append(" and  s.account_id = :sysUserId");
			args.put("sysUserId", Long.valueOf(where.get("sysUserId")));
		}
		if(!StringUtils.isBlank(where.get("search"))){
			sbsql.append(" and  c.name like :content");
			args.put("content", "%"+where.get("search")+"%");
		}
		if(!StringUtils.isBlank(where.get("mobile"))){
			sbsql.append(" and  c.mobile like :content");
			args.put("content", "%"+where.get("mobile")+"%");
		}
		sbsql.append(" order by s.id desc");
		return PageFactory.createMapPageBySql(this, sbsql.toString(), args, currentPage, pageSize);
		//return PageFactory.createPageBySql(this, sbsql.toString(), args,TProxyTransactionStatements.class, currentPage, pageSize);
    }
	
	public Page userAccountBook(Long userId,Short userTpye,String filter,int currentPage,int pageSize) throws Exception{
		if(userId == null || userId == 0L || (userTpye!=Constants.UserType.TruckUser.getType() && userTpye!=Constants.UserType.consignorUser.getType()))
			return null;
		StringBuilder sb = new StringBuilder();
		
		if(userTpye==Constants.UserType.consignorUser.getType()){
			sb.append("select * from t_cargo_transaction_statements where 1=1 and account_id="+userId+"  and pay!=0");
		}
		
		if(userTpye==Constants.UserType.TruckUser.getType()){
			sb.append("select * from t_driver_transaction_statements where 1=1 and account_id="+userId+"  and pay!=0");
		}
		
		if("1".equals(filter)){//�����֧��
			sb.append(" and is_turnout ");
		}
		if("2".equals(filter)){//�����֧��
			sb.append(" and is_turnout=false ");
		}
		sb.append(" order BY id desc");
		
		String sql = sb.toString();
		if(userTpye==Constants.UserType.consignorUser.getType()){
			return PageFactory.createPageBySql(this, sql.toString(), null,TCargoTransactionStatements.class, currentPage, pageSize);
		}
		if(userTpye==Constants.UserType.TruckUser.getType()){
			return PageFactory.createPageBySql(this, sql.toString(), null,TDriverTransactionStatements.class, currentPage, pageSize);
		}
		return null;		
	}
	

	public List userList(String search) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select sysuser.* from sys_user sysuser where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(search)){
			sbsql.append(" and  sysuser.real_name like :content");
			args.put("content", "%"+search+"%");
		}
		sbsql.append(" order by sysuser.created_date desc");
		return this.loadBySQL(sbsql.toString(), args, SysUser.class);
    }
	
	public SysUser queryUserByName(String name) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select sysuser.* from sys_user sysuser where sysuser.user_name='"+name+"'");
		Map args = new HashMap();
		List<SysUser> l = this.loadBySQL(sbsql.toString(), args, SysUser.class);
		if(l!=null&&l.size()>0){
			return l.get(0);
		}
		return null;
    }
	
	
	public SysUser queryUserByParentId(String parentId) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select sysuser.* from sys_user sysuser where sysuser.parent_id="+parentId);
		Map args = new HashMap();
		List<SysUser> l = this.loadBySQL(sbsql.toString(), args, SysUser.class);
		if(l!=null&&l.size()>0){
			return l.get(0);
		}
		return null;
    }
	
	public List listRoles(String search) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select role.* from sys_role role  where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(search)){
			sbsql.append(" and  role.name like :content");
			args.put("content", "%"+search+"%");
		}
		sbsql.append(" order by role.created_date desc");
		return this.loadBySQL(sbsql.toString(), args, SysRole.class);
    }
	
	
	public List listProxy(String search) throws Exception {
		StringBuffer sbsql = new StringBuffer(" select sysuser.* from sys_user sysuser where 1=1 and department='A0001' and proxy_admin = true ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(search)){
			sbsql.append(" and  sysuser.user_name like :content");
			args.put("content", "%"+search+"%");
		}
		sbsql.append(" order by sysuser.created_date desc");
		return this.loadBySQL(sbsql.toString(), args, SysUser.class);
    }
	
	public List listRolesByIDString(String idString) throws Exception {
		StringBuffer sb = new StringBuffer("select dd.* from sys_role dd where id in(");
		sb.append(idString);
		sb.append(")");
		return this.loadBySQL(sb.toString(), null, SysRole.class);
    }
	
	
	/*public Page sysLogListByPage(String search,int currentPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer(" select suser.real_name,suser.user_name,sysuser.* from sys_user suser join sys_user_operation sysuser on suser.id=sysuser.user_id where 1=1 ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(search)){
			sql.append(" and  user.real_name like :content");
			args.put("content", "%"+search+"%");
		}
		sql.append(" order by sysuser.created_date desc");
		return PageFactory.createMapPageBySql(this, sql.toString(), args, currentPage, pageSize);
    }*/
	
	public Page sysLogListByPage(String search,int currentPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer(" select id, user_ip as userIp,dated,logger,log_level as logLevel,(message->>'realName') as realName,(message->>'userName') as userName,(message->>'message') as message,(message->>'userId') as userId from sys_logs  where 1=1  ");
		Map args = new HashMap();
		if(!StringUtils.isBlank(search)){
			sql.append(" and  (message->>'realName') LIKE '%"+search+"%'");
		}
		sql.append(" order by dated desc");
		return PageFactory.createMapPageBySql(this, sql.toString(), args, currentPage, pageSize);
    }

}
