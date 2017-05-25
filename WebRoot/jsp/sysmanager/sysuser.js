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
	var proxyParas={};
	
	Ext.define("DeptModel", {
	    extend: "Ext.data.TreeModel",
	    fields: [
	        "id","url","name","status"
	    ]
	});
	
	
	
	var treestore = Ext.create('Ext.data.TreeStore', {
		model:'DeptModel',
	    root: { expanded: true,name:'用户权限' },//
	    autoLoad:false,
	    proxy: {
	        type: 'ajax',
	        url: '../../sys/user/listPermission.action',
	        reader: {
	            type: 'json',
	            successProperty: 'success'
	        }
	    },
	    listeners: {
        	beforeload: function (proxy, params) {
               	this.proxy.extraParams = paras;
            }
         }
	});
	
	
	Ext.define('roleModel', {
        extend: 'Ext.data.Model',
        
        fields: ['id','roleName'
        ],
        idProperty: 'id'
    });
	
	Ext.define('proxyModel', {
        extend: 'Ext.data.Model',
        fields: ['id','proxyName'
        ],
        idProperty: 'id'
    });
	
	var roleStore = Ext.create('Ext.data.Store', {
	    model:'roleModel',
	    autoLoad:true,
		proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/user/listRoles.action',
            reader: {
                root: 'root'
            }
        }
	});
	
	
	var proxyStore = Ext.create('Ext.data.Store', {
	    model:'proxyModel',
	    autoLoad:true,
		proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/user/listProxy.action',
            reader: {
                root: 'root'
            }
        },
        listeners: {
        	beforeload: function (proxy, params) {
            }
         }
	});
	
    Ext.define('dataModel', {
        extend: 'Ext.data.Model',
        fields: ['id','userName','createTime','status','','realName','empNumber','department','position','balance','mobileUser','proxyAdmin'
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
            url: '../../sys/user/userListByPage.action',
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
    
    var statusStore = Ext.create('Ext.data.Store', {
		 fields: ['code', 'name'],
		 data : [
		 {"code":"effective","name":"有效"},
		 {"code":"ineffective","name":"无效"}
		 ]
		});		
    
    var isMobileUserStore = Ext.create('Ext.data.Store', {
		 fields: ['code', 'name'],
		 data : [
		 {"code":true,"name":"能"},
		 {"code":false,"name":"不能"}
		 ]
		});	
    
    var departmentStore = Ext.create('Ext.data.Store', {
		 fields: ['code', 'name'],
		 data : [
		 {"code":"A0001","name":"区域代理"},
		 {"code":"B0001","name":"内部员工"},
		 {"code":"C0001","name":"合作商"}
		 ]
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
            	createButton,'-',{
			        iconCls:'refush',
			        text:'用户充值',
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
			        	/*if(records[0].data.proxyAdmin=='否'){
			        		Ext.MessageBox.alert("提示","只能给代理充值！");
			        		return;
			        	}*/
			        	Ext.getCmp("proxyUserId").setValue(records[0].data.id);
			        	var h = Ext.getBody().getHeight();
			        	balancewindow.setHeight(h);
			        	balancewindow.show();
			        }
	            },'-',{
			        iconCls:'refush',
			        text:'刷新',
			        xtype: 'button',
			        handler: function () {
			    		store.load();
			        }
			    },'-','->',
			    {width:180,labelWidth:50,name:'titleSearch',id:'titleSearch',xtype:'textfield',fieldLabel:'用户名'},
		    	queryButton
	    ]
	});
	
	var record_start = 0;
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '用户列表',
        columnLines:true,
        store: store,
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
			}),
        	{text: '真实名称',
            dataIndex: 'realName',
            flex: 2,
            sortable: true
        	},
        	{text: '登录名称',
                dataIndex: 'userName',
                flex: 2,
                sortable: true
            },
            {text: '用户类型',
                dataIndex: 'department',
                flex: 2,
                sortable: true,
                renderer: function (value, meta, record) {
            		if(value=='A0001'){
            			return "区域代理";
            		}else if(value=='B0001'){
            			return "内部员工";  
            		}else if(value=='C0001'){
            			return "合作商";
            		}else if(value='D0001'){
            			return "车主";
            		}else if(value='E0001'){
            			return "货主";
            		}
            	}
            },
            {text: '公司职位',
                dataIndex: 'position',
                flex: 2,
                sortable: true
            },
            {text: '状态',
                dataIndex: 'status',
                flex: 2,
                sortable: true
            },
            {text: '手机是否能登录',
            	dataIndex: 'mobileUser',
            	flex: 2,
            	sortable: true
            },
            {text: '是否是代理',
            	dataIndex: 'proxyAdmin',
            	flex: 2,
            	sortable: true
            },
            {text: '账上余额',
            	dataIndex: 'balance',
            	flex: 2,
            	sortable: true
            },
        	{text: '注册时间',
                dataIndex: 'createTime',
                flex: 2,
                sortable: true
            }
        	,
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
    	            	Ext.Ajax.request({url:'../../sys/user/queryUser.action',  params : {userId:id},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							var form = Ext.getCmp('dictionaryform');
							form.getForm().reset();
							Ext.getCmp('userName').setValue(responseObj.data.sysUser['userName']);
							
							
							if(responseObj.data.sysUser['departmentId']!=0){
								Ext.getCmp('proxyId').setValue(responseObj.data.sysUser['departmentId']);
							}else{
								Ext.getCmp('proxyId').setValue(-1);
							}
							
							//Ext.getCmp('proxyId').setValue(responseObj.data.sysUser['departmentId']);
							Ext.getCmp('realName').setValue(responseObj.data.sysUser['realName']);
							Ext.getCmp('position').setValue(responseObj.data.sysUser['position']);
							Ext.getCmp('status').setValue(responseObj.data.sysUser['status']);
							Ext.getCmp('isMobileUser').setValue(responseObj.data.sysUser['mobileUser']);
							Ext.getCmp('mobile2').setValue(responseObj.data.sysUser['mobile']);
							
							if(responseObj.data['sysroles']!=null){
								var nsrs = responseObj.data['sysroles'].split(",");
								var combobox = Ext.getCmp('newRoles');
								var store = combobox.store;
								var arrTemp = new Array();
								var length = store.count();
								for(var j=0;j<nsrs.length;j++){
									for (var i = 0; i < length; i++) {
										if(store.getAt(i).get("id")==nsrs[j]){
											arrTemp.push(store.getAt(i));
										}
									}
								}
								combobox.setValue(arrTemp);
							}
							Ext.getCmp('userId').setValue(id);
							
							/*if(responseObj.data.sysUser['department']=='B0001'){
		        	  			Ext.getCmp("newRoles").setDisabled(false);
		        	  		}else{
		        	  			Ext.getCmp("newRoles").setDisabled(true);
		        	  		}*/
							var h = Ext.getBody().getHeight();
							windowedit.setHeight(h);
							windowedit.show();
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
	      title:'新建',  
	      width:500,  
	      height:500,  
	      closeAction:'hide',  
	      plain:true,  
	      layout:'form',  
	      monitorValid:true,
	      listeners: {
		  		close: function() {
		  			Ext.getBody().unmask();
		         },
		         show: function() {
		        	 Ext.getBody().mask();
			     }
		   },
	      items:[
	             Ext.create('Ext.form.Panel', {
	            	 id:'dictionaryform',
	            	 layout:'form',  
	                 baseCls:'x-plain',  
	                 style:'padding:8px;',  
	                 labelWidth:30, 
			         items:[
				         {	  
				        	  forceSelection: true,
				        	  xtype: 'combobox',
				        	  fieldLabel:'所属代理/部门',
				        	  labelWidth: 60,
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
						{  
						  id:'saveuserName',
						  name:'userName',
						  allowBlank:false,
						  blankText:'登录名',
						  labelWidth:60,  
						  xtype:'textfield',  
						  fieldLabel:'登录名',  
						  anchor:'90%'  
						},
						{  
							  id:'savepassword',
							  name:'password',
							  allowBlank:true,
							  blankText:'密码',
							  labelWidth:60,  
							  xtype:'textfield',  
							  inputType: 'password',    //密码
							  fieldLabel:'登录密码',  
							  anchor:'90%'  
						},
						{  
							  id:'saverealName',
							  name:'realName',
							  allowBlank:false,
							  blankText:'真实名称',
							  labelWidth:60,  
							  xtype:'textfield',  
							  fieldLabel:'真实名称',  
							  anchor:'90%'  
						},
						{  
							  id:'saveposition',
							  name:'position',
							  allowBlank:false,
							  blankText:'公司职位',
							  labelWidth:60,  
							  xtype:'textfield',  
							  fieldLabel:'公司职位',  
							  anchor:'90%'  
						},
						{	  
				        	  forceSelection: true,
				        	  xtype: 'combobox',
				        	  fieldLabel:'用户角色',
				        	  labelWidth: 60,
				        	  anchor: '100%',
				  			  editable:false,
				  			  store: roleStore,
				  			  //multiSelect:true,
				  			  queryMode: 'local',
				  	    	  displayField: 'roleName',
				  	    	  valueField: 'id',
				  	    	  id:'savenewRoles',
				  	    	  name:'newRoles'
				          },
				          {  
							  id:'mobile',
							  name:'mobile',
							  blankText:'手机号码',
							  labelWidth:80,  
							  xtype:'textfield',  
							  fieldLabel:'手机号码',  
							  anchor:'90%'  
						},
			          {	  
			        	  forceSelection: true,
			        	  xtype: 'combobox',
			        	  fieldLabel:'用户状态',
			        	  labelWidth: 60,
			        	  anchor: '100%',
			  			  editable:false,
			  			  store: statusStore,
			  			  queryMode: 'local',
			  	    	  displayField: 'name',
			  	    	  valueField: 'code',
			  	    	  id:'savestatus',
			  	    	  name:'status'
			          },
			          {	  
			        	  forceSelection: true,
			        	  xtype: 'combobox',
			        	  fieldLabel:'是否能手机登录',
			        	  labelWidth: 100,
			        	  anchor: '100%',
			  			  editable:false,
			  			  store: isMobileUserStore,
			  			  queryMode: 'local',
			  	    	  displayField: 'name',
			  	    	  valueField: 'code',
			  	    	  value:false,
			  	    	  id:'saveisMobileUser',
			  	    	  name:'isMobileUser'
			          }
			          ] 
	             })
	      ],  
	      buttonAlign:'center',
	      buttons:[{  
	          	  text:'确定',
	          	  formBind: true,
	        	  handler: function() {
			    	  var name = Ext.getCmp("saveuserName").getValue();
					  if(name==null||name==""){
						 Ext.getCmp("userName").focus(false, 100); 
						 return;
					  }
			          var form = Ext.getCmp('dictionaryform');
		              form.submit({
		                  url: '../../sys/user/saveUser.action',
		                  waitMsg: '上传中，请稍等...',
	          			  method: "POST",
	                      success: function (form, action) {
		            	  	location="javascript:location.reload()";
		            	  	window.close();
	                      },
	                      failure: function (form, action) { 
	                    	Ext.Msg.alert('操作失败', action.result.appMsg);
	                        window.close();
	                      }
		              });
	          	}
	      },{  
	          	  text:'取消',
	        	  handler: function() {
					window.close();
	    		}
	      }],  
	  }); 
  
  
  var windowedit = new Ext.Window({  
      title:'新建',  
      width:500,  
      height:500,  
      closeAction:'hide',  
      plain:true,  
      layout:'form',  
      monitorValid:true,
      listeners: {
	  		close: function() {
	  			Ext.getBody().unmask();
	         },
	         show: function() {
	        	 Ext.getBody().mask();
		     }
	   },
      items:[
             Ext.create('Ext.form.Panel', {
            	 id:'editform',
            	 layout:'form',  
                 baseCls:'x-plain',  
                 style:'padding:8px;',  
                 labelWidth:120, 
		         items:[
					{  
					    id:'userId',
					    name:'userId',
					    labelWidth:40,  
					    xtype:'hiddenfield',  
					},
			         {	  
			        	  forceSelection: true,
			        	  xtype: 'combobox',
			        	  fieldLabel:'所属代理/部门',
			        	  labelWidth: 120,
			        	  anchor: '100%',
			        	  editable : true,//这项必须为true
			  			  store: proxyStore,
			  			  loadingText :'正在加载',
			  			  queryParam : 'proxyName',
			  			  queryMode: 'local',
			  	    	  displayField: 'proxyName',
			  	    	  valueField: 'id',
			  	    	  id:'proxyId',
			  	    	  name:'proxyId'
			          },
					{  
					  id:'userName',
					  name:'userName',
					  allowBlank:false,
					  blankText:'登录名',
					  labelWidth:65,  
					  xtype:'textfield',  
					  fieldLabel:'登录名',  
					  anchor:'90%'  
					},
					{  
						  id:'password',
						  name:'password',
						  allowBlank:true,
						  blankText:'密码',
						  labelWidth:65,  
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
						  labelWidth:80,  
						  xtype:'textfield',  
						  fieldLabel:'真实名称',  
						  anchor:'90%'  
					},
					{  
						  id:'position',
						  name:'position',
						  allowBlank:false,
						  blankText:'公司职位',
						  labelWidth:65,  
						  xtype:'textfield',  
						  fieldLabel:'公司职位',  
						  anchor:'90%'  
					},
					{	  
			        	  forceSelection: true,
			        	  xtype: 'combobox',
			        	  fieldLabel:'用户角色',
			        	  labelWidth: 65,
			        	  anchor: '100%',
			  			  editable:false,
			  			  store: roleStore,
			  			  //multiSelect:true,
			  			  queryMode: 'local',
			  	    	  displayField: 'roleName',
			  	    	  valueField: 'id',
			  	    	  id:'newRoles',
			  	    	  name:'newRoles'
			          },
			          {  
						  id:'mobile2',
						  name:'mobile',
						  blankText:'手机号码',
						  labelWidth:80,  
						  xtype:'textfield',  
						  fieldLabel:'手机号码',  
						  anchor:'90%'  
					},
		          {	  
		        	  forceSelection: true,
		        	  xtype: 'combobox',
		        	  fieldLabel:'用户状态',
		        	  labelWidth: 65,
		        	  anchor: '100%',
		  			  editable:false,
		  			  store: statusStore,
		  			  queryMode: 'local',
		  	    	  displayField: 'name',
		  	    	  valueField: 'code',
		  	    	  id:'status',
		  	    	  name:'status'
		          },
		          {	  
		        	  forceSelection: true,
		        	  xtype: 'combobox',
		        	  fieldLabel:'是否能手机登录',
		        	  labelWidth: 120,
		        	  anchor: '100%',
		  			  editable:false,
		  			  store: isMobileUserStore,
		  			  queryMode: 'local',
		  	    	  displayField: 'name',
		  	    	  valueField: 'code',
		  	    	  value:false,
		  	    	  id:'isMobileUser',
		  	    	  name:'isMobileUser'
		          }
		          ] 
             })
      ],  
      buttonAlign:'center',
      buttons:[{  
          	  text:'确定',
          	  formBind: true,
        	  handler: function() {
		    	  var name = Ext.getCmp("userName").getValue();
				  if(name==null||name==""){
					 Ext.getCmp("userName").focus(false, 100); 
					 return;
				  }
		          var form = Ext.getCmp('editform');
	              form.submit({
	                  url: '../../sys/user/editUser.action',
	                  waitMsg: '上传中，请稍等...',
          			  method: "POST",
                      success: function (form, action) {
	            	  	location="javascript:location.reload()";
	            	  	windowedit.close();
                      },
                      failure: function (form, action) { 
                    	Ext.Msg.alert('操作失败', action.result.appMsg);
                    	windowedit.close();
                      }
	              });
          	}
      },{  
          	  text:'取消',
        	  handler: function() {
        		  windowedit.close();
    		}
      }],  
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
          url: '../../sys/user/addBalance4ProxyUserTally.action',
          labelWidth:30,  
          items:[
			{  
			    id:'proxyUserId',
			    name:'proxyUserId',
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
	        		  var form = this.up('form').getForm();
	                  if (form.isValid()) {
	                	  form.submit({
	                          success: function(form, action) {
		                            store.reload();
		                            balancewindow.close();
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
	        		  balancewindow.close();
	    		}
	      }]
      }
      ]
  }); 
});