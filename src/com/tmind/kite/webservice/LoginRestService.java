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
import com.tmind.kite.utils.SynchLoginStatus;
import com.tmind.kite.web.FrameworkApplication;

@Path("loginRest")
public class LoginRestService {
	
	protected static final Logger logger = Logger.getLogger(LoginRestService.class);
	
	@Context 
	private HttpServletRequest request;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("login/{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@PathParam(value="telno") String telno,@PathParam(value="password") String pwd,
						@PathParam(value="clientType") String clientType){
		
		String returnValue = "";
		
		//如果手机号码或者客户端类型为空，则跳转入异常提示页面
		if(telno==null||"".equals(telno)||clientType==null||"".equals(clientType)){
			logger.info("手机号码或者客户端类型为空");
			HashMap map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_KEY_STATUS, CommonConstants.MSG_CODE_REST_ACCESS_URL_NULL_PARAMS);
			map.put(CommonConstants.REST_MSG_KEY_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NULL_TELNO_CLIENTTYPE);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}

		logger.debug("[Login Web Service Request] : <User ID:"+telno+", Password:"+pwd+">");
		
		//根据手机号码和客户端类型检查用户是否已经在其他客户端登录,如果登录则根据情况同步登录状态
		HashMap loginStatus = SynchLoginStatus.synchLogin(telno, clientType);
		if(loginStatus!=null && !loginStatus.isEmpty()){
			String status = (String)loginStatus.get(CommonConstants.REST_MSG_KEY_STATUS);
//			String msg = (String)loginStatus.get(CommonConstants.REST_MSG_KEY_CONTENT);
			//向相应的客户端推送提示消息

			if(clientType.equals(CommonConstants.ACCESS_FROM_IOS)
					&&status.equals(CommonConstants.MSG_CODE_REST_SWITCH_ANDROID_TO_IOS)){
				//给Android客户端推送通知消息
				
			}else if(clientType.equals(CommonConstants.ACCESS_FROM_ANDROID)
					&&status.equals(CommonConstants.MSG_CODE_REST_SWITCH_IOS_TO_ANDROID)){
				//给IOS客户端推送通知消息
				
			}else if(clientType.equals(CommonConstants.ACCESS_FROM_WEIXIN)
					&&status.equals(CommonConstants.MSG_CODE_REST_SWITCH_WEB_TO_APP)){
				//给用户发通知短信
			}
		}
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

				//添加用户个人设置相关信息
				resultMap.put(CommonConstants.URGENT_USER_NAME, user.getUrgentUserName());
				resultMap.put(CommonConstants.URGENT_TEL_NO, user.getUrgentTelNo());
				resultMap.put(CommonConstants.SERVICE_PASSWORD, user.getServicePwd());
				resultMap.put(CommonConstants.SECURITY_QUESTION, user.getSecurityQue());
				resultMap.put(CommonConstants.SECURITY_ANSWER, user.getSecurityAns());
				
				//移出用户对象
				resultMap.remove(CommonConstants.LOGIN_USER_OBJECT);
				
			}
			Gson gson = new Gson();
			returnValue= gson.toJson(resultMap);
			
			logger.debug("[Login Web Service Response] : <"+returnValue+">");
		}
		return returnValue;
	}
}
