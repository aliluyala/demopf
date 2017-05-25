package com.ydy258.ydy;


public class Constants {


		
		public static final String no_permission_msg  ="û��Ȩ��";
		public static final String no_permission_code  ="-0001";
		
		public static final String no_session_code  ="-0002";
		public static final String no_session_msg  ="�Ự����";
		
		public static final String data_err_code  ="-0003";
		public static final String data_err_msg  ="�ύ��������";
		
		
		public static final String login_err_code  ="-0004";
		public static final String login_err_msg  ="�˺Ż��������";
		
		
		public static final String success_code  ="0000";
		public static final String success_msg  ="�����ɹ�";
		
		public static final String failed_code  ="-0000";
		public static final String failed_msg  ="����ʧ��";
		
		
		public static final String LOGIN_KEY = "emp.session.user";
		
		/**
		 * 
		* �����ƣ�isLeaf
		* ����ʱ�䣺2015-3-26 ����11:32:15   
		* �����ˣ�������  
		* ��������   
		*     
		*
		 */
		public enum isLeaf {
			yes {
				@Override
				public int getValue() {
					return 1;
				}
			},
			no {
				@Override
				public int getValue() {
					return 0;
				}
			};
			public abstract int getValue();
		}
		
		/*�̶���ROOT �����ݷ���һ��*/
		public enum dataRoot {
			poseType {//����̶���ROOT
				@Override
				public String getKeyString() {
					return "poseType";
				}
			},
			stickerType {//����̶���ROOT
				@Override
				public String getKeyString() {
					return "stickerType";
				}
			};
			public abstract String getKeyString();
		}
		
		
		
		/*应用信息*/
		public enum UserType {
			TruckUser {
				@Override
				public  Short getType() {
					return 2;
				}
			},
			consignorUser {
				@Override
				public Short getType() {
					return 1;
				}
			},
			sysUser {
				@Override
				public Short getType() {
					return 3;
				}
			};
			public abstract Short getType();
			public static String getDescript(Short args){
				if(args == null){
					return "";
				}
				switch (args) {
		        case 2:
		            return "����";
		        case 1:
		        	return "����";
		        case 3:
		        	return "ϵͳ�û�";
		        default:
		        	return "";
		        }
			}
		}
		
		/*应用信息*/
		public enum InsuranceCompany {
			dd {
				@Override
				public  Integer getType() {
					return 3;
				}
				@Override
				public String getScript() {
					return "��ػ�����";
				}
			},
			
			ddTruckInsurance {//�������Ǳ�����
				@Override
				public  Integer getType() {
					return 11;
				}
				@Override
				public String getScript() {
					return "����������";
				}
			},
			chinaLife {
				@Override
				public  Integer getType() {
					return 2;
				}
				@Override
				public String getScript() {
					return "��������";
				}
			},
			accidentChinaLife {
				@Override
				public  Integer getType() {
					return 4;
				}
				@Override
				public String getScript() {
					return "��������";
				}
			},
			PICC {
				@Override
				public Integer getType() {
					return 1;
				}
				@Override
				public String getScript() {
					return "�˱�������";
				}
			};
			public abstract Integer getType();
			public abstract String getScript();
			public static InsuranceCompany getEmunByStatus(Integer status){
				if(status == null){
					return null;
				}
				switch (status) {
		        case 2:
		            return chinaLife;
		        case 1:
		        	return PICC;
		        case 3:
		        	return dd;
		        case 4:
		        	return accidentChinaLife;
		        case 11:
		        	return ddTruckInsurance;
		        default:
		        	return null;
		        }
			}
		}
		
		
		/*应用信息*/
		public enum InsuranceStatus {
			success {
				@Override
				public  Integer getStatus() {
					return 2;
				}
				public  String getScript(){
					return "��ɽ���";
				}
			},
			refuce {
				@Override
				public Integer getStatus() {
					return 4;
				}
				public  String getScript(){
					return "�ܱ�";
				}
			},
			pay {
				@Override
				public  Integer getStatus() {
					return 5;
				}
				public  String getScript(){
					return "��֧��";
				}
			},
			checking {
				@Override
				public Integer getStatus() {
					return 3;
				}
				public  String getScript(){
					return "�����";
				}
			},
			sendSuccess {
				@Override
				public Integer getStatus() {
					return 6;
				}
				public  String getScript(){
					return "���ύ";
				}
			},
			cancle {
				@Override
				public Integer getStatus() {
					return 7;
				}
				public  String getScript(){
					return "��ȡ��";
				}
			},
			waitPay {
				@Override
				public Integer getStatus() {
					return 1;
				}
				public  String getScript(){
					return "�ȴ�����";
				}
			};
			public abstract Integer getStatus();
			public abstract String getScript();
			public static InsuranceStatus getEmunByStatus(Integer status){
				if(status == null){
					return null;
				}
				switch (status) {
		        case 1:
		            return waitPay;
		        case 2:
		        	return success;
		        case 3:
		        	return checking;
		        case 4:
		        	return refuce;
		        case 5:
		        	return pay;
		        case 6:
		        	return sendSuccess;
		        case 7:
		        	return cancle;
		        default:
		        	return null;
		        }
			}
		}
		
		
		public enum GoodStatus {
			success {
				@Override
				public  Integer getStatus() {
					return 2;
				}
				public  String getScript(){
					return "�ɹ�";
				}
			},
			refuce {
				@Override
				public Integer getStatus() {
					return 4;
				}
				public  String getScript(){
					return "�ܾ�";
				}
			},
			checking {
				@Override
				public Integer getStatus() {
					return 3;
				}
				public  String getScript(){
					return "�����";
				}
			},
			waitPay {
				@Override
				public Integer getStatus() {
					return 1;
				}
				public  String getScript(){
					return "������";
				}
			};
			public abstract Integer getStatus();
			public abstract String getScript();
			public static GoodStatus getEmunByStatus(Integer status){
				if(status == null){
					return null;
				}
				switch (status) {
		        case 1:
		            return waitPay;
		        case 2:
		        	return success;
		        case 3:
		        	return checking;
		        case 4:
		        	return refuce;
		        default:
		        	return null;
		        }
			}
		}
		
