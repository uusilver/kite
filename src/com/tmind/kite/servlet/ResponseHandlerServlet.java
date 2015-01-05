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

import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.DigestHandler;

public class ResponseHandlerServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4285028177905885896L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		//生成6位随机数字
		String urlContent = request.getParameter("c");
		try {
			String realContent = DigestHandler.decryptBASE64(urlContent);
			String telno = realContent.split("@")[0];
			String key = realContent.split("@")[1];
			response.setHeader("Content-type", "text/html;charset=UTF-8");  
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			if(updateUserRespFlag(telno)&&checkKey(key)){
				out.write("状态确认成功，祝您一路平安!请于到达目的地后关闭系统!");
			}else{
				out.write("信息过期!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	  private boolean updateUserRespFlag(String telno){
		    boolean flag = false;
			Connection conn  = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			conn = DBUtils.getConnection();
			String sql = "update m_user set resp_flag='Y', resp_time=? where tel_no=?";
			System.out.println("用户更新安全信息服务:"+sql);
			try {
				ps = conn.prepareStatement(sql);
				ps.setTimestamp(1, new Timestamp((new java.util.Date()).getTime()));
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
	  
	  private boolean checkKey(String key){
		  boolean flag = false;
			Connection conn  = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			conn = DBUtils.getConnection();
			String sql = "select active_flag from m_task_key where key_val=?";
			System.out.println("用户任务代码表:"+sql);
			try {
				ps = conn.prepareStatement(sql);
				ps.setString(1, key);
				rs = ps.executeQuery();
				if(rs.next()&&rs.getString("active_flag").equalsIgnoreCase("N"))
					flag = true;
				//更新key状态
				sql = "update m_task_key set active_flag='Y' where key_val=?";
				System.out.println("用户更新代码表:"+sql);
				ps = conn.prepareStatement(sql);
				ps.setString(1, key);
				ps.execute();
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
