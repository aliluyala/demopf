Ext.Loader.setConfig({enabled: true});

Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.util.*',
    'Ext.toolbar.Paging',
    'Ext.ModelManager'
]);


Ext.onReady(function(){
	Ext.tip.QuickTipManager.init();
	
    Ext.define('travelModel', {
        extend: 'Ext.data.Model',
        fields: ['orderNo','price','userType','status','created_date'
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
            url: '../../sys/proxy/idCardqueryOrderByPage.action',
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
    		paras.orderNo = encodeURIComponent(Ext.getCmp("orderNo").getValue());
    		paras.status = Ext.getCmp("status").getValue();
    		//paras.date = Ext.getCmp("datetime").getValue();
    		paras.startTime  = Ext.util.Format.date(Ext.getCmp("startTime").getValue(), 'Y-m-d');
    		paras.endTime  = Ext.util.Format.date(Ext.getCmp("endTime").getValue(), 'Y-m-d');
    		store.load();
		}
    });
    
    
	var bbar = Ext.create('Ext.PagingToolbar', {
            store: store,
            displayInfo: true,
            columnWidth: 6,
            pageSize:25,
            id:'bbar',
            beforePageText:'页',
            inputItemWidth:50,
            afterPageText:'共{0}页',
            displayMsg: '显示的数据 {0} - {1} 共 {2}',
            emptyMsg: '没有要显示的数据'
    });
	Ext.define('appTypeModel', {
	     extend: 'Ext.data.Model',
	     fields: ['id','appNameZh']
	});
	
	
	var checkStatusStore = Ext.create('Ext.data.Store', {
		fields: ['code', 'name'],
		 data : [
		 {"code":"1","name":"等待付款"},
		 {"code":"2","name":"完成交易"},
		 {"code":"3","name":"审核中"},
		 {"code":"4","name":"拒保"}
		 ]
	});
	var tbar = Ext.create("Ext.Toolbar", {
		flex:1,
	    items: ['-',
	            {
			        iconCls:'refush',
			        text:'刷新',
			        xtype: 'button',
			        handler: function () {
			    		store.load();
			        }
	            },
	            '-','->',
				{width:210,forceSelection: true,xtype: 'combobox',fieldLabel:'审核状态:',labelWidth: 65,anchor: '100%',
					store: checkStatusStore,emptyText :'请选择...',queryMode: 'local',displayField: 'name',valueField: 'code',
					id:'status',name:'status'},'-',
					{width:180,labelWidth:50,name:'orderNo',id:'orderNo',xtype:'textfield',fieldLabel:'订单号'},'-',
					{
				        xtype: 'datefield',
				        id:'startTime',
				        labelWidth: 65,
				        fieldLabel: '开始时间',
				        name: 'startTime',
				        format: 'Y-m-d'
				    },
				    {
				        xtype: 'datefield',
				        id:'endTime',
				        labelWidth: 65,
				        fieldLabel: '结束时间',
				        name: 'endTime',
				        format: 'Y-m-d'
				    },
					queryButton
	    ]
	});
	
    //var pluginExpanded = true;
	//fields: ['driverName','mobile','plateNumber','idCardNumber','idCardPic','drivingLicenseNumber','drivingLicensePic','registrationNumber','registrationPic',
      //       'registrationPic','lockedDate','loginDate','loginIp','isCheck','truckType','deadWeight','loadStatus'
	var record_start = 0;
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        id:'grid',
        autoExpandColumn: 'name',
        title: '保险列表',
        columnLines:true,
        store: store,
        //paramNames: { createtime:  },
        loadMask:true,
        disableSelection: false,
        tbar:[tbar],
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
        // fields: ['orderNo','policyNo','ratio','price','recognizeePhone','userType','goodsName','insuredAmount','fromLoc','toLoc','company','status','created_date'
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
        	{text: '价格',
                dataIndex: 'price',
                flex: 1,
                sortable: true
            },
            {text: '用户类型',
            	dataIndex: 'userType',
            	flex: 1,
            	sortable: true
            },{text: '生成时间',
            	dataIndex: 'created_date',
            	flex: 2,
            	sortable: true
            },{text: '状态',
            	dataIndex: 'status',
            	flex: 1,
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