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
        fields: ['id','goodsName','price','costPrice','units','ratio','discountOff','returnRatio','createdDate','storeNumber','condition','rtnRatio','rtnRatio1','specialRatio','priceFlag'
        ],
        idProperty: 'id'
    });
    // create the Data Store0-不需要,1-需要
	
    var statusStore = Ext.create('Ext.data.Store', {
		 fields: ['code', 'name'],
		 data : [
		 {"code":1,"name":"计件计算"},
		 {"code":0,"name":"输入计算"}
		 ]
		});		
   
    
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
            url: '../../sys/thirdmall/thirdMallGoodsListByPage.action',
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
               var proxy  = store.getProxy(), reader = proxy.getReader(),raw  = reader.rawData;
               var  value=eval("("+raw.message+")");
               if(value.userType!='B0001'){
	               grid.columns[6].hide();
	               grid.columns[7].hide();
	               grid.columns[8].hide();
	               grid.columns[9].hide();
	               grid.columns[10].hide();
               }
               if(value.userType!='C0001'){
            	   createButton.hide();
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
    		paras.goodsName = encodeURIComponent(Ext.getCmp("goodsNameSearch").getValue());
    		paras.proxyId = encodeURIComponent(Ext.getCmp("parentId").getValue());
    		paras.storeId = encodeURIComponent(Ext.getCmp("sonId").getValue());
    		store.load();
		}
    });
    
    
    var createButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'add', iconCls:'add',text: '增加',
    	handler: function() {
			Ext.Ajax.request({url:'../../sys/thirdmall/queryMallGoods.action',  params : {},method:'post',type:'json',  success:function(result, request){
				var responseObj =  Ext.decode(result.responseText);
				if(responseObj.success!=null&&!responseObj.success){
					Ext.MessageBox.alert("错误提示",responseObj.appMsg);
					return;
				}
				var form = Ext.getCmp('dictionaryform');
				form.getForm().reset();
				if(responseObj.data['userType']=="B0001"){
					Ext.getCmp('ratio').setVisible(true);
					Ext.getCmp('rtnRatio').setVisible(true);
					Ext.getCmp('condition').setVisible(true);
					Ext.getCmp('rtnRatio1').setVisible(true);
					Ext.getCmp('specialRatio').setVisible(true);
					Ext.getCmp('discountOff').setVisible(true);
				}else{
					Ext.getCmp('ratio').setVisible(false);
					Ext.getCmp('rtnRatio').setVisible(false);
					Ext.getCmp('condition').setVisible(false);
					Ext.getCmp('rtnRatio1').setVisible(false);
					Ext.getCmp('specialRatio').setVisible(false);
					Ext.getCmp('discountOff').setVisible(false);
				}
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
			    {width:180,labelWidth:65,name:'goodsName',id:'goodsNameSearch',xtype:'textfield',fieldLabel:'商品名称'},
		    	queryButton
	    ]
	});
	
	/*Ext.define('proxyModel', {
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
                 mallStore.load();
            },
            beforeload: function (proxy, params) {
            	paras.userId = encodeURIComponent(Ext.getCmp("saveproxyId").getValue());
               	this.proxy.extraParams = paras;
            }
            
         }
	});*/
	
	
	/*Ext.define('MallModel', {
        extend: 'Ext.data.Model',
        fields: ['id','storeName'
        ],
        idProperty: 'id'
    });
	
	
	var mallStore = Ext.create('Ext.data.Store', {
	    model:'MallModel',
	    autoLoad:true,
		proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/user/thirdMallListByPage2.action',
            reader: {
                root: 'root',
                totalProperty: 'totalCount'
            }
        },
        listeners: {
        	load: function (proxy, params) {
                 store.load();
            },
            beforeload: function (proxy, params) {
            	paras.userId=Ext.getCmp("saveproxyId").getValue();
               	this.proxy.extraParams = paras;
            }
         }
	});*/
	
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '商品列表',
        columnLines:true,
        store: store,
        loadMask:true,
        disableSelection: false,
        tbar:[tbar],
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
        columns:[
        	{text: '商品名称',
            dataIndex: 'goodsName',
            flex: 1,
            sortable: true
        	},
        	{text: '价格',
	            dataIndex: 'price',
	            flex: 1,
	            sortable: true
        	},
            {text: '单位',
            	dataIndex: 'units',
            	flex: 1,
            	sortable: true
            },
            {text: '结算方式',
            	dataIndex: 'priceFlag',
            	flex: 1,
            	sortable: true
            },
            {text: '库存',
            	dataIndex: 'storeNumber',
            	flex: 1,
            	sortable: true
            },
            {text: '创建时间',
                dataIndex: 'createdDate',
                flex: 1,
                sortable: true
            },
            {text: '配比减率',
            	dataIndex: 'ratio',
            	flex: 1,
            	sortable: true
            },
            {text: '折扣率',
            	dataIndex: 'discountOff',
            	flex: 1,
            	sortable: true
            },
            {text: '条件数',
            	dataIndex: 'condition',
            	flex: 1,
            	sortable: true
            },
            {text: '大于条件数返点率',
            	dataIndex: 'rtnRatio',
            	flex: 1,
            	sortable: true
            },
            {text: '小于条件数返点率',
            	dataIndex: 'rtnRatio1',
            	flex: 1,
            	sortable: true
            },
            {text: '专项补贴率',
            	dataIndex: 'specialRatio',
            	flex: 1,
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
    	            	Ext.Ajax.request({url:'../../sys/thirdmall/queryMallGoods.action',  params : {mallGoodsId:id},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							var form = Ext.getCmp('dictionaryform');
							form.getForm().reset();
							Ext.getCmp('goodsName').setValue(responseObj.data['goodsName']);
							Ext.getCmp('price').setValue(responseObj.data['price']);
							Ext.getCmp('units').setValue(responseObj.data['units']);
							Ext.getCmp('storedNumber').setValue(responseObj.data['storedNumber']);
							Ext.getCmp('priceFlag').setValue(responseObj.data['priceFlag']);
							Ext.getCmp('goodId').setValue(id);
							if(responseObj.data['userType']=="B0001"){
								Ext.getCmp('ratio').setValue(responseObj.data['ratio']);
								Ext.getCmp('rtnRatio').setValue(responseObj.data['rtnRatio']);
								Ext.getCmp('condition').setValue(responseObj.data['condition']);
								Ext.getCmp('rtnRatio1').setValue(responseObj.data['rtnRatio1']);
								Ext.getCmp('specialRatio').setValue(responseObj.data['specialRatio']);
								Ext.getCmp('discountOff').setValue(responseObj.data['discountOff']);
							}else{
								Ext.getCmp('ratio').setVisible(false);
								Ext.getCmp('rtnRatio').setVisible(false);
								Ext.getCmp('condition').setVisible(false);
								Ext.getCmp('rtnRatio1').setVisible(false);
								Ext.getCmp('specialRatio').setVisible(false);
								Ext.getCmp('discountOff').setVisible(false);
							}
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
	            },{
	            	iconCls:'del',
	                tooltip: 'del',
	                altText:'del',
	                width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	            		var rec = grid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
	            		Ext.Msg.confirm('系统提示','确定要删除吗？',
    			        function(btn){
        			        if(btn=='yes'){
        			        	Ext.Ajax.request({url:'../../sys/thirdmall/delGoods.action',  params : {mallGoodId:id},method:'post',type:'json',  success:function(result, request){
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
        						}
            	            	});							
        			        }else{
        			          
        			        }
        			      },this);
    	            	
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
	                 url: '../../sys/thirdmall/addGoods.action',
	                 labelWidth:30, 
			         items:[
						{  
						    id:'goodId',
						    name:'goodId',
						    labelWidth:100,  
						    xtype:'hiddenfield',  
						},
						{  
						  id:'goodsName',
						  name:'goodsName',
						  allowBlank:false,
						  blankText:'商品名称',
						  labelWidth:100,  
						  xtype:'textfield',  
						  fieldLabel:'商品名称',  
						  anchor:'90%'  
						},
						{	  
				        	  forceSelection: true,
				        	  xtype: 'combobox',
				        	  fieldLabel:'结算方式',
				        	  labelWidth: 120,
				        	  anchor: '100%',
				  			  editable:false,
				  			  store: statusStore,
				  			  queryMode: 'local',
				  	    	  displayField: 'name',
				  	    	  valueField: 'code',
				  	    	  id:'priceFlag',
				  	    	  name:'priceFlag',
				  	    	  value:0
				          },
						{  
						  id:'price',
						  name:'price',
						  allowBlank:false,
						  blankText:'商品价格',
						  labelWidth:100,  
						  xtype:'numberfield',  
		                  decimalPrecision: 2,
		                  minValue : 0,
						  fieldLabel:'商品价格',  
						  anchor:'90%'  
						},
						{  
						  id:'units',
						  name:'units',
						  allowBlank:false,
						  blankText:'单位',
						  labelWidth:100,  
						  xtype:'textfield',  
						  fieldLabel:'单位',  
						  anchor:'90%'  
						},
						{  
							id:'storedNumber',
							name:'storedNumber',
							allowBlank:false,
							blankText:'库存',
							labelWidth:100,  
							xtype:'numberfield',  
							fieldLabel:'库存', 
							minValue : 0,
							anchor:'90%'  
						},
						{  
							id:'discountOff',
							name:'discountOff',
							blankText:'折扣率',
							labelWidth:100,  
							xtype:'numberfield',  
			                decimalPrecision: 2, 
			                minValue : 0,
			                maxValue : 1,
							fieldLabel:'折扣率',  
							anchor:'90%'  
						},
						{  
							id:'ratio',
							name:'ratio',
							blankText:'配比率',
							labelWidth:100,  
							xtype:'numberfield',  
			                decimalPrecision: 2, 
			                minValue : 0,
			                maxValue : 1,
							fieldLabel:'配比率',  
							anchor:'90%'  
						},
						{  
							id:'condition',
							name:'condition',
							blankText:'补贴率',
							labelWidth:100,  
							xtype:'numberfield',  
			                decimalPrecision: 2, 
			                minValue : 0,
							fieldLabel:'条件数',  
							anchor:'90%'  
						},
						{  
							id:'rtnRatio',
							name:'rtnRatio',
							blankText:'大于条件数返点率',
							labelWidth:100,  
							xtype:'numberfield',  
			                decimalPrecision: 2, 
			                minValue : 0,
			                maxValue : 1,
							fieldLabel:'大于条件数返点率',  
							anchor:'90%'  
						},
						{  
							id:'rtnRatio1',
							name:'rtnRatio1',
							blankText:'小于条件数返点率',
							labelWidth:100,  
							xtype:'numberfield',  
			                decimalPrecision: 2, 
			                minValue : 0,
			                maxValue : 1,
							fieldLabel:'小于条件数返点率',  
							anchor:'90%'  
						},
						{  
							id:'specialRatio',
							name:'specialRatio',
							blankText:'专项补贴率',
							labelWidth:100,  
							xtype:'numberfield',  
			                decimalPrecision: 2, 
			                minValue : 0,
			                maxValue : 1,
							fieldLabel:'专项补贴率',  
							anchor:'90%'  
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
						            	  	//location="javascript:location.reload()";
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
	      ] 
	  }); 
  
});