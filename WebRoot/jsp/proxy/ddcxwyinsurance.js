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
        fields: ['id','orderNo','proxyId','proxyName','saleUserId','saleUserName','xczPath','bdPath','status','createdDate','balance','count','name','mobile','idCard','startTime','type'],
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
            url: '../../sys/insurance/ddcxwyinsuranceList.action',
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
		
    
    var continueStore = Ext.create('Ext.data.Store', {
		fields: ['code', 'name'],
		 data : [
		 {"code":2,"name":"两人"},
		 {"code":3,"name":"三人"}
		 ]
	});
    
    var typeStore = Ext.create('Ext.data.Store', {
		fields: ['code', 'name'],
		 data : [
		 {"code":1,"name":"500元"},
		 {"code":2,"name":"700元"},
		 {"code":3,"name":"1000元"}
		 ]
	});
    
    var createButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'add', iconCls:'add',text: '购买',
    	handler: function() {
	    	var form = Ext.getCmp('addform');
			form.getForm().reset();
			var h = Ext.getBody().getHeight();
			windowadd.setHeight(h);
			windowadd.show();
		}
    });
    
    var queryButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'query', iconCls:'zoom',text: '查 询',
    	handler: function() {
    		paras.proxyName = encodeURIComponent(Ext.getCmp("searchproxyName").getValue());
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
	            },'-',createButton,
	            '-','->',{width:180,name:'proxyName',id:'searchproxyName',xtype:'textfield',fieldLabel:'被保险人名称'},
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
			})
			,
			{text: '订单编号',
            	dataIndex: 'orderNo',
            	flex: 1,
            	sortable: true
            },
            {text: '保险类型',
            	dataIndex: 'type',
            	flex: 1,
            	sortable: true,
            	renderer: function (value, meta, record) {
            		if(value!=null){
            			return value+"类型";
            		}
            	}
            },
            {text: '代理名称',
            	dataIndex: 'proxyName',
            	flex: 1,
            	sortable: true
            },
        	/*{text: '业务员名',
                dataIndex: 'saleUserName',
                flex: 1,
                sortable: true
            },*/
        	{text: '新增时间',
                dataIndex: 'createdDate',
                flex: 1,
                sortable: true
            },
            {text: '费用',
            	dataIndex: 'balance',
            	flex: 1,
            	sortable: true
            },
            {text: '被保险人名称',
            	dataIndex: 'name',
            	flex: 1,
            	sortable: true
            },
            {text: '被保险人手机号',
            	dataIndex: 'mobile',
            	flex: 1,
            	sortable: true
            },
            {text: '身份证/机构代码证',
            	dataIndex: 'idCard',
            	flex: 1,
            	sortable: true
            },
            {text: '保险起始时间',
            	dataIndex: 'startTime',
            	flex: 1,
            	sortable: true
            }
            ,{text: '保险人数',
            	dataIndex: 'count',
            	flex: 1,
            	sortable: true
            }
            ,{text: '是否审核',
            	dataIndex: 'status',
            	flex: 1,
            	sortable: true,
            	renderer: function (value, meta, record) {
            		if(value==3){
            			return "<span style='color:red;font-weight:bold;'>待审核</span>";
            		}else{
            			return "已审核";  
            		}
            	}
            },{
	            xtype:'actioncolumn', 
	            flex: 6,
	            header: '查看行车证  | 查看保险单  |',
	            //align:'center',
	            items: ['-','-',{
	            	iconCls:'dtl',
	            	tooltip: '查看行车证',
	                width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	                	var rec = grid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
	            		//双击事件的操作
	                	var location = (window.location+'').split('/'); 
	                	var basePath = location[0]+'//'+location[2]+'/'+'ydypf';
	                	var p = rec.data.xczPath;
	                	if(p!=null&&p!=""){
	                		Ext.getCmp("path").update('<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+basePath+'/'+p+'"></iframe>');
	                		var h = Ext.getBody().getHeight();
	    	          		checkwindow.setHeight(h);
	                		checkwindow.show();
	                	}else{
	                		Ext.getCmp("path").update("");
	                		Ext.MessageBox.alert("提示","还没有保单");
	                	}
	                	
	                }
	            },'-','-','-',{
	            	iconCls:'dtl',
	            	tooltip: '查看保险单',
	                width : '80',
	                hidden:true,
	                handler: function(grid, rowIndex, colIndex) {
	                	var rec = grid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
	            		//双击事件的操作
	                	var location = (window.location+'').split('/'); 
	                	var basePath = location[0]+'//'+location[2]+'/'+'ydypf';
	                	var p = rec.data.bdPath;
	                	if(p!=null&&p!=""){
	                		Ext.getCmp("path").update('<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+basePath+'/'+p+'"></iframe>');
	                		var h = Ext.getBody().getHeight();
	    	          		checkwindow.setHeight(h);
	                		checkwindow.show();
	                	}else{
	                		Ext.getCmp("path").update("");
	                		Ext.MessageBox.alert("提示","还没有保单");
	                	}
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
  var windowadd = new Ext.Window({  
      title:'上传行驶证',  
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
          id:'addform',
          baseCls:'x-plain',  
          xtype:'form',
          style:'padding:8px;',  
          url: '../../sys/insurance/save.action',
          labelWidth:30,  
          //fields: ['id','path','descript','price','type','discount','discountPrice','discountPoint','cost'
          items:[
{
	width:455,
	forceSelection: true,
	xtype: 'combobox',
	fieldLabel:'保险类别',
	labelWidth: 55,
	store: typeStore,
	emptyText :'请选择...',
	queryMode: 'local',
	displayField: 'name',
	valueField: 'code',
	id:'type',
	name:'type',
	value:1,
	listeners : {
		select : function(combo, record,index){
			//alert(record[0].data.code*100);
			if(record[0].data.code==1){
				Ext.getCmp("balancecell1").setValue(200000);
				Ext.getCmp("balancecell2").setValue(100000);
			}else if(record[0].data.code==2){
				Ext.getCmp("balancecell1").setValue(300000);
				Ext.getCmp("balancecell2").setValue(100000);
			}else{
				Ext.getCmp("balancecell1").setValue(300000);
				Ext.getCmp("balancecell2").setValue(200000);
			}
			
			var type = Ext.getCmp("type").getValue();
			var count = Ext.getCmp("count").getValue();
			if(type==1){
				Ext.getCmp("balance").setValue(count*500);
			}else if(type==2){
				Ext.getCmp("balance").setValue(count*700);
			}else{
				Ext.getCmp("balance").setValue(count*1000);
			}
			
		  }
	}
},
{
      xtype: 'textfield',
      readOnly:true,
      fieldLabel: '意外身故伤残(元)',
      id:'balancecell1',
      name: 'balancecell1',
      value: 200000
  },
  {
      xtype: 'textfield',
      readOnly:true,
      fieldLabel: '意外住院医疗(元)',
      id:'balancecell2',
      name: 'balancecell2',
      value: 100000
  },
  {
		width:455,
		forceSelection: true,
		xtype: 'combobox',
		fieldLabel:'保险人数',
		labelWidth: 80,
		store: continueStore,
		emptyText :'请选择...',
		queryMode: 'local',
		displayField: 'name',
		valueField: 'code',
		id:'count',
		name:'count',
		value:2,
		listeners : {
			select : function(combo, record,index){
				//alert(record[0].data.code*100);
				var type = Ext.getCmp("type").getValue();
				if(type==1){
					Ext.getCmp("balance").setValue(record[0].data.code*500);
				}else if(type==2){
					Ext.getCmp("balance").setValue(record[0].data.code*700);
				}else{
					Ext.getCmp("balance").setValue(record[0].data.code*1000);
				}
				
			  }
		}
	},
	{
	      xtype: 'textfield',
	      readOnly:true,
	      fieldLabel: '所需费用(元)',
	      id:'balance',
	      name: 'balance',
	      value: 1000
	  },
  {
      xtype: 'textfield',
      allowBlank: false,
      fieldLabel: '被保险人名称',
      regex:/^[\u4E00-\u9FA5]+$/,
      regexText:'请输入汉字',
      id:'name',
      name: 'name',
  },
  {
      xtype: 'textfield',
      allowBlank: false,
      fieldLabel: '被保险人电话',
      regex:/^1[\d]{10}$/,
      regexText:'请输入正确的手机号码',
      id:'mobile',
      name: 'mobile',
  },
  {
      xtype: 'textfield',
      allowBlank: false,
      labelWidth: 120,
      fieldLabel: '身份证/机构代码证',
      //regex:/(^\d{15}$)|(^\d{17}([0-9]|[x,X])$)/,
      regexText:'请输入正确的身份证/机构代码证',  
      id:'idCard',
      name: 'idCard',
  },
  {
	  allowBlank: false,
      xtype: 'datefield',
      id:'startTime',
      fieldLabel: '保险起始日期',
      name: 'startTime',
      format: 'Y-m-d'
  },
  
	  		{
		        xtype: 'filefield',
		        name: 'photo',
		        fieldLabel: '行驶证',
		        labelWidth: 50,
		        msgTarget: 'side',
		        allowBlank: false,
		        anchor: '100%',
		        buttonText: '上传文件'
		    }
  ,{
	  xtype:'checkboxfield',
      boxLabel  : "<a href='../../doc/最新通行无忧条款（司乘险）.docx'>已阅读最新通行无忧条款（司乘险）</a>",
      name      : 'topping',
      inputValue: '1',
      id        : 'checkbox1'
  }
        ],
	      buttonAlign:'center',
	      buttons:[{  
	          	  text:'确定',
	          	  formBind: true,
	        	  handler: function() {
	        		  if(Ext.getCmp('checkbox1').getValue()==false){
	        			  Ext.Msg.alert('提示', "请阅读条款！");
	        			  return;
	        		  }
	        		  var form = this.up('form').getForm();
	                  if (form.isValid()) {
	                      form.submit({
	                          success: function(form, action) {
	                             store.reload();
	                             windowadd.close();
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
	        		  windowadd.close();
	    		}
	      }]
      }
      ]
  });  
  
  var checkwindow = new Ext.Window({  
      title:'保险单',  
      width:800,  
      height:600,
      autoHeight : true,
      autoWidth:false,
      closeAction:'hide',  
      layout:'fit',
	  listeners: {
	  		close: function() {
	  			Ext.getBody().unmask();
	         },
			  show: function() {
			 	 Ext.getBody().mask();
			  }
	   },
      items:[{
    	  xtype:'panel',
    	  id:'path',
          html: ''
      }
      ]
  });  
  
});