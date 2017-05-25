package com.ydy258.ydy;

import org.apache.commons.beanutils.converters.AbstractConverter;

public class EnumConverter extends AbstractConverter {
	/** ö������ */
	private final Class<?> enumClass;

	/**
	 * @param enumClass
	 *            ö������
	 */
	public EnumConverter(Class<?> enumClass) {
		this(enumClass, null);
	}

	/**
	 * @param enumClass
	 *            ö������
	 * @param defaultValue
	 *            Ĭ��ֵ
	 */
	public EnumConverter(Class<?> enumClass, Object defaultValue) {
		super(defaultValue);
		this.enumClass = enumClass;
	}

	/**
	 * ��ȡĬ������
	 * 
	 * @return Ĭ������
	 */
	@Override
	protected Class<?> getDefaultType() {
		return this.enumClass;
	}

	/**
	 * ת��Ϊö�ٶ���
	 * 
	 * @param type
	 *            ����
	 * @param value
	 *            ֵ
	 * @return ö�ٶ���
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object convertToType(Class type, Object value) {
		String stringValue = value.toString().trim();
		return Enum.valueOf(type, stringValue);
	}

	/**
	 * ת��Ϊ�ַ���
	 * 
	 * @param value
	 *            ֵ
	 * @return �ַ���
	 */
	protected String convertToString(Object value) {
		return value.toString();
	}
}
