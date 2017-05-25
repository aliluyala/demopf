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
        fields: ['accountId','pay','tradeType','remark','createdDate','point','orderNo','userType'],
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
            url: '../../sys/statistics/companyStatements.action',
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
    		//paras.checkStatus = Ext.getCmp("bsc").getValue();
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
		 {"code":"false","name":"未审核"},
		 {"code":"true","name":"已审校"}
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
	            '-','->',{width:180,labelWidth:65,name:'orderNo',id:'orderNo',xtype:'textfield',fieldLabel:'订单编号'},'-',
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
        // fields: ['accountId','pay','tradeType','remark','createdDate','userName','point','orderNo','userType'],
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
            	sortable: true
            },
            {text: '用户类型',
            	dataIndex: 'userType',
            	flex: 1,
            	sortable: true
            }
            ,
        	{text: '收支额度',
                dataIndex: 'pay',
                flex: 1,
                sortable: true
            },
        	{text: '收支说明',
            	dataIndex: 'remark',
            	flex: 2,
            	sortable: true
            },{text: '收支时间',
            	dataIndex: 'createdDate',
            	flex: 2,
            	sortable: true
            },{text: '点数收支',
            	dataIndex: 'point',
            	flex: 6,
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
  var window = new Ext.Window({  
      title:'司机审核',  
      width:500,  
      height:750,  
      closeAction:'hide',  
      plain:true,  
      layout:'form',
      autoScroll:true,
      monitorValid:true,
	  listeners: {
	  		close: function() {
	  			Ext.getBody().unmask();
	         }
			  ,
			  show: function() {
			 	 Ext.getBody().mask();
			  }
	   },
      items:[{  
          layout:'form',  
          id:'checkform',
          baseCls:'x-plain',  
          xtype:'form',
          style:'padding:8px;',  
          url: '../../sys/member/checkedConsignorUser.action',
          labelWidth:30,  
          items:[
			{  
			    id:'consignorId',
			    name:'consignorId',
			    labelWidth:40,  
			    xtype:'hiddenfield',  
			},
			{
			    xtype: 'label',
			    text: '身份证照片：'
			},
             Ext.create('Ext.Img', {
             	  id:'idCardp',
             	  height:350,
             	  width : 455,
             	  name:'idCardp',
                 src: '../../img/pic.jpg',
                 listeners: {
               	  render: function() {
               		  this.mon(this.imgEl, 'mouseover', function () {
               			  
               		  }, this);
                     }
                 }
           }),
           {  
         	  id:'idCard',
         	  name:'idCard',
         	  allowBlank:false,
         	  labelWidth:40,
         	  width : 455,
               xtype:'textfield',  
               fieldLabel:'身份证'
          },
         {
        	width:455,
        	forceSelection: true,
        	xtype: 'combobox',
        	fieldLabel:'审核状态:',
        	labelWidth: 55,
			store: checkStatusStore,
			emptyText :'请选择...',
			queryMode: 'local',
			displayField: 'name',
			valueField: 'code',
			id:'checked',
			name:'checked',
			listeners : {
			       afterRender : function(combo) {
			    	   if(this.getValue()==null){
			    		   combo.setValue('false');
			    	   }
			      }
			}
		}
          ],
	      buttonAlign:'center',
	      buttons:[{  
	          	  text:'确定',
	          	  formBind: true,
	        	  handler: function() {
	        		  var form = this.up('form').getForm();
	                  if (form.isValid()) {
	                      form.submit({
	                          success: function(form, action) {
	                             store.reload();
	       	        		  	 window.close();
	                          },
	                          failure: function(form, action) {
	                              Ext.Msg.alert('操作失败', action.result.appMsg);
	                          }
	                      });
	                  }
	          	  }
	      },{  
	          	  text:'取消',
	        	  handler: function() {
					window.close();
	    		}
	      }]
      }
      ]
  });  
  
  //['realName','mobile','idCard','isLocked','consignorType','loginDate','loginIp','cities','registrationPic',
   //'address','isAuth','fax','registerTime','isPerfect'
  var editwindow = new Ext.Window({  
      title:'司机审核',  
      width:500,  
      height:750,  
      closeAction:'hide',  
      plain:true,  
      layout:'form',
      autoScroll:true,
      monitorValid:true,
	  listeners: {
	  		close: function() {
	  			Ext.getBody().unmask();
	         },
	         show: function() {
	        	 Ext.getBody().mask();
		     }
	   },
      items:[{  
          layout:'form',  
          id:'editform',
          baseCls:'x-plain',  
          xtype:'form',
          style:'padding:8px;',  
          url: '../../sys/member/editConsignorUser.action',
          labelWidth:30,  
          items:[
		{  
		    id:'editConsignorId',
		    name:'consignorId',
		    labelWidth:40,  
		    xtype:'hiddenfield',  
		},
	    {  
    	  id:'realName',
    	  name:'realName',
    	  allowBlank:false,
    	  labelWidth:70,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'名称'
	    },
	    {  
    	  id:'mobile',
    	  name:'mobile',
    	  allowBlank:false,
    	  labelWidth:70,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'手机号'
	    },
	    {  
    	  id:'editidCard',
    	  name:'idCard',
    	  allowBlank:false,
    	  labelWidth:70,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'身份证号码'
	    }
          ],
	      buttonAlign:'center',
	      buttons:[{  
	          	  text:'确定',
	          	  formBind: true,
	        	  handler: function() {
	        		  var form = this.up('form').getForm();
	                  if (form.isValid()) {
	                      form.submit({
	                          success: function(form, action) {
	                             store.reload();
	       	        		  	 editwindow.close();
	                          },
	                          failure: function(form, action) {
	                              Ext.Msg.alert('操作失败', action.result.appMsg);
	                          }
	                      });
	                  }
	          	  }
	      },{  
	          	  text:'取消',
	        	  handler: function() {
	        		editwindow.close();
	    		}
	      }]
      }
      ]
  });  
  
  Ext.define('StatementsModel', {
      extend: 'Ext.data.Model',
      fields: ['accountId','pay','deposit','balance','tradeType','remark','createdDate'
      ],
      idProperty: 'id'
  });
  var paras2={};
  var store2 = Ext.create('Ext.data.Store', {
      model: 'StatementsModel',
      remoteSort: false,
      autoLoad:false,
      proxy: {
  		timeout: 120000,//120s
        method:'POST',
      	type: 'ajax',
          url: '../../sys/member/consignorStatements.action',
          reader: {
              root: 'root',
              totalProperty: 'totalCount'
          }
      },
      listeners: {
      	beforeload: function (proxy, params) {
             this.proxy.extraParams = paras2;
          }
       }
  });
  var window2 = new Ext.Window({  
      title:'支付详细',  
      width:500,  
      height:750,  
      closeAction:'hide',  
      plain:true,  
      layout:'fit',
      autoScroll:true,
      monitorValid:true,
      listeners: {
	  		close: function() {
	  			Ext.getBody().unmask();
	         }
			  ,
			  show: function() {
			 	 Ext.getBody().mask();
			  }
	   },
      items:[
		Ext.create('Ext.grid.Panel', {
		    stripeRows: true,
		    autoWidth: true,
		    id:'grid1',
		    autoExpandColumn: 'name',
		    columnLines:true,
		    store: store2,
		    //paramNames: { createtime:  },
		    loadMask:true,
		    disableSelection: false,
		    viewConfig: { 
		    	loadingText: '正在查询,请等待...'
			} ,
		    columns:[new Ext.grid.RowNumberer({   header : "序号",   width : 40,
				renderer: function (value, metadata, record, rowIndex) {
					return record_start + 1 + rowIndex;
				} 
			}),
        	{text: '账单',
	            dataIndex: 'pay',
	            flex: 2,
	            sortable: true
        	},{text: '支付说明',
	            dataIndex: 'remark',
	            flex: 2,
	            sortable: true
        	},
        	{text: '支付时间',
	            dataIndex: 'createdDate',
	            flex: 2,
	            sortable: true
        	},{text: '余额',
	            dataIndex: 'balance',
	            flex: 2,
	            sortable: true
        	}],
		    forceFit: true,
		})
      ]
       
  });  
});