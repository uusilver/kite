package com.tmind.kite.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

/**
 * Servlet implementation class Log4jInitServlet
 */
public class Log4jInitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Log4jInitServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * <li>HttpServlet的初始化方法</li> <li>调用该方法来初始化Log4j日志系统</li>
	 */
	public void init() {
		String prefix = getServletContext().getRealPath("/");
		String file = getInitParameter("log4j");
		System.setProperty("webRoot", prefix);
		if (file != null) {
			System.out.println("log4j 初始化成功");
			PropertyConfigurator.configure(prefix + file);
		} else {
			System.out.println("log4j 初始化失败");
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
