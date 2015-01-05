package com.tmind.kite.model;

public class TxtMsgModel {
	
	private String telno;
	private String content;
	public String getTelno() {
		return telno;
	}
	public void setTelno(String telno) {
		this.telno = telno;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "TxtMsgModel [telno=" + telno + ", content=" + content + "]";
	}
	
	
	
}
