package com.ddao.msg;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统待发消息表实体类；
 * </br> Date:2014-05-24
 * @author hyq
 */
@Entity
@Table(name="WAITFOR_SEND_MESSAGE")
public class WaitforSendMessage implements Serializable {
	private Integer messageId,//自增主键
		messageSendCount,//发送失败次数
		messagePort,//远程主机服务端口
		messageSendType=0,//消息发送类型，现在有两种类型：0:自动扫表重发,1:手动重发，,默认为0;
		messageType=0;//,0,短信消息,	1;网络消息，将来还可以是	2:qq消息，	3:微信消息	4:email消息

	private String messageBusinessId,//业务号，唯一性
		messageIp,//主机地址
		messageContent,//消息内容：只支持字符串型消息，多个消息按序用“,”隔开；
		messageErrorText,//上次发送错误文本
		messageUri,//远程服务URI
		messaageMethodName;//消息发送的方法名称；
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="MESSAGE_ID")
	public Integer getMessageId() {
		return messageId;
	}

	public void setMessageId(Integer messageId) {
		this.messageId = messageId;
	}
	
	@Column(name="MESSAGE_TYPE")	
	public Integer getMessageType() {
		return messageType;
	}
	
	/**
	 * 0,短信消息,	1;网络消息，将来还可以是	2:qq消息，	3:微信消息	4:email消息
	 * @param messageType
	 */
	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}
	@Column(name="MESSAGE_METHOD_NAME",nullable=false)
	public String getMessaageMethodName() {
		return messaageMethodName;
	}

	public void setMessaageMethodName(String messaageMethodName) {
		this.messaageMethodName = messaageMethodName;
	}
	@Column(name="MESSAGE_SEND_TYPE")
	public Integer getMessageSendType() {
		return messageSendType;
	}

	/**
	 * 消息发送类型，现在有两种类型：0:自动扫表重发,1:手动重发，,默认为0;
	 * @param messageSendType
	 */
	public void setMessageSendType(Integer messageSendType) {
		this.messageSendType = messageSendType;
	}

	@Column(name="MESSAGE_SEND_COUNT")
	public Integer getMessageSendCount() {
		return messageSendCount;
	}

	public void setMessageSendCount(Integer messageSendCount) {
		this.messageSendCount = messageSendCount;
	}

	@Column(name="MESSAGE_PORT",nullable=false)
	public Integer getMessagePort() {
		return messagePort;
	}

	
	public void setMessagePort(Integer messagePort) {
		this.messagePort = messagePort;
	}

	@Column(name="MESSAGE_BUSSINESS_ID")
	public String getMessageBusinessId() {
		return messageBusinessId;
	}

	public void setMessageBusinessId(String messageBusinessId) {
		this.messageBusinessId = messageBusinessId;
	}

	@Column(name="MESSAGE_IP",nullable=false)
	public String getMessageIp() {
		return messageIp;
	}

	public void setMessageIp(String messageIp) {
		this.messageIp = messageIp;
	}

	@Column(name="MESSAGE_CONTENT")
	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	@Column(name="MESSAGE_ERROR_TEXT")
	public String getMessageErrorText() {
		return messageErrorText;
	}

	public void setMessageErrorText(String messageErrorText) {
		this.messageErrorText = messageErrorText;
	}
	
	@Column(name="MESSAGE_URI",nullable=false)
	public String getMessageUri() {
		return messageUri;
	}

	public void setMessageUri(String messageUri) {
		this.messageUri = messageUri;
	}

}
