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

import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.utils.DBUtils;


public class ValidationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2662511918019956239L;

	protected static final Logger logger = Logger.getLogger(ValidationServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		try {
			HttpSession session = request.getSession(true);  
			// 将内容输出到响应客户端对象的输出流中，生成的图片中包含4个字符
			String randomCodeOnServer = (String) session.getAttribute(CommonConstants.VALIDATE_CODE);
			String randomCode = request.getParameter(CommonConstants.RANDOM_CODE);
			String telNo = request.getParameter(CommonConstants.TEL_NUMBER);
			String errorCode = "";
			//Login用的标示为ForLogin,配置在login.html的JS中
			if(randomCode.equalsIgnoreCase("ForLogin")){
				errorCode = notExistOrLocked(telNo);
				if("notExist".equals(errorCode)){
					//error2:用户不存在
					out.write("error2");
				}else if("locked".equals(errorCode)){
					//error5：用户账号被锁住
					out.write("error5");
				}else{
					out.write("success");
				}
			}else{
				if(!randomCode.equalsIgnoreCase(randomCodeOnServer))
				{
					out.write("error1");
				}else{
					errorCode = notExistOrLocked(telNo);
					if("notExist".equals(errorCode)){
						//error2:用户不存在
						out.write("error2");
					}else if("locked".equals(errorCode)){
						//error5：用户账号被锁住
						out.write("error5");
					}else{
						out.write("success");
					}
				}
			}  
			   
		} catch (Exception e) {

			e.printStackTrace();
		}
		out.flush();
		out.close();

	}
	
	//Todo 从数据库查询手机号码是否存在或者被锁定
	private String notExistOrLocked(String telNo){
		String flag = "notExist";
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "select id,login_err_times,timestampdiff(minute,locked_time,now()) as val from m_user where tel_no=? and active_flag='Y'";
		logger.debug("查询手机号是否存在"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, telNo);
			rs = ps.executeQuery();
			if(rs.next()){
				int loginErrTimes = rs.getInt("login_err_times");
				int interval = rs.getInt("val");
				if(CommonConstants.MAX_LOGIN_ATTEMPT_TIMES.equals(loginErrTimes+"") && CommonConstants.LOGIN_LOCK_TIME>interval){
					flag = "locked";
				}else{
					flag="released";
				}
			}else{
				flag = "notExist";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
		return flag;
	}
}
