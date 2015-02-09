package com.tmind.kite.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.constants.MessageContent;

public class UserProfileHandler {

	protected static final Logger logger = Logger.getLogger(UserProfileHandler.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap getUrgentUserInfo(String telno){
		
		HashMap map = new HashMap();
		
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		
		String sql = " select urgent_name, urgent_telno "
					+" from m_user where tel_no=?";
		
		logger.debug("查询用户个人设置:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, telno);
			rs = ps.executeQuery();
			String urgentName=null;
			String urgentTelNo=null;
			if(rs.next()){
				urgentName = rs.getString("urgent_name");
				urgentTelNo = rs.getString("urgent_telno");
				
				HashMap contentMap = new HashMap();
				contentMap.put("urgent_telno", urgentTelNo);
				contentMap.put("urgent_name", urgentName);
				
				map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_GET_PROFILE_SUCCESS);
				map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, contentMap);
			}else{
				
				map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_GET_EMPTY_PROFILE);
				map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_USER_PROFILE_EMPTY);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
		return map;
	}
	
	/**
	 * 保存用户个人设置信息
	 * @param telno
	 * @param urgentName
	 * @param urgentTelNo
	 * @param servicePwd
	 * @param securityQue
	 * @param securityAns
	 * @param clientType
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap saveUserProfile(String telno,String urgentName,String urgentTelNo,
			String servicePwd,String securityQue, String securityAns, String clientType){
			
			HashMap map = new HashMap();
			HashMap params = new LinkedHashMap(); 
			Connection conn  = null;
			PreparedStatement ps = null;
			int index = 1;
			int record = 0;
			conn = DBUtils.getConnection();
			
			String sql = " update m_user set ";
			
			if(urgentName!=null){
				sql += "urgent_name=?,";
				params.put(index, urgentName);
				index++;
			}
			
			if(urgentTelNo!=null&&!"".equals(urgentTelNo)){
				sql += "urgent_telno=?,";
				params.put(index, urgentTelNo);
				index++;
			}
			
			if(servicePwd!=null&&!"".equals(servicePwd)){
				sql += "service_pwd=?,";
				params.put(index, servicePwd);
				index++;
			}
			
			if(securityQue!=null&&!"".equals(securityQue)){
				sql += "security_que=?,";
				params.put(index, securityQue);
				index++;
			}
			
			if(securityAns!=null&&!"".equals(securityAns)){
				sql += "security_ans=?,";
				params.put(index, securityAns);
				index++;
			}
			
			if(sql.lastIndexOf(",")!=-1){
				sql = sql.substring(0,sql.length()-1);
			}
			
			sql += " where tel_no=? ";
			
			logger.debug("查询用户个人设置:"+sql);
			try {
				ps = conn.prepareStatement(sql);
				
				if(!params.isEmpty()){
					for(int i = 0 ; i<params.size(); i++){
						ps.setString(i+1, (String)params.get(i+1));
					}
				}
				ps.setString(params.size()+1, telno);
				
				record = ps.executeUpdate();
				if(record==1){
					map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_SAVE_PROFILE_SUCCESS);
				}else{
					map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_SAVE_PROFILE_FAILED);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info(e.getMessage());
				map.put(CommonConstants.REST_MSG_FORMAT_STATUS, CommonConstants.MSG_CODE_REST_SAVE_PROFILE_FAILED);
				map.put(CommonConstants.REST_MSG_FORMAT_MSG_CONTENT, MessageContent.MSG_DATABASE_EXECUTE_EXCEPTION);
			}finally{
				DBUtils.freeConnection(conn, ps, null);
			}
			return map;
		}
}
