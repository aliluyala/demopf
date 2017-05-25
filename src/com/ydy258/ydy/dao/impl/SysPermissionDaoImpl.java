package com.ydy258.ydy.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ydy258.ydy.dao.ISysPermissionDao;
import com.ydy258.ydy.entity.Consignor;
import com.ydy258.ydy.entity.SysPermission;
import com.ydy258.ydy.entity.SysRole;
import com.ydy258.ydy.entity.SysUser;
import com.ydy258.ydy.entity.Truck;

@Repository
public class SysPermissionDaoImpl extends BaseDaoImpl implements ISysPermissionDao {

	@Override
	public void addPermission(SysRole role, String[] addPermission) {
		if(addPermission!=null&&addPermission.length>0){
			for(String id:addPermission){
				String addsql = "insert into sys_role_permission (role_id,permission_id,created_date) values('"+role.getId()+"','"+id+"',now())";
				entityManager.createNativeQuery(addsql).executeUpdate();
			}
		}
	}

	@Override
	public void delPermission(SysRole role, String[] delPermission) {
		if(role.getId()!=null&&delPermission!=null&&delPermission.length>0){
			for(String id:delPermission){
				String delsql = "delete from sys_role_permission where role_id='"+role.getId()+"' and permission_id='"+id+"'";
				entityManager.createNativeQuery(delsql).executeUpdate();
			}
		}
	}
	
	
	public SysUser userLogin(String userName,String password) throws Exception {
		StringBuffer sql = new StringBuffer("select * from sys_user sysuser where 1=1 ");
		sql.append(" and sysuser.user_name=:userName ");
		sql.append(" and sysuser.password=:password");
		Map args = new HashMap();
		args.put("userName", userName);
		args.put("password", password);
		//return this.getSQLTotalCnt(sql.toString(), args)>0;
		List<SysUser> users =  this.loadBySQL(sql.toString(), args, SysUser.class);
		if(null!=users&&users.size()>0){
			return users.get(0);
		}
		return null;
    }
	
	
	public Truck truckLogin(String mobile,String password) throws Exception {
		StringBuffer sql = new StringBuffer("select * from t_truck_info truck where 1=1 ");
		sql.append(" and truck.mobile=:mobile ");
		sql.append(" and truck.password=:password ");
		Map args = new HashMap();
		args.put("mobile", mobile);
		args.put("password", password);
		//return this.getSQLTotalCnt(sql.toString(), args)>0;
		List<Truck> users =  this.loadBySQL(sql.toString(), args, Truck.class);
		if(null!=users&&users.size()>0){
			return users.get(0);
		}
		return null;
    }
	
	public Consignor consignorLogin(String mobile,String password) throws Exception {
		StringBuffer sql = new StringBuffer("select * from t_consignor_info consignor where 1=1 ");
		sql.append(" and consignor.mobile=:mobile ");
		sql.append(" and consignor.password=:password ");
		Map args = new HashMap();
		args.put("mobile", mobile);
		args.put("password", password);
		//return this.getSQLTotalCnt(sql.toString(), args)>0;
		List<Consignor> users =  this.loadBySQL(sql.toString(), args, Consignor.class);
		if(null!=users&&users.size()>0){
			return users.get(0);
		}
		return null;
    }

	
	public List queryParent() throws Exception{
		String sql = " select dd.* from sys_permission dd where dd.parent_id=-1 ";
		return this.loadBySQL(sql, null, SysPermission.class);
	}

	public List listByParentId(long parentId) throws Exception {
		String sql = " select dd.* from sys_permission dd where dd.parent_id=:pid ";
		Map args = new HashMap();
		args.put("pid", parentId);
		return this.loadBySQL(sql, args, SysPermission.class);
    }
	
	
	
	
	public List listSysPermissionByIDString(String idString) throws Exception {
		StringBuffer sb = new StringBuffer("select dd.* from sys_permission dd where id in(");
		String[] nps = idString.split(",");
		for(int i=0;i<nps.length;i+=1){
			if(i==nps.length-1){
				sb.append(nps[i].split("-")[1]);
			}else{
				sb.append(nps[i].split("-")[1]+",");
			}
		}
		sb.append(")");
		return this.loadBySQL(sb.toString(), null, SysPermission.class);
    }
	
	public List rolePermissionByRoleID(String roleId) throws Exception {
		StringBuffer sb = new StringBuffer("select * from sys_role_permission where role_id='"+roleId+"'");
		return this.loadMapBySQL(sb.toString());
    }
}
