package com.tmind.kite.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.DigestHandler;


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
			String pwd = request.getParameter(CommonConstants.USER_PASSWORD);
			String errorCode = "";
			//Login用的标示为ForLogin,配置在login.html的JS中
			if(randomCode.equalsIgnoreCase("ForLogin")){
				errorCode = notExistOrLocked(telNo,pwd);
				if("notExist".equals(errorCode)){
					//error2:用户不存在
					out.write("error2");
				}else if("locked".equals(errorCode)){
					//error5：用户账号被锁住
					out.write("error5");
				}else if(errorCode.startsWith("incorrectPwd")){
					//用户密码错误
					String leftAttampts = errorCode.substring(errorCode.indexOf(":"));
					out.write("error6:"+leftAttampts);
				}else{
					out.write("success");
				}
			}else{
				if(!randomCode.equalsIgnoreCase(randomCodeOnServer))
				{
					out.write("error1");
				}else{
					errorCode = notExistOrLocked(telNo,pwd);
					if("notExist".equals(errorCode)){
						//error2:用户不存在
						out.write("error2");
					}else if("locked".equals(errorCode)){
						//error5：用户账号被锁住
						out.write("error5");
					}else if(errorCode.startsWith("incorrectPwd")){
						//用户密码错误
						String leftAttampts = errorCode.substring(errorCode.indexOf(":"));
						out.write("error6:"+leftAttampts);
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
	private String notExistOrLocked(String telNo,String password){
		String flag = "notExist";
		Connection conn  = null;
		PreparedStatement ps = null;
		PreparedStatement updPs = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "select id,user_pwd,login_err_times,timestampdiff(minute,locked_time,now()) as val from m_user where tel_no=? and active_flag='Y'";

		
		String UPDATE_TIMES_SQL = "update m_user set login_err_times=? where tel_no=?";
		
		String UPDATE_TIMES_DATETIME_SQL = "update m_user set login_err_times=?,locked_time=? where tel_no=?";
		
		logger.debug("查询手机号是否存在"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, telNo);
			rs = ps.executeQuery();
			if(rs.next()){
				int loginErrTimes = rs.getInt("login_err_times");
				int interval = rs.getInt("val");
				String pwd = rs.getString("user_pwd");
				if(CommonConstants.MAX_LOGIN_ATTEMPT_TIMES.equals(loginErrTimes+"") && CommonConstants.LOGIN_LOCK_TIME>interval){
					flag = "locked";
				}else if(!pwd.equals(DigestHandler.makeMD5(password))){
					flag="incorrectPwd";
					
					//纪录用户密码输入错误次数
					loginErrTimes +=1;
					if(loginErrTimes==new Integer(CommonConstants.MAX_LOGIN_ATTEMPT_TIMES)){
						updPs = conn.prepareStatement(UPDATE_TIMES_DATETIME_SQL);
						updPs.setInt(1, loginErrTimes);
						updPs.setTimestamp(2, new Timestamp((new java.util.Date()).getTime()));
						updPs.setString(3, telNo);
						flag+=":0";
					}else{
						updPs = conn.prepareStatement(UPDATE_TIMES_SQL);
						if(loginErrTimes>new Integer(CommonConstants.MAX_LOGIN_ATTEMPT_TIMES)){
							loginErrTimes = 1;
						}
						updPs.setInt(1, loginErrTimes);
						updPs.setString(2, telNo);
						flag+=":"+(new Integer(CommonConstants.MAX_LOGIN_ATTEMPT_TIMES)-loginErrTimes);
					}
					updPs.executeUpdate();
					updPs.close();
				}else{
					flag = "success";
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
