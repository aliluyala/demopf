Ext.define('datacenter.view.Header', { 
    extend: 'Ext.panel.Panel' ,
    border: false,
    layout:'anchor',
    region:'north',
    cls: 'docs-header',
    height:90,
    items: [{
		id:'header-top',
		xtype:'box',
		cls:'header',
		border:false,
		anchor: 'none -40'
	},new Ext.Toolbar({
		anchor: 'none -50',
		items:[
			{
			 //此处加载登录用户信息
			 xtype:'label',
			 iconCls: 'grid-add',
			 id:'head-lb-1',
			 cls:'welcome',
			 text:'欢迎登陆,XXX',
			 margin:'0 20 0 20'
			 },'-',
			 {
			 xtype:'label',
			 id:'head-lb-3',
			 margin:'0 0 0 20',
			 cls:'welcome',
			 text:'当前日期：2013-03-20'
			 }, '->', {
			 	xtype:'button',
			 	text:'首页',
				iconCls: 'home',
				//tooltip: '全屏显示主操作窗口',
				handler: function(){
				 	var main = Ext.getCmp("content-panel"); 
			        var tab = main.getComponent("HomePage");       
			        main.setActiveTab(tab);  
				}
			 },'-',{
			 	xtype:'button',
			 	id:'changepassword',
			 	text:'修改密码',
				iconCls: 'key'
			 },'-',{
			 	xtype:'button',
			 	text:'刷新',
				iconCls: 'refush',
				handler: function(){
				}
			 },'-',{
			 	xtype:'button',
			 	text:'帮助',
				iconCls: 'book',
				handler: function(){
				}
			 },'-', {
				xtype:'button',
			 	text:'注销',
				iconCls: 'user_go',
				handler: function(){
				 Ext.Ajax.request({url:'sys/user/logout.action',  params : {},method:'post',type:'json',
						success:function(result, request){
							var responseObj =  Ext.decode(result.responseText);
							if(responseObj.success){
								var redirect = 'login.jsp'; 
						        window.location = redirect;
							}
						}
				});
				 }
			 },'-'
		]}
	)]
}); 
