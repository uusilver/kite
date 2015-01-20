package com.tmind.kite.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tmind.kite.utils.DBUtils;

public class StartBodyGuardServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2369276585257232858L;
	
	protected static final Logger logger = Logger.getLogger(StartBodyGuardServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
	    logger.debug("开启服务..........");
	    PrintWriter out = response.getWriter();
	    String telno = (String) request.getSession().getAttribute("telno");
	    String clientType = request.getParameter("clientType");
	    String status = "ERROR";
	    if(telno!=null&&!"".equals(telno)&&clientType!=null&&!"".equals(clientType)){
		    status = startService(telno,clientType);
	    }
	    if(status.equalsIgnoreCase("OK")){
	    	out.write("success");   
		}else if(status.equalsIgnoreCase("TXT_OUT")){
			//短信次数为0:error11
			out.write("error11");
		}else if(status.equalsIgnoreCase("ERROR")){
			//开启服务失败:error6
			out.write("error6");
		}
	    
	}
	
	
	
	@SuppressWarnings("resource")
	private String startService(String telno,String clientType){
		String flag = "OK";
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		
		try {
			String sql = "select id,txt_times from m_user where tel_no=? and active_flag='Y' ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, telno);
			rs = ps.executeQuery();
			
			if(rs.next()){
				
				int userId = rs.getInt("id");
				int times = rs.getInt("txt_times");
				//用户可用短信次数为0
				if(times==0){
					flag = "TXT_OUT";
				}else{
//					sql = "update m_user set service_flag='Y', service_start_time=?, service_start_minute=?, touch_freq=? where tel_no=?";
					
					sql = " insert into web_service_record "
						+ " (user_id,service_start_time,service_start_minute,access_client,service_flag)"
						+ " values(?,?,?,?,?)";
					logger.debug("用户开启服务:"+sql);
					ps = conn.prepareStatement(sql);
					ps.setInt(1, userId);
					ps.setTimestamp(2, new Timestamp((new java.util.Date()).getTime()));
					Calendar calNow=Calendar.getInstance();
					calNow.setTime(new Date());
					ps.setInt(3, calNow.get(Calendar.MINUTE));
					ps.setInt(4, new Integer(clientType));
					ps.setString(5, "Y");
					ps.execute();
					flag = "OK";
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = "ERROR";
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
		return flag;	
	}
	
}
