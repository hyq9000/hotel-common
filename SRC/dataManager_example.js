/*
 * 最常用的配置应该是这个样子的。 
 */
example=new DataManager({
	entityName:"user",//必配:定义实体类对象名称，也就是在action中的实体对象名称;
	entityText:"系统用户",//必配：定义实体类的中文名称,
	entityId:'userId',
	pageSize :20 ,//必配，未配意为无需分页
	listAction:"/action/user!list.action",//必配:查取数据列表ACTION	
	//配置对该数据表新增、修改时的一个默认表单,
	defaulForm:"/userCenter/userForm.html",	
	columns:[
         {field: "userId",title: "用户ID",visible:false},
         {field: "userName",title: "姓名"},
         {field: "userTel",title: "联系电话"},
         {field: "userQQ",title: "QQ号码"},
         {field: "userAge",title: "年龄",type:"int"}
	],
	toolbar:[
         {"ADD":"/action/user!regist.action"},
         {"DELETE","/action/user!delete.action"},
         "QUERY":[userName,userAge]
	],
	rowbar:[
	   {'UPDATE':"/action/user!update.action"},//必配：该功能的提交ACTION,
	   'DELETE'，
	   {'DETAIL':"/action/user!delete.action"}	   
	]	
});