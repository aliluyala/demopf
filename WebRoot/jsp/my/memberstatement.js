Ext.Loader.setConfig({enabled: true});

Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.util.*',
    'Ext.toolbar.Paging',
    'Ext.ModelManager'
]);


Ext.onReady(function(){
	var paras={};
	
	Ext.define("DeptModel", {
	    extend: "Ext.data.TreeModel",
	    fields: [
	        "id","url","name","status"
	    ]
	});
	
	
	
	Ext.define('roleModel', {
        extend: 'Ext.data.Model',
        
        fields: ['id','roleName'
        ],
        idProperty: 'id'
    });
	
	Ext.define('proxyModel', {
        extend: 'Ext.data.Model',
        fields: ['id','proxyName'
        ],
        idProperty: 'id'
    });
	
	
			
    Ext.define('dataModel', {
        extend: 'Ext.data.Model',
        fields: ['id','orderNo','pay','isTurnout','createTime','balance','remark'
        ],
        idProperty: 'id'
    });
    // create the Data Store
	
	var pageSize = 40;
    var store = Ext.create('Ext.data.Store', {
        pageSize: pageSize,
        model: 'dataModel',
        remoteSort: false,
        autoLoad:true,
        proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/user/userAccountBook.action',
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
            	{
			        iconCls:'refush',
			        text:'刷新',
			        xtype: 'button',
			        handler: function () {
			    		store.load();
			        }
			    },{
			        iconCls:'refush',
			        text:'充值',
			        xtype: 'button',
			        handler: function () {
			        	var h = Ext.getBody().getHeight();
						window2.setHeight(h);
			        	window2.show();
			        }
			    }
	    ]
	});
	
	var record_start = 0;
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '收支明细',
        columnLines:true,
        store: store,
        loadMask:true,
        disableSelection: false,
        tbar:[tbar],
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
    	selModel: {
            injectCheckbox: 0,
            mode: "SIMPLE",     //"SINGLE"/"SIMPLE"/"MULTI"
            checkOnly: true     //只能通过checkbox选择
        },
        selType: "checkboxmodel",
        columns:[
			new Ext.grid.RowNumberer({   header : "序号",   width : 40,
				renderer: function (value, metadata, record, rowIndex) {
					return record_start + 1 + rowIndex;
				} 
			}),
        	{text: '订单号',
            dataIndex: 'orderNo',
            flex: 2,
            sortable: true
        	},
        	{text: '支付数额',
                dataIndex: 'pay',
                flex: 1,
                sortable: true
            },
            {text: '时间',
                dataIndex: 'createTime',
                flex: 2,
                sortable: true
            },
            {text: '余额',
                dataIndex: 'balance',
                flex: 1,
                sortable: true
            },
            {text: '说明',
                dataIndex: 'remark',
                flex: 10,
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
  
  
  var location = (window.location+'').split('/'); 
	var basePath = location[0]+'//'+location[2]+'/'+location[3];
	basePath = basePath+"/jsp/llpay/plainPay.jsp";
  var window2 = new Ext.Window({  
      title:'用户充值',  
      width:1000,  
      height:600,  
      closeAction:'hide',  
      plain:true,  
      layout:'fit',
      autoScroll:false,
      monitorValid:true,
      html : '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+basePath+'"></iframe>',
      listeners: {
	  		close: function() {
	  			Ext.getBody().unmask();
	         }
			  ,
			  show: function() {
			 	 Ext.getBody().mask();
			  }
	   }
  });  
});