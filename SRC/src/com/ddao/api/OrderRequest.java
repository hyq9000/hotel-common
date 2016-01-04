package com.ddao.api;

/**
 * 本接口由资源企业实现，提供订单采购的远程调用接口； * 
 * @author hyq
 *<br/> date:2014年2月25日
 */
public interface OrderRequest {
	/**
	 * 处理由采购系统提交过来的采购订单,具体处理过程以落地资源应用业务需求为准;
	 * @param orderJson JSON对象,结构如下：
	 * <pre>
	 *	{
	 *  	orderId;//总订单号 String 
	 *		agencieName; //旅行社名String 
	 *		deptId; //部门ID Integer 
	 *		personNum;//总人数  float 
	 *		total;//订单总金额 BigDecimal 
	 *      guideName; //导游姓名
	 *      guideMoblie; //导游电话号码
	 *      time;//日期 datetime
	 *      status;//订单的状态    
	 *		orderDetaile:[
	 *			ordersId;//总订单号 String				 
	 * 			detailId;//订单明细项ID Integer					
				productionId;//明细项ID Integer
				itemName;//明细项名称  String
				budgetNum;//明细项数量 Integer
				ordertime;//日期 datetime					  
				price;//明细项单价 BigDecimer
				
	 *		]
	 * }
	 *  </pre>
	 * @return 返回订单提交整型状态码：1：接收成功，0，接收未成功
	 */
	int handleOrder(String orderJson);

	/**
	 * 处理采购系统发送来的消息,实现将传来消息在前台以模态窗口方式显示；
	 * @param messageJson 格式化消息内容,格式如下：<br/>
	 *  {
	 *  	type:"text"|"html"|"script",
	 *  	title:"消息标题"
	 *  	content:"消息内容"
	 *  
	 *  }
	 * @return 返回订单提交整型状态码：1：提交成功，0，提交未成功,-1:网络异常
	 */
	int handleMsg(String messageJson);

	/**
	 * 将采购系统的授权码保存到资源应用本地数据表中；
	 * @param 授权码  一个随机的字符串 
	 * @return 返回参数 1 表示授权码已经保存成功  否则 0 表示授权保存失败
	 */
	int handleAuthorizedCode(String authorizedCode);
	
	/**
	 * 保存由采购系统提交过来的采购订单；（这些保存的订单，需要提供一个条件查看的功能；）；
	 * @param orderJson JSON对象,结构如下：
	 * <pre>
	 * {
	 *  	orderId;//总订单号 String 
	 *		agencieName; //旅行社名String 
	 *		companyName;//落地资源企业名String 
	 *		deptId; //部门ID Integer 
	 *		personNum;//总人数  float 
	 *		total;//订单总金额 BigDecimal 
	 *      time;//日期 datetime
	 *		items:[
	 *			ordersId;//总订单号 String				 
	 * 			detailId;//订单明细项ID Integer					
				productionId;//明细项ID Integer
				itemName;//明细项名称  String
				budgetNum;//明细项数量 Integer
				ordertime;//日期 datetime					  
				price;//明细项单价 BigDecimer
				
	 *		]
	 * }
	 * </pre>
	 * @return 返回订单提交整型状态码：1：接收成功，0，接收未成功
	 */
	int saveOrder(String orderJson);
	
	/**
	 * 提取资源企业的详情（简介）信息并封装成json格式；(hzh add 作用于点到网修改的酒店资料信息同步到酒店)
	 * @param authorizedCode
	 * @param enterpriseInfoJson json需要封装成如下格式：<pre>
	 * 	{
	 * 		type:企业类型代号,//资源企业类型,0  酒店，1 餐饮 2 景区 3 导游 4旅行社 5 运输 
	 * 		info:{
	 * 			 Integer id;//企业ID
				 String account;//企业银行帐号
				 String address;//具体地址
				 String contactsMobile;//联系人电话
				 String contactsName;//联系人
				 String email;//联系人邮箱
				 String fax;//传真号
				 String host;//ip或域名
				 Double lat;//定位纬度				 
				 Double  Lng;//定位经度
				 Integer level;//星级
				 String  name;//企业名
				 Integer parentId;//父级id
				 Integer hostPort;//端口
				 String provinces;//省市区
				 String remark;//备注
				 Integer roomCount;//房间数
				 String synopsis;//企业简介
				 String zipcode;//邮编
	 * 		}
	 *	}
	 * </pre>
	 * @return 
	 */
	 int upLoadEnterpriseInfo(String authorizedCode,String enterpriseInfoJson);	
     
}
