/* 
 * 可快速的定义一个针对实体数据表的增删改查之UI完整功能;
 * 1,属性给出值的，描述为默认值，
 * 2,注释为“必配”，则实现应对未配抛异常，未作“必配”说明，则为选配项
 * 3，定义一个默认配置文件，意在为同一项目的所有数据表格定义统一风格；
 * 4,任何一次ajax提交，应即时启动锁屏功能，提示友好信息(如：请求正在处理中...),处理完后，解锁屏幕；
 * 5,提供一个java小工具，根据指定的数据库及表，自动生成本js文本;
 */
dm=new DataManager({
	entityName:"",//必配:定义实体类对象名称，也就是在action中的实体对象名称;
	entityId:'',//必配，实体对象之ID属性名
	entityText:"",//必配：定义实体类的中文名称,
	icon:'默认数据表图标',//表标题栏左上角图标
	full:true,//表格平铺父级容器,此值配置为true,则忽略size,position值
	size:{//数据表格的大小		
		height:400,//表格高
		width:800 //表格宽
	}, 
	position:{//数据表格位置坐标
		x:0,//相对于父级容器的左上角
		y:0 //相对于父级容器的左上角
	}
	pageSize :, ////必配，每页行数;如无此项，则为不分页
	listAction:"",//必配:查取数据列表ACTION
	
	//原生UI的属性配置,以方便用户对原生配置的需求,也算是补充本构件的不足之处
	nativeConfig:{
	/*
	 * 需要原生的UI额外配置的项，在此配置如:
	 */
	},	
	
	//配置对该数据表新增、修改时的一个默认表单,
	defaulForm:{		
		url:"",//必配：新增表单录入页URL
		title: "新增"+entityText,//弹窗标题
		icon:"默认窗口图标",//弹窗左上角图标URL
		height:400,//表单高
		width:350 //表单宽
	},	
	//表格列字段属性数组
	columns:[ 	
          'CHECK_BOX':true,//显示多选框列
          'INDEX':false,//显示行序号
          {
    	  	field: "",//必配，实体类属性名称
    	  	title: "",  //必配 ，表列名
    	  	width :100, /*列宽像素*/,
    	  	/*
    	  	 * 可以有以下几种：number | string_n | string |  date- |date | image | money 
    	  	 * image:用<img>显示该URL的图片,
    	  	 * money:前面加"￥",如 ￥123.00
    	  	 * date-cn:解析成"2012年12月12日 23:12:23"格式；
    	  	 * date :解析成"2012-12-12 23:12:23"格式；
    	  	 * string-n :原样显示;当文本长度超过n时，超过部分用“..."代替";
    	  	 * number,string:原样显示
    	  	 */
    	  	type: "string",
    	  	color:"默认颜色",//字体颜色
    	  	visible:true //该列在表中是否可见
    	  	/*
    	  	 * 自定义列的显示形式,有此配置，则上面type配置所引发的显示效果失效
    	  	 * value:当前列的值
    	  	 * row:当前行对象
    	  	 * index:当前列所在的下标
    	  	 */
    	  	formatter: function(value,row,index){
    	  		return "html脚本";
    	  	}
          }
	], 	
	/*
	 * 表头工具栏配置
	 */
	toolbar:{
		align:'TOP',//工具栏放置数据表的上(TOP)、下(BOTTOM)、左(LEFT)、右(RIGHT)方;
		buttons:[
		     /*
		      * 提供新增功能，当按下新增，弹出新增表单，“保存并继续”提交表单后，新增的数据加载到当前数据表的首行;
		      * 并清空各表单项,等待用户下一次新增；
		      * 按“保存并关闭”，则新增的数据加载到当前数据表的首行，并立刻关闭表单弹窗;
		      */
			 "ADD":{
				 text:'新增',//工具按钮文本
				 icon:"默认新增图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
				 action:"",//必配；提交新增的ACTION
				 //默认为上级defaultForm的配置
				 form:{
					url:"默认为defaultForm的url",//必配：新增表单录入页URL
					title: "新增"+entityText,//弹窗标题
					icon:"默认窗口图标",//弹窗左上角图标URL
					onload:function(){/*功能FORM加载时做什么*/},
					onclose:function(){/*功能FORM关闭时做什么*/},
					height:400,//表单高
					width:350 //表单宽
				 }
			 },
			 /*
			  * 批量删除，首先提供 确认删除 确认框，
			  * 1点确认后，提交当前选中行的实体ID组成的字符串到后台(如未选中行，则些删除按钮不可用)，删除成功后，从当前数据表中删除选中的行;
			  * 2点取消，则不做任何操作,
			  */
			 "DELETE":{
				 text:'删除',//工具按钮文本
				 icon:"默认删除图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
				 /*
				  * 必配：提交批量删除ACTION,默认为单行删除ACTION，应传当前选中行的主键ID序列字符串，格式如下：
				  * "id1,id2,id3,id4,...",如"12,4,5,2"
				  */
				 action:"",//批量删除之action;
			 },
			 /*
		      * 提供简单单条件查询功能：自定义查询条件字段，可选择各种关系操作符（>,<,>=,<=,=,!=，起...止;
		      * 操作符说明：
		      * 	1针对字符串，= 应作 模糊等于处理；>,<,>=,<=都应不可见
		      * 	2针对日期，则只应有起止两个日期框可选;
		      * 应提供清空查询条件功能，实现无条件原始查询
		      * 默认说明：
		      * 	1，条件框不提供默认项，需要用户选择，如款选择，则查询按钮不可用
		      *     2,操作符选择框，默认为"=";
		      */
			 "QUERY":{
				 text:'查询',//工具按钮文本
				 icon:"默认查询图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
				 isEmbed:true,//该录入界面是否直接嵌在表头工具栏上,false时，为弹窗式
				 action:listAction，//默认为 listAction的值,
				 //当isEmbed为true时，此项配置无效
			 	 form:{
					url:"",//查询条件数据录入页URL
					title:"查询"+entityText,//弹窗标题
					icon:"默认窗口图标",//弹窗左上角图标URL
					fields:[/*默认为所有列*/]//如果是日期类型，则需要出现起止两个输入框	,    
					onload:function(){/*功能FORM加载时做什么*/},
					onclose:function(){/*功能FORM关闭时做什么*/},
					height:50,//表单高
					width:200 //表单宽
				 }
			 },
			 {//配置可扩展功能按钮
				 text:'',//必填:工具按钮文本
				 icon:"默认图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
				 ajax:{//ajax提供相关配置
					 url:""，//必配：该功能的提交ACTION
					 data:{},//参数
					 success:function(rs){},//成功响应处理逻辑，rs为响应内容
					 error:function(msg){} //请求出现错误
				 },
			 	 form:{
					url:"",//必配：查询数据录入页URL
					title:"",//必配：弹窗标题
					icon:"默认窗口图标",//弹窗左上角图标URL
					onload:function(){/*功能FORM加载时做什么*/},
					onclose:function(){/*功能FORM关闭时做什么*/},
					height:400,//表单高
					width:350 //表单宽
				 }
			 }
		]
	},
	//数据行中工具按钮配置
	rowbar:{
		align:0,//将此工具栏放在第几列
		button:[
		   'UPDATE':{
			   text:'修改',//必填:工具按钮文本
			   icon:"默认图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
			   action:""，//必配：该功能的提交ACTION
			   form:{//defaultForm的配置
				   	url:"默认为defaultForm的url",//必配：查询数据录入页URL
					title:"修改"+entityText,//必配：弹窗标题
					icon:"默认窗口图标",//弹窗左上角图标URL
					onload:function(){/*默认实现将当前行的数据值加载到表单页各项中*/},
					onclose:function(){/*无实现*/},
					height:400,//表单高
					width:350 //表单宽
			   }
		   },
		   //删除当前行按钮
		   'DELETE':{
			   text:'删除',//必填:工具按钮文本
			   icon:"默认删除图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
			   action:"默认为批量删除action"，//单项删除功能的提交ACTION,应传一个当前选中行的主键ID
		   }
		   //查看详情按钮
		   'DETAIL':{
			   text:'详情',//必填:工具按钮文本
			   icon:"默认详情图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
			   action:"",//必配：查看详情功能的提交ACTION,会传一个当前行的主键
			   form:{//默认为上级defaultForm的配置
				   	url:"默认为defaultForm的url",//必配：查询数据录入页URL
				   	title:entityText+"详情",//必配：弹窗标题
					icon:"默认窗口图标",//弹窗左上角图标URL
					onload:function(){/*功能FORM加载时做什么*/},
					onclose:function(){/*功能FORM加载时做什么*/},
					height:400,//表单高
					width:350 //表单宽
			   }
		   },
		   //配置一个行功能扩展按钮
		   {
			 text:'',//必填:工具按钮文本
			 icon:"默认图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
			 ajax:{//ajax提供相关配置
				 url:""，//必配：该功能的提交ACTION
				 data:{},//参数
				 success:function(rs){},//成功响应处理逻辑，rs为响应内容
				 error:function(msg){} //请求出现错误
			 },
			 form:{
				url:"",//必配：查询数据录入页URL
				title:"",//必配：弹窗标题
				icon:"默认窗口图标",//弹窗左上角图标URL
				onload:function(){/*功能FORM加载时做什么*/},
				onclose:function(){/*功能FORM加载时做什么*/},
				height:400,//表单高
				width:350 //表单宽
			 }  		
		   }
		]
	}	
});

/*
 * 提供常用的几个方法
 */
/*
 * 返回当前数据表中的所有数据对象,数据结构如下：
 * data=[
 * 		{数据行对象},
 * 	  	{name:'',age:12,address:'',...},
 * 		{...}，  	  
 * 	  	...	
 * ]
 */
data=dm.getData();
/*
 * 取得选中row对象,可以单行或多行选中
 * rows=[
 * 	{name:'',age:12,address:'',...}
 * ] 
 */
rows=dm.getSelectedRows();
/*
 * 采用指定的参数值，加载数据
 * 参数:加载时，要传的参数列表 
 */
dm.load({name:'',age:23});

/*
 * 调用UI原生函数
 */
result=dm.invoke({
	functionName:"",//原生函数名
	//参数值
	params:[]
	}
);

/*
 * 绑定事件
 */ 
dm.bindEvent({
	eventName:"",//事件名称:原生UI支持的事件
	handle:function(evp1,evp2,evp3/*事件参数*/){
		//定义事件处理
	}
});
