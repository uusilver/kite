package com.tmind.kite.servlet;


import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.model.User;
import com.tmind.kite.utils.LoginHandler;
import com.tmind.kite.utils.SessionUtils;
import com.tmind.kite.web.FrameworkApplication;

public class LogoutServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2038345694885720544L;
	
	protected static final Logger logger = Logger.getLogger(LogoutServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response){

		User user = (User)SessionUtils.getObjectAttribute(request, CommonConstants.USER_LOGIN_TOKEN);
		String telNo = null;
		String userId = null;
		if(user!=null){
			//获取session管理器，并将session从中清除
			HashMap<String,Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
			telNo = user.getTelNo();
			userId = user.getId();
			if(sessionManager!=null && sessionManager.containsKey(telNo)){
				sessionManager.remove(telNo);
			}
			
			//更新用户登录状态为0，即未登录
			LoginHandler.logoutInDBLevel(0, new Integer(userId));
			
			//清除session
			SessionUtils.removeObjectAttribute(request, CommonConstants.USER_LOGIN_TOKEN);
			
			logger.info("用户"+telNo+"登出");
		}else{
			logger.info("用户未登录，无需退出");
		}
	}
}
