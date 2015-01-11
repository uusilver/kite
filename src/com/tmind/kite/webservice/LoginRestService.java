package com.tmind.kite.webservice;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import com.google.gson.Gson;

import com.tmind.kite.utils.LoginHandler;

@Path("loginRest")
public class LoginRestService {
	
	protected static final Logger logger = Logger.getLogger(LoginRestService.class);
	
	@GET
	@Path("login/{userName}/{password}")
	@Produces(MediaType.TEXT_PLAIN)
	public String login(@PathParam(value="userName") String telno,@PathParam(value="password") String pwd){
		String returnValue = "";

		logger.debug("[Login Web Service Request] : <User ID:"+telno+", Password:"+pwd+">");
		
		HashMap resultMap = LoginHandler.login(telno, pwd);
		
		if(!resultMap.isEmpty()){
			Gson gson = new Gson();
			returnValue= gson.toJson(resultMap);
			
			logger.debug("[Login Web Service Response] : <"+returnValue+">");
		}
		return returnValue;
	}
}
