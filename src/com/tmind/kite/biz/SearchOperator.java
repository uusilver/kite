package com.tmind.kite.biz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.model.CommentsModel;
import com.tmind.kite.model.SearchBoxAutoCompleteModel;
import com.tmind.kite.model.SearchBoxWrapModel;
import com.tmind.kite.model.SearchDetailInfoModel;
import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.GetGsonObject;
import com.tmind.kite.utils.StringImageUtil;

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
		//总纪录数
		//结果数组大小，最多只返回五条结果数，数组指定长度为5
		//如果用户输入的内容为数字则进行keywords的telno匹配
			if(keyWords.matches("\\d+")){
				model = queryByTelno(keyWords, currentPageNo*limitSize, currentPageNo*limitSize+limitSize,model);
				
			}else{
				//匹配昵称
				model = queryByName(keyWords, currentPageNo*limitSize+1, currentPageNo*limitSize+limitSize, model);
				if(model.size()<=limitSize){ //最多5个结果
					model = queryByFullText(keyWords, currentPageNo*limitSize+1, currentPageNo*limitSize+limitSize,model);
				}
				//TODO匹配全文对比，需考虑性能问题：是否开发该段
			}
		modelWrapper.setStatus("success");
		modelWrapper.setModel(model);
		logger.info("获得搜索自动完成内容为:"+modelWrapper);
		return GetGsonObject.getInstance().toJson(modelWrapper);
	}
	
	@SuppressWarnings("resource")
	public static String queryDetailInfoById(String queryId){
		SearchDetailInfoModel detailModel = new SearchDetailInfoModel();
		Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
    		String sql = "select id, telno_keywords, name_keywords, full_text_keywords, remarks1, useful_mark_num, look_score, talk_score, act_score, "
    				+ "peronal_score, come_from, comments_table_name, detail_info_table_name,  pic_name_url from m_search_info where id = ? and active_flag='Y'";
    		conn = DBUtils.getConnection();
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, queryId);
    		rs = ps.executeQuery();
    		if(rs.next()){
    			detailModel.setId(rs.getString("id"));
        		detailModel.setTelno(rs.getString("telno_keywords"));
        		detailModel.setName(rs.getString("name_keywords"));
        		detailModel.setFull_text(rs.getString("full_text_keywords"));
        		detailModel.setUseful_mark_num(rs.getString("useful_mark_num"));
        		detailModel.setLook_score(rs.getString("look_score"));
        		detailModel.setTalk_score(rs.getString("talk_score"));
        		detailModel.setAct_score(rs.getString("act_score"));
        		detailModel.setPeronal_score(rs.getString("peronal_score"));
        		detailModel.setTotal_score(rs.getString("remarks1"));
        		detailModel.setCome_from(rs.getString("come_from"));
        		detailModel.setComments_table_name(rs.getString("comments_table_name"));
        		String picNameUrl = rs.getString("pic_name_url");
        		//获取图片转化的字符串
        		detailModel.setPicStr(StringImageUtil.getImageStr(picNameUrl));
    		}
    		List<CommentsModel> cmList = new ArrayList<CommentsModel>();
    		sql = "select comments_content, add_date, add_by from search_comments where query_id=? and active_flag='Y'";
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, queryId);
    		rs = ps.executeQuery();
    		int totalCommentsNum  = 0;
    		while(rs.next()){
    			CommentsModel cModel = new CommentsModel();
    			cModel.setComment_content(rs.getString("comments_content"));
    			cModel.setComment_date(rs.getString("add_date"));
    			cModel.setComment_by(rs.getString("add_by"));
    			cmList.add(cModel);
    		}
    		detailModel.setCommentList(cmList);
    		detailModel.setComments_no(String.valueOf(totalCommentsNum));
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
	
	
	public static String getPersonHistory(String keyWords, int limitSize, int currentPageNo){
		SearchBoxWrapModel modelWrapper = new SearchBoxWrapModel();
		List<SearchBoxAutoCompleteModel> model = new ArrayList<SearchBoxAutoCompleteModel>();
		model = queryByPersonalHistory(keyWords, currentPageNo*limitSize, currentPageNo*limitSize+limitSize,model);
		modelWrapper.setStatus("success");
		modelWrapper.setModel(model);
		logger.info("获得个人查询历史记录:"+modelWrapper);
		return GetGsonObject.getInstance().toJson(modelWrapper);
	}
	
