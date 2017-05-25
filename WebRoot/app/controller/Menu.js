Ext.define('datacenter.controller.Menu',{ 
    extend: 'Ext.app.Controller', 
   	stores: ['Menus'],  
    models: ['Menu'],  
    views: ['Menu'],  
    init: function () {  
		
    //初始化部分，下面是部分是给菜单绑定单击事件，接下来会用，这里先注释  
        this.control({  
            'treepanel': {  
              itemmousedown: this.loadMenu
          	},
	        'label[id=head-lb-1]': {
          		render: this.onPanelRendered
	        },
          	'sxptmenu': {
          		render: this.getmenu
	        },
	        'button[id=changepassword]': {
	        	click: this.changepassword
	        }
          });
    },
    
    onPanelRendered: function() {
    	Ext.Ajax.request({url:'sys/user/getUser.action',  params : {},method:'post',type:'json',  success:function(result, request){
			var responseObj =  Ext.decode(result.responseText);
			Ext.getCmp('head-lb-1').setText('欢迎登陆,'+responseObj.data['realName']);
			Ext.getCmp('head-lb-3').setText('当前日期：'+Ext.Date.format(new Date(), 'Y-m-d'));
		}});
    },
    
    changepassword: function() {
    	editwindow.show();
    },
   
    
    /** 
     * 加载菜单树 
     */  
    getmenu: function (json) {  
    		var test = Ext.getCmp('menu-panel');
    		var cs  = [];
    		Ext.Ajax.request({  
                url : 'sys/user/userPermission.action',  
                success : function(response) {  
                    var json = Ext.JSON.decode(response.responseText);
                    Ext.each(json, function(el) {  
                                var panel = Ext.create(  
                                        'Ext.panel.Panel', {  
                                            title : el.text,  
                                            layout : 'fit'  
                                        });  
                                panel.add(buildTree(el)); 
                                cs.push(panel);
                                //test.add(panel);
                       });
                    test.add(cs);
                },  
                failure : function(request) {  
                    Ext.MessageBox.show({  
                                title : '操作提示',  
                                msg : "连接服务器失败",  
                                buttons : Ext.MessageBox.OK,  
                                icon : Ext.MessageBox.ERROR  
                            });  
                },  
                method : 'post'  
            });
    },
    
    loadMenu:function(selModel, record){ 
    	if (record.get('leaf')) {
        	if(record.get('optype')=='window'){
        		var win= Ext.getCmp(record.get('url'));
        		if(!win){
        			win=Ext.widget(record.get('url'));
        		}
        		win.show();
        	}
        	else{
        		var panel = Ext.getCmp(record.get('id'));
	            if(!panel){  
	            	var location = (window.location+'').split('/'); 
	            	var basePath = location[0]+'//'+location[2]+'/'+location[3];
	                panel ={
	                	id:record.get('url'),
	                    title: record.get('text'), 
                        html : '<iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+basePath+record.get("url")+'"></iframe>',
	                    closable: true  
	                };
	                this.openTab(panel,record.get('url')); 
	            }else{ 
	                var main = Ext.getCmp("content-panel"); 
	                main.setActiveTab(panel);  
	            }
        	}
        }  
      },
     openTab : function (panel,id){
        var o = (typeof panel == "string" ? panel : id || panel.id); 
        var main = Ext.getCmp("content-panel"); 
        var tab = main.getComponent(o);       
        if (tab) { 
            main.setActiveTab(tab);  
        } else if(typeof panel!="string"){ 
            panel.id = o;  
            var p = main.add(panel);  
            main.setActiveTab(p);  
        }  

    }  

});


var editwindow = new Ext.Window({  
    title:'密码修改',  
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
        url: 'sys/user/changePassword.action',
        labelWidth:30,  
        items:[
	    {  
	  	  id:'srcPassword',
	  	  name:'srcPassword',
	  	  allowBlank:false,
	  	  labelWidth:70,
	  	  width : 55,
          xtype:'textfield',  
          fieldLabel:'旧密码'
	    },
	    {  
		  	  id:'password',
		  	  name:'password',
		  	  allowBlank:false,
		  	  labelWidth:70,
		  	  width : 55,
	          xtype:'textfield',  
	          fieldLabel:'新密码'
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

/** 
 * 组建树 
 */  
function buildTree(json) {  
    return Ext.create('Ext.tree.Panel', {  
                rootVisible : false,  
                border : false,
                store : Ext.create('Ext.data.TreeStore', {  
                	model: 'datacenter.model.Menu',
                    root : {  
                        expanded : true,  
                        children : json.children  
                    } 
                })
    });  
}


