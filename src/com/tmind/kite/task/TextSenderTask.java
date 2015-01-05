package com.tmind.kite.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimerTask;

import com.tmind.kite.model.TxtMsgModel;
import com.tmind.kite.servlet.DaemonPosterServlet;
import com.tmind.kite.utils.DBUtils;
import com.tmind.kite.utils.DigestHandler;

public class TextSenderTask extends TimerTask {

  public void run() {
    // System.out.println("call at " + (new Date()));
    // TODO 此处添加具体任务代码
	try {
		checkUserStatus();
		checkUserRespAndSendToUrgentNameIfNecess();
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  
	  
  }
  
  private void checkUserStatus() throws Exception{
	  Connection conn = null;
	  PreparedStatement ps = null;
	  ResultSet rs = null;
	  String sql = "select * from m_user where active_flag='Y' and service_flag='Y' and resp_flag='Y' and txt_times>0";
	  conn = DBUtils.getConnection();
	  try {
		rs = conn.prepareStatement(sql).executeQuery();
		while(rs.next()){
			  TxtMsgModel txtModel = new TxtMsgModel();
			  //String str = "新加短信-->"+System.currentTimeMillis();
			  int touchFrequency = rs.getInt("touch_freq");
			  System.out.println("设置的时间间隔分钟:"+touchFrequency);
			  
			  Calendar calNow=Calendar.getInstance();
			  calNow.setTime(new Date());
			  
			  int minutesInDb = rs.getInt("remarks1");
			  
			  int minutesDiffer = calNow.get(Calendar.MINUTE) - minutesInDb;
			  System.out.println("系统相差分钟为:"+minutesDiffer);
			  if(minutesDiffer%touchFrequency==0){
				  String telno = rs.getString("tel_no");
				  //更新用户的应答状态为N,成功则继续
				  if(updateUserRespFlag(telno)){
					  txtModel.setTelno(telno);
					  String content = "您好,风筝网现与您确认您的安全,请点击安全连接  ";
					  content += "http://127.0.0.1:9090/kite/resp?c=";
					  String encrypContent = telno+"@"+generateRandomKey();
					  content += DigestHandler.encryptBASE64(encrypContent.getBytes());
					  txtModel.setContent(content);
					  System.out.println("校验短信发送:"+txtModel);
					  DaemonPosterServlet.addIntoQueue(txtModel);
				  }
			  }
			  
		  }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		DBUtils.freeConnection(conn, ps, rs);
	}
  }
  
  
  //用户没有在规定的时间内应答，则发送信息到紧急联系人
  private void checkUserRespAndSendToUrgentNameIfNecess() throws Exception{
	  Connection conn = null;
	  PreparedStatement ps = null;
	  ResultSet rs = null;
	  String sql = "select * from m_user where (timestampdiff(minute,resp_time,now())>standard_check_time)>0 and active_flag='Y' and service_flag='Y' and resp_flag='N' and txt_times>0;";
	  System.out.println("紧急联系人查询:"+sql);
	  conn = DBUtils.getConnection();
	  try {
		rs = conn.prepareStatement(sql).executeQuery();
		while(rs.next()){
			String telno = rs.getString("tel_no");
			if(updateUserRespFlag(telno)){
				TxtMsgModel txtModel = new TxtMsgModel();
				txtModel.setTelno(rs.getString("urgent_telno"));
				String content = "您好:"+rs.getString("user_name")+"的用户在安全时间内未与系统联系，请您注意!点击安全连接关闭消息 ";
				content += "http://127.0.0.1:9090/kite/resp?c=";
				String encrypContent = telno+"@"+generateRandomKey();
				content += DigestHandler.encryptBASE64(encrypContent.getBytes());
				txtModel.setContent(content);
				System.out.println("发送至紧急联系人:"+txtModel);
				DaemonPosterServlet.addIntoQueue(txtModel);
			}
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		DBUtils.freeConnection(conn, ps, rs);
	}
	  
  }
  
  //更新用户应答状态
  private boolean updateUserRespFlag(String telno){
	  boolean flag = false;
		Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "update m_user set resp_flag='N', txt_times=txt_times-1 where tel_no=?";
		System.out.println("用户开启服务:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, telno);
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
  //产生6位随机字符串并保存到数据库
  private String generateRandomKey(){
	    String result = null;
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < 6; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }
	    
	    Connection conn  = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		conn = DBUtils.getConnection();
		String sql = "insert into m_task_key (key_val, active_flag) values(?,'N')";
		System.out.println("用户开启服务:"+sql);
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, sb.toString());
			ps.execute();
			result = sb.toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtils.freeConnection(conn, ps, rs);
		}
	    return result;  
  }

}
