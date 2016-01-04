package com.ddao.api;


/**
 * UDP穿透时，定义实时将最新的客户端动态IP及端口更新到数据库的相关接口,分别由打洞代理客户及服务端实现
 * </br>Date:2014-06-25
 * @author hyq
 */
public interface PortAndProxyService{	
	
	/**
	 * 根据授权码找到指定的资源企业，并更新该企业的IP，端口,
	 * 该方法由打洞服务器实现
	 * @param authorizeCode 授 权码
	 * @param ip 新IP
	 * @param port 端口
	 * @param resourceType 资源企业类型
	 * @return 更新成功，则返回1，否则返回0;
	 */
	int updateIPAndPort(int resourceType,String authorizeCode,String ip,int port) throws Exception;
	/**
	 * 根据授权码及资源企业类型查取对应的最新IP及端口号
	 * 该方法由打洞服务端实现
	 * @param authorizeCode 授 权码
	 * @param resourceType 资源企业类型
	 * @return 成功则返回一个"IP址，端口号"格式的字符串，如“220.168.23.124:4560",否则返回null;
	 */
	String getIPAndPort(int resourceType,String authorizeCode) throws Exception;
	
	/**
	 * 返回布署同一IP的所有资源企业（包括子级企业）的授权码；
	 * 该方法由打洞客户端实现
	 * @return 查取成功，则返回授权码字符串数组
	 */
	String[] queryAuthorizeCode()  throws Exception;
	
}
