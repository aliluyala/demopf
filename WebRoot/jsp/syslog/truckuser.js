Ext.Loader.setConfig({enabled: true});
Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.util.*',
    'Ext.toolbar.Paging',
    'Ext.ModelManager'
]);

Ext.onReady(function(){
	
    
	Ext.create('Ext.Viewport',{
		layout: 'vbox',
		items:[{
	        xtype: 'panel',
	        title: '车主分布图',
	        html:'<div id="container"></div>',
	        width: '100%',
	        flex: 10
	    }/*,
	    {
	        xtype: 'panel',
	        title: 'Inner Panel Three',
	        width: '100%',
	        flex: 1
	    }*/],
        renderTo: 'btsAlarm-grid'
	});
	
	var flag = 0;
	var map = new AMap.Map("container", {
		resizeEnable: true,
		center:[116.368904,30.923423],
		zoom:8
		});
	map.on('zoomchange', function() {
		if(map.getZoom()<=6&&flag==1){
			flag = 0;
			store2.load();
		}
		if(map.getZoom()>=7&&flag==0){
			flag = 1;
			store.load();
		}
		//paras.radius = map.getBounds().
		//store.load();
    });
	/* map.on('mapmove', function() {
	     document.getElementById('tip').innerHTML = "地图图块加载完毕！当前地图中心点为：" + map.getCenter();
	 });*/
	Ext.define('travelModel', {
        extend: 'Ext.data.Model',
        fields: ['ll','ln','countt','province'
        ],
        idProperty: 'id'
    });
    // create the Data Store
	var paras={};
	var pageSize = 40;
    var store = Ext.create('Ext.data.Store', {
        pageSize: pageSize,
        model: 'travelModel',
        remoteSort: false,
        autoLoad:true,
        proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/statistics/getAllTruck.action',
            reader: {
                root: 'root',
                totalProperty: 'totalCount'
            }
        },
        listeners: {
            load: function () {
            	map.clearMap();
            	for (var i = 0, marker; i < store.getCount(); i++) {
            		var ss = store.getAt(i).data;
            		var a = [ss.ll, ss.ln];
                    var marker = new AMap.Marker({
                        position: a,
                        content: '<div style="width:34px; height:22px; background-image:url(car_icon-.png);"></div>',
                        map: map
                    });
                }
            	 //map.setFitView();
            },
        	beforeload: function (proxy, params) {
        		paras.longitude = map.getCenter().getLng();
        		paras.latitude = map.getCenter().getLat();
        		this.proxy.extraParams = paras;
            }
         }
    });
    
    var store2 = Ext.create('Ext.data.Store', {
        pageSize: pageSize,
        model: 'travelModel',
        remoteSort: false,
        autoLoad:false,
        proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/statistics/getAllProvinceTruck.action',
            reader: {
                root: 'root',
                totalProperty: 'totalCount'
            }
        },
        listeners: {
            load: function () {
            	map.clearMap();
            	for (var i = 0, marker; i < store2.getCount(); i++) {
            		var ss = store2.getAt(i).data;
            		var a = [ss.ll, ss.ln];
                    var marker = new AMap.Marker({
                        position: a,
                        content: '<div class="amap-marker marker-route" style="width:96px; height:49px; text-align: center; line-height:40px;   background-image:url(map_icon.png);">'+ss.province+' · '+ss.countt+'</div>',
                        map: map
                    });
                }
            	 //map.setFitView();
            },
        	beforeload: function (proxy, params) {
        		paras.longitude = map.getCenter().getLng();
        		paras.latitude = map.getCenter().getLat();
        		this.proxy.extraParams = paras;
            }
         }
    });
	//初始化地图对象，加载地图
	/*var map = new AMap.Map("container", {resizeEnable: true});
    var lnglats = [
        [116.368904, 39.923423],
        [116.382122, 39.921176],
        [116.387271, 39.922501],
        [116.398258, 39.914600]
    ];
    var infoWindow = new AMap.InfoWindow({offset: new AMap.Pixel(0, -30)});
    for (var i = 0, marker; i < lnglats.length; i++) {
        var marker = new AMap.Marker({
            position: lnglats[i],
            map: map
        });
        marker.content = '我是第' + (i + 1) + '个Marker';
        marker.on('click', markerClick);
        marker.emit('click', {target: marker});
    }
    function markerClick(e) {
        infoWindow.setContent(e.target.content);
        infoWindow.open(map, e.target.getPosition());
    }
    map.setFitView();*/
   
});