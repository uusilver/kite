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

public class QueryMainServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4188434648874653761L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	    String telno = (String) request.getSession().getAttribute("telno");
	    Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "select service_flag, urgent_name, urgent_telno from m_user where tel_no=?";
		System.out.println("查询用户个人设置:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, telno);
			rs = ps.executeQuery();
			PrintWriter out = response.getWriter();
			if(rs.next()){
				if(rs.getString("urgent_name")==null||rs.getString("urgent_telno")==null){
					    out.write("error12"); 
					
				}else{
				    out.write(rs.getString("service_flag")); 
				}
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
		
		
	    
		
	    
	}
}
