package com.tmind.kite.utils;

import com.tmind.kite.constants.MessageContent;

public class ComposeMessage {

	public static String composeMessage(String msgFormat,String[] values){
		
		String msg = msgFormat;
		
		if(values!=null&&values.length>0){
			for(int i = 0 ; i<values.length ; i++){
				msg = msg.replace("{"+i+"}", values[i]);
			}
		}
		return msg;
	}
	
	public static void main(String[] args){
		String[] values = {"23:56","Andorid"};
		String msg = composeMessage(MessageContent.MSG_LOGIN_OTHER_CLIENT,values);
		System.out.println(msg);
	}
}
