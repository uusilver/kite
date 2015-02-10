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
import com.tmind.kite.utils.UserProfileHandler;

@Path("userProfile")
public class UserProfileRestService {
	
	protected static final Logger logger = Logger.getLogger(UserProfileRestService.class);
	
	/**
	 * 根据用户手机号码获取用户个人这是信息
	 * @param telno
	 * @param clientType
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("getUrgentUserInfo/{telno}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUrgentUserInfo(@PathParam(value="telno") String telno,@PathParam(value="clientType") String clientType){
		
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
		HashMap resultMap = UserProfileHandler.getUrgentUserInfo(telno);

		Gson gson = new Gson();
		returnValue= gson.toJson(resultMap);
		
		return returnValue;
	}
	
	/**
	 * 保存用户个人设置信息
	 * @param telno
	 * @param urgentName
	 * @param urgentTelNo
	 * @param servicePwd
	 * @param securityQue
	 * @param securityAns
	 * @param clientType
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("saveUserProfile/{telno}/{urgentName}/{urgentTelNo}/{servicePwd}/{securityQue}/{securityAns}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String saveUserProfile(@PathParam(value="telno") String telno,@PathParam(value="urgentName") String urgentName,
			@PathParam(value="urgentTelNo") String urgentTelNo,@PathParam(value="servicePwd") String servicePwd,
			@PathParam(value="securityQue") String securityQue,@PathParam(value="securityAns") String securityAns,
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
		
		if(urgentName==null||"".equals(urgentName)||urgentTelNo==null||"".equals(urgentTelNo)
				||servicePwd==null||"".equals(servicePwd)
				||securityQue==null||"".equals(securityQue)
				||securityAns==null||"".equals(securityAns)){
			logger.info("紧急联系人、紧急联系人手机号码、服务密码、或者安全问题为空");
			map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_GET_EMPTY_PROFILE);
			map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NULL_USER_PROFILE);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}

		logger.debug("[Regist Web Service Request] : <User ID:"+telno+", ClientType:"+clientType+">");
		
		//保存用户个人设置信息
		HashMap resultMap = UserProfileHandler.saveUserProfile(telno, urgentName, urgentTelNo, servicePwd, securityQue, securityAns, clientType);

		Gson gson = new Gson();
		returnValue= gson.toJson(resultMap);
		
		return returnValue;
	}
	
	/**
	 * 保存紧急联系人手机号码和姓名
	 * @param telno
	 * @param urgentName
	 * @param urgentTelNo
	 * @param clientType
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("saveUrgentUserInfo/{telno}/{urgentName}/{urgentTelNo}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String saveUrgentUserInfo(@PathParam(value="telno") String telno,@PathParam(value="urgentName") String urgentName,
			@PathParam(value="urgentTelNo") String urgentTelNo,@PathParam(value="clientType") String clientType){
		
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
		
		if(urgentName==null||"".equals(urgentName)||urgentTelNo==null||"".equals(urgentTelNo)){
			logger.info("紧急联系人或者紧急联系人手机号码为空");
			map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_SAVE_PROFILE_FAILED);
			map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NULL_URGENT_USER_PROFILE);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}

		logger.debug("[Regist Web Service Request] : <User ID:"+telno+", ClientType:"+clientType+">");
		
		//保存紧急联系人姓名和手机号码
		HashMap resultMap = UserProfileHandler.saveUserProfile(telno, urgentName, urgentTelNo, null, null, null, null);

		HashMap returnMap = new HashMap();
		
		if(resultMap!=null&&!resultMap.isEmpty()){
			String status = (String)resultMap.get(CommonConstants.REST_MSG_FORMAT_STATUS);
			
			//保存紧急联系人手机号码和姓名成功
			if(CommonConstants.MSG_CODE_REST_SAVE_PROFILE_SUCCESS.equals(status)){
				returnMap.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_SAVE_URGENT_USER_SUCCESS);
			}else{
				//保存紧急联系人手机号码和姓名失败,包含数据库异常
				returnMap.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_SAVE_URGENT_USER_FAILED);
			}
		}
		
		Gson gson = new Gson();
		returnValue= gson.toJson(returnMap);
		
		return returnValue;
	}
	
	/**
	 * 保存服务密码
	 * @param telno
	 * @param servicePwd
	 * @param clientType
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("saveServicePassword/{telno}/{servicePwd}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String saveServicePassword(@PathParam(value="telno") String telno,@PathParam(value="servicePwd") String servicePwd,
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
		
		if(servicePwd==null||"".equals(servicePwd)){
			logger.info("服务密码为空");
			map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_SAVE_PROFILE_FAILED);
			map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NULL_SERVICE_PWD);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}

		logger.debug("[Regist Web Service Request] : <User ID:"+telno+", ClientType:"+clientType+">");
		
		//保存服务密码
		HashMap resultMap = UserProfileHandler.saveUserProfile(telno, null, null, servicePwd, null, null, null);

		HashMap returnMap = new HashMap();
		
		if(resultMap!=null&&!resultMap.isEmpty()){
			String status = (String)resultMap.get(CommonConstants.REST_MSG_FORMAT_STATUS);
			
			//保存服务密码成功
			if(CommonConstants.MSG_CODE_REST_SAVE_PROFILE_SUCCESS.equals(status)){
				returnMap.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_SAVE_SERVICE_PWD_SUCCESS);
			}else{
				//保存服务密码失败,包含数据库异常
				returnMap.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_SAVE_SERVICE_PWD_FAILED);
			}
		}
		
		Gson gson = new Gson();
		returnValue= gson.toJson(returnMap);
		
		return returnValue;
	}

	
	/**
	 * 保存安全问答信息
	 * @param telno
	 * @param securityQue
	 * @param securityAns
	 * @param clientType
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GET
	@Path("saveSecurityInfo/{telno}/{securityQue}/{securityAns}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String saveSecurityInfo(@PathParam(value="telno") String telno,@PathParam(value="securityQue") String securityQue,
			@PathParam(value="securityAns") String securityAns,@PathParam(value="clientType") String clientType){
		
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
		
		if(securityQue==null||"".equals(securityQue)||securityAns==null||"".equals(securityAns)){
			logger.info("安全问答信息为空");
			map = new HashMap<String,String>();
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_SAVE_PROFILE_FAILED);
			map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_ACCESS_DENIED_FOR_NULL_SECURITY_QA);
			Gson gson = new Gson();
			returnValue= gson.toJson(map);
			return returnValue;
		}

		logger.debug("[Regist Web Service Request] : <User ID:"+telno+", ClientType:"+clientType+">");
		
		//保存安全问答信息
		HashMap resultMap = UserProfileHandler.saveUserProfile(telno, null, null, null, securityQue, securityAns, null);

		HashMap returnMap = new HashMap();
		
		if(resultMap!=null&&!resultMap.isEmpty()){
			String status = (String)resultMap.get(CommonConstants.REST_MSG_FORMAT_STATUS);
			
			//保存安全问答成功
			if(CommonConstants.MSG_CODE_REST_SAVE_PROFILE_SUCCESS.equals(status)){
				returnMap.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_SAVE_SECURITY_QA_SUCCESS);
			}else{
				//保存安全问答失败,包含数据库异常
				returnMap.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_SAVE_SECURITY_QA_FAILED);
			}
		}
		
		Gson gson = new Gson();
		returnValue= gson.toJson(returnMap);
		
		return returnValue;
	}
}
