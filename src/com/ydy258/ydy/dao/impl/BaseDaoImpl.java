package com.ydy258.ydy.dao.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.ydy258.ydy.dao.IBaseDao;
import com.ydy258.ydy.entity.Entity;

@Repository(value="baseDaoImpl")
public class BaseDaoImpl implements IBaseDao {
	private static final Log log = LogFactory.getLog(BaseDaoImpl.class);
	
	//注入实体管理器
	@PersistenceContext
	protected EntityManager entityManager;	
	
	@Override
	public void save(Object entity) {
		// TODO Auto-generated method stub
		entityManager.persist(entity);
	}

	@Override
	public <T> T getById(Class<T> clazz, Object id) {
		// TODO Auto-generated method stub
		return entityManager.find(clazz, id);
	}

	@Override
	public void update(Object entity) {
		// TODO Auto-generated method stub
		entityManager.merge(entity);
		//entityManager.flush();
	}
	
	/**
	 * 
	 * @Title:        save 
	 * @Description:  TODO 保存
	 * @param:        @param entity
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       Long    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月4日 下午2:57:34
	 */
	public Long save(Entity entity)throws Exception {
		if (entity == null)
			return null;
		if ("".equals(entity.id))
			entity.id = null;
		entityManager.persist(entity);
		return entity.id;
	}

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
	public void save(List<Entity> list)throws Exception {
		Entity entity;
		for (Iterator iterator = list.iterator(); iterator.hasNext(); save(entity))
			entity = (Entity) iterator.next();

	}

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
	public <T> void delete(Class<T> clazz,Long id)throws Exception {
		T entity = this.getById(clazz, id);
		if (entity != null)
			entityManager.remove(entity);
	}

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
	public void delete(Entity entity)throws Exception {
		if (entity == null){
			return;
		} else {
			entityManager.remove(entity);
			return;
		}
	}
	
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
	public Object queryNum(final String jpql, final Map<String,String> args)throws Exception {
		if (log.isDebugEnabled()) {
			log.debug((new StringBuilder("queryNum :")).append(jpql).toString());
		}
		javax.persistence.Query query = entityManager.createQuery(jpql);
		if (args != null) {
			String s;
			for (Iterator iterator = args.keySet().iterator(); iterator.hasNext(); query.setParameter(s, args.get(s))) {
				s = (String) iterator.next();
			}
		}
		return query.getSingleResult();
	}
	
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
	public int getSQLTotalCnt(final String sql, final Map<String,String> args)throws Exception {
		if (log.isDebugEnabled()) {
			log.debug((new StringBuilder("queryNum :")).append(sql).toString());
		}
		javax.persistence.Query query = entityManager.createNativeQuery(sql);
		if (args != null) {
			String s;
			for (Iterator iterator = args.keySet().iterator(); iterator.hasNext(); query.setParameter(s, args.get(s))) {
				s = (String) iterator.next();
			}
		}
		return Integer.parseInt(query.getSingleResult().toString());
	}
	
	public int getSQLTotalCnt(final String sql)throws Exception {
		return getSQLTotalCnt(sql, null);
	}
	
