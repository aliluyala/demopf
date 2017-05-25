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
            url: '../../sys/insurance/ddcxwyinsuranceListCheck.action',
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
	            },'-',{
			        iconCls:'refush',
			        text:'审核',
			        id:'balanceButtion',
			        xtype: 'button',
			        handler: function () {
			        	var records = grid.getSelectionModel().getSelection();
			        	if(records==null||records.length<=0){
			        		Ext.MessageBox.alert("提示","请选择一项");
			        		return;
			        	}
			        	if(records.length>1){
			        		Ext.MessageBox.alert("提示","本操作最多只能选择一项");
			        		return;
			        	}
			        	if(records[0].data.status!=3){
			        		Ext.MessageBox.alert("提示","已审核,不能再审！");
			        		return;
			        	}
			        	
			        	var location = (window.location+'').split('/'); 
	                	var basePath = location[0]+'//'+location[2]+'/'+'ydypf';
	                	var p = records[0].data.xczPath;
	                	
	                	
	                	if(p!=null&&p!=""){
	                		Ext.getCmp("path2").setSrc(basePath+'/'+p);
	                	}else{
	                		Ext.getCmp("path2").setSrc("");
	                	}
			        	Ext.getCmp("id").setValue(records[0].data.id);
			        	Ext.getCmp("orderNo").setValue(records[0].data.orderNo);
			        	Ext.getCmp("type").setValue(records[0].data.type);
			        	Ext.getCmp("createdDate").setValue(records[0].data.createdDate);
			        	Ext.getCmp("name").setValue(records[0].data.name);
			        	Ext.getCmp("idCard").setValue(records[0].data.idCard);
			        	Ext.getCmp("count").setValue(records[0].data.count);
			        	Ext.getCmp("startTime").setValue(records[0].data.startTime);
						var h = Ext.getBody().getHeight();
						windowedit.setHeight(h);
						windowedit.show();
			        }
	            },
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
            	flex: 2,
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
           /* {text: '费用',
            	dataIndex: 'balance',
            	flex: 1,
            	sortable: true
            },*/
            {text: '被保险人名称',
            	dataIndex: 'name',
            	flex: 1,
            	sortable: true
            },
            /*{text: '被保险人手机号',
            	dataIndex: 'mobile',
            	flex: 1,
            	sortable: true
            },*/
            {text: '身份证/机构代码证',
            	dataIndex: 'idCard',
            	flex: 2,
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
	            flex: 3,
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
  
  
  var windowedit = new Ext.Window({  
      title:'保单上传',  
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
          url: '../../sys/insurance/update.action',
          labelWidth:30,  
          //fields: ['id','path','descript','price','type','discount','discountPrice','discountPoint','cost'
          items:[
			{  
			    id:'id',
			    name:'id',
			    labelWidth:40,  
			    xtype:'hiddenfield',  
			},
			{
	  	        xtype: 'displayfield',
	  	        fieldLabel: '订单号',
	  	        id:'orderNo',
	  	        name: 'orderNo',
	  	        value: '10'
	  	    },
	  	    {
	  	    	xtype: 'displayfield',
	  	    	fieldLabel: '投保类型',
	  	    	id:'type',
	  	    	name: 'type',
	  	    	value: '10'
	  	    },
	  	    {
	  	    	xtype: 'displayfield',
	  	    	fieldLabel: '创建时间',
	  	    	id:'createdDate',
	  	    	name: 'createdDate',
	  	    	value: '10'
	  	    },
	  	    {
	  	    	xtype: 'displayfield',
	  	    	fieldLabel: '被保险人名称',
	  	    	id:'name',
	  	    	name: 'name',
	  	    	value: '10'
	  	    },
	  	    {
	  	    	xtype: 'displayfield',
	  	    	labelWidth: 80,
	  	    	fieldLabel: '身份证/机构代码证',
	  	    	id:'idCard',
	  	    	name: 'idCard',
	  	    	value: '10'
	  	    },
	  	    {
	  	    	xtype: 'displayfield',
	  	    	fieldLabel: '保险起始时间',
	  	    	id:'startTime',
	  	    	name: 'startTime',
	  	    	value: '10'
	  	    },
	  	    {
	  	    	xtype: 'displayfield',
	  	    	fieldLabel: '保险人数',
	  	    	id:'count',
	  	    	name: 'count',
	  	    	value: '10'
	  	    },
	  	  {
	     		xtype: 'label',
	     		text: '行驶照片：'
	     },
	  	  Ext.create('Ext.Img', {
        	  id:'path2',
        	  height:650,
        	  width : 455,
        	  name:'xszp',
   	      src: '../../img/pic.png'
       	}),
	  		{
		        xtype: 'filefield',
		        name: 'photo',
		        fieldLabel: '保单',
		        labelWidth: 50,
		        msgTarget: 'side',
		        allowBlank: false,
		        anchor: '100%',
		        buttonText: '上传文件'
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
	                             windowedit.close();
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
	        		  windowedit.close();
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