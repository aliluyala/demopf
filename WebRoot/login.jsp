<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>网站后台</title>
        <!-- <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=0"> -->
        <meta http-equiv=“X-UA-Compatible” content=“IE=8″>
		<script type="text/javascript" src="extjs/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="extjs/ext-all.js"></script>
		<script type="text/javascript" src="login.js"></script>
        <style type="text/css">
         /* 初始化 */
        @charset "utf-8";
        /* 取消链接高亮  */
        body,div,ul,li,ol,h1,h2,h3,h4,h5,h6,input,textarea,select,p,dl,dt,dd,a,img,button,form,table,th,tr,td,tbody,article,
        aside, details,figcaption,figure,footer,header,hgroup, menu,nav,section{ -webkit-tap-highlight-color:rgba(0, 0, 0, 0); }
        /* 设置HTML5元素为块 */
        article, aside, details,figcaption,figure,footer,header,hgroup, menu,nav,section {
        display: block;
        }
        /* 图片自适应 */
        img { max-width: 100%; height: auto;width:auto\9; /* ie8 */-ms-interpolation-mode:bicubic;/*为了照顾ie图片缩放失真*/}
        /* 初始化 */
        body,div,ul,li,ol,h1,h2,h3,h4,h5,h6,input,textarea,select,p,dl,dt,dd,a,img,button,form,table,th,tr,td,tbody,article,
        aside, details,figcaption,figure,footer,header,hgroup, menu,nav,section{margin:0; padding:0; border:none; color:#444444;}
        body{ font: normal 14px/1.5 Tahoma,"Lucida Grande",Verdana,"Microsoft Yahei",STXihei,hei; overflow:hidden;}
        em,i{ font-style:normal;}
        strong{ font-weight: normal;}
        .clearfix:after{content:""; display:block; visibility:hidden; height:0; clear:both;}
        .clearfix{zoom:1;}
        a{text-decoration:none; color:#444444; font-family:Microsoft YaHei,Tahoma,Arial,sans-serif;}
        a:hover{color:#444444; text-decoration:none;}
        ul,ol{list-style:none;}
        h1, h2, h3, h4, h5, h6{ font-size:100%; font-family: Microsoft YaHei;}
        img{border: none; display:block; }
        body,html{ width:100%; height:100%; background: #fbfbfb; overflow-x: hidden; }
        

        /* css */
        input{outline: 0 none;}
        body,html{width:100%; height:100%; overflow:hidden;}
        .content .bg{position: absolute; top: 0; left: 0; background-size: cover; background-repeat: no-repeat; background-position: center center; width: 100%;}
        .content .content_login{ width: 790px; position: absolute; top: 50%; left:50%; margin:0 auto;  margin-left:-395px; margin-top:-256px;} 
        .content .content_layout{/* width: 790px; */ margin: 0 auto; overflow: hidden; position: relative; height: 422px; z-index: 999;  /* position: absolute; top: 25%; left: 25%; margin:0 auto; */ background:url(img/login_bor22.png) no-repeat ; background-size: cover; background-repeat: no-repeat; background-position: center center; /* margin-top:8%;margin-top:8%\9; */ }
        .login_box_warp {position: absolute; top: 96px; right: 8px; width: 420px; height:312px; padding: 37px 58px 25px; -webkit-box-sizing: border-box; -moz-box-sizing: border-box; box-sizing: border-box;  z-index: 1000; }
        .title{ height: 20px; line-height: 20px; font-size: 20px; color: #f69803;   font-weight: 200; display:inline-block;font-family: "Microsoft Yahei" ;}
        .title_en{  font-size: 16px; color: #a9a9a8; }
        .login_con_tit{ margin-top: 20px; position: relative; line-height: 30px; padding: 10px 0;box-sizing: border-box; background: #fff; overflow: hidden;  }
        .login_con_tit .left{width: 20%;text-align: center; display: inline-block;border-right: 1px solid #ccc;box-sizing: border-box; }       
        .login_con_tit input{line-height: 30px; padding: 10px 0; width:80%; border:none; background: #fff; font-family: "Microsoft Yahei"; font-size: 14px; background:rgba(0, 0, 0, 0);  padding-left:6px;  -webkit-box-sizing: border-box;-moz-box-sizing: border-box; box-sizing: border-box; position:absolute; top:0; left:62px; vertical-align: middle; }

        /*  WebKit browsers */
        .login_con_inp:-webkit-input-placeholder {color: #777; }
        /* Mozilla Firefox 4 to 18 */
        .login_con_inp:-moz-placeholder {color: #777; opacity: 1;}
        /* Mozilla Firefox 19+ */
        .login_con_inp:-moz-placeholder {color: #777; opacity: 1;}
        /* Internet Explorer 10+ */
        .login_con_inp:-ms-input-placeholder {color: #777;}

        .login_but{width:100%; height:50px; line-height: 50px; background:#f69803; font-size: 16px; color:#fff;  border-radius: 3px;  cursor: pointer;  text-align: center;  }
        .prompt{width:100%; height:26px; display:block; line-height: 28px; color:red;  } 
        .content{ /* position: relative; */ width:100%;}
        .footer{width: 100%; margin: 0 auto; overflow: hidden; position: relative; height: auto; z-index: 998;   position: absolute; bottom: 2%;  margin:0 auto;text-align: center; }
        .footer p{color:#cccccc; }
        </style>

    </head>
    <body>  
        <div class="content">        
            <div class="bg" style="background-image: url(img/login_bg.png);width:100%;height:100%;"></div>
            <div class="content_login"> 
                <div class="content_layout"></div>                            
                <div class="login_box_warp">
                    <div class="login_box">
                        <div class="login_title">
                            <div class="title" >用户登录</div>
                            <div class="title title_en" >UserLogin</div>
                        </div>  
                        <form id="form" action="sys/user/userLogin.action" method="post">
                            <div class="login_con_tit"> 
                                <i class="left">账号</i> 
                                <i class="line"></i>
                                <input type="text" id="userName" name="userName" class="login_con_inp" placeholder="请输入用户名" > 
                            </div>
                            <div class="login_con_tit">
                                <i class="left">密码</i> 
                                <i class="line"></i>
                                <input name="passWord" id="passWord" type="password" class="login_con_inp" placeholder="请输入密码" />
                            </div>
                            <div class="prompt">
                                <span  id="tip" style="display:none">                                    
                                   		 账户名与密码不匹配，请重新输入
                                </span>
                            </div> 
                            <div class="login_but"  id="loginbut" >登录</a>
                            </div>
                        </form> 
                    </div>
                </div>               
            </div> 
        </div>
        <div class="footer">
                <p>Copyright© 2015~2017 Yidao Netword Techonology. all Rights Rese</p>
                <p>东莞驿道网络科技有限公司  版权所有</p>
                <p>客服电话：400-675-6568</p>
        </div>    

    </body>
</html>