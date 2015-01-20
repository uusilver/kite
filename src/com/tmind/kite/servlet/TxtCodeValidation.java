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

import com.tmind.kite.utils.DBUtils;

public class TxtCodeValidation extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2534001044148977994L;
	
	protected static final Logger logger = Logger.getLogger(TxtCodeValidation.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		// 设置输出内容为图像，格式为jpeg
		try {
			HttpSession session = request.getSession(true);  
			String telno = (String) session.getAttribute("telno");
			// 将内容输出到响应客户端对象的输出流中，生成的图片中包含4个字符
			String txtCode = request.getParameter("txtCode");
			String codeType = request.getParameter("codeType");
			//验证类型0是用户，1是紧急联系人
			logger.debug("短信验证类型为:"+codeType);
			if(validateTxtCode(telno,txtCode,codeType)){
				out.write("success");
			}else{
				//短信校验错误返回error3
				out.write("error3");
			}
			   
		} catch (Exception e) {

			e.printStackTrace();
		}
		out.flush();
		out.close();

	}
	
	//Todo 校验短信随机码
	private boolean validateTxtCode(String telno, String txtCode, String codeType){
		logger.debug("随机码校验:"+txtCode+"电话为:"+telno);
		
		boolean flag = false;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "select id,random_num from m_random_num where tel_no=? and type_t=? and random_num=? and active_flag='N'";
		logger.debug("校验随机码:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, telno);
			ps.setString(2, codeType);
			ps.setString(3, txtCode);
			rs = ps.executeQuery();
			if(rs.next()){
				if(txtCode.equalsIgnoreCase(rs.getString("random_num"))){
					sql = "update m_random_num set active_flag='Y' where id=?";
					ps = conn.prepareStatement(sql);
					ps.setInt(1, rs.getInt("id"));
					ps.execute();
					flag = true;
				}else{
					flag = false;
				}
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
