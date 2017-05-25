Ext.define('datacenter.view.South',{ 
    extend: 'Ext.Toolbar', 
    initComponent : function(){ 
        Ext.apply(this,{ 
            id:"bottom", 
            //frame:true, 
            region:"south", 
            height:23, 
            items:['->',"版权所有     Copyright © 2017 驿道网络科技有限公司",'->'] 
        }); 
        this.callParent(arguments); 
    } 
}); 
