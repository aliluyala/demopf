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
	    extend: "Ext.data.Model",
	    fields: [
	        "id","url","name","status"
	    ]
	});
	
	
	
	var treestore = Ext.create('Ext.data.TreeStore', {
		model:'DeptModel',
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
	
    Ext.define('travelModel', {
        extend: 'Ext.data.Model',
        
        fields: ['id','name','createTime','status'
        ],
        idProperty: 'id'
    });
    // create the Data Store
	
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
            url: '../../sys/user/roleListByPage.action',
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
    
    var createButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'add', iconCls:'add',text: '增加',
    	handler: function() {
	    	var form = Ext.getCmp('dictionaryform');
			form.getForm().reset();
			window.show();
		}
    });
    
    var addPermissionButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'addpermission', iconCls:'add',text: '授权',anchor:'20%',
    	handler: function() {
    		paras.roleId=Ext.getCmp("roleId").getValue();
    		treestore.load();
    		var h = Ext.getBody().getHeight();
    		addpermissionwindow.setHeight(h);
    		addpermissionwindow.show();
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
			    {width:180,labelWidth:50,name:'titleSearch',id:'titleSearch',xtype:'textfield',fieldLabel:'用户名'},
		    	queryButton
	    ]
	});
    //var pluginExpanded = true;
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '角色列表',
        columnLines:true,
        store: store,
        loadMask:true,
        disableSelection: false,
        tbar:[tbar],
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
        columns:[
        	{text: '角色名称',
            dataIndex: 'name',
            flex: 1,
            sortable: true
        	},
        	{text: '注册时间',
                dataIndex: 'createTime',
                flex: 1,
                sortable: true
            }
        	,
            {
	            xtype:'actioncolumn', 
	            flex: 6,
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
    	            	Ext.Ajax.request({url:'../../sys/user/queryRole.action',  params : {id:id},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							var form = Ext.getCmp('dictionaryform');
							form.getForm().reset();
							Ext.getCmp('name').setValue(responseObj.data.sysrole['roleName']);
							Ext.getCmp('oldpermissions').setValue(responseObj.data['syspermissions']);
							Ext.getCmp('newpermissions').setValue(responseObj.data['syspermissions']);
							Ext.getCmp('status').setValue(responseObj.data.sysrole['status']);
							Ext.getCmp('roleId').setValue(id);
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
  
  
  
//创建文本上传域
    var exteditor = new Ext.form.HtmlEditor({
    	id:'contentedit',
    	width : 370,
        height:280,
        fieldLabel: '内容'
   });
  
	var window = new Ext.Window({  
	      title:'新建',  
	      width:500,  
	      height:200,  
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
						    id:'roleId',
						    name:'roleId',
						    labelWidth:40,  
						    xtype:'hiddenfield',  
						},
						addPermissionButton,
						{  
						    id:'oldpermissions',
						    name:'oldpermissions',
						    labelWidth:40,  
						    xtype:'hiddenfield',  
						},
						{  
						    id:'newpermissions',
						    name:'newpermissions',
						    labelWidth:40,  
						    xtype:'hiddenfield',  
						},
						{  
						  id:'name',
						  name:'name',
						  allowBlank:false,
						  blankText:'名称必需要',
						  labelWidth:60,  
						  xtype:'textfield',  
						  fieldLabel:'名      |   称',  
						  anchor:'90%'  
						},
			          {	  
			        	  forceSelection: true,
			        	  xtype: 'combobox',
			        	  fieldLabel:'状      |   态',
			        	  labelWidth: 60,
			        	  anchor: '100%',
			  			  editable:false,
			  			  store: statusStore,
			  			  queryMode: 'local',
			  	    	  displayField: 'name',
			  	    	  valueField: 'code',
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
		                  url: '../../sys/user/saveRole.action',
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
  
  
	var roleRightPanel = Ext.create('Ext.tree.Panel', {
	          id:'gridtree',
	          columnLines : true,
	          rowLines : true,
	          itemId: "tree",
	          store: treestore,
	          enableDD : false, 
	            split: true, 
	            width : 212, 
	            minSize : 130, 
	            maxSize : 300, 
	            rootVisible: false, 
	            containerScroll : true, 
	            collapsible : true, 
	            autoScroll: false,
	          columns: [
	              {
	                  xtype: 'treecolumn',
	                  text: '名称',
	                  dataIndex: "name",
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
	              }
	          ]
    });
	
	
  var addpermissionwindow = new Ext.Window({  
      title:'授权窗口',  
      width:500,  
      height:650,  
      closeAction:'hide',  
      plain:true,  
      layout:'fit',  
      monitorValid:true,
      autoScroll: true,
      items:[roleRightPanel],  
      buttonAlign:'center',
      buttons:[{  
      	  	text:'确定',
      	  	handler: function() {
		    	  var checkedNodes = roleRightPanel.getChecked();//tree必须事先创建好.
		    	  var s = [];
		    	  for(var i=0;i<checkedNodes.length;i++){
		    		  if(checkedNodes[i].id.indexOf("root")<0){
		    			  s.push(checkedNodes[i].id);
		    		  }
		    	  }
		    	  Ext.getCmp("newpermissions").setValue(s);
		    	  addpermissionwindow.close();
			}
      	}],  
  });
  
//首先给树添加checkchange监听事件
  roleRightPanel.on('checkchange',function(node, checked){
  checkboxSelected(node,checked);
  });
   
   
  function checkboxSelected(node,checked){
    setChildChecked(node,checked);    
    setParentChecked(node,checked);       
  }
  //选择子节点树
  function setChildChecked(node,checked){
  node.expand();
  node.set('checked',checked);
  if(node.hasChildNodes()){
   node.eachChild(function(child) {
     setChildChecked(child,checked);
    });
    }
   }
   //选择父节点树
  function setParentChecked(node,checked){
    node.set({checked:checked});
     var parentNode = node.parentNode;
     if(parentNode !=null){
        var flag = false;
        parentNode.eachChild(function(childnode) {
          if(childnode.get('checked')){ 
               flag = true;
         }
        });
        if(checked == false){
            if(!flag){
                setParentChecked(parentNode,checked);
             }
         }else{
             if(flag){
                setParentChecked(parentNode,checked);
              }
           }
        }
   }
  
});