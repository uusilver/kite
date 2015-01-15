package com.tmind.kite.web;

import java.util.HashMap;

public class FrameworkApplication {

	//session缓存全局变量
	private static HashMap<String,Object> sessionManager;
	
	private static class InstanceHolder{
		private static FrameworkApplication instance = new FrameworkApplication();
	}
	
	/**
	 * 构造函数，初始化对象
	 */
	private FrameworkApplication(){
		if(sessionManager==null){
			sessionManager = new HashMap<String,Object>();
		}
	}
	
	/**
	 * 获取单例对象实例
	 * @return 单例对象
	 */
	public static FrameworkApplication getInstance(){
		return InstanceHolder.instance;
	}
	
	/**
	 * 获取session缓存对象
	 * @return HashMap
	 */
	public static HashMap<String,Object> getSessionManager(){
		return sessionManager;
	}
}
