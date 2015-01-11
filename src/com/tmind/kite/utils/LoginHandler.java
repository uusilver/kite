package com.tmind.kite.utils;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.model.User;

public class LoginHandler {
	
	protected static final Logger logger = Logger.getLogger(LoginHandler.class);

	private static final String QUERY_SQL = "select id,user_pwd,txt_times,locked_time from m_user where tel_no=? and status='A'";
	
	private static final String UPDATE_TIMES_SQL = "update m_user set txt_times=? where id=?";
	
	private static final String UPDATE_TIMES_DATETIME_SQL = "update m_user set txt_times=?,locked_time=? where id=?";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap login(String teleNo,String password) {
		HashMap map = new HashMap();
		String resultCode = "";
		
		if(teleNo==null||"".equals(teleNo)){
			logger.info("用户名为空");
			resultCode = CommonConstants.MSG_CODE_REST_LOGIN_NULL_USERID;
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, resultCode);
			map.put(CommonConstants.REST_MSG_FORMAT_TIMES, "0");
			return map;
		}
		
		if(password==null||"".equals(password)){
			logger.info("密码为空");
			resultCode = CommonConstants.MSG_CODE_REST_LOGIN_NULL_PWD;
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, resultCode);
			map.put(CommonConstants.REST_MSG_FORMAT_TIMES, "0");
			return map;
		}
		
		String pwd = null;
		try {
			//Base64解密
			pwd = new String(Base64.getDecoder().decode(password.getBytes()),CommonConstants.CHARSETNAME_UTF_8);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			logger.info("用户密码解密失败，异常信息如下：");
			logger.info(e1.getMessage());
			resultCode = CommonConstants.MSG_CODE_REST_LOGIN_DECODE_PWD_ERR;
			map.put(CommonConstants.REST_MSG_FORMAT_STATUS, resultCode);
			map.put(CommonConstants.REST_MSG_FORMAT_TIMES, "0");
			return map;
		}
		
		Connection conn  = null;
		PreparedStatement ps = null;
		PreparedStatement updPs = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		
		logger.debug("查询手机号是否存在,[SQL:"+QUERY_SQL+"]");
		try {
			ps = conn.prepareStatement(QUERY_SQL);
			ps.setString(1, teleNo);
			//ps.setString(2, DigestHandler.makeMD5(password));
			rs = ps.executeQuery();
			List userList = new ArrayList();
			String id = null;
			String user_pwd = null;
			String txt_times = null;
			Date lockTime = null;
			while(rs.next()){
				id = (new Integer(rs.getInt("Id"))).toString();
				user_pwd = rs.getString("user_pwd");
				txt_times = (new Integer(rs.getInt("txt_times"))).toString();
				lockTime = rs.getDate("locked_time");

				logger.debug("获取用户信息,[User ID:"+id+",TelNo:"+teleNo+"]");
				
				User user = new User();
				user.setId(id);
				user.setUserPwd(user_pwd);
				user.setTxtTimes(txt_times);
				
				userList.add(user);
			}
			
			//用户信息不存在
			if(userList.size()==0){
				resultCode = CommonConstants.MSG_CODE_REST_LOGIN_NO_USER;
				map.put(CommonConstants.REST_MSG_FORMAT_STATUS, resultCode);
				map.put(CommonConstants.REST_MSG_FORMAT_TIMES, "0");
				return map;
			}
			
			//存在多个用户信息
			if(userList.size()>1){
				resultCode = CommonConstants.MSG_CODE_REST_LOGIN_MULTIPLE_USER;
				map.put(CommonConstants.REST_MSG_FORMAT_STATUS, resultCode);
				map.put(CommonConstants.REST_MSG_FORMAT_TIMES, "0");
				return map;
			}
			
			//用户信息存在一条，需要校验用户密码是否正确
			if(userList.size()==1){
				User user = (User)userList.get(0);
				String userPwd = user.getUserPwd();
				int errTimes = new Integer(user.getTxtTimes());
				int userId = new Integer(user.getId());
				
				//用户账号已经被锁定,并且还没有超过20分钟的锁定时长
				if(!isReleased(lockTime,CommonConstants.LOGIN_LOCK_TIME)){
					resultCode = CommonConstants.MSG_CODE_REST_LOGIN_LOCKED_USER;
				}
				//如果密码匹配，则登陆成功，登陆失败次数更新为0
				else if(DigestHandler.makeMD5(pwd).equals(userPwd)){
					resultCode = CommonConstants.MSG_CODE_REST_LOGIN_SUCCESS;
					updPs = conn.prepareStatement(UPDATE_TIMES_DATETIME_SQL);
					errTimes = 0;
					updPs.setInt(1, errTimes);
					updPs.setDate(2, null);
					updPs.setInt(3, userId);
					updPs.executeUpdate();
					updPs.close();
				}
				//用户密码不正确，失败计数累计
				else{
					resultCode = CommonConstants.MSG_CODE_REST_LOGIN_INCORRECT_PWD;
					errTimes +=1;
					if(errTimes==new Integer(CommonConstants.MAX_LOGIN_ATTEMPT_TIMES)){
						updPs = conn.prepareStatement(UPDATE_TIMES_DATETIME_SQL);
						updPs.setInt(1, errTimes);
						updPs.setDate(2, (java.sql.Date)DateUtils.convertString2Date(DateUtils.getChar14()));
						updPs.setInt(3, userId);
					}else{
						updPs = conn.prepareStatement(UPDATE_TIMES_SQL);
						if(errTimes>new Integer(CommonConstants.MAX_LOGIN_ATTEMPT_TIMES)){
							errTimes = 1;
						}
						updPs.setInt(1, errTimes);
						updPs.setInt(2, userId);
					}
					updPs.executeUpdate();
					updPs.close();
				}
				map.put(CommonConstants.REST_MSG_FORMAT_STATUS, resultCode);
				map.put(CommonConstants.REST_MSG_FORMAT_TIMES, errTimes);
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.info("[UserID:"+teleNo+"]-查询登陆用户信息失败，异常信息如下：");
			logger.info(e.getMessage());
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
		return map;
	}
	
	/**
	 * 和当前时间进行比较，如果时差超过指定的时长，则返回true，否则返回false
	 * @param dateTime
	 * @param timeRange
	 * @return
	 */
	private static boolean isReleased(Date dateTime,int timeRange){
		
		boolean value = false;
		
		Date currentTime = DateUtils.convertString2Date(DateUtils.getChar14());
		
		long diff = currentTime.getTime() - dateTime.getTime();
		
		long minutes = (diff/(60*1000));
		
		if(minutes>timeRange){
			value = true;
		}
		return value;
	}
}
