/* 线型图
 * Ext JS Library 3.3.0
 */
Ext.onReady(function(){
	var tbar = Ext.create("Ext.Toolbar", {
		flex:1,
	    items: [
		          {
			        xtype: 'radiogroup',
			        columns: 5,
			        width:350,
			        id:'days',
			        items: [
			            { boxLabel: '近15天', name: 'rb', inputValue: '15' },
			            { boxLabel: '近30天', name: 'rb', inputValue: '30', checked: true},
			            { boxLabel: '近60天', name: 'rb', inputValue: '60' },
			            { boxLabel: '近365天', name: 'rb', inputValue: '365' }
			        ],
			        listeners: {
						change: function (t,n,o,e) {
							store.load();
			            }
			        }
			    },'->','-',{
			        xtype: 'label',
			        forId: 'myFieldId',
			        id:'fileId',
			        text: '该时间段内新增用户：100个',
			        margin: '0 0 0 10'
			    },'-'
	    ]
	});
	
	Ext.define('DataModel', {
        extend: 'Ext.data.Model',
        fields: ['date','count'],
        idProperty: 'date'
    });
	
	var paras={};
	var pageSize = 40;
	var store = Ext.create('Ext.data.Store', {
		pageSize: pageSize,
		model: 'DataModel',
	    remoteSort: false,
        autoLoad:true,
        proxy:  {
    		timeout: 120000,//120s
            method:'POST',
        	type: 'ajax',
            url: '../../sys/statistics/newtruckUserStatistics.action',
            reader: {
                root: 'root',
                totalProperty: 'totalCount'
            }
        },
        listeners: {
            load: function () {
        		Ext.getCmp("fileId").setText("该时间段内新增用户："+this.getTotalCount()+"个");
               if(this.getCount() == 0)
               {
            	   Ext.Msg.alert("提示","<font size='2' color='red'>暂无数据!</font>");
            	}
            },
        	beforeload: function (proxy, params) {
            	paras.days = Ext.getCmp("days").getValue();
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
	            fields: ['count'],
	            label: {
	                renderer: Ext.util.Format.numberRenderer('0,0')
	            },
	            title: '新增用户数',
	            grid: true,
	            minimum: 0
	        },
	        {
	            type: 'Category',
	            position: 'bottom',
	            fields: ['date'],
	            title: '时间日期',
	            grid: true,
	            minorTickSteps : 5,
	            label: {
	                rotate: {
	                    degrees: 315
	                }
	            }
	        }
	    ],
	    series: [
	        {
	            type: 'line',
	            smooth:true,
	            highlight: {
	                size: 2,
	                radius: 2
	            },
	            tips: {
	            	  trackMouse: true,
	            	  width: 140,
	            	  height: 28,
	            	  renderer: function(storeItem, item) {
	            	    this.setTitle(storeItem.get('date') + ': ' + storeItem.get('count') + '个');
	            	  }
	            	},
	            axis: 'left',
	            xField: 'date',
	            yField: 'count',
	            markerConfig: {
	                type: 'circle',
	                size: 1,
	                radius: 3,
	                'stroke-width': 0
	            }
	        }
	    ]
	});
	
    var grid = Ext.create('Ext.grid.Panel', {
        stripeRows: true,
        flex:5,
        autoWidth: true,
        autoExpandColumn: 'name',
        title: '用户增量统计',
        columnLines:true,
        store: store,
        loadMask:true,
        disableSelection: false,
        viewConfig: { 
	    	loadingText: '正在查询,请等待...'
    	} ,
        columns:[
        	{text: '日期',
            dataIndex: 'date',
            flex: 1,
            sortable: true
        	},
        	{text: '用户数',
                dataIndex: 'count',
                flex: 10,
                sortable: true
            }
        	],
        forceFit: true,
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
			tbar,
			{
				layout: 'fit',
				flex:10,
			    id: 'card1',
			    title: '用户增量统计曲线图',
			    xtype: "panel",
			    items:[cc]
			},
			grid
		  ],
		  renderTo: 'btsAlarm-grid'   
	});
});