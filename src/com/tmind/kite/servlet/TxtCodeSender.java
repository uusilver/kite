package com.tmind.kite.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.model.User;
import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.SessionUtils;
import com.tmind.kite.utils.TxtHandler;

public class TxtCodeSender extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3426443876978110228L;
	
	protected static final Logger logger = Logger.getLogger(TxtCodeSender.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		//1.从session中获取用户信息
		User user = (User)SessionUtils.getObjectAttribute(request, CommonConstants.USER_LOGIN_TOKEN);
		
		//2.获得session里的电话号码
		String telno = user.getTelNo();
		PrintWriter out = response.getWriter();
		//生成6位随机数字
		String txtCode = "";
		 while(txtCode.length()<6)
			 txtCode+=(int)(Math.random()*10);
		try {
			String codeType = request.getParameter(CommonConstants.SMS_CODE_TYPE);
			//验证类型0是用户，1是紧急联系人
			logger.debug("短信验证类型为:"+codeType);
			if(insertRandomCode(telno,codeType,txtCode))
				if(TxtHandler.sendTxt(telno, txtCode)){
					out.write("success");   
				}else{
					//发送失败:error4
					out.write("error4");
				}
			   
		} catch (Exception e) {

			e.printStackTrace();
		}
		out.flush();
		out.close();

	}
	
	private boolean insertRandomCode(String telno, String codeType, String txtCode){
		boolean flag = false;
		Connection conn = null;
		PreparedStatement ps = null;
		
		conn = DBUtils.getConnection();
		String sql = "insert into m_random_num (tel_no,type_t,random_num,active_flag) values (?,?,?,'N')";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, telno);
			ps.setString(2, codeType);
			ps.setString(3, txtCode);
			ps.execute();
			flag = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtils.freeConnection(conn, ps, null);
		}
		
		return flag;
		
	}
}
