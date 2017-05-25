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
	
	var updateStore = Ext.create('Ext.data.Store', {
		 fields: ['code', 'name'],
		 data : [
		 {"code":true,"name":"是"},
		 {"code":false,"name":"否"}
		 ]
		});	
	var appStore = Ext.create('Ext.data.Store', {
		 fields: ['code', 'name'],
		 data : [
		 {"code":1,"name":"司机版"},
		 {"code":2,"name":"货主版"},
		 {"code":3,"name":"商家版"},
		 {"code":4,"name":"管理版"}
		 ]
		});	
	
    Ext.define('travelModel', {
        extend: 'Ext.data.Model',
        fields: ['apkUrl','newMd5','targetSize','newVersion','updateLog','createdDate','appType','forceUpdate','versionName'
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
            url: '../../sys/apkupdate/jarApkByPage.action',
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
		 {"code":"false","name":"审核不通过"},
		 {"code":"true","name":"审核通过"}
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
	            },'-',{
			        iconCls:'refush',
			        text:'发布新版本',
			        xtype: 'button',
			        handler: function () {
			        	Ext.getCmp("editform").form.reset();
			        	var h = Ext.getBody().getHeight();
			        	updatewindow.setHeight(h);
			        	updatewindow.show();
			        }
	            },
	            '-','->'
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
        title: 'apk列表',
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
            checkOnly: true     //只能通过checkbox选择apkUrl','newMd5','targetSize','newVersion','updateLog','createdDate'
        },
        selType: "checkboxmodel",
        listeners: {
            itemdblclick: function (me, record, item, index, e, eOpts) {
            	var p = record.data;
            	if(p.appType==1){
            		Ext.getCmp('appName').setValue("司机版");
        		}else if(p.appType==2){
        			Ext.getCmp('appName').setValue("货主版");
        		}else if(p.appType==3){
        			Ext.getCmp('appName').setValue("商家版");
        		}else if(p.appType==4){
        			Ext.getCmp('appName').setValue("管理版");
        		}
                Ext.getCmp('apkUrl').setValue(p.apkUrl);
                Ext.getCmp('targetSize').setValue(p.targetSize);
                Ext.getCmp('newVersiond').setValue(p.newVersion);
                Ext.getCmp('updateLogd').setValue(p.updateLog);
                Ext.getCmp('createdDate').setValue(p.createdDate);
            	var h = Ext.getBody().getHeight();
            	detailwindow.setHeight(h);
            	detailwindow.show();
            }
        },
        // fields: ['id','name','email','sex','createTime','phone','address','resume','birthday','status','age','phone'
        columns:[
			new Ext.grid.RowNumberer({   header : "序号",   width : 40,
				renderer: function (value, metadata, record, rowIndex) {
					return record_start + 1 + rowIndex;
				} 
			}),
			{text: '应用类型',
	            dataIndex: 'appType',
	            flex: 2,
	            sortable: true,
	            renderer: function (value, meta, record) {
            		if(value==1){
            			return "司机版";
            		}else if(value==2){
            			return "货主版";  
            		}else if(value==3){
            			return "商家版";  
            		}else if(value==4){
            			return "管理版";  
            		}
            	}
        	},
        	{text: '版本号',
        		dataIndex: 'newVersion',
        		flex: 2,
        		sortable: true
        	},
        	{text: '版本名',
        		dataIndex: 'versionName',
        		flex: 2,
        		sortable: true
        	},
        	{text: '文件大小(M)',
                dataIndex: 'targetSize',
                flex: 2,
                sortable: true
            },
        	{text: '创建日期',
            	dataIndex: 'createdDate',
            	flex: 3,
            	sortable: true
            },
            {text: '版本日志',
                dataIndex: 'updateLog',
                flex: 12,
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
  
  
  var  detailwindow = new Ext.Window({  
      title:'确认信息',  
      width:500,  
      height:600,  
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
          id:'detailform',
          baseCls:'x-plain',  
          xtype:'form',
          style:'padding:8px;',  
          url: '../../sys/proxy/checkChinaLife.action',
          labelWidth:30,  
          items:[
			{
			      xtype: 'displayfield',
			      fieldLabel: '应用名称',
			      id:'appName',
			      name: 'appName',
			      value: '11'
			  }, 
			{
			      xtype: 'displayfield',
			      fieldLabel: '版本号',
			      id:'newVersiond',
			      name: 'newVersion',
			      value: '10'
			  }, 
	  		{
	  	        xtype: 'displayfield',
	  	        fieldLabel: '下载地址',
	  	        id:'apkUrl',
	  	        name: 'apkUrl',
	  	        value: '10'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '文件大小(m)',
	  	        name: 'targetSize',
	  	        id:'targetSize',
	  	        value: '11'
	  	    },{
	  	        xtype: 'displayfield',
	  	        fieldLabel: '更新日志',
	  	        name: 'updateLogd',
	  	        id:'updateLogd',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '上传日期',
	  	        name: 'createdDate',
	  	        id:'createdDate',
	  	        value: '11'
	  	    }
        ],
	      buttonAlign:'center',
	      buttons:[{  
	          	  text:'确定',
	          	  formBind: true,
	        	  handler: function() {
	        		  detailwindow.close();
	          	  }
	      },{  
	          	  text:'取消',
	        	  handler: function() {
	        		  detailwindow.close();
	    		}
	      }]
      }
      ]
  }) ;
  
  
  var  updatewindow = new Ext.Window({  
      title:'确认信息',  
      width:500,  
      height:600,  
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
          url: '../../sys/apkupdate/jarUpload.action',
          labelWidth:30,  
          items:[
		{	  
			  forceSelection: true,
			  xtype: 'combobox',
			  fieldLabel:'应用类型',
			  labelWidth: 100,
			  anchor: '100%',
			  editable:false,
			  store: appStore,
			  queryMode: 'local',
			  displayField: 'name',
			  valueField: 'code',
			  value:false,
			  id:'appType',
			  name:'appType'
		  },
	  	{
	        xtype: 'filefield',
	        name: 'photo',
	        id: 'photo',
	        fieldLabel: 'jar上传',
	        labelWidth: 100,
	        //msgTarget: 'side',
	        allowBlank: false,
	        anchor: '100%',
	        buttonText: 'jar上传'
	    },
	    {  
			  id:'versionName',
			  name:'versionName',
			  allowBlank:false,
			  blankText:'版本名必需要',
			  labelWidth:60,  
			  xtype:'textfield',  
			  fieldLabel:'版本名',  
			  anchor:'90%'  
		},
		{  
			  id:'version',
			  name:'version',
			  allowBlank:false,
			  blankText:'版本号必需要',
			  labelWidth:60,  
			  xtype:'numberfield',  
              decimalPrecision: 0,  
			  fieldLabel:'版本号',  
			  anchor:'90%'  
		},
		{  
			  id:'updateLog',
			  name:'updateLog',
			  allowBlank:false,
			  blankText:'更新日志',
			  labelWidth:100,  
			  xtype:'textarea',  
			  fieldLabel:'更新日志',  
			  anchor:'90%'  
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
	                             updatewindow.close();
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
	        		  updatewindow.close();
	    		}
	      }]
      }
      ]
  });  
  
});