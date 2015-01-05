package com.tmind.kite.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmind.kite.utils.DBUtils;

public class StopBodyGuardServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7213742721844414990L;


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	    System.out.println("关闭服务..........");
	    PrintWriter out = response.getWriter();
	    String telno = (String) request.getSession().getAttribute("telno");
	    if(stopService(telno)){
	    	out.write("success"); 
		}else{
			//关闭服务失败:error7
			out.write("error7");
		}
	    
	}
	
	
	private boolean stopService(String telno){
		boolean flag = false;
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "update m_user set service_flag='N', service_stop_time=?  where tel_no=?";
		System.out.println("用户关闭服务:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setTimestamp(1, new Timestamp((new java.util.Date()).getTime()));
			ps.setString(2, telno);
			ps.execute();
			flag = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = false;
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
		return flag;	
	}
}
