package com.ddao.msg;

import java.util.Calendar;

/**
 * 消息发送成功或失败后之回调接口:调用UndeliveredMessageService的sendMsg方法时，需要传该接口的实例；
 * </br>Date 2014-05-26
 * @author hyq
 *  */
public interface CallBackInterface {	


	public void callback(int[] rs);
}
