package com.ydy258.ydy.dao;

import java.util.List;
import java.util.Map;

import com.ydy258.ydy.entity.Entity;

public interface IBaseDao {
	/**
     * 持久化实体
     * @param entity
     */
    void save(Object entity);
    void update(Object entity);
    
    /**
     * 根据主键查询实体
     * @param <T>
     * @param clazz  实体类
     * @param id     主键
     * @return
     */
    <T> T getById(Class<T> clazz,Object id);
    
    
	public Long save(Entity entity)throws Exception;

	/**
	 * 
	 * @Title:        save 
	 * @Description:  TODO 保存集合
	 * @param:        @param list
	 * @param:        @throws Exception    
	 * @return:       void    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月4日 下午2:58:01
	 */
	public void save(List<Entity> list)throws Exception;

	/**
	 * 
	 * @Title:        delete 通过ID删除
	 * @Description:  TODO
	 * @param:        @param clazz
	 * @param:        @param id
	 * @param:        @throws Exception    
	 * @return:       void    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月4日 下午2:58:24
	 */
	public <T> void delete(Class<T> clazz,Long id)throws Exception;

	/**
	 * 
	 * @Title:        delete 删除指定实体
	 * @Description:  TODO
	 * @param:        @param entity
	 * @param:        @throws Exception    
	 * @return:       void    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月4日 下午2:59:04
	 */
	public void delete(Entity entity)throws Exception ;
	
	/**
	 * 
	 * @Title:        queryNum 查询数字
	 * @Description:  TODO
	 * @param:        @param jpql
	 * @param:        @param args
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       Object    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月4日 下午2:59:43
	 */
	public Object queryNum(final String jpql, final Map<String,String> args)throws Exception;
	/**
	 * 
	 * @Title:        getSQLTotalCnt 
	 * @Description:  TODO 查询总数
	 * @param:        @param sql
	 * @param:        @param args
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       int    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月4日 下午3:00:46
	 */
	public int getSQLTotalCnt(final String sql, final Map<String,String> args)throws Exception;
	
	public int getSQLTotalCnt(final String sql)throws Exception ;
	
	/**
	 * 
	 * @Title:        getJPQLTotalCnt 
	 * @Description:  TODO jpql 查询总数
	 * @param:        @param jpql
	 * @param:        @param args
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       int    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午4:59:44
	 */
	public int getJPQLTotalCnt(final String jpql,final Map<String,String> args)throws Exception;

	/**
	 * 
	 * @Title:        loadByJPQL 
	 * @Description:  TODO
	 * @param:        @param jpql 
	 * @param:        @param args
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       List    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月4日 下午3:02:47
	 */
	public List loadByJPQL(final String jpql, final Map<String,String> args)throws Exception;

	/**
	 * 
	 * @Title:        loadByJPQL 
	 * @Description:  TODO jpql分布查询
	 * @param:        @param jpql 
	 * @param:        @param args 参数
	 * @param:        @param start 开始位置
	 * @param:        @param size 一页大小
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       List<Entity>    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午4:49:42
	 */
	public List<Entity> loadByJPQL(final String jpql, final Map<String,String> args, final int start, final int size)throws Exception;

	/**
	 * 
	 * @Title:        execute 
	 * @Description:  TODO 执行sql查询语句
	 * @param:        @param jpql jpsql
	 * @param:        @param args 参数
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       Object    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午4:50:14
	 */
	public Object execute(final String jpql, final Map<String,String> args)throws Exception;


	/**
	 * 
	 * @Title:        loadBySQL 
	 * @Description:  TODO 原生sql查询
	 * @param:        @param sql 
	 * @param:        @param args 参数
	 * @param:        @param clazz 要查询的对象
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       List    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午4:50:54
	 */
	public List loadBySQL(final String sql, final Map<String,String> args,Class clazz)throws Exception;
	
	/**
	 * 
	 * @Title:        loadBySQL 
	 * @Description:  TODO 原生sql分页查询
	 * @param:        @param sql
	 * @param:        @param args 参数
	 * @param:        @param clazz 要查询
	 * @param:        @param start 开始位置
	 * @param:        @param size 页面大小
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       List<Entity>    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午4:53:02
	 */

	public List<Entity> loadBySQL(final String sql, final Map<String,String> args,Class clazz, final int start, final int size)throws Exception;

	/**
	 * 
	 * @Title:        executeSQL 
	 * @Description:  TODO 原生sql执行
	 * @param:        @param sql 
	 * @param:        @param args 参数
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       Object    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午4:54:38
	 */
	public Object executeSQL(final String sql, final Map<String,String> args)throws Exception;
	
	/**
	 * 
	 * @Title:        loadMapBySQL 
	 * @Description:  TODO 原生sql 一般关联，比较复杂的查询的时候要用到的 返回的map的List
	 * @param:        @param sql
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       List    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午4:56:16
	 */
	public List loadMapBySQL(final String sql)throws Exception;
	
	/**
	 * 
	 * @Title:        loadMapBySQL 
	 * @Description:  TODO  原生sql 一般关联，比较复杂的查询的时候要用到的 返回的map的List
	 * @param:        @param sql
	 * @param:        @param args
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       List    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午4:57:28
	 */
	public List loadMapBySQL(final String sql,final Map<String,String> args)throws Exception;
	
	/**
	 * 
	 * @Title:        loadMapBySQL 
	 * @Description:  TODO 原生sql分页查询 一般关联，比较复杂的查询的时候要用到的 返回的map  的List
	 * @param:        @param sql
	 * @param:        @param args
	 * @param:        @param start
	 * @param:        @param size
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       List    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月7日 下午4:58:23
	 */
	
	public List loadMapBySQL(final String sql,final Map<String,String> args,final int start,final int size)throws Exception;
}

