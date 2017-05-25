package com.ydy258.ydy.service;

public interface IBaseService {
	/**
	 * 
	 * @Title:        save 
	 * @Description:  TODO 保存实体 、修改
	 * @param:        @param entity    
	 * @return:       void    
	 * @throws 
	 * @author        Administrator
	 * @Date          2015年8月8日 下午2:06:08
	 */
    void save(Object entity);
    /**
     * 
     * @Title:        update 
     * @Description:  TODO 修改实体对像
     * @param:        @param entity    
     * @return:       void    
     * @throws 
     * @author        Administrator
     * @Date          2015年8月8日 下午2:06:33
     */
    void update(Object entity);
    
   /**
    * 
    * @Title:        getById 
    * @Description:  TODO 查询指定类型、指定ID实体对像
    * @param:        @param clazz 类型
    * @param:        @param id ID
    * @param:        @return    
    * @return:       T    
    * @throws 
    * @author        Administrator
    * @Date          2015年8月8日 下午2:07:31
    */
    <T> T getById(Class<T> clazz,Object id);
    /**
     * 
     * @Title:        delete 
     * @Description:  TODO 删除指定类型、指定ID的实体
     * @param:        @param clazz 类型
     * @param:        @param id ID
     * @param:        @throws Exception    
     * @return:       void    
     * @throws 
     * @author        Administrator
     * @Date          2015年8月8日 下午2:11:09
     */
    public <T> void delete(Class<T> clazz,Long id)throws Exception;
}