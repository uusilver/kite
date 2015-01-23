package com.tmind.kite.webservice;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.model.User;
import com.tmind.kite.utils.LoginHandler;
import com.tmind.kite.utils.SessionUtils;
import com.tmind.kite.utils.SynchLoginStatus;
import com.tmind.kite.web.FrameworkApplication;

@Path("loginRest")
public class LoginRestService {
	
	protected static final Logger logger = Logger.getLogger(LoginRestService.class);
	
	@Context 
	private HttpServletRequest request;
	
	@SuppressWarnings("rawtypes")
	@GET
	@Path("login/{userName}/{password}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@PathParam(value="userName") String telno,@PathParam(value="password") String pwd,
						@PathParam(value="clientType") String clientType){
		
		String returnValue = "";
		
		//如果手机号码或者客户端类型为空，则跳转入异常提示页面
		if(telno==null||"".equals(telno)||clientType==null||"".equals(clientType)){
			logger.info("密码为空");
			HashMap map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_LOGIN_NULL_CLIENT_TYPE);
			map.put(CommonConstants.REST_MSG_FORMAT_TIMES, "0");
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}

		logger.debug("[Login Web Service Request] : <User ID:"+telno+", Password:"+pwd+">");
		
		//根据手机号码和客户端类型检查用户是否已经在其他客户端登录,如果登录则根据情况同步登录状态
		HashMap loginStatus = SynchLoginStatus.synchLogin(telno, clientType);
		
		//用户登录
		HashMap resultMap = LoginHandler.login(telno, pwd,clientType);
		
		if(!resultMap.isEmpty()){
			if(resultMap.get(CommonConstants.LOGIN_USER_OBJECT)!=null){
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
				
				resultMap.remove(CommonConstants.LOGIN_USER_OBJECT);
			}
			Gson gson = new Gson();
			returnValue= gson.toJson(resultMap);
			
			logger.debug("[Login Web Service Response] : <"+returnValue+">");
		}
		return returnValue;
	}
}
