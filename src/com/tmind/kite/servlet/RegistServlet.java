package com.tmind.kite.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tmind.kite.utils.DBUtils;


public class RegistServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8845479346221043741L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response){

			try {
				request.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String username = request.getParameter("username");
			System.out.println("用户名:"+username);
			String telno = request.getParameter("telno");
			//电话写入session
			HttpSession session = request.getSession();
			session.setAttribute("telno", telno);
			if(insertUserIntoDb(username,telno)){
				try {
					response.sendRedirect("gen-pwd.html");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	}
	
	//Todo 存数据库
	private boolean insertUserIntoDb(String username, String telno){
		boolean flag = false;
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		//默认15分钟后如果没有回信则联系紧急联系人
		String sql = "insert into m_user (user_name,reg_date,tel_no,active_flag,standard_check_time,txt_times) "
				+ " values (?,?,?,?,?,?)";
		System.out.println("用户注册:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
			ps.setString(3, telno);
			ps.setString(4, "N");
			ps.setInt(5, 15);
			ps.setInt(6, 20);
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
