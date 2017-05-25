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
	<script type="text/javascript" src="../../kindeditor/kindeditor.js"></script> 
	<link rel="stylesheet" type="text/css" href="http://developer.amap.com/Public/css/demo.Default.css" /> 
	<script language="javascript" src="http://webapi.amap.com/maps?v=1.3&key=d47341e527d8eb59f83fe10bb2089633"></script>
	
	<script type="text/javascript" src="../../pluin/datetime/UX_TimePickerField.js"></script>

	<script type="text/javascript" src="../../pluin/datetime/UX_DateTimePicker.js"></script>
	
	<script type="text/javascript" src="../../pluin/datetime/UX_DateTimeMenu.js"></script>
	
	<script type="text/javascript" src="../../pluin/datetime/UX_DateTimeField.js"></script>
<script language="javascript">
Ext.Loader.setConfig({enabled: true});
Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.util.*',
    'Ext.toolbar.Paging',
    'Ext.ModelManager'
]);

Ext.onReady(function(){

	
	Ext.define('DataModel', {
	     extend: 'Ext.data.Model',
	     fields: ['id','name']
	});
    
    var userStore = Ext.create('Ext.data.Store', {
	    model:'DataModel',
	    autoLoad:true,
		proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../userManager/userList2.action',
            reader: {
                root: 'root'
            }
        }
	});
	
		var windowzb = new Ext.Window({  
	      title:'新建',  
	      width:500,  
	      height:250,  
	      closeAction:'hide',  
	      plain:true,  
	      layout:'fit',  
	      monitorValid:true,
	      items:[
	             Ext.create('Ext.form.Panel', {
	            	 id:'dictionaryform',
	            	 layout:'form',  
	                 baseCls:'x-plain',  
	                 style:'padding:8px;',  
	                 labelWidth:30, 
			         items:[
	         			{
			              xtype : "hiddenfield",
			              id:'travel_no',
			              name:'travel_no'
			             },
						{
				              xtype: 'filefield',
				              id:'file',
				              name:'file',
				              blankText:'文件上传必需要',
				              width: 220,
				              fieldLabel: '应用文件',
				              hideLabel: false,
				              buttonText: '选择文件'
				          },
				          {	  
				        	  forceSelection: true,
				        	  xtype: 'combobox',
				        	  fieldLabel:'用户名称',
				        	  anchor: '100%',
				  			  editable:true,
				  			  store: userStore,
				  			  queryMode: 'local',
				  	    	  displayField: 'name',
				  	    	  valueField: 'id',
				  	    	  id:'userId',   
				  	    	  name:'userId'
				          },
				          {  
				        	  id:'address',
				        	  name:'address',
				        	  allowBlank:false,
				              xtype:'textfield',  
				              fieldLabel:'直播地址',  
				              anchor    : '100%'
				          },{
						        xtype: 'datetimefield',
						        id:'datetime',
						        anchor: '100%',
						        fieldLabel: '时间',
						        name: 'datetime',
						        format: 'Y-m-d',
						        value: new Date()
						    },
				          {
				              xtype     : 'textareafield',
				              grow      : true,
				              name      : 'travelDesc',
				              fieldLabel: '直播内容',
				              anchor    : '100%'
				          }
			          ],
			           buttonAlign:'center',
			           buttons:[{  
			          	  text:'确定',
			          	  formBind: true,
			        	  handler: function() {
					          var form = Ext.getCmp('dictionaryform');
				              form.submit({
				                  url: '../../token/fileUpload.action',
				                  waitMsg: '上传中，请稍等...',
			          			  method: "POST",
			                      success: function (form, action) {
				            	  	windowzb.close();
			                      },
			                      failure: function (form, action) { 
			                        Ext.Msg.alert('错误提示', "上传失败！");
			                        windowzb.close();
			                      }
				              });
			          	}
			      },{  
			          	  text:'取消',
			        	  handler: function() {
							windowzb.close();
			    		}
			      }]
	             })
	      ]  
	  }); 
	  
	 var searchstore = Ext.create('Ext.data.Store', {
									     fields:['code','info'],
									     data : [
									     ]
									 });
	 Ext.create('Ext.form.Panel',{
			id:'travelform',layout: 'form',items:[
							{	  
				        	  forceSelection: true,
				        	  labelWidth:30,
				        	  xtype: 'combobox',
				        	  fieldLabel:'搜索',
				  			  editable:true,
				  			  store: searchstore,
				  			  queryMode: 'local',
				  	    	  displayField: 'info',
				  	    	  valueField: 'code',
				  	    	  id:'search',
				  	    	  name:'search',
				  	    	   enableKeyEvents:true,
				  	    	  listeners:{  
								  select : function(combo, record,index){
								  	searchaddress(combo.getValue(),combo.findRecordByValue(combo.getValue()).get("info"))
								  },
								  keydown : function(combo, record,index){
										if(record.keyCode==13){
											alert(combo.getValue());
											searchaddress("",combo.getValue());
										}else{
											autoSearch();
										}
								  }
							  }
				          },
							{
					              html:'<div id="iCenter"></div>'
					         },
					         { // 行1
						        layout : "column", // 从左往右的布局
						        boder:false, 
						        items : [
						        {
					              xtype : "hiddenfield",
					              id:'qjtravel_no',
					              name:'travel_no'
					             },
					        	{
					              xtype : "hiddenfield",
					              id:'totalpoints',
					              name:'totalpoints'
					             },
					            {
						           columnWidth : .2, // 该列有整行中所占百分比
						           layout : "form", // 从上往下的布局
						           items : [ {
								              xtype: 'filefield',
								              id:'travelfile',
								              name:'travelfile',
								               labelWidth:30,
								              fieldLabel: '截图',
								              hideLabel: false,
								              buttonText: '选择文件'
								          }]
						          },
						        {
						           columnWidth : .1, // 该列有整行中所占百分比
						           layout : "form", // 从上往下的布局
						           items : [{
						              xtype : "textfield",
						              fieldLabel : "标题",
						              labelWidth:30,
						              id:'title',
						              name:'title'
						             }]
						          },{
						           columnWidth : .1, // 该列有整行中所占百分比
						           layout : "form", // 从上往下的布局
						           items : [{
						              xtype : "textfield",
						              fieldLabel : "里程",
						              id:'totalm',
						              name:'mileage',
						              labelWidth:30
						             }]
						          }, {
						           columnWidth : .1,
						           layout : "form",
						           items : [{
						              xtype : "textfield",
						              fieldLabel : "均速",
						              labelWidth:30,
						              id:'speedavg',
						              name:'averageSpeed'
						             }]
						          }, {
						           columnWidth : .1,
						           layout : "form",
						           items : [{	  
							        	  forceSelection: true,
							        	  labelWidth:30,
							        	  xtype: 'combobox',
							        	  fieldLabel:'用户',
							  			  editable:true,
							  			  store: userStore,
							  			  queryMode: 'local',
							  	    	  displayField: 'name',
							  	    	  valueField: 'id',
							  	    	  id:'userIdtravel',
							  	    	  name:'userId'
							          }]
						          }, 
						          {
						           columnWidth : .1,
						           layout : "form",
						           items : [{
									        xtype: 'datetimefield',
									        id:'datetimetravel',
									        labelWidth:30,
									        fieldLabel: '时间',
									        name: 'createTime',
									        format: 'Y-m-d',
									        value: new Date()
									    }]
						          },{
						           columnWidth : .1,
						           layout : "form",
						           items : [{
									            xtype: 'button',
									            text : '骑行导航',
									             handler : function() {
									             	driving_route();
									             }
								        	}]
						          },{
						           columnWidth : .1,
						           layout : "form",
						           items : [{
									            xtype: 'button',
									            text : '确定提交',
									             handler : function() {
					      	    					  var form = Ext.getCmp('travelform');
										              form.submit({
										                  url: '../../fans/save.action',
										                  waitMsg: '上传中，请稍等...',
									          			  method: "POST",
									                      success: function (form, action) {
										            	  	//windowzb.close();
									                      },
									                      failure: function (form, action) { 
									                        Ext.Msg.alert('错误提示', "上传失败！");
									                       // windowzb.close();
									                      }
										              });
									             }
								        	}]
						          },{
						           columnWidth : .1,
						           layout : "form",
						           items : [{
									            xtype: 'button',
									            text : '重新设置',
									             handler : function() {
									             	var form = Ext.getCmp('travelform');
									             	form.getForm().reset();
									             	pointArray = [];
									             	start_xy="";
													end_xy="";
													totalm = 0;
													totalpoints = "";
									             	mapObj.clearMap();
									             }
								        	}]
						          }
						          ]
						      }
			],renderTo: 'btsAlarm-grid'
	});
	
	
	var mapObj,mouseTool,editorTool,toolBar,contextMenu;
	var route_text, steps;
	var polyline;
	
	
	//起、终点
	var start_xy;
	var end_xy;
	
	var pointArray = [];
	//初始化地图对象，加载地图
	function mapInit(){
		//mapObj = new AMap.Map("iCenter");
		mapObj = new AMap.Map("iCenter",{
			//二维地图显示视口
			view: new AMap.View2D({
				center:new AMap.LngLat(108.946609,34.262324),//地图中心点
				zoom:4 //地图显示的缩放级别
			})
		});
		
		mapObj.plugin(["AMap.ToolBar"],function(){		
			toolBar = new AMap.ToolBar(); 
			mapObj.addControl(toolBar);		
		});
		//创建右键菜单
		contextMenu = new AMap.ContextMenu();
		//右键显示全国范围
		contextMenu.addItem("缩放至全国范围",function(e){
			mapObj.setZoomAndCenter(4,new AMap.LngLat(108.946609,34.262324));
		},2);
		//右键添加Marker标记
		contextMenu.addItem("标记起点",function(e){
			  //起点、终点图标
			var sicon = new AMap.Icon({
				image: "http://api.amap.com/Public/images/js/poi.png",
				size:new AMap.Size(44,44),
				imageOffset: new AMap.Pixel(-334, -180)
			});
			var startmarker = new AMap.Marker({
				icon : sicon, //复杂图标
				visible : true, 
				position : contextMenuPositon,
				map:mapObj,
				offset : {
					x : -16,
					y : -40
				}
			});
			start_xy =  contextMenuPositon;
		},4);
		
		//右键添加Marker标记
		contextMenu.addItem("标记终点",function(e){
			var eicon = new AMap.Icon({
			image: "http://api.amap.com/Public/images/js/poi.png",
			size:new AMap.Size(44,44),
			imageOffset: new AMap.Pixel(-334, -134)
			});
			var endmarker = new AMap.Marker({
				icon : eicon, //复杂图标
				visible : true, 
				position : contextMenuPositon,
				map:mapObj,
				offset : {
					x : -16,
					y : -40
				}
			});
			end_xy =  contextMenuPositon;
		},5);
		
		//右键添加Marker标记
		contextMenu.addItem("途经点",function(e){
			var marker = new AMap.Marker({
			map:mapObj,
			position:contextMenuPositon, //基点位置
			icon:"http://webapi.amap.com/images/marker_sprite.png", //marker图标，直接传递地址url
			offset:{x:-8,y:-34} //相对于基点的位置
			});
			pointArray.push(contextMenuPositon);
		},6);
		
		contextMenu.addItem("发直播",function(){
			var lnglatXY = contextMenuPositon;
			var MGeocoder;
		    //加载地理编码插件
		    mapObj.plugin(["AMap.Geocoder"], function() {        
		        MGeocoder = new AMap.Geocoder({ 
		            radius: 1000,
		            extensions: "all"
		        });
		        //返回地理编码结果 
		        AMap.event.addListener(MGeocoder, "complete", sendblog); 
		        //逆地理编码
		        MGeocoder.getAddress(lnglatXY); 
		    });
		},7);
		
		
		//地图绑定鼠标右击事件——弹出右键菜单
		AMap.event.addListener(mapObj,'rightclick',function(e){
			contextMenu.open(mapObj,e.lnglat);
			contextMenuPositon = e.lnglat;
		});
	}
		
			//回调函数
	function sendblog(data) {
		var address;
	    address = data.regeocode.formattedAddress;
	    Ext.getCmp("address").setValue(address);
	    Ext.getCmp("travel_no").setValue(Ext.getCmp("qjtravel_no").getValue());
	    windowzb.show();
	}  
	
	
	//驾车导航
	function driving_route() {
		var MDrive;
		if(pointArray.length<=0){
			mapObj.plugin(["AMap.Driving"], function() {
				var DrivingOption = {
					//驾车策略，包括 LEAST_TIME，LEAST_FEE, LEAST_DISTANCE,REAL_TRAFFIC
					policy: AMap.DrivingPolicy.LEAST_FEE 
				};        
		        MDrive = new AMap.Driving(DrivingOption); //构造驾车导航类 
		        AMap.event.addListener(MDrive, "complete", driving_routeCallBack); //返回导航查询结果
		        MDrive.search(start_xy, end_xy); //根据起终点坐标规划驾车路线
	    	});
		}else{
			pointArray.push(end_xy);
			for(var v=0;v<pointArray.length;v++){
				if(v==0){
					end_xy = pointArray[v];
				}else{
					start_xy = end_xy;
					end_xy = pointArray[v];
				}
				mapObj.plugin(["AMap.Driving"], function() {
					var DrivingOption = {
						//驾车策略，包括 LEAST_TIME，LEAST_FEE, LEAST_DISTANCE,REAL_TRAFFIC
						policy: AMap.DrivingPolicy.LEAST_FEE 
					};        
			        MDrive = new AMap.Driving(DrivingOption); //构造驾车导航类 
			        AMap.event.addListener(MDrive, "complete", driving_routeCallBack); //返回导航查询结果
			        MDrive.search(start_xy, end_xy); //根据起终点坐标规划驾车路线
	    		});
			}
		}
	}
		
	var totalm = 0;
	var totalpoints = "";
	function driving_routeCallBack(data) {
			var routeS = data.routes;
			if (routeS.length>0) { 
			 	for(var v =0; v< routeS.length;v++){
			 		//步行导航路段数
					steps = routeS[v].steps;
				}
				drivingDrawLine();
			}
		}
	//绘制步行导航路线
	function drivingDrawLine() {
		var drawpath = new Array(); 
		for(var s=0; s<steps.length; s++) {
			var plength = steps[s].path.length;
			for (var p=0; p<plength; p++) {
				drawpath.push(steps[s].path[p]);
			}
		}
		var polyline = new AMap.Polyline({
				map:mapObj,
				path:drawpath,
				strokeColor:"#FF33FF",//线颜色
				strokeOpacity:1,//线透明度
				strokeWeight:3,//线宽
				strokeStyle:"solid"//线样式				
			}); 
		totalm=totalm+polyline.getLength();
		Ext.getCmp("totalm").setValue(totalm);
		if(totalpoints=="")	 
			totalpoints = totalpoints+polyline.getPath();
		else
			totalpoints = totalpoints+","+polyline.getPath();
		Ext.getCmp("totalpoints").setValue(totalpoints);
		
		//设置骑迹编号
		var timestamp=new Date().getTime();
		Ext.getCmp("qjtravel_no").setValue(timestamp+MathRand());
		mapObj.setFitView();
	}	
	
	mapInit();
	
	

	//输入提示
	function autoSearch() {
	    var keywords = Ext.getCmp("search").getValue();
	    var auto;
	    //加载输入提示插件
	        mapObj.plugin(["AMap.Autocomplete"], function() {
	        var autoOptions = {
	            city: "" //城市，默认全国
	        };
	        auto = new AMap.Autocomplete(autoOptions);
	        //查询成功时返回查询结果
	        if ( keywords.length > 0) {
	            AMap.event.addListener(auto,"complete",autocomplete_CallBack);
	            auto.search(keywords);
	        }
	        else {
	            document.getElementById("result1").style.display = "none";
	        }
	    });
	}

	
		//输出输入提示结果的回调函数
	function autocomplete_CallBack(data) {
	    var resultStr = "";
	    var tipArr = data.tips;
	    if (tipArr&&tipArr.length>0) {
	    	var addrs = [];
	        for (var i = 0; i < tipArr.length; i++) {
	        	    var addr={};
	        	    addr.code=tipArr[i].adcode;
            		addr.info = tipArr[i].name+"_"+tipArr[i].district;
            		addrs.push(addr);
	        }
	        searchstore1 = Ext.create('Ext.data.Store', {
									     fields:['code','info'],
									     data : [
									     ]
									 });
	        searchstore1.add(addrs);
	        Ext.getCmp("search").bindStore(searchstore1);
	    }
	}

	function searchaddress(adcode,keywords){
		  //var keywords = Ext.getCmp("search").getValue();
     	  var text = keywords.replace(/<[^>].*?>.*<\/[^>].*?>/g,"");
	      mapObj.plugin(["AMap.PlaceSearch"], function() {       
		        var msearch = new AMap.PlaceSearch();  //构造地点查询类
		        AMap.event.addListener(msearch, "complete", placeSearch_CallBack); //查询成功时的回调函数
				msearch.setCity(adcode);
		        msearch.search(text);  //关键字查询查询
	    });
	}
	
	function placeSearch_CallBack(data) {
	    //清空地图上的InfoWindow和Marker
	    windowsArr = [];
	    marker     = [];
	    //mapObj.clearMap();
	    var resultStr1 = "";
	    var poiArr = data.poiList.pois;
	    var resultCount = poiArr.length;
	    for (var i = 0; i < resultCount; i++) {
	         addmarker(i, poiArr[i]);
	    }
	    mapObj.setFitView();
	}
	
	//添加查询结果的marker&infowindow   
	function addmarker(i, d) {
	    var lngX = d.location.getLng();
	    var latY = d.location.getLat();
	    var markerOption = {
	        map:mapObj,
	        icon:"http://webapi.amap.com/images/" + (i + 1) + ".png",
	        position:new AMap.LngLat(lngX, latY)
	    };
	    var mar = new AMap.Marker(markerOption);         
	    marker.push(new AMap.LngLat(lngX, latY));
	}

	function MathRand() 
	{ 
		var Num=""; 
		for(var i=0;i<6;i++) 
		{ 
			Num+=Math.floor(Math.random()*10); 
		} 
		return Num;
	} 
	});
</script>
</head>
<body>
 <div id="btsAlarm-grid"></div>
</body>

</html>						
