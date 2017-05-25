/* 线型图
 * Ext JS Library 3.3.0
 */
Ext.onReady(function(){
	var queryButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'query', iconCls:'search',text: '查 询',
    	handler: function() {
    		var datefrom = Ext.getCmp("datefrom");
	    	var dtf = new Date(datefrom.getValue());
	    	var dateto = Ext.getCmp("dateto");
	    	var dtt = new Date(dateto.getValue());
	    	var formatStr = 'Y-W';
	    	
	    	alert(new Date(dtf-4*24*3600*1000));
	    	
	    	alert(Ext.Date.format(new Date(dtf+20*7*24*3600*1000), formatStr));
	    	
	    	var dateString = "";
	    	for(var ds=dtf;ds<=dtt;ds = ds + 7*24*3600*1000){
	    		var da = new Date(ds);
	    		var formatStr = 'Y-W';
	    		dateString += Ext.Date.format(da, formatStr);
	    	}
	    	alert(dateString);
	    	return;
	    	
    		paraslist.search = encodeURIComponent(Ext.getCmp("userName").getValue());
    		
    		liststore.load();
		}
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
		        value: new Date(new Date()-20*7*24*3600*1000)
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
		    {
		    	xtype:'textfield',
		        fieldLabel: '用户名',
		        id:'userName',
		        queryMode: 'local',
		        labelWidth:50
		    },
	    	queryButton
	    	]
	});
	
	Ext.define('DataModel', {
        extend: 'Ext.data.Model',
        fields: ['id1','name','travels','travel_blogs','travel_praises','blog_praises','travel_reviews','blog_reviews','travel_stores','blog_stores','travel_report','blog_report','point','date'
        ],
        idProperty: 'id1'
    });
	
	var paras={};
	var pageSize = 40;
	var store = Ext.create('Ext.data.Store', {
		pageSize: pageSize,
		model: 'DataModel',
	    remoteSort: false,
        autoLoad:false,
        proxy:  {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../statistics/userActiveOfStatistics.action',
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
            	
            	var datefrom = Ext.getCmp("datefrom");
    	    	var dtf = new Date(datefrom.getValue());
    	    	var dateto = Ext.getCmp("dateto");
    	    	var dtt = new Date(dateto.getValue());
    	    	
        		var formatStr = 'Y-W';
        		var datefStr = Ext.Date.format(dtf, formatStr);
        		var datetStr = Ext.Date.format(dtt, formatStr);
        		
        		
        		paras.datef = datefStr;
        		paras.datet = datetStr;
        		
               	this.proxy.extraParams = paras;
            }
         }
	});
	var cc=Ext.create('Ext.chart.Chart', {
		flex:1,
		width : 1000,
	    height: 300,
	    frame:true,
	    animate: true,
	    store: store,
	    axes: [
	        {
	            type: 'Numeric',
	            position: 'left',
	            fields: ['travels', 'point'],
	            label: {
	                renderer: Ext.util.Format.numberRenderer('0,0')
	            },
	            title: '数 据',
	            grid: true,
	            minimum: 0
	        },
	        {
	            type: 'Category',
	            position: 'bottom',
	            fields: ['date'],
	            title: '时 间'
	        }
	    ],
	    series: [
	        {
	            type: 'line',
	            highlight: {
	                size: 7,
	                radius: 7
	            },
	            tips: {
	            	  trackMouse: true,
	            	  width: 140,
	            	  height: 28,
	            	  renderer: function(storeItem, item) {
	            	    this.setTitle(storeItem.get('date') + ': ' + storeItem.get('travels') + ' views');
	            	  }
	            	},
	            axis: 'left',
	            xField: 'date',
	            yField: 'travels',
	            markerConfig: {
	                type: 'cross',
	                size: 4,
	                radius: 4,
	                'stroke-width': 0
	            }
	        },
	        {
	            type: 'line',
	            highlight: {
	                size: 7,
	                radius: 7
	            },
	            tips: {
	            	  trackMouse: true,
	            	  width: 140,
	            	  height: 28,
	            	  renderer: function(storeItem, item) {
	            	    this.setTitle(storeItem.get('date') + ': ' + storeItem.get('point') + ' views');
	            	  }
	            	},
	            axis: 'left',
	            fill: true,
	            xField: 'date',
	            yField: 'point',
	            markerConfig: {
	                type: 'circle',
	                size: 4,
	                radius: 4,
	                'stroke-width': 0
	            }
	        }
	    ]
	});
	
	
	Ext.define('listModel', {
        extend: 'Ext.data.Model',
        fields: ['id1','name','travels','travel_blogs','travel_praises','blog_praises','travel_reviews','blog_reviews','travel_stores','blog_stores','travel_report','blog_report','point','date'
        ],
        idProperty: 'id1'
    });
    
	var paraslist={};
	var pageSize = 40;
    var liststore = Ext.create('Ext.data.Store', {
        pageSize: pageSize,
        model: 'listModel',
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
        		paraslist.currentPage=liststore.currentPage;
            	paraslist.pageSize = liststore.pageSize;
            	Ext.getCmp('query').setDisabled(true);
               	this.proxy.extraParams = paraslist;
            }
         }
    });
	
    var bbar = Ext.create('Ext.PagingToolbar', {
            store: liststore,
            displayInfo: true,
            columnWidth: 6,
            beforePageText:'页',
            inputItemWidth:50,
            afterPageText:'共{0}页',
            displayMsg: '显示的数据 {0} - {1} 共 {2}',
            emptyMsg: '没有要显示的数据'
    });
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        flex:8,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '用户活跃指数',
        columnLines:true,
        store: liststore,
        loadMask:true,
        disableSelection: false,
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
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
        bbar: bbar,
        listeners:{
		  itemdblclick:function(dataview, record, item, index, e){
		       paras.search = encodeURIComponent(record.get("name"));
		       store.load();
		}}
    });
    
    Ext.create('Ext.Viewport',{
      region:'center',     
      //新的布局方式     
      layout:{     
        type:'vbox',     
        align : 'stretch',     
        pack  : 'start'    
      },     
      defaults:{     
        //子元素平均分配宽度     
        flex:1     
      },     
      split:true,     
      frame:true,     
      //前面定义的一个Grid数组     
      items:[
			formQuery,
			{
				layout: 'fit',
				flex:7,
			    id: 'card1',
			    title: '图表',
			    xtype: "panel",
			    items:[cc]
			},
			grid
		  ],
		  renderTo: 'btsAlarm-grid'   
	});
});