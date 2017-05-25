Ext.Loader.setConfig({enabled: true});		//开启动态加载
Ext.application({
	name:'datacenter',			
	autoCreateViewport: true,
	appFolder:'app',	//指定根目录
	controllers:[		
		'Menu'
	],
	launch: function() {
	}
});
