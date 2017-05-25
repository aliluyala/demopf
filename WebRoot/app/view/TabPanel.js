Ext.define('dataModel', {
    extend: 'Ext.data.Model',
    fields: ['id','userName','createTime','status','','realName','empNumber','department','position','balance','mobileUser','proxyAdmin'
    ],
    idProperty: 'id'
});
// create the Data Store
var paras={};
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
        url: 'sys/user/userListByPage.action',
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
		store.load();
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
    items: [
	    	queryButton
    ]
});

var record_start = 0;
var grid = Ext.create('Ext.grid.Panel', {
    stripeRows: true,
    autoWidth: true,
    autoExpandColumn: 'name',
    //title: '用户列表',
    columnLines:true,
    store: store,
    loadMask:true,
    disableSelection: false,
    viewConfig: { 
    	loadingText: '正在查询,请等待...'
	} ,
    columns:[
    	{text: '真实名称',
        dataIndex: 'realName',
        flex: 2,
        sortable: true
    	},
    	{text: '登录名称',
            dataIndex: 'userName',
            flex: 2,
            sortable: true
        }
    	],
    forceFit: true,
    // paging bar on the bottom
    bbar: bbar
});

var truckChart;
var truckData=[];
var consignorData=[];
var compData=[];
var mallData=[];
Ext.define('datacenter.view.TabPanel',{
    extend: 'Ext.tab.Panel', 
    initComponent : function(){ 
        Ext.apply(this,{ 
            id: 'content-panel', 
            region: 'center',  
            defaults: { 
               autoScroll:true, 
               bodyPadding: 10 
            }, 
            activeTab: 0, 
            border: false,
	        items: [{ 
              id: 'HomePage', 
              title: '首页', 
              iconCls:'home',
              layout: 'border',
			  items: [
			      {
                    xtype: 'panel',
                    flex: 5,
                    region: 'center',  
                    border: 0,
                    width: 604,
                    layout: {
                        align: 'stretch',
                        type: 'vbox'
                    },
                    header: false,
                    title: 'p1',
                    items: [
                         {
                            xtype: 'panel',
                            flex: 1,
                            margin: '0 0 10 0 ',
							layout: {
		                        align: 'stretch',
		                        type: 'hbox'
		                    },
		                    header: false,
		                    border: 0,
                            items:[
	                            {
	                            	xtype: 'panel',
		                            flex: 1,
		                            margin: '0 0 0 0 ',
		                            contentEl:'panel_el'
	                            },
	                            {
	                            	xtype: 'panel',
		                            flex: 1,
		                            margin: '0 0 0 10 ',
		                            contentEl:'panel_el1'
	                            },
	                            {
	                            	xtype: 'panel',
		                            flex: 1,
		                            margin: '0 0 0 10 ',
		                            contentEl:'panel_el2'
	                            },
	                            {
	                            	xtype: 'panel',
		                            flex: 1,
		                            margin: '0 0 0 10 ',
		                            contentEl:'panel_el3'
	                            }
                            ]
                        },
                        {
                            xtype: 'panel',
                            flex: 2,
                            contentEl:'container3',
                            listeners: {
                            	resize:function(p,width,height,eOpts){
                            		truckChart = Highcharts.chart('container3', {

                            		    title: {
                            		        text: '运得易用户增涨报表'
                            		    },

                            		    /* subtitle: {
                            		        text: 'Source: thesolarfoundation.com'
                            		    },
                            		 */
                            		    yAxis: {
                            		        title: {
                            		            text: '用户数量'
                            		        }
                            		    },
                            		    xAxis: {
                            		        type: 'datetime',
                            		        dateTimeLabelFormats: {
                            		            month: '%Y-%m'
                            		        }
                            		    },
                            		    legend: {
                            		        layout: 'vertical',
                            		        align: 'right',
                            		        verticalAlign: 'middle'
                            		    },

                            		    plotOptions: {
                            		        series: {
                            		        	pointStart: Date.UTC(2015,4,1),
                            		            pointIntervalUnit: 'month'
                            		        }
                            		    },

                            		    series: [{
                            		        name: '司机',
                            		        data:[]
                            		    },{
                            		        name: '货主',
                            		        data:[]
                            		    },{
                            		        name: '物流公司',
                            		        data:[]
                            		    },{
                            		        name: '商家',
                            		        data:[]
                            		    }]

                            		});
                            		if(truckData.length>0){
                            			for(var i=0;i<truckData.length;i++){
            								truckChart.series[0].addPoint(truckData[i]);
            								truckChart.series[1].addPoint(consignorData[i]);
            								truckChart.series[2].addPoint(compData[i]);
            								truckChart.series[3].addPoint(mallData[i]);
            							}
                            		}
                            	},
                            	afterrender: function (proxy, params) {
                            			Ext.Ajax.request({url:'sys/statistics/newtruckUserStatisticsByMonth.action', async: true,  params : {month:'2015-05'},method:'post',type:'json',  success:function(result, request){
                							var responseObj =  Ext.decode(result.responseText);
                							if(responseObj.success!=null&&!responseObj.success){
                								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
                								return;
                							}
                							var data = responseObj.root;
                							for(var i=0;i<data.length;i++){
                								truckData.push(data[i].count);
                								truckChart.series[0].addPoint(data[i].count);
                							}
                                		}
                                		});
                            			
                            			Ext.Ajax.request({url:'sys/statistics/newconsignorUserStatisticsByMonth.action', async: true,  params : {month:'2015-05',usreType:'0'},method:'post',type:'json',  success:function(result, request){
                							var responseObj =  Ext.decode(result.responseText);
                							if(responseObj.success!=null&&!responseObj.success){
                								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
                								return;
                							}
                							var data = responseObj.root;
                							for(var i=0;i<data.length;i++){
                								consignorData.push(data[i].count);
                								truckChart.series[1].addPoint(data[i].count);
                							}
                                		}
                                		});
                            			
                            			Ext.Ajax.request({url:'sys/statistics/newconsignorUserStatisticsByMonth.action', async: true,  params : {month:'2015-05',usreType:'1'},method:'post',type:'json',  success:function(result, request){
                							var responseObj =  Ext.decode(result.responseText);
                							if(responseObj.success!=null&&!responseObj.success){
                								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
                								return;
                							}
                							var data = responseObj.root;
                							for(var i=0;i<data.length;i++){
                								compData.push(data[i].count);
                								truckChart.series[2].addPoint(data[i].count);
                							}
                                		}
                                		});
                            			
                            			Ext.Ajax.request({url:'sys/statistics/newMallUserStatisticsByMonth.action', async: true,  params : {month:'2015-05'},method:'post',type:'json',  success:function(result, request){
                							var responseObj =  Ext.decode(result.responseText);
                							if(responseObj.success!=null&&!responseObj.success){
                								Ext.MessageBox.alert("错误提示",responseObj.appMsg);
                								return;
                							}
                							var data = responseObj.root;
                							for(var i=0;i<data.length;i++){
                								mallData.push(data[i].count);
                								truckChart.series[3].addPoint(data[i].count);
                							}
                                		}
                                		});
                            		}
                            		
                             },
                            //html:'<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="http://localhost:8080/ydypf/thirdpart/highcharts/examples/line-basic/index.htm"></iframe>',
                            layout:{
                                type: 'absolute'
                            }
                        },
                        {
                            xtype: 'panel',
                            flex: 2,
                            margin: '10 0 0 0 ',
                            title: '任务提示',
                        	layout: {
		                        align: 'stretch',
		                        type: 'hbox'
		                    },
		                    header: false,
		                    border: 0,
                            items:[
	                            {
	                            	xtype: 'panel',
		                            flex: 1,
		                            margin: '0 0 0 0 ',
		                            id:'pieId1',
		                            layout:{
		                                type: 'absolute'
		                            },
		                            contentEl:'container1',
		                            listeners: {
		                            	resize:function(p,width,height,eOpts){
		                            		Highcharts.chart('container1', {
		                            		    chart: {
		                            		        plotBackgroundColor: null,
		                            		        plotBorderWidth: null,
		                            		        plotShadow: false,
		                            		        type: 'pie'
		                            		    },
		                            		    title: {
		                            		        text: '用户充值方式占比'
		                            		    },
		                            		    tooltip: {
		                            		        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		                            		    },
		                            		    plotOptions: {
		                            		        pie: {
		                            		            allowPointSelect: true,
		                            		            cursor: 'pointer',
		                            		            size:'90%',
		                            		            dataLabels: {
		                            		                enabled: true,
		                            		                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
		                            		                style: {
		                            		                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		                            		                }
		                            		            }
		                            		        }
		                            		    },
		                            		    series: [{
		                            		        name: 'Brands',
		                            		        colorByPoint: true,
		                            		        data: [{
		                            		            name: 'Internet Explorer',
		                            		            y: 56.33
		                            		        }, {
		                            		            name: 'Chrome',
		                            		            y: 24.03,
		                            		            sliced: true,
		                            		            selected: true
		                            		        }, {
		                            		            name: 'Firefox',
		                            		            y: 10.38
		                            		        }, {
		                            		            name: 'Safari',
		                            		            y: 4.77
		                            		        }, {
		                            		            name: 'Opera',
		                            		            y: 0.91
		                            		        }, {
		                            		            name: 'Proprietary or Undetectable',
		                            		            y: 0.2
		                            		        }]
		                            		    }]
		                            		});
		                            	},
		                            	afterrender: function (proxy, params) {
		                       
		                                }
		                             }
		                            //html:'<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="http://localhost:8080/ydypf/thirdpart/highcharts/examples/pie-basic/index.htm"></iframe>',
	                            },
	                            {
	                            	xtype: 'panel',
		                            flex: 1,
		                            margin: '0 0 0 10 ',
		                            contentEl:'container2',
		                            listeners: {
		                            	resize:function(p,width,height,eOpts){
		                            		Highcharts.chart('container2', {
		                            		    chart: {
		                            		        plotBackgroundColor: null,
		                            		        plotBorderWidth: null,
		                            		        plotShadow: false,
		                            		        type: 'pie'
		                            		    },
		                            		    title: {
		                            		        text: '用户消费方式占比'
		                            		    },
		                            		    tooltip: {
		                            		        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		                            		    },
		                            		    plotOptions: {
		                            		        pie: {
		                            		            allowPointSelect: true,
		                            		            cursor: 'pointer',
		                            		            size:'90%',
		                            		            dataLabels: {
		                            		                enabled: true,
		                            		                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
		                            		                style: {
		                            		                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
		                            		                }
		                            		            }
		                            		        }
		                            		    },
		                            		    series: [{
		                            		        name: 'Brands',
		                            		        colorByPoint: true,
		                            		        data: [{
		                            		            name: 'Internet Explorer',
		                            		            y: 56.33
		                            		        }, {
		                            		            name: 'Chrome',
		                            		            y: 24.03,
		                            		            sliced: true,
		                            		            selected: true
		                            		        }, {
		                            		            name: 'Firefox',
		                            		            y: 10.38
		                            		        }, {
		                            		            name: 'Safari',
		                            		            y: 4.77
		                            		        }, {
		                            		            name: 'Opera',
		                            		            y: 0.91
		                            		        }, {
		                            		            name: 'Proprietary or Undetectable',
		                            		            y: 0.2
		                            		        }]
		                            		    }]
		                            		});
		                            	},
		                            	afterrender: function (proxy, params) {
		                       
		                                }
		                             }
		                            //html:'<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="http://localhost:8080/ydypf/thirdpart/highcharts/examples/pie-basic/index.htm"></iframe>',
	                            }]
                        }
                    ]
                },
                {
                    xtype: 'panel',
                   width:260,
                    region: 'east',  
                    collapsible:true,
                    margin: '0 0 0 10',
                    header: true,
                    border: 1,
                    layout:'fit',
                    title: '代理列表',
                    items:[
                           grid
                           ]
                }
			  ]
            }] 
        }); 
        this.callParent(arguments); 
    } 
}); 
