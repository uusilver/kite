package com.tmind.kite.biz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tmind.kite.model.CommandListModel;
import com.tmind.kite.model.CommandModel;
import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.GetGsonObject;

/**
 * 
 * @author lijunying
 * @desc: 用来操作约会对象信息的增删改查
 */
public class DatePersonInfoOperator {

	public static boolean addDatePersonBasicInfo(String telno_keywords, String name_keywords, String full_text_keywords, 
							                     float look_score, float talk_score, float act_score, float peronal_score, String telno, String clientType){
		Connection conn = null;
    	PreparedStatement ps = null;
    	try{
    		//TODO 未来修改成mysql的全文索引
    		//TODO 限制查询的条数为5条
    		String sql = "insert into m_search_info (telno_keywords, name_keywords, full_text_keywords, source_from, add_date, upd_date, active_flag"
    				+ " add_who, comments_table_name, detail_info_table_name, useful_mark_num, look_score, talk_score, act_score, peronal_score, come_from) "
    				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    		conn = DBUtils.getConnection();
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, telno_keywords);
    		ps.setString(2, name_keywords);
    		ps.setString(3, full_text_keywords);
    		ps.setString(4, "IOS");
    		ps.setDate(5, (java.sql.Date) new Date());
    		ps.setDate(6, (java.sql.Date) new Date());
    		ps.setString(7, "Y");
    		ps.setString(8, telno);
    		ps.setString(9, "search_comments");
    		ps.setString(10, "search_detail_info");
    		ps.setInt(11, 0);
    		ps.setFloat(12, look_score);
    		ps.setFloat(13, talk_score);
    		ps.setFloat(14, act_score);
    		ps.setFloat(15, peronal_score);
    		ps.setString(16, "IOS");
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
	
	//点赞
	public static boolean markInfoUseful(String queryId){
		Connection conn = null;
    	PreparedStatement ps = null;
    	try{
    		//TODO 未来修改成mysql的全文索引
    		//TODO 限制查询的条数为5条
    		String sql = "update m_search_info set useful_mark_num = useful_mark_num+1 where id=?";
    		conn = DBUtils.getConnection();
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, queryId);
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
	
	//获取评论列表
	public static String queryCommands(String queryId){
		Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
    		//TODO 未来修改成mysql的全文索引
    		//TODO 限制查询的条数为5条
    		String sql = "select id, comments_content from search_comments where query_id=?";
    		conn = DBUtils.getConnection();
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, queryId);
    		rs = ps.executeQuery();
    		CommandModel cm = new CommandModel();
    		cm.setQuery_id(queryId);
    		List<CommandListModel> cmlist = new ArrayList<CommandListModel>();
    		while(rs.next()){
    			CommandListModel ele = new CommandListModel();
    			cmlist.add(ele);
    		}
    		cm.setComdList(cmlist);
    		return GetGsonObject.getInstance().toJson(cm);
    	}catch(Exception e){
    		DBUtils.freeConnection(conn, ps, rs);
    		return null;
    	}finally{
    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
    		DBUtils.freeConnection(conn, ps, rs);
    	}
	}
}
