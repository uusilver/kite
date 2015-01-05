package com.tmind.kite.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.DigestHandler;

public class LoginServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8117908425309580896L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response){


		String telno = request.getParameter("telno");
		String password = request.getParameter("password");
		
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "select id from m_user where tel_no=? and user_pwd=?";
		
		System.out.println("查询手机号是否存在"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, telno);
			ps.setString(2, DigestHandler.makeMD5(password));
			rs = ps.executeQuery();
			if(rs.next())
				{
					request.getSession().setAttribute("telno", telno);
					System.out.println("用户登陆系统:"+telno);
					try {
						response.sendRedirect("main.html");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else{
					try {
						response.sendRedirect("login.html?1");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
		//登陆后写入session
		
}


}