	/**
	 * jpql分布查询
	 */
	public int getJPQLTotalCnt(final String jpql,final Map<String,String> args)throws Exception {
		return Integer.parseInt(queryNum(jpql, args).toString());
	}
	

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
	public List loadByJPQL(final String jpql, final Map<String,String> args)throws Exception {
		if (log.isDebugEnabled()) {
			log.debug((new StringBuilder("queryNum :")).append(jpql).toString());
		}
		javax.persistence.Query query = entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT);
		if (args != null) {
			String s;
			for (Iterator iterator = args.keySet().iterator(); iterator.hasNext(); query.setParameter(s, args.get(s))) {
				s = (String) iterator.next();
			}
		}
		return query.getResultList();
	}

	/**
	 * 执行sql查询语句
	 */
	public List<Entity> loadByJPQL(final String jpql, final Map<String,String> args, final int start, final int size)throws Exception {
		if (log.isDebugEnabled()) {
			log.debug((new StringBuilder("queryNum :")).append(jpql).toString());
		}
		javax.persistence.Query query = entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT);
		query.setMaxResults(size);
		query.setFirstResult(start);
		if (args != null) {
			String s;
			for (Iterator iterator = args.keySet().iterator(); iterator.hasNext(); query.setParameter(s, args.get(s))) {
				s = (String) iterator.next();
			}
		}
		return query.getResultList();
	}

	/**
	 * 
	 */
	public Object execute(final String jpql, final Map<String,String> args)throws Exception {
		if (log.isDebugEnabled()) {
			log.debug((new StringBuilder("queryNum :")).append(jpql).toString());
		}
		javax.persistence.Query query = entityManager.createQuery(jpql,Long.class).setFlushMode(FlushModeType.COMMIT);
		if (args != null) {
			String s;
			for (Iterator iterator = args.keySet().iterator(); iterator.hasNext(); query.setParameter(s, args.get(s))) {
				s = (String) iterator.next();
			}
		}
		return Integer.valueOf(query.executeUpdate());
		
	}

	/**
	 * 原生sql查询
	 */
	public List loadBySQL(final String sql, final Map<String,String> args,Class clazz)throws Exception {
		if (log.isDebugEnabled()) {
			log.debug((new StringBuilder("queryNum :")).append(sql).toString());
		}
		javax.persistence.Query query = entityManager.createNativeQuery(sql,clazz).setFlushMode(FlushModeType.COMMIT);
		if (args != null) {
			String s;
			for (Iterator iterator = args.keySet().iterator(); iterator.hasNext(); query.setParameter(s, args.get(s))) {
				s = (String) iterator.next();
			}
		}
		return query.getResultList();
	}

	/**
	 * 原生sql分页查询
	 */
	public List<Entity> loadBySQL(final String sql, final Map<String,String> args,Class clazz, final int start, final int size)throws Exception{
		if (log.isDebugEnabled()) {
			log.debug((new StringBuilder("queryNum :")).append(sql).toString());
		}
		javax.persistence.Query query = entityManager.createNativeQuery(sql,clazz).setFlushMode(FlushModeType.COMMIT);
		query.setMaxResults(size);
		query.setFirstResult(start);
		if (args != null) {
			String s;
			for (Iterator iterator = args.keySet().iterator(); iterator.hasNext(); query.setParameter(s, args.get(s))) {
				s = (String) iterator.next();
			}
		}
		return query.getResultList();
	}

	/**
	 * 原生sql执行
	 */
	public Object executeSQL(final String sql, final Map<String,String> args)throws Exception {
		if (log.isDebugEnabled()) {
			log.debug((new StringBuilder("queryNum :")).append(sql).toString());
		}
		javax.persistence.Query query = entityManager.createNativeQuery(sql,Long.class).setFlushMode(FlushModeType.COMMIT);
		
		if (args != null) {
			String s;
			for (Iterator iterator = args.keySet().iterator(); iterator.hasNext(); query.setParameter(s, args.get(s))) {
				s = (String) iterator.next();
			}
		}
		return Integer.valueOf(query.executeUpdate());
	}
	
	/**
	 * 原生sql 一般关联，比较复杂的查询的时候要用到的 返回的map的List
	 */
	public List loadMapBySQL(final String sql)throws Exception {
		if (log.isDebugEnabled()) {
			log.debug((new StringBuilder("queryNum :")).append(sql).toString());
		}
		javax.persistence.Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT);
		SQLQuery nativeQuery= query.unwrap(SQLQuery.class);
		nativeQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		return nativeQuery.list();
	}
	
	/**
	 * 原生sql 一般关联，比较复杂的查询的时候要用到的 返回的map的List
	 */
	public List loadMapBySQL(final String sql,final Map<String,String> args)throws Exception {
		if (log.isDebugEnabled()) {
			log.debug((new StringBuilder("queryNum :")).append(sql).toString());
		}
		javax.persistence.Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT);
		SQLQuery nativeQuery= query.unwrap(SQLQuery.class);
		nativeQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		if (args != null) {
			String s;
			for (Iterator iterator = args.keySet().iterator(); iterator.hasNext(); nativeQuery.setParameter(s, args.get(s))) {
				s = (String) iterator.next();
			}
		}
		return nativeQuery.list();
	}
	
	/**
	 *  原生sql分页查询 一般关联，比较复杂的查询的时候要用到的 返回的map  的List
	 */
	public List loadMapBySQL(final String sql,final Map<String,String> args,final int start,final int size)throws Exception {
		if (log.isDebugEnabled()) {
			log.debug((new StringBuilder("queryNum :")).append(sql).toString());
		}
		javax.persistence.Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT);
		SQLQuery nativeQuery= query.unwrap(SQLQuery.class);
		nativeQuery.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		nativeQuery.setMaxResults(size);
		nativeQuery.setFirstResult(start);
		if (args != null) {
			String s;
			for (Iterator iterator = args.keySet().iterator(); iterator.hasNext(); nativeQuery.setParameter(s, args.get(s))) {
				s = (String) iterator.next();
			}
		}
		return nativeQuery.list();
	}


	
}