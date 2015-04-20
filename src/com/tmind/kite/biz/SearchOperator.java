package com.tmind.kite.biz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.model.SearchBoxAutoCompleteModel;
import com.tmind.kite.model.SearchBoxWrapModel;
import com.tmind.kite.model.SearchDetailInfoModel;
import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.GetGsonObject;

public class SearchOperator {
	protected static final Logger logger = Logger.getLogger(SearchOperator.class);

	/**
	 * 
	 * @param keyWords
	 * @param limitSize, 这个用来控制搜索的条数
	 * @return
	 */
	public static String autoCompleteBoxSearch(String keyWords, int limitSize, int currentPageNo){
		SearchBoxWrapModel modelWrapper = new SearchBoxWrapModel();
		
		List<SearchBoxAutoCompleteModel> model = new ArrayList<SearchBoxAutoCompleteModel>();
		DBUtils.getConnection();
		//总纪录数
		//结果数组大小，最多只返回五条结果数，数组指定长度为5
		//如果用户输入的内容为数字则进行keywords的telno匹配
		ResultSet rs4Telno = null;
		ResultSet rs4Name = null;
		ResultSet rs4FullText = null;
		try {
			if(keyWords.matches("\\d+")){
				rs4Telno = queryByTelno(keyWords, currentPageNo*limitSize+1, currentPageNo*limitSize+limitSize);
				while((rs4Telno.next() && model.size()<=limitSize)){ //最多5个结果
					SearchBoxAutoCompleteModel s = new SearchBoxAutoCompleteModel();
					s.setId(rs4Telno.getString("id")); 
					s.setMatchWords(rs4Telno.getString("telno_keywords")+" "+rs4Telno.getString("name_keywords"));
					model.add(s);
				}
			}else{
				//匹配昵称
				rs4Name = queryByName(keyWords, currentPageNo*limitSize+1, currentPageNo*limitSize+limitSize);
				while(rs4Name.next() && model.size()<=limitSize){ //最多5个结果
					SearchBoxAutoCompleteModel s = new SearchBoxAutoCompleteModel();
					s.setId(rs4Name.getString("id")); 
					s.setMatchWords(rs4Name.getString("telno_keywords")+" "+rs4Name.getString("name_keywords"));
					model.add(s);
				}
				//全文内容检索匹配
				rs4FullText = queryByFullText(keyWords, currentPageNo*limitSize+1, currentPageNo*limitSize+limitSize);
				while(rs4FullText.next() && model.size()<=limitSize){ //最多5个结果
					SearchBoxAutoCompleteModel s = new SearchBoxAutoCompleteModel();
					s.setId(rs4FullText.getString("id")); 
					s.setMatchWords(rs4FullText.getString("telno_keywords")+" "+rs4FullText.getString("name_keywords"));
					model.add(s);
				}
				//TODO匹配全文对比，需考虑性能问题：是否开发该段
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			modelWrapper.setStatus("error");
			return GetGsonObject.getInstance().toJson(modelWrapper);
		}finally{
			//清空结果集
			DBUtils.freeConnection(null, null, rs4Telno);
			DBUtils.freeConnection(null, null, rs4Name);
			DBUtils.freeConnection(null, null, rs4FullText);
		}
		modelWrapper.setStatus("success");
		modelWrapper.setModel(model);
		logger.info("获得搜索自动完成内容为:"+modelWrapper);
		return GetGsonObject.getInstance().toJson(modelWrapper);
	}
	
	public static String queryDetailInfoById(String queryId){
		SearchDetailInfoModel detailModel = new SearchDetailInfoModel();
		Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
    		String sql = "select id, telno_keywords, name_keywords, useful_mark_num, look_score, talk_score, act_score, "
    				+ "peronal_score, come_from, comments_table_name, detail_info_table_name from m_search_info where id = ?";
    		conn = DBUtils.getConnection();
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, queryId);
    		rs = ps.executeQuery();
    		if(rs.next()){
    			detailModel.setId(rs.getString("id"));
        		detailModel.setTelno(rs.getString("telno_keywords"));
        		detailModel.setName(rs.getString("name_keywords"));
        		detailModel.setUseful_mark_num(rs.getString("useful_mark_num"));
        		detailModel.setLook_score(rs.getString("look_score"));
        		detailModel.setTalk_score(rs.getString("talk_score"));
        		detailModel.setAct_score(rs.getString("act_score"));
        		detailModel.setPeronal_score(rs.getString("peronal_score"));
        		detailModel.setCome_from(rs.getString("come_from"));
        		detailModel.setComments_table_name(rs.getString("comments_table_name"));
    		}
    		//TODO查询评价表，查询出评价的数目
    		
    		detailModel.setComments_no("");
    	}catch(Exception e){
    		DBUtils.freeConnection(conn, ps, rs);
    		return null;
    	}finally{
    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
    		DBUtils.freeConnection(conn, ps, null);
    	}
		return GetGsonObject.getInstance().toJson(detailModel);
	}
	
	public static boolean addIntoSearchHistory(String keyWords, String telno){
		if(addIntoHistory(keyWords, telno)){
			return true;
		}else{
			return false;
		}
	}
