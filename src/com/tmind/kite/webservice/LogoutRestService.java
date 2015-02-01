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
import com.tmind.kite.constants.MessageContent;
import com.tmind.kite.model.User;
import com.tmind.kite.utils.LoginHandler;
import com.tmind.kite.utils.SessionUtils;
import com.tmind.kite.web.FrameworkApplication;

@Path("logout")
public class LogoutRestService {
	
	protected static final Logger logger = Logger.getLogger(LogoutRestService.class);

	@Context 
	private HttpServletRequest request;
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("logout/{userName}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String logout(@PathParam(value="userName") String telno,@PathParam(value="clientType") String clientType){
		
		String returnValue = "";

		HashMap map = new HashMap<String,String>();
		//如果手机号码或者客户端类型为空，则跳转入异常提示页面
		if(telno==null||"".equals(telno)||clientType==null||"".equals(clientType)){
			logger.info("手机号码或者客户端类型为空");
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_LOGIN_NULL_CLIENT_TYPE);
			map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NULL_TELNO_CLIENTTYPE);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}
		
		User user = (User)SessionUtils.getObjectAttribute(request, CommonConstants.USER_LOGIN_TOKEN);
		String telNo = null;
		String userId = null;
		if(user!=null){
			telNo = user.getTelNo();
			if(telno.equals(telNo)){

				userId = user.getId();
				//获取session管理器，并将session从中清除
				HashMap<String,Object> sessionManager = FrameworkApplication.getInstance().getSessionManager();
				if(sessionManager!=null && sessionManager.containsKey(telNo)){
					sessionManager.remove(telNo);
				}
				
				//更新用户登录状态为0，即未登录
				LoginHandler.logoutInDBLevel(0, new Integer(userId));
				
				//清除session
				SessionUtils.removeObjectAttribute(request, CommonConstants.USER_LOGIN_TOKEN);
				
				logger.info("用户"+telNo+"登出");
				
				map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_LOGOUT_SUCCESS);
				map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, "");
				Gson gson = new Gson();
				returnValue= gson.toJson(map);
				
			}else{

				map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_LOGOUT_TELNO_INCORRECT);
				map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_USER_TELNO_INCORRECT);
				Gson gson = new Gson();
				returnValue= gson.toJson(map);
			}
		}else{
			logger.info("用户未登录，无需退出");

			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_NO_LOGIN_ACCESS_DENIED);
			map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NO_LOGIN);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
		}
		
		return returnValue;
	}

}
