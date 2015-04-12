package com.tmind.kite.biz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.tmind.kite.model.SearchBoxAutoCompleteModel;
import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.GetGsonObject;

public class SearchOperator {
	protected static final Logger logger = Logger.getLogger(SearchOperator.class);

	
	public String autoCompleteBoxSearch(String keyWords){
		SearchBoxAutoCompleteModel model = new SearchBoxAutoCompleteModel();
		DBUtils.getConnection();
		//如果用户输入的内容为数字则进行keywords的telno匹配
		if(keyWords.matches("\\d+")){
			
		}else{
			//匹配昵称
			
			//TODO匹配全文对比，需考虑性能问题：是否开发该段
		}
		logger.info("获得搜索自动完成内容为:"+model);
		return GetGsonObject.getInstance().toJson(model);
	}
	
	//查询含有telno的结果集
	public ResultSet queryByTelno(String telno){
		Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
    		//TODO 未来修改成mysql的全文索引
    		String sql = "select telno_keywords, name_keywords from m_search_info where telno_keywords like '%?%'";
    		conn = DBUtils.getConnection();
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, telno);
    		return ps.executeQuery();
    	}catch(Exception e){
    		DBUtils.freeConnection(conn, ps, rs);
    		return null;
    	}finally{
    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
    		DBUtils.freeConnection(conn, ps, null);
    	}
	}
}
