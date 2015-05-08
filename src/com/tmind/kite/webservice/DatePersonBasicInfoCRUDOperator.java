package com.tmind.kite.webservice;
/**
 * 完成对约会对象信息的增删改操作集合
 */
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.tmind.kite.biz.DatePersonInfoOperator;
import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.model.SearchDetailInfoModel;
import com.tmind.kite.utils.GetGsonObject;


@Path("dateRest")
public class DatePersonBasicInfoCRUDOperator {

	@POST
	@Path("addDatePersonBasicInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_HTML)
	public String addDatePersonBasicInfo(String data) throws Exception{
            					         
		//TODO 校验用户的基本信息是否合法
		//插入一条新纪录
		SearchDetailInfoModel m = GetGsonObject.getInstance().fromJson(data, SearchDetailInfoModel.class);
		if(DatePersonInfoOperator.addDatePersonBasicInfo(m.getTelno(), m.getName(), m.getFull_text(), Float.valueOf(m.getLook_score()), Float.valueOf(m.getTalk_score()), Float.valueOf(m.getAct_score()), Float.valueOf(m.getPeronal_score()), m.getTelno(), m.getClientType(), m.getPicStr(), m.getPicType())){
			return CommonConstants.SUCCESS;
		}else{
			return CommonConstants.ERROR;
		}
	}
	
	/*
	@GET
	@Path("updateDatePersonBasicInfo")
	@Produces(MediaType.TEXT_HTML)
	public String updateDatePersonBasicInfo(){
		return null;
	}
	*/
	
	@GET
	@Path("deleteDatePersonBasicInfo/{queryId}/{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_HTML)
	public String deleteDatePersonBasicInfo(@PathParam(value="queryId") String queryId,
			 						        @PathParam(value="telno") String telno,
			 						        @PathParam(value="password") String password,
			 						        @PathParam(value="clientType") String clientType
											){
		//TODO 校验用户的基本信息是否合法
		if(DatePersonInfoOperator.deleteDatePersonBasicInfo(queryId)){
			return CommonConstants.SUCCESS;
		}else{
			return CommonConstants.ERROR;
		}
	}
	
	//查询是否点赞过
	@GET
	@Path("queryMarkOrNot/{queryId}/{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_HTML)
	public String queryMarkOrNot(@PathParam(value="queryId") String queryId,
								 @PathParam(value="telno") String telno,
            					 @PathParam(value="password") String password,
            					 @PathParam(value="clientType") String clientType
            					){
		//TODO 校验用户的基本信息是否合法
		if(DatePersonInfoOperator.queryMarkOrNot(queryId, telno)){
			//成功表示有点过赞
			return CommonConstants.SUCCESS;
		}else{
			//ERROR表示没有点过赞
			return CommonConstants.ERROR;
		}
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
		if(DatePersonInfoOperator.markInfoUseful(queryId, telno)){
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
		return DatePersonInfoOperator.queryCommands(queryId);
	}

	
	
	//针对显示信息，添加评论
	@GET
	@Path("commentAdd/{queryId}/{commentContent}/{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_HTML)
	public String commentAdd(@PathParam(value="queryId") String queryId,
						     @PathParam(value="commentContent") String commentContent,
             			     @PathParam(value="telno") String telno,
             			     @PathParam(value="password") String password,
             			     @PathParam(value="clientType") String clientType
             				){
		//TODO 校验用户的基本信息是否合法
				if(DatePersonInfoOperator.commentAdd(queryId, commentContent, telno)){
					return CommonConstants.SUCCESS;
				}else{
					return CommonConstants.ERROR;
				}
	}
	
	//删除评论内容，不提供更新评论内容
	@GET
	@Path("commentDelete/{commandId}/{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_HTML)
	public String commentDelete(@PathParam(value="commandId") String commandId,
		     			        @PathParam(value="telno") String telno,
		     			        @PathParam(value="password") String password,
		     			        @PathParam(value="clientType") String clientType
			     				){
		//TODO 校验用户的基本信息是否合法
		if(DatePersonInfoOperator.commentDelete(commandId)){
			return CommonConstants.SUCCESS;
		}else{
			return CommonConstants.ERROR;
		}
	}
	
	
}
