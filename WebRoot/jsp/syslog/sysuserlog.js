Ext.Loader.setConfig({enabled: true});

Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.util.*',
    'Ext.toolbar.Paging',
    'Ext.ModelManager'
]);


Ext.onReady(function(){
    Ext.define('travelModel', {
        extend: 'Ext.data.Model',
        fields: ['userId','realName','userName','createTime','userIp','logLevel','message','logger'
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
            url: '../../sys/user/sysLogListByPage.action',
            reader: {
                root: 'root',
                totalProperty: 'totalCount'
            }
        },
        listeners: {
            load: function () {
               Ext.getCmp('query').setDisabled(false);
               if(this.getCount() == 0)
               {
            	   Ext.Msg.alert("提示","<font size='2' color='red'>暂无数据!</font>");
            	}
            },
        	beforeload: function (proxy, params) {
            	paras.currentPage=store.currentPage;
            	paras.pageSize = store.pageSize;
            	Ext.getCmp('query').setDisabled(true);
               	this.proxy.extraParams = paras;
            }
         }
    });
				
    var queryButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'query', iconCls:'zoom',text: '查 询',
    	handler: function() {
    		paras.search = encodeURIComponent(Ext.getCmp("titleSearch").getValue());
    		store.load();
		}
    });
    
    var TS7store = Ext.create('Ext.data.Store', {
		 fields: ['code', 'name'],
		 data : [
		 {"code":"effective","name":"有效"},
		 {"code":"ineffective","name":"无效"}
		 ]
		});		
    
	var bbar = Ext.create('Ext.PagingToolbar', {
            store: store,
            displayInfo: true,
            columnWidth: 6,
            beforePageText:'页',
            inputItemWidth:50,
            afterPageText:'共{0}页',
            displayMsg: '显示的数据 {0} - {1} 共 {2}',
            emptyMsg: '没有要显示的数据'
    });
	
	var tbar = Ext.create("Ext.Toolbar", {
		flex:1,
	    items: [
            	'-',{
			        iconCls:'refush',
			        text:'刷新',
			        xtype: 'button',
			        handler: function () {
			    		store.load();
			        }
			    },'-','->',
			    {width:180,name:'titleSearch',id:'titleSearch',xtype:'textfield',fieldLabel:'用户名'},
		    	 queryButton
	    ]
	});
	
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '用户列表',
        columnLines:true,
        store: store,
        //paramNames: { createtime:  },
        loadMask:true,
        disableSelection: false,
        tbar:[tbar],
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
        //  ['userId','realName','userName','createTime','userIp','logLevel','message','logger'
        columns:[
        	{text: '用户名',
            dataIndex: 'realName',
            flex: 1,
            sortable: true
        	},
        	{text: '登录名',
                dataIndex: 'userName',
                flex: 1,
                sortable: true
            }
        	,
        	{text: 'ip',
                dataIndex: 'userIp',
                flex: 1,
                sortable: true
            }
        	,
        	{text: '日志级别',
                dataIndex: 'logLevel',
                flex: 1,
                sortable: true
            }
        	,
        	{text: '操作时间',
                dataIndex: 'createTime',
                flex: 1,
                sortable: true
            }
        	,
        	{text: '信息内容',
                dataIndex: 'message',
                flex: 4,
                sortable: true
            }
        	],
        forceFit: true,
        // paging bar on the bottom
        bbar: bbar
    });
  Ext.create('Ext.Viewport',{
		layout: 'fit',items:[grid],renderTo: 'btsAlarm-grid'
	});
  
  
});