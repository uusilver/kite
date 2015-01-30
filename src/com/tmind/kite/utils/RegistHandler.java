package com.tmind.kite.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;


public class RegistHandler {

	protected static final Logger logger = Logger.getLogger(RegistHandler.class);
	
	
	/**
	 * 保存用户手机号码和登录密码到数据库
	 * @param userName
	 * @param telno
	 * @param password
	 * @return
	 */
	public static HashMap<String,String> saveUserTelAndPwd(String userName, String telno, String password, String clientType){

		HashMap<String,String> map = new HashMap<String,String>();
		Connection conn  = null;
		PreparedStatement ps_check = null;
		PreparedStatement ps_insert = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		
		//查询用户是否存在SQL
		String check_sql = "select * from m_user where tel_no=? ";
		
		//保存用户信息SQL
		String insert_sql = "insert into m_user (user_name,tel_no,user_pwd,reg_date,reg_from,active_flag,standard_check_time,txt_times) "
				+ " values (?,?,?,?,?,?,?,?)";
		
		try {

			logger.debug("用户注册:"+check_sql);
			//检查用户是否已经存在
			ps_check = conn.prepareStatement(check_sql);
			ps_check.setString(1, telno);
			rs = ps_check.executeQuery();
			if(rs.next()){
				logger.info("用户["+telno+"]已经存在");
				map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_REGIST_TELNO_EXIST);
				rs.close();
				ps_check.close();
				return map;
			}
			
			logger.debug("用户注册:"+insert_sql);
			ps_insert = conn.prepareStatement(insert_sql);
			ps_insert.setString(1, userName);
			ps_insert.setString(2, telno);
			ps_insert.setString(3, password);
			ps_insert.setDate(4, new java.sql.Date(new java.util.Date().getTime()));
			ps_insert.setString(5, clientType);
			ps_insert.setString(6, "Y");
			ps_insert.setInt(7, CommonConstants.MAX_STANDARD_CHECK_TIME);
			ps_insert.setInt(8, CommonConstants.MAX_DEFAULT_SMS_COUNTS);
			ps_insert.execute();
			
			logger.info("用户["+telno+"]保存成功");
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_REGIST_SUCCESS);
			
		} catch (SQLException e) {
			logger.info("用户["+telno+"]保存成功");
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_REGIST_DB_EXCEPTION);
			
			e.printStackTrace();
		}finally{
			DBUtils.freeConnection(conn, ps_insert, rs);
		}
		
		return map;
	}
	
	
	/**
	 * 保存用户服务密码、安全问题和回复答案到数据库
	 * @param service_pwd
	 * @param security_que
	 * @param security_ans
	 * @return
	 */
	public static HashMap<String,String> saveServicePwdAndQA(String telno, String service_pwd, String security_que, String security_ans){

		HashMap<String,String> map = new HashMap<String,String>();
		Connection conn  = null;
		PreparedStatement ps = null;
		conn = DBUtils.getConnection();
		//
		String sql = "update m_user set service_pwd=?,security_que=?,security_ans=? "
				+ " where tel_no=? ";
		logger.debug("用户设置服务密码和安全问题:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, service_pwd);
			ps.setString(2, security_que);
			ps.setString(3, security_ans);
			ps.setString(4, telno);
			ps.executeUpdate();
			
			logger.info("保存服务密码和安全问题成功");
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_REGIST_SUCCESS);
			
		} catch (SQLException e) {
			logger.info("保存服务密码和安全问题失败");
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_REGIST_DB_EXCEPTION);
			e.printStackTrace();
		}finally{
			DBUtils.freeConnection(conn, ps, null);
		}
		return map;
	}
}
