package com.ydy258.ydy.junit;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
* �����ļ������� 
* @ClassName: BaseSpringTestCase 
* @Description: Ҫ��ʵ��Spring�Զ�ע�룬����̳д���
* @author yusj  
* @date 2014��6��9�� ����3:16:44 
*
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "file:src/main/webapp/WEB-INF/config/applicationContext.xml",
    "file:src/main/webapp/WEB-INF/config/captcha-context.xml",
    "file:src/main/webapp/WEB-INF/config/springmvc-servlet.xml"
})

// ���ע��@Transactional �ع�����ݿ����  
@Transactional
public class BaseSpringTestCase {
	
}
