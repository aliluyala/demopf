Ext.Loader.setConfig({enabled: true});

Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.util.*',
    'Ext.toolbar.Paging',
    'Ext.ModelManager'
]);

Ext.onReady(function(){
	
	function changeRowClass(record, rowIndex, rowParams, store){
		    if (record.get("status") == "2") {        
		        return 'x-grid-record-yellow';
		    }
	} 
	 
	var paras={};
    Ext.define('dataModel', {
        extend: 'Ext.data.Model',
        fields: ['id','orderNo','price','cardNumber','cardTypeName','truckMoblie','truckName','createDate','payDate','payType','status'
        ],
        idProperty: 'id'
    });
    // create the Data Store
	
	var checkStatusStore = Ext.create('Ext.data.Store', {
		fields: ['status', 'name'],
		 data : [
		 {"status":3,"name":"充值成功"},
		 {"status":4,"name":"充值失败"}
		 ]
	});
	
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
            url: '../../sys/card/truckCardSDGSCByPage.action',
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
			    {width:180,labelWidth:50,name:'orderNo',id:'orderNo',xtype:'textfield',fieldLabel:'订单号'},'-',
			    queryButton
	    ]
	});
	
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '加油列表',
        columnLines:true,
        store: store,
        loadMask:true,
        disableSelection: false,
        tbar:[tbar],
        viewConfig: { 
        	getRowClass:changeRowClass,
	    	loadingText: '正在查询,请等待...'
    	} ,
    	//fields: ['id','orderNo','price','cardNumber','cardTypeName','truckMoblie','truckName','createDate','payDate','payType','status'
        columns:[
        	{text: '订单号',
            dataIndex: 'orderNo',
            flex: 1.2,
            sortable: true
        	},
        	{text: '卡号',
                dataIndex: 'cardNumber',
                flex: 1,
                sortable: true,
            },
        	{text: '充值额度',
	            dataIndex: 'price',
	            flex: 0.5,
	            sortable: true
        	},
            {text: '提交订单时间',
            	dataIndex: 'createDate',
            	flex: 1,
            	sortable: true
            },
            {text: '支付订单时间',
            	dataIndex: 'payDate',
            	flex: 1,
            	sortable: true
            },
            {text: '订单状态',
            	dataIndex: 'status',
            	flex: 0.5,
            	sortable: true,
            	renderer: function (value, meta, record) {
            		if(value==2){
            			return "<span style='color:red;font-weight:bold;'>等待充值</span>";
            		}else if(value==1){
            			return "提交";  
            		}else if(value==3){
            			return "充值成功";
            		}else{
            			return "充值失败";
            		}
            	}
            },
            {
	            xtype:'actioncolumn', 
	            flex: 3,
	            header: '操作',
	            //align:'center',
	            items: [{
	            	iconCls:'edit',
	                tooltip: 'Edit',
	                altText:'test',
	                width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	                	var rec = grid.getStore().getAt(rowIndex);
	                	if(rec.get("status")!=2){
	                		Ext.MessageBox.alert("错误提示","不是等待充值状态不能充值");
	                		return;
	                	}
	            		var id = rec.get("id");                    
	            		var form = Ext.getCmp('editform');
						form.getForm().reset();
						Ext.getCmp('id').setValue(id);
						var h = Ext.getBody().getHeight();
						windowedit.setHeight(h);
						windowedit.show();
	                }
	            }]
			 }
        	],
        forceFit: true,
        // paging bar on the bottom
        bbar: bbar
    });
  Ext.create('Ext.Viewport',{
		layout: 'fit',items:[grid],renderTo: 'btsAlarm-grid'
	});
  
  
	var windowedit = new Ext.Window({  
	      title:'编辑',  
	      width:500,  
	      height:500,  
	      closeAction:'hide',  
	      plain:true,  
	      layout:'form',  
	      monitorValid:true,
	      items:[
	             Ext.create('Ext.form.Panel', {
	            	 id:'editform',
	            	 layout:'form',  
	                 baseCls:'x-plain',  
	                 style:'padding:8px;',  
	                 url: '../../sys/card/truckCardRHSDGSEdit.action',
	                 labelWidth:30, 
	                 //fields: ['id','orderNo','price','cardNumber','cardTypeName','truckMoblie','truckName','createDate','payDate','payType','status'
			         items:[
						{  
						    id:'id',
						    name:'id',
						    labelWidth:100,  
						    xtype:'hiddenfield',  
						},
				         {
				        	width:210,
				        	forceSelection: true,
				        	xtype: 'combobox',
				        	fieldLabel:'审核状态:',
				        	labelWidth: 70,
				        	anchor: '100%',
							store: checkStatusStore,
							emptyText :'请选择...',
							queryMode: 'local',
							displayField: 'name',
							valueField: 'status',
							id:'status',
							name:'status',
							listeners : {
							       afterRender : function(combo) {
							    	   if(this.getValue()==null){
							    		   this.setValue(3);
							    	   }
							    	   if(this.getValue()==3){
											Ext.getCmp('tips').setVisible(false);
										}else{
											Ext.getCmp('tips').setVisible(true);
										}
							       },
									select : function(combo,record){
										if(this.getValue()==3){
											Ext.getCmp('tips').setVisible(false);
										}else{
											Ext.getCmp('tips').setVisible(true);
										}
									}
							}
						},
					    {
							xtype:'radiogroup',
							fieldLabel:'不通过原因',
							id:'tips',
							name:'tips',
							items:[
							{boxLabel:'卡号不正确',name:'handleErrMsg',inputValue:"卡号不正确", checked: true},
							{boxLabel:'其它原因',name:'handleErrMsg',inputValue:"其它原因"}
							]},
			          ],
			          buttonAlign:'center',
				      buttons:[{  
				          	  text:'确定',
				          	  formBind: true,
				        	  handler: function() {
				        		  var btn = this;
				        		  btn.setDisabled(true);
				        		  var form = this.up('form').getForm();
				                  if (form.isValid()) {
				                	  form.submit({
					          			  method: "POST",
					                      success: function (form, action) {
					                    	btn.setDisabled(false);
					                        store.reload();
						            	  	location="javascript:location.reload()";
						            	  	windowedit.close();
					                      },
					                      failure: function (form, action) { 
					                    	btn.setDisabled(false);
					                    	Ext.Msg.alert('操作失败', action.result.appMsg);
					                    	windowedit.close();
					                      }
				                      });
				                	  
					                  
				                  }
				          	  }
				      },{  
				          	  text:'取消',
				        	  handler: function() {
				        		  windowedit.close();
				    		}
				      }]
	             })
	      ],  
	  }); 
});