package com.tmind.kite.utils;

public class TxtHandler {

	public static boolean sendTxt(String telno, String content){
		System.out.println("电话短信发出,号码为:"+telno);
		System.out.println("短信内容为:"+content);
		return true;
	}
	
	public static boolean receiveTxt(String telno){
		System.out.println("电话短信收取,号码为:"+telno);
		return true;
	}
}
