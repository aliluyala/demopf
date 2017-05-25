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
        fields: ['id','tellerName','tellerId','lastLogin','isLock','createdDate'
        ],
        idProperty: 'id'
    });
    // create the Data Store
	
    Ext.define('parentModel', {
	     extend: 'Ext.data.Model',
	     fields: ['id','proxyName']
	});
  
  Ext.define('SonModel', {
	     extend: 'Ext.data.Model',
	     fields: ['id','storeName']
	});
	
	var parentStore = Ext.create('Ext.data.Store', {
	    model:'parentModel',
	    autoLoad:true,
		proxy: {
 		timeout: 120000,//120s
         method:'POST',
     	type: 'ajax',
         url: '../../sys/user/listProxy2.action',
         reader: {
             root: 'root'
         }
     }
	});
	
	var paras1={};
	var sonStore = Ext.create('Ext.data.Store', {
	    model:'SonModel',
	    autoLoad:false,
		proxy: {
 		timeout: 120000,//120s
         method:'POST',
     	type: 'ajax',
         url: '../../sys/thirdmall/thirdMallListByPage2.action',
         reader: {
             root: 'root'
         }
     },
     listeners: {
     	beforeload: function (proxy, params) {
            	this.proxy.extraParams = paras1;
         }
      }
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
            url: '../../sys/thirdmall/thirdMallTellerListByPage.action',
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
    		paras.tellerName = encodeURIComponent(Ext.getCmp("tellerNameSearch").getValue());
    		paras.proxyId = encodeURIComponent(Ext.getCmp("parentId").getValue());
    		paras.storeId = encodeURIComponent(Ext.getCmp("sonId").getValue());
    		store.load();
		}
    });
    
    var LockStore = Ext.create('Ext.data.Store', {
		 fields: ['id', 'name'],
		 data : [
		 {"id":"false","name":"否"},
		 {"id":"true","name":"是"}
		 ]
		});		
    
    var createButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'add', iconCls:'add',text: '增加',
    	handler: function() {
	    	var form = Ext.getCmp('dictionaryform');
	    	Ext.getCmp('tellerId').setDisabled(false);
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
            	createButton,'-',{
			        iconCls:'refush',
			        text:'刷新',
			        xtype: 'button',
			        handler: function () {
			    		store.load();
			        }
			    },'-','->',
			    {	  
		        	  forceSelection: true,
		        	  xtype: 'combobox',
		        	  fieldLabel:'所属代理',
		        	  labelWidth: 65,
		        	  anchor: '100%',
		  			  editable:false,
		  			  store: parentStore,
		  			  emptyText :'请选择...',
		  			  queryMode: 'local',
		  	    	  displayField: 'proxyName',
		  	    	  valueField: 'id',
		  	    	  allowBlank: false,
		  	    	  editable : true,//这项必须为true
		        	  queryParam : 'proxyName',
		  	    	  id:'parentId',
		  	    	  name:'parentId',
		  	    	  listeners:{  
						  select : function(combo, record,index){
							paras1.userId = record[0].data.id;
							sonStore.removeAll();
							Ext.getCmp("sonId").setValue("");
							sonStore.load();
						  }
					  }
		          },
		          {	  
		        	  xtype: 'combobox',
		        	  fieldLabel:'所属店家',
		        	  labelWidth: 65,
		        	  anchor: '100%',
		  			  editable:false,
		  			  store: sonStore,
		  			  queryMode: 'local',
		  			 editable : true,//这项必须为true
		        	  queryParam : 'storeName',
		  	    	  displayField: 'storeName',
		  	    	  valueField: 'id',
		  	    	  id:'sonId',
		  	    	  name:'sonId',
		  	    	  listeners:{  
						  select : function(combo, record,index){
							  paras.storeId = record[0].data.id;
							  //store.load();
						  }
					  }
		          },
			    {width:180,labelWidth:65,name:'tellerName',id:'tellerNameSearch',xtype:'textfield',fieldLabel:'员工名称'},
		    	queryButton
	    ]
	});
	
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '员工列表',
        columnLines:true,
        store: store,
        loadMask:true,
        disableSelection: false,
        tbar:[tbar],
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
        columns:[
        	{text: '员工名称',
            dataIndex: 'tellerName',
            flex: 1,
            sortable: true
        	},
        	{text: '员工编号',
	            dataIndex: 'tellerId',
	            flex: 1,
	            sortable: true
        	},
        	{text: '是否锁定',
                dataIndex: 'isLock',
                flex: 1,
                sortable: true,
                renderer: function (value, meta, record) {
                	if(value==false){
            			return "否";
            		}else{
            			return "是";  
            		}
            	}
            },
            {text: '创建时间',
            	dataIndex: 'createdDate',
            	flex: 8,
            	sortable: true
            },
            {
	            xtype:'actioncolumn', 
	            flex: 1,
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
    	            	Ext.Ajax.request({url:'../../sys/thirdmall/queryMallTeller.action',  params : {tellerId:id},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							var form = Ext.getCmp('dictionaryform');
							form.getForm().reset();
							Ext.getCmp('tellerName').setValue(responseObj.data['tellerName']);
							Ext.getCmp('tellerId').setValue(responseObj.data['tellerId']);
							Ext.getCmp('isLock').setValue(responseObj.data['isLock']+"");
							Ext.getCmp('tellerId').setDisabled(true);
							Ext.getCmp('id').setValue(id);
							var h = Ext.getBody().getHeight();
							window.setHeight(h);
							window.show();
						},
						failure: function(result,options){
							var responseObj =  Ext.decode(result.responseText);
							Ext.MessageBox.alert('提示',responseObj.appMsg);
						}
    	            	});
	                }
	            },'-',{
	            	width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	                	var rec = grid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
    	            	Ext.Ajax.request({url:'../../sys/thirdmall/lockTeller.action',  params : {tellerId:id},method:'post',type:'json',  success:function(result, request){
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
	                },
		            getClass: function(v, meta, rec) {
		                if (rec.get('isLock')) {
		                    return 'icon-lock';
		                } else {
		                    return 'icon-lock_open';
		                }
		            },
		            getTip: function (v, meta, rec) {
		            	if (rec.get('isLock')) {
		                    return '解锁';
		                } else {
		                	return '锁定';
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
	                 url: '../../sys/thirdmall/addTeller.action',
	                 labelWidth:30, 
	                 //['id','tellerName','tellerId','lastLogin','isLock','createdDate'
			         items:[
						{  
						    id:'id',
						    name:'id',
						    labelWidth:100,  
						    xtype:'hiddenfield',  
						},
						{  
						  id:'tellerName',
						  name:'tellerName',
						  allowBlank:false,
						  blankText:'员工名称',
						  labelWidth:100,  
						  xtype:'textfield',  
						  fieldLabel:'员工名称',  
						  anchor:'90%'  
						},
						{  
						  id:'tellerId',
						  name:'tellerId',
						  allowBlank:false,
						  blankText:'员工编号',
						  labelWidth:100,  
						  xtype:'textfield',  
						  fieldLabel:'员工编号',  
						  anchor:'90%'  
						},
						{  
							id:'password',
							name:'password',
							blankText:'密码',
							labelWidth:100,  
							xtype:'textfield',  
							inputType: 'password',    //密码
							fieldLabel:'密码',  
							anchor:'90%'  
						},
						{	  
			        	  forceSelection: true,
			        	  xtype: 'combobox',
			        	  fieldLabel:'是否锁定',
			        	  labelWidth: 100,
			        	  anchor: '100%',
			  			  editable:false,
			  			  store: LockStore,
			  			  queryMode: 'local',
			  	    	  displayField: 'name',
			  	    	  valueField: 'id',
			  	    	  id:'isLock',
			  	    	  name:'isLock',
			  	    	  value:'false'
					      }
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