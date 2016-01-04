globalConfig={
		title:entityText+"管理",
		icon:'默认数据表图标',//表标题栏左上角图标
		height:400,//表格高
		width:800, //表格宽
		pageSize :20, //每页行数;如无此项，则为不分页
		
		//配置对该数据表新增、修改时的一个默认表单,
		defaulForm:{		
			title: "新增"+entityText,//弹窗标题
			icon:"默认窗口图标",//弹窗左上角图标URL
			height:400,//表单高
			width:350 //表单宽
		},	
		/*
		 * 工具栏按照配置的顺序排列
		 */
		toolbar:{
			align:'TOP',//工具栏放置数据表的上(TOP)、下(BOTTOM)、左(LEFT)、右(RIGHT)方;
			buttons:[
				 "ADD":{
					 text:'新增',//工具按钮文本
					 icon:"默认新增图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
					 //默认为上级defaultForm的配置
					 form:{
						title: "新增"+entityText,//弹窗标题
						icon:"默认窗口图标",//弹窗左上角图标URL
						height:400,//表单高
						width:350 //表单宽
					 }
				 },
				 //批量删除，要求提供 确认删除 确认框,可简单配置‘DELETE'
				 "DELETE":{
					 text:'删除',//工具按钮文本
					 icon:"默认删除图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
					 action:''//必配
				 },
				 /*
			      * 提供简单单条件查询功能：自定义查询条件字段，可选择各种关系操作符（>,<,>=,<=,=,!=，起...止;
			      * 针对字符串，= 应作 模糊等于处理；>,<,>=,<=都应不可选
			      * 针对日期，则只应有起止可选
			      */
				 "QUERY":{
					 text:'查询',//工具按钮文本
					 icon:"默认查询图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
					 isEmbed:true,//该录入界面是否直接嵌在表头工具栏上,false时，为弹窗式
					 action:listAction，//默认为 listAction的值,
					 //当isEmbed为true时，此项配置无效
				 	 form:{
						title:"查询"+entityText,//弹窗标题
						icon:"默认窗口图标",//弹窗左上角图标URL   
						height:400,//表单高
						width:350 //表单宽
					 }
				 }			
			]
		},
		//数据行中工具按钮配置,按钮按配置顺序先后摆放
		rowbar:{
			align:0,//将此工具栏放在第几列
			button:[
			   'UPDATE':{
				   text:'修改',//必填:工具按钮文本
				   icon:"默认图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
				   form:{//默认为上级defaultForm的配置
						title:"修改"+entityText,//必配：弹窗标题
						icon:"默认窗口图标",//弹窗左上角图标URL
						height:400,//表单高
						width:350 //表单宽
				   }
			   },
			   //删除当前行按钮
			   'DELETE':{
				   text:'删除',//必填:工具按钮文本
				   icon:"默认删除图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
			   }
			   //查看详情按钮
			   'DETAIL':{
				   text:'详情',//必填:工具按钮文本
				   icon:"默认详情图标",//工具图标,有此项，则在鼠标悬停后的提示text,无此项，则用按钮
				   form:{//默认为上级defaultForm的配置
						title:entityText+"详情",//必配：弹窗标题
						icon:"默认窗口图标",//弹窗左上角图标URL
						height:400,//表单高
						width:350 //表单宽
				   }
			   }
			]
		}	
};