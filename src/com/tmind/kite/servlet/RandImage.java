package com.tmind.kite.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.tmind.kite.biz.RandomGraphic;

public class RandImage extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4898794577181365656L;

	protected static final Logger logger = Logger.getLogger(RandImage.class);

	public RandImage() {
		super();

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		// 设置输出内容为图像，格式为jpeg
		try {
			HttpSession session = req.getSession(true);  
			// 将内容输出到响应客户端对象的输出流中，生成的图片中包含4个字符
			String v = RandomGraphic.createInstance(4).drawAlpha(
					RandomGraphic.GRAPHIC_JPEG, res.getOutputStream(), session);
			// 将字符串的值保留在session中，便于和用户手工输入的验证码比较，比较部分不是本文讨论重点，故略
			logger.debug("验证码的内容是--->" + session.getAttribute("validateCode"));
			res.setContentType("images/jpeg");  
			res.setHeader("Pragma", "No-cache");  
			res.setHeader("Cache-Control", "no-cache");  
			res.setDateHeader("Expires", 0);   
			   
			   
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}