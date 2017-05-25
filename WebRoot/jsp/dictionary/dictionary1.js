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
	
	var leafStore = Ext.create('Ext.data.Store', {
	    model:'statusModel',
	    data:[['1','是'],
	    	 ['0','否']]
	});
	
	Ext.define('parentModel', {
	     extend: 'Ext.data.Model',
	     fields: ['id','name']
	});
	
	var parentStore = Ext.create('Ext.data.Store', {
	    model:'parentModel',
	    autoLoad:true,
		proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../dictionary/queryParent.action',
            reader: {
                root: 'root'
            }
        }
	});
	
	var queryButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'query', iconCls:'add',text: '增加',
    	handler: function() {
			//Ext.getCmp("grid").reload();
			//store.load();
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
	        "id", "parentId","typeDes","name","status"
	    ]
	});
	
	
	
	var store = Ext.create('Ext.data.TreeStore', {
		model:'DeptModel',
	    root: { expanded: true,name:'数据字典' },//
	    
	    proxy: {
	        type: 'ajax',
	        url: '../../dictionary/listByPage.action',
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
	                    text: "描述",
	                    dataIndex: "typeDes",
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
	    	               /* getClass: function(v,meta,record) {  
	    	            		alert(record.get('parentId'));
	    	                    if (record.get('parentId') == null||record.get('parentId') == ''){
	    	                        return '';
	    	                    }
	    	                 },*/
	    	                handler: function(grid, rowIndex, colIndex) {
	    	            		var rec = grid.getStore().getAt(rowIndex);
	    	            		var id = rec.get("id");
		    	            	Ext.Ajax.request({url:'../../dictionary/query.action',  params : {id:id},method:'post',type:'json',  success:function(result, request){
									var responseObj =  Ext.decode(result.responseText);
									if(responseObj.success!=null&&!responseObj.success){
										Ext.MessageBox.alert("错误提示",responseObj.appMsg);
										return;
									}
									var form = Ext.getCmp('dictionaryform');
									form.getForm().reset();
									Ext.getCmp('name').setValue(responseObj.data['name']);
									Ext.getCmp('parentId').setValue(responseObj.data['parentId']);
									Ext.getCmp('status').setValue(responseObj.data['status']);
									Ext.getCmp('typeDes').setValue(responseObj.data['typeDesc']);
									Ext.getCmp('root').setValue(responseObj.data['root']);
									Ext.getCmp('id').setValue(id);
				        	  		if(responseObj.data['parentId']==-1){
				        	  			Ext.getCmp('root').enable();
				        	  		}else{
				        	  			Ext.getCmp('root').disable();
				        	  		}
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
		    	            	Ext.Ajax.request({url:'../../dictionary/query.action',  params : {id:id},method:'post',type:'json',  success:function(result, request){
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
									Ext.getCmp('root').disable();
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
	      height:350,  
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
	                 labelWidth:30, 
			         items:[
						{  
						    id:'id',
						    name:'id',
						    labelWidth:40,  
						    xtype:'hiddenfield',  
						},
			            {  
			        	  id:'name',
			        	  name:'name',
			        	  allowBlank:false,
			        	  blankText:'名称必需要',
			        	  labelWidth:40,  
			              xtype:'textfield',  
			              fieldLabel:'名称',  
			              anchor:'90%'  
			          },
			          {	  
			        	  forceSelection: true,
			        	  xtype: 'combobox',
			        	  fieldLabel:'所属结点',
			        	  labelWidth: 60,
			        	  anchor: '100%',
			  			  editable:false,
			  			  store: parentStore,
			  			  emptyText :'请选择...',
			  			  queryMode: 'local',
			  	    	  displayField: 'name',
			  	    	  valueField: 'id',
			  	    	  allowBlank: false,
			  	    	  id:'parentId',
			  	    	  name:'parentId',
			  	    	  listeners:{  
							  select : function(combo, record,index){
			        	  		var combSort = Ext.getCmp('parentId');
			        	  		if(combSort.getValue()==-1){
			        	  			Ext.getCmp('root').enable();
			        	  		}else{
			        	  			Ext.getCmp('root').disable();
			        	  		}
							  }
						  }
			          },
			          {  
			        	  id:'root',
			        	  name:'root',
			        	  labelWidth:40,  
			              xtype:'textfield',
			              disabled:true,
			              fieldLabel:'根',  
			              anchor:'90%'  
			          },
			          {	  
			        	  forceSelection: true,
			        	  xtype: 'combobox',
			        	  fieldLabel:'叶子',
			        	  labelWidth: 60,
			        	  anchor: '100%',
			        	  value:'1',
			  			  editable:false,
			  			  store: leafStore,
			  			  queryMode: 'local',
			  	    	  displayField: 'desc',
			  	    	  valueField: 'val',
			  	    	  id:'isLeaf',
			  	    	  name:'isLeaf'
			          },
			          {	  
			        	  forceSelection: true,
			        	  xtype: 'combobox',
			        	  fieldLabel:'状态',
			        	  labelWidth: 60,
			        	  anchor: '100%',
			  			  editable:false,
			  			  value:'1',
			  			  store: statusStore,
			  			  queryMode: 'local',
			  	    	  displayField: 'desc',
			  	    	  valueField: 'val',
			  	    	  id:'status',
			  	    	  name:'status'
			          },
			          {
		        	  	 id:'typeDes',
		        	  	 name:'typeDes',
		        	  	 allowBlank:false,
		        	  	 blankText:'简介必需要',
			             xtype : 'textarea',
			             emptyText:'简介',
			             width : 270,
			             height:220,
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
		                  url: '../../dictionary/save.action',
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