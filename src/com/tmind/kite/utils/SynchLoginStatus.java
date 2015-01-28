package com.tmind.kite.utils;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.constants.MessageContent;
import com.tmind.kite.model.User;
import com.tmind.kite.web.FrameworkApplication;

public class SynchLoginStatus {

	protected static final Logger logger = Logger.getLogger(SynchLoginStatus.class);

	/**
	 * 根据手机号码和客户端类型检查用户是否已经登录
	 * 1.如果返回结果为null则说明用户没有在任何客户端登录
	 * 2.如果返回结果为空Map对象，则说明用户请求中缺少客户端类型信息，此请求为不合法请求
	 * @param telNo
	 * @param clientType
	 * @return HashMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap synchLogin(String telNo, String clientType) {

		HashMap resultMap = new HashMap();

		//根据登录用户的手机号码从session管理器中获取session对象
		HttpSession session = getSession(telNo);
		
		User user =null;
		String loginedClient = "";
		if(session!=null){
			user = (User)session.getAttribute(CommonConstants.USER_LOGIN_TOKEN);
			
			if(CommonConstants.ACCESS_FROM_IOS.equals(user.getClientType())){
				loginedClient = "IOS App";
			}else if(CommonConstants.ACCESS_FROM_ANDROID.equals(user.getClientType())){
					loginedClient = "Android App";
			}else if(CommonConstants.ACCESS_FROM_WEIXIN.equals(user.getClientType())){
				loginedClient = "Weixin App";
			}else if(CommonConstants.ACCESS_FROM_WEBAPP.equals(user.getClientType())){
				loginedClient = "Web App";
			}
			
		}else{
			return null;
		}

		// 用户从IOS或者Android APP进入
		if (CommonConstants.ACCESS_FROM_IOS.equals(clientType)|| CommonConstants.ACCESS_FROM_ANDROID.equals(clientType)) {
			
			logger.info("用户[" + telNo + "]请求来自："+ (clientType.equals(CommonConstants.ACCESS_FROM_IOS) ? "IOS APP": "Android APP"));

			// 如果取到用户信息,则说明用户已经登录
			if (user != null) {

				// 用户已经在微信或者Web App登录,IOS或者Android
				// App有优先权将其从Web端退出，并登录IOS或者Android APP
				if (CommonConstants.ACCESS_FROM_WEIXIN.equals(user.getClientType())|| CommonConstants.ACCESS_FROM_WEBAPP.equals(user.getClientType())) {

					
					logger.info("用户已经在"+loginedClient+"登录，IOS或者Android App有优先权将其从"+loginedClient+"端退出，并登录IOS或者Android APP");

					// 获取session管理器，并将session从中清除
					HashMap<String, Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
					if (sessionManager != null) {
						
						// 销毁已经存在的session
						session.invalidate();
						
						// 从session管理器中移除原session
						sessionManager.remove(telNo);
					}
					logger.info("用户 [TelNo:" + telNo + "，Id=" + user.getId() + "] 已经退出Web App!");
					String[] values = { loginedClient, loginedClient };
					String msg = ComposeMessage.composeMessage(MessageContent.MSG_LOGIN_OTHER_CLIENT_FOR_WEB, values);
					resultMap.put(CommonConstants.REST_MSG_FORMAT_STATUS,CommonConstants.MSG_CODE_REST_LOGIN_OTHER_CLIENT);
					resultMap.put(CommonConstants.REST_MSG_FORMAT_CONTENT, msg);
				}

				// 用户已经在IOS App登录，此时需要在Android上登录
				if (CommonConstants.ACCESS_FROM_IOS.equals(user.getClientType())&& CommonConstants.ACCESS_FROM_ANDROID.equals(clientType)) {
					
					logger.info("用户已经在IOS App登录，此时需要在Android上登录");

					// 获取session管理器，并将session从中清除
					HashMap<String, Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
					if (sessionManager != null) {
						
						// 销毁已经存在的session
						session.invalidate();
						
						// 从session管理器中移除原session
						sessionManager.remove(user.getTelNo());
					}

					logger.info("用户 [TelNo:" + telNo + "，Id=" + user.getId()+ "] 已经退出IOS App!");


					String currentDateTime = DateUtils.formatChar12(DateUtils.getChar12());
					String[] values = { currentDateTime, "Andorid" };
					String msg = ComposeMessage.composeMessage(MessageContent.MSG_LOGIN_OTHER_CLIENT_FOR_APP, values);
					resultMap.put(CommonConstants.REST_MSG_FORMAT_STATUS,CommonConstants.MSG_CODE_REST_LOGIN_OTHER_CLIENT);
					resultMap.put(CommonConstants.REST_MSG_FORMAT_CONTENT, msg);
				}

				// 用户已经在Android App登录，此时需要在IOS上登录
				if (CommonConstants.ACCESS_FROM_ANDROID.equals(user.getClientType())&& CommonConstants.ACCESS_FROM_IOS.equals(clientType)) {
					
					logger.info("用户已经在Android App登录，此时需要在IOS上登录");

					// 获取session管理器，并将session从中清除
					HashMap<String, Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
					if (sessionManager != null) {
						
						// 销毁已经存在的session
						session.invalidate();
						
						//从session管理器中移除原session
						sessionManager.remove(user.getTelNo());
					}

					logger.info("用户 [TelNo:" + telNo + "，Id=" + user.getId()+ "] 已经退出Android App!");


					String currentDateTime = DateUtils.formatChar12(DateUtils.getChar12());
					String[] values = { currentDateTime, "Apple" };
					String msg = ComposeMessage.composeMessage(MessageContent.MSG_LOGIN_OTHER_CLIENT_FOR_APP, values);
					resultMap.put(CommonConstants.REST_MSG_FORMAT_STATUS,CommonConstants.MSG_CODE_REST_LOGIN_OTHER_CLIENT);
					resultMap.put(CommonConstants.REST_MSG_FORMAT_CONTENT, msg);
				}
			}
		}

		// 用户从WebAPP或者微信进入
		if (CommonConstants.ACCESS_FROM_WEIXIN.equals(clientType) || CommonConstants.ACCESS_FROM_WEBAPP.equals(clientType)) {

			logger.info("用户[" + telNo + "]的请求来自："+ (clientType.equals(CommonConstants.ACCESS_FROM_WEIXIN) ? "微信": "Web APP"));

			// 判断如果取到用户信息,则说明用户已经登录
			if (user != null) {
				if (CommonConstants.ACCESS_FROM_IOS.equals(user.getClientType())|| CommonConstants.ACCESS_FROM_ANDROID.equals(user.getClientType())) {

					logger.info("用户已经在"+loginedClient+"登录，则跳转到服务关闭页面，允许用户强制退出App");
					
					// 已经在IOS或者Android登录,需跳转到强制关闭服务页面将用户退出IOS或者Android APP
					String[] values = { loginedClient };
					String msg = ComposeMessage.composeMessage(MessageContent.MSG_LOGIN_APP_CLIENT_FOR_WEB, values);
					resultMap.put(CommonConstants.REST_MSG_FORMAT_STATUS,CommonConstants.MSG_CODE_REST_LOGIN_WEB_TO_APP);
					resultMap.put(CommonConstants.REST_MSG_FORMAT_CONTENT, msg);
				} 
			}
		}
		
		return resultMap;
	}
	

	/**
	 * 根据用户手机号码从session管理器中获取session
	 * @param request
	 * @return
	 */
	private static HttpSession getSession(String telNo){
		
		HttpSession session = null;
		
		if(telNo==null || "".equals(telNo)){
			return null;
		}
		//获取session管理器，并从中根据用户手机号获取对应的session对象
		HashMap<String,Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
		if(sessionManager!=null && sessionManager.containsKey(telNo)){
			session = (HttpSession)sessionManager.get(telNo);
		}
		
		return session;
	}
}
