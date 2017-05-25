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
	Ext.define('Area', {
        extend: 'Ext.data.Model',
        fields: ['id','name']
    });
	
	//专线------------------------------------------------------------------------------------------------------
	Ext.define('DataModel', {
        extend: 'Ext.data.Model',
        fields: ['src','desc','phone']
    });
	
	var freightLineParas={};
	var freightStore = Ext.create('Ext.data.Store', {
		pageSize: pageSize,
		model: 'DataModel',
	    remoteSort: false,
        autoLoad:false,
        proxy:  {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/member/freightLineList.action',
            reader: {
                root: 'root',
                totalProperty: 'totalCount'
            }
        },
        listeners: {
            load: function () {
               if(this.getCount() == 0)
               {
            	}
            },
        	beforeload: function (proxy, params) {
               	this.proxy.extraParams = freightLineParas;
            }
         }
	});
	
	var freightTbar = Ext.create("Ext.Toolbar", {
		flex:1,
	    items: ['-',
	            {
			        iconCls:'refush',
			        text:'刷新',
			        xtype: 'button',
			        handler: function () {
			        	freightStore.load();
			        }
	            },'-',{
			        iconCls:'refush',
			        text:'新增专线',
			        xtype: 'button',
			        handler: function () {
			        	var h = Ext.getBody().getHeight();
			        	addFreight.setHeight(h);
			        	addFreight.show();
			        }
	            },'-'
	    ]
	});
	
    var freightLineGrid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        flex:2,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '专线列表',
        tbar:[freightTbar],
        columnLines:true,
        store: freightStore,
        loadMask:true,
        disableSelection: false,
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
        columns:[
        	{text: '出发地',
            dataIndex: 'src',
            flex: 1.5,
            sortable: true
        	},
        	{text: '目的地',
                dataIndex: 'desc',
                flex: 1.5,
                sortable: true
            },{text: '电话',
                dataIndex: 'phone',
                flex: 3,
                sortable: true
            },{
	            xtype:'actioncolumn', 
	            flex: 3,
	            header: '删除',
	            //align:'center',
	            items: ['-',{
	            	iconCls:'del',
	            	tooltip: '删除',
	                width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	                	var rec = freightLineGrid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
	            		Ext.Msg.confirm('系统提示','确定要删除吗？',function(btn){
	            			if(btn=='yes'){
		    	            	Ext.Ajax.request({url:'../../sys/member/deleteFreightLine.action',  params : {id:id},method:'post',type:'json',  success:function(result, request){
									var responseObj =  Ext.decode(result.responseText);
									if(responseObj.success!=null&&!responseObj.success){
										Ext.MessageBox.alert("错误提示",responseObj.appMsg);
										return;
									}
									freightStore.reload();
								},
								failure: function(result,options){
									var responseObj =  Ext.decode(result.responseText);
									Ext.MessageBox.alert('提示',responseObj.appMsg);
								}});
	            			}
	            		});
	                }
	            }]
			 }
        	],
        forceFit: true
    });
	
    //落货-------------------------------------------------
    
	Ext.define('WarehouseModel', {
        extend: 'Ext.data.Model',
        fields: ['place','phone','address','contact']
    });
	
	var warehouseParas={};
	var warehouseStore = Ext.create('Ext.data.Store', {
		pageSize: pageSize,
		model: 'WarehouseModel',
	    remoteSort: false,
        autoLoad:false,
        proxy:  {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/member/warehouseList.action',
            reader: {
                root: 'root',
                totalProperty: 'totalCount'
            }
        },
        listeners: {
            load: function () {
               if(this.getCount() == 0)
               {
            	}
            },
        	beforeload: function (proxy, params) {
               	this.proxy.extraParams = warehouseParas;
            }
         }
	});
	
	var warehouseTbar = Ext.create("Ext.Toolbar", {
		flex:1,
	    items: ['-',
	            {
			        iconCls:'refush',
			        text:'刷新',
			        xtype: 'button',
			        handler: function () {
			        	warehouseStore.load();
			        }
	            },'-',{
			        iconCls:'refush',
			        text:'新增落货地',
			        xtype: 'button',
			        handler: function () {
			        	var h = Ext.getBody().getHeight();
			        	addWarehouse.setHeight(h);
			        	addWarehouse.show();
			        }
	            },'-'
	    ]
	});
	
    var addwarehouseGrid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        flex:2,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '落货地列表',
        tbar:[warehouseTbar],
        columnLines:true,
        store: warehouseStore,
        loadMask:true,
        disableSelection: false,
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
        columns:[
        	{text: '联系人',
            dataIndex: 'contact',
            flex: 1,
            sortable: true
        	},
        	{text: '电话',
                dataIndex: 'phone',
                flex: 1,
                sortable: true
            },
            {text: '所在地市',
                dataIndex: 'place',
                flex: 1,
                sortable: true
            },
            {text: '详细地址',
                dataIndex: 'address',
                flex: 1,
                sortable: true
            },{
	            xtype:'actioncolumn', 
	            flex: 1,
	            header: '删除|编辑',
	            //align:'center',
	            items: ['-',{
	            	iconCls:'del',
	            	tooltip: '删除',
	                width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	                	var rec = addwarehouseGrid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
	            		Ext.Msg.confirm('系统提示','确定要删除吗？',function(btn){
	            			if(btn=='yes'){
		    	            	Ext.Ajax.request({url:'../../sys/member/deleteWarehouse.action',  params : {id:id},method:'post',type:'json',  success:function(result, request){
									var responseObj =  Ext.decode(result.responseText);
									if(responseObj.success!=null&&!responseObj.success){
										Ext.MessageBox.alert("错误提示",responseObj.appMsg);
										return;
									}
									warehouseStore.reload();
								},
								failure: function(result,options){
									var responseObj =  Ext.decode(result.responseText);
									Ext.MessageBox.alert('提示',responseObj.appMsg);
								}});
	            			}
	            		});
	                }
	            },'-',{
	            	iconCls:'edit',
	            	tooltip: '编辑',
	                width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	                	var rec = addwarehouseGrid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
    	            	Ext.Ajax.request({url:'../../sys/member/findHouseInfo.action',  params : {houseId:id},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							var form = Ext.getCmp('addForm4');
							form.getForm().reset();
							Ext.getCmp('houseId4').setValue(id);
							Ext.getCmp('usreId4').setValue(responseObj.data['userId']);
							Ext.getCmp('sonId4').setValue(responseObj.data['place']);
							Ext.getCmp('contactb').setValue(responseObj.data['contact']);
							Ext.getCmp('phoneb3').setValue(responseObj.data['phone']);
							Ext.getCmp('addressb').setValue(responseObj.data['address']);
							var h = Ext.getBody().getHeight();
							editWarehouse.setHeight(h);
							editWarehouse.show();
						},
						failure: function(result,options){
							var responseObj =  Ext.decode(result.responseText);
							Ext.MessageBox.alert('提示',responseObj.appMsg);
						}});
	                	
	                }
	            }]
			 }
        	],
        forceFit: true
    });
    
    //专线新增
    var parentStore = Ext.create('Ext.data.Store', {
	    model:'Area',
	    autoLoad:true,
		proxy: {
   		timeout: 120000,//120s
           method:'POST',
       	type: 'ajax',
           url: '../../dictionary/getProvince.action',
           reader: {
               root: 'root'
           }
       }
	});
	
	var paras1={};
	var sonStore = Ext.create('Ext.data.Store', {
	    model:'Area',
	    autoLoad:false,
		proxy: {
   		timeout: 120000,//120s
           method:'POST',
       	type: 'ajax',
           url: '../../dictionary/getCity.action',
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
	 //专线新增
    var parentStore2 = Ext.create('Ext.data.Store', {
	    model:'Area',
	    autoLoad:true,
		proxy: {
   		timeout: 120000,//120s
           method:'POST',
       	type: 'ajax',
           url: '../../dictionary/getProvince.action',
           reader: {
               root: 'root'
           }
       }
	});
	
	var paras2={};
	var sonStore2 = Ext.create('Ext.data.Store', {
	    model:'Area',
	    autoLoad:false,
		proxy: {
   		timeout: 120000,//120s
           method:'POST',
       	type: 'ajax',
           url: '../../dictionary/getCity.action',
           reader: {
               root: 'root'
           }
       },
       listeners: {
       	beforeload: function (proxy, params) {
              	this.proxy.extraParams = paras2;
           }
        }
	});
    var addFreight = new Ext.Window({  
        title:'新建专线',  
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
            id:'addForm',
            baseCls:'x-plain',  
            xtype:'form',
            style:'padding:8px;',  
            url: '../../sys/member/addFreightLine.action',
            labelWidth:30,  
            items:[
						{  
						    id:'userId',
						    name:'userId',
						    labelWidth:40,  
						    xtype:'hiddenfield'
						},
						{

						    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
						    xtype:'fieldset',
						    title: '联系方式',
						    collapsible: true,
						    layout:'form',
						    labelWidth:80,  
						    items :[
								{
								      xtype: 'textfield',
								      labelWidth:60,  
								      fieldLabel: '电话',
								      name: 'phone',
								      allowBlank: false,
								      id:'addFremobile2',
								      emptyText:"必需填写",
								  }
							]
						},
						{
						    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
						    xtype:'fieldset',
						    title: '出发地',
						    collapsible: true,
						    layout:'form',
						    labelWidth:80,  
						    items :[{
						      	  forceSelection: true,
					        	  xtype: 'combobox',
					        	  fieldLabel:'省份',
					        	  labelWidth: 60,
					        	  anchor: '100%',
					  			  editable:false,
					  			  store: parentStore,
					  			  emptyText :'请选择...',
					  			  queryMode: 'local',
					  	    	  displayField: 'name',
					  	    	  valueField: 'id',
					  	    	  allowBlank: false,
					  	    	  editable : true,//这项必须为true
					        	 // queryParam : 'proxyName',
					  	    	  id:'parentId',
					  	    	  name:'parentId',
					  	    	  listeners:{  
									  select : function(combo, record,index){
										paras1.provinceId = record[0].data.id;
										sonStore.removeAll();
										Ext.getCmp("sonId").setValue("");
										sonStore.load();
									  }
								  }
					          },
					          {	  
					        	  xtype: 'combobox',
					        	  fieldLabel:'市区',
					        	  labelWidth: 60,
					        	  anchor: '100%',
					  			  editable:true,
					  			  allowBlank: false,
					  			  store: sonStore,
					  			  queryMode: 'local',
					  			  editable : true,//这项必须为true
					        	  //queryParam : 'storeName',
					  	    	  displayField: 'name',
					  	    	  valueField: 'name',
					  	    	  id:'sonId',
					  	    	  name:'sonId',
					  	    	  listeners:{  
									  select : function(combo, record,index){
										  //paras.storeId = record[0].data.id;
										  //store.load();
									  }
								  }
					          }
							]
						      },
						      {
								    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
								    xtype:'fieldset',
								    title: '目的地',
								    collapsible: true,
								    layout:'form',
								    labelWidth:80,  
								    items :[{
								      	  forceSelection: true,
							        	  xtype: 'combobox',
							        	  fieldLabel:'省份',
							        	  labelWidth: 60,
							        	  anchor: '100%',
							  			  editable:false,
							  			  store: parentStore2,
							  			  emptyText :'请选择...',
							  			  queryMode: 'local',
							  	    	  displayField: 'name',
							  	    	  valueField: 'id',
							  	    	  allowBlank: false,
							  	    	  editable : true,//这项必须为true
							        	 // queryParam : 'proxyName',
							  	    	  id:'parentId1',
							  	    	  name:'parentId1',
							  	    	  listeners:{  
											  select : function(combo, record,index){
												paras2.provinceId = record[0].data.id;
												sonStore2.removeAll();
												Ext.getCmp("sonId1").setValue("");
												sonStore2.load();
											  }
										  }
							          },
							          {	  
							        	  xtype: 'combobox',
							        	  fieldLabel:'市区',
							        	  labelWidth: 60,
							        	  anchor: '100%',
							  			  editable:false,
							  			  store: sonStore2,
							  			  allowBlank: false,
							  			  queryMode: 'local',
							  			  editable : true,//这项必须为true
							        	  //queryParam : 'storeName',
							  	    	  displayField: 'name',
							  	    	  valueField: 'name',
							  	    	  id:'sonId1',
							  	    	  name:'sonId1',
							  	    	  listeners:{  
											  select : function(combo, record,index){
												  //paras.storeId = record[0].data.id;
												  //store.load();
											  }
										  }
							          }
									]
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
  	                        	freightStore.reload();
  	                             addFreight.close();
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
  	        		addFreight.close();
  	    		}
  	      }]
        }
        ]
    });  
    
    
    
    var addWarehouse = new Ext.Window({  
        title:'新建',  
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
            id:'addForm3',
            baseCls:'x-plain',  
            xtype:'form',
            style:'padding:8px;',  
            url: '../../sys/member/addWarehouse.action',
            labelWidth:30,  
            items:[
						{  
						    id:'userId3',
						    name:'userId',
						    labelWidth:40,  
						    xtype:'hiddenfield'
						},
						{
						    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
						    xtype:'fieldset',
						    title: '落货地地址',
						    collapsible: true,
						    layout:'form',
						    labelWidth:80,  
						    items :[{
						      	  forceSelection: true,
					        	  xtype: 'combobox',
					        	  fieldLabel:'省份',
					        	  labelWidth: 60,
					        	  anchor: '100%',
					  			  editable:false,
					  			  store: parentStore,
					  			  emptyText :'请选择...',
					  			  queryMode: 'local',
					  	    	  displayField: 'name',
					  	    	  valueField: 'id',
					  	    	  allowBlank: false,
					  	    	 // editable : true,//这项必须为true
					        	 // queryParam : 'proxyName',
					  	    	  id:'parentId3',
					  	    	  name:'parentId',
					  	    	  listeners:{  
									  select : function(combo, record,index){
										paras1.provinceId = record[0].data.id;
										sonStore.removeAll();
										Ext.getCmp("sonId3").setValue("");
										sonStore.load();
									  }
								  }
					          },
					          {	  
					        	  xtype: 'combobox',
					        	  fieldLabel:'市区',
					        	  labelWidth: 60,
					        	  anchor: '100%',
					  			  editable:false,
					  			  store: sonStore,
					  			  allowBlank: false,
					  			  queryMode: 'local',
					  			  editable : true,//这项必须为true
					        	  //queryParam : 'storeName',
					  	    	  displayField: 'name',
					  	    	  valueField: 'name',
					  	    	  id:'sonId3',
					  	    	  name:'sonId',
					  	    	  listeners:{  
									  select : function(combo, record,index){
										  //paras.storeId = record[0].data.id;
										  //store.load();
									  }
								  }
					          }
							]
						      },
						      
		      {
				    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
				    xtype:'fieldset',
				    title: '详情',
				    collapsible: true,
				    layout:'form',
				    labelWidth:80,  
				    items :[
						{  
						    id:'contact',
						    name:'contact',
						    xtype:'textfield',
						    allowBlank:false,
					    	labelWidth:80,
					    	regexText : '不是有效人名',
				  	        regex:/^[\u4e00-\u9fa5]{2,4}$/,
					        fieldLabel:'联系名称'
						},
						{  
						    id:'phone3',
						    name:'phone',
						    xtype:'textfield',
						    allowBlank:false,
					    	labelWidth:70,
					        fieldLabel:'联系电话',
						},
						{  
						    id:'address',
						    name:'address',
						    xtype:'textfield',
					    	allowBlank:false,
					    	labelWidth:70,
					        fieldLabel:'详细地址'
						},
						{
						    xtype: 'filefield',
						    name: 'pic1',
						    id: 'pica1',
						    fieldLabel: '实景图片1',
						    labelWidth: 100,
						    //msgTarget: 'side',
						    anchor: '100%',
						    buttonText: '实景图片1'
						},
						{
						    xtype: 'filefield',
						    name: 'pic2',
						    id: 'pica2',
						    fieldLabel: '实景图片2',
						    labelWidth: 100,
						    //msgTarget: 'side',
						    anchor: '100%',
						    buttonText: '实景图片2'
						},
						{
						    xtype: 'filefield',
						    name: 'pic3',
						    id: 'pica3',
						    fieldLabel: '实景图片3',
						    labelWidth: 100,
						    //msgTarget: 'side',
						    anchor: '100%',
						    buttonText: '实景图片3'
						},
						{
						    xtype: 'filefield',
						    name: 'pic4',
						    id: 'pica4',
						    fieldLabel: '实景图片4',
						    labelWidth: 100,
						    //msgTarget: 'side',
						    anchor: '100%',
						    buttonText: '实景图片4'
						},
						{
						    xtype: 'filefield',
						    name: 'pic5',
						    id: 'pica5',
						    fieldLabel: '实景图片5',
						    labelWidth: 100,
						    //msgTarget: 'side',
						    anchor: '100%',
						    buttonText: '实景图片5'
						}]}
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
  	                        	warehouseStore.reload();
  	                             addWarehouse.close();
  	                          },
  	                          failure: function(form, action) {
  	                              //Ext.Msg.alert('操作失败', action.result.appMsg);
  	                          }
  	                      });
  	                  }
  	          	  }
  	      },{  
  	          	  text:'取消',
  	        	  handler: function() {
  	        		addWarehouse.close();
  	    		}
  	      }]
        }
        ]
    });
    
    
    var editWarehouse = new Ext.Window({
        title:'新建',  
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
            id:'addForm4',
            baseCls:'x-plain',  
            xtype:'form',
            style:'padding:8px;',  
            url: '../../sys/member/editWarehouse.action',
            labelWidth:30,  
            items:[
						{  
						    id:'houseId4',
						    name:'houseId',
						    labelWidth:40,  
						    xtype:'hiddenfield'
						},
						{  
							id:'usreId4',
							name:'userId',
							labelWidth:40,  
							xtype:'hiddenfield'
						},
						{
						    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
						    xtype:'fieldset',
						    title: '落货地地址',
						    collapsible: true,
						    layout:'form',
						    labelWidth:80,  
						    items :[{
						      	  forceSelection: true,
					        	  xtype: 'combobox',
					        	  fieldLabel:'省份',
					        	  labelWidth: 60,
					        	  anchor: '100%',
					  			  editable:false,
					  			  store: parentStore,
					  			  emptyText :'请选择...',
					  			  queryMode: 'local',
					  	    	  displayField: 'name',
					  	    	  valueField: 'id',
					  	    	  //allowBlank: false,
					  	    	 // editable : true,//这项必须为true
					        	 // queryParam : 'proxyName',
					  	    	  id:'parentId4',
					  	    	  name:'parentId',
					  	    	  listeners:{  
									  select : function(combo, record,index){
										paras1.provinceId = record[0].data.id;
										sonStore.removeAll();
										Ext.getCmp("sonId4").setValue("");
										sonStore.load();
									  }
								  }
					          },
					          {	  
					        	  xtype: 'combobox',
					        	  fieldLabel:'市区',
					        	  labelWidth: 60,
					        	  anchor: '100%',
					  			  editable:false,
					  			  store: sonStore,
					  			  allowBlank: false,
					  			  queryMode: 'local',
					  			  editable : true,//这项必须为true
					        	  //queryParam : 'storeName',
					  	    	  displayField: 'name',
					  	    	  valueField: 'name',
					  	    	  id:'sonId4',
					  	    	  name:'sonId',
					  	    	  listeners:{  
									  select : function(combo, record,index){
										  //paras.storeId = record[0].data.id;
										  //store.load();
									  }
								  }
					          }
							]
						      },
						      
		      {
				    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
				    xtype:'fieldset',
				    title: '详情',
				    collapsible: true,
				    layout:'form',
				    labelWidth:80,  
				    items :[
						{  
						    id:'contactb',
						    name:'contact',
						    xtype:'textfield',
						    allowBlank:false,
					    	labelWidth:80,
					    	regexText : '不是有效人名',
				  	        regex:/^[\u4e00-\u9fa5]{2,4}$/,
					        fieldLabel:'联系名称'
						},
						{  
						    id:'phoneb3',
						    name:'phone',
						    xtype:'textfield',
						    allowBlank:false,
					    	labelWidth:70,
					        fieldLabel:'联系电话',
						},
						{  
						    id:'addressb',
						    name:'address',
						    xtype:'textfield',
					    	allowBlank:false,
					    	labelWidth:70,
					        fieldLabel:'详细地址'
						},
						{
						    xtype: 'filefield',
						    name: 'pic1',
						    id: 'picb1',
						    fieldLabel: '实景图片1',
						    labelWidth: 100,
						    emptyText:"如果不修改请不要提交",
						    //msgTarget: 'side',
						    anchor: '100%',
						    buttonText: '实景图片1'
						},
						{
						    xtype: 'filefield',
						    name: 'pic2',
						    id: 'picb2',
						    fieldLabel: '实景图片2',
						    labelWidth: 100,
						    emptyText:"如果不修改请不要提交",
						    //msgTarget: 'side',
						    anchor: '100%',
						    buttonText: '实景图片2'
						},
						{
						    xtype: 'filefield',
						    name: 'pic3',
						    id: 'picb3',
						    fieldLabel: '实景图片3',
						    labelWidth: 100,
						    emptyText:"如果不修改请不要提交",
						    //msgTarget: 'side',
						    anchor: '100%',
						    buttonText: '实景图片3'
						},
						{
						    xtype: 'filefield',
						    name: 'pic4',
						    id: 'picb4',
						    fieldLabel: '实景图片4',
						    labelWidth: 100,
						    emptyText:"如果不修改请不要提交",
						    //msgTarget: 'side',
						    anchor: '100%',
						    buttonText: '实景图片4'
						},
						{
						    xtype: 'filefield',
						    name: 'pic5',
						    id: 'picb5',
						    fieldLabel: '实景图片5',
						    labelWidth: 100,
						    emptyText:"如果不修改请不要提交",
						    //msgTarget: 'side',
						    anchor: '100%',
						    buttonText: '实景图片5'
						}]}
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
  	                        	warehouseStore.reload();
  	                             editWarehouse.close();
  	                          },
  	                          failure: function(form, action) {
  	                              //Ext.Msg.alert('操作失败', action.result.appMsg);
  	                          }
  	                      });
  	                  }
  	          	  }
  	      },{  
  	          	  text:'取消',
  	        	  handler: function() {
  	        		editWarehouse.close();
  	    		}
  	      }]
        }
        ]
    });
    
    //物流公司------------------------------------------------------------------------------------------------------------------------
    Ext.define('travelModel', {
        extend: 'Ext.data.Model',
        fields: ['realName','mobile','idCard','isLocked','consignorType','loginDate','loginIp','cities','proxy',
                 'address','isAuth','fax','registerTime','isPerfect','authStep','expireTime','balance','points','tellerId'
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
            url: '../../sys/member/consignorCompanyByPage.action',
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
               grid.getSelectionModel().select(0,true);
               var records = grid.getSelectionModel().getSelection();
               if(records.length!=0){
            	   Ext.getCmp("userId").setValue(records[0].data.id);
            	   Ext.getCmp("userId3").setValue(records[0].data.id);
                   freightLineParas.userId=records[0].data.id;
                   warehouseParas.userId=records[0].data.id;
                   freightStore.load();
                   warehouseStore.load();
               }else{
            	   freightLineParas.userId=-1;
            	   warehouseParas.userId=-1;
            	   freightStore.load();
                   warehouseStore.load();
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
    		paras.phone = encodeURIComponent(Ext.getCmp("phoneSearch").getValue());
    		//paras.code = encodeURIComponent(Ext.getCmp("code").getValue());
    		paras.checkStatus = Ext.getCmp("bsc").getValue();
    		//paras.date = Ext.getCmp("datetime").getValue();
    		//paras.date  = Ext.util.Format.date(Ext.getCmp("datetime").getValue(), 'Y-m-d');
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
	
	var continueStore = Ext.create('Ext.data.Store', {
		fields: ['code', 'name'],
		 data : [
		 {"code":"1","name":"一年"},
		 {"code":"2","name":"两年"},
		 {"code":"3","name":"三年"},
		 {"code":"4","name":"四年"},
		 {"code":"5","name":"五年"}
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
			        text:'续费',
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
			        	Ext.getCmp("contiuneConsignorId").setValue(records[0].data.id);
			        	var h = Ext.getBody().getHeight();
			        	continuewindow.setHeight(h);
			        	continuewindow.show();
			        }
	            },
	            '-',{
			        iconCls:'refush',
			        text:'充值',
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
			        	var balanceform = Ext.getCmp("balanceform").getForm();
			        	balanceform.reset();
			        	Ext.getCmp("balanceConsignorId").setValue(records[0].data.id);
			        	var h = Ext.getBody().getHeight();
			        	balancewindow.setHeight(h);
			        	balancewindow.show();
			        }
	            },'-',{
			        iconCls:'refush',
			        text:'退款',
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
			        	var balanceform = Ext.getCmp("minusBalanceTallyform").getForm();
			        	balanceform.reset();
			        	Ext.getCmp("minusBalanceTallyId").setValue(records[0].data.id);
			        	var h = Ext.getBody().getHeight();
			        	minusBalanceTally.setHeight(h);
			        	minusBalanceTally.show();
			        }
	            },'-',{
			        iconCls:'refush',
			        text:'新增公司',
			        xtype: 'button',
			        handler: function () {
			        	var h = Ext.getBody().getHeight();
			        	addCompanyWindow.setHeight(h);
			        	addCompanyWindow.show();
			        }
	            },
	            '-','->',
				{width:210,forceSelection: true,xtype: 'combobox',fieldLabel:'状态:',labelWidth: 50,anchor: '100%',
					store: checkStatusStore,emptyText :'请选择...',queryMode: 'local',displayField: 'name',valueField: 'code',
					id:'bsc',name:'bsc'},'-',
					{width:180,labelWidth:50,name:'titleSearch',id:'titleSearch',xtype:'textfield',fieldLabel:'名称'},'-',
					{width:180,labelWidth:50,name:'phoneSearch',id:'phoneSearch',xtype:'textfield',fieldLabel:'手机'},'-',
					queryButton
	    ]
	});
	
    //var pluginExpanded = true;
	// ['realName','mobile','idCard','isLocked','consignorType','loginDate','loginIp','cities','registrationPic',
    //'address','isAuth','fax','registerTime','isPerfect'
	var record_start = 0;
    var grid = Ext.create('Ext.grid.Panel', {
    	flex:10,
        stripeRows: true,
        autoWidth: true,
        id:'grid',
        autoExpandColumn: 'name',
        title: '物流公司列表',
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
            injectCheckbox: 1,
            mode: "single",     //
            checkOnly: false     //
        },
        selType: "checkboxmodel",
        listeners: {
            itemclick: function (me, record, item, index, e, eOpts) {
               var p = record.data;
               Ext.getCmp("userId").setValue(p.id);
               Ext.getCmp("userId3").setValue(p.id);
               freightLineParas.userId=p.id;
               warehouseParas.userId=p.id;
               freightStore.load();
               warehouseStore.load();
            }
        },
        // fields: ['id','name','email','sex','createTime','phone','address','resume','birthday','status','age','phone'
        columns:[
			new Ext.grid.RowNumberer({   header : "序号",   width : 40,
				renderer: function (value, metadata, record, rowIndex) {
					return record_start + 1 + rowIndex;
				} 
			}),
        	{text: '货主名称',
	            dataIndex: 'realName',
	            flex: 1.5,
	            sortable: true
        	},
        	{text: '手机',
                dataIndex: 'mobile',
                flex: 2,
                sortable: true
            },
        	{text: '货主身份证',
                dataIndex: 'idCard',
                flex: 2,
                sortable: true
            },{text: '注册时间',
            	dataIndex: 'registerTime',
            	flex: 2,
            	sortable: true
            },{text: '服务时间',
            	dataIndex: 'expireTime',
            	flex: 2,
            	sortable: true
            },{text: '余额',
            	dataIndex: 'balance',
            	flex: 1,
            	sortable: true
            },{text: '积分',
            	dataIndex: 'points',
            	flex: 1,
            	sortable: true
            }
        	,{text: '是否审核',
            	dataIndex: 'isAuth',
            	flex: 1,
            	sortable: true,
            	renderer: function (value, meta, record) {
            		if(value==false){
            			return "<span style='color:red;font-weight:bold;'>未审核</span>";
            		}else{
            			return "已审核";  
            		}
            	}
            },{text: '是否锁定',
            	dataIndex: 'isLocked',
            	flex: 1,
            	sortable: true,
            	renderer: function (value, meta, record) {
            		if(value==true){
            			return "<span style='color:red;font-weight:bold;'>已锁定</span>";
            		}else{
            			return "正常";  
            		}
            	}
            },{text: '开发者代号',
            	dataIndex: 'tellerId',
            	flex: 1,
            	sortable: true
            },{text: '是否是审核状态',
            	dataIndex: 'authStep',
            	flex: 1,
            	sortable: true,
            	renderer: function (value, meta, record) {
            		//alert(record.data.isAuth);
            		if(value==3&&record.data.isAuth==false){
            			return "<span style='color:red;font-weight:bold;'>是</span>";
            		}else{
            			return "否";  
            		}
            	}
            },{text: '登录时间',
            	dataIndex: 'loginDate',
            	flex: 2,
            	sortable: true
            },{
	            xtype:'actioncolumn', 
	            flex: 3,
	            header: '编辑  | 审核  | 流水  | 锁',
	            //align:'center',
	            items: ['-',{
	            	iconCls:'edit',
	            	tooltip: '编辑',
	                width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	                	var rec = grid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
    	            	Ext.Ajax.request({url:'../../sys/member/findConsignorInfo.action',  params : {consignorId:id},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							
							var form = Ext.getCmp('editdetailform');
							form.getForm().reset();
							Ext.getCmp('editconId').setValue(id);
							Ext.getCmp('editmobile2').setValue(responseObj.data['mobile']);
							Ext.getCmp('editcompanyName').setValue(responseObj.data['companyName']);
							Ext.getCmp('editcompanyAddr').setValue(responseObj.data['address']);
							Ext.getCmp('edituserName').setValue(responseObj.data['realName']);
							Ext.getCmp('editidCard').setValue(responseObj.data['idCard']);
							Ext.getCmp('editdescript').setValue(responseObj.data['descript']);
							Ext.getCmp('editbussDescript').setValue(responseObj.data['bussDescript']);
							var h = Ext.getBody().getHeight();
							editCompanyWindow.setHeight(h);
							editCompanyWindow.show();
						},
						failure: function(result,options){
							var responseObj =  Ext.decode(result.responseText);
							Ext.MessageBox.alert('提示',responseObj.appMsg);
						}});
	                	
	                }
	            },'-',{
	            	iconCls:'check',
	            	tooltip: '审核',
	                width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	                	var rec = grid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
	            		/*if(rec.get("authStep")!=3||rec.get("isAuth")){
	            			Ext.MessageBox.alert('提示',"不是审核状态不能审核");
	            			return;
	            		}*/
    	            	Ext.Ajax.request({url:'../../sys/member/findConsignorInfo.action',  params : {consignorId:id},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							var form = Ext.getCmp('checkform');
							form.getForm().reset();
							var location = (window.location+'').split('/'); 
			            	var basePath = location[0]+'//'+location[2]+'/'+'hyt';
			            	Ext.getCmp('idCardPic').setSrc('../../img/pic.png');
			            	Ext.getCmp('businessLicensePic').setSrc('../../img/pic.png');
			            	if(responseObj.data['idCardPic']!=null&&responseObj.data['idCardPic']!=''){
			            		var idCardPicsrc = responseObj.data['idCardPic'];
			            		if(responseObj.data['idCardPic'].indexOf(".webp")>-1){
			            			//idCardPicsrc = responseObj.data['idCardPic'].replace(".webp",".jpg");
			            		}
			            		Ext.getCmp('idCardPic').setSrc(basePath+"/"+idCardPicsrc);
			            	}
			            	if(responseObj.data['businessLicensePic']!=null&&responseObj.data['businessLicensePic']!=''){
			            		var businessLicensePicsrc = responseObj.data['businessLicensePic'];
			            		if(responseObj.data['businessLicensePic'].indexOf(".webp")>-1){
			            			//businessLicensePicsrc = responseObj.data['businessLicensePic'].replace(".webp",".jpg");
			            		}
			            		Ext.getCmp('businessLicensePic').setSrc(basePath+"/"+businessLicensePicsrc);
			            	}
							Ext.getCmp('officePic').setSrc(basePath+"/"+responseObj.data['officePic']);
							Ext.getCmp('consignorId').setValue(id);
							Ext.getCmp('checked').setValue(responseObj.data['isAuth']+"");
							if(responseObj.data['isAuth']){
								Ext.getCmp('tips').setVisible(false);
							}
							var h = Ext.getBody().getHeight();
							checkwindow.setHeight(h);
		                	checkwindow.show();
						},
						failure: function(result,options){
							var responseObj =  Ext.decode(result.responseText);
							Ext.MessageBox.alert('提示',responseObj.appMsg);
						}});
	                }
	            },'-',{
	            	iconCls:'dtl',
	            	tooltip: '账号流水',
	                width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	                	var rec = grid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
	                	paras2.consignorId = id;
	                	store2.load();
	                	var h = Ext.getBody().getHeight();
	                	var w = Ext.getBody().getWidth();
						window2.setHeight(h);
						window2.setWidth(w);
	                	window2.show();
	                }
	            },'-',{
	            	width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	                	var rec = grid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
    	            	Ext.Ajax.request({url:'../../sys/member/consignorlocked.action',  params : {consignorId:id},method:'post',type:'json',  success:function(result, request){
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
		                if (rec.get('isLocked')) {
		                    return 'icon-lock';
		                } else {
		                    return 'icon-lock_open';
		                }
		            },
		            getTip: function (v, meta, rec) {
		            	if (rec.get('isLocked')) {
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
		layout:{     
	        type:'hbox',     
	        align : 'stretch',     
	        pack  : 'start'    
	      },     
	      defaults:{     
	        //子元素平均分配宽度     
	        flex:1     
	      },     
	      split:true,     
	      frame:true,     
	      //前面定义的一个Grid数组     
	      items:[
	             grid,
	             {
	            	 flex:3,
	            	 layout:{     
	         	        type:'vbox',     
	         	        align : 'stretch',     
	         	        pack  : 'start'    
	         	      },
	         	      items:[
								freightLineGrid
								,addwarehouseGrid
	         	             ]
	             }
			  ],
		renderTo: 'btsAlarm-grid'
	});
  
  var continuewindow = new Ext.Window({  
      title:'续费',  
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
          id:'continueform',
          baseCls:'x-plain',  
          xtype:'form',
          style:'padding:8px;',  
          url: '../../sys/member/addExpireTime4Consignor.action',
          labelWidth:30,  
          items:[
			{  
			    id:'contiuneConsignorId',
			    name:'consignorId',
			    labelWidth:40,  
			    xtype:'hiddenfield',  
			},
			{
        	width:455,
        	forceSelection: true,
        	xtype: 'combobox',
        	fieldLabel:'续费年限:',
        	labelWidth: 55,
			store: continueStore,
			emptyText :'请选择...',
			queryMode: 'local',
			displayField: 'name',
			valueField: 'code',
			id:'years',
			name:'years',
			value:'1',
			listeners : {}
		}
          ],
	      buttonAlign:'center',
	      buttons:[{  
	          	  text:'确定',
	          	  //formBind: true,
	        	  handler: function() {
	        		  var btn = this;
	        		  btn.setDisabled(true);
	        		  var form = this.up('form').getForm();
	                  if (form.isValid()) {
	                	  form.submit({
	                          success: function(form, action) {
	                        	    btn.setDisabled(false);
		                            store.reload();
		                            continuewindow.close();
	                          },
	                          failure: function(form, action) {
	                        	  btn.setDisabled(false);
	                              Ext.Msg.alert('操作失败', action.result.appMsg);
	                          }
	                      });
	                  }
	          	  }
	      },{  
	          	  text:'取消',
	        	  handler: function() {
	        		  continuewindow.close();
	    		}
	      }]
      }
      ]
  }); 
  
  var balancewindow = new Ext.Window({  
      title:'续费',  
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
          id:'balanceform',
          baseCls:'x-plain',  
          xtype:'form',
          style:'padding:8px;',  
          url: '../../sys/member/addBalance4Consignor.action',
          labelWidth:30,  
          items:[
			{  
			    id:'balanceConsignorId',
			    name:'consignorId',
			    labelWidth:40,  
			    xtype:'hiddenfield',  
			},
			 {  
	           	  id:'balance',
	           	  name:'balance',
	           	  labelWidth:70,
	           	  width : 55,
	           	  allowBlank:false,
                  xtype:'numberfield',  
                  fieldLabel:'充值额度'
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
	                          success: function(form, action) {
	                        	  btn.setDisabled(false);
		                            store.reload();
		                            balancewindow.close();
	                          },
	                          failure: function(form, action) {
	                        	  btn.setDisabled(false);
	                              Ext.Msg.alert('操作失败', action.result.appMsg);
	                          }
	                      });
	                  }
	          	  }
	      },{  
	          	  text:'取消',
	        	  handler: function() {
	        		  balancewindow.close();
	    		}
	      }]
      }
      ]
  }); 
  var checkwindow = new Ext.Window({  
      title:'物流公司审核',  
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
			    text: '名片照片：'
			},
             Ext.create('Ext.Img', {
             	  id:'idCardPic',
             	  height:350,
             	  width : 455,
             	  name:'idCardPic',
                 src: '../../img/pic.png',
                 listeners: {
               	  render: function() {
               		  this.mon(this.imgEl, 'mouseover', function () {
               			  
               		  }, this);
                     }
                 }
             }),
       	   {
			    xtype: 'label',
			    text: '营业执照：'
			},
	           Ext.create('Ext.Img', {
	          	  id:'businessLicensePic',
	          	  height:350,
	          	  width : 455,
	          	  name:'businessLicensePic',
	              src: '../../img/pic.png',
	              listeners: {
	            	  render: function() {
	            		  this.mon(this.imgEl, 'mouseover', function () {
	            			  
	            		  }, this);
	                  }
	              }
	        }),
	     {
			    xtype: 'label',
			    text: '门头照：'
		},
	     Ext.create('Ext.Img', {
	       	  id:'officePic',
	       	  height:350,
	       	  width : 455,
	       	  name:'officePic',
	           src: '../../img/pic.png',
	           listeners: {
	         	  render: function() {
	         		  this.mon(this.imgEl, 'mouseover', function () {
	         			  
	         		  }, this);
	               }
	           }
	     }),
         {
        	width:455,
        	forceSelection: true,
        	xtype: 'combobox',
        	fieldLabel:'审核状态:',
        	labelWidth: 70,
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
			      },
					select : function(combo,record){
						if(combo.getValue()=='true'){
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
			{boxLabel:'资料不全',name:'tips',inputValue:1, checked: true},
			{boxLabel:'图片不清',name:'tips',inputValue:2},
			{boxLabel:'资料不正确',name:'tips',inputValue:3}
			]},
          ],
	      buttonAlign:'center',
	      buttons:[{  
	          	  text:'确定',
	          	  //formBind: true,
	        	  handler: function() {
	        		  var btn = this;
	        		  btn.setDisabled(true);
	        		  var form = this.up('form').getForm();
	                  if (form.isValid()) {
	                      form.submit({
	                          success: function(form, action) {
	                        	  btn.setDisabled(false);
		                            store.reload();
		       	        		  	checkwindow.close();
	                          },
	                          failure: function(form, action) {
	                        	  btn.setDisabled(false);
	                              Ext.Msg.alert('操作失败', action.result.appMsg);
	                          }
	                      });
	                  }
	          	  }
	      },{  
	          	  text:'取消',
	        	  handler: function() {
	        		  checkwindow.close();
					//
	    		}
	      }]
      }
      ]
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
	  	        fieldLabel: '货主名称',
	  	        id:'realNamed',
	  	        name: 'realName',
	  	        value: '10'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '手机',
	  	        id:'mobiled',
	  	        name: 'mobile',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '身份证',
	  	        name: 'idCard',
	  	        id:'idCardd',
	  	        value: '11'
	  	    },{
	  	        xtype: 'displayfield',
	  	        fieldLabel: '代理区号',
	  	        name: 'proxy',
	  	        id:'proxyd',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '地址',
	  	        name: 'address',
	  	        id:'addressd',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '注册时间',
	  	        name: 'registerTime',
	  	        id:'registerTimed',
	  	        value: '11'
	  	    }
	  	  , {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '服务到期时间',
	  	        name: 'expireTime',
	  	        id:'expireTimed',
	  	        value: '11'
	  	    }
	  	  , {
	  		  xtype: 'displayfield',
	  		  fieldLabel: '服务时间',
	  		  name: 'expireTime',
	  		  id:'expireTimedd',
	  		  value: '11'
	  	  }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '余额',
  	        name: 'balance',
  	        id:'balanced',
  	        value: '11'
  	    }
	  	, {
	  		xtype: 'displayfield',
	  		fieldLabel: '驿道币',
	  		name: 'points',
	  		id:'pointsd',
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
  });   
  //['realName','mobile','idCard','isLocked','consignorType','loginDate','loginIp','cities','registrationPic',
   //'address','isAuth','fax','registerTime','isPerfect'
  var editwindow = new Ext.Window({  
      title:'物流公司修改',  
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
      fields: ['accountId','pay','deposit','balance','tradeType','remark','createdDate','payType','points'
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
  
  var minusBalanceTally = new Ext.Window({  
      title:'退款',  
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
          id:'minusBalanceTallyform',
          baseCls:'x-plain',  
          xtype:'form',
          style:'padding:8px;',  
          url: '../../sys/member/minusBalanceTallyForConsingor.action',
          labelWidth:30,  
          items:[
			{  
			    id:'minusBalanceTallyId',
			    name:'consignorId',
			    labelWidth:40,  
			    xtype:'hiddenfield',  
			},
			 {  
	           	  id:'minusBalanceTallybalance',
	           	  name:'balance',
	           	  labelWidth:70,
	           	  width : 55,
	           	  allowBlank:false,
                  xtype:'numberfield',  
                  fieldLabel:'退款额度'
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
	                          success: function(form, action) {
	                        	    btn.setDisabled(false);
		                            store.reload();
		                            minusBalanceTally.close();
	                          },
	                          failure: function(form, action) {
	                        	  btn.setDisabled(false);
	                              Ext.Msg.alert('操作失败', action.result.appMsg);
	                          }
	                      });
	                  }
	          	  }
	      },{  
	          	  text:'取消',
	        	  handler: function() {
	        		  minusBalanceTally.close();
	    		}
	      }]
      }
      ]
  }); 
  
  var window2 = new Ext.Window({  
      title:'支付详细',  
      width:500,  
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
			{text: '支付类型',
	            dataIndex: 'payType',
	            flex: 2,
	            sortable: true
        	},
        	{text: '账单',
	            dataIndex: 'pay',
	            flex: 2,
	            sortable: true
        	},{text: '驿道币',
	            dataIndex: 'points',
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
  
  var  editCompanyWindow = new Ext.Window({  
      title:'填写资料',  
      width:500,  
      height:600,  
      closeAction:'hide',  
      plain:true,  
      layout:'form',
      autoScroll:true,
      monitorValid:true,
      frame : true,  
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
          id:'editdetailform',
          baseCls:'x-plain',  
          xtype:'form',
          style:'padding:8px;',  
          url: '../../sys/member/editCompanyConsignorUser.action',
          labelWidth:80,  
          items:[
{  
    id:'editconId',
    name:'consignorId',
    labelWidth:40,  
    xtype:'hiddenfield',  
},
					{
				    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
				    xtype:'fieldset',
				    title: '基本信息',
				    collapsible: true,
				    layout:'form',
				    labelWidth:80,  
				    items :[
					  		 {
					  	        xtype: 'textfield',
					  	        fieldLabel: '用户手机',
					  	        name: 'mobile',
					  	        allowBlank: false,
					  	        id:'editmobile2',
					  	        emptyText:"必需填写",
					  	        regexText : '不是有效的手机号码',
					  	        regex: /^1(3|4|5|7|8)\d{9}$/
					  	    }, {
					  	        xtype: 'textfield',
					  	        labelWidth:120,  
					  	        inputType: 'password',    //密码
					  	        fieldLabel: '用户密码',
					  	        name: 'password',
					  	        id:'editpassword',
					  	        emptyText:"如果不修改请不要填写",
					  	        regexText : '密码为10至20位',
					  	        regex: /^[a-zA-Z0-9]{6,20}$/
					  	    }
					  	    ]
				      }, {
					    	  xtype:'fieldset',
					    	    title: '认证信息',
					    	    collapsible: true,
					    	    layout:'form',
					          items :[
							{
							      xtype: 'textfield',
							      labelWidth:120,  
							      allowBlank: false,
							      emptyText:"必需填写",
							      fieldLabel: '公司名称',
							      name: 'companyName',
							      id:'editcompanyName'
							  }, {
							      xtype: 'textfield',
							      labelWidth:120,  
							      fieldLabel: '公司地址',
							      allowBlank: false,
							      emptyText:"必需填写",
							      name: 'companyAddr',
							      id:'editcompanyAddr'
							  },
						  	{
					  	        xtype: 'textfield',
					  	        fieldLabel: '真实名称',
					  	        name: 'userName',
					  	        regexText : '不是有效人名',
					  	        regex:/^[\u4e00-\u9fa5]{2,4}$/,
					  	        id:'edituserName'
					  	    }
						  	, {
					  	        xtype: 'textfield',
					  	        fieldLabel: '身份证号',
					  	        name: 'idCard',
					  	        id:'editidCard',
					  	        regexText : '不是有效的身份证号',
					  	        regex:/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
					  	    }
						  	
					        ]
					      },
					      {
					    	  xtype:'fieldset',
					    	  id:'set5',
					    	    title: '上传图片',
					    	    collapsible: true,
					    	    layout:'form',
					          items :[
								{
								    xtype: 'filefield',
								    name: 'idCardPic',
								    id: 'idCardPic3',
								    fieldLabel: '名片照片',
								    labelWidth: 100,
								    emptyText:"如果不修改请不要提交",
								    //msgTarget: 'side',
								    anchor: '100%',
								    buttonText: '名片上传'
								},{
								    xtype: 'filefield',
								    name: 'avartPic',
								    id: 'avartPic3',
								    fieldLabel: '本人头像',
								    labelWidth: 100,
								    emptyText:"如果不修改请不要提交",
								    //msgTarget: 'side',
								    anchor: '100%',
								    buttonText: '本人头像'
								},{
								    xtype: 'filefield',
								    name: 'officePic',
								    id: 'officePic3',
								    fieldLabel: '门头照片',
								    emptyText:"如果不修改请不要提交",
								    labelWidth: 100,
								    //msgTarget: 'side',
								    anchor: '100%',
								    buttonText: '门头照片'
								},{
								    xtype: 'filefield',
								    name: 'businessLicensePic',
								    id: 'businessLicensePic3',
								    fieldLabel: '营业执照',
								    emptyText:"如果不修改请不要提交",
								    labelWidth: 100,
								    //msgTarget: 'side',
								    anchor: '100%',
								    buttonText: '营业执照'
								}
					        ]
					      
					      },{
							    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
							    xtype:'fieldset',
							    title: '描述信息',
							    collapsible: true,
							    layout:'form',
							    labelWidth:80,  
							    items :[
										{
										      xtype: 'textarea',
										      name: 'bussDescript',
										      fieldLabel: '业务描述',
										      id:'editbussDescript'
										  }
											, 	
											{  
												fieldLabel: '简介描述',
											    xtype: 'textarea',  
											    anchor: '100%',  
											    name: 'descript',  
											    id:"editdescript"
											},
											{
											    xtype: 'filefield',
											    name: 'pic1',
											    fieldLabel: '实景图片1',
											    labelWidth: 100,
											    emptyText:"如果不修改请不要提交",
											    //msgTarget: 'side',
											    anchor: '100%',
											    buttonText: '实景图片1'
											},
											{
											    xtype: 'filefield',
											    name: 'pic2',
											    emptyText:"如果不修改请不要提交",
											    fieldLabel: '实景图片2',
											    labelWidth: 100,
											    //msgTarget: 'side',
											    anchor: '100%',
											    buttonText: '实景图片2'
											},
											{
											    xtype: 'filefield',
											    name: 'pic3',
											    emptyText:"如果不修改请不要提交",
											    fieldLabel: '实景图片3',
											    labelWidth: 100,
											    //msgTarget: 'side',
											    anchor: '100%',
											    buttonText: '实景图片3'
											},
											{
											    xtype: 'filefield',
											    name: 'pic4',
											    emptyText:"如果不修改请不要提交",
											    fieldLabel: '实景图片4',
											    labelWidth: 100,
											    //msgTarget: 'side',
											    anchor: '100%',
											    buttonText: '实景图片4'
											},
											{
											    xtype: 'filefield',
											    name: 'pic5',
											    fieldLabel: '实景图片5',
											    emptyText:"如果不修改请不要提交",
											    labelWidth: 100,
											    //msgTarget: 'side',
											    anchor: '100%',
											    buttonText: '实景图片5'
											}
								  	    ]
					      
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
	                        	  editCompanyWindow.close();
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
	        		  editCompanyWindow.close();
	    		}
	      }]
      }
      ]
  });  
  
  var  addCompanyWindow = new Ext.Window({  
      title:'填写资料',  
      width:500,  
      height:600,  
      closeAction:'hide',  
      plain:true,  
      layout:'form',
      autoScroll:true,
      monitorValid:true,
      frame : true,  
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
          url: '../../sys/member/saveConsignorUser.action',
          labelWidth:80,  
          items:[
					{
				    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
				    xtype:'fieldset',
				    title: '基本信息',
				    collapsible: true,
				    layout:'form',
				    labelWidth:80,  
				    items :[
					  		 {
					  	        xtype: 'textfield',
					  	        fieldLabel: '用户手机',
					  	        name: 'mobile',
					  	        allowBlank: false,
					  	        id:'mobile2',
					  	        emptyText:"必需填写",
					  	        regexText : '不是有效的手机号码',
					  	        regex: /^1(3|4|5|7|8)\d{9}$/
					  	    }, {
					  	        xtype: 'textfield',
					  	        labelWidth:120,  
					  	        inputType: 'password',    //密码
					  	        fieldLabel: '用户密码',
					  	        name: 'password',
					  	        id:'password',
					  	        regexText : '密码为10至20位',
					  	        regex: /^[a-zA-Z0-9]{6,20}$/
					  	    }
					  	    ]
				      }, {
					    	  xtype:'fieldset',
					    	    title: '认证信息',
					    	    collapsible: true,
					    	    layout:'form',
					          items :[
							{
							      xtype: 'textfield',
							      labelWidth:120,  
							      allowBlank: false,
							      fieldLabel: '公司名称',
							      emptyText:"必需填写",
							      name: 'companyName',
							      id:'companyName'
							  }, {
							      xtype: 'textfield',
							      labelWidth:120,  
							      fieldLabel: '公司地址',
							      emptyText:"必需填写",
							      allowBlank: false,
							      name: 'companyAddr',
							      id:'companyAddr'
							  },
						  	{
					  	        xtype: 'textfield',
					  	        fieldLabel: '真实名称',
					  	        name: 'userName',
					  	        regexText : '不是有效人名',
					  	        regex:/^[\u4e00-\u9fa5]{2,4}$/,
					  	        id:'userName'
					  	    }
						  	, {
					  	        xtype: 'textfield',
					  	        fieldLabel: '身份证号',
					  	        name: 'idCard',
					  	        id:'idCard',
					  	        regexText : '不是有效的身份证号',
					  	        regex:/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
					  	    }
						  	
					        ]
					      },
					      {
					    	  xtype:'fieldset',
					    	  id:'set3',
					    	    title: '上传图片',
					    	    collapsible: true,
					    	    layout:'form',
					          items :[
								{
								    xtype: 'filefield',
								    name: 'idCardPic',
								    id: 'idCardPic2',
								    fieldLabel: '名片照片',
								    labelWidth: 100,
								    //msgTarget: 'side',
								    anchor: '100%',
								    buttonText: '名片上传'
								},{
								    xtype: 'filefield',
								    name: 'avartPic',
								    id: 'avartPic2',
								    fieldLabel: '本人头像',
								    labelWidth: 100,
								    //msgTarget: 'side',
								    anchor: '100%',
								    buttonText: '本人头像'
								},{
								    xtype: 'filefield',
								    name: 'officePic',
								    id: 'officePic2',
								    fieldLabel: '门头照片',
								    labelWidth: 100,
								    //msgTarget: 'side',
								    anchor: '100%',
								    buttonText: '门头照片'
								},{
								    xtype: 'filefield',
								    name: 'businessLicensePic',
								    id: 'businessLicensePic2',
								    fieldLabel: '营业执照',
								    labelWidth: 100,
								    //msgTarget: 'side',
								    anchor: '100%',
								    buttonText: '营业执照'
								}
					        ]
					      },
					      {
							    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
							    xtype:'fieldset',
							    title: '描述信息',
							    collapsible: true,
							    layout:'form',
							    labelWidth:80,  
							    items :[
										{
										      xtype: 'textarea',
										      name: 'bussDescript',
										      fieldLabel: '业务描述',
										      id:'bussDescript'
										  }
											, 	
											{  
												fieldLabel: '简介描述',
											    xtype: 'textarea',  
											    anchor: '100%',  
											    name: 'descript'
											},
											{
											    xtype: 'filefield',
											    name: 'pic1',
											    id: 'pic1',
											    fieldLabel: '实景图片1',
											    labelWidth: 100,
											    //msgTarget: 'side',
											    anchor: '100%',
											    buttonText: '实景图片1'
											},
											{
											    xtype: 'filefield',
											    name: 'pic2',
											    id: 'pic2',
											    fieldLabel: '实景图片2',
											    labelWidth: 100,
											    //msgTarget: 'side',
											    anchor: '100%',
											    buttonText: '实景图片2'
											},
											{
											    xtype: 'filefield',
											    name: 'pic3',
											    id: 'pic3',
											    fieldLabel: '实景图片3',
											    labelWidth: 100,
											    //msgTarget: 'side',
											    anchor: '100%',
											    buttonText: '实景图片3'
											},
											{
											    xtype: 'filefield',
											    name: 'pic4',
											    id: 'pic4',
											    fieldLabel: '实景图片4',
											    labelWidth: 100,
											    //msgTarget: 'side',
											    anchor: '100%',
											    buttonText: '实景图片4'
											},
											{
											    xtype: 'filefield',
											    name: 'pic5',
											    id: 'pic5',
											    fieldLabel: '实景图片5',
											    labelWidth: 100,
											    //msgTarget: 'side',
											    anchor: '100%',
											    buttonText: '实景图片5'
											}
								  	    ]
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
	                             addCompanyWindow.close();
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
	        		  addCompanyWindow.close();
	    		}
	      }]
      }
      ]
  });  
});