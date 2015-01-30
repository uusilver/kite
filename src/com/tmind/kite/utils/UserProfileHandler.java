package com.tmind.kite.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.tmind.kite.constants.CommonConstants;
import com.tmind.kite.constants.MessageContent;

public class UserProfileHandler {

	protected static final Logger logger = Logger.getLogger(UserProfileHandler.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap getUserProfile(String telno){
		
		HashMap map = new HashMap();
		
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		
		String sql = " select urgent_name, urgent_telno, service_pwd, security_que,  security_ans "
					+" from m_user where tel_no=?";
		
		logger.debug("查询用户个人设置:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, telno);
			rs = ps.executeQuery();
			String urgentName=null;
			String urgentTelNo=null;
			String servicePwd=null;
			String securityQue=null;
			String securityAns=null;
			if(rs.next()){
				urgentName = rs.getString("urgent_name");
				urgentTelNo = rs.getString("urgent_telno");
				servicePwd =  rs.getString("service_pwd");
				securityQue =  rs.getString("security_que");
				securityAns =  rs.getString("security_ans");
				
				HashMap contentMap = new HashMap();
				contentMap.put("urgent_telno", urgentTelNo);
				contentMap.put("urgent_name", urgentName);
				contentMap.put("service_pwd", servicePwd);
				contentMap.put("security_que", securityQue);
				contentMap.put("security_ans", securityAns);
				
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
}
