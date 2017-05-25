package com.ydy258.ydy;

public final class AppConst {
	public static final int ERROR_SUCCESS = 0;
	
	public static final int ERROR_SERVER_EXCEPT = 0x10000000;
	
	public static final int ERROR_CHECKCODE_ERROR  = 0x400001;
	public static final int ERROR_CHECKCODE_EXPIRE = 0x400002;
	public static final int ERROR_MOBILE_IS_USED = 0x400003;
	public static final int ERROR_INVALID_MOBILE = 0x400004;
	public static final int ERROR_USER_NOT_EXISTS = 0x40000005;
	public static final int ERROR_NOT_ALLOW_FILE = 0x40000006;
	public static final int ERROR_SESSION_EXPIRE = 0x40000007;
	public static final int ERROR_TRUCK_ALREADY_REG = 0x40000008;
	public static final int ERROR_TRUCK_NOT_EXISTS = 0x40000009;
	
	public static final int ERROR_OLD_PASSWORD_ERROR = 0x40000010;
	
	public static final int ERROR_NECESSARY_ARGS_ERROR = 0x40000011;
	
	public static final int ERROR_DEVICE_USED_ERROR = 0x40000012;
	
	
	
	
	public static final String MSG_SUCCESS = "ok";
	public static final String MSG_SERVER_EXCEPT = "服务器异常，请联系客服！"; 
	public static final String MSG_CHECKCODE_ERROR = "验证码错误，请重新输入！";
	public static final String MSG_CHECKCODE_EXPIRE = "验证码已过期，请重新获取！";
	public static final String MSG_MOBILE_IS_USED = "手机号已被使用！";
	public static final String MSG_INVALID_MOBILE = "非法手机号，请核对！";
	public static final String MSG_USER_NOT_EXISTS = "用户不存在或者密码错误！";
	public static final String MSG_NOT_ALLOW_FILE = "本系统只允许上传JPG、GIF、PNG图片文件！";
	public static final String MSG_SESSION_EXPIRE = "会话已过期，请重新登录！";
	public static final String MSG_TRUCK_ALREADY_REG = "货车已注册！";
	public static final String MSG_TRUCK_NOT_EXISTS = "车牌号或者密码错误！";
	
	public static final String MSG_OLD_PASSWORD_ERROR = "旧密码错名误";
	
	public static final String MSG_NECESSARY_ARGS_ERROR = "必要参数不能为空;";
	
	public static final String MSG_DEVICE_USED_ERROR = "同一设备不能注册两账号";
	
	public static final String MSG_NO_DATA = "没有满足条件的数据！";
	
	public static final int GEOHASH_LENGTH = 4;
	public static final String CONSIGNOR_CACHE_NAME = "consignorInfo";
	public static final String TRUCK_CACHE_NAME = "truckInfo";
}
