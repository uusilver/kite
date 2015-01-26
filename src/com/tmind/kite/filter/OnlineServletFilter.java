package com.tmind.kite.filter;

import java.io.IOException;
import java.io.PrintWriter;
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


public class OnlineServletFilter extends HttpServlet implements Filter {
	
	private static final long serialVersionUID = 1L;
	
	protected static final Logger logger = Logger.getLogger(OnlineServletFilter.class);

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String path = req.getServletPath();
		
		//从session中获取用户信息
		User user = (User)SessionUtils.getObjectAttribute(req, CommonConstants.USER_LOGIN_TOKEN);
		
		
		// 用户访问的入口，1：ISO APP；2：Android；3：微信；4：WebAPP
		String clientType = req.getParameter(CommonConstants.CLIENT_TYPE);
		
		if(clientType==null||"".equals(clientType)){
			
			//可以从session中获取clientType，如果获取不到也说明session不存在
			clientType = String.valueOf(SessionUtils.getObjectAttribute(req, CommonConstants.CLIENT_TYPE));
			
			//如果session中没有clientType，则跳转到错误提示页面，提供正确的微信访问方式和Web访问连接
			if(clientType==null||"".equals(clientType)||"null".equalsIgnoreCase(clientType)){
				logger.info("无法获取客户端类型，不允许继续操作");
				res.setHeader("Cache-Control", "no-store");
				res.setDateHeader("Expires", 0);
				res.setHeader("Pragma", "no-cache");

			    if(path.indexOf(".html")!=-1){
			    	RequestDispatcher dispatcher = req.getRequestDispatcher("index.htm");
					dispatcher.forward(req, res);
			    }
			    if(path.indexOf(".k")!=-1){
				    PrintWriter out = response.getWriter();
				    out.write("unknownClient");
			    }
				
//				if(user==null){
//					dispatcher = req.getRequestDispatcher("index.htm");
//					dispatcher.forward(req, res);
//				}else{
//					dispatcher = req.getRequestDispatcher("error.htm");
//					dispatcher.forward(req, res);
//				}
				
				return;
				
			}else{

				logger.info("**********首次访问中可以获取客户端类型："+clientType+"****************");
				
				// 判断如果没有取到用户信息,则说明用户没有登录，就跳转到登陆页面
				if (user == null) {

					if(path.equalsIgnoreCase("/login.html")||path.equalsIgnoreCase("/regist.html")
							||path.equalsIgnoreCase("/pre-reset-pwd.html")||path.equalsIgnoreCase("/404.html")
							||path.equalsIgnoreCase("/gen-pwd.html")||path.equalsIgnoreCase("/ValidationServlet.k")
							||path.equalsIgnoreCase("/LoginServlet.k")||path.equalsIgnoreCase("/Regist.k")){
						// 已经登陆,继续此次请求
						chain.doFilter(request, response);
						logger.info("不需要登陆，可操作");
						return;
					}

					logger.info("用户没有登陆，不允许操作");

					res.setHeader("Cache-Control", "no-store");
					res.setDateHeader("Expires", 0);
					res.setHeader("Pragma", "no-cache");
					
					// 跳转到登陆页面
					RequestDispatcher dispatcher = request.getRequestDispatcher("index.htm");
					dispatcher.forward(request, response);
					return;
				}
			}
		}else{
			logger.info("**********客户端类型："+clientType+"****************");
			SessionUtils.setObjectAttribute(req, CommonConstants.CLIENT_TYPE, clientType);
		}
		

		//用户手机号码
		String telNo = req.getParameter(CommonConstants.TEL_NUMBER);
		
		if((telNo==null||"".equals(telNo)) && user!=null){
			telNo = user.getTelNo();
		}

		logger.info("**********用户手机号码："+telNo+"****************");
		
		//用户从WebAPP或者微信进入
		if(CommonConstants.ACCESS_FROM_WEIXIN.equals(clientType)||CommonConstants.ACCESS_FROM_WEBAPP.equals(clientType)){

			String loginedFrom = clientType.equals(CommonConstants.ACCESS_FROM_WEIXIN)?"微信":"Web APP";
			
			logger.info("请求来自："+loginedFrom+",用户["+telNo+"]请求的路径："+path);

			if(path.equalsIgnoreCase("/login.html")||path.equalsIgnoreCase("/regist.html")
					||path.equalsIgnoreCase("/pre-reset-pwd.html")||path.equalsIgnoreCase("/404.html")
					||path.equalsIgnoreCase("/gen-pwd.html")||path.equalsIgnoreCase("/ValidationServlet.k")
					||path.equalsIgnoreCase("/LoginServlet.k")||path.equalsIgnoreCase("/Regist.k")){
				// 已经登陆,继续此次请求
				chain.doFilter(request, response);
				logger.info("不需要登陆，可操作");
				return;
			}
			
			// 判断如果没有取到用户信息,则说明用户没有登录，就跳转到登陆页面
			if (user == null) {
				// 跳转到登陆页面
				RequestDispatcher dispatcher = request.getRequestDispatcher("login.html?clientType="+clientType);
				dispatcher.forward(request, response);
				logger.info("用户没有登陆，不允许操作");

				res.setHeader("Cache-Control", "no-store");
				res.setDateHeader("Expires", 0);
				res.setHeader("Pragma", "no-cache");
				return;
			} else{
				logger.info("用户 [TelNo:"+telNo+"，Id="+user.getId()+"] 已经登录"+loginedFrom+"!");
				// 已经登陆,继续此次请求
				chain.doFilter(request, response);
				logger.info("用户已经登陆，允许操作");
				return;
			}
		}
	}

	public void destroy() {

	}
}