		/*应用信息*/
		public enum OrderUserStatus {
			sendStatus {
				@Override
				public  Short getStatus() {
					return 1;
				}
			};
			public abstract Short getStatus();
		}
		
		
		public enum TradeType {
			jyrecharge{
				@Override
				public  Integer getType() {
					return -2;
				}
				public  String getZHName() {
					return "���ͳ�ֵ��";
				}
			},
			glrecharge{
				@Override
				public  Integer getType() {
					return -1;
				}
				public  String getZHName() {
					return "��·�ѳ�ֵ��";
				}
			},
			recharge{
				@Override
				public  Integer getType() {
					return 1;
				}
				public  String getZHName() {
					return "��ֵ��";
				}
			},
			insurance{
				@Override
				public  Integer getType() {
					return 3;
				}
				public  String getZHName() {
					return "���գ�";
				}
			},
			checkIdCard{
				@Override
				public  Integer getType() {
					return 2;
				}
				public  String getZHName() {
					return "������֤��";
				}
			},
			turnInDeposit{
				@Override
				public  Integer getType() {
					return 4;
				}
				public  String getZHName() {
					return "����֤��";
				}
			},
			turnOutDeposit{
				@Override
				public  Integer getType() {
					return 5;
				}
				public  String getZHName() {
					return "�˱�֤��";
				}
			},
			turnOutOil{
				@Override
				public  Integer getType() {
					return 6;
				}
				public  String getZHName() {
					return "����վ��";
				}
			},
			turnOutInsurance{
				@Override
				public  Integer getType() {
					return 7;
				}
				public  String getZHName() {
					return "�˱��ս�";
				}
			},
			proxyRecharge{
				@Override
				public  Integer getType() {
					return 8;
				}
				public  String getZHName() {
					return "�����ֵ";
				}
			},
			refund{
				@Override
				public  Integer getType() {
					return 103;
				}
				public  String getZHName() {
					return "�û��˿�";
				}
			},
			proxyRenew{
				@Override
				public  Integer getType() {
					return 101;
				}
				public  String getZHName() {
					return "��������";
				}
			},
			proxy4User{
				@Override
				public  Integer getType() {
					return 102;
				}
				public  String getZHName() {
					return "����Ϊ�û�����";
				}
			},
			cardRH{
				@Override
				public  Integer getType() {
					return 104;
				}
				public  String getZHName() {
					return "����ֵ�˿";
				}
			},
			experTime{
				@Override
				public  Integer getType() {
					return 9;
				}
				public  String getZHName() {
					return "������ʱ������";
				}
			};
			public abstract Integer getType();
			public abstract String getZHName();
		}
		
		public enum InsuranceType {
			XH{
				@Override
				public  String getTypeCode() {
					return "YDB001";
				}
				@Override
				public  Double getJG() {
					return 5D;
				}
			},
			ZH{
				@Override
				public  String getTypeCode() {
					return "YDL001";
				}
				@Override
				public  Double getJG() {
					return 6D;
				}
			};
			public abstract String getTypeCode();
			public abstract Double getJG();
		}
		
}
