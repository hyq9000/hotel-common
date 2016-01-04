package com.ddao.api;

import java.math.BigDecimal;

/**
 * 此接口由采购系统实现，提供一致的订单返馈功能接口
 * @author hyq 
 * <br/>date:2014年2月25日
 */
public interface OrderResponse {
	/**
	 * 订单返馈后，将订单回单情况保存到数据库，并更改订单状态；
	 * @param orderJson 与平台订单及订单项实体类同结构的JSON对象,如下：
	 *  <pre>
	 *	{
	 *  	orderId;//总订单号 String 
	 *  	orderStart://订单状态0 退单，1回单。
	 *		deptId; //部门ID Integer 
	 *		personNum;//总人数  float 
	 *		total;//订单总金额 BigDecimal 
	 *		backremark;//总订单成功处理结果的回单描述；具体内容再议
	 *		items:[
	 *			ordersId;//总订单号 String				 
	 * 			detailId;//订单明细项ID Integer					
				productionId;//资源产品ID Integer
				orderTime;//行程日期
				budgetNum;//明细项数量 Integer					  
				price;//明细项单价 BigDecimer
				receiptdetails://明细项成功处理结果回单描述，如是订房，可为房号,具体内容再议
	 * }
	 *  </pre>
	 * @return 返回订单提交整型状态码：1：处理成功，0，处理未成功
	 */
	int handleOrderResponse(String authorizedCode, String orderJson);
	
	/**
	 * 同步更新资源企业的单个或多个资源项的基本信息；
	 * @param resourceJson
	 * {
	 * 		  int  t:  //资源企业类型,0  酒店，1 餐饮 2 景区 3 导游 4旅行社 5 运输 
	 * 		  int  f:  //如果为0则表示房型新增操作，为1则表示房型修改
	 * 		  String o： //将要被替换的新的房型名称
	 *  	  info:{
	 *      	String s:  //类别名称：
	 *      	String n:  //数量  	 如果数量的值为0 则表示酒店要对这种房型进行是删除操作
	 *      	Double p:  //价格：资源企业定的原价
	 *  	  }
	 *  }
	 * @return 返回订单提交整型状态码：1：处理成功，0，处理未成功
	 */
	int handleSynchronizeInfo(String authorizedCode,String resourceJson);
	
	/**
	 * 同步更新资源企业可计调的单个资源，或多个资源的增减数量；
	 * @param resourceJson
	 * @param resourceJson json需要封装成如下格式：<pre>
	 *  {
	 * 		Sting  t:  //资源企业类型, 0：酒店 1：餐饮 2：景区 3：导游 ：4：旅行社 5：车队
	 * 		//当资源状态发生变化(只关心可计划调度相关的状态)时，更新数量
	 *      numberinfo:[
 *      	   {
 *      		String s:  //类别名称：
 *     	    	String n:  //数量：房型当时总数量
 *     			Date   d：   //订房的时间
 *             }
	 *       ]      
	 *  }
	 * @return 返回整型状态码：1：处理成功，0，处理未成功
	 */
	int handleSynchronizeStatus(String authorizedCode,String resourceJson);

	/**
	 * 同步更新资源企业的单个资或多个资源项的协议价格；
	 * @param 协议价格，
	 * @param resourceJson json需要封装成如下格式：<pre>
	 *  {
	 * 		Sting  t:  //资源企业类型,0：酒店 1：餐饮 2：景区 3：导游 ：4：旅行社 5：车队
	 * 		//当与旅行社的协议价发生变化时，更新协议价格
	 *      priceinfo:[
 *      		{
 *		    	String a:  //旅行社ID：如果有协议类型值，此值为'';
 *				string t:  //协议类型码(旅行社为：4,散客为：10);如果有旅行社ID值,则此值为'';
 *          	Double p:  //价格:旅行社与资源企业的协议价格；
 *          	String n:  //资源项名称
 *              }
	 *      ]
	 *  }
	 * @return
	 */
	int handleSynchronizePrice(String autorizedCode,String resourceJson);
	/**
	 * 提取资源企业的详情（简介）信息并封装成json格式；
	 * @param authorizedCode
	 * @param enterpriseInfoJson json需要封装成如下格式：<pre>
	 * 	{
	 * 		type:企业类型代号,//1:酒店2:餐厅3:旅行社4:景区5:车队6：导游
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
	 
	 /**
	  * 查取当前已经通过审核的旅行社信息；
	  * @param AgencyID 
	  *  {
	  *  	agid:[ID1,ID2,...] //已经存在的旅行社ID集合
	  *  }
	  * @return 一个包含旅行社信息的json对象；格式如下：
	  * 
	  * 	[{
		         Integer  id; // 旅行社ID
		         String  name;  //旅行社名称
				 String  addr; //旅行社地址
				 String  mobile; //联系人电话
				 String  cName;	 // 联系人名称
				 String  email;   // 邮件
				 String  faxNum;  //传真号                
	  *     }]
	  * 
	  */
	 String getAllAgency(String authorizedCode,String agencyIdInfo);
	 
}
