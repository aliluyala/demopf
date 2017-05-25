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
    Ext.define('dataModel', {
        extend: 'Ext.data.Model',
        fields: ['id','orderNo','addTime','consignorName','consignorMobile','origin','destination','truckType',
                 'truckLength','goodsType','weight','bulk','fee','bond','payType','memo'
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
            url: '../../sys/statistics/waybillList.action',
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
    		paras.content = encodeURIComponent(Ext.getCmp("content").getValue());
    		store.load();
		}
    });
    
    
    var createButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'add', iconCls:'add',text: '增加',
    	handler: function() {
	    	var form = Ext.getCmp('dictionaryform');
			form.getForm().reset();
			var h = Ext.getBody().getHeight();
			window.setHeight(h);
			window.show();
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
			    {width:180,labelWidth:65,name:'content',id:'content',xtype:'textfield',fieldLabel:'出发地'},
		    	queryButton
	    ]
	});
	
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        title: '货源列表',
        columnLines:true,
        store: store,
        loadMask:true,
        disableSelection: false,
        tbar:[tbar],
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
        columns:[
        	{text: '发布时间',
                dataIndex: 'addTime',
                flex: 1.5,
                sortable: true
            },
            {text: '货主名称',
                dataIndex: 'consignorName',
                flex: 1,
                sortable: true
            },
            {text: '货主电话',
                dataIndex: 'consignorMobile',
                flex: 1,
                sortable: true
            },
            {text: '出发地',
                dataIndex: 'origin',
                flex: 1,
                sortable: true
            },
            {text: '目的地',
                dataIndex: 'destination',
                flex: 1,
                sortable: true
            },
            {text: '要求车类型',
                dataIndex: 'truckType',
                flex: 1,
                sortable: true
            },
            {text: '要求车长',
                dataIndex: 'truckLength',
                flex: 1,
                sortable: true
            },
            {text: '货物类型',
                dataIndex: 'goodsType',
                flex: 1,
                sortable: true
            },
            {text: '货重量',
                dataIndex: 'weight',
                flex: 1,
                sortable: true
            },
            {text: '运费',
                dataIndex: 'fee',
                flex: 1,
                sortable: true
            },
            {text: '保证金',
                dataIndex: 'bond',
                flex: 1,
                sortable: true
            },
            {text: '说明',
            	dataIndex: 'memo',
            	flex: 1,
            	sortable: true
            },
/*            {
	            xtype:'actioncolumn', 
	            flex: 1,
	            header: '操作',
	            //align:'center',
	            items: [{
	            	iconCls:'del',
	                tooltip: 'del',
	                altText:'del',
	                width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	            		var rec = grid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");                    
	            		var rec = addwarehouseGrid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
	            		Ext.Msg.confirm('系统提示','确定要删除吗？',function(btn){
	            			if(btn=='yes'){
		    	            	Ext.Ajax.request({url:'../../sys/thirdmall/deleteCategory.action',  params : {cateId:id},method:'post',type:'json',  success:function(result, request){
									var responseObj =  Ext.decode(result.responseText);
									if(responseObj.success!=null&&!responseObj.success){
										Ext.MessageBox.alert("错误提示",responseObj.appMsg);
										return;
									}
									store.reload();
								},
								failure: function(result,options){
									var responseObj =  Ext.decode(result.responseText);
									Ext.MessageBox.alert('提示',responseObj.appMsg);
								}});
	            			}
	            		});
	                }
	            }]
			 }*/
        	],
        forceFit: true,
        // paging bar on the bottom
        bbar: bbar
    });
  Ext.create('Ext.Viewport',{
		layout: 'fit',items:[grid],renderTo: 'btsAlarm-grid'
	});
  
  //fields: ['id','storeName','storeType','address','','description','createdDate'
	var window = new Ext.Window({  
	      title:'新建',  
	      width:500,  
	      height:500,  
	      closeAction:'hide',  
	      plain:true,  
	      layout:'form',  
	      monitorValid:true,
	      items:[
	             Ext.create('Ext.form.Panel', {
	            	 id:'dictionaryform',
	            	 layout:'form',  
	                 baseCls:'x-plain',  
	                 style:'padding:8px;',  
	                 url: '../../sys/thirdmall/addCategory.action',
	                 labelWidth:30, 
	                 //['id','tellerName','tellerId','lastLogin','isLock','createdDate'
			         items:[
						{  
						  id:'cateName1',
						  name:'cateName',
						  allowBlank:false,
						  blankText:'类型名称',
						  labelWidth:100,  
						  xtype:'textfield',  
						  fieldLabel:'类型名称',  
						  anchor:'90%'  
						},
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
						            	  	window.close();
					                      },
					                      failure: function (form, action) { 
					                    	btn.setDisabled(false);
					                    	Ext.Msg.alert('操作失败', action.result.appMsg);
					                        window.close();
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
	             })
	      ],  
	  }); 
  
});