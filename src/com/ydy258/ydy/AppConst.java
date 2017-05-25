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
	public static final String MSG_SERVER_EXCEPT = "�������쳣������ϵ�ͷ���"; 
	public static final String MSG_CHECKCODE_ERROR = "��֤��������������룡";
	public static final String MSG_CHECKCODE_EXPIRE = "��֤���ѹ��ڣ������»�ȡ��";
	public static final String MSG_MOBILE_IS_USED = "�ֻ����ѱ�ʹ�ã�";
	public static final String MSG_INVALID_MOBILE = "�Ƿ��ֻ��ţ���˶ԣ�";
	public static final String MSG_USER_NOT_EXISTS = "�û������ڻ����������";
	public static final String MSG_NOT_ALLOW_FILE = "��ϵͳֻ�����ϴ�JPG��GIF��PNGͼƬ�ļ���";
	public static final String MSG_SESSION_EXPIRE = "�Ự�ѹ��ڣ������µ�¼��";
	public static final String MSG_TRUCK_ALREADY_REG = "������ע�ᣡ";
	public static final String MSG_TRUCK_NOT_EXISTS = "���ƺŻ����������";
	
	public static final String MSG_OLD_PASSWORD_ERROR = "�����������";
	
	public static final String MSG_NECESSARY_ARGS_ERROR = "��Ҫ��������Ϊ��;";
	
	public static final String MSG_DEVICE_USED_ERROR = "ͬһ�豸����ע�����˺�";
	
	public static final String MSG_NO_DATA = "û���������������ݣ�";
	
	public static final int GEOHASH_LENGTH = 4;
	public static final String CONSIGNOR_CACHE_NAME = "consignorInfo";
	public static final String TRUCK_CACHE_NAME = "truckInfo";
}
