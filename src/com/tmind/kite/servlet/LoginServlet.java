package com.tmind.kite.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.DigestHandler;
import com.tmind.kite.utils.LoginHandler;

public class LoginServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8117908425309580896L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response){


		String telno = request.getParameter("telno");
		String password = request.getParameter("password");
		
		System.out.println("用户手机号码："+telno+",密码："+password);
		try {
			HashMap resultMap = LoginHandler.login(telno, password);
			String status = (String)resultMap.get("status");
			if("success".equals(status)){
				response.sendRedirect("main.html");
			}else{
				response.sendRedirect("login.html?1");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//登陆后写入session
		
}


}
