package com.tmind.kite.biz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.tmind.kite.model.LongKiteServiceStatusModel;
import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.GetGsonObject;

public class LongKiteServiceOperator {
	
	protected static final Logger logger = Logger.getLogger(LongKiteServiceOperator.class);
	
	public static boolean addLongKiteServicePlan(String aim, String purpose, String estimate_time, String telno){
		Connection conn = null;
    	PreparedStatement ps = null;
    	try{
    		//TODO 未来修改成mysql的全文索引
    		//TODO 限制查询的条数为5条
    		String sql = "insert into long_kite_service_aim (add_date, active_flag, aim, purpose, estimate_time, add_by) values (?,?,?,?,?,?)";
    		//select id, telno_keywords, name_keywords from m_search_info where telno_keywords like '%13851483034%' and active_flag='Y' limit 0, 5
    		logger.info("SQL:"+sql);
    		conn = DBUtils.getConnection();
    		ps = conn.prepareStatement(sql);
    		java.util.Date date=new java.util.Date();
    		Timestamp tt=new Timestamp(date.getTime());
			ps.setTimestamp(1, tt);
			ps.setString(2, "Y");
			ps.setString(3, aim);
			ps.setString(4, purpose);
			ps.setString(5, estimate_time);
			ps.setString(6, telno);
    		ps.execute();
    		return true;
    	}catch(Exception e){
    		logger.warn(e.getMessage());
    		DBUtils.freeConnection(conn, ps, null);
    		return false;
    	}finally{
    		//ResultSet要传出，因此不能在final中关闭，当有异常抛出时才可关闭ResultSet
    		DBUtils.freeConnection(conn, ps, null);
    	}
	}
	
	@SuppressWarnings("resource")
	public static String queryLongKiteServiceInfo(String telno){
		Connection conn = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try{
    		String sql = "select service_flag, touch_freq from m_user where active_flag = 'Y' and tel_no=?";
    		logger.info("SQL:"+sql);
    		conn = DBUtils.getConnection();
    		ps = conn.prepareStatement(sql);
    		ps.setString(1, telno);
    		rs = ps.executeQuery();
			LongKiteServiceStatusModel serviceStatusModel = new LongKiteServiceStatusModel();
    		if(rs.next()){
    			String service_flag = rs.getString("service_flag");
    			String touch_freq = rs.getString("touch_freq");
    			serviceStatusModel.setStatus(service_flag);
    			serviceStatusModel.setTouchFreq(touch_freq);
    			//如果开启了长线风筝服务，加载服务的相关信息，出行目的地，出行的的目的，如朋友聚会，网友约会等，和预测的时间比如2小时，3小时等
    			if(service_flag.equalsIgnoreCase("Y")){
    				sql = "select aim, purpose, estimate_time from long_kite_service_aim where active_flag='Y' and add_by=? order by add_date desc";
    				ps = conn.prepareStatement(sql);
    	    		ps.setString(1, telno);
    	    		rs = ps.executeQuery();
    	    		if(rs.next()){
    	    			serviceStatusModel.setAim(rs.getString("aim"));
    	    			serviceStatusModel.setPurpose(rs.getString("purpse"));
    	    			serviceStatusModel.setEstime_time(rs.getString("estimate_time"));
    	    		}
    			}//end of if
    		}
    		return GetGsonObject.getInstance().toJson(serviceStatusModel);
    	}catch(Exception e){
    		logger.warn(e.getMessage());
    		DBUtils.freeConnection(conn, ps, rs);
    		return null;
    	}finally{
    		DBUtils.freeConnection(conn, ps, null);
    	}
	}
}
