package com.ydy258.ydy;


public class Constants {


		
		public static final String no_permission_msg  ="没有权限";
		public static final String no_permission_code  ="-0001";
		
		public static final String no_session_code  ="-0002";
		public static final String no_session_msg  ="会话过期";
		
		public static final String data_err_code  ="-0003";
		public static final String data_err_msg  ="提交数据有误";
		
		
		public static final String login_err_code  ="-0004";
		public static final String login_err_msg  ="账号或密码错误";
		
		
		public static final String success_code  ="0000";
		public static final String success_msg  ="操作成功";
		
		public static final String failed_code  ="-0000";
		public static final String failed_msg  ="操作失败";
		
		
		public static final String LOGIN_KEY = "emp.session.user";
		
		/**
		 * 
		* 类名称：isLeaf
		* 创建时间：2015-3-26 上午11:32:15   
		* 创建人：邓忠明  
		* 类描述：   
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
		
		/*固定的ROOT 与数据分类一致*/
		public enum dataRoot {
			poseType {//分类固定的ROOT
				@Override
				public String getKeyString() {
					return "poseType";
				}
			},
			stickerType {//分类固定的ROOT
				@Override
				public String getKeyString() {
					return "stickerType";
				}
			};
			public abstract String getKeyString();
		}
		
		
		
		/*搴旂敤淇℃伅*/
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
		            return "车主";
		        case 1:
		        	return "货主";
		        case 3:
		        	return "系统用户";
		        default:
		        	return "";
		        }
			}
		}
		
		/*搴旂敤淇℃伅*/
		public enum InsuranceCompany {
			dd {
				@Override
				public  Integer getType() {
					return 3;
				}
				@Override
				public String getScript() {
					return "大地货运险";
				}
			},
			
			ddTruckInsurance {//车主无忧保代码
				@Override
				public  Integer getType() {
					return 11;
				}
				@Override
				public String getScript() {
					return "车主无忧险";
				}
			},
			chinaLife {
				@Override
				public  Integer getType() {
					return 2;
				}
				@Override
				public String getScript() {
					return "人寿人险";
				}
			},
			accidentChinaLife {
				@Override
				public  Integer getType() {
					return 4;
				}
				@Override
				public String getScript() {
					return "人寿人险";
				}
			},
			PICC {
				@Override
				public Integer getType() {
					return 1;
				}
				@Override
				public String getScript() {
					return "人保货运险";
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
		
		
		/*搴旂敤淇℃伅*/
		public enum InsuranceStatus {
			success {
				@Override
				public  Integer getStatus() {
					return 2;
				}
				public  String getScript(){
					return "完成交易";
				}
			},
			refuce {
				@Override
				public Integer getStatus() {
					return 4;
				}
				public  String getScript(){
					return "拒保";
				}
			},
			pay {
				@Override
				public  Integer getStatus() {
					return 5;
				}
				public  String getScript(){
					return "已支付";
				}
			},
			checking {
				@Override
				public Integer getStatus() {
					return 3;
				}
				public  String getScript(){
					return "审核中";
				}
			},
			sendSuccess {
				@Override
				public Integer getStatus() {
					return 6;
				}
				public  String getScript(){
					return "已提交";
				}
			},
			cancle {
				@Override
				public Integer getStatus() {
					return 7;
				}
				public  String getScript(){
					return "已取消";
				}
			},
			waitPay {
				@Override
				public Integer getStatus() {
					return 1;
				}
				public  String getScript(){
					return "等待付款";
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
					return "成功";
				}
			},
			refuce {
				@Override
				public Integer getStatus() {
					return 4;
				}
				public  String getScript(){
					return "拒绝";
				}
			},
			checking {
				@Override
				public Integer getStatus() {
					return 3;
				}
				public  String getScript(){
					return "待审核";
				}
			},
			waitPay {
				@Override
				public Integer getStatus() {
					return 1;
				}
				public  String getScript(){
					return "待付款";
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
		
		/*搴旂敤淇℃伅*/
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
					return "加油充值！";
				}
			},
			glrecharge{
				@Override
				public  Integer getType() {
					return -1;
				}
				public  String getZHName() {
					return "过路费充值！";
				}
			},
			recharge{
				@Override
				public  Integer getType() {
					return 1;
				}
				public  String getZHName() {
					return "充值！";
				}
			},
			insurance{
				@Override
				public  Integer getType() {
					return 3;
				}
				public  String getZHName() {
					return "买保险！";
				}
			},
			checkIdCard{
				@Override
				public  Integer getType() {
					return 2;
				}
				public  String getZHName() {
					return "检查身份证！";
				}
			},
			turnInDeposit{
				@Override
				public  Integer getType() {
					return 4;
				}
				public  String getZHName() {
					return "交保证金！";
				}
			},
			turnOutDeposit{
				@Override
				public  Integer getType() {
					return 5;
				}
				public  String getZHName() {
					return "退保证金！";
				}
			},
			turnOutOil{
				@Override
				public  Integer getType() {
					return 6;
				}
				public  String getZHName() {
					return "加油站！";
				}
			},
			turnOutInsurance{
				@Override
				public  Integer getType() {
					return 7;
				}
				public  String getZHName() {
					return "退保险金";
				}
			},
			proxyRecharge{
				@Override
				public  Integer getType() {
					return 8;
				}
				public  String getZHName() {
					return "代理充值";
				}
			},
			refund{
				@Override
				public  Integer getType() {
					return 103;
				}
				public  String getZHName() {
					return "用户退款";
				}
			},
			proxyRenew{
				@Override
				public  Integer getType() {
					return 101;
				}
				public  String getZHName() {
					return "代理续费";
				}
			},
			proxy4User{
				@Override
				public  Integer getType() {
					return 102;
				}
				public  String getZHName() {
					return "代理为用户续费";
				}
			},
			cardRH{
				@Override
				public  Integer getType() {
					return 104;
				}
				public  String getZHName() {
					return "卡充值退款！";
				}
			},
			experTime{
				@Override
				public  Integer getType() {
					return 9;
				}
				public  String getZHName() {
					return "续服务时间赠送";
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
