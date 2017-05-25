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
        fields: ['realName','mobile','plateNumber','orderNo','rechargeMuch','tradeType','createdDate','payPoint','returnPoint','specialBalance','isSuccess','rechargeTotal'],
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
            url: '../../sys/statistics/userjyRechargeRequest.action',
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
    		//paras.checkStatus = Ext.getCmp("bsc").getValue();
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
	var tbar = Ext.create("Ext.Toolbar", {
		flex:1,
		id:'toolbar',
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
			        text:'结算',
			        id:'balanceButtion',
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
			        	if(records[0].data.isSuccess==true){
			        		Ext.MessageBox.alert("提示","已结算！");
			        		return;
			        	}
			        	Ext.Ajax.request({url:'../../sys/statistics/queryjyRechargeRequest.action',  params : {tradeId:records[0].data.id},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							Ext.getCmp("tradeId").setValue(responseObj.data['id']);
							Ext.getCmp("recharge").setValue(responseObj.data['recharge']);
							Ext.getCmp("orderNeedPoints").setValue(responseObj.data['orderNeedPoints']);
							Ext.getCmp("userPoints").setValue(responseObj.data['userPoints']);
							Ext.getCmp("orderNo_").setValue(responseObj.data['orderNo']);
							Ext.getCmp("points").setValue(responseObj.data['points']);
							Ext.getCmp("total").setValue(responseObj.data['total']);
							var h = Ext.getBody().getHeight();
							windowshow.setHeight(h);
							windowshow.show();
						},
						failure: function(result,options){
							var responseObj =  Ext.decode(result.responseText);
							Ext.MessageBox.alert('提示',responseObj.appMsg);
						}});
			        }
	            },
	            '-','->',{width:180,labelWidth:65,name:'orderNo',id:'orderNo',xtype:'textfield',fieldLabel:'订单编号'},'-',
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
	// ['realName','mobile','idCard','isLocked','consignorType','loginDate','loginIp','cities','registrationPic',
    //'address','isAuth','fax','registerTime','isPerfect'
	var record_start = 0;
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        id:'grid',
        autoExpandColumn: 'name',
        title: '公司流水',
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
			})
			,
			{text: '订单编号',
            	dataIndex: 'orderNo',
            	flex: 2,
            	sortable: true
            },
            {text: '司机名称',
            	dataIndex: 'realName',
            	flex: 1,
            	sortable: true
            },
        	{text: '手机号码',
                dataIndex: 'mobile',
                flex: 1,
                sortable: true
            },
            {text: '车牌号码',
            	dataIndex: 'plateNumber',
            	flex: 1,
            	sortable: true
            }
        	,{text: '充值数额',
            	dataIndex: 'rechargeMuch',
            	flex: 1,
            	sortable: true
            },{text: '交易类型',
            	dataIndex: 'tradeType',
            	flex: 1,
            	sortable: true,
            	renderer: function (value, meta, record) {
            		if(value==-1){
            			return "高速充值";
            		}else{
            			return "油卡充值";  
            		}
            	}
            }
            ,{text: '需要支付的点数',
            	dataIndex: 'payPoint',
            	flex: 1,
            	sortable: true
            }
            ,{text: '返点数',
            	dataIndex: 'returnPoint',
            	flex: 1,
            	sortable: true
            }
            ,{text: '专项费用',
            	dataIndex: 'specialBalance',
            	flex: 1,
            	sortable: true
            }
            ,{text: '实际充值总额',
            	dataIndex: 'rechargeTotal',
            	flex: 1,
            	sortable: true
            }
            ,{text: '是否结算',
            	dataIndex: 'isSuccess',
            	flex: 1,
            	sortable: true,
            	renderer: function (value, meta, record) {
            		if(value==false){
            			return "<span style='color:red;font-weight:bold;'>未结算</span>";
            		}else{
            			return "已结算";  
            		}
            	}
            }
        	],
        forceFit: true,
        // paging bar on the bottom
        bbar: bbar
    });
  Ext.create('Ext.Viewport',{
		layout: 'fit',items:[grid],renderTo: 'btsAlarm-grid'
	});
  
  
  var windowshow = new Ext.Window({  
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
          url: '../../sys/statistics/balanceUserjyRechargeRequest.action',
          labelWidth:30,  
          //fields: ['id','path','descript','price','type','discount','discountPrice','discountPoint','cost'
          items:[
	  		{  
	  		    id:'tradeId',
	  		    name:'tradeId',
	  		    labelWidth:120,  
	  		    xtype:'hiddenfield',  
	  		},
	  		{  
		      	  id:'orderNo_',
		      	  name:'orderNo_',
		      	  disabled:false,
		      	  labelWidth:120,
		      	  readOnly:true,
		      	  width : 65,
	              xtype:'textfield',  
	              fieldLabel:'订单号'
		  	    },
	  		{  
		      	  id:'orderNeedPoints',
		      	  name:'orderNeedPoints',
		      	  disabled:false,
		      	  readOnly:true,
		      	  labelWidth:120,
		      	  width : 65,
	              xtype:'textfield',  
	              fieldLabel:'本单最多可用驿道币'
		  	    },
	  	  {  
		      	  id:'userPoints',
		      	  name:'userPoints',
		      	  readOnly:true,
		      	  labelWidth:120,
		      	  width : 65,
	              xtype:'textfield',  
	              fieldLabel:'该用户所剩驿道币'
		  	    },
	  	    {  
	      	  id:'points',
	      	  name:'points',
	      	readOnly:true,
	      	  labelWidth:120,
	      	  width : 65,
              xtype:'textfield',  
              fieldLabel:'本次所用驿道币'
	  	    },
	  	    {  
	      	  id:'recharge',
	      	  name:'recharge',
	      	  readOnly:true,
	      	  labelWidth:160,
	      	  width : 65,
              xtype:'textfield',  
              fieldLabel:'充值额度'
	  	    },
	  	    {  
	      	  id:'total',
	      	  name:'total',
	      	   readOnly:true,
	      	  labelWidth:160,
	      	  width : 65,
	            xtype:'textfield',  
	            fieldLabel:'本次充值总额度'
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
	                             windowshow.close();
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
	        		  windowshow.close();
	    		}
	      }]
      }
      ]
  });  
});