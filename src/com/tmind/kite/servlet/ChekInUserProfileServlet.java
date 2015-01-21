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

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.model.User;
import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.SessionUtils;

@SuppressWarnings("serial")
public class ChekInUserProfileServlet extends HttpServlet{
	
	protected static final Logger logger = Logger.getLogger(ChekInUserProfileServlet.class);
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		//1.从session中获取用户信息
		User user = (User)SessionUtils.getObjectAttribute(request, CommonConstants.USER_LOGIN_TOKEN);
		String telno = user.getTelNo();
		
	    String urgentName = request.getParameter("urgentName");
	    String urgentTelno = request.getParameter("urgentTelno");
	    String touchFrequency = request.getParameter("touchFrequency");
	    logger.debug(urgentName+":"+urgentTelno+":"+touchFrequency);
	    if(checkInUserProfile(telno, urgentName,urgentTelno,touchFrequency)){
			//更新用户个人信息失败:error5
	    	out.write("success"); 
		}else{
			out.write("error5");
		}
	    
	}
	
	public boolean checkInUserProfile(String telno, String urgentName, String urgentTelno, String touchFrequency){
		logger.debug("用户:"+telno);
		logger.debug("紧急联系人姓名:"+urgentName);
		logger.debug("紧急联系人电话:"+urgentTelno);
		logger.debug("信息发送频率:"+touchFrequency);
		boolean flag = false;
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "update m_user set urgent_name=?, urgent_telno=?, touch_freq=? where tel_no=?";
		logger.debug("用户设置更新:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, urgentName);
			ps.setString(2, urgentTelno);
			ps.setString(3, touchFrequency);
			ps.setString(4, telno);
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
