package com.ydy258.ydy.util;

import java.util.Locale;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.web.servlet.LocaleResolver;

public class SpringUtils implements ApplicationContextAware, DisposableBean {
	/** applicationContext */
	private static ApplicationContext applicationContext;

	/**
	 * ����ʵ��
	 */
	private SpringUtils() {
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringUtils.applicationContext = applicationContext;
	}

	public void destroy() throws Exception {
		applicationContext = null;
	}

	/**
	 * ��ȡapplicationContext
	 * 
	 * @return applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * ��ȡʵ��
	 * 
	 * @param name
	 *            Bean���
	 * @return ʵ��
	 */
	public static Object getBean(String name) {
		Assert.hasText(name);
		return applicationContext.getBean(name);
	}

	/**
	 * ��ȡʵ��
	 * 
	 * @param name
	 *            Bean���
	 * @param type
	 *            Bean����
	 * @return ʵ��
	 */
	public static <T> T getBean(String name, Class<T> type) {
		Assert.hasText(name);
		Assert.notNull(type);
		return applicationContext.getBean(name, type);
	}

	/**
	 * ��ȡ��ʻ���Ϣ
	 * 
	 * @param code
	 *            ����
	 * @param args
	 *            ����
	 * @return ��ʻ���Ϣ
	 */
	public static String getMessage(String code, Object... args) {
		LocaleResolver localeResolver = getBean("localeResolver", LocaleResolver.class);
		Locale locale = localeResolver.resolveLocale(null);
		return applicationContext.getMessage(code, args, locale);
	}
}