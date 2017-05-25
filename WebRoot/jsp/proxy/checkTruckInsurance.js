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
	
    Ext.define('travelModel', {
        extend: 'Ext.data.Model',
        fields: ['orderNo','policyNo','goodsName','weigthOrCount','packQty','cargoNo','goodsTypeNo','mainGlausesCode','insuredAmount','truckType',
                 'transportType','truckWeight','transportNo','fromLoc','toLoc','departureDate','effDate','recognizeeName','recognizeePhone','cardNo','policyNo',
                 'company','status','created_date','policyPath','days','endDate','personsCount','registerNo','lisencePath'
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
            url: '../../sys/proxy/truckInsuranceByPage.action',
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
				
    var checkStatusStore2 = Ext.create('Ext.data.Store', {
		fields: ['code', 'name'],
		 data : [
		 {"code":"false","name":"审核不通过"},
		 {"code":"true","name":"审核通过"}
		 ]
	});
    
    var queryButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'query', iconCls:'zoom',text: '查 询',
    	handler: function() {
    		paras.orderNo = encodeURIComponent(Ext.getCmp("orderNo").getValue());
    		paras.status = Ext.getCmp("status").getValue();
    		//paras.date = Ext.getCmp("datetime").getValue();
    		paras.startTime  = Ext.util.Format.date(Ext.getCmp("startTime").getValue(), 'Y-m-d');
    		paras.endTime  = Ext.util.Format.date(Ext.getCmp("endTime").getValue(), 'Y-m-d');
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
		 {"code":"1","name":"等待付款"},
		 {"code":"2","name":"完成交易"},
		 {"code":"3","name":"审核中"},
		 {"code":"4","name":"拒保"}
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
	            },
	            '-',{
			        iconCls:'refush',
			        text:'审核',
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
			        	if(records[0].data.status!='审核中'){
			        		Ext.MessageBox.alert("提示","不是待审核状态,不能审核！");
			        		return;
			        	}
			        	var form = Ext.getCmp('editform');
						form.getForm().reset();
						var location = (window.location+'').split('/'); 
		            	var basePath = location[0]+'//'+location[2]+'/'+'hyt';
		            	Ext.getCmp('xszzp').setSrc(basePath+"/"+records[0].data.lisencePath);
						Ext.getCmp('id').setValue(records[0].data.id);
	                	Ext.getCmp('orderNo').setValue(records[0].data.orderNo);
				  		Ext.getCmp('insuredAmount').setValue(records[0].data.insuredAmount);
				  		Ext.getCmp('departureDate').setValue(records[0].data.departureDate);
				  		Ext.getCmp('recognizeeName').setValue(records[0].data.recognizeeName);
				  		Ext.getCmp('cardNo').setValue(records[0].data.cardNo);
				  		Ext.getCmp('recognizeePhone').setValue(records[0].data.recognizeePhone);
				  		Ext.getCmp('peopleNum').setValue(records[0].data.personsCount);
				  		var h = Ext.getBody().getHeight();
				  		var w = Ext.getBody().getWidth();
				  		checkwindow.setHeight(h);
				  		checkwindow.setWidth(w);
			        	checkwindow.show();
			        }
	            },
//	            '-',{
//			        iconCls:'refush',
//			        text:'查看保单',
//			        xtype: 'button',
//			        handler: function () {
//			        	var records = grid.getSelectionModel().getSelection();
//			        	if(records==null||records.length<=0){
//			        		Ext.MessageBox.alert("提示","请选择一项");
//			        		return;
//			        	}
//			        	if(records.length>1){
//			        		Ext.MessageBox.alert("提示","本操作最多只能选择一项");
//			        		return;
//			        	}
//	                	var location = (window.location+'').split('/'); 
//	                	var basePath = location[0]+'//'+location[2]+'/'+'ydypf';
//	                	var p = records[0].data.policyPath;
//	                	if(p!=null&&p!=""){
//	                		Ext.getCmp("path").update('<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+basePath+'/'+p+'"></iframe>');
//	                		var h = Ext.getBody().getHeight();
//	                		showwindow.setHeight(h);
//	                		showwindow.show();
//	                	}else{
//	                		Ext.getCmp("path").update("");
//	                		Ext.MessageBox.alert("提示","还没有保单");
//	                	}
//			        }
//	            },
	            '-','->',
				{width:210,forceSelection: true,xtype: 'combobox',fieldLabel:'审核状态:',labelWidth: 65,anchor: '100%',
					store: checkStatusStore,emptyText :'请选择...',queryMode: 'local',displayField: 'name',valueField: 'code',
					id:'status',name:'status'},'-',
					{width:180,labelWidth:50,name:'orderNo',id:'orderNo',xtype:'textfield',fieldLabel:'订单号'},'-',
					{
				        xtype: 'datefield',
				        id:'startTime',
				        labelWidth: 65,
				        fieldLabel: '开始时间',
				        name: 'startTime',
				        format: 'Y-m-d'
				    },
				    {
				        xtype: 'datefield',
				        id:'endTime',
				        labelWidth: 65,
				        fieldLabel: '结束时间',
				        name: 'endTime',
				        format: 'Y-m-d'
				    },
					queryButton
	    ]
	});
	
    //var pluginExpanded = true;
	//fields: ['driverName','mobile','plateNumber','idCardNumber','idCardPic','drivingLicenseNumber','drivingLicensePic','registrationNumber','registrationPic',
      //       'registrationPic','lockedDate','loginDate','loginIp','isCheck','truckType','deadWeight','loadStatus'
	var record_start = 0;
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        id:'grid',
        autoExpandColumn: 'name',
        title: '保险列表',
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
        	{text: '订单号',
	            dataIndex: 'orderNo',
	            flex: 2,
	            sortable: true
        	},
        	{text: '保额(万元/人)',
            	dataIndex: 'insuredAmount',
            	flex: 1,
            	sortable: true
            },
        	{text: '开始时间',
            	dataIndex: 'departureDate',
            	flex: 2,
            	sortable: true,
            	renderer: function(value, m, record) { 
         			return record.get("departureDate");
         		}
            },
            {text: '生成时间',
            	dataIndex: 'created_date',
            	flex: 2,
            	sortable: true
            },{text: '状态',
            	dataIndex: 'status',
            	flex: 1,
            	sortable: true,
            	renderer: function(value, m, record) { 
            		if(value=='审核中'){
            			return "<span style='color:red;font-weight:bold;'>审核中</span>";
            		}else{
            			return value;  
            		}
         		}
            },{text: '保单号/拒保原因',
                dataIndex: 'policyNo',
                flex: 2,
                sortable: true
            }
        	],
        	listeners: {
                itemdblclick: function (me, record, item, index, e, eOpts) {
                	var form = Ext.getCmp('detailform');
					form.getForm().reset();
					var location = (window.location+'').split('/'); 
	            	var basePath = location[0]+'//'+location[2]+'/'+'hyt';
	            	Ext.getCmp('xszzp2').setSrc(basePath+"/"+record.data.lisencePath);
                	Ext.getCmp('id2').setValue(record.data.id);
                	Ext.getCmp('orderNo2').setValue(record.data.orderNo);
			  		Ext.getCmp('insuredAmount2').setValue(record.data.insuredAmount);
			  		Ext.getCmp('departureDate2').setValue(record.data.departureDate);
			  		Ext.getCmp('recognizeeName2').setValue(record.data.recognizeeName);
			  		Ext.getCmp('cardNo2').setValue(record.data.cardNo);
			  		Ext.getCmp('recognizeePhone2').setValue(record.data.recognizeePhone);
			  		Ext.getCmp('peopleNum2').setValue(record.data.personsCount);
			  		var h = Ext.getBody().getHeight();
			  		var w = Ext.getBody().getWidth();
			  		detailwindow.setHeight(h);
			  		detailwindow.setWidth(w);
			  		detailwindow.show();
                }
            },
        forceFit: true,
        // paging bar on the bottom
        bbar: bbar
    });
  Ext.create('Ext.Viewport',{
		layout: 'fit',items:[grid],renderTo: 'btsAlarm-grid'
	});
  var showwindow = new Ext.Window({  
      title:'保险单',  
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
  
  
  
  var  checkwindow = new Ext.Window({  
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
          id:'editform',
          baseCls:'x-plain',  
          xtype:'form',
          style:'padding:8px;',  
          url: '../../sys/proxy/checkTruckUInsurance.action',
          labelWidth:40,  
          items:[
                 {
				    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
				    xtype:'fieldset',
				    title: '保险信息',
				    collapsible: true,
				    layout:'form',
				    items :[
					  		{  
					  		    id:'id',
					  		    name:'id',
					  		    labelWidth:120,  
					  		    xtype:'hiddenfield',  
					  		},
					  		 {
					  	        xtype: 'displayfield',
					  	        fieldLabel: '订单号',
					  	        name: 'orderNo',
					  	        id:'orderNo',
					  	        value: '11'
					  	    }, {
					  	        xtype: 'displayfield',
					  	        fieldLabel: '保险金额(万元/人)',
					  	        name: 'insuredAmount',
					  	        id:'insuredAmount',
					  	        labelWidth:120,  
					  	        value: '11'
					  	    },
					  	  {
					  	        xtype: 'displayfield',
					  	        fieldLabel: '开始时间',
					  	        name: 'departureDate',
					  	        id:'departureDate',
					  	        value: '11'
					  	    }
							, {
					  	        xtype: 'displayfield',
					  	        fieldLabel: '核载人数',
					  	        name: 'peopleNum',
					  	        id:'peopleNum',
					  	        value: '11'
					  	    }
					  	    ]
				      }, {
						  xtype:'fieldset',
						    title: '被保人信息',
						    collapsible: true,
						    layout:'form',
						  items :[
							{
						      xtype: 'displayfield',
						      fieldLabel: '姓名/单位',
						      name: 'recognizeeName',
						      id:'recognizeeName',
						      value: '11'
						  }
							, {
						      xtype: 'displayfield',
						      fieldLabel: '被保人电话',
						      name: 'recognizeePhone',
						      id:'recognizeePhone',
						      value: '11'
						  }
							, {
						      xtype: 'displayfield',
						      labelWidth:120,  
						      fieldLabel: '身份证/机构代码证',
						      name: 'cardNo',
						      id:'cardNo',
						      value: '11'
						  }
						]
						},
						{

							  xtype:'fieldset',
							    title: '行驶有证',
							    collapsible: true,
							    layout:'form',
							  items :[
										Ext.create('Ext.Img', {
											  id:'xszzp',
											  height:350,
											  width : 600,
											  name:'jszp',
										 src: '../../img/pic.png'
										})
							]
						},
						{
				        	width:210,
				        	forceSelection: true,
				        	xtype: 'combobox',
				        	fieldLabel:'审核状态:',
				        	labelWidth: 55,
				        	anchor: '100%',
							store: checkStatusStore2,
							emptyText :'请选择...',
							queryMode: 'local',
							displayField: 'name',
							valueField: 'code',
							id:'checked',
							name:'checked',
							listeners : {
							       afterRender : function(combo) {
							    	   if(this.getValue()==null){
							    		   combo.setValue('true');
							    		   Ext.getCmp('tips').setVisible(false);
							    		   Ext.getCmp('policy').setValue("");
										   Ext.getCmp('policy').setVisible(true);
							    	   }
							       },
									select : function(combo,record){
										if(combo.getValue()=='true'){
											Ext.getCmp('tips').setVisible(false);
											Ext.getCmp('policy').setValue("");
											Ext.getCmp('policy').setVisible(true);
										}else{
											Ext.getCmp('tips').setVisible(true);
											Ext.getCmp('policy').setValue(" ");
											Ext.getCmp('policy').setVisible(false);
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
							{boxLabel:'资料不合法',name:'tips',inputValue:2},
							{boxLabel:'其它原因',name:'tips',inputValue:3}
							]},
							
	    {  
			  id:'policy',
			  name:'policy',
			  allowBlank:false,
			  blankText:'保单号',
			  labelWidth:60,  
			  xtype:'textfield',  
			  fieldLabel:'保单号',  
			  anchor:'90%'  
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
	                             checkwindow.close();
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
	        		  checkwindow.close();
	    		}
	      }]
      }
      ]
  });  
  
  
  var  detailwindow = new Ext.Window({  
      title:'确认信息',  
      width:600,  
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
          url: '../../sys/proxy/checkChinaLife.action',
          labelWidth:80,  
          items:[
					{
				    //第 1 列中的  Fieldset - 通过toggle 按钮来 收缩/展开
				    xtype:'fieldset',
				    title: '保险信息',
				    collapsible: true,
				    layout:'form',
				    labelWidth:80,  
				    items :[
					  		{  
					  		    id:'id2',
					  		    name:'id',
					  		    xtype:'hiddenfield',  
					  		},
					  		 {
					  	        xtype: 'displayfield',
					  	        fieldLabel: '订单号',
					  	        name: 'orderNo',
					  	        id:'orderNo2',
					  	        value: '11'
					  	    }, {
					  	        xtype: 'displayfield',
					  	      labelWidth:120,  
					  	        fieldLabel: '保险金额(万元/人)',
					  	        name: 'insuredAmount',
					  	        id:'insuredAmount2',
					  	        value: '11'
					  	    },
					  	  {
					  	        xtype: 'displayfield',
					  	        fieldLabel: '开始时间',
					  	        name: 'departureDate',
					  	        id:'departureDate2',
					  	        value: '11'
					  	    }
							, {
					  	        xtype: 'displayfield',
					  	        fieldLabel: '核保人数',
					  	        name: 'peopleNum',
					  	        id:'peopleNum2',
					  	        value: '11'
					  	    }
					  	    ]
				      }, {
					    	  xtype:'fieldset',
					    	    title: '被保人信息',
					    	    collapsible: true,
					    	    layout:'form',
					          items :[
						  	{
					  	        xtype: 'displayfield',
					  	        fieldLabel: '姓名/单位',
					  	        name: 'recognizeeName',
					  	        id:'recognizeeName2',
					  	        value: '11'
					  	    }
						  	, {
					  	        xtype: 'displayfield',
					  	        fieldLabel: '被保人电话',
					  	        name: 'recognizeePhone',
					  	        id:'recognizeePhone2',
					  	        value: '11'
					  	    }
						  	, {
					  	        xtype: 'displayfield',
					  	        labelWidth:120,  
					  	        fieldLabel: '身份证/机构代码证',
					  	        name: 'cardNo',
					  	        id:'cardNo2',
					  	        value: '11'
					  	    }
					        ]
					      },
					      {

							  xtype:'fieldset',
							    title: '行驶有证',
							    collapsible: true,
							    layout:'form',
							  items :[
										Ext.create('Ext.Img', {
											  id:'xszzp2',
											  height:350,
											  width : 600,
											  name:'jszp',
										 src: '../../img/pic.png'
										})
							]
						}
				      ],
	      buttonAlign:'right',
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
  
});