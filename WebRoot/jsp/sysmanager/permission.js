Ext.Loader.setConfig({enabled: true});

Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.util.*',
    'Ext.toolbar.Paging',
    'Ext.ModelManager'
]);
 

Ext.onReady(function(){
	
	Ext.define('statusModel', {
	     extend: 'Ext.data.Model',
	     fields: ['val','desc']
	});
	var statusStore = Ext.create('Ext.data.Store', {
	    model:'statusModel',
	    data:[['1','有效'],
	    	 ['0','失效']]
	});
	
	Ext.define('parentModel', {
	     extend: 'Ext.data.Model',
	     fields: ['id','permissionName']
	});
	
	var parentStore = Ext.create('Ext.data.Store', {
	    model:'parentModel',
	    autoLoad:true,
		proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/user/queryParent.action',
            reader: {
                root: 'root'
            }
        }
	});
	
	var paras={};
	var sonStore = Ext.create('Ext.data.Store', {
	    model:'parentModel',
	    autoLoad:false,
		proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/user/querySon.action',
            reader: {
                root: 'root'
            }
        },
        listeners: {
        	beforeload: function (proxy, params) {
               	this.proxy.extraParams = paras;
            }
         }
	});
	
	var queryButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'query', iconCls:'add',text: '增加',
    	handler: function() {
			var form = Ext.getCmp('dictionaryform');
			form.getForm().reset();
			window.show();
		}
    });
	
	var form = Ext.create('Ext.form.FormPanel',{
		 flex:1,
		 layout:'column',
		 frame:true,
		 region:'center',
		 bodyPadding: 5,
		 items:[
		        queryButton
		        ]
	});
	
	
	
	
	Ext.define("DeptModel", {
	    extend: "Ext.data.TreeModel",
	    fields: [
	        "id", "parentId","url","name","status"
	    ]
	});
	
	
	
	var store = Ext.create('Ext.data.TreeStore', {
		model:'DeptModel',
	    root: { expanded: true,name:'用户权限' },//
	    
	    proxy: {
	        type: 'ajax',
	        url: '../../sys/user/listByPage.action',
	        reader: {
	            type: 'json',
	            successProperty: 'success'
	        }
	    },
	    autoLoad: true
	});
	
	var bbar = Ext.create('Ext.PagingToolbar', {
        //store: store,
        displayInfo: true,
        columnWidth: 6,
        beforePageText:'页',
        inputItemWidth:50,
        afterPageText:'共{0}页',
        displayMsg: '显示的数据 {0} - {1} 共 {2}',
        emptyMsg: '没有要显示的数据'
});
	
	var viewport = Ext.create("Ext.Viewport", {
		layout: 'fit',
	    renderTo: 'btsAlarm-grid',
	    items: [
	        {
	            xtype: "treepanel",
	            id:'grid',
	            tbar:[form],
	            columnLines : true,
	            rowLines : true,
	            itemId: "tree",
	            store: store,
	            columns: [
	                {
	                    xtype: 'treecolumn',
	                    text: '名称',
	                    dataIndex: "name",
	                    flex: 1,
	                    sortable: false
	                },
	                {
	                    text: '名称',
	                    dataIndex: "url",
	                    flex: 1,
	                    sortable: false
	                },
	                {
	                    text: "状态",
	                    dataIndex: "status",
	                    flex: 1,
	                    sortable: false,
	                    renderer: function(value, m, record) { 
	            			if(record.get("status")==1){
	            				return "有效";
	            			}else{
	            				return "失效";
	            			}
	            		}
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
		    	            	Ext.Ajax.request({url:'../../sys/user/query.action',  params : {id:id},method:'post',type:'json',  success:function(result, request){
									var responseObj =  Ext.decode(result.responseText);
									if(responseObj.success!=null&&!responseObj.success){
										Ext.MessageBox.alert("错误提示",responseObj.appMsg);
										return;
									}
									var form = Ext.getCmp('dictionaryform');
									form.getForm().reset();
									Ext.getCmp('name').setValue(responseObj.data['permissionName']);
									Ext.getCmp('parentId').setValue(responseObj.data['parentId']);
									Ext.getCmp('status').setValue(responseObj.data['status']);
									Ext.getCmp('url').setValue(responseObj.data['permissionUrl']);
									Ext.getCmp('id').setValue(id);
									window.show();
								},
								failure: function(result,options){
									var responseObj =  Ext.decode(result.responseText);
									Ext.MessageBox.alert('提示',responseObj.appMsg);
								}
		    	            	});
	    	                }
	    	            },{},{
	    	            	iconCls:'add',
	    	                tooltip: 'Delete',
	    	                altText:'test',
	    	                width : '80',
	    	                flex: 2,
	    	                handler: function(grid, rowIndex, colIndex) {
		    	            	var rec = grid.getStore().getAt(rowIndex);
	    	            		var id = rec.get("id");
		    	            	Ext.Ajax.request({url:'../../sys/user/query.action',  params : {id:id},method:'post',type:'json',  success:function(result, request){
									var responseObj =  Ext.decode(result.responseText);
									if(responseObj.success!=null&&!responseObj.success){
										Ext.MessageBox.alert("错误提示",responseObj.appMsg);
										return;
									}
									var form = Ext.getCmp('dictionaryform');
									form.getForm().reset();
									Ext.getCmp('parentId').setValue(responseObj.data['parentId']);
									if(responseObj.data['parentId']==-1){
										Ext.getCmp('parentId').setValue(responseObj.data['id']);
									}
									window.show();
								}});
	    	                }                
	    	            }]
	    			 }
	            ]
	        }
	    ]
	});
	
	
	
	var window = new Ext.Window({  
	      title:'新建',  
	      width:500,  
	      height:300,  
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
	                 labelWidth:60, 
			         items:[
						{  
						    id:'id',
						    name:'id',
						    labelWidth:60,  
						    xtype:'hiddenfield',  
						},
			            {  
			        	  id:'name',
			        	  name:'name',
			        	  allowBlank:false,
			        	  blankText:'名称必需要',
			        	  labelWidth:60,  
			              xtype:'textfield',  
			              fieldLabel:'名称',  
			              anchor:'90%'  
			          },
			          {	  
			        	  forceSelection: true,
			        	  xtype: 'combobox',
			        	  fieldLabel:'所属结点',
			        	  labelWidth: 65,
			        	  anchor: '100%',
			  			  editable:false,
			  			  store: parentStore,
			  			  emptyText :'请选择...',
			  			  queryMode: 'local',
			  	    	  displayField: 'permissionName',
			  	    	  valueField: 'id',
			  	    	  allowBlank: false,
			  	    	  id:'parentId',
			  	    	  name:'parentId',
			  	    	  listeners:{  
							  select : function(combo, record,index){
								paras.parentId = record[0].data.id;
								sonStore.load();
			        	  		var combSort = Ext.getCmp('parentId');
			        	  		if(combSort.getValue()==-1){
			        	  			Ext.getCmp('url').disable();
			        	  		}else{
			        	  			Ext.getCmp('url').enable();
			        	  		}
							  }
						  }
			          },
			          {	  
			        	  xtype: 'combobox',
			        	  fieldLabel:'所属结点',
			        	  labelWidth: 65,
			        	  anchor: '100%',
			  			  editable:false,
			  			  store: sonStore,
			  			  queryMode: 'local',
			  	    	  displayField: 'permissionName',
			  	    	  valueField: 'id',
			  	    	  id:'sonId',
			  	    	  name:'sonId',
			  	    	  listeners:{  
							  select : function(combo, record,index){
							  }
						  }
			          },
			          {  
			        	  id:'url',
			        	  name:'url',
			        	  labelWidth:60,  
			              xtype:'textfield',  
			              fieldLabel:'URL',  
			              anchor:'90%'  
			          },
			          {	  
			        	  forceSelection: true,
			        	  xtype: 'combobox',
			        	  fieldLabel:'状态',
			        	  labelWidth: 60,
			        	  anchor: '100%',
			  			  editable:false,
			  			  store: statusStore,
			  			  queryMode: 'local',
			  	    	  displayField: 'desc',
			  	    	  valueField: 'val',
			  	    	  id:'status',
			  	    	  name:'status'
			          }
			          ] 
	             })
	      ],  
	      buttonAlign:'center',
	      buttons:[{  
	          	  text:'确定',
	          	  formBind: true,
	        	  handler: function() {
			    	  var name = Ext.getCmp("name").getValue();
					  if(name==null||name==""){
						 Ext.getCmp("name").focus(false, 100); 
						 return;
					  }
			          var form = Ext.getCmp('dictionaryform');
		              form.submit({
		                  url: '../../sys/user/save.action',
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
	
});