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
        fields: ['orderNo','policyNo','recognizeePhone','goodsName','weigthOrCount','packQty','cargoNo','goodsTypeNo','mainGlausesCode','insuredAmount','truckType',
                 'transportType','truckWeight','transportNo','fromLoc','toLoc','departureDate','effDate','recognizeeName','recognizeePhone','cardNo','policyNo',
                 'company','status','created_date','policyPath'
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
            url: '../../sys/proxy/insuranceChinaLifeByPage.action',
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
	            },'-',{
			        iconCls:'refush',
			        text:'撤单',
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
			        		Ext.MessageBox.alert("提示","不是待审核状态,不能撤单！");
			        		return;
			        	}
			        	Ext.MessageBox.confirm("提示","确定要撤单吗？",function(but){
			        		if(but=='yes'){
			        			Ext.Ajax.request({url:'../../sys/proxy/cancleChinaLifeInsurance.action',  
		    	            		params : {id:records[0].data.id},
		    	            		method:'post',
		    	            		type:'json',  
		    	            		success:function(result, request){
										var responseObj =  Ext.decode(result.responseText);
										if(responseObj.success!=null&&!responseObj.success){
											Ext.MessageBox.alert("错误提示",responseObj.appMsg);
											return;
										}
										Ext.MessageBox.alert("提示",responseObj.appMsg);
										store.load();
									},
									failure: function(result,options){
										var responseObj =  Ext.decode(result.responseText);
										Ext.MessageBox.alert('提示',responseObj.appMsg);
									}
		    	            	});
			        		}
			        	});
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
				  		Ext.getCmp('id').setValue(records[0].data.id);
				  		Ext.getCmp('goodsName').setValue(records[0].data.goodsName);
				  		Ext.getCmp('weigthOrCount').setValue(records[0].data.weigthOrCount);
				  		Ext.getCmp('packQty').setValue(records[0].data.packQty);
				  		Ext.getCmp('cargoNo').setValue(records[0].data.cargoNo);
				  		Ext.getCmp('mainGlausesCode').setValue(records[0].data.mainGlausesCode);
				  		Ext.getCmp('insuredAmount').setValue(records[0].data.insuredAmount);
				  		Ext.getCmp('transportType').setValue(records[0].data.transportType);
				  		Ext.getCmp('transportNo').setValue(records[0].data.transportNo);
				  		Ext.getCmp('truckWeight').setValue(records[0].data.truckWeight);
				  		Ext.getCmp('truckType').setValue(records[0].data.truckType);
				  		Ext.getCmp('fromLoc').setValue(records[0].data.fromLoc);
				  		Ext.getCmp('toLoc').setValue(records[0].data.toLoc);
				  		Ext.getCmp('departureDate').setValue(records[0].data.departureDate);
				  		Ext.getCmp('recognizeeName').setValue(records[0].data.recognizeeName);
				  		Ext.getCmp('recognizeePhone').setValue(records[0].data.recognizeePhone);
				  		Ext.getCmp('cardNo').setValue(records[0].data.cardNo);
				  		var h = Ext.getBody().getHeight();
				  		checkwindow.setHeight(h);
			        	checkwindow.show();
			        }
	            },
	            '-',{
			        iconCls:'refush',
			        text:'查看保单',
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
	                	var location = (window.location+'').split('/'); 
	                	var basePath = location[0]+'//'+location[2]+'/'+'ydypf';
	                	var p = records[0].data.policyPath;
	                	if(p!=null&&p!=""){
	                		Ext.getCmp("path").update('<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+basePath+'/'+p+'"></iframe>');
	                		var h = Ext.getBody().getHeight();
	                		showwindow.setHeight(h);
	                		showwindow.show();
	                	}else{
	                		Ext.getCmp("path").update("");
	                		Ext.MessageBox.alert("提示","还没有保单");
	                	}
			        }
	            },
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
        	{text: '保额(万元)',
            	dataIndex: 'insuredAmount',
            	flex: 1,
            	sortable: true
            },
        	{text: '货物名称',
                dataIndex: 'goodsName',
                flex: 1,
                sortable: true
            },
        	{text: '被保人电话',
                dataIndex: 'recognizeePhone',
                flex: 1,
                sortable: true
            },
        	{text: '货物类型',
            	dataIndex: 'mainGlausesCode',
            	flex: 1,
            	sortable: true
            },
            {text: '车牌号',
            	dataIndex: 'transportNo',
            	flex: 1,
            	sortable: true
            },{text: '起始地',
            	dataIndex: 'fromLoc',
            	flex: 2,
            	sortable: true
            },{text: '目的地',
            	dataIndex: 'toLoc',
            	flex: 2,
            	sortable: true
            },{text: '启运时间',
            	dataIndex: 'departureDate',
            	flex: 2,
            	sortable: true
            },{text: '生成时间',
            	dataIndex: 'created_date',
            	flex: 2,
            	sortable: true
            },{text: '状态',
            	dataIndex: 'status',
            	flex: 1,
            	sortable: true
            },{text: '保单号',
                dataIndex: 'policyNo',
                flex: 2,
                sortable: true
            }
        	],
        	listeners: {
                itemdblclick: function (me, record, item, index, e, eOpts) {
                	Ext.getCmp('id2').setValue(record.data.id);
			  		Ext.getCmp('goodsName2').setValue(record.data.goodsName);
			  		Ext.getCmp('weigthOrCount2').setValue(record.data.weigthOrCount);
			  		Ext.getCmp('packQty2').setValue(record.data.packQty);
			  		Ext.getCmp('cargoNo2').setValue(record.data.cargoNo);
			  		Ext.getCmp('mainGlausesCode2').setValue(record.data.mainGlausesCode);
			  		Ext.getCmp('insuredAmount2').setValue(record.data.insuredAmount);
			  		Ext.getCmp('transportType2').setValue(record.data.transportType);
			  		Ext.getCmp('transportNo2').setValue(record.data.transportNo);
			  		Ext.getCmp('truckWeight2').setValue(record.data.truckWeight);
			  		Ext.getCmp('truckType2').setValue(record.data.truckType);
			  		Ext.getCmp('fromLoc2').setValue(record.data.fromLoc);
			  		Ext.getCmp('toLoc2').setValue(record.data.toLoc);
			  		Ext.getCmp('departureDate2').setValue(record.data.departureDate);
			  		Ext.getCmp('recognizeeName2').setValue(record.data.recognizeeName);
			  		Ext.getCmp('cardNo2').setValue(record.data.cardNo);
			  		Ext.getCmp('effDate2').setValue(record.data.effDate);
			  		Ext.getCmp('recognizeePhone2').setValue(record.data.recognizeePhone);
			  		var h = Ext.getBody().getHeight();
			  		detailwindow.setHeight(h);
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
          url: '../../sys/proxy/checkChinaLife.action',
          labelWidth:30,  
          items:[
	  		{  
	  		    id:'id',
	  		    name:'id',
	  		    labelWidth:120,  
	  		    xtype:'hiddenfield',  
	  		},
	  		{
	  	        xtype: 'displayfield',
	  	        fieldLabel: '货物名称',
	  	        id:'goodsName',
	  	        name: 'goodsName',
	  	        value: '10'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '货物重量/件数',
	  	        id:'weigthOrCount',
	  	        name: 'weigthOrCount',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '包装种类',
	  	        name: 'packQty',
	  	        id:'packQty',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '运单号/票号',
	  	        name: 'cargoNo',
	  	        id:'cargoNo',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '保险类型',
	  	        name: 'mainGlausesCode',
	  	        id:'mainGlausesCode',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '保险金额(万元)',
	  	        name: 'insuredAmount',
	  	        id:'insuredAmount',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '运输方式',
	  	        name: 'transportType',
	  	        id:'transportType',
	  	        value: '11'
	  	    }
	  	  , {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '车吨位',
	  	        name: 'truckWeight',
	  	        id:'truckWeight',
	  	        value: '11'
	  	    }
	  	  , {
	  		  xtype: 'displayfield',
	  		  fieldLabel: '厂牌型号',
	  		  name: 'truckType',
	  		  id:'truckType',
	  		  value: '11'
	  	  }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '车牌号',
  	        name: 'transportNo',
  	        id:'transportNo',
  	        value: '11'
  	    }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '启运地',
  	        name: 'fromLoc',
  	        id:'fromLoc',
  	        value: '11'
  	    }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '目的地',
  	        name: 'toLoc',
  	        id:'toLoc',
  	        value: '11'
  	    }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '启运时间',
  	        name: 'departureDate',
  	        id:'departureDate',
  	        value: '11'
  	    }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '姓名/单位',
  	        name: 'recognizeeName',
  	        id:'recognizeeName',
  	        value: '11'
  	    }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '联系电话',
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
  	    },
	  	{
	        xtype: 'filefield',
	        name: 'photo',
	        fieldLabel: '保单上传',
	        labelWidth: 120,
	        msgTarget: 'side',
	        allowBlank: false,
	        anchor: '100%',
	        buttonText: '保单上传'
	    },
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
	  		    id:'id2',
	  		    name:'id',
	  		    labelWidth:120,  
	  		    xtype:'hiddenfield',  
	  		},
	  		{
	  	        xtype: 'displayfield',
	  	        fieldLabel: '货物名称',
	  	        id:'goodsName2',
	  	        name: 'goodsName',
	  	        value: '10'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '货物重量/件数',
	  	        id:'weigthOrCount2',
	  	        name: 'weigthOrCount',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '包装种类',
	  	        name: 'packQty2',
	  	        id:'packQty2',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '运单号/票号',
	  	        name: 'cargoNo2',
	  	        id:'cargoNo2',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '保险类型',
	  	        name: 'mainGlausesCode',
	  	        id:'mainGlausesCode2',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '保险金额(万元)',
	  	        name: 'insuredAmount',
	  	        id:'insuredAmount2',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '运输方式',
	  	        name: 'transportType',
	  	        id:'transportType2',
	  	        value: '11'
	  	    }
	  	  , {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '车吨位',
	  	        name: 'truckWeight',
	  	        id:'truckWeight2',
	  	        value: '11'
	  	    }
	  	  , {
	  		  xtype: 'displayfield',
	  		  fieldLabel: '厂牌型号',
	  		  name: 'truckType',
	  		  id:'truckType2',
	  		  value: '11'
	  	  }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '车牌号',
  	        name: 'transportNo',
  	        id:'transportNo2',
  	        value: '11'
  	    }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '启运地',
  	        name: 'fromLoc',
  	        id:'fromLoc2',
  	        value: '11'
  	    }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '目的地',
  	        name: 'toLoc',
  	        id:'toLoc2',
  	        value: '11'
  	    }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '启运时间',
  	        name: 'departureDate',
  	        id:'departureDate2',
  	        value: '11'
  	    }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '签约时间',
  	        name: 'effDate',
  	        id:'effDate2',
  	        value: '11'
  	    }
	  	, {
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
});