<%@ page language="java" import="java.util.*" pageEncoding="utf8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
	<head>
	<meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=0">
	<title>团购消费凭证</title>
	<script type="text/javascript" src="../../jquerylib/jquery-3.1.0.min.js"></script>   
	<script type="text/javascript" src="../../jquerylib/jquery.PrintArea.js"></script>   
	<script>  
		$(document).ready(function(){  
			  $("input#biuuu_button").click(function(){  
			  		$("div#myPrintArea").printArea();  
			}); 
			
			$.ajax({
				    url: "http://www.hzhuti.com",    //请求的url地址
				    dataType: "json",   //返回格式为json
				    async: true, //请求是否异步，默认为异步，这也是ajax重要特性
				    data: { "id":"value" },    //参数值
				    type: "GET",   //请求方式
				    success: function(req) {
				        //请求成功时处理
				    },
				    error: function() {
				        //请求出错处理
				    }
			}); 
		});  
	   
	</script>  
	
	
	</head>
<body>
	
  
	<div id="myPrintArea">
		<style type="text/css">
		  /* 初始化 */
          @charset "utf-8";
          /* 去除iPhone中默认的input样式 */
          input[type="submit"],
          input[type="reset"],
          input[type="button"],
          input{-webkit-appearance:none; resize: none;}
          /* 取消链接高亮  */
          body,div,ul,li,ol,h1,h2,h3,h4,h5,h6,input,textarea,select,p,dl,dt,dd,a,img,button,form,table,th,tr,td,tbody,article,
          aside, details,figcaption,figure,footer,header,hgroup, menu,nav,section{ -webkit-tap-highlight-color:rgba(0, 0, 0, 0); }
          /* 设置HTML5元素为块 */
          article, aside, details,figcaption,figure,footer,header,hgroup, menu,nav,section {
          display: block;
          }
          /* 图片自适应 */
          img { max-width: 100%;height: auto;width:auto\9; /* ie8 */-ms-interpolation-mode:bicubic;/*为了照顾ie图片缩放失真*/}
          /* 初始化 */
          body,div,ul,li,ol,h1,h2,h3,h4,h5,h6,input,textarea,select,p,dl,dt,dd,a,img,button,form,table,th,tr,td,tbody,article,
          aside, details,figcaption,figure,footer,header,hgroup, menu,nav,section{margin:0; padding:0; border:none; color:#000;}
          body{ font: normal Tahoma,"Lucida Grande",Verdana,"Microsoft Yahei",STXihei,hei; overflow:hidden; }
          em,i{ font-style:normal;}
          strong{ font-weight: normal;}
          .clearfix:after{content:""; display:block; visibility:hidden; height:0; clear:both;}
          .clearfix{zoom:1;}         
          ul,ol{list-style:none;}
          h1, h2, h3, h4, h5, h6{ font-size:100%; font-family: Microsoft YaHei;}
          img{border: none; display:block; }
          body,html{ width:240px; height:auto; background: #fff; overflow-x: hidden; }
          body{font-size: 12px !important;}
     

          /*css*/
          header{ width:100%; line-height:30px; font-size: 1.2rem; text-align:center; box-sizing: border-box; padding:2.5%; }
          header h2{font-size:14px;}
          .shop_message{width:100%; padding:0 2.5%; box-sizing: border-box; display:flex; }
          .shop_message_left ul li{ line-height:1.1rem; }
          .shop_message_left{float: left; width:74%; }
          .shop_message_right{float: right; width:30%; height:auto; flex:1; }
          .shop_message_right img{ width:74px; height:auto; margin-top:5px; }
          .shop_sale h4,footer h4{ width:100%; height:40px; line-height:40px;text-align: center; font-weight: normal; overflow:hidden; padding:0 2.5%; box-sizing: border-box;}
          .shop_sale_list{ width:100%;  padding:0 2.5%; box-sizing: border-box;text-align: center;  font-size: 12px !important;  }
          table{width:100%; text-align: center; border-collapse: collapse;}
          tbody tr{border-collapse: collapse;     }
          tbody th{ border-collapse: collapse;  }
          tbody tr td,tbody tr th{ text-align: center; border: 1px solid #000; box-sizing: border-box; padding:1.8% 1%; line-height: 16px; }
          .telephone,.user_name{ width:100%; padding:0 2.5%; box-sizing: border-box;}
          .telephone p{ line-height:40px;}
          .user_name{font-size: 14px; font-weight:700; line-height:30px;}

	</style>
	  <header>
        <h2>运的易消费凭证</h2>
      </header>
      <div class="shop_message">
        <div class="shop_message_left">
          <ul>
            <li>商户名: <span id="storeName"></span></li>
            <li>柜员号: <span id="tellerId"></span></li>
            <li>订单号: <span id="orderNo"></span></li>
            <li>支付账号: <span id="mobile"></span></li>
            <li>交易时间: <span id="payTime"></span></li>
          </ul>
        </div>
        <div class="shop_message_right wechat">
          <img src="wechat.png" alt="" />
        </div>
      </div>
      <div class="shop_sale">
         <h4>------------- 销 ---------- 售 -------------</h4>
      </div>
      <div class="shop_sale_list">
        <table id="tableId" style="font-size:12px" >
          <tbody>
            <tr>
              <th>商品名称</th>
              <th style="width:20%">单价</th>
              <th style="width:20%">数量</th>
              <th style="width:18%">金额</th>
            </tr>
            <tr style="font-weight: 700">
              <td>合计</td>
              <td>---</td>
              <td id="count"></td>
              <td id="total"></td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="telephone">
        <p>运的易客服电话：400-675-6568</p>
      </div>
      <div class="user_name">
        <p>客户确认:</p>
      </div>
      <footer>
         <h4>------------------ 第1联 ------------------</h4>
      </footer>

                    
	</div>
	<input id="biuuu_button" type="button" hidden value="打印"></input>  
</body>
</html>