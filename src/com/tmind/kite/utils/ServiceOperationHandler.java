package com.tmind.kite.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;


public class ServiceOperationHandler {
	
	protected static final Logger logger = Logger.getLogger(ServiceOperationHandler.class);
	
	/**
	 * 开启风筝服务
	 * @param telno
	 * @param serviceType  1长线风筝,2短线风筝
	 * @param frenqency 推送消息间隔时间
	 * @param clientType 客户端类型
	 * @return
	 */
	@SuppressWarnings("resource")
	public static String startService(String telno,String serviceType,String frenqency,String clientType){
		String flag = "OK";
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		
		try {
			String sql = "select id,txt_times from m_user where tel_no=? and active_flag='Y' ";
			ps = conn.prepareStatement(sql);
			ps.setString(1, telno);
			rs = ps.executeQuery();
			
			if(rs.next()){
				
				int userId = rs.getInt("id");

				sql = " insert into app_service_record "
					+ " (user_id,service_start_time,service_start_minute,access_client,service_flag,service_type,touch_freq)"
					+ " values(?,?,?,?,?,?,?)";
				logger.debug("用户开启服务:"+sql);
				ps = conn.prepareStatement(sql);
				ps.setInt(1, userId);
				ps.setTimestamp(2, new Timestamp((new java.util.Date()).getTime()));
				Calendar calNow=Calendar.getInstance();
				calNow.setTime(new Date());
				ps.setInt(3, calNow.get(Calendar.MINUTE));
				ps.setInt(4, new Integer(clientType));
				ps.setString(5, "Y");
				ps.setString(6, serviceType);
				ps.setInt(7, new Integer(frenqency));
				ps.execute();
				flag = "OK";
			}else{
				flag = "userNotExist";
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = "ERROR";
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
		return flag;	
	}
	
	/**
	 * 关闭风筝服务
	 * @param telno
	 */
	public static String stopService(String telno){
		String flag = "OK";
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = " update m_user m,app_service_record r"
				   + " set r.service_flag='N', r.service_stop_time=?  "
				   + " where m.id = r.user_id "
				   + " and m.tel_no=?";
		logger.debug("用户关闭服务:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setTimestamp(1, new Timestamp((new java.util.Date()).getTime()));
			ps.setString(2, telno);
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag = "ERROR";
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
		return flag;	
	}
}
