package com.tmind.kite.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class DigestHandler {
	public static String decryptBASE64(String key) throws Exception {               
        byte[] bs = (new BASE64Decoder()).decodeBuffer(key); 
        return new String(bs);
    }               
                  
    /**         
     * BASE64加密   
   * @param key          
     * @return          
     * @throws Exception          
     */              
    public static String encryptBASE64(byte[] key) throws Exception {               
        return (new BASE64Encoder()).encodeBuffer(key);               
    }    
    
    public static String makeMD5(String password) {   
    	MessageDigest md;   
    	   try {   
    	    // 生成一个MD5加密计算摘要   
    	    md = MessageDigest.getInstance("MD5");   
    	    // 计算md5函数   
    	    md.update(password.getBytes());   
    	    // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符   
    	    // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值   
    	    String pwd = new BigInteger(1, md.digest()).toString(16);   
    	    System.err.println(pwd);   
    	    return pwd;   
    	   } catch (Exception e) {   
    	    e.printStackTrace();   
    	   }   
    	   return password;   
    	}
}
