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
import javax.servlet.http.HttpSession;

import com.tmind.kite.utils.DBUtils;


public class ValidationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2662511918019956239L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		try {
			HttpSession session = request.getSession(true);  
			// 将内容输出到响应客户端对象的输出流中，生成的图片中包含4个字符
			String randomCodeOnServer = (String) session.getAttribute("validateCode");
			String randomCode = request.getParameter("randomCode");
			String telNo = request.getParameter("telno");
			//Login用的标示为ForLogin,配置在login.html的JS中
			if(randomCode.equalsIgnoreCase("ForLogin")){
				if(notExistTelno(telNo)){
					out.write("error2");
				}else{
					out.write("success");
				}
			}else{
				if(!randomCode.equalsIgnoreCase(randomCodeOnServer))
				{
					out.write("error1");
				}else if(notExistTelno(telNo)){
					out.write("success"); 
				}else{
					out.write("error2");
				}
			}  
			   
		} catch (Exception e) {

			e.printStackTrace();
		}
		out.flush();
		out.close();

	}
	
	//Todo 从数据库查询手机号码是否存在
	private boolean notExistTelno(String telNo){
		boolean flag = false;
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "select id from m_user where tel_no=? and active_flag='Y'";
		System.out.println("查询手机号是否存在"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, telNo);
			rs = ps.executeQuery();
			if(rs.next())
				flag = false;
			else
				flag = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
		
		
		return flag;
	}
}
