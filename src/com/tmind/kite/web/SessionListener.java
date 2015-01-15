package com.tmind.kite.web;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.model.User;
import com.tmind.kite.utils.LoginHandler;

public class SessionListener implements HttpSessionListener {
	
	protected static final Logger logger = Logger.getLogger(SessionListener.class);

	/* Session失效事件 */
	public void sessionDestroyed(HttpSessionEvent event) {
		ServletContext ctx = event.getSession().getServletContext();
		User user = (User) ctx.getAttribute(CommonConstants.USER_LOGIN_TOKEN);
		String telNo = null;
		String id = null;
		if (user != null) {
			telNo = user.getTelNo();
			id = user.getId();

			logger.info("Session即将关闭，此session中用户信息为：[TelNo:"+telNo+"，Id="+id+"]");
			//更新用户表中的登录状态为0，表示未登录
			if(LoginHandler.logoutInDBLevel(0, new Integer(id))){
				//获取session管理器，并将session从中清除
				HashMap<String,Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
				if(sessionManager!=null){
					sessionManager.remove(user.getTelNo());
				}
				logger.info("用户 [TelNo:"+telNo+"，Id="+id+"] 已经退出应用");
			}
		} else {
			logger.info("Session即将关闭，但是此session中用户信息为空");
		}
	}

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub

	}
}
