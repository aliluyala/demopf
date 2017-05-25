/jsp/dictionary/dictionary.jsp
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/dictionary/queryParent.action',29,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/dictionary/listByPage.action',29,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/dictionary/query.action',29,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/dictionary/query.action',29,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/dictionary/save.action',29,1,'true');



/jsp/syslog/sysuserlog.jsp
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/sysLogListByPage.action',31,1,'true');



/jsp/sysmanager/permission.jsp
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/queryParent.action',4,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/listByPage.action',4,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/query.action',4,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/query.action',4,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/save.action',4,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/querySon.action',4,1,'true');2015-12-14







/jsp/sysmanager/sysrole.jsp
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/listPermission.action',3,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/roleListByPage.action',3,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/queryRole.action',3,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/queryRole.action',3,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/saveRole.action',3,1,'true');




/jsp/sysmanager/sysuser.jsp
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/listPermission.action',2,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/listRoles.action',2,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/userListByPage.action',2,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/queryUser.action',2,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/user/saveUser.action',2,1,'true');


/jsp/usermanager/consignoruser.jsp
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/consignorUserByPage.action',6,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/findConsignorInfo.action',6,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/findConsignorInfo.action',6,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/consignorlocked.action',6,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/checkedConsignorUser.action',6,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/editConsignorUser.action',6,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/consignorStatements.action',6,1,'true');


insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/addExpireTime4Consignor.action',6,1,'true');2015-11-24

insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/addBalance4Consignor.action',6,1,'true');2015-11-25

/jsp/usermanager/newconsignoruserstatistics.jsp
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/newconsignorUserStatistics.action',7,1,'true');


/jsp/usermanager/newtruckuserstatistics.jsp
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/newtruckUserStatistics.action',60,1,'true');


/jsp/usermanager/truckuser.jsp
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/truckUserByPage.action',20,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/findTruckInfo.action',20,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/findTruckInfo.action',20,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/trucklocked.action',20,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/checkedTruckUser.action',20,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/editTruckUser.action',20,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/struckStatements.action',20,1,'true');

insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/addExpireTime4Truck.action',20,1,'true');2015-11-24
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/addBalance4Truck.action',20,1,'true');2015-11-25


insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/addjyRecharge4Truck.action',20,1,'true');2015-12-11
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/addgsRecharge4Truck.action',20,1,'true');2015-12-11

jsp/statistics/companyaccountbooks.jsp
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/companyAccountBooks.action',62,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/balanceCompanyStatements.action',62,1,'true');2015-11-25

/jsp/statistics/companystatement.jsp
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/companyStatements.action',61,1,'true');


/jsp/proxy/proxyinsurace.js
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/findTruckInfo.action',8,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/struckStatements.action',8,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/proxy/insuranceByPage.action',8,1,'true');



insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/proxy/cancleInsurance.action',8,1,'true');2015-11-24


/jsp/proxy/proxyidcardqueryorder.js                                                                                                                        
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/proxy/idCardqueryOrderByPage.action',63,1,'true');

/jsp/proxy/proxyconfig.jsp                                                                                                                        
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/proxy/proxyConfigListByPage.action',65,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/proxy/queryConfig.action',65,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/proxy/proxyConfigSave.action',65,1,'true');


/jsp/proxy/proxyidcardqueryorder.js                                                                                                                        
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/member/feedbackByPage.action',64,1,'true');

/jsp/mall/mall.js
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/thirdmall/thirdMallListByPage.action',67,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/thirdmall/queryMall.action',67,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/thirdmall/addMall.action',67,1,'true');


insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/thirdmall/thirdMallGoodsListByPage.action',68,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/thirdmall/queryMallGoods.action',68,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/thirdmall/delGoods.action',68,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/thirdmall/addGoods.action',68,1,'true');


insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/thirdmall/thirdMallOrderListByPage.action',70,1,'true');



insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/thirdmall/thirdMallTellerListByPage.action',69,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/thirdmall/queryMallTeller.action',69,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/thirdmall/lockTeller.action',69,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/thirdmall/addTeller.action',69,1,'true');

2015-11-25
INSERT INTO public.sys_permission (id,permission_name,permission_url,parent_id,status,is_leaf) VALUES (75,'代理账本','/jsp/statistics/proxystatement.jsp',21,'1',NULL);
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/proxyStatements.action',75,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/balanceProxyStatements.action',75,1,'true');


2015-12-11

INSERT INTO public.sys_permission (id,permission_name,permission_url,parent_id,status,is_leaf) VALUES (92,'油卡充值业务结算','/jsp/statistics/userjyrechargerequest.jsp',21,'1',NULL);
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/userjyRechargeRequest.action',92,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/queryjyRechargeRequest.action',92,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/balanceUserjyRechargeRequest.action',92,1,'true');

INSERT INTO public.sys_permission (id,permission_name,permission_url,parent_id,status,is_leaf) VALUES (93,'高速充值业务结算','/jsp/statistics/usergsrechargerequest.jsp',21,'1',NULL);
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/usergsRechargeRequest.action',93,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/querygsRechargeRequest.action',93,1,'true');
insert into sys_permission(permission_name,permission_url,parent_id,status,is_leaf) values('button','/sys/statistics/balanceUsergsRechargeRequest.action',93,1,'true');
