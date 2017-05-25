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
        fields: ['id','orderNo','customName','receiveName','receiveMobile','receiveAdress','receiveAdressDtl','billType','billName','billStatus',
                 'billRecognition','billAddress','billAddressDtl','billPhone','bank','bankAcc','mailCharge','taxCharge','goodsNumber','amount','cost','discount','points','addTime','orderStatus','payTime','tellerName','payType'
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
            url: '../../sys/thirdmall/thirdMallOrderListByPage.action',
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
               //alert(reader.getMessage(raw));
               var  value=eval("("+raw.message+")");
            },
        	beforeload: function (proxy, params) {
            	paras.currentPage=store.currentPage;
            	paras.pageSize = store.pageSize;
            	Ext.getCmp('query').setDisabled(true);
               	this.proxy.extraParams = paras;
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
            url: '../../sys/thirdmall/thirdMallOrderListByPage.action',
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
               //alert(reader.getMessage(raw));
               var  value=eval("("+raw.message+")");
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
    		paras = {};
    		paras.orderNo = encodeURIComponent(Ext.getCmp("orderNoSearch").getValue());
    		paras.proxyId = encodeURIComponent(Ext.getCmp("parentId").getValue());
    		paras.storeId = encodeURIComponent(Ext.getCmp("sonId").getValue());
    		paras.date  = Ext.util.Format.date(Ext.getCmp("datetime").getValue(), 'Y-m-d');
    		store.load();
		}
    });
    
    var excelButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'excelquery', iconCls:'zoom',text: '导出excel',
    	handler: function() {
    		paras = {};
    		paras.orderNo = encodeURIComponent(Ext.getCmp("orderNoSearch").getValue());
    		paras.proxyId = encodeURIComponent(Ext.getCmp("parentId").getValue());
    		paras.storeId = encodeURIComponent(Ext.getCmp("sonId").getValue());
    		paras.date  = Ext.util.Format.date(Ext.getCmp("datetime").getValue(), 'Y-m-d');
    		Ext.Ajax.request({url:'../../sys/thirdmall/thirdMallOrderExcel.action',  params : paras,method:'post',type:'json',  success:function(result, request){
				var responseObj =  Ext.decode(result.responseText);
				if(responseObj.success!=null&&!responseObj.success){
					Ext.MessageBox.alert("错误提示",responseObj.appMsg);
					return;
				}
				//window.localtion.href=""
				
				var location = (window.location+'').split('/'); 
            	var basePath = location[0]+'//'+location[2]+'/'+'ydypf';
				window.location.href = basePath+"/"+responseObj.data;
			},
			failure: function(result,options){
				var responseObj =  Ext.decode(result.responseText);
				Ext.MessageBox.alert('提示',responseObj.appMsg);
			}});
		}
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
	    items: [{
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
		          {
				        xtype: 'datefield',
				        id:'datetime',
				        labelWidth: 65,
				        fieldLabel: '支付时间',
				        name: 'datetime',
				        format: 'Y-m-d'
				    },
			    {width:180,labelWidth:60,name:'orderNo',id:'orderNoSearch',xtype:'textfield',fieldLabel:'订单号'},
		    	queryButton,excelButton
	    ]
	});
	
	var orderNo;
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '订单列表',
        columnLines:true,
        store: store,
        loadMask:true,
        disableSelection: false,
        tbar:[tbar],
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
    	//fields: ['id','orderNo','customName','goodsNumber','amount','cost','discount','points','addTime','orderStatus','payTime','tellerName','goodsName'
        columns:[
        	{text: '订单号',
            dataIndex: 'orderNo',
            flex: 1,
            sortable: true
        	},
        	{text: '客户名',
	            dataIndex: 'customName',
	            flex: 1,
	            sortable: true
        	},
        	{text: '金额',
        		dataIndex: 'amount',
        		flex: 1,
        		sortable: true
        	},
        	{text: '实付金额',
        		dataIndex: 'discount',
        		flex: 1,
        		sortable: true
        	},
        	{text: '使用驿道币',
        		dataIndex: 'points',
        		flex: 1,
        		sortable: true
        	},
        	{text: '新增时间',
        		dataIndex: 'addTime',
        		flex: 1,
        		sortable: true
        	},
        	{text: '员工名称',
        		dataIndex: 'tellerName',
        		flex: 1,
        		sortable: true
        	},
        	{text: '订单状态',
                dataIndex: 'orderStatus',
                flex: 1,
                sortable: true,
                renderer: function (value, meta, record) {
                	if(value==1){
            			return "新订单";
            		}else if(value==2){
            			return "已成交";  
            		}else if(value==3){
            			return "已评价";
            		}else if(value==5){
            			return "需要发票";
            		}else if(value==3){
            			return "发票已发出";
            		}else{
            			return "已取消";
            		}
            	}
            },
            {text: '订单状态',
                dataIndex: 'billStatus',
                flex: 1,
                sortable: true,
                renderer: function (value, meta, record) {
                	if(value==null||value==1){
            			return "不需要发票";
            		}else if(value==2){
            			return "<span style='color:red;font-weight:bold;'>发票申请中</span>"; 
            		}else{
            			return "发票已发出";  
            		}
            	}
            },
        	{text: '支付方式',
                dataIndex: 'payType',
                flex: 1,
                sortable: true,
                renderer: function (value, meta, record) {
                	if(value=="1"){
            			return "余额支付";
            		}else if(value==2){
            			return "微信支付";  
            		}else if(value==3){
            			return "支付宝支付";
            		}else{
            			return "连连支付";
            		}
            	}
            }
        	,{text: '支付时间',
        		dataIndex: 'payTime',
        		flex: 1,
        		sortable: true
        	},{
	            xtype:'actioncolumn', 
	            flex: 3,
	            header: '开发票',
	            //align:'center',
	            items: ['-',{
	            	iconCls:'edit',
	            	tooltip: '开发票',
	                width : '80',
	                handler: function(grid, rowIndex, colIndex) {
	                	var rec = grid.getStore().getAt(rowIndex);
	            		var id = rec.get("id");
                        var form = Ext.getCmp('editform');
						form.getForm().reset();
						Ext.getCmp('orderId').setValue(id);
						Ext.getCmp('receiveName').setValue(rec.get("receiveName"));
						Ext.getCmp('receiveMobile').setValue(rec.get("receiveMobile"));
						Ext.getCmp('receiveAdress').setValue(rec.get("receiveAdress"));
						Ext.getCmp('receiveAdressDtl').setValue(rec.get("receiveAdressDtl"));
						Ext.getCmp('billType').setValue(rec.get("billType")==1?"普通发票":"增值发票");
						Ext.getCmp('billName').setValue(rec.get("billName"));
						Ext.getCmp('billRecognition').setValue(rec.get("billRecognition"));
						Ext.getCmp('billAddress').setValue(rec.get("billAddress"));
						Ext.getCmp('billAddressDtl').setValue(rec.get("billAddressDtl"));
						Ext.getCmp('billPhone').setValue(rec.get("billPhone"));
						Ext.getCmp('bank').setValue(rec.get("bank"));
						Ext.getCmp('bankAcc').setValue(rec.get("bankAcc"));
						Ext.getCmp('mailCharge').setValue(rec.get("mailCharge"));
						Ext.getCmp('taxCharge').setValue(rec.get("taxCharge"));
						Ext.getBody().mask();
						var h = Ext.getBody().getHeight();
						editwindow.setHeight(h);
						editwindow.show();
	                	
	                }
	            }]
			 }
        	],
        	listeners: {
                itemdblclick: function (me, record, item, index, e, eOpts) {
                	/*var orderNo = record.data.orderNo;
	            	Ext.Ajax.request({url:'../../sys/thirdmall/thirdMallOrderQuery.action',  params : {orderNo:orderNo},method:'post',type:'json',  success:function(result, request){
						var responseObj =  Ext.decode(result.responseText);
						if(responseObj.success!=null&&!responseObj.success){
							Ext.MessageBox.alert("错误提示",responseObj.appMsg);
							return;
						}
						//双击事件的操作
	                	var location = (window.location+'').split('/'); 
	                	var basePath = location[0]+'//'+location[2]+'/'+'ydy';
	                	var p = responseObj.data;
	                	if(p!=null&&p!=""){
	                		Ext.getCmp("path").update('<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+basePath+'/'+p+'"></iframe>');
	                		var h = Ext.getBody().getHeight();
	    	          		checkwindow.setHeight(h);
	                		checkwindow.show();
	                	}else{
	                		Ext.getCmp("path").update("");
	                		Ext.MessageBox.alert("提示","还没有保单");
	                	}
					},
					failure: function(result,options){
						var responseObj =  Ext.decode(result.responseText);
						Ext.MessageBox.alert('提示',responseObj.appMsg);
					}});*/
                	//Ext.getCmp("htmlpath").update('<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="print.jsp"></iframe>');
                	orderNo = record.data.orderNo;
                	if(Ext.getDom("mainFrame")!=null){
                		Ext.Ajax.request({url:'../../sys/thirdmall/thirdMallOrderQuery.action',  params : {orderNo:orderNo},method:'post',type:'json',  success:function(result, request){
    						var responseObj =  Ext.decode(result.responseText);
    						if(responseObj.success!=null&&!responseObj.success){
    							Ext.MessageBox.alert("错误提示",responseObj.appMsg);
    							return;
    						}
    						var p = responseObj.data;
    						var child = document.getElementById("mainFrame").contentWindow;
    		        		child.document.getElementById("storeName").innerHTML=p.storeName;
    		        		child.document.getElementById("tellerId").innerHTML=p.tellerId;
    		        		child.document.getElementById("orderNo").innerHTML=p.orderNo.substr(8,p.orderNo.lenght);
    		        		child.document.getElementById("mobile").innerHTML=p.mobile;
    		        		child.document.getElementById("payTime").innerHTML=p.payTime;
    		        		child.document.getElementById("count").innerHTML=p.count;
    		        		child.document.getElementById("total").innerHTML=p.total;
    						var tab=child.document.getElementById('tableId');
    						var rowNum=tab.rows.length;
						    if(rowNum>0){
						        for(var i=1;i<rowNum-1;i++){
						        	tab.deleteRow(1);
						        }  
						    }
    						for(var a=0;a<p.list.length;a++){
    							var tr=tab.insertRow(a+1);  
    							var td=tr.insertCell(0);
    							td.innerHTML=p.list[a].goodName; 
    							td=tr.insertCell(1);
    							td.innerHTML=p.list[a].price; 
    							td=tr.insertCell(2);
    							td.innerHTML=p.list[a].number;  
    							td=tr.insertCell(3);
    							td.innerHTML=p.list[a].ant;  
    						}
    					},
    					failure: function(result,options){
    						var responseObj =  Ext.decode(result.responseText);
    						Ext.MessageBox.alert('提示',responseObj.appMsg);
    					}});
                	}
                	var h = Ext.getBody().getHeight();
                	windowMap.setHeight(h);
                	windowMap.show();
                }
            },
        forceFit: true,
        // paging bar on the bottom
        bbar: bbar
    });
  Ext.create('Ext.Viewport',{
		layout: 'fit',items:[grid],renderTo: 'btsAlarm-grid'
	});
  
  var checkwindow = new Ext.Window({  
      title:'打印订单',  
      width:800,  
      height:600,
      autoHeight : true,
      autoWidth:false,
      closeAction:'hide',  
      layout:'fit',
	  listeners: {
	  		close: function() {
	  			Ext.getBody().unmask();
	         },
			  show: function() {
			 	 Ext.getBody().mask();
			  }
	   },
      items:[{
    	  xtype:'panel',
    	  id:'path',
          html: ''
      }
      ]
  });  
  
  
  var windowMap = new Ext.Window({  
      title:'订单详情',  
      width:360,  
      height:600,  
      closeAction:'hide',  
      plain:true,  
      layout:'fit',
      autoScroll:true,
      monitorValid:true,
	   html : '<iframe scrolling="auto" id="mainFrame" name="mainFrame" frameborder="0" width="100%" height="100%" src="print.jsp"></iframe>',
	   listeners: {
		   render :function(v1, v2, v3) { 
               Ext.getDom("mainFrame").contentWindow.onload = function() { 
	            	Ext.Ajax.request({url:'../../sys/thirdmall/thirdMallOrderQuery.action',  params : {orderNo:orderNo},method:'post',type:'json',  success:function(result, request){
						var responseObj =  Ext.decode(result.responseText);
						if(responseObj.success!=null&&!responseObj.success){
							Ext.MessageBox.alert("错误提示",responseObj.appMsg);
							return;
						}
						var p = responseObj.data;
						var child = document.getElementById("mainFrame").contentWindow;
		        		child.document.getElementById("storeName").innerHTML=p.storeName;
		        		child.document.getElementById("tellerId").innerHTML=p.tellerId;
		        		child.document.getElementById("orderNo").innerHTML=p.orderNo.substr(8,p.orderNo.lenght);
		        		child.document.getElementById("mobile").innerHTML=p.mobile;
		        		child.document.getElementById("payTime").innerHTML=p.payTime;
		        		child.document.getElementById("count").innerHTML=p.count;
		        		child.document.getElementById("total").innerHTML=p.total;
						var tab=child.document.getElementById('tableId');  
						for(var a=0;a<p.list.length;a++){
							var tr=tab.insertRow(a+1);  
							var td=tr.insertCell(0);
							td.innerHTML=p.list[a].goodName; 
							td=tr.insertCell(1);
							td.innerHTML=p.list[a].price; 
							td=tr.insertCell(2);
							td.innerHTML=p.list[a].number;  
							td=tr.insertCell(3);
							td.innerHTML=p.list[a].ant;  
						}
					},
					failure: function(result,options){
						var responseObj =  Ext.decode(result.responseText);
						Ext.MessageBox.alert('提示',responseObj.appMsg);
					}});
              	 
              };  
	   }
	   },
	   buttonAlign:'center',
	      buttons:[{  
	          	  text:'打印',
	          	  formBind: true,
	        	  handler: function() {
	        		  var child = document.getElementById("mainFrame").contentWindow;
	        		  child.document.getElementById("biuuu_button").click();
	          	  }
	      },{  
	          	  text:'取消',
	        	  handler: function() {
	        		  windowMap.close();
	    		}
	      }]
  }); 
  
  
  var editwindow = new Ext.Window({  
      title:'司机审核',  
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
          url: '../../sys/thirdmall/thirdMallOrderModify.action',
          labelWidth:30,  
          items:[
		{  
		    id:'orderId',
		    name:'orderId',
		    labelWidth:40,  
		    xtype:'hiddenfield',  
		},
		{
  	    	xtype: 'displayfield',
  	    	fieldLabel: '发票类型',
  	    	id:'billType',
  	    	name: 'billType',
  	    },
	   {
  	        xtype: 'displayfield',
  	        fieldLabel: '接受人名称',
  	        id:'receiveName',
  	        name: 'receiveName',
  	    },
  	    {
  	    	xtype: 'displayfield',
  	    	fieldLabel: '接受人手机',
  	    	id:'receiveMobile',
  	    	name: 'receiveMobile',
  	    },
  	    {
  	    	xtype: 'displayfield',
  	    	fieldLabel: '接受人地址',
  	    	id:'receiveAdress',
  	    	name: 'receiveAdress',
  	    },
  	    {
  	    	xtype: 'displayfield',
  	    	fieldLabel: '接受人详细地址',
  	    	id:'receiveAdressDtl',
  	    	name: 'receiveAdressDtl',
  	    },
  	    {
  	    	xtype: 'displayfield',
  	    	fieldLabel: '抬头',
  	    	id:'billName',
  	    	name: 'billName',
  	    },
  	    {
  	    	xtype: 'displayfield',
  	    	fieldLabel: '纳税人识别号',
  	    	id:'billRecognition',
  	    	name: 'billRecognition',
  	    },
  	    {
  	    	xtype: 'displayfield',
  	    	fieldLabel: '公司地址',
  	    	id:'billAddress',
  	    	name: 'billAddress',
  	    },
  	    {
  	    	xtype: 'displayfield',
  	    	fieldLabel: '公司详细地址',
  	    	id:'billAddressDtl',
  	    	name: 'billAddressDtl',
  	    },
  	    {
  	    	xtype: 'displayfield',
  	    	fieldLabel: '公司电话',
  	    	id:'billPhone',
  	    	name: 'billPhone',
  	    },
  	    {
  	    	xtype: 'displayfield',
  	    	fieldLabel: '开户银行',
  	    	id:'bank',
  	    	name: 'bank',
  	    },
  	    {
  	    	xtype: 'displayfield',
  	    	fieldLabel: '银行账号',
  	    	id:'bankAcc',
  	    	name: 'bankAcc',
  	    },
  	    {
  	    	xtype: 'displayfield',
  	    	fieldLabel: '邮费',
  	    	id:'mailCharge',
  	    	name: 'mailCharge',
  	    },
  	    {
  	    	xtype: 'displayfield',
  	    	fieldLabel: '税费',
  	    	id:'taxCharge',
  	    	name: 'taxCharge',
  	    }
          ],
	      buttonAlign:'center',
	      buttons:[{  
	          	  text:'确认已开发票',
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
});