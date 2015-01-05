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
public class ChekInUserProfileServlet extends HttpServlet{
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		String telno = (String) request.getSession().getAttribute("telno");
	    String urgentName = request.getParameter("urgentName");
	    String urgentTelno = request.getParameter("urgentTelno");
	    String touchFrequency = request.getParameter("touchFrequency");
	    System.out.println(urgentName+":"+urgentTelno+":"+touchFrequency);
	    if(checkInUserProfile(telno, urgentName,urgentTelno,touchFrequency)){
			//更新用户个人信息失败:error5
	    	out.write("success"); 
		}else{
			out.write("error5");
		}
	    
	}
	
	public boolean checkInUserProfile(String telno, String urgentName, String urgentTelno, String touchFrequency){
		System.out.println("用户:"+telno);
		System.out.println("紧急联系人姓名:"+urgentName);
		System.out.println("紧急联系人电话:"+urgentTelno);
		System.out.println("信息发送频率:"+touchFrequency);
		boolean flag = false;
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "update m_user set urgent_name=?, urgent_telno=?, touch_freq=? where tel_no=?";
		System.out.println("用户设置更新:"+sql);
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
