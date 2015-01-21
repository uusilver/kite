package com.tmind.kite.utils;

import org.apache.log4j.Logger;


public class TxtHandler {

	protected static final Logger logger = Logger.getLogger(TxtHandler.class);

	public static boolean sendTxt(String telno, String content){
		logger.debug("电话短信发出,号码为:"+telno);
		logger.debug("短信内容为:"+content);
		return true;
	}
	
	public static boolean receiveTxt(String telno){
		logger.debug("电话短信收取,号码为:"+telno);
		return true;
	}
}
