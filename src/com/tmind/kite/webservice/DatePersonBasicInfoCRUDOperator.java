package com.tmind.kite.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 完成对约会对象信息的增删改操作集合
 */
public class DatePersonBasicInfoCRUDOperator {

	@GET
	@Path("addDatePersonBasicInfo")
	@Produces(MediaType.TEXT_HTML)
	public String addDatePersonBasicInfo(){
		return null;
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
	
	@GET
	@Path("markInfoUseful")
	@Produces(MediaType.TEXT_HTML)
	public String markInfoUseful(){
		
		return null;
	}
	
	//查询评论内容
	@GET
	@Path("queryCommand")
	@Produces(MediaType.TEXT_HTML)
	public String queryCommand(){
			
		return null;
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
