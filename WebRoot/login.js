Ext.onReady(function(){
	Ext.get("form").dom.onkeydown=function(e){
		if(e.keyCode==13){
			login();
		}
	};
	Ext.get("loginbut").dom.onclick=function(){
		login();
	};
	
	Ext.get("userName").dom.onfocus=function(e){
		var tip = Ext.get("tip");
		tip.setStyle('display','none');
	};
	Ext.get("passWord").dom.onfocus=function(e){
		var tip = Ext.get("tip");
		tip.setStyle('display','none');
	};
	function login(){
		var userName = Ext.get("userName").getValue();
		var passWord = Ext.get("passWord").getValue();
		var tip = Ext.get("tip");
		Ext.Ajax.request({url:'sys/user/userLogin.action',  params : {userName:userName,passWord:passWord},method:'post',type:'json',
				success:function(result, request){
					var responseObj =  Ext.decode(result.responseText);
					if(responseObj.success){
//				       window.location.href = 'index.jsp';
						//window.navigate("index.jsp"); 
				       //window.navigate("index.jsp");
				       window.location.replace("index.jsp");
					}else{
						tip.setStyle('display','');
					}
				}
		});
	}
});