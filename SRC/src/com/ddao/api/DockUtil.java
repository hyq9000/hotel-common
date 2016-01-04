package com.ddao.api;

import java.io.UnsupportedEncodingException;

import javax.print.Doc;
import javax.print.attribute.DocAttribute;

import org.apache.commons.codec.digest.DigestUtils;

import com.common.log.ExceptionLogger;

/**
 * 提供对接一些工具
 * @author 黄曾海,hyq
 */
public class DockUtil {
	
	/**
	 * 酒店生成授权码
	 * @param authorizedCode (酒店名称+酒店联系号码)
	 * @return 授权码
	 */
	public static String getAuthorizedCode(String authorizedCode ){
		try {
			String code = "DIANDAO!@#"+ authorizedCode;
			//为解决utf-8双字节问题 ，需要转成字节数组  hyq 2014-07-15
			byte[] data=code.getBytes("ISO8859-1");			
			String generateAuthorizedCode = DigestUtils.md5Hex(data);		
			return generateAuthorizedCode;
		} catch (UnsupportedEncodingException e) {
			ExceptionLogger.writeLog(e, DockUtil.class);
			return null;
		}
	}
}
