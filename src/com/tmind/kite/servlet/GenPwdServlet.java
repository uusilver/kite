package com.tmind.kite.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.DigestHandler;

public class GenPwdServlet extends HttpServlet{
	/**
	 * 用户输入密码的servlet
	 */
	private static final long serialVersionUID = 5470219676969588707L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		//生成6位随机数字
		String telno = (String) request.getSession().getAttribute("telno");
	    String password = request.getParameter("inputPassword1");
	    System.out.println("获得密码");
	    updateUserPassword(telno,DigestHandler.makeMD5(password));
	    response.sendRedirect("user-profile.html");
	    
	}
	
	private boolean updateUserPassword(String telno, String password){
		System.out.println("用户:"+telno+" 密码被更新");
		boolean flag = false;
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "update m_user set user_pwd=?, active_flag='Y' where tel_no=?";
		System.out.println("用户更新密码:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, password);
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
