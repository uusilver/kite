package com.tmind.kite.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.constants.MessageContent;
import com.tmind.kite.model.User;
import com.tmind.kite.utils.ComposeMessage;
import com.tmind.kite.utils.DateUtils;
import com.tmind.kite.utils.SessionUtils;
import com.tmind.kite.web.FrameworkApplication;


public class OnlineFilter extends HttpServlet implements Filter {
	
	private static final long serialVersionUID = 1L;
	
	protected static final Logger logger = Logger.getLogger(OnlineFilter.class);

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		
		//用户访问的入口，1：ISO APP；2：Android；3：微信；4：WebAPP
		String clientType = req.getParameter(CommonConstants.CLIENT_TYPE);
		String telNo = req.getParameter(CommonConstants.TEL_NUMBER);
		
		if(clientType==null||"".equals(clientType)){
			clientType = String.valueOf(SessionUtils.getObjectAttribute(req, CommonConstants.CLIENT_TYPE));
			if(clientType==null||"".equals(clientType)){
				Gson gson = new Gson();
				Map errCode = new LinkedHashMap();
				errCode.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_ACCESS_DENIED);
				errCode.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED);
				String returnValue= gson.toJson(errCode);
				res.getOutputStream().write(returnValue.getBytes());
				return;
			}
		}else{
			SessionUtils.setObjectAttribute(req, CommonConstants.CLIENT_TYPE, clientType);
		}
		
		//获取session中的用户信息
		User user = getUserFromSession(req);
		
		if((telNo==null||"".equals(telNo)) && user!=null){
			telNo = user.getTelNo();
		}

		logger.info("**********************"+telNo+"****************");
		
		String path = req.getServletPath();
		
		//用户从IOS或者Android APP进入
		if(CommonConstants.ACCESS_FROM_IOS.equals(clientType)||CommonConstants.ACCESS_FROM_ANDROID.equals(clientType)){
			logger.info("请求来自："+(clientType.equals(CommonConstants.ACCESS_FROM_IOS)?"IOS APP":"Android APP")+",用户["+telNo+"]请求的路径："+path);	
			
			// 如果没有取到用户信息,则说明用户没有登录，就跳转到登陆页面
			if (user != null) {
				
				// 用户已经在微信或者Web App登录,IOS或者Android App有优先权将其从Web端退出，并登录IOS或者Android APP
				if(CommonConstants.ACCESS_FROM_WEIXIN.equals(user.getClientType())||CommonConstants.ACCESS_FROM_WEBAPP.equals(user.getClientType())){
					
					logger.info("用户已经在微信或者Web App登录，IOS或者Android App有优先权将其从Web端退出，并登录IOS或者Android APP");
					
					//获取session管理器，并将session从中清除
					HashMap<String,Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
					if(sessionManager!=null){
						sessionManager.remove(user.getTelNo());
					}
					//主动销毁当前session
					getSession(req).invalidate();
					logger.info("用户 [TelNo:"+telNo+"，Id="+user.getId()+"] 已经退出Web App!");
					chain.doFilter(request, response);
					return;
				}
				
				//用户已经在IOS App登录，此时需要在Android上登录
				if(CommonConstants.ACCESS_FROM_IOS.equals(user.getClientType()) && CommonConstants.ACCESS_FROM_ANDROID.equals(clientType)){
					logger.info("用户已经在IOS App登录，此时需要在Android上登录");
					
					//获取session管理器，并将session从中清除
					HashMap<String,Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
					if(sessionManager!=null){
						sessionManager.remove(user.getTelNo());
					}
					//主动销毁当前session
					getSession(req).invalidate();
					logger.info("用户 [TelNo:"+telNo+"，Id="+user.getId()+"] 已经退出Web App!");
					
					HashMap resultMap = new HashMap();

					String currentDateTime = DateUtils.formatChar12(DateUtils.getChar12());
					String[] values = {currentDateTime,"Andorid"};
					String msg = ComposeMessage.composeMessage(MessageContent.MSG_LOGIN_OTHER_CLIENT,values);
					resultMap.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_LOGIN_OTHER_CLIENT);
					resultMap.put(CommonConstants.REST_MSG_FORMAT_CONTENT, msg);
					Gson gson = new Gson();
					String returnValue= gson.toJson(resultMap);
					res.getOutputStream().write(returnValue.getBytes());
					chain.doFilter(request, response);
					return;
				}

				//用户已经在Android App登录，此时需要在IOS上登录
				if(CommonConstants.ACCESS_FROM_ANDROID.equals(user.getClientType()) && CommonConstants.ACCESS_FROM_IOS.equals(clientType)){
					logger.info("用户已经在Android App登录，此时需要在IOS上登录");

					
					//获取session管理器，并将session从中清除
					HashMap<String,Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
					if(sessionManager!=null){
						sessionManager.remove(user.getTelNo());
					}
					//主动销毁当前session
					getSession(req).invalidate();
					logger.info("用户 [TelNo:"+telNo+"，Id="+user.getId()+"] 已经退出Web App!");
					
					HashMap resultMap = new HashMap();

					String currentDateTime = DateUtils.formatChar12(DateUtils.getChar12());
					String[] values = {currentDateTime,"Apple"};
					String msg = ComposeMessage.composeMessage(MessageContent.MSG_LOGIN_OTHER_CLIENT,values);
					resultMap.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_LOGIN_OTHER_CLIENT);
					resultMap.put(CommonConstants.REST_MSG_FORMAT_CONTENT, msg);
					Gson gson = new Gson();
					String returnValue= gson.toJson(resultMap);
					res.getOutputStream().write(returnValue.getBytes());
					return;
				}
				
			}
		}
		
		//用户从WebAPP或者微信进入
		if(CommonConstants.ACCESS_FROM_WEIXIN.equals(clientType)||CommonConstants.ACCESS_FROM_WEBAPP.equals(clientType)){

			logger.info("请求来自："+(clientType.equals(CommonConstants.ACCESS_FROM_WEIXIN)?"微信":"Web APP")+",用户["+telNo+"]请求的路径："+path);

			// 这里设置如果没有登陆将要转发到的页面
			RequestDispatcher dispatcher = null;
			
			if(path.equalsIgnoreCase("/login.html")||path.equalsIgnoreCase("/regist.html")
					||path.equalsIgnoreCase("/pre-reset-pwd.html")||path.equalsIgnoreCase("/404.html")
					||path.equalsIgnoreCase("/gen-pwd.html")){
				// 已经登陆,继续此次请求
				chain.doFilter(request, response);
				logger.info("不需要登陆，可操作");
				return;
			}
			
			// 判断如果没有取到用户信息,则说明用户没有登录，就跳转到登陆页面
			if (user == null) {
				// 跳转到登陆页面
				dispatcher = request.getRequestDispatcher("login.html");
				dispatcher.forward(request, response);
				logger.info("用户没有登陆，不允许操作");

				res.setHeader("Cache-Control", "no-store");
				res.setDateHeader("Expires", 0);
				res.setHeader("Pragma", "no-cache");
				return;
			} else if(CommonConstants.ACCESS_FROM_IOS.equals(user.getClientType())||CommonConstants.ACCESS_FROM_ANDROID.equals(user.getClientType())){
				
				// 已经在IOS或者Android登录,需跳转到强制关闭服务页面将用户退出IOS或者Android APP
				dispatcher = request.getRequestDispatcher("shutdownApp.html");
				dispatcher.forward(request, response);
				logger.info("用户已经在IOS或者Android登录，则跳转到服务关闭页面，允许用户强制退出App。");
				return;
			}else{
				// 已经登陆,继续此次请求
				chain.doFilter(request, response);
				logger.info("用户已经登陆，允许操作");
				return;
			}
		}
	}

	public void destroy() {

	}
	
	/**
	 * 根据用户手机号码获取session，并从中取出用户信息
	 * @param request
	 * @return
	 */
	private User getUserFromSession(HttpServletRequest request){
		
		HttpSession session = null;
		
		//1.从session中获取用户信息
		User user = (User)SessionUtils.getObjectAttribute(request, CommonConstants.USER_LOGIN_TOKEN);
		
		//2.如果用户信息不存在，则尝试从session管理器中获取用户信息
		if(user==null){

			String telNo = request.getParameter(CommonConstants.TEL_NUMBER);
			if(telNo==null || "".equals(telNo)){
				return null;
			}
			
			//获取session管理器，并从中根据用户手机号获取对应的session对象
			HashMap<String,Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
			if(sessionManager!=null && sessionManager.containsKey(telNo)){
				session = (HttpSession)sessionManager.get(telNo);
				if(session!=null){
					user = (User)session.getAttribute(CommonConstants.USER_LOGIN_TOKEN);
				}
			}else{
				logger.info("用户["+telNo+"]没有登录，请先登录");
			}
		}
		return user;
	}
	
	/**
	 * 根据用户手机号码获取session
	 * @param request
	 * @return
	 */
	private HttpSession getSession(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		
		//如果session不存在，则尝试从session管理器中获取
		if(session==null){
			
			String telNo = request.getParameter(CommonConstants.TEL_NUMBER);
			if(telNo==null || "".equals(telNo)){
				return null;
			}
			//获取session管理器，并从中根据用户手机号获取对应的session对象
			HashMap<String,Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
			if(sessionManager!=null && sessionManager.containsKey(telNo)){
				session = (HttpSession)sessionManager.get(telNo);
			}
		}
		return session;
	}
}
