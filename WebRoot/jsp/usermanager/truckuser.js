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
        fields: ['driverName','mobile','plateNumber','idCardNumber','idCardPic','drivingLicenseNumber','drivingLicensePic','registrationNumber','registrationPic',
                 'registrationPic','lockedDate','loginDate','loginIp','isCheck','truckType','deadWeight','authStep','loadStatus','isLocked','balance','expireTime','points','gsCard','jyCard','czCard','proxy','tellerId','registerTime'
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
            url: '../../sys/member/truckUserByPage.action',
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
    	xtype:'button',id:'query', iconCls:'zoom',text: '查 询',width:104,height:'80%',
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
			        	Ext.getCmp("contiuneTruckId").setValue(records[0].data.id);
			        	var h = Ext.getBody().getHeight();
			        	continuewindow.setHeight(h);
			        	continuewindow.show();
			        }
	            },'-',{
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
			        	Ext.getCmp("balanceTruckId").setValue(records[0].data.id);
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
	            },'-',{
			        iconCls:'refush',
			        text:'充驿道币',
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
			        	var addPoints4Truckform = Ext.getCmp("addPoints4Truckform").getForm();
			        	addPoints4Truckform.reset();
			        	Ext.getCmp("truckIdf").setValue(records[0].data.id);
			        	var h = Ext.getBody().getHeight();
			        	addPoints4Truck.setHeight(h);
			        	addPoints4Truck.show();
			        }
	            },
	            /*'-',{
			        iconCls:'refush',
			        text:'用户充油卡',
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
			        	Ext.getCmp("jyRechargeTruckId").setValue(records[0].data.id);
			        	var h = Ext.getBody().getHeight();
			        	jyRechargewindow.setHeight(h);
			        	jyRechargewindow.show();
			        }
	            },
	            '-',{
			        iconCls:'refush',
			        text:'用户高速费',
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
			        	Ext.getCmp("gsRechargeTruckId").setValue(records[0].data.id);
			        	var h = Ext.getBody().getHeight();
			        	gsRechargewindow.setHeight(h);
			        	gsRechargewindow.show();
			        }
	            },*/
	            '-'
	    ]
	});
	
    //var pluginExpanded = true;
	//fields: ['driverName','mobile','plateNumber','idCardNumber','idCardPic','drivingLicenseNumber','drivingLicensePic','registrationNumber','registrationPic',
      //       'registrationPic','lockedDate','loginDate','loginIp','isCheck','truckType','deadWeight','loadStatus'
	var record_start = 0;
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        flex:6,
        width:'100%',
        id:'grid',
        autoExpandColumn: 'name',
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
        listeners: {
        	
        	             
            itemdblclick: function (me, record, item, index, e, eOpts) {
            	var p = record.data;
                
               // ['driverName','mobile','plateNumber','idCardNumber','','','','registrationNumber','',
                // '','','','','','truckType','deadWeight','','','balance','expireTime','points','gsCard','jyCard','czCard','proxy'

                Ext.getCmp('driverNamed').setValue(p.driverName);
                Ext.getCmp('mobiled').setValue(p.mobile);
                Ext.getCmp('plateNumberd').setValue(p.plateNumber);
                Ext.getCmp('idCardNumberd').setValue(p.idCardNumber);
                Ext.getCmp('registrationNumberd').setValue(p.registrationNumber);
                Ext.getCmp('truckTyped').setValue(p.truckType);
                Ext.getCmp('deadWeightd').setValue(p.deadWeight);
                Ext.getCmp('balanced').setValue(p.balance);
                Ext.getCmp('expireTimed').setValue(p.expireTime);
                Ext.getCmp('pointsd').setValue(p.points);
                Ext.getCmp('gsCardd').setValue(p.gsCard);
                Ext.getCmp('jyCardd').setValue(p.jyCard);
                Ext.getCmp('czCardd').setValue(p.czCard);
                Ext.getCmp('proxyd').setValue(p.proxy);
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
        	{text: '司机名称',
	            dataIndex: 'driverName',
	            flex: 1,
	            sortable: true
        	},
        	{text: '手机',
                dataIndex: 'mobile',
                flex: 1.5,
                sortable: true
            },
        	{text: '车牌号',
                dataIndex: 'plateNumber',
                flex: 1,
                sortable: true
            },
        	{text: '司机身份证',
                dataIndex: 'idCardNumber',
                flex: 2,
                sortable: true
            },{text: '货车类型',
            	dataIndex: 'truckType',
            	flex: 1,
            	sortable: true
            },{text: '注册时间',
            	dataIndex: 'registerTime',
            	flex: 2,
            	sortable: true
            },{text: '服务时间',
            	dataIndex: 'expireTime',
            	flex: 2,
            	sortable: true
            },
            
        	/*{text: '驾驶证号',
            	dataIndex: 'drivingLicenseNumber',
            	flex: 2,
            	sortable: true
            },*/
            {text: '是否审核',
            	dataIndex: 'isCheck',
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
            },{text: '余额',
            	dataIndex: 'balance',
            	flex: 1,
            	sortable: true
            },{text: '积分',
            	dataIndex: 'points',
            	flex: 1,
            	sortable: true
            },{text: '开发者代号',
            	dataIndex: 'tellerId',
            	flex: 1,
            	sortable: true
            },
            /*{text: '是否侍审核状态',
            	dataIndex: 'authStep',
            	flex: 2,
            	sortable: true,
            	renderer: function (value, meta, record) {
            		//alert(record.data.isAuth);
            		if(value==2&&record.data.isCheck==false){
            			return "<span style='color:red;font-weight:bold;'>是</span>";
            		}else{
            			return "否";  
            		}
            	}
            },*/
            {text: '登录时间',
            	dataIndex: 'loginDate',
            	flex: 2,
            	sortable: true
            },
            {
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
    	            	Ext.Ajax.request({url:'../../sys/member/findTruckInfo.action',  params : {truckId:id},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							var form = Ext.getCmp('editform');
							form.getForm().reset();
							Ext.getCmp('edittruckId').setValue(id);
							Ext.getCmp('driverNamee').setValue(responseObj.data['driverName']);
							Ext.getCmp('mobilee').setValue(responseObj.data['mobile']);
							Ext.getCmp('plateNumbere').setValue(responseObj.data['plateNumber']);
							Ext.getCmp('drivingLicenseNumbere').setValue(responseObj.data['drivingLicenseNumber']);
							Ext.getBody().mask();
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
	            		/*if(rec.get("authStep")==0||rec.get("authStep")==1||rec.get("isCheck")){
	            			Ext.MessageBox.alert('提示',"不是审核状态不能审核");
	            			return;
	            		}*/
    	            	Ext.Ajax.request({url:'../../sys/member/findTruckInfo.action',  params : {truckId:id},method:'post',type:'json',  success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success!=null&&!responseObj.success){
								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
								return;
							}
							var form = Ext.getCmp('checkform');
							form.getForm().reset();
							var location = (window.location+'').split('/'); 
			            	var basePath = location[0]+'//'+location[2]+'/'+'hyt';
							//alert(basePath+"/"+responseObj.data['driverPic']);
			            	//Ext.getCmp('brzp').setSrc("../../img/pic.png");
			            	Ext.getCmp('jszp').setSrc("../../img/pic.png");
			            	Ext.getCmp('idCardp').setSrc("../../img/pic.png");
			            	Ext.getCmp('xszp').setSrc("../../img/pic.png");
			            	
			            	/*if(responseObj.data['driverPic']!=null&&responseObj.data['driverPic']!=''){
		            			var driverPicsrc = responseObj.data['driverPic'];
			            		if(responseObj.data['driverPic'].indexOf(".webp")>-1){
			            			//driverPicsrc = responseObj.data['driverPic'].replace(".webp",".jpg");
			            		}
			            		Ext.getCmp('brzp').setSrc(basePath+"/"+driverPicsrc);
			            	}*/
			            	if(responseObj.data['idCardPic']!=null&&responseObj.data['idCardPic']!=''){
			            		var idCardPicsrc = responseObj.data['idCardPic'];
			            		if(responseObj.data['idCardPic'].indexOf(".webp")>-1){
			            			//idCardPicsrc = responseObj.data['idCardPic'].replace(".webp",".jpg");
			            		}
			            		Ext.getCmp('idCardp').setSrc(basePath+"/"+idCardPicsrc);
			            	}
			            	if(responseObj.data['drivingLicensePic']!=null&&responseObj.data['drivingLicensePic']!=''){
			            		var drivingLicensePicsrc = responseObj.data['drivingLicensePic'];
			            		if(responseObj.data['drivingLicensePic'].indexOf(".webp")>-1){
			            			//drivingLicensePicsrc = responseObj.data['drivingLicensePic'].replace(".webp",".jpg");
			            		}
			            		Ext.getCmp('jszp').setSrc(basePath+"/"+drivingLicensePicsrc);
			            	}
			            	if(responseObj.data['registrationPic']!=null&&responseObj.data['registrationPic']!=''){
			            		var registrationPicsrc =responseObj.data['registrationPic'];
			            		if(responseObj.data['registrationPic'].indexOf(".webp")>-1){
			            			//registrationPicsrc = responseObj.data['registrationPic'].replace(".webp",".jpg");
			            		}
			            		Ext.getCmp('xszp').setSrc(basePath+"/"+registrationPicsrc);
			            	}
			            	
			            	//drivingLicensePic:身份证照片
			            	//idCardPic:驾驶证
			            	//registrationPic行驶证
			            	//driverPic : 人车合照片
							Ext.getCmp('truckId').setValue(id);
							Ext.getCmp('idCardNumber').setValue(responseObj.data['idCardNumber']);
							Ext.getCmp('brandType').setValue(responseObj.data['brandType']);
							Ext.getCmp('truckIDCode').setValue(responseObj.data['truckIDCode']);
							Ext.getCmp('motorCode').setValue(responseObj.data['motorCode']);
							Ext.getCmp('registerDate').setValue(responseObj.data['registerDate']);
							Ext.getCmp('dispCardDate').setValue(responseObj.data['dispCardDate']);
							Ext.getCmp('registrationNumber').setValue(responseObj.data['registrationNumber']);
							
							Ext.getCmp('gsCard').setValue(responseObj.data['gsCard']);
							Ext.getCmp('jyCard').setValue(responseObj.data['jyCard']);
							Ext.getCmp('czCard').setValue(responseObj.data['czCard']);
							
							Ext.getCmp('checked').setValue(responseObj.data['isCheck']+"");
							if(responseObj.data['isCheck']){
								Ext.getCmp('tips').setVisible(false);
							}
							var h = Ext.getBody().getHeight();
							windowcheck.setHeight(h);
		                	windowcheck.show();
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
	                	paras2.truckId = id;
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
    	            	Ext.Ajax.request({url:'../../sys/member/trucklocked.action',  params : {truckId:id},method:'post',type:'json',  success:function(result, request){
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
    
  var queryPanel = Ext.create('Ext.Panel', {
	  flex:1.1,
  	  width:'100%',
  	layout: {
  	    type: 'vbox',
  	    pack :'center',
  	    padding:'2px'
  	},
  	title: '车主列表',
  	 border:true,
  	  items:[
  	         {
  	        	 layout:'hbox',
  	        	 flex:1,
  	        	 border:false,
  	        	 items:[
						{width:180,forceSelection: true,xtype: 'combobox',fieldLabel:'审核状态:',labelWidth: 70,anchor: '100%',
							store: checkStatusStore,emptyText :'请选择...',queryMode: 'local',displayField: 'name',valueField: 'code',
							id:'bsc',name:'bsc'},
						    {width:180,labelWidth:70,name:'titleSearch',id:'titleSearch',xtype:'textfield',fieldLabel:'用户名称'},
							{width:180,labelWidth:70,name:'phoneSearch',id:'phoneSearch',xtype:'textfield',fieldLabel:'手机号码'}
  	        	        ]
  	         },{
  	        	 layout:'hbox',
  	        	 flex:1,
  	        	 border:false,
  	        	 items:[
						{width:180,labelWidth:70,name:'code',id:'code',xtype:'textfield',fieldLabel:'注册代码'},
						{
							width:180,
						    xtype: 'datefield',
						    id:'datetime',
						    labelWidth: 70,
						    fieldLabel: '注册时间',
						    name: 'datetime',
						    format: 'Y-m-d'
						},{width:75,border:false},
						queryButton
  	        	        ]
  	         	}
  	         ]
  });
  Ext.create('Ext.Viewport',{
		layout: 'vbox',items:[queryPanel,grid],renderTo: 'btsAlarm-grid'
	});
  
  
  var continuewindow = new Ext.Window({  
      title:'续服务时间',  
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
          url: '../../sys/member/addExpireTime4Truck.action',
          labelWidth:30,  
          items:[
			{  
			    id:'contiuneTruckId',
			    name:'driverId',
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
      title:'充值',  
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
          url: '../../sys/member/addBalance4Truck.action',
          labelWidth:30,  
          items:[
			{  
			    id:'balanceTruckId',
			    name:'driverId',
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
  
  var addPoints4Truck = new Ext.Window({  
      title:'充值',  
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
          id:'addPoints4Truckform',
          baseCls:'x-plain',  
          xtype:'form',
          style:'padding:8px;',  
          url: '../../sys/member/addPoints4Truck.action',
          labelWidth:30,  
          items:[
			{  
			    id:'truckIdf',
			    name:'truckId',
			    labelWidth:40,  
			    xtype:'hiddenfield',  
			},
			 {  
	           	  id:'pointf',
	           	  name:'points',
	           	  labelWidth:70,
	           	  width : 55,
	           	  allowBlank:false,
	           	  allowDecimals: false, // 允许小数点
	           	  allowNegative: false, // 允许负数
                  xtype:'numberfield',  
                  fieldLabel:'充值额度'
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
		                            addPoints4Truck.close();
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
	        		  addPoints4Truck.close();
	    		}
	      }]
      }
      ]
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
          url: '../../sys/member/minusBalanceTally.action',
          labelWidth:30,  
          items:[
			{  
			    id:'minusBalanceTallyId',
			    name:'driverId',
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
  
  
  var jyRechargewindow = new Ext.Window({  
      title:'充油卡',  
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
          id:'jyRechargeform',
          baseCls:'x-plain',  
          xtype:'form',
          style:'padding:8px;',  
          url: '../../sys/member/addjyRecharge4Truck.action',
          labelWidth:30,  
          items:[
			{  
			    id:'jyRechargeTruckId',
			    name:'driverId',
			    labelWidth:40,  
			    xtype:'hiddenfield',  
			},
			 {  
	           	  id:'recharge',
	           	  name:'recharge',
	           	  labelWidth:70,
	           	  width : 55,
	           	  allowBlank:false,
                  xtype:'numberfield',  
                  fieldLabel:'充值额度'
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
		                            jyRechargewindow.close();
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
	        		  jyRechargewindow.close();
	    		}
	      }]
      }
      ]
  }); 
  
  
  var gsRechargewindow = new Ext.Window({  
      title:'充高速费',  
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
          id:'gsRechargeform',
          baseCls:'x-plain',  
          xtype:'form',
          style:'padding:8px;',  
          url: '../../sys/member/addgsRecharge4Truck.action',
          labelWidth:30,  
          items:[
			{  
			    id:'gsRechargeTruckId',
			    name:'driverId',
			    labelWidth:40,  
			    xtype:'hiddenfield',  
			},
			 {  
	           	  id:'gsrecharge',
	           	  name:'recharge',
	           	  labelWidth:70,
	           	  width : 55,
	           	  allowBlank:false,
                  xtype:'numberfield',  
                  fieldLabel:'充值额度'
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
		                            gsRechargewindow.close();
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
	        		  gsRechargewindow.close();
	    		}
	      }]
      }
      ]
  }); 
  
  var windowcheck = new Ext.Window({  
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
          url: '../../sys/member/checkedTruckUser.action',
          labelWidth:30,  
          items:[
			{  
			    id:'truckId',
			    name:'truckId',
			    labelWidth:40,  
			    xtype:'hiddenfield',  
			},
			/*{
				xtype: 'label',
				text: '人车合照：'
			},
           Ext.create('Ext.Img', {
           	  id:'brzp',
           	  height:350,
           	  width : 455,
           	  name:'brzp',
             src: '../../img/pic.png'
           }),*/
           {
        		xtype: 'label',
        		text: '驾驶照片：'
        	},
           Ext.create('Ext.Img', {
         	  id:'jszp',
         	  height:350,
         	  width : 455,
         	  name:'jszp',
   	      src: '../../img/pic.png'
           }),
           {  
         	  id:'drivingLicenseNumber',
         	  name:'drivingLicenseNumber',
         	  labelWidth:80,
         	  width : 455,
               xtype:'textfield',  
               fieldLabel:'驾驶证号'
          },
           {
       		xtype: 'label',
       		labelWidth:80,
       		text: '本人持身份证照片：'
       	},
         Ext.create('Ext.Img', {
        	  id:'idCardp',
        	  height:350,
        	  width : 455,
        	  name:'idCardp',
    	      src: '../../img/pic.png'
    	}),
    	{  
        	  id:'idCardNumber',
        	  name:'idCardNumber',
        	  labelWidth:40,
        	  width : 55,
              xtype:'textfield',  
              fieldLabel:'身份证'
         },
         {
     		xtype: 'label',
     		text: '行驶照片：'
     	},
         Ext.create('Ext.Img', {
        	  id:'xszp',
        	  height:350,
        	  width : 455,
        	  name:'xszp',
   	      src: '../../img/pic.png'
       	}),
       	{  
      	  id:'registrationNumber',
      	  name:'registrationNumber',
      	  labelWidth:70,
      	  width : 55,
            xtype:'textfield',  
            fieldLabel:'行驶证号'
  	    },
	    {  
    	  id:'brandType',
    	  name:'brandType',
    	  labelWidth:70,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'品牌型号'
	    },
	    {  
    	  id:'truckIDCode',
    	  name:'truckIDCode',
    	  labelWidth:70,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'识别代码'
	    },
	    {  
    	  id:'motorCode',
    	  name:'motorCode',
    	  labelWidth:80,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'发动机号码'
	    },
	    {  
    	  id:'registerDate',
    	  name:'registerDate',
    	  labelWidth:70,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'注册日期'
	    },
	    {  
    	  id:'dispCardDate',
    	  name:'dispCardDate',
    	  labelWidth:70,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'发证日期'
	    },
	    {  
    	  id:'gsCard',
    	  name:'gsCard',
    	  labelWidth:70,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'高速卡'
	    },
	    {  
	    	  id:'jyCard',
	    	  name:'jyCard',
	    	  labelWidth:70,
	    	  width : 55,
	          xtype:'textfield',  
	          fieldLabel:'加油卡'
		   },
	       {  
	    	  id:'czCard',
	    	  name:'czCard',
	    	  labelWidth:70,
	    	  width : 55,
	          xtype:'textfield',  
	          fieldLabel:'充值卡'
		    },
         {
        	width:210,
        	forceSelection: true,
        	xtype: 'combobox',
        	fieldLabel:'审核状态:',
        	labelWidth: 70,
        	anchor: '100%',
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
			]}
          ],
	      buttonAlign:'center',
	      buttons:[{  
	          	  text:'确定',
	          	  //formBind: true,
	        	  handler: function() {
	        		  var btn = this;
	        		  btn.setDisabled(true);
	        		  var form = this.up('form').getForm();
	        		  if(Ext.getCmp('checked').getValue()=="true"){
	        			  if (form.isValid()) {
		                      form.submit({
		                          success: function(form, action) {
		                        	  btn.setDisabled(false);
		                             store.reload();
		       	        		  	 windowcheck.close();
		                          },
		                          failure: function(form, action) {
		                        	  btn.setDisabled(false);
		                        	  Ext.Msg.alert('操作失败', action.result.appMsg);
		                          }
		                      });
		                  }
	        		  }else{
	                      form.submit({
	                          success: function(form, action) {
	                        	 btn.setDisabled(false);
	                             store.reload();
	       	        		  	 windowcheck.close();
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
					windowcheck.close();
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
	  	        fieldLabel: '司机名称',
	  	        id:'driverNamed',
	  	        name: 'driverName',
	  	        value: '10'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '手机',
	  	        id:'mobiled',
	  	        name: 'mobile',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '车牌号',
	  	        name: 'plateNumberd',
	  	        id:'plateNumberd',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '身份证',
	  	        name: 'idCardNumber',
	  	        id:'idCardNumberd',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '行驶证',
	  	        name: 'registrationNumber',
	  	        id:'registrationNumberd',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '车类型',
	  	        name: 'truckType',
	  	        id:'truckTyped',
	  	        value: '11'
	  	    }, {
	  	        xtype: 'displayfield',
	  	        fieldLabel: '处理吨位',
	  	        name: 'deadWeight',
	  	        id:'deadWeightd',
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
	  		  fieldLabel: '服务时间',
	  		  name: 'expireTime',
	  		  id:'expireTimed',
	  		  value: '11'
	  	  }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '驿道币',
  	        name: 'points',
  	        id:'pointsd',
  	        value: '11'
  	    }
	  	, {
	  		xtype: 'displayfield',
	  		fieldLabel: '代理区号',
	  		name: 'proxy',
	  		id:'proxyd',
	  		value: '11'
	  	}
	  	
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '高速卡号',
  	        name: 'gsCard',
  	        id:'gsCardd',
  	        value: '11'
  	    }
	  	, {
  	        xtype: 'displayfield',
  	        fieldLabel: '加油卡号',
  	        name: 'jyCard',
  	        id:'jyCardd',
  	        value: '11'
  	    }
	  	, {
	  		xtype: 'displayfield',
	  		fieldLabel: '充值卡号',
	  		name: 'czCard',
	  		id:'czCardd',
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
          url: '../../sys/member/editTruckUser.action',
          labelWidth:30,  
          items:[
		{  
		    id:'edittruckId',
		    name:'truckId',
		    labelWidth:40,  
		    xtype:'hiddenfield',  
		},
	    {  
    	  id:'driverNamee',
    	  name:'driverName',
    	  allowBlank:false,
    	  labelWidth:70,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'名称'
	    },
	    {  
    	  id:'mobilee',
    	  name:'mobile',
    	  allowBlank:false,
    	  labelWidth:70,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'手机号'
	    },
	    {  
    	  id:'plateNumbere',
    	  name:'plateNumber',
    	  allowBlank:false,
    	  labelWidth:70,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'车牌号'
	    },
	    {  
    	  id:'drivingLicenseNumbere',
    	  name:'drivingLicenseNumber',
    	  allowBlank:true,
    	  labelWidth:70,
    	  width : 55,
          xtype:'textfield',  
          fieldLabel:'驾驶证'
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
      fields: ['accountId','pay','deposit','balance','tradeType','remark','createdDate','points','restPoints','payType'
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
          url: '../../sys/member/struckStatements.action',
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
  var window2 = new Ext.Window({  
      title:'支付详细',  
      width:900,  
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
	            flex: 1,
	            sortable: true
        	},
        	{text: '金额',
	            dataIndex: 'pay',
	            flex: 1,
	            sortable: true
        	},{text: '积分',
	            dataIndex: 'points',
	            flex: 1,
	            sortable: true
        	},{text: '余额',
	            dataIndex: 'balance',
	            flex: 1,
	            sortable: true
        	},{text: '剩余积分',
	            dataIndex: 'restPoints',
	            flex: 1,
	            sortable: true
        	},
        	{text: '支付时间',
	            dataIndex: 'createdDate',
	            flex: 3,
	            sortable: true
        	},{text: '支付说明',
	            dataIndex: 'remark',
	            flex: 3,
	            sortable: true
        	}],
		    forceFit: true,
		})
      ]
       
  });  
});