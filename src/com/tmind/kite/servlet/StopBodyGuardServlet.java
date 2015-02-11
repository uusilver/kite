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

import org.apache.log4j.Logger;

import com.tmind.kite.utils.DBUtils;

public class StopBodyGuardServlet extends HttpServlet{

	private static final long serialVersionUID = -7213742721844414990L;
	
	protected static final Logger logger = Logger.getLogger(StopBodyGuardServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	    logger.debug("关闭服务..........");
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
		String sql = " update m_user m,web_service_record r"
				   + " set r.service_flag='N', r.service_stop_time=?  "
				   + " where m.id = r.user_id "
				   + " and m.tel_no=?";
		logger.debug("用户关闭服务:"+sql);
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
