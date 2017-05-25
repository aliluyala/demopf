Ext.define('datacenter.store.Menus',{
    extend: 'Ext.data.TreeStore',
    requires: 'datacenter.model.Menu',
 	model: 'datacenter.model.Menu',
 	autoLoad: true,
 	proxy: {
        type: 'ajax',
        //method:'POST',
        url: 'sys/user/userPermission.action',
        //url: 'data/manager.json',
        reader: {
            type: 'json',
            //root: 'root',
            successProperty: 'success'
        }
    } 
});
