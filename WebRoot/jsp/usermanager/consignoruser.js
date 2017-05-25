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
            url: '../../sys/member/consignorUserByPage.action',
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
    		paras.phone = encodeURIComponent(Ext.getCmp("phoneSearch").getValue());
    		paras.code = encodeURIComponent(Ext.getCmp("code").getValue());
    		paras.checkStatus = Ext.getCmp("bsc").getValue();
    		//paras.date = Ext.getCmp("datetime").getValue();
    		paras.date  = Ext.util.Format.date(Ext.getCmp("datetime").getValue(), 'Y-m-d');
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
			        text:'续服务费',
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
			        	var balanceform = Ext.getCmp("balanceform").getForm();
			        	balanceform.reset();
			        	Ext.getCmp("balanceConsignorId").setValue(records[0].data.id);
			        	var h = Ext.getBody().getHeight();
			        	balancewindow.setHeight(h);
			        	balancewindow.show();
			        }
	            },'-',{
			        iconCls:'refush',
			        text:'用户退款',
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
	            },
	            '-','->',
				{width:210,forceSelection: true,xtype: 'combobox',fieldLabel:'审核状态:',labelWidth: 70,anchor: '100%',
					store: checkStatusStore,emptyText :'请选择...',queryMode: 'local',displayField: 'name',valueField: 'code',
					id:'bsc',name:'bsc'},'-',
					{width:180,labelWidth:50,name:'titleSearch',id:'titleSearch',xtype:'textfield',fieldLabel:'用户名'},'-',
					{width:180,labelWidth:50,name:'phoneSearch',id:'phoneSearch',xtype:'textfield',fieldLabel:'手机号'},'-',
					{width:180,labelWidth:50,name:'code',id:'code',xtype:'textfield',fieldLabel:'注册码'},'-',
					{
				        xtype: 'datefield',
				        id:'datetime',
				        labelWidth: 70,
				        fieldLabel: '注册时间',
				        name: 'datetime',
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
        title: '货主列表',
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
            mode: "SIMPLE",     //
            checkOnly: true     //
        },
        selType: "checkboxmodel",
        listeners: {
            itemdblclick: function (me, record, item, index, e, eOpts) {
               var p = record.data;
	           Ext.getCmp('realNamed').setValue(p.realName);
               Ext.getCmp('mobiled').setValue(p.mobile);
               Ext.getCmp('idCardd').setValue(p.idCard);
               Ext.getCmp('proxyd').setValue(p.proxy);
               Ext.getCmp('addressd').setValue(p.address);
               Ext.getCmp('registerTimed').setValue(p.registerTime);
               Ext.getCmp('expireTimed').setValue(p.expireTime);
               Ext.getCmp('balanced').setValue(p.balance);
               Ext.getCmp('pointsd').setValue(p.points);
               var h = Ext.getBody().getHeight();
               detailwindow.setHeight(h);
               detailwindow.show();          
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
            },
        	{text: '地址',
            	dataIndex: 'address',
            	flex: 2,
            	sortable: true
            },{text: '传真',
            	dataIndex: 'fax',
            	flex: 2,
            	sortable: true
            },{text: '是否审核',
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
							var form = Ext.getCmp('editform');
							form.getForm().reset();
							Ext.getCmp('editConsignorId').setValue(id);
							Ext.getCmp('realName').setValue(responseObj.data['realName']);
							Ext.getCmp('mobile').setValue(responseObj.data['mobile']);
							Ext.getCmp('editidCard').setValue(responseObj.data['idCard']);
							var h = Ext.getBody().getHeight();
							editwindow.setHeight(h);
							editwindow.show();
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
			            	if(responseObj.data['idCardPic']!=null&&responseObj.data['idCardPic']!=''){
			            		var idCardPicsrc = responseObj.data['idCardPic'];
			            		if(responseObj.data['idCardPic'].indexOf(".webp")>-1){
			            			//idCardPicsrc = responseObj.data['idCardPic'].replace(".webp",".jpg");
			            		}
			            		Ext.getCmp('idCardPic').setSrc(basePath+"/"+idCardPicsrc);
			            	}
							Ext.getCmp('officePic').setSrc(basePath+"/"+responseObj.data['officePic']);
							Ext.getCmp('consignorId').setValue(id);
							Ext.getCmp('checkidCard').setValue(responseObj.data['idCard']);
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
		layout: 'fit',items:[grid],renderTo: 'btsAlarm-grid'
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
      title:'货主审核',  
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
			    text: '身份证正面：'
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
           	  id:'checkidCard',
           	  name:'checkidCard',
           	  labelWidth:70,
           	  width : 55,
                 xtype:'textfield',  
                 fieldLabel:'身份证号'
       	    },
	     {
			    xtype: 'label',
			    text: '身份证反面：'
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
      title:'货主审核',  
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
  
  
});