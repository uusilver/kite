package com.tmind.kite.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tmind.kite.model.User;

public class WSUserValidator {

	@SuppressWarnings("null")
	public static boolean validateUser(String telno, String password){
		//TODO 建立用户缓存
		//TODO 验证用户的合法性
		User user = (User) EhcacheUtil.getInstance().get("kiteCache", telno);
		if(user==null){
			Connection conn = null;
	    	PreparedStatement ps = null;
	    	ResultSet rs = null;
	    	try{
	    		//TODO 未来修改成mysql的全文索引
	    		//TODO 限制查询的条数为5条
	    		String sql = "select user_pwd from m_user where tel_no=?";
	    		conn = DBUtils.getConnection();
	    		ps = conn.prepareStatement(sql);
	    		ps.setString(1, telno);
	    		if(rs.next()){
	    			user = new User();
	    			user.setTelNo(telno);
	    			user.setUserPwd(rs.getString("user_pwd"));
	    			EhcacheUtil.getInstance().put("kiteCache", telno, user);
	    		}
	    		ps.execute();
	    	}catch(Exception e){
	    		DBUtils.freeConnection(conn, ps, rs);
	    	}finally{
	    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
	    		DBUtils.freeConnection(conn, ps, rs);
	    	}
		}
		if(user.getUserPwd().equalsIgnoreCase(password)){
			return true;
		}else{
			return false;
		}
	}//end of method
	
}
