package com.tmind.kite.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DBUtils {
	private static Properties config=new Properties();  
    private static BasicDataSource ds;  
    static{  
        InputStream in=DBUtils.class.getResourceAsStream("config.properties");  
        try {  
            config.load(in);  
            //1. 使用配置文件初始化BasicDataSource  
            ds= (BasicDataSource) BasicDataSourceFactory.createDataSource(config);    
              
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
      
    public static Connection getConnection(){  
        try{  
            return ds.getConnection();  
        }catch(Exception ex){  
            throw new RuntimeException(ex);  
        }  
    }  
      
    public static boolean freeConnection(Connection conn,Statement st,ResultSet rs){  
        boolean ret=true;  
        if(conn!=null){  
            try{  
            	conn.close();  
            }catch(Exception e){  
                throw new RuntimeException(e);  
            }finally{  
                ret=false;  
            }  
        }  
        if(st!=null){  
            try{  
                st.close();  
            }catch(Exception e){  
                throw new RuntimeException(e);  
            }finally{  
                ret=false;  
            }  
        }  
        if(rs!=null){  
            try{  
                rs.close();   
            }catch(Exception e){  
                throw new RuntimeException(e);  
            }finally{  
                ret=false;  
            }  
        }  
        return ret;  
    }  
    
    	
}
