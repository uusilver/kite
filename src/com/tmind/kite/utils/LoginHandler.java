package com.tmind.kite.utils;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.model.User;

public class LoginHandler {
	
	protected static final Logger logger = Logger.getLogger(LoginHandler.class);

	private static final String QUERY_SQL = "select id,user_pwd,login_err_times,locked_time from m_user where tel_no=? and active_flag='Y'";
	
	private static final String UPDATE_TIMES_SQL = "update m_user set login_err_times=? where id=?";
	
	private static final String UPDATE_TIMES_DATETIME_SQL = "update m_user set login_err_times=?,locked_time=? where id=?";
	
	private static final String UPDATE_LOGIN_SUCCESS_SQL = "update m_user set login_err_times=?,locked_time=?,login_flag=?,access_client=? where id=?";
	
	private static final String UPDATE_LOGIN_FLAG_SQL = "update m_user set login_flag=? where id=?";
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap login(String teleNo,String password,String clientType) {
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
			pwd = new String(Base64.decodeBase64(password.getBytes()),CommonConstants.CHARSETNAME_UTF_8);
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
			String login_err_times = null;
			Date lockTime = null;
			while(rs.next()){
				id = (new Integer(rs.getInt("Id"))).toString();
				user_pwd = rs.getString("user_pwd");
				login_err_times = (new Integer(rs.getInt("login_err_times"))).toString();
				lockTime = rs.getTimestamp("locked_time");

				logger.debug("获取用户信息,[User ID:"+id+",TelNo:"+teleNo+"]");
				
				User user = new User();
				user.setId(id);
				user.setTelNo(teleNo);
				user.setUserPwd(user_pwd);
				user.setTxtTimes(login_err_times);
				
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
				if(lockTime!=null&&!isReleased(lockTime,CommonConstants.LOGIN_LOCK_TIME)){
					resultCode = CommonConstants.MSG_CODE_REST_LOGIN_LOCKED_USER;
				}
				//如果密码匹配，则登陆成功，登陆失败次数更新为0
				else if(DigestHandler.makeMD5(pwd).equals(userPwd)){
					resultCode = CommonConstants.MSG_CODE_REST_LOGIN_SUCCESS;
					updPs = conn.prepareStatement(UPDATE_LOGIN_SUCCESS_SQL);
					errTimes = 0;
					
					//用户登录失败次数更新为0
					updPs.setInt(1, errTimes);
					
					//用户账号锁定时间点更新为空
					updPs.setDate(2, null);
					
					//用户登录状态更新为1，即已登录
					updPs.setInt(3, 1);
					
					//用户访问入口
					updPs.setInt(4, new Integer(clientType));
					updPs.setInt(5, userId);
					updPs.executeUpdate();
					updPs.close();
					map.put(CommonConstants.LOGIN_USER_OBJECT, user);
				}
				//用户密码不正确，失败计数累计
				else{
					resultCode = CommonConstants.MSG_CODE_REST_LOGIN_INCORRECT_PWD;
					errTimes +=1;
					if(errTimes==new Integer(CommonConstants.MAX_LOGIN_ATTEMPT_TIMES)){
						updPs = conn.prepareStatement(UPDATE_TIMES_DATETIME_SQL);
						updPs.setInt(1, errTimes);
						updPs.setTimestamp(2, new Timestamp((new java.util.Date()).getTime()));
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
	
	public static boolean logoutInDBLevel(int flag,int id){
		
		int exeResult = 0;
		boolean returnValue = false;
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		
		logger.debug("更新用户登录状态位,[SQL:"+UPDATE_LOGIN_FLAG_SQL+",flag="+flag+",id="+id+"]");
		try {
			ps = conn.prepareStatement(UPDATE_LOGIN_FLAG_SQL);
			ps.setInt(1, flag);
			ps.setInt(2, id);
			exeResult = ps.executeUpdate();
			if(exeResult>0){
				returnValue = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.info("[UserID:"+id+"]-更新用户登陆状态失败，异常信息如下：");
			logger.info(e.getMessage());
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
		return returnValue;
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
