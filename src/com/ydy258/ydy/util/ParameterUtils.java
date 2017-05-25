package com.ydy258.ydy.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

public class ParameterUtils {
	// private static final String blankStr = "";
	public ParameterUtils() {
	}

	public static void setCharacterEncoding(HttpServletRequest request)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("UTF-8");
	}

	public static String getString(HttpServletRequest request, String paramName)
			throws UnsupportedEncodingException {
		String value = request.getParameter(paramName);
		return value != null ? value : "";
	}

	public static String[] getArray(HttpServletRequest request, String paramName)
			throws UnsupportedEncodingException {
		return request.getParameterValues(paramName);
	}

	public static byte getByte(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (value == null || value.length() == 0)
			return 0;
		else
			return Byte.parseByte(value);
	}

	public static int getInt(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (value == null || value.length() == 0)
			return 0;
		else
			return Integer.parseInt(value);
	}

	public static long getLong(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (value == null || value.length() == 0)
			return 0L;
		else
			return Long.parseLong(value);
	}

	public static short getShort(HttpServletRequest request, String paramName) {
		String value = request.getParameter(paramName);
		if (value == null || value.length() == 0)
			return 0;
		else
			return Short.parseShort(value);
	}

	public static boolean getBoolean(HttpServletRequest request,
			String paramName) {
		String value = request.getParameter(paramName);
		if (value == null || value.length() == 0)
			return false;
		else
			return Boolean.valueOf(value).booleanValue();
	}
	
	public static double getDouble(HttpServletRequest request,
			String paramName) {
		String value = request.getParameter(paramName);
		if (value == null || value.length() == 0)
			return 0.00;
		else
			return Double.valueOf(value).doubleValue();
	}
}