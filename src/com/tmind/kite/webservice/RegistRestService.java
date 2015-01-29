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
import com.tmind.kite.utils.DigestHandler;
import com.tmind.kite.utils.RegistHandler;
import com.tmind.kite.utils.SessionUtils;
import com.tmind.kite.web.FrameworkApplication;

@Path("registRest")
public class RegistRestService {
	
	protected static final Logger logger = Logger.getLogger(RegistRestService.class);
	
	@Context 
	private HttpServletRequest request;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("regist/{userName}/telno/{password}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String regist(@PathParam(value="userName") String userName,@PathParam(value="telno") String telno,
						@PathParam(value="password") String pwd,@PathParam(value="clientType") String clientType){
		
		String returnValue = "";
		HashMap map = null;
		//如果手机号码或者客户端类型为空，则跳转入异常提示页面
		if(telno==null||"".equals(telno)||pwd==null||"".equals(pwd)
				||clientType==null||"".equals(clientType)){
			logger.info("手机号码、登录密码或者客户端类型为空");
			map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_LOGIN_NULL_CLIENT_TYPE);
			map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NULL_TELNO_CLIENTTYPE);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}

		logger.debug("[Regist Web Service Request] : <User ID:"+telno+", Password:"+pwd+" , ClientType:"+clientType+">");
		
		//用户注册
		HashMap resultMap = RegistHandler.saveUserTelAndPwd(userName, telno, DigestHandler.makeMD5(pwd));
		
		if(resultMap!=null&&!resultMap.isEmpty()){
			map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_REGIST_SUCCESS);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
		}else{
			map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_LOGIN_SUCCESS);
			map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NULL_TELNO_CLIENTTYPE);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
		}
		return returnValue;
	}
}