package com.ddao.msg;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.common.dbutil.DaoJpaImpl;
import com.common.log.ExceptionLogger;
import com.common.net.SimpleNetTool;
import com.common.rpc.Rpc;
import com.common.rpc.RpcHessian;
import com.common.tools.TestTool;
import com.ddao.api.OrderRequest;
import com.ddao.api.OrderResponse;
import com.ddao.sms.SendMessageService;
import com.ddao.sms.SendMessageServiceImpl;

/**
 * 当网络失去连接或者出现异常情况，导致订单不能正常发送，把 未能发送出去的订单保存到表里，在第二次再次发送订单，
 * 查询此表，把未发送完的订单发送出去，如果发送成功，就把保留的未发送订单删除。
 * 注意：
 *    在新增待发消息行时，messageContent的值，就是要调用对方法的所有参数，用"~,~"连接起来；如:</br>
 *    调用方法 handleOrderResponse(String authorizedCode, String orderJson)时，则messageContext的值为
 *    authorizedCode+"~,~"+orderJson;
 * @author zengdinggen, hyq
 */
@Stateless
@Remote(UndeliveredMessageService.class)
public class UndeliveredMessageServiceImpl extends DaoJpaImpl<WaitforSendMessage> 
	implements UndeliveredMessageService {
	//这个值需要外部注入，现暂采用默认值
	private boolean proxy=true;
	//这个值需要外部注入，现暂采用默认值
	private String proxyIp="localhost";
	//这个值需要外部注入，现暂采用默认值
	private int proxyPort=8000;
	

	public static void main(String[] args) {
		Rpc<OrderRequest> rpc=new RpcHessian<OrderRequest>(){};
		OrderRequest orderRequest=rpc.getRemoteObject("http","192.168.0.108",
				7777,"/hotel/orderrequestservlet");		
	}
	
	/**
	 * 远程调用的hession实例
	 */
	private Rpc<OrderRequest> rpc=new RpcHessian<OrderRequest>(){};
	
	@Override
	public int sendMsg()throws Exception {
		List<WaitforSendMessage> list=this.getAll();
		int rs=0;
		if(list!=null && list.size()>0){			
			/*
			 * 将所有消息都取出来，根据消息的类型调用不同的发送接口
			 */		
			for(int i=0;i<list.size();i++){
				 try {
					WaitforSendMessage target=list.get(i);
					//如果消息不是“自动重发”的，则跳过；
					if(!target.getMessageSendType().equals(UndeliveredMessageService.MESSAGE_SEND_TYPE_AUTO)){
						continue;
					}
					if(target.getMessageType().equals(UndeliveredMessageService.MESSAGE_TYPE_MSG)){	//如果是网络消息，则调用网络发送接口					
						rs+=this.sendMsg_(target);
					}else if (target.getMessageType().equals(UndeliveredMessageService.MESSAGE_TYPE_SMS)){//如果是短消息,则调用短消息发送接口，
						rs+=this.sendSms_(target);	
					}
				} catch (Exception e) {
					ExceptionLogger.writeLog(e, this.getClass());
				}
			}			
		}
		return rs;
	}	

	@Override
	public int sendMsg(String businessId) throws Exception{
		
		//查取到指定业务号的消息，
		String ql="from WaitforSendMessage WHERE messageBusinessId=?";
		List<WaitforSendMessage> list=this.query(ql, businessId);
		WaitforSendMessage target=list!=null && list.size()>0?  list.get(0):null;		
		if(target==null){
			throw new Exception("没有找到业务号为：'"+businessId+"'的待发消息数据行!");
		}else{
			if(target.getMessageType().equals(UndeliveredMessageService.MESSAGE_TYPE_MSG)){	//如果是网络消息,则调用网络消息发送接口	
					return this.sendMsg_(target);
			}else if (target.getMessageType().equals(UndeliveredMessageService.MESSAGE_TYPE_SMS)){//如果是短消息,则调用短消息发送接口，
				return this.sendSms_(target);	
			}else
				return 0;
		}
	}

	/**
	 * 发送给定的消息
	 * @param target
	 * @return
	 * @throws Exception
	 */
	private int sendMsg_(WaitforSendMessage target) throws Exception{
		if(target!=null){ 	
			//先测试网络是否可达
			if(!SimpleNetTool.isReachable(target.getMessageIp())){	
				throw new Exception("待发消息的目标主机("+target.getMessageIp()+")网络不可达!");
			}

			/*
			 * 根据消息行的相关字段的数据，取得远程调用对象实例，把消息内容作为参数列表，调用该实例对应的方法，以实现消息发送；
			 * 说明：消息行中存储了构建远程对象的所有数据，IP,PORT,URI,要调用的方法(method),方法的参数（消息内容）；
			 */
			Object rs=new Integer(0);
			
			OrderRequest orderRequest=null;
			//如果要求代发网络消息
			if(this.proxy)
				orderRequest=rpc.getProxyRemoteObject("http",target.getMessageIp(),target.getMessagePort(),target.getMessageUri(),this.proxyIp,this.proxyPort);
			else
				orderRequest=rpc.getRemoteObject("http",target.getMessageIp(),target.getMessagePort(),target.getMessageUri());
			
			//取理远程服务对象，则根据消息行数据，解析方法及参数，并执行调用；
			if(orderRequest!=null){					
				String[] params=null;				
				String paramMsg=target.getMessageContent();
				Class<String> clazz[]=null;
				if(paramMsg!=null && !paramMsg.equals("")){					
					params= paramMsg.split("~,~");					
					if(params.length>0){
						clazz=new Class[params.length];
						for(int i=0;i<params.length;i++){
							clazz[i]=String.class;
						}
					}
				}
				try {
					Method method = orderRequest.getClass().getMethod(target.getMessaageMethodName(),clazz);
					System.out.println("开始发送调用请求给代理：---"+System.currentTimeMillis());
					rs=method.invoke(orderRequest,params);	
					if(rs.equals(1)){//如果发送远调调用成功，则删除待发数据行
						this.delete(target);
					}else{
						//如果发送失败，重发次数加1；
						Integer tmp=target.getMessageSendCount();
						tmp=tmp==null?0:tmp;
						target.setMessageSendCount(++tmp);
						target.setMessageErrorText("待发消息的目标方法("+target.getMessaageMethodName()+")执行不成功!");
						this.update(target);
						throw new Exception("待发消息的目标方法("+target.getMessaageMethodName()+")执行不成功!");
					}
				} catch (NoSuchMethodException e) {
					//如果发送失败，重发次数加1；
					Integer tmp=target.getMessageSendCount();
					tmp=tmp==null?0:tmp;
					target.setMessageSendCount(++tmp);
					target.setMessageErrorText("待发消息的目标方法名称("+target.getMessaageMethodName()+")不正确!");
					this.update(target);
					throw new Exception("待发消息的目标方法名称("+target.getMessaageMethodName()+")不正确!");
				}				
			}else{//联系不到主机的服务,重发次数加1；
				Integer tmp=target.getMessageSendCount();
				tmp=tmp==null?0:tmp;
				target.setMessageSendCount(++tmp);
				target.setMessageErrorText("待发消息的目标主机的服务端口("+target.getMessagePort()+")或URI("+target.getMessageUri()+")不正确或不可用!");
				this.update(target);
				throw new Exception("待发消息的目标主机的服务端口("+target.getMessagePort()+")或URI("+target.getMessageUri()+")不正确或不可用!");
			}
			return (Integer)rs;	
			
		}else
			return 0;
	}

	
	/**
	 * 发送短信消息，成功则删除消息，否则重发次数加1，保存发磅错误原因
	 * @param target 待发消息对象
	 * @return 发送成功返回1 否则返回0;
	 * @throws Exception
	 */
	private int sendSms_(WaitforSendMessage target) throws Exception {	
		if(target!=null){ 					
			String rs="";		
			SendMessageService sms=new SendMessageServiceImpl();			
			//取理远程服务对象，则根据消息行数据，解析方法及参数，并执行调用；
			if(sms!=null){					
				rs=sms.sendMessage(target.getMessageIp(), target.getMessageContent());
				if(rs.equals("发送成功")){//如果发送远调调用成功，则删除待发数据行
					this.delete(target);
				}else{
					//如果发送失败，重发次数加1；
					Integer tmp=target.getMessageSendCount();
					tmp=tmp==null?0:tmp;
					target.setMessageSendCount(++tmp);
					target.setMessageErrorText(rs.toString());
					this.update(target);
					throw new Exception("短消息发送失败!");
				}		
				return 1;
			}
		}
		return 0;
	}
	
	@Override
	public int sendSms(String businessId) throws Exception {
		//查取到指定业务号的消息，
		String ql="from WaitforSendMessage WHERE messageBusinessId=?";
		List<WaitforSendMessage> list=this.query(ql, businessId);
		WaitforSendMessage target=list!=null && list.size()>0?  list.get(0):null;		
		if(target!=null){ 					
			String rs="";		
			SendMessageService sms=new SendMessageServiceImpl();			
			//取理远程服务对象，则根据消息行数据，解析方法及参数，并执行调用；
			if(sms!=null){					
				rs=sms.sendMessage(target.getMessageIp(), target.getMessageContent());
				if(rs.equals("发送成功")){//如果发送远调调用成功，则删除待发数据行
					this.delete(target);
				}else{
					//如果发送失败，重发次数加1；
					Integer tmp=target.getMessageSendCount();
					tmp=tmp==null?0:tmp;
					target.setMessageSendCount(++tmp);
					target.setMessageErrorText(rs.toString());
					this.update(target);
					throw new Exception("短消息发送失败");
				}		
				return 1;
			}
		}
		return 0;
	}
	
	@Override
	public void automaticSendSms() throws Exception
	{
		//查询 内容为短信的而且是要求自动发送的消息
		String hql="FROM WaitforSendMessage WHERE messageSendType = ? AND messageType = ?";
		List<WaitforSendMessage>  list=this.query(hql, UndeliveredMessageService.MESSAGE_SEND_TYPE_AUTO,UndeliveredMessageService.MESSAGE_TYPE_SMS);
		if(list != null && list.size()>0)
		{
			for(int i = 0 ;i<list.size();i++)
			{
				WaitforSendMessage wm = list.get(i);
				String businessId = wm.getMessageBusinessId();//获取业务ID号
				this.sendSms(businessId);//循环调用短信重发方法进行短信发送
			}
		}
	}

	
	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	
	/**
	 * 是否采购代理
	 * @param proxy
	 */
	public void setProxy(boolean proxy) {
		this.proxy = proxy;
	}
}
