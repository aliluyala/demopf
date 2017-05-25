package com.ydy258.ydy.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/** 
 *   实体对象的基类，�?有实体类�?继承该类
 */

public class Entity implements Serializable, IElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1863560649962507646L;

	// 把日志记录到log4j中输�?
	private static final Log logger = LogFactory.getLog(Entity.class);
	
	public static  enum Status{start,end,ineffective,effective};
	
    public Long id;
    public Entity creator;
    public String lastModifier;
    
    public Date createdDate;
    public Date lastModifiedDate;
    public Boolean invalid;

    /**
     * 无参构�?�函数，构�?�一个无效的实体
     */
    public Entity() {
        createdDate = new Date();
        lastModifiedDate = new Date();
        invalid = Boolean.valueOf(false);
    }
    

    /**
     * 剪切字符�?
     * @param s - 源字符串
     * @param i - 长度
     * @return 剪切后的字符�? 
     */
    public String cutString(String s, int i) {
        int j = s.length() <= i ? s.length() : i;
        return s.substring(0, j);
    }

    /**
     * 字符�? --> 日期 类型转换，字符串的格式必须是4位年�?2位月�?2位日，时间可�?
     * @param s
     * @return
     * @throws ParseException
     */
    public static Date makeDateFromString(String s) throws ParseException {
        SimpleDateFormat simpledateformat = null;
        String s1 = s.substring(4, 5);
        if(s.length() == 10)
            simpledateformat = new SimpleDateFormat((new StringBuilder("yyyy")).append(s1).append("MM").append(s1).append("dd").toString(), Locale.US);
        else
            simpledateformat = new SimpleDateFormat((new StringBuilder("yyyy")).append(s1).append("MM").append(s1).append("dd").append(" HH:mm:ss").toString(), Locale.US);
        return simpledateformat.parse(s);
    }

    
    public void accept(IVisitor ivisitor) {
        ivisitor.visit(this);
    }
    
    
    /**
     * 判断是否同一实例
     */
    @Override
	public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        Entity entity = (Entity)obj;
        return id == null ? entity.id == null : id.equals(entity.id);
    }

    
    /**
     * @return 如果id属�?�为null，则返回super.hashCode()，否则返回id的hashCode
     */
    @Override
	public int hashCode() {
        return id == null ? super.hashCode() : id.hashCode();
    }

    
    /**
     * @return 返回由属性字段Field组成的属性名数组
     */
    public String[] getProperties() {
        Field afield[] = getClass().getFields();
        String as[] = new String[afield.length];
        int i = 0;
        Field afield1[] = afield;
        int j = 0;
        for(int k = afield1.length; j < k; j++)
        {
            Field field = afield1[j];
            as[i++] = field.getName();
        }

        return as;
    }

    
    /**
     * 获得指定属�?�名的�??
     * @param s - 属�?�名�?
     * @return 返回指定属�?�名的�??
     */
    public Object getPropertyValue(String s) {
        Object obj = null;
        try {
            Field field = getClass().getField(s);
            obj = field.get(this);
        }
        catch(Exception _ex) {
            logger.warn((new StringBuilder(String.valueOf(getClass().getName()))).append(" haven't ").append(s).toString());
        }
        return obj;
    }

    
    /**
     * 设置指定属�?�名的�??
     * @param s - 属�?�名�?
     * @param obj - �?
     */
    public void setPropertyValue(String s, Object obj) {
        try {
            Field field = getClass().getField(s);
            String s1 = field.getType().getSimpleName();
            if(s1.equals(obj.getClass().getSimpleName()))
                field.set(this, obj);
            else
            	if(obj.getClass().equals(String.class))
            		field.set(this, String.valueOf(obj));
            	else
                if(s1.equals("Integer") || s1.equals("int"))
                    field.set(this, Integer.valueOf(obj.toString()).intValue());
                else
                if(s1.equals("Float") || s1.equals("float"))
                    field.set(this, Float.valueOf(obj.toString()).floatValue());
                else
                if(s1.equals("Double") || s1.equals("double"))
                    field.set(this, Double.valueOf(Double.valueOf((String)obj).doubleValue()));
                else
                if(s1.equals("Boolean") || s1.equals("boolean"))
                    field.set(this, Boolean.valueOf(obj.toString()).booleanValue());
                else
                if("Timestamp".equals(s1) || "Date".equals(s1))
                {
                    SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                    String s2 = (String)obj;
                    if(s2.indexOf("/") >= 0)
                    {
                        field.set(this, simpledateformat.parse(s2));
                    } else
                    {
                        SimpleDateFormat simpledateformat1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US);
                        field.set(this, simpledateformat1.parse(s2.replaceFirst("CST", "")));
                    }
                }
        }
        catch(Exception _ex) {
            logger.warn((new StringBuilder(String.valueOf(getClass().getName()))).append(" setPropertyValue error ").append(" for ").append(s).toString());
        }
    }

    
    private String makeNewLine(String s) {
        return (new StringBuilder(String.valueOf(s))).append("\n").toString();
    }

    
    private String makePropertyXML(String s) {
        StringBuilder stringbuilder = new StringBuilder();
        Object obj = getPropertyValue(s);
        String s1 = "";
        if(obj == null)
            return s1;
        stringbuilder.append((new StringBuilder("<")).append(s).append(">").toString());
        String s2 = obj.getClass().getName();
        if("java.sql.Timestamp".equals(s2) || "java.util.Date".equals(s2))
            s1 = (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US)).format(obj);
        else
            s1 = obj.toString();
        stringbuilder.append(s1);
        stringbuilder.append(makeNewLine((new StringBuilder("</")).append(s).append(">").toString()));
        return stringbuilder.toString();
    }

    
    /**
     * 实体转换成xml
     * @return xml格式的字符串
     */
    public String toXML() {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("\n\r<");
        stringbuilder.append(getClass().getSimpleName());
        stringbuilder.append(makeNewLine(">"));
        String as[] = getProperties();
        int i = 0;
        for(int j = as.length; i < j; i++)
        {
            String s = as[i];
            String s1 = makePropertyXML(s);
            if(s1 != "")
                stringbuilder.append(s1);
        }

        stringbuilder.append("</");
        stringbuilder.append(getClass().getSimpleName());
        stringbuilder.append(makeNewLine(">"));
        return stringbuilder.toString();
    }

    
    private void setValue(String s, String s1) {
        String s2 = (new StringBuilder("<")).append(s1).append(">").toString();
        String s3 = (new StringBuilder("</")).append(s1).append(">").toString();
        int i = s.indexOf(s2);
        int j = s.indexOf(s3);
        if(i + s2.length() <= j)
        {
            String s4 = s.substring(i + s2.length(), j);
            setPropertyValue(s1, s4);
        } else
        {
            logger.warn((new StringBuilder("did't found :")).append(s1).append("--").append(s).toString());
        }
    }

    
    /**
     * 根据xml初始化实体属�?
     * @param s - xml格式的字符串
     */
    public void initFromXML(String s) {
        String as[] = getProperties();
        int i = 0;
        for(int j = as.length; i < j; i++)
        {
            String s1 = as[i];
            setValue(s, s1);
        }

    }

    
    /**
     * 根据另外�?个实体初始化本实体的属�??
     * @param entity
     */
    public void initFromEntity(Entity entity) {
        String as[] = getProperties();
        String as1[] = as;
        int i = 0;
        for(int j = as1.length; i < j; i++)
        {
            String s = as1[i];
            Object obj = entity.getPropertyValue(s);
            setPropertyValue(s, obj);
        }

    }

    
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date date) {
        createdDate = date;
    }

    public Entity getCreator() {
        return creator;
    }

    public void setCreator(Entity s) {
        creator = s;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean boolean1) {
        invalid = boolean1;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date date) {
        lastModifiedDate = date;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(String s) {
        lastModifier = s;
    }

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
}