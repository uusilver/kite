package com.tmind.kite.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.tmind.kite.biz.DatePersonInfoOperator;
import com.tmind.kite.constants.CommonConstants;

/**
 * 完成对约会对象信息的增删改操作集合
 */
public class DatePersonBasicInfoCRUDOperator {

	@GET
	@Path("addDatePersonBasicInfo/{telno_keywords}/{name_keywords}/{full_text_keywords}/{look_score}/{talk_score}/{act_score}/{peronal_score}//{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_HTML)
	public String addDatePersonBasicInfo(@PathParam(value="telno_keywords") String telno_keywords,
            				             @PathParam(value="name_keywords") String name_keywords,
            				             @PathParam(value="full_text_keywords") String full_text_keywords,
            				             @PathParam(value="look_score") float look_score,
            				             @PathParam(value="talk_score") float talk_score,
            				             @PathParam(value="act_score") float act_score,
            				             @PathParam(value="peronal_score") float peronal_score,
            				             @PathParam(value="telno") String telno,
            				             @PathParam(value="password") String password,
            				             @PathParam(value="clientType") String clientType
            					         ){
		//TODO 校验用户的基本信息是否合法
		//插入一条新纪录
		if(DatePersonInfoOperator.addDatePersonBasicInfo(telno_keywords, name_keywords, full_text_keywords, look_score, talk_score, act_score, peronal_score, telno, clientType)){
			return CommonConstants.SUCCESS;
		}else{
			return CommonConstants.ERROR;
		}
	}
	
	@GET
	@Path("updateDatePersonBasicInfo")
	@Produces(MediaType.TEXT_HTML)
	public String updateDatePersonBasicInfo(){
		return null;
	}
	
	@GET
	@Path("deleteDatePersonBasicInfo")
	@Produces(MediaType.TEXT_HTML)
	public String deleteDatePersonBasicInfo(){
		return null;
	}
	
	//点赞
	@GET
	@Path("markInfoUseful/{queryId}/{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_HTML)
	public String markInfoUseful(@PathParam(value="queryId") String queryId,
								 @PathParam(value="telno") String telno,
            					 @PathParam(value="password") String password,
            					 @PathParam(value="clientType") String clientType
            					){
		//TODO 校验用户的基本信息是否合法
		if(DatePersonInfoOperator.markInfoUseful(queryId)){
			return CommonConstants.SUCCESS;
		}else{
			return CommonConstants.ERROR;
		}
	}
	
	//查询评论内容
	@GET
	@Path("queryCommand/{queryId}/{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_HTML)
	public String queryCommand(@PathParam(value="queryId") String queryId,
			 	               @PathParam(value="telno") String telno,
			                   @PathParam(value="password") String password,
			                   @PathParam(value="clientType") String clientType
			                   ){
		//TODO 校验用户的基本信息是否合法
		if(DatePersonInfoOperator.queryCommands(queryId)!=null){
			return CommonConstants.SUCCESS;
		}else{
			return CommonConstants.ERROR;
		}
	}
	
	//针对显示信息，添加评论
	@GET
	@Path("commentAdd")
	@Produces(MediaType.TEXT_HTML)
	public String commentAdd(){
		
		return null;
	}
	
	//删除评论内容，不提供更新评论内容
	@GET
	@Path("commentDelete")
	@Produces(MediaType.TEXT_HTML)
	public String commentDelete(){
		
		return null;
	}
	
	
}
