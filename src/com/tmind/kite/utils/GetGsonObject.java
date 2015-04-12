package com.tmind.kite.utils;

import com.google.gson.Gson;

public class GetGsonObject {

	/*
	 * @author: Junying Li
	 * 获得Gson对象的类，采用饿汉单例模式，类加载比较慢，但是获得对象更快
	 * 所有获得Gson对象的地方都从这个类里获取
	 */
	private static Gson instance = new Gson();
	
	//私有化构造类
	private GetGsonObject(){}
	
	public static Gson getInstance(){
		return instance;
	}
}
