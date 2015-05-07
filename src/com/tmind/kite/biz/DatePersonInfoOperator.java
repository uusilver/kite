package com.tmind.kite.biz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.tmind.kite.model.CommandListModel;
import com.tmind.kite.model.CommandModel;
import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.GetGsonObject;
import com.tmind.kite.utils.StringImageUtil;

/**
 * 
 * @author lijunying
 * @desc: 用来操作约会对象信息的增删改查
 */
public class DatePersonInfoOperator {
	
	protected static final Logger logger = Logger.getLogger(DatePersonInfoOperator.class);
	
	
	public static boolean addDatePersonBasicInfo(String telno_keywords, String name_keywords, String full_text_keywords, 
							                     float look_score, float talk_score, float act_score, float peronal_score, String telno, String clientType, String picStr, String picType) throws Exception{
		Connection conn = null;
    	PreparedStatement ps = null;
    	
    		//保存图片进磁盘
    		String uuidPicName = UUID.randomUUID().toString()+"."+picType;
    		if(StringImageUtil.generateImage(picStr, uuidPicName)){
	    			try{
	    			//TODO 未来修改成mysql的全文索引
	        		//TODO 限制查询的条数为5条
	        		String sql = "insert into m_search_info (telno_keywords, name_keywords, full_text_keywords, source_from, add_date, upd_date, active_flag,"
	        				+ " add_by, comments_table_name, detail_info_table_name, useful_mark_num, look_score, talk_score, act_score, peronal_score, come_from, remarks1, pic_name_url, pic_type) "
	        				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	        		logger.info(sql);
	        		conn = DBUtils.getConnection();
	        		ps = conn.prepareStatement(sql);
	        		ps.setString(1, telno_keywords);
	        		ps.setString(2, name_keywords);
	        		ps.setString(3, full_text_keywords);
	        		ps.setString(4, "IOS");
	        		java.util.Date date=new java.util.Date();
	        		Timestamp tt=new Timestamp(date.getTime());
	        		ps.setTimestamp(5, tt);
	        		ps.setTimestamp(6, tt);
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
	        		ps.setString(17, uuidPicName);
	        		ps.setString(18, getTotalScore(look_score, talk_score, act_score, peronal_score));
	        		ps.setString(19, picType); //保存图片类型 jpg, png
	        		ps.execute();
	        		return true;
	        	}catch(Exception e){
	        		logger.warn(e.getMessage());
	        		DBUtils.freeConnection(conn, ps, null);
	        		return false;
	        	}finally{
	        		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
	        		DBUtils.freeConnection(conn, ps, null);
	        	}
    		}else{
    			logger.info("保存图片失败");
    			return false;
    		}
    		
		
	}
	
	public static boolean deleteDatePersonBasicInfo(String queryId){
		Connection conn = null;
    	PreparedStatement ps = null;
    	try{
    		//TODO 未来修改成mysql的全文索引
    		//TODO 限制查询的条数为5条
    		String sql = "update m_search_info set active_flag = 'N' where id=?";
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
	
	//查询是否点赞过
	public static boolean queryMarkOrNot(String queryId, String telno){
		Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
    		//TODO 未来修改成mysql的全文索引
    		//TODO 限制查询的条数为5条
    		String sql = "select id from date_person_mark_record_table where mark_queryid=? and mark_by=?";
    		conn = DBUtils.getConnection();
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, queryId);
    		ps.setString(2, telno);
    		rs = ps.executeQuery();
    		if(rs.next())
    		{
    			return true;
    		}else{
    			return false;
    		}
    	}catch(Exception e){
    		DBUtils.freeConnection(conn, ps, rs);
    		return false;
    	}finally{
    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
    		DBUtils.freeConnection(conn, ps, null);
    	}
	}
	//点赞
	@SuppressWarnings("resource")
	public static boolean markInfoUseful(String queryId, String telno){
		Connection conn = null;
    	PreparedStatement ps = null;
    	try{
    		//TODO 未来修改成mysql的全文索引
    		//TODO 限制查询的条数为5条
    		String sql = "update m_search_info set useful_mark_num = useful_mark_num+1 where id=?";
    		conn = DBUtils.getConnection();
    		conn.setAutoCommit(false);
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, queryId);
    		ps.execute();
    		sql = "insert into date_person_mark_record_table (mark_queryid, mark_by, mark_date) values (?,?,?)";
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, queryId);
    		ps.setString(2, telno);
    		java.util.Date date=new java.util.Date();
    		Timestamp tt=new Timestamp(date.getTime());
    		ps.setTimestamp(3, tt);
    		ps.execute();
    		conn.commit();
    		return true;
    	}catch(Exception e){
    		try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
    		String sql = "select id, comments_content from search_comments where query_id=? and active_flag='Y'";
    		conn = DBUtils.getConnection();
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, queryId);
    		rs = ps.executeQuery();
    		CommandModel cm = new CommandModel();
    		cm.setQuery_id(queryId);
    		List<CommandListModel> cmlist = new ArrayList<CommandListModel>();
    		while(rs.next()){
    			CommandListModel ele = new CommandListModel();
    			ele.setId(rs.getString("id"));
    			ele.setComments_content(rs.getString("comments_content"));
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
	
	public static boolean commentAdd(String queryId, String commentContent, String telno){
		Connection conn = null;
    	PreparedStatement ps = null;
    	try{
			String sql = "insert into search_comments (query_id, comments_content, add_date, add_by, active_flag) "
					+ "values(?,?,?,?,'Y')";
			conn = DBUtils.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, queryId);
			ps.setString(2, commentContent);
			java.util.Date date=new java.util.Date();
    		Timestamp tt=new Timestamp(date.getTime());
			ps.setTimestamp(3, tt);
			ps.setString(4, telno);
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
	
	public static boolean commentDelete(String commandId){
		Connection conn = null;
    	PreparedStatement ps = null;
    	try{
			String sql = "update search_comments set active_flag='N' where id=? ";
			conn = DBUtils.getConnection();
			ps = conn.prepareStatement(sql);
			ps.setString(1, commandId);
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
	
	/**************/
	
	private  static String getTotalScore(float look_score, float talk_score, float act_score, float peronal_score){
		//统计大概分数，不需要精确计算，所以不试用BigDecimal了
		float total=(look_score+talk_score+act_score+peronal_score)/4;
		DecimalFormat decimalFormat=new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		return decimalFormat.format(total);//format 返回的是字符串
	}
}