/******************************************************私有方法***********************************************************/
	//查询含有telno的结果集
	private static List<SearchBoxAutoCompleteModel> queryByTelno(String telno, int pre, int post, List<SearchBoxAutoCompleteModel> model){
		Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
    		//TODO 未来修改成mysql的全文索引
    		//TODO 限制查询的条数为5条
    		String sql = "select id, telno_keywords, name_keywords, full_text_keywords, source_from, add_date, remarks1 from m_search_info where telno_keywords like ? and active_flag='Y' limit ?, ?";
    		//select id, telno_keywords, name_keywords from m_search_info where telno_keywords like '%13851483034%' and active_flag='Y' limit 0, 5
    		logger.info("SQL:"+sql);
    		conn = DBUtils.getConnection();
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, "%"+telno+"%");
    		ps.setInt(2, pre);
    		ps.setInt(3, post);
    		rs = ps.executeQuery();
    		while(rs.next()&&model.size()<5){
    			SearchBoxAutoCompleteModel s = new SearchBoxAutoCompleteModel();
				s.setId(rs.getString("id")); 
				s.setMatchWords(rs.getString("telno_keywords")+" "+rs.getString("name_keywords"));
				s.setName_keywords(rs.getString("name_keywords"));
				s.setFull_text_keywords(rs.getString("full_text_keywords"));
				s.setSource_from(rs.getString("source_from"));
				s.setAdd_date(String.valueOf(rs.getTimestamp("add_date")));
				s.setTotal_score(rs.getString("remarks1"));
				model.add(s);
    		}
    		return model;
    	}catch(Exception e){
    		logger.warn(e.getMessage());
    		DBUtils.freeConnection(conn, ps, rs);
    		return null;
    	}finally{
    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
    		DBUtils.freeConnection(conn, ps, null);
    	}
	}
	
	//查询含有昵称的结果集
		private static List<SearchBoxAutoCompleteModel> queryByName(String name, int pre, int post, List<SearchBoxAutoCompleteModel> model){
			Connection conn = null;
	    	PreparedStatement ps = null;
	    	ResultSet rs = null;
	    	try{
	    		//TODO 未来修改成mysql的全文索引
	    		//TODO 限制查询的条数为5条
	    		String sql = "select id, telno_keywords, name_keywords, full_text_keywords, source_from, add_date, remarks1 from m_search_info where name_keywords like ? and active_flag='Y' limit ?,? ";
	    		conn = DBUtils.getConnection();
	    		ps = conn.prepareStatement(sql);
	    		ps.setString(1, "%"+name+"%");
	    		ps.setInt(2, pre);
	    		ps.setInt(2, post);
	    		rs = ps.executeQuery();
	    		while(rs.next()&&model.size()<5){
	    			SearchBoxAutoCompleteModel s = new SearchBoxAutoCompleteModel();
					s.setId(rs.getString("id")); 
					s.setMatchWords(rs.getString("telno_keywords")+" "+rs.getString("name_keywords"));
					s.setName_keywords(rs.getString("name_keywords"));
					s.setFull_text_keywords(rs.getString("full_text_keywords"));
					s.setSource_from(rs.getString("source_from"));
					s.setAdd_date(String.valueOf(rs.getTimestamp("add_date")));
					s.setTotal_score(rs.getString("remarks1"));
					model.add(s);
	    		}
	    		return model;
	    	}catch(Exception e){
	    		DBUtils.freeConnection(conn, ps, rs);
	    		return null;
	    	}finally{
	    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
	    		DBUtils.freeConnection(conn, ps, rs);
	    	}
		}
	//文关键字查询匹配  
		private static List<SearchBoxAutoCompleteModel> queryByFullText(String context, int pre, int post, List<SearchBoxAutoCompleteModel> model ){
			Connection conn = null;
	    	PreparedStatement ps = null;
	    	ResultSet rs = null;
	    	try{
	    		//TODO 未来修改成mysql的全文索引
	    		//TODO 限制查询的条数为5条
	    		String sql = "select id, telno_keywords, name_keywords, full_text_keywords, source_from, add_date, remarks1 from m_search_info where full_text_keywords like ? and active_flag='Y' limit ?, ? ";
	    		conn = DBUtils.getConnection();
	    		ps = conn.prepareStatement(sql);
	    		ps.setString(1, "%"+context+"%");
	    		ps.setInt(2, pre);
	    		ps.setInt(2, post);
	    		rs = ps.executeQuery();
	    		while(rs.next()&&model.size()<5){
	    			SearchBoxAutoCompleteModel s = new SearchBoxAutoCompleteModel();
					s.setId(rs.getString("id")); 
					s.setMatchWords(rs.getString("telno_keywords")+" "+rs.getString("name_keywords"));
					s.setName_keywords(rs.getString("name_keywords"));
					s.setFull_text_keywords(rs.getString("full_text_keywords"));
					s.setSource_from(rs.getString("source_from"));
					s.setAdd_date(String.valueOf(rs.getTimestamp("add_date")));
					s.setTotal_score(rs.getString("remarks1"));
					model.add(s);
	    		}
	    		return model;
	    	}catch(Exception e){
	    		DBUtils.freeConnection(conn, ps, rs);
	    		return null;
	    	}finally{
	    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
	    		DBUtils.freeConnection(conn, ps, rs);
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
	    		java.util.Date date=new java.util.Date();
	    		Timestamp tt=new Timestamp(date.getTime());
	    		ps.setTimestamp(2, tt);
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
		
		private static List<SearchBoxAutoCompleteModel> queryByPersonalHistory(String telno, int pre, int post, List<SearchBoxAutoCompleteModel> model){
			Connection conn = null;
	    	PreparedStatement ps = null;
	    	ResultSet rs = null;
	    	try{
	    		//TODO 未来修改成mysql的全文索引
	    		//TODO 限制查询的条数为5条
	    		String sql = "select id, telno_keywords, name_keywords, full_text_keywords, source_from, add_date, remarks1 from m_search_info where add_by=? and active_flag='Y' limit ?, ?";
	    		//select id, telno_keywords, name_keywords from m_search_info where telno_keywords like '%13851483034%' and active_flag='Y' limit 0, 5
	    		logger.info("SQL:"+sql);
	    		conn = DBUtils.getConnection();
	    		ps = conn.prepareStatement(sql);
	    		ps.setString(1, telno);
	    		ps.setInt(2, pre);
	    		ps.setInt(3, post);
	    		rs = ps.executeQuery();
	    		while(rs.next()&&model.size()<5){
	    			SearchBoxAutoCompleteModel s = new SearchBoxAutoCompleteModel();
					s.setId(rs.getString("id")); 
					s.setMatchWords(rs.getString("telno_keywords")+" "+rs.getString("name_keywords"));
					s.setName_keywords(rs.getString("name_keywords"));
					s.setFull_text_keywords(rs.getString("full_text_keywords"));
					s.setSource_from(rs.getString("source_from"));
					s.setAdd_date(String.valueOf(rs.getTimestamp("add_date")));
					s.setTotal_score(rs.getString("remarks1"));
					model.add(s);
	    		}
	    		return model;
	    	}catch(Exception e){
	    		logger.warn(e.getMessage());
	    		DBUtils.freeConnection(conn, ps, rs);
	    		return null;
	    	}finally{
	    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
	    		DBUtils.freeConnection(conn, ps, null);
	    	}
		}
}
