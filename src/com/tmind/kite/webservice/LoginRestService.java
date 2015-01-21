package com.tmind.kite.webservice;

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
import com.tmind.kite.web.FrameworkApplication;

@Path("loginRest")
public class LoginRestService {
	
	protected static final Logger logger = Logger.getLogger(LoginRestService.class);
	
	@Context 
	private HttpServletRequest request;
	
	@GET
	@Path("login/{userName}/{password}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@PathParam(value="userName") String telno,@PathParam(value="password") String pwd,
						@PathParam(value="clientType") String clientType){
		
		String returnValue = "";

		logger.debug("[Login Web Service Request] : <User ID:"+telno+", Password:"+pwd+">");
		
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
