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
        fields: ['id','cardParentType','cardTypeName','condition','returnPoint1','returnPoint2','returnSpecial','special','radio'
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
            url: '../../sys/card/truckCardRHPriceByPage.action',
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
    
    
    var createButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'add', iconCls:'add',text: '增加',
    	handler: function() {
	    	var form = Ext.getCmp('saveform');
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
			    }
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
        	{text: '类型名称',
            dataIndex: 'cardTypeName',
            flex: 1,
            sortable: true
        	},
        	{text: '直补率',
                dataIndex: 'radio',
                flex: 1,
                sortable: true,
            },
            {text: '专项补贴',
                dataIndex: 'special',
                flex: 1,
                sortable: true,
            },
        	{text: '大于条件返点率',
	            dataIndex: 'returnPoint1',
	            flex: 1,
	            sortable: true
        	},
        	{text: '条件数',
                dataIndex: 'condition',
                flex: 1,
                sortable: true,
            },
            {text: '小于条件返点率',
            	dataIndex: 'returnPoint2',
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
    	            	Ext.Ajax.request({url:'../../sys/card/truckCardRHPriceQuery.action',  params : {id:id},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							var form = Ext.getCmp('editform');
							form.getForm().reset();
							Ext.getCmp('cardTypeNameEdit').setValue(responseObj.data['cardTypeName']);
							Ext.getCmp('cardTypeEdit').setValue(responseObj.data['cardType']);
							Ext.getCmp('returnPoint1Edit').setValue(responseObj.data['returnPoint1']+"");
							Ext.getCmp('returnPoint2Edit').setValue(responseObj.data['returnPoint2']+"");
							Ext.getCmp('conditionEdit').setValue(responseObj.data['condition']+"");
							Ext.getCmp('specialEdit').setValue(responseObj.data['returnSpecial']+"");
							Ext.getCmp('radioEdit').setValue(responseObj.data['radio']+"");
							Ext.getCmp('id').setValue(id);
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
	            	 id:'saveform',
	            	 layout:'form',  
	                 baseCls:'x-plain',  
	                 style:'padding:8px;',  
	                 url: '../../sys/card/truckCardRHPriceSave.action',
	                 labelWidth:30, 
	                 //['id','tellerName','tellerId','lastLogin','isLock','createdDate'
			         items:[
						{  
						  id:'cardTypeName',
						  name:'cardTypeName',
						  allowBlank:false,
						  blankText:'卡类型名',
						  labelWidth:120,  
						  xtype:'textfield',  
						  fieldLabel:'卡类型名',  
						  anchor:'90%'  
						},
						{  
						  id:'cardType',
						  name:'cardType',
						  allowBlank:false,
						  blankText:'类型编号',
						  labelWidth:120,  
						  xtype:'textfield',  
						  fieldLabel:'类型编号',  
						  anchor:'90%'  
						},
						{  
						  id:'radio',
						  name:'radio',
						  allowBlank:false,
						  blankText:'直补率',
						  labelWidth:120,  
						  xtype:'numberfield',  
		                  decimalPrecision: 2,
		                  minValue : 0,
		                  maxValue:1,
						  fieldLabel:'直补率',  
						  anchor:'90%'  
						},
						{  
						  id:'special',
						  name:'special',
						  allowBlank:false,
						  blankText:'专项补贴率',
						  labelWidth:120,  
						  xtype:'numberfield',  
		                  decimalPrecision: 2,
		                  minValue : 0,
		                  maxValue:1,
						  fieldLabel:'专项补贴率',  
						  anchor:'90%'  
						},
						{  
						  id:'returnPoint1',
						  name:'returnPoint1',
						  allowBlank:false,
						  blankText:'大于条件返点率',
						  labelWidth:120,  
						  xtype:'numberfield',  
		                  decimalPrecision: 2,
		                  minValue : 0,
		                  maxValue:1,
						  fieldLabel:'大于条件返点率',  
						  anchor:'90%'  
						},
						{  
						  id:'condition',
						  name:'condition',
						  allowBlank:false,
						  blankText:'条件数',
						  labelWidth:120,  
						  xtype:'numberfield',  
		                  decimalPrecision: 2,
		                  minValue : 0,
						  fieldLabel:'商品价格',  
						  anchor:'90%'  
						},
						{  
						  id:'returnPoint2',
						  name:'returnPoint2',
						  allowBlank:false,
						  blankText:'小于条件返点率',
						  labelWidth:120,  
						  xtype:'numberfield',  
		                  decimalPrecision: 2,
		                  minValue : 0,
		                  maxValue:1,
						  fieldLabel:'小于条件返点率',  
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
  
	var windowedit = new Ext.Window({  
	      title:'编辑',  
	      width:500,  
	      height:500,  
	      closeAction:'hide',  
	      plain:true,  
	      layout:'form',  
	      monitorValid:true,
	      items:[
	             Ext.create('Ext.form.Panel', {
	            	 id:'editform',
	            	 layout:'form',  
	                 baseCls:'x-plain',  
	                 style:'padding:8px;',  
	                 url: '../../sys/card/truckCardRHPriceEdit.action',
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
						  id:'cardTypeNameEdit',
						  name:'cardTypeName',
						  allowBlank:false,
						  blankText:'卡类型名',
						  labelWidth:120,  
						  xtype:'textfield',  
						  fieldLabel:'卡类型名',  
						  anchor:'90%'  
						},
						{  
						  id:'cardTypeEdit',
						  name:'cardType',
						  allowBlank:false,
						  blankText:'类型编号',
						  labelWidth:120,  
						  xtype:'textfield',  
						  fieldLabel:'类型编号',  
						  anchor:'90%'  
						},
						{  
						  id:'radioEdit',
						  name:'radio',
						  allowBlank:false,
						  blankText:'直补率',
						  labelWidth:120,  
						  xtype:'numberfield',  
		                  decimalPrecision: 2,
		                  minValue : 0,
		                  maxValue:1,
						  fieldLabel:'直补率',  
						  anchor:'90%'  
						},
						{  
						  id:'specialEdit',
						  name:'special',
						  allowBlank:false,
						  blankText:'专项补贴率',
						  labelWidth:120,  
						  xtype:'numberfield',  
		                  decimalPrecision: 2,
		                  minValue : 0,
		                  maxValue:1,
						  fieldLabel:'专项补贴率',  
						  anchor:'90%'  
						},
						{  
						  id:'returnPoint1Edit',
						  name:'returnPoint1',
						  allowBlank:false,
						  blankText:'大于条件返点率',
						  labelWidth:120,  
						  xtype:'numberfield',  
		                  decimalPrecision: 2,
		                  minValue : 0,
		                  maxValue:1,
						  fieldLabel:'大于条件返点率',  
						  anchor:'90%'  
						},
						{  
						  id:'conditionEdit',
						  name:'condition',
						  allowBlank:false,
						  blankText:'条件数',
						  labelWidth:120,  
						  xtype:'numberfield',  
		                  decimalPrecision: 2,
		                  minValue : 0,
						  fieldLabel:'条件数',  
						  anchor:'90%'  
						},
						{  
						  id:'returnPoint2Edit',
						  name:'returnPoint2',
						  allowBlank:false,
						  blankText:'小于条件返点率',
						  labelWidth:120,  
						  xtype:'numberfield',  
		                  decimalPrecision: 2,
		                  minValue : 0,
		                  maxValue:1,
						  fieldLabel:'小于条件返点率',  
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
						            	  	windowedit.close();
					                      },
					                      failure: function (form, action) { 
					                    	btn.setDisabled(false);
					                    	Ext.Msg.alert('操作失败', action.result.appMsg);
					                    	windowedit.close();
					                      }
				                      });
				                	  
					                  
				                  }
				          	  }
				      },{  
				          	  text:'取消',
				        	  handler: function() {
				        		  windowedit.close();
				    		}
				      }]
	             })
	      ],  
	  }); 
});