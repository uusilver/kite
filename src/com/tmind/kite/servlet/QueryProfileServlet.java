package com.tmind.kite.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmind.kite.utils.DBUtils;

@SuppressWarnings("serial")
public class QueryProfileServlet extends HttpServlet{
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
	    String telno = (String) request.getSession().getAttribute("telno");
	    Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "select urgent_name, urgent_telno, touch_freq from m_user where tel_no=?";
		System.out.println("查询用户个人设置:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, telno);
			rs = ps.executeQuery();
			if(rs.next()){
				PrintWriter out = response.getWriter();
				//System.out.println(rs.getString("urgent_name"));
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
