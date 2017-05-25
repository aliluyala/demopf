<%@ page language="java" import="java.util.*" pageEncoding="GB2312"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
		<meta http-equiv="Content-Type" content="text/html; charset=GB2312" />

		<title>iPlat-智能网优分析平台</title>
		<link rel="stylesheet" type="text/css"
			href="<%=basePath %>/login/login1.css" />
		<style type="text/css">
			.butt {
				margin-left: 60px;
			}
			.in_font {
				text-indent: 52px;
				margin: 5px 0px 5px 0px;
				color: #900;
				height: 10px;
			}
		</style>

	</head>
	<body class="all1">
		<div class="all2">
			<div class="bg">
				<div class="login_bor">
					<div class="login_con">
						<form id="form1" action="Login.do" method="post"  onkeydown="if(event.keyCode==13){login();}">
							<div class="login_con_tit">
								账号:
							<input name="account" id="account" type="text"
								class="login_con_inp" onkeypress="maxtextvalue(this,15);" value=""
								onblur="getPwdAndChk();"  />
							</div>
							<div class="login_con_tit">
								密码:
							<input name="password" id="password" type="password" style="*display:block;" 
								class="login_con_inp" onkeypress="maxtextvalue(this,15);"  />
								</div>
								<div style="color:#F90;padding:2px; position:absolute; display:none;" id="capital">大写锁定已开启</div> 
							<br />
							<input name="" type="checkbox" value="" id="chkRememberPwd" align="right"/>&nbsp;记住密码					
                    
							<div id="se" class="in_font"><%=request.getParameter("message") == null ? "": request.getParameter("message")%></div>			
							<div class="login_but"  onclick="login();" >
					    </div>
						</form>
					</div>
				</div>
			</div>
			<div class="foot">
				<div class="foot_line">
					<p align="center" ;style="font-family: fantasy" >
						中国移动南昌分公司&nbsp; | &nbsp;  建议使用IE8及以上版本浏览器浏览
					</p>
				</div>
			</div>
		</div>
	</body>
</html>
