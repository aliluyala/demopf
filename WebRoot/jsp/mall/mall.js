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
        fields: ['id','storeName','storeType','address','description','createdDate','realName','userName','phone','real','isFirstRet','retRadio'
        ],
        idProperty: 'id'
    });
    // create the Data Store
	
	var pageSize = 40;
    var store = Ext.create('Ext.data.Store', {
        pageSize: pageSize,
        model: 'dataModel',
        remoteSort: false,
        autoLoad:false,
        proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/thirdmall/thirdMallListByPage.action',
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
            	paras.userId = encodeURIComponent(Ext.getCmp("saveproxyId").getValue());
            	Ext.getCmp('query').setDisabled(true);
               	this.proxy.extraParams = paras;
            }
         }
    });
				
    var queryButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'query', iconCls:'zoom',text: '查 询',
    	handler: function() {
    		paras.realName = encodeURIComponent(Ext.getCmp("realNameSearch").getValue());
    		paras.storeName = encodeURIComponent(Ext.getCmp("storeNameSearch").getValue());
    		paras.userId = encodeURIComponent(Ext.getCmp("saveproxyId").getValue());
    		store.load();
		}
    });
    
    var StoreType = Ext.create('Ext.data.Store', {
		 fields: ['id', 'name'],
		 data : [
		 {"id":"1","name":"加油站"},
		 {"id":"2","name":"住宿"},
		 {"id":"3","name":"美食"},
		 {"id":"4","name":"汽车维修"},
		 {"id":"5","name":"商店"},
		 ]
		});		
    
    var isReal = Ext.create('Ext.data.Store', {
		 fields: ['id', 'name'],
		 data : [
		 {"id":true,"name":"真实"},
		 {"id":false,"name":"测试"}
		 ]
		});		
    var isFirstRet = Ext.create('Ext.data.Store', {
    	fields: ['id', 'name'],
    	data : [
    	        {"id":true,"name":"是"},
    	        {"id":false,"name":"否"}
    	        ]
    });		
              
    
    var createButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'add', iconCls:'add',text: '增加',
    	handler: function() {
	    	var form = Ext.getCmp('dictionaryform');
			form.getForm().reset();
			Ext.getCmp('userName').setDisabled(false);
			var h = Ext.getBody().getHeight();
			window.setHeight(h);
			window.show();
		}
    });
    
    Ext.define('proxyModel', {
        extend: 'Ext.data.Model',
        fields: ['id','proxyName'
        ],
        idProperty: 'id'
    });
	
	
	var proxyStore = Ext.create('Ext.data.Store', {
	    model:'proxyModel',
	    autoLoad:true,
		proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/user/listProxy2.action',
            reader: {
                root: 'root',
                totalProperty: 'totalCount'
            }
        },
        listeners: {
        	load: function (proxy, params) {
        		 var proxy  = proxyStore.getProxy(), reader = proxy.getReader(),raw  = reader.rawData;
                 //alert(reader.getMessage(raw));
                 var  value=eval("("+raw.message+")");
                 if(value.userType=='B0001'){
                	 Ext.getCmp("saveproxyId").setValue(value.userId);
                 }else{
                	 Ext.getCmp("saveproxyId").hide();
                 }
                 if(value.userType!='A0001'){
                	 createButton.hide();
                 }
                 store.load();
            }
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
		        	  editable : true,//这项必须为true
		        	  queryParam : 'proxyName',
		  			  store: proxyStore,
		  			  //multiSelect:true,
		  			  queryMode: 'local',
		  	    	  displayField: 'proxyName',
		  	    	  valueField: 'id',
		  	    	  id:'saveproxyId',
		  	    	  name:'proxyId'
		          },
			    {width:180,labelWidth:60,name:'storeName',id:'storeNameSearch',xtype:'textfield',fieldLabel:'店名称'},
			    {width:180,labelWidth:75,name:'realName',id:'realNameSearch',xtype:'textfield',fieldLabel:'管理员名称'},
		    	queryButton
	    ]
	});
	
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '运的易商户列表',
        columnLines:true,
        store: store,
        loadMask:true,
        disableSelection: false,
        tbar:[tbar],
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
        columns:[
        	{text: '商店名称',
            dataIndex: 'storeName',
            flex: 1,
            sortable: true
        	},
        	{text: '管理员名称',
	            dataIndex: 'realName',
	            flex: 1,
	            sortable: true
        	},
        	{text: '管理员登录名',
                dataIndex: 'userName',
                flex: 1,
                sortable: true
            },
        	{text: '商店类型',
                dataIndex: 'storeType',
                flex: 1,
                sortable: true, 
                renderer: function (value, meta, record) {
            		if(value=='1'){
            			return "加油站";
            		}else if(value=='2'){
            			return "住宿";
            		}else if(value=='3'){
            			return "美食";
            		}else if(value=='4'){
            			return "汽车维修";
            		}else if(value=='5'){
            			return "餐饮";
            		}else if(value=='6'){
            			return "汽配商家";
            		}else{
            			return "未知";  
            		}
            	}
            },
            {text: '创建时间',
                dataIndex: 'createdDate',
                flex: 1,
                sortable: true
            },
            {text: '电话',
                dataIndex: 'phone',
                flex: 1,
                sortable: true
            },
            {text: '真实测试',
                dataIndex: 'real',
                flex: 1,
                sortable: true,
                renderer: function (value, meta, record) {
            		if(value==true){
            			return "<span style='color:red;font-weight:bold;'>真实</span>";
            		}else{
            			return "测试";  
            		}
            	}
            },
            {text: '是否首单返',
                dataIndex: 'isFirstRet',
                flex: 1,
                sortable: true,
                renderer: function (value, meta, record) {
            		if(value==true){
            			return "<span style='color:red;font-weight:bold;'>是</span>";
            		}else{
            			return "否";  
            		}
            	}
            },
            {text: '首单返比率',
                dataIndex: 'retRadio',
                flex:6,
                sortable: true
            },
            {text: '说明',
                dataIndex: 'description',
                flex:6,
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
    	            	Ext.Ajax.request({url:'../../sys/thirdmall/queryMall.action',  params : {mallId:id},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							var form = Ext.getCmp('dictionaryform');
							form.getForm().reset();
							Ext.getCmp('storeName').setValue(responseObj.data['storeName']);
							Ext.getCmp('realName').setValue(responseObj.data['realName']);
							Ext.getCmp('userName').setValue(responseObj.data['userName']);
							Ext.getCmp('address').setValue(responseObj.data['address']);
							Ext.getCmp('storeType').setValue(responseObj.data['storeType']+"");
							Ext.getCmp('description').setValue(responseObj.data['description']);
							Ext.getCmp('longitude').setValue(responseObj.data['longitude']);
							Ext.getCmp('latitude').setValue(responseObj.data['latitude']);
							Ext.getCmp('phone').setValue(responseObj.data['phone']);
							Ext.getCmp('real').setValue(responseObj.data['real']);
							Ext.getCmp('userName').setDisabled(true);
							Ext.getCmp('mallId').setValue(id);
							Ext.getCmp('isFirstRet').setValue(responseObj.data['isFirstRet']);
							Ext.getCmp('retRadio').setValue(responseObj.data['retRadio']);
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
	var window = new Ext.Window({  
	      title:'新增',  
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
		         }
				  ,
				  show: function() {
				 	 Ext.getBody().mask();
				  }
		   },
	      items:[{  
	          layout:'form',  
	          id:'dictionaryform',
	          baseCls:'x-plain',  
	          xtype:'form',
	          style:'padding:8px;',  
	          url: '../../sys/thirdmall/addMall.action',
	          labelWidth:30,  
	          items:[
				{  
						    id:'mallId',
						    name:'mallId',
						    labelWidth:100,  
						    xtype:'hiddenfield',  
						},
						{  
						  id:'storeName',
						  name:'storeName',
						  allowBlank:false,
						  blankText:'商店名称',
						  labelWidth:100,  
						  xtype:'textfield',  
						  fieldLabel:'商店名称',  
						  anchor:'90%'  
						},
						{  
						  id:'userName',
						  name:'userName',
						  allowBlank:false,
						  labelWidth:100,  
						  xtype:'textfield',  
						  fieldLabel:'管理员登录名',  
						  anchor:'90%'  
						},
						{  
						  id:'password',
						  name:'password',
						  labelWidth:100,  
						  xtype:'textfield',  
						  inputType: 'password',    //密码
						  fieldLabel:'登录密码',  
						  anchor:'90%'  
						},
						{  
						  id:'realName',
						  name:'realName',
						  allowBlank:false,
						  blankText:'真实名称',
						  labelWidth:100,  
						  xtype:'textfield',  
						  fieldLabel:'管理员真实名称',  
						  anchor:'90%'  
						},
						{	  
			        	  forceSelection: true,
			        	  xtype: 'combobox',
			        	  fieldLabel:'商店类型',
			        	  labelWidth: 100,
			        	  anchor: '100%',
			  			  editable:false,
			  			  store: StoreType,
			  			  queryMode: 'local',
			  	    	  displayField: 'name',
			  	    	  valueField: 'id',
			  	    	  id:'storeType',
			  	    	  name:'storeType',
			  	    		  value:'1'
				          },
				          {  
				        	  id:'address',
				        	  name:'address',
				        	  allowBlank:false,
				        	  blankText:'地址',
				        	  xtype:'textfield',  
				        	  fieldLabel:'地址',  
				        	  anchor:'90%' ,
				        	  flex: 2,
				        	},{  
				        	    layout:'hbox',
				        	    baseCls:'x-plain',  
				        	    items: [  
				        	        {  
							           	  id:'longitude',
							           	  name:'longitude',
							           	  allowBlank:false,
						                  xtype:'numberfield',  
						                  decimalPrecision: 8,
						                  fieldLabel:'经度',
						                  maxValue:136,//最大值 
						                  minValue:73, //最小值 
						                  flex      : 6  
							       	  },  
				        	        {  
				        	            xtype     : 'button',  
				        	            text: '取经纬度',  
				        	            flex      : 1 ,
				        	            handler: function () {
				        	            	var h = Ext.getBody().getHeight();
											windowMap.setHeight(h);
											windowMap.show();
				    			        }
				        	        }] 
				        	}
				        	 ,
				       	 {  
				           	  id:'latitude',
				           	  name:'latitude',
				           	  labelWidth:70,
				           	  width : 55,
				           	  allowBlank:false,
			                  xtype:'numberfield',  
			                  decimalPrecision: 8,
			                  maxValue:54,//最大值 
			                  minValue:18, //最小值 
			                  fieldLabel:'纬度'
				       	  },
				       	{  
				           	  id:'phone',
				           	  name:'phone',
				           	  labelWidth:70,
				           	  width : 55,
				           	  allowBlank:false,
			                  xtype:'textfield',  
			                  decimalPrecision: 8,
			                  fieldLabel:'电话号码'
				       	  },
				       	{	  
				        	  forceSelection: true,
				        	  xtype: 'combobox',
				        	  fieldLabel:'商店类型',
				        	  labelWidth: 100,
				        	  anchor: '100%',
				  			  editable:false,
				  			  store: isReal,
				  			  queryMode: 'local',
				  	    	  displayField: 'name',
				  	    	  valueField: 'id',
				  	    	  id:'real',
				  	    	  name:'isReal',
				  	    	  value:true
					     },
					     {	  
					    	 forceSelection: true,
					    	 xtype: 'combobox',
					    	 fieldLabel:'是否首单返',
					    	 labelWidth: 100,
					    	 anchor: '100%',
					    	 editable:false,
					    	 store: isFirstRet,
					    	 queryMode: 'local',
					    	 displayField: 'name',
					    	 valueField: 'id',
					    	 id:'isFirstRet',
					    	 name:'isFirstRet',
					    	 value:false
					     },
					     {  
								id:'retRadio',
								name:'retRadio',
								blankText:'首单返比率',
								labelWidth:100,  
								xtype:'numberfield',  
				                decimalPrecision: 2, 
				                minValue : 0,
				                maxValue : 1,
								fieldLabel:'首单返比率',  
								anchor:'90%',
								value:1
							},
						{  
						  id:'description',
						  name:'description',
						  blankText:'备注',
						  allowBlank:true,
						  labelWidth:100,  
						  xtype:'textfield',  
						  fieldLabel:'备注',  
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
	
	
	
    
	var windowMap = new Ext.Window({  
	      title:'取经纬度',  
	      width:800,  
	      height:600,  
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
				  },
				  afterrender:function(){
				  }
		   },
	      items:[{
			    xtype: 'panel',
			    html : '<iframe scrolling="auto" id="mainFrame" frameborder="0" width="100%" height="100%" src="map.jsp"></iframe>',
			    width: '100%',
			    flex: 10
			}
	      ],
		   buttonAlign:'center',
		      buttons:[{  
		          	  text:'确定',
		          	  formBind: true,
		        	  handler: function() {
		        		  var child = document.getElementById("mainFrame").contentWindow;
		        		  Ext.getCmp("longitude").setValue(child.document.getElementById("lnglat").value.split(",")[0]);
		        		  Ext.getCmp("latitude").setValue(child.document.getElementById("lnglat").value.split(",")[1]);
		        		  windowMap.close();
		          	  }
		      },{  
		          	  text:'取消',
		        	  handler: function() {
		        		  windowMap.close();
		    		}
		      }]
	  }); 
});