package com.tmind.kite.webservice;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.constants.MessageContent;
import com.tmind.kite.model.User;
import com.tmind.kite.utils.ServiceOperationHandler;
import com.tmind.kite.utils.SessionUtils;

@Path("serviceControl")
public class StartOrStopRestService {
	
	protected static final Logger logger = Logger.getLogger(StartOrStopRestService.class);


	@Context 
	private HttpServletRequest request;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("startService/{telno}/{serviceType}/{freq}/{optType}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String startService(@PathParam(value="telno") String telno,@PathParam(value="serviceType") String serviceType,
			@PathParam(value="freq") String freq, @PathParam(value="optType") String optType, 
			@PathParam(value="clientType") String clientType){
		
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
		
		
		if(serviceType==null||"".equals(serviceType)||freq==null||"".equals(freq)||optType==null||"".equals(optType)){
			logger.info("服务类型、间隔时间或者服务开启/关闭指令为空");
			map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_EMPTY_OPEN_OR_CLOSE_SERVICE_COMMAND);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}
		
		//开启服务
		String resultFlag = ServiceOperationHandler.startService(telno, serviceType, freq, clientType);

		map = new HashMap<String,String>();
		if(resultFlag!=null && "OK".equals(resultFlag)){
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_START_SERVICE_SUCCESS);
		}else{
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_START_SERVICE_FAILED);
		}

		Gson gson = new Gson();
		returnValue= gson.toJson(map);
		return returnValue;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("stopService/{telno}/{servicePwd}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String startService(@PathParam(value="telno") String telno,@PathParam(value="servicePwd") String servicePwd,
			@PathParam(value="clientType") String clientType){
		
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
		logger.debug("[Stop Service Web Service Request] : <User ID:"+telno+", ClientType:"+clientType+">");
		
		
		if(servicePwd==null||"".equals(servicePwd)){
			logger.info("服务密码为空");
			map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_EMPTY_SERVICE_PWD);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}
		
		//验证服务密码是否正确
		String pwd = null;
		try {
			//Base64解密
			pwd = new String(Base64.decodeBase64(servicePwd.getBytes()),CommonConstants.CHARSETNAME_UTF_8);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			logger.info("用户密码解密失败，异常信息如下：");
			logger.info(e1.getMessage());
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_LOGIN_DECODE_PWD_ERR);
			map.put(CommonConstants.REST_MSG_FORMAT_TIMES, "0");
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}

		
		//从session中获取用户信息
		User user = (User)SessionUtils.getObjectAttribute(request, CommonConstants.USER_LOGIN_TOKEN);
		
		if(user.getServicePwd().equals(pwd)){
			//关闭服务
			String resultFlag = ServiceOperationHandler.stopService(telno);

			map = new HashMap<String,String>();
			if(resultFlag!=null && "OK".equals(resultFlag)){
				map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_STOP_SERVICE_SUCCESS);
			}else{
				map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_STOP_SERVICE_FAILED);
			}
		}else{
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_VALID_SERVICE_PWD_FAILED);
		}
		


		Gson gson = new Gson();
		returnValue= gson.toJson(map);
		return returnValue;
	}
}
