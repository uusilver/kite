package com.tmind.kite.webservice;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.constants.MessageContent;
import com.tmind.kite.utils.DigestHandler;
import com.tmind.kite.utils.RegistHandler;
import com.tmind.kite.utils.UserProfileHandler;

@Path("userProfile")
public class UserProfileRestService {
	
	protected static final Logger logger = Logger.getLogger(UserProfileRestService.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("getProfile/{telno}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserProfile(@PathParam(value="telno") String telno,@PathParam(value="clientType") String clientType){
		
		String returnValue = "";
		HashMap map = null;
		//如果手机号码或者客户端类型为空，则跳转入异常提示页面
		if(telno==null||"".equals(telno)||clientType==null||"".equals(clientType)){
			logger.info("手机号码或者客户端类型为空");
			map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_LOGIN_NULL_CLIENT_TYPE);
			map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NULL_TELNO_CLIENTTYPE);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}

		logger.debug("[Regist Web Service Request] : <User ID:"+telno+", ClientType:"+clientType+">");
		
		//获取用户个人设置信息
		HashMap resultMap = UserProfileHandler.getUserProfile(telno);

		Gson gson = new Gson();
		returnValue= gson.toJson(resultMap);
		
		return returnValue;
	}
}
