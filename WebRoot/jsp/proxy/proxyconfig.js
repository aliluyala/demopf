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
	
	
    Ext.define('dataModel', {
        extend: 'Ext.data.Model',
        fields: ['id','path','descript','price','type','discount','discountPrice','discountPoint','cost','proxyPrice','proxyName'
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
            url: '../../sys/proxy/proxyConfigListByPage.action',
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
	    items: ['-',{
			        iconCls:'refush',
			        text:'刷新',
			        xtype: 'button',
			        handler: function () {
			    		store.load();
			        }
			    },'-','->',
			    {width:180,labelWidth:50,name:'titleSearch',id:'titleSearch',xtype:'textfield',fieldLabel:'商品名'},
		    	queryButton
	    ]
	});
	
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '商品价格配置',
        columnLines:true,
        store: store,
        id:"grid",
        loadMask:true,
        disableSelection: false,
        tbar:[tbar],
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
        columns:[
			{text: '代理名称',
			    dataIndex: 'proxyName',
			    flex: 2,
			    sortable: true
			},
        	{text: '商品名称',
                dataIndex: 'descript',
                flex: 2,
                sortable: true
            },
            {
	            xtype:'actioncolumn', 
	            flex: 8,
	            header: '操作',
	            //align:'center',
	            items: [{
	            	iconCls:'edit',
	                tooltip: 'Edit',
	                altText:'test',
	                width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	            		var rec = grid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
    	            	Ext.Ajax.request({url:'../../sys/proxy/queryConfig.action',  
    	            		params : {configId:id},
    	            		method:'post',
    	            		type:'json',  
    	            		success:function(result, request){
								var responseObj =  Ext.decode(result.responseText);
								if(responseObj.success!=null&&!responseObj.success){
									Ext.MessageBox.alert("错误提示",responseObj.appMsg);
									return;
								}
								var form = Ext.getCmp('editform');
								form.getForm().reset();
								Ext.getCmp('configId').setValue(responseObj.data.business['id']);
								Ext.getCmp('descript').setValue(responseObj.data.business['descript']);
								Ext.getCmp('truckPrice').setValue(responseObj.data.business['truckPrice']);
								Ext.getCmp('consignorPrice').setValue(responseObj.data.business['consignorPrice']);
								Ext.getCmp('truckDiscountPoint').setValue(responseObj.data.business['truckDiscountPoint']);
								Ext.getCmp('consignorDiscountPoint').setValue(responseObj.data.business['consignorDiscountPoint']);
								var h = Ext.getBody().getHeight();
								editwindow.setHeight(h);
								editwindow.show();
							},
							failure: function(result,options){
								var responseObj =  Ext.decode(result.responseText);
								Ext.MessageBox.alert('提示',responseObj.appMsg);
							}
    	            	});
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
  
  
  
//创建文本上传域
    var exteditor = new Ext.form.HtmlEditor({
    	id:'contentedit',
    	width : 370,
        height:280,
        fieldLabel: '内容'
   });
  
    var editwindow = new Ext.Window({  
        title:'价格配置',  
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
            url: '../../sys/proxy/proxyConfigSave.action',
            labelWidth:30,  
            //fields: ['id','path','descript','price','type','discount','discountPrice','discountPoint','cost'
            items:[
	  		{  
	  		    id:'configId',
	  		    name:'configId',
	  		    labelWidth:40,  
	  		    xtype:'hiddenfield',  
	  		},
	  	    {  
	      	  id:'descript',
	      	  name:'descript',
	      	disabled:true,
	      	  labelWidth:90,
	      	  width : 65,
	            xtype:'textfield',  
	            fieldLabel:'商品名称'
	  	    },
	  	    {  
	      	  id:'truckPrice',
	      	  name:'truckPrice',
	      	  allowBlank:false,
	      	  labelWidth:160,
	      	  width : 65,
	      	xtype:'numberfield',  
            decimalPrecision: 0, 
            minValue : 0,
            maxValue : 10000,  
	            fieldLabel:'司机价格'
	  	    },
	  	    {  
	      	  id:'consignorPrice',
	      	  name:'consignorPrice',
	      	  allowBlank:false,
	      	  labelWidth:160,
	      	  width : 65,
	      	xtype:'numberfield',  
            decimalPrecision: 0, 
            minValue : 0,
            maxValue : 10000,   
	            fieldLabel:'货主价格'
	  	    },
	  	    {  
	      	  id:'truckDiscountPoint',
	      	  name:'truckDiscountPoint',
	      	  labelWidth:160,
	      	 // disabled:true,
	      	  width : 75,
	      	xtype:'numberfield',  
            decimalPrecision: 2, 
            minValue : 0,
            maxValue : 1,   
              fieldLabel:'司机折扣率'
	  	    },
	  	  {  
	      	  id:'consignorDiscountPoint',
	      	  name:'consignorDiscountPoint',
	      	  labelWidth:160,
	      	 // disabled:true,
	      	  width : 75,
		      	xtype:'numberfield',  
	            decimalPrecision: 2, 
	            minValue : 0,
	            maxValue : 1, 
              fieldLabel:'货主折扣率'
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
  
    
    var ddwindow = new Ext.Window({  
        title:'价格配置',  
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
            id:'ddeditform',
            baseCls:'x-plain',  
            xtype:'form',
            style:'padding:8px;',  
            url: '../../sys/proxy/proxyConfigSave.action',
            labelWidth:30,  
            //fields: ['id','path','descript','price','type','discount','discountPrice','discountPoint','cost'
            items:[
	  		{  
	  		    id:'ddconfigId',
	  		    name:'configId',
	  		    labelWidth:40,  
	  		    xtype:'hiddenfield',  
	  		},
	  	    {  
	      	  id:'dddescript',
	      	  name:'descript',
	      	disabled:true,
	      	  labelWidth:90,
	      	  width : 65,
	            xtype:'textfield',  
	            fieldLabel:'商品名称'
	  	    },
	  	  {  
				id:'discount',
				name:'discount',
				blankText:'直减率',
				labelWidth:100,  
				xtype:'numberfield',  
                decimalPrecision: 2, 
                minValue : 0,
                maxValue : 1,
				fieldLabel:'直减率',  
				anchor:'90%'  
			},
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
  	                           ddwindow.close();
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
  	        		ddwindow.close();
  	    		}
  	      }]
        }
        ]
    });
});