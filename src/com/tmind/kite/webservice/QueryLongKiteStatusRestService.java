package com.tmind.kite.webservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.tmind.kite.model.LongKiteServiceStatusModel;
import com.tmind.kite.utils.GetGsonObject;

@Path("queryLongKiteStatusRest")
public class QueryLongKiteStatusRestService {
	protected static final Logger logger = Logger.getLogger(QueryLongKiteStatusRestService.class);

	/**
	 * @param telno
	 * @param pwd
	 * @param clientType
	 * @return String
	 * 用于获得长线风筝状态
	 * 状态码1:开启
	 * 状态码0:关闭
	 * @Cindy 不再提醒都标记保存在本地
	 */
	@GET
	@Path("retreiveLongKiteServiceStatus/{telno}/{password}/{clientType}")
	@Produces(MediaType.TEXT_PLAIN)
	public String retreiveLongKiteServiceStatus(@PathParam(value="telno") String telno,
            									@PathParam(value="password") String pwd,
            									@PathParam(value="clientType") String clientType
            									){
		
		//TODO 查询数据库根据telno来获得长线风筝都运行状态
		LongKiteServiceStatusModel serviceStatusModel = new LongKiteServiceStatusModel();
		return GetGsonObject.getInstance().toJson(serviceStatusModel);
	}
	
}
