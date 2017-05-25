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
        fields: ['userId','orderNo','userType','pay','remark','point','date','costpay','proxyprice','isSuccess'],
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
            url: '../../sys/statistics/companyAccountBooks.action',
            reader: {
            	messageProperty:'message',
                root: 'root',
                totalProperty: 'totalCount'
            }
        },
        listeners: {
            load: function () {
               Ext.getCmp('query').setDisabled(false);
               var proxy  = store.getProxy(), reader = proxy.getReader(),raw  = reader.rawData;
               var  value=eval("("+reader.getMessage(raw)+")");
               Ext.getCmp('head-lb-1').setText('代理营业额：'+(value.totalpay1==null?'0':value.totalpay1));
               Ext.getCmp('head-lb-2').setText('代理成本：'+(value.totalpay3==null?'0':value.totalpay3));
               Ext.getCmp('head-lb-3').setText('代理利润：'+(value.pay12==null?'0':value.pay12));
               if(value.userType=='A0001'){
                   Ext.getCmp('toolbar').remove('head-lb-4');
                   Ext.getCmp('toolbar').remove('head-lb-5');
                   Ext.getCmp('toolbar').remove('balanceButtion');
                   Ext.getCmp('grid').down('#costpay').hide();
               }else{
            	   Ext.getCmp('head-lb-4').setText('公司成本：'+(value.totalpay2==null?'0':value.totalpay2));
                   Ext.getCmp('head-lb-5').setText('公司利润：'+(value.pay13==null?'0':value.pay13));
               }
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
    		//paras.checkStatus = Ext.getCmp("bsc").getValue();
    		//paras.date = Ext.getCmp("datetime").getValue();
    		paras.tradeType = Ext.getCmp("tradeType").getValue();
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
		 {"code":"2","name":"身份证查验"},
		 {"code":"3","name":"买保险"},
		 {"code":"4,5","name":"保证金"}
		 ]
	});
	var tbar = Ext.create("Ext.Toolbar", {
		flex:1,
		id:'toolbar',
	    items: ['-',
	            {
			        iconCls:'refush',
			        text:'刷新',
			        xtype: 'button',
			        handler: function () {
			    		store.load();
			        }
	            },'-',{
			        iconCls:'refush',
			        text:'结算',
			        id:'balanceButtion',
			        xtype: 'button',
			        handler: function () {
			        	var records = grid.getSelectionModel().getSelection();
			        	if(records==null||records.length<=0){
			        		Ext.MessageBox.alert("提示","请选择一项");
			        		return;
			        	}
			        	var ids = [];
			        	for(var i=0;i<records.length;i++){
			        		if(records[i].data.isSuccess){
			        			Ext.MessageBox.alert('提示',records[i].data.orderNo+"已经结算");
			        			return;
			        		}
			        		ids.push(records[i].data.id);
			        	}
    	            	Ext.Ajax.request({url:'../../sys/statistics/balanceCompanyStatements.action',  params : {tradeId:ids},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							store.load();
							Ext.MessageBox.alert('提示',"操作成功！");
						},
						failure: function(result,options){
							var responseObj =  Ext.decode(result.responseText);
							Ext.MessageBox.alert('提示',responseObj.appMsg);
						}});
			        }
	            },
	            '-',
	            {
	   			 xtype:'label',
	   			 id:'head-lb-1',
	   			 margin:'0 10 0 10',
	   			 cls:'welcome',
	   			 text:'代理营业额：0'
	   			 },
	   			'-',
	            {
	   			 xtype:'label',
	   			 id:'head-lb-2',
	   			 margin:'0 10 0 10',
	   			 cls:'welcome',
	   			 text:'代理成本：0'
	   			 },
	   			'-',
	   			{
	   			 xtype:'label',
	   			 id:'head-lb-4',
	   			 margin:'0 10 0 10',
	   			 cls:'welcome',
	   			 text:'公司成本：0'
	   			 },'-',
	            {
	   			 xtype:'label',
	   			 id:'head-lb-3',
	   			 margin:'0 10 0 10',
	   			 cls:'welcome',
	   			 text:'代理利润：0'
	   			 },'-',
	   			{
	   			 xtype:'label',
	   			 id:'head-lb-5',
	   			 margin:'0 10 0 10',
	   			 cls:'welcome',
	   			 text:'公司利润：0'
	   			 },'-',
	            '->',{width:180,labelWidth:65,name:'orderNo',id:'orderNo',xtype:'textfield',fieldLabel:'订单编号'},'-',
	            	{width:210,
	            	forceSelection: true,
	            	xtype: 'combobox',
	            	fieldLabel:'交易类型:',
	            	labelWidth: 65,
	            	anchor: '100%',
					store: checkStatusStore,
					emptyText :'请选择...',
					queryMode: 'local',
					displayField: 'name',
					valueField: 'code',
					id:'tradeType',
					name:'tradeType'},'-',
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
	// ['realName','mobile','idCard','isLocked','consignorType','loginDate','loginIp','cities','registrationPic',
    //'address','isAuth','fax','registerTime','isPerfect'
	var record_start = 0;
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        id:'grid',
        autoExpandColumn: 'name',
        title: '公司流水',
        columnLines:true,
        store: store,
        //paramNames: { createtime:  },
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
        // fields: ['cts1userId','cts1orderNo','cts1userType','cts1pay','cts1remark','cts1point','cts1date','cts2userId','cts2userType','cts2pay','cts2remark','cts2point','cts2date'],
    	
        columns:[
			new Ext.grid.RowNumberer({   header : "序号",   width : 40,
				renderer: function (value, metadata, record, rowIndex) {
					return record_start + 1 + rowIndex;
				} 
			})
			,
			{text: '订单编号',
            	dataIndex: 'orderNo',
            	flex: 2,
            	renderer:function(val){ 
                    return "<span style='color:green'>"+val+"</span>"; 
                },
            	sortable: true
            },
            {text: '收入来源用户类型',
            	dataIndex: 'userType',
            	flex: 1,
            	renderer:function(val){ 
                    return "<span style='color:green'>"+val+"</span>"; 
                },
            	sortable: true
            }
            ,
        	{text: '收入额度',
                dataIndex: 'pay',
                flex: 1,
                renderer:function(val){ 
                    return "<span style='color:green'>"+val+"</span>"; 
                },
                sortable: true
            },
        	{text: '收入说明',
            	dataIndex: 'remark',
            	flex: 2,
            	renderer:function(val){ 
                    return "<span style='color:green'>"+val+"</span>"; 
                },
            	sortable: true
            },{text: '收入时间',
            	dataIndex: 'date',
            	flex: 2,
            	renderer:function(val){ 
                    return "<span style='color:green'>"+val+"</span>"; 
                },
            	sortable: true
            },{text: '代理成本',
            	dataIndex: 'proxyprice',
            	flex: 2,
            	renderer:function(val){ 
            		if(val==null||val=='null'){
            			val = 0;
            		}
                    return "<span style='color:green'>"+val+"</span>"; 
                },
            	sortable: true
            },{text: '公司成本',
            	id:'costpay',
            	dataIndex: 'costpay',
            	flex: 2,
            	renderer:function(val){ 
            		if(val==null||val=='null'){
            			val = 0;
            		}
                    return "<span style='color:green'>"+val+"</span>"; 
                },
            	sortable: true
            },
            {text: '是否结算',
            	dataIndex: 'isSuccess',
            	flex: 1,
            	renderer:function(val){ 
            		if(val==false){
            			return "<span style='color:red;font-weight:bold;'>未结算</span>";
            		}else{
            			return "已结算";  
            		}
                },
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