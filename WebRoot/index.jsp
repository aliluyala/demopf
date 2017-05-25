<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>应用运营平台</title>
    <link href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="extjs/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<link rel="stylesheet" type="text/css" href="css/icons.css">
	<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
	<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
	<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script src="thirdpart/highcharts/code/highcharts.js"></script>
	<script src="thirdpart/highcharts/code/modules/exporting.js"></script>
	<script type="text/javascript" src="extjs/ext-all.js"></script> 
	<script type="text/javascript" src="app.js"></script> 
	
	
  </head>
<iframe id='contentIframe' name='contentIframe' style='height:100%;width:100%' frameborder="no"></iframe>
<body>
	<div  id='panel_el' class="panel panel-default"  style="height:100%;border-radius:15px 50px">
		<div class="panel-heading" style="border-radius:15px 50px 0px 0px">
			<h3 class="glyphicon glyphicon-tags panel-title">
				司机数量
			</h3>
		</div>
		<div class="panel-body" style="text-align:center">
			 <span><h1>10000</h1></span>
		</div>
	</div>
	
	<div  id='panel_el1' class="panel panel-default"  style="height:100%;border-radius:15px 50px">
		<div class="panel-heading" style="border-radius:15px 50px 0px 0px">
			<h3 class="glyphicon glyphicon-tags panel-title">
				司机数量
			</h3>
		</div>
		<div class="panel-body" style="text-align:center">
			 <span><h1>10000</h1></span>
		</div>
	</div>
	
	<div  id='panel_el2' class="panel panel-default"  style="height:100%;border-radius:15px 50px">
		<div class="panel-heading" style="border-radius:15px 50px 0px 0px">
			<h3 class="glyphicon glyphicon-tags panel-title">
				司机数量
			</h3>
		</div>
		<div class="panel-body" style="text-align:center">
			 <span><h1>10000</h1></span>
		</div>
	</div>
	
	<div  id='panel_el3' class="panel panel-default"  style="height:100%;border-radius:15px 50px">
		<div class="panel-heading" style="border-radius:15px 50px 0px 0px">
			<h3 class="glyphicon glyphicon-tags panel-title">
				司机数量
			</h3>
		</div>
		<div class="panel-body" style="text-align:center">
			 <span><h1>10000</h1></span>
		</div>
	</div>
	
	<div id="container1" style="height:100%;margin:0;"></div>
	<div id="container2" style="height:100%;margin:0;"></div>
	<div id="container3" style="height:100%;margin:0;"></div>

</body>
</html>
