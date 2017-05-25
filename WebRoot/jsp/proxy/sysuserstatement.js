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
	var proxyParas={};
	
	Ext.define("DeptModel", {
	    extend: "Ext.data.TreeModel",
	    fields: [
	        "id","url","name","status"
	    ]
	});
	
	
	
	var treestore = Ext.create('Ext.data.TreeStore', {
		model:'DeptModel',
	    root: { expanded: true,name:'用户权限' },//
	    autoLoad:false,
	    proxy: {
	        type: 'ajax',
	        url: '../../sys/user/listPermission.action',
	        reader: {
	            type: 'json',
	            successProperty: 'success'
	        }
	    },
	    listeners: {
        	beforeload: function (proxy, params) {
               	this.proxy.extraParams = paras;
            }
         }
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
	
	var roleStore = Ext.create('Ext.data.Store', {
	    model:'roleModel',
	    autoLoad:true,
		proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/user/listRoles.action',
            reader: {
                root: 'root'
            }
        }
	});
	
	
	var proxyStore = Ext.create('Ext.data.Store', {
	    model:'proxyModel',
	    autoLoad:true,
		proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/user/listProxy.action',
            reader: {
                root: 'root'
            }
        },
        listeners: {
        	beforeload: function (proxy, params) {
            }
         }
	});
	
	
			
    Ext.define('dataModel', {
        extend: 'Ext.data.Model',
        fields: ['id','orderNo','pay','isTurnout','createTime','balance','remark',"mobile","name"
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
            url: '../../sys/user/userPayListByPage.action',
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
            	paras.name=Ext.getCmp('titleSearch').getValue();
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
			    },'-','->',
			    {width:180,labelWidth:50,name:'titleSearch',id:'titleSearch',xtype:'textfield',fieldLabel:'用户名'},
		    	queryButton
	    ]
	});
	
	var record_start = 0;
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '用户列表',
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
            {text: '是否支出',
                dataIndex: 'isTurnout',
                flex: 1,
                sortable: true,
                renderer: function (value, meta, record) {
            		if(value==false){
            			return "收入";
            		}else if(value==true){
            			return "支出";  
            		}
            	}
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
            {text: '用户名称',
                dataIndex: 'name',
                flex: 1,
                sortable: true
            },{text: '用户手机',
                dataIndex: 'mobile',
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
  
});