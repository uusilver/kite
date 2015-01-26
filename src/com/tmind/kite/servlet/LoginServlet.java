package com.tmind.kite.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.model.User;
import com.tmind.kite.utils.LoginHandler;
import com.tmind.kite.utils.SessionUtils;
import com.tmind.kite.utils.SynchLoginStatus;
import com.tmind.kite.web.FrameworkApplication;

public class LoginServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8117908425309580896L;

	protected static final Logger logger = Logger.getLogger(LoginServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {

		String telNo = request.getParameter(CommonConstants.TEL_NUMBER);
		String password = request.getParameter(CommonConstants.USER_PASSWORD);
		
		//从session中获取用户访问入口代码
		String clientType = String.valueOf(SessionUtils.getObjectAttribute(request, CommonConstants.CLIENT_TYPE));

		logger.info("用户手机号码：" + telNo + ",密码：" + password + ",登录入口："+ clientType);
		
		//如果手机号码或者客户端类型为空，则跳转入异常提示页面
		if(telNo==null||"".equals(telNo)||clientType==null||"".equals(clientType)){
			try {
				response.sendRedirect("access_denied.html");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//根据手机号码和客户端类型检查用户是否已经在IOS或者Android客户端登录,如果登录则终止此次登录，并跳转到登录页面
		HashMap loginStatus = SynchLoginStatus.synchLogin(telNo, clientType);
		if(loginStatus!=null && !loginStatus.isEmpty()){
			String resultCode = (String)loginStatus.get(CommonConstants.REST_MSG_FORMAT_STATUS);
			if(resultCode!=null && !"".equals(resultCode) 
					&& CommonConstants.MSG_CODE_REST_LOGIN_WEB_TO_APP.equals(resultCode)){
				try {
					Gson gson = new Gson();
					String returnValue= gson.toJson(loginStatus);
//					response.setContentType("text/html;charset=UTF-8");
					response.getOutputStream().write(returnValue.getBytes());
//					response.sendRedirect("login.html?clientType="+clientType);
					return;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		try {
			password = new String(Base64.encodeBase64(password.getBytes()),CommonConstants.CHARSETNAME_UTF_8);
			
			HashMap resultMap = LoginHandler.login(telNo, password, clientType);
			String status = (String) resultMap.get(CommonConstants.REST_MSG_FORMAT_STATUS);
			if ("success".equals(status)) {
				User user = (User) resultMap.get(CommonConstants.LOGIN_USER_OBJECT);
				
				user.setClientType(clientType);
				user.setLoginFlag("1");
				
				// 登陆后写入session
				SessionUtils.setObjectAttribute(request,CommonConstants.USER_LOGIN_TOKEN,user);
				
				//获取session管理器，并将session缓存进此管理器
				HashMap<String,Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
				if(sessionManager!=null){
					sessionManager.put(user.getTelNo(), request.getSession());
				}
				response.sendRedirect("main.html");
			} else {
				response.sendRedirect("login.html?clientType="+clientType);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
