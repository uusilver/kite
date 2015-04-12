package com.tmind.kite.webservice;
/**
 * 完成用户的搜索操作的rest服务集合
 */
import javax.ws.rs.GET;
/*
 * @author lijunying
 * @用于搜索的Rest请求
 */
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

@Path("searchRest")
public class SearchRestService {

	protected static final Logger logger = Logger.getLogger(SearchRestService.class);

	/**
	 * @param keyWords
	 * @param telno
	 * @param password
	 * @param clientType
	 * @return String
	 * 用于返回自动完成框内所需的数据
	 */
	@GET
	@Path("getAutoCompleteArrays/{keyWords}/{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAutoCompleteArrays(@PathParam(value="keyWords") String keyWords,
			                            @PathParam(value="telno") String telno,
			                            @PathParam(value="password") String pwd,
							            @PathParam(value="clientType") String clientType
							            ){
		//TODO 根据传入的keywords信息来进行搜索
		
		return null;
	}
	
	/**
	 * 
	 * @param keyWords
	 * @param telno
	 * @param pwd
	 * @param clientType
	 * @return String
	 * 用户点击搜索后完成搜索，返回搜索结果(结果必须包含ID，这样可以用于进一步查询，被查询对象的详细证据时的数据)
	 */
	@GET
	@Path("getSearchResult/{keyWords}/{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSearchResult(@PathParam(value="keyWords") String keyWords,
			                      @PathParam(value="telno") String telno,
			                      @PathParam(value="password") String pwd,
							      @PathParam(value="clientType") String clientType){
		return null;
	}
	
	/**
	 * 
	 * @param queryId
	 * @param telno
	 * @param pwd
	 * @param clientType
	 * @return String
	 * 根据用户选择的需要展示详细信息的查询结果，来加载
	 * 
	 */
	@GET
	@Path("getDetailInfoByQueryId/{queryId}/{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getDetailInfoByQueryId(@PathParam(value="queryId") String queryId,
            						     @PathParam(value="telno") String telno,
                                         @PathParam(value="password") String pwd,
		                                 @PathParam(value="clientType") String clientType){
		return null;
	}
	
	/**
	 * 
	 * @param keyWords
	 * @param telno
	 * @param pwd
	 * @param clientType
	 * @return String
	 * 保存用户的搜索纪录进表
	 */
	@GET
	@Path("recordSearchWordsIntoHistoryTable/{keyWords}/{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String recordSearchWordsIntoHistoryTable(@PathParam(value="keyWords") String keyWords,
            						                @PathParam(value="telno") String telno,
            						                @PathParam(value="password") String pwd,
            						                @PathParam(value="clientType") String clientType){
		//TODO 创建用户的搜索纪录表
		return null;
	}
}
