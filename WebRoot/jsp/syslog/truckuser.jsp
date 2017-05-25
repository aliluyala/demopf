<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>步行导航</title>
<link rel="stylesheet" type="text/css" href="../../extjs/resources/css/ext-all.css" />	
	<script type="text/javascript" src="../../extjs/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="../../extjs/ext-all.js"></script>
    
    
    <link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
    <style type="text/css">
      .amap-marker .marker-route {
            position: absolute;
            width: 96px;
            height: 49px;
            color: #0079ff;
            background: url(map_icon.png) no-repeat;
            cursor: pointer;
            font-size:16px;
            font-weight:bold;
            line-height:24px;     
            text-algin:center;  
            margin:0 auto;   
            font: 'Microsoft Yahei';  
                   }
        .title {
            font: 13px 'Microsoft Yahei';
            color: #09f;
            
        }
        .amap-info-content {
            font-size: 12px;
        }
    </style>
    <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=d47341e527d8eb59f83fe10bb2089633"></script>
    <script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>
    
	<script type="text/javascript" src="truckuser.js"></script>
</head>
<body>
 <div id="btsAlarm-grid"></div>
</body>

</html>						