/******************************************************私有方法***********************************************************/
	//查询含有telno的结果集
	private static ResultSet queryByTelno(String telno, int pre, int post){
		Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
    		//TODO 未来修改成mysql的全文索引
    		//TODO 限制查询的条数为5条
    		String sql = "select id, telno_keywords, name_keywords from m_search_info where telno_keywords like '%?%' limit ?, ?";
    		conn = DBUtils.getConnection();
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, telno);
    		ps.setInt(2, pre);
    		ps.setInt(2, post);
    		return ps.executeQuery();
    	}catch(Exception e){
    		DBUtils.freeConnection(conn, ps, rs);
    		return null;
    	}finally{
    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
    		DBUtils.freeConnection(conn, ps, null);
    	}
	}
	
	//查询含有昵称的结果集
		private static ResultSet queryByName(String name, int pre, int post){
			Connection conn = null;
	    	PreparedStatement ps = null;
	    	ResultSet rs = null;
	    	try{
	    		//TODO 未来修改成mysql的全文索引
	    		//TODO 限制查询的条数为5条
	    		String sql = "select id, telno_keywords, name_keywords from m_search_info where name_keywords like '%?%' limit ?, ?";
	    		conn = DBUtils.getConnection();
	    		ps = conn.prepareStatement(sql);
	    		ps.setString(1, name);
	    		ps.setInt(2, pre);
	    		ps.setInt(2, post);
	    		return ps.executeQuery();
	    	}catch(Exception e){
	    		DBUtils.freeConnection(conn, ps, rs);
	    		return null;
	    	}finally{
	    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
	    		DBUtils.freeConnection(conn, ps, null);
	    	}
		}
	//文关键字查询匹配  
		private static ResultSet queryByFullText(String context, int pre, int post ){
			Connection conn = null;
	    	PreparedStatement ps = null;
	    	ResultSet rs = null;
	    	try{
	    		//TODO 未来修改成mysql的全文索引
	    		//TODO 限制查询的条数为5条
	    		String sql = "select id, telno_keywords, name_keywords from m_search_info where full_text_keywords like '%?%' limit ?, ?";
	    		conn = DBUtils.getConnection();
	    		ps = conn.prepareStatement(sql);
	    		ps.setString(1, context);
	    		ps.setInt(2, pre);
	    		ps.setInt(2, post);
	    		return ps.executeQuery();
	    	}catch(Exception e){
	    		DBUtils.freeConnection(conn, ps, rs);
	    		return null;
	    	}finally{
	    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
	    		DBUtils.freeConnection(conn, ps, null);
	    	}
		}
		
	//添加搜索纪录
		private static boolean addIntoHistory(String keyWords, String telno ){
			Connection conn = null;
	    	PreparedStatement ps = null;
	    	try{
	    		//TODO 未来修改成mysql的全文索引
	    		//TODO 限制查询的条数为5条
	    		String sql = "insert into search_history(search_key_words, add_date, add_by) values(?,?,?)";
	    		conn = DBUtils.getConnection();
	    		ps = conn.prepareStatement(sql);
	    		ps.setString(1, keyWords);
	    		ps.setDate(2, (java.sql.Date) new Date());
	    		ps.setString(3, telno);
	    		ps.execute();
	    		return true;
	    	}catch(Exception e){
	    		DBUtils.freeConnection(conn, ps, null);
	    		return false;
	    	}finally{
	    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
	    		DBUtils.freeConnection(conn, ps, null);
	    	}
		}
}
