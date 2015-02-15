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

@Path("registRest")
public class RegistRestService {
	
	protected static final Logger logger = Logger.getLogger(RegistRestService.class);
	
//	@Context 
//	private HttpServletRequest request;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("regist/{userName}/{telno}/{password}/{clientType}")
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
			map.put(CommonConstants.REST_MSG_KEY_STATUS, CommonConstants.MSG_CODE_REST_LOGIN_NULL_CLIENT_TYPE);
			map.put(CommonConstants.REST_MSG_KEY_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NULL_TELNO_CLIENTTYPE);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}

		logger.debug("[Regist Web Service Request] : <User ID:"+telno+", Password:"+pwd+" , ClientType:"+clientType+">");
		
		//用户注册
		HashMap resultMap = RegistHandler.saveUserTelAndPwd(userName, telno, DigestHandler.makeMD5(pwd),clientType);

		Gson gson = new Gson();
		returnValue= gson.toJson(resultMap);
		
		return returnValue;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("servicePwdAndSecurityQASetting/{telno}/{servicePwd}/{securityQue}/{securityAns}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String saveServicePwdAndSecurityQA(@PathParam(value="telno") String telno,@PathParam(value="servicePwd") String servicePwd,
			@PathParam(value="securityQue") String securityQue,@PathParam(value="securityAns") String securityAns,
			@PathParam(value="clientType") String clientType){

		String returnValue = "";
		HashMap map = null;
		//如果手机号码或者客户端类型为空，则跳转入异常提示页面
		if(telno==null||"".equals(telno)||servicePwd==null||"".equals(servicePwd)
				||clientType==null||"".equals(clientType)){
			logger.info("手机号码、服务密码或者客户端类型为空");
			map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_KEY_STATUS, CommonConstants.MSG_CODE_REST_LOGIN_NULL_CLIENT_TYPE);
			map.put(CommonConstants.REST_MSG_KEY_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NULL_TELNO_CLIENTTYPE);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}

		logger.debug("[Regist Web Service Request] : <User ID:"+telno+", Password:"+servicePwd+" , ClientType:"+clientType+">");
		
		//设置服务密码和安全问题与答案
		HashMap resultMap = RegistHandler.saveServicePwdAndQA(telno, DigestHandler.makeMD5(servicePwd), securityQue, securityAns);

		Gson gson = new Gson();
		returnValue= gson.toJson(resultMap);
		
		return returnValue;
	}
}
