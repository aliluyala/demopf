<%@ page language="java" import="java.util.*" pageEncoding="utf8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
	<title></title>
	<link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
	<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=d47341e527d8eb59f83fe10bb2089633&plugin=AMap.Autocomplete"></script>
    <script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>

<body>
 <div id="container"></div>
<div id="myPageTop">
    <table>
        <tr>
            <td>
                <label>按关键字搜索：</label>
            </td>
            <td class="column2">
                <label>左击获取经纬度：</label>
            </td>
        </tr>
        <tr>
            <td>
                <input type="text" placeholder="请输入关键字进行搜索" id="tipinput">
            </td>
            <td class="column2">
                <input type="text" readonly="true" id="lnglat">
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript">
    var map = new AMap.Map("container", {
        resizeEnable: true,
        center: [116.397428, 39.90923],//地图中心点
        zoom: 13 //地图显示的缩放级别
    });
    //为地图注册click事件获取鼠标点击出的经纬度坐标
    var clickEventListener = map.on('click', function(e) {
    	map.clearMap();
        document.getElementById("lnglat").value = e.lnglat.getLng() + ',' + e.lnglat.getLat();
        var a = [e.lnglat.getLng(), e.lnglat.getLat()];
        var marker = new AMap.Marker({
            icon: "http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",
            position: a
        });
        marker.setMap(map);
        
    });
    var auto = new AMap.Autocomplete({
        input: "tipinput"
    });
    AMap.event.addListener(auto, "select", select);//注册监听，当选中某条记录时会触发
    function select(e) {
        if (e.poi && e.poi.location) {
            map.setZoom(15);
            map.setCenter(e.poi.location);
        }
    }
</script>

</body>
</html>