package com.ddao.api;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;

import com.common.log.ExceptionLogger;
import com.common.rpc.RpcHessian;

/**
 * 封装了UDP打洞、代理服务端与客户端共用的一些逻辑操作；
 * </br>Date :2014-07-10
 * @author hyq
 * 
 */
public class PortAndProxyUtils {
	/**
	 * 取得HTTP请求包中内容的HASH值，以此作为“会话ID”，与远程调用会话;
	 * @param data http请求的协议全部数据
	 * @return 正常返回内容的HASH值,否则返回null;
	 */
	public  static String generateSessionId(byte[] data){
		/*
		 * 实现思路：
		 * 1,解析HTTP协议，获得HTTP请求体内容；
		 * 2，求HTTP的内容的哈希散列算法，得内容的哈希码值
		 * 3，返回该哈希码值
		 */
		//TODO:先简单的这样实现,后续需要只对内容求哈希
		return DigestUtils.md5Hex(data);
	}
	
	/**
	 * 用sesionId封装原生的HTTP协议数据包
	 * 在原生的请求前，加sessionId作为前辍；格式"会话ID：http请求协议内容";	
	 * @param sessioinId
	 * @param http 原生HTTP协议数据包字节数组
	 * @return 正确则返回合并后的数据
	 */
	public static byte[] pakageHttpBySessionId(String sessionId,byte[] httpData){
		return ArrayUtils.addAll((sessionId+":").getBytes(),httpData);
	}
	
	/**
	 * 对addSessionIdToHttp的封装操作作解封装，还原成原生的HTTP数据包；就是去掉前面的sessionId;
	 * @param packagedHttpData 封闭的HTTP协议数据包字节数组
	 * @return 正确则返回原生HTTP的数据包
	 */
	public static byte[] unpakageHttpBySessionId(byte[] packagedHttpData){
		/*
		 * 查找第一个“：”的位置，然后取该位置之后的所有数据
		 */
		int index=ArrayUtils.indexOf(packagedHttpData,(byte)':');
		return ArrayUtils.subarray(packagedHttpData, index+1,packagedHttpData.length);
	}
	
	/**
	 * 获取由addSessionIdToHttp（）封闭的数据包中的sessionId的值
	 * @param packagedHttpData
	 * @return 正确则返回sessionId值
	 */
	public static String getSessionId(byte[] packagedHttpData){
		/*
		 * 查找第一个“：”的位置，然后取该位置之前的所有数据
		 */
		int index=ArrayUtils.indexOf(packagedHttpData,(byte)':');
		byte[] tmp=ArrayUtils.subarray(packagedHttpData, 0,index);
		return new String(tmp);
	}
	
	
	/**
	 * 根据网络包中的数据(这个数据包是经过encodeProxyURl（）方法处理过的),解析出真实的动态IP及端口号;
	 * @param data
	 * @return 解析成功，则返回"ip:端口"格式的字符串。否则返回null;
	 */
	public  static String getIpAndPort(byte[] data){
		String msg=new String(data);
		int start=msg.indexOf("-REQUEST-"),
				end=msg.indexOf("HTTP/1.1");		
		return msg.substring(start+9, end).trim();
	}
	
	
	/**
	 * encodeProxyURl()方法的逆操作
	 * 将代理处理过的数据包还原原始数据包;如：
	 * 将原来的"url-REDIRECT-Ip:端口 HTTP/1.1 XXXX"中的"-REQUEST-Ip:端口 "去掉
	 * @param proxyedData 代理处理过的数据包数据 
	 * @return 返回原始数据包
	 */
	public static  byte[] restoreData(byte[] proxyedData){
		/*
		 * 这里之所以要用"iso8859-1"在String 与byte[]数据组间转换，是因为系统默认为utf-8编码，该编码是双字节编码，在有些地方，会出现乱码问题，
		 * 而强制指定‘ISO8859-1“，就可以在单字节单转拘，也就可以避免以上问题，这个东西调了很久，才找到，来之不易呀；
		 */
		try {
			String proxyStringData = new String(proxyedData,"ISO8859-1");		
			String tmp=proxyStringData.substring(proxyStringData.indexOf("-REQUEST-"),proxyStringData.indexOf("HTTP/1.1"));
			return proxyStringData.replace(tmp," ").getBytes("ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			ExceptionLogger.writeLog(e, PortAndProxyUtils.class);	
		}
		return null;			
	}
	
	/**
	 *  生成代理URL,过程如下：
	 *  将真实IP及端口附在url后,用"-REQUEST-"分隔
	 * 	原生URL如：http://209.234.20.45:45678/hotel/dosomething
	 *  生成代理URL如:http://localhost:8888/hotel/dosomething-REDIRECT-209.234.20.45:45678
	 * @param nativeContent 原生Hessian调用的URL；
	 * @param ip 远程资源应用的真实IP
	 * @param port 远程资源应用的真实端口
	 * @return 返回一个新的URI；
	 */
	public static  String encodeProxyURl(String nativeUrl,String proxyIp,int proxyPort){
		try {
			URL url=new URL(nativeUrl);
			String host=url.getHost();
			int port =url.getPort();
			String path=url.getPath();
			//把真实目标IP及端口附在后面，前面改成代理IP及端口
			String newUrl=url.getProtocol()+"://"+proxyIp+":"+proxyPort+path+"-REQUEST-"+host+":"+port;
			return newUrl;
		} catch (MalformedURLException e) {
			ExceptionLogger.writeLog(e, PortAndProxyUtils.class);
			return null;
		}
	}
	
	
	/**
	 * 还原代理URL为原生URL,过程如下：
	 * 代理URL如:  http://localhost:8888/hotel/dosomething-REQUEST-209.234.20.45:45678	
	 * 还原成URL如： http://209.234.20.45:45678/hotel/dosomething
	 * @param proxyUrl 代理URL
	 * @return 返回一个新的URI；
	 */
	public static String uncodeProxyURl(String proxyUrl){
		try {
			String[] tmps=proxyUrl.split("-REQUEST-");
			if(tmps!=null && tmps.length>1){
				String[] ipAndPort=tmps[1].split(":");
				if(ipAndPort!=null && ipAndPort.length>1){
					URL url=new URL(tmps[0]);
					String path=url.getPath();
					String newUrl=url.getProtocol()+"://"+ipAndPort[0]+":"+ipAndPort[1]+"/"+path;
					return newUrl;
				}
			}
			return null;			
		} catch (MalformedURLException e) {
			ExceptionLogger.writeLog(e, PortAndProxyUtils.class);
			return null;
		}
	}
	
	
	
	
}
