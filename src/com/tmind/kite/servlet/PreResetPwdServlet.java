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

import org.apache.log4j.Logger;

import com.tmind.kite.utils.DBUtils;

public class PreResetPwdServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4528790504740609986L;

	protected static final Logger logger = Logger.getLogger(PreResetPwdServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
	    String ptelno =  request.getParameter("ptelno");
	    String urgentName =  request.getParameter("urgentName");
	    String urgentTelno =  request.getParameter("urgentTelno");
	    
	    Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "select active_flag from m_user where tel_no=? and urgent_name=? and urgent_telno=?";
		logger.debug("查询用户个人设置:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, ptelno);
			ps.setString(2, urgentName);
			ps.setString(3, urgentTelno);
			rs = ps.executeQuery();
			if(rs.next()&&rs.getString("active_flag").equalsIgnoreCase("Y")){
				request.getSession().setAttribute("telno", ptelno);
				response.sendRedirect("gen-pwd.html");
			}else{
				response.sendRedirect("404.html?1");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
	}
}
