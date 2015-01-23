package com.tmind.kite.filter;

import java.io.IOException;
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

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.constants.MessageContent;
import com.tmind.kite.model.User;
import com.tmind.kite.utils.SessionUtils;

public class OnlineRestFilter extends HttpServlet implements Filter  {
	
	private static final long serialVersionUID = 1L;
	
	protected static final Logger logger = Logger.getLogger(OnlineRestFilter.class);

	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		
		String path = req.getServletPath();
		
		String requestURI = req.getRequestURI();
		
		String clientType = null;
		
		String telNo = null;
		
		//如果访问URI中包含/rest/字样，则说明请求webservice服务
		if(requestURI!=null&&!"".equals(requestURI)){
			//如果访问URI中包含/loginRest/login/字样，则说明请求login webservice服务
			int indexNo = requestURI.indexOf("/loginRest/login/");
			if(indexNo!=-1){
				String[] params = requestURI.substring(indexNo+"/loginRest/login/".length()).split("/");
				if(params!=null&&params.length==3){
					telNo = params[0];
					clientType = params[2];
				}
			}else{
				clientType = requestURI.substring(requestURI.length()-2);
				if(clientType.startsWith("/")){
					clientType = clientType.substring(1);
				}
			}
		}
		
		//如果请求中没有clientType值一律视为非法请求
		if(clientType==null||"".equals(clientType)){
			Gson gson = new Gson();
			Map errCode = new LinkedHashMap();
			errCode.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_UNKNOWN_CLIENT_ACCESS_DENIED);
			errCode.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_UNKNOWN_CLIENT);
			String returnValue= gson.toJson(errCode);
//			res.setContentType("text/html;charset=UTF-8");
			res.getOutputStream().write(returnValue.getBytes());
			return;
		}
		
		//从session中获取用户信息
		User user = (User)SessionUtils.getObjectAttribute(req, CommonConstants.USER_LOGIN_TOKEN);
		
		if((telNo==null||"".equals(telNo)) && user!=null){
			telNo = user.getTelNo();
		}

		logger.info("**********************"+telNo+"****************");
		
		//用户从IOS或者Android APP进入
		if(CommonConstants.ACCESS_FROM_IOS.equals(clientType)||CommonConstants.ACCESS_FROM_ANDROID.equals(clientType)){
			
			String loginedFrom = clientType.equals(CommonConstants.ACCESS_FROM_IOS)?"IOS APP":"Android APP";
			
			logger.info("请求来自："+loginedFrom+",用户["+telNo+"]请求的路径："+path);	
			
			// 如果取到用户信息,则说明用户已经登录，跳转到目标页面
			if (user != null) {
				logger.info("用户 [TelNo:"+telNo+"，Id="+user.getId()+"] 已经登录"+loginedFrom+"!");
				chain.doFilter(request, response);
				return;
			}else {
				if(requestURI.indexOf("/loginRest/login/")!=-1||requestURI.indexOf("/registRest/regist/")!=-1){
					// 访问这些数据不需要登录,继续此次请求
					chain.doFilter(request, response);
					logger.info("不需要登陆，可操作");
					return;
				}else{
					Gson gson = new Gson();
					Map errCode = new LinkedHashMap();
					errCode.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_NO_LOGIN_ACCESS_DENIED);
					errCode.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NO_LOGIN);
					String returnValue= gson.toJson(errCode);
//					res.setContentType("text/html;charset=UTF-8");
					res.getOutputStream().write(returnValue.getBytes());
					return;
				}
			}
		}
	}

	public void destroy() {

	}
}
