package com.ddao.msg;

import com.common.dbutil.Dao;

/**
 * 当网络失去连接或者出现异常情况，导致订单，消息不能正常发送，把 未能发送出去的订单保存到表里，
 * 在第二次再次发送订单，查询此表，把未发送完的订单发送出去，
 * 如果发送成功，就把保留的未发送订单删除。目前这个版本支持短消息，网络消息的发送
 * @author zengdinggen,hyq
 */
public interface UndeliveredMessageService extends Dao<WaitforSendMessage>{
	/**
	 * 给WaitforSendMessage赋值时,根据消息类型的不同；各个字段说明如下，未作说明的，无需赋值
	 * <li>如果是短消息时，按以下要求赋各字段值:</li>
	 * 		<div style='padding-left:40px">messagePort:-1;</div>
	 * 		<div style='padding-left:40px">messageIp:手机号;</div>
	 * 		<div style='padding-left:40px">messageMethodName:no;</div>
	 * 		<div style='padding-left:40px">messageMethodUri:no;</div>
	 * 		<div style='padding-left:40px">messageContent:短消息内容</div>		
	 * 		<div style='padding-left:40px">messageType:0;</div>	 		
	 * 		<div style='padding-left:40px">messageSendType:0:自动重发，1，手动重发</div>
	 * <li>如果是网络消息时，则按以下要求赋各字段值:</li>
	 * 		<div style='padding-left:40px">messagePort:服务端口号;</div>
	 * 		<div style='padding-left:40px">messageIp:服务主机IP;</div>
	 * 		<div style='padding-left:40px">messageMethodName:发消息时，所要调用的接口方法名称</div>
	 * 		<div style='padding-left:40px">messageUri:服务URI</div>
	 * 		<div style='padding-left:40px">messageContent:网络消息内容</div>
	 * 		<div style='padding-left:40px">messageType:1;</div>
	 * 		<div style='padding-left:40px">messageSendType:0:自动重发，1，手动重发</div>
	 */
	void add(WaitforSendMessage entity) throws Exception;
	
	
	/**
	 * 实现失败短信消息的重新发送</br>
	 * 查询所有发送失败的且messageType的值为0和messageSendType值为0的所有失败短信消息
	 * 调用已有的sendSms方法进行循环短信发送
	 * <font color='red'>注意：建议在新线程执行本方法</font>
	 * @throws Exception
	 */
	
	void automaticSendSms()throws Exception;
	
	/**
	 * 短消息
	 */
	int MESSAGE_TYPE_SMS=0,
			/**
			 * 网络消息
			 */
			MESSAGE_TYPE_MSG=1,
			/**
			 * qq消息
			 */
			MESSAGE_TYPE_MSG_QQ=2,
			/**
			 * 微信消息
			 */
			MESSAGE_TYPE_MM=3,
			/**
			 * 邮箱消息
			 */
			MESSAGE_TYPE_EMAIL=4,
			/**
			 * 自动重发
			 */
			MESSAGE_SEND_TYPE_AUTO=0,
			/**
			 * 手动重发
			 */
			MESSAGE_SEND_TYPE_MANUAL=1;
	
	/**测试远程主机超时时长,单位:毫秒*/
	int TIMEOUT = 3000;   	
	/**
	 *  客户端应用:查取所有待发消息行，发送给采购平台；</br>
	 *  针对每行发起新线程，完成以下工作：<ol>
	 * 	<li>根据待发消息行提供IP,端口，消息内容，发送每条消息数据；
	 * 	<li>发送成功了，则从库中删除该行数据，否则继续保留该行;
	 * 	<li>并记录递增重发次数，失败原因;
	 * </ol>	 * 
	 * @return 如果发送成功，则返回成功的条数,否则返回0;	 
	 */
	public int sendMsg() throws Exception;
		
	/**
	 *  采购端:查取给定ID的消息行，将消息内容发送到给定的客户端应用</br>,
	 *  具体完成以下工作：<ol>
	 * 	<li>根据待发消息行提供IP,端口，消息内容，发送消息数据；
	 * 	<li>发送成功了，则从库中删除该行数据，否则继续保留该行;
	 * 	<li>并记录递增重发次数，失败原因;
	 * </ol>
	 * 	<font color='red'>注意：建议在新线程执行本方法</font>
	 * @param bussinessId 业务号，指的是业务帐单号
	 * @return 如果发送成功，则返回1,否则返回0;
	 */
	public int sendMsg(String businessId) throws Exception;
	
	/**
	 *  采购端:查取给定业务帐单ID的消息行，将消息内容以短信的方式发送到给定的资源企业的手机上</br>,
	 * 	<font color='red'>注意：建议在新线程执行本方法</font>
	 * @param bussinessId 业务号，指的是业务帐单号
	 * @deprecated 此方法已经不建议使用，后继请调用sendMsg(String bussinessId);
	 * @return 如果发送成功，则返回1,否则返回0;
	 */
	public int sendSms(String bussinessiId) throws Exception;
}
