package com.tmind.kite.servlet;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2038345694885720544L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response){

		
		request.getSession().removeAttribute("telno");
		System.out.println("用户登出");
}
}
