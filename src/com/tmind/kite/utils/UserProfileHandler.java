package com.tmind.kite.utils;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class UserProfileHandler {

	protected static final Logger logger = Logger.getLogger(UserProfileHandler.class);
	
	public static HashMap getUserProfile(String telno){
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "select urgent_name, urgent_telno, touch_freq from m_user where tel_no=?";
		logger.debug("查询用户个人设置:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, telno);
			rs = ps.executeQuery();
			if(rs.next()){
				PrintWriter out = response.getWriter();
				//logger.debug(rs.getString("urgent_name"));
			    out.write(rs.getString("urgent_name")+"@"+rs.getString("urgent_telno")+"@"+rs.getString("touch_freq")); 
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
	}
}
