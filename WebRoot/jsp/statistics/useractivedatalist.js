Ext.Loader.setConfig({enabled: true});

Ext.require([
    'Ext.grid.*',
    'Ext.data.*',
    'Ext.util.*',
    'Ext.toolbar.Paging',
    'Ext.ModelManager'
]);

Ext.onReady(function(){
	var states = Ext.create('Ext.data.Store', {
	    fields: ['abbr', 'name'],
	    data : [
	        {"abbr":"AL", "name":"Alabama"},
	        {"abbr":"AK", "name":"Alaska"},
	        {"abbr":"AZ", "name":"Arizona"}
	        //...
	    ]
	});
	
    Ext.define('DataModel', {
        extend: 'Ext.data.Model',
        fields: ['id1','name','travels','travel_blogs','travel_praises','blog_praises','travel_reviews','blog_reviews','travel_stores','blog_stores','travel_report','blog_report','point','date'
        ],
        idProperty: 'id1'
    });
    // create the Data Store
	var paras={};
	var pageSize = 40;
    var store = Ext.create('Ext.data.Store', {
        pageSize: pageSize,
        model: 'DataModel',
        remoteSort: false,
        autoLoad:true,
        proxy: {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../statistics/userActiveOfStatisticsWeekByPage.action',
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
    	xtype:'button',id:'query', iconCls:'search',text: '查 询',
    	handler: function() {
	    	var datefrom = Ext.getCmp("datefrom");
	    	var dtf = new Date(datefrom.getValue());
	    	var dateto = Ext.getCmp("dateto");
	    	var dtt = new Date(dateto.getValue());
	    	
    		var fileItype = Ext.getCmp("FileItype");
    		var value = fileItype.getValue();
    		var formatStr = 'Y-W';
    		if(value.FileItype=='month'){
    			formatStr = 'Y-m';
    		}
    		if(value.FileItype=='year'){
    			formatStr = 'Y';
    		}
    		var datefStr = Ext.Date.format(dtf, formatStr);
    		var datetStr = Ext.Date.format(dtt, formatStr);
    		
    		paras.datef = datefStr;
    		paras.datet = datetStr;
    		paras.search = encodeURIComponent(Ext.getCmp("titleSearch").getValue());
    		store.load();
		}
    });
    
    var sendButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'send', iconCls:'up',text: '发送信息',
    	handler: function() {
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
	var formQuery = Ext.create('Ext.form.FormPanel',{
		 flex:1,
		 layout:'column',
		 frame:true,
		 region:'left',
		 bodyPadding: 5,
		 items:[{
		        xtype: 'datefield',
		        id:'datefrom',
		        anchor: '100%',
		        fieldLabel: '从',
		        labelWidth:20,
		        name: 'date',
		        format: 'Y年-W周',
		        value: new Date()
		    }, {
		        xtype: 'datefield',
		        id:'dateto',
		        anchor: '100%',
		        fieldLabel: '到',
		        labelWidth:20,
		        name: 'date',
		        format: 'Y年-W周',
		        altFormats: 'm,d,Y|m.d.Y',
		        value: new Date()
		    },
	    	{width:180,labelWidth:55,name:'titleSearch',id:'titleSearch',xtype:'textfield',fieldLabel:'用户名'},
	    	queryButton
	    	]
	});
	var formQuery2 = Ext.create('Ext.form.FormPanel',{
		 flex:1,
		 layout:'column',
		 frame:true,
		 region:'left',
		 bodyPadding: 5,
		 items:[
					{
						xtype:'radiogroup',
						name:'FileItype', 
						id:'FileItype',
						items:[
						{boxLabel:'周报表',name:'FileItype',inputValue:'week',checked: true},
						{boxLabel:'月报表',name:'FileItype',inputValue:'month'},
						{boxLabel:'年报表',name:'FileItype',inputValue:'year'}
						],
						listeners : {
						     'change' : function(checkbox, newValue,oldValue,eOpts){ 
											if(newValue.FileItype=='week'){
												var datefrom = Ext.getCmp("datefrom");
												datefrom.format='Y年-W周';
												var dateto = Ext.getCmp("dateto");
												dateto.format='Y年-W周';
												store.getProxy().url = '../../statistics/userActiveOfStatisticsWeekByPage.action';
											}
											if(newValue.FileItype=='month'){
												var combo = Ext.getCmp("checkState");
												var datefrom = Ext.getCmp("datefrom");
												datefrom.format='Y年-m月';
												var dateto = Ext.getCmp("dateto");
												dateto.format='Y年-m月';
												store.getProxy().url = '../../statistics/userActiveOfStatisticsMonthByPage.action';
											}
											if(newValue.FileItype=='year'){
												var datefrom = Ext.getCmp("datefrom");
												datefrom.format='Y年';
												var dateto = Ext.getCmp("dateto");
												dateto.format='Y年';
												store.getProxy().url = '../../statistics/userActiveOfStatisticsYearByPage.action';
											}
									        store.load();
						                }
						}
					}
	    	]
	});
	
	
	var form = Ext.create('Ext.form.FormPanel',{
		 flex:1,
		 layout:'form',
		 frame:true,
		 region:'center',
		 bodyPadding: 2,
		 items:[formQuery2,formQuery]
	});
	 
    //var pluginExpanded = true;
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '用户活跃指数',
        columnLines:true,
        store: store,
        //paramNames: { createtime:  },
        loadMask:true,
        disableSelection: false,
        tbar:[form],
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
        // ['id','userName','travels','travelBlogs','travelPraises','blogPraises','travelReviews','blogReviews','travelStores','blogStores','travelReport','blogReport','point','date'
        columns:[
        	{text: '用户名',
            dataIndex: 'name',
            flex: 2,
            sortable: true
        	},
        	{text: '骑迹数',
                dataIndex: 'travels',
                flex: 2,
                sortable: true
            },
        	{text: '直播数',
                dataIndex: 'travel_blogs',
                flex: 2,
                sortable: true
            },
        	{text: '骑迹点赞数',
                dataIndex: 'travel_praises',
                flex: 2,
                sortable: true
            }
        	,
        	{text: '直播点赞数',
                dataIndex: 'blog_praises',
                flex: 2,
                sortable: true
            },
        	{text: '骑迹评论赞数',
                dataIndex: 'travel_reviews',
                flex: 2,
                sortable: true
            }
        	,
        	{text: '直播评论赞数',
                dataIndex: 'blog_reviews',
                flex: 2,
                sortable: true
            }
        	,
        	{text: '骑迹收藏数',
                dataIndex: 'travel_stores',
                flex: 2,
                sortable: true
            }
        	,
        	{text: '直播收藏数 ',
                dataIndex: 'blog_stores',
                flex: 2,
                sortable: true
            }
        	,
        	{text: '骑迹举报数 ',
                dataIndex: 'travel_report',
                flex: 2,
                sortable: true
            }
        	,
        	{text: '直播举报数',
                dataIndex: 'blog_report',
                flex: 2,
                sortable: true
            }
        	,
        	{text: '活跃指数 ',
                dataIndex: 'point',
                flex: 2,
                sortable: true
            }
        	,
        	{text: '统计时间段 ',
                dataIndex: 'date',
                flex: 2,
                sortable: true
            }
        	],
        forceFit: true,
        // paging bar on the bottom
        bbar: bbar
    });
  Ext.create('Ext.Viewport',{
		layout: 'fit',items:[grid],renderTo: 'btsAlarm-grid'
	});
});