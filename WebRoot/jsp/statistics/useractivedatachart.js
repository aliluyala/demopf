/* 线型图
 * Ext JS Library 3.3.0
 */
Ext.onReady(function(){
	var queryButton = Ext.create('Ext.Button',{
    	xtype:'button',id:'query', iconCls:'search',text: '查 询',
    	handler: function() {
    		store.load();
		}
    });
	
	var states = Ext.create('Ext.data.Store', {
	    fields: ['userName', 'userName'],
		proxy: {
            type: 'ajax',
            imeout: 120000,//120s
            method:'POST',
            url: '../../statistics/usersInStatisticsWeek.action',
            reader: {
                type: 'json',
                root: 'root'
            }
        },
        listeners: {
        	load: function () {
        	//设置初始值
        	var mv = states.getAt(0);
        	Ext.getCmp("userName").setValue(mv);
        	store.load();
            },
        	beforeload: function (proxy, params) {
	        	if(Ext.getCmp("userName").getValue()!=null&&Ext.getCmp("userName").getValue()!=""){
	        		paras.search = encodeURIComponent(Ext.getCmp("userName").getValue());
	               	this.proxy.extraParams = paras;
	        	}
            }
         },
        autoLoad: true
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
		    Ext.create('Ext.form.ComboBox', {
		        fieldLabel: '用户名',
		        store: states,
		        id:'userName',
		        queryMode: 'local',
		        displayField: 'userName',
		        labelWidth:40,
		        valueField: 'userName',
		        editable: true,
		        listeners : {
					render : function(f) {
						f.el.on('keyup', function(e) {
							if(e.keyCode==13){
								states.load();
							}
						});
				}}
		    }),
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
		//fields: ['id1','name','travels','travel_blogs','travel_praises','blog_praises','travel_reviews','blog_reviews','travel_stores','blog_stores','travel_report','blog_report','point','date'],
		model: 'DataModel',
	    remoteSort: false,
        autoLoad:false,
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
        		paras.search = encodeURIComponent(Ext.getCmp("userName").getValue());
        		
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
	
    Ext.create('Ext.Viewport',{
		layout: 'fit',items:[
								Ext.create('Ext.panel.Panel', {
								    title: '图形报表',
								    layout: 'form',
								    items: [
												formQuery2,
												formQuery,
												{
													layout: 'fit',
												    id: 'card1',
												    title: '图表',
												    xtype: "panel",
												    items:[cc]
												}
								           ]
								})
		                     ],renderTo: 'btsAlarm-grid'
	});
});