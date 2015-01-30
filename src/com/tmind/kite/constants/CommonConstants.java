package com.tmind.kite.constants;

/**
 * 
 * @title:通用常量类 
 * @description: 定义了系统常量信息
 * @author: Yang Yang
 */
public class CommonConstants
{

	/**超级管理员帐号*/
	public static final String SUPER_ADMIN_NAME = "admin";

	/**逗号分割符*/
	public static final String SPLIT_SYMBOL_COMMA = ",";

	/**竖杆分割符*/
	public static final String SPLIT_SYMBOL_VERTICAL_LINE = "|";

	/**横线分割符*/
	public static final String SPLIT_SYMBOL_TRANSVERSE_LINE = "-";

	/**冒号分割符*/
	public static final String SPLIT_SYMBOL_COLON = ":";

	/**下划线分割符*/
	public static final String SPLIT_SYMBOL_UNDERLINE = "_";

	/**文件后缀名（文本文件）*/
	public static final String FILE_SUFFIX_TXT = ".txt";

	/**文件路径分割符*/
	public static final String FILE_SEPARATOR = "/";

	/**无上级单位的页面展示数据*/
	public static final String NO_PARENT_CORP = "---";
	
	/**文字编码字符集：UTF－8*/
	public static final String CHARSETNAME_UTF_8 = "UTF-8";
	
	/** 用户登录令牌 */
	public static final String USER_LOGIN_TOKEN = "loginToken";
	
	/** 登录成功的用户 */
	public static final String LOGIN_USER_OBJECT = "user";
	
	/** 每页显示记录数量 */
	public static final int MAX_PAGE_ITEMS = 10;
	
	/** 间隔时间：用户未回复服务器最大间隔时间，单位为分钟*/
	public static final int MAX_STANDARD_CHECK_TIME = 15;
	
	/** 默认短信条数，用户注册时分配*/
	public static final int MAX_DEFAULT_SMS_COUNTS = 20;
	
	/** 登陆尝试次数 */
	public static final String MAX_LOGIN_ATTEMPT_TIMES = "3";
	
	/** 尝试登陆失败x次后账号被锁定多分钟数 */
	public static final int LOGIN_LOCK_TIME = 20;

	/** 用户姓名 */
	public static final String 	USER_NAME = "userName";

	/** 用户手机号码标识 */
	public static final String TEL_NUMBER = "telno";

	/** 用户密码标识 */
	public static final String USER_PASSWORD = "password";

	/** 短信验证类型代码标识 */
	public static final String SMS_CODE_TYPE = "codeType";

	/** 随机码 */
	public static final String TXT_CODE = "txtCode";
	
	/** 校验码标识 */
	public static final String VALIDATE_CODE = "validateCode";

	/** 随机码标识 */
	public static final String RANDOM_CODE = "randomCode";
	
	/** 用户请求来源标识,即客户端类型 */
	public static final String CLIENT_TYPE = "clientType";
	
	/** 用户请求来自IOS APP */
	public static final String ACCESS_FROM_IOS = "1";
	
	/** 用户请求来自Android APP */
	public static final String ACCESS_FROM_ANDROID = "2";
	
	/** 用户请求来自Weixin */
	public static final String ACCESS_FROM_WEIXIN = "3";
	
	/** 用户请求来自Web APP */
	public static final String ACCESS_FROM_WEBAPP = "4";
	
	/**REST消息格式：status*/
	public static final String REST_MSG_FORMAT_STATUS = "status";
	
	/**REST消息格式：times*/
	public static final String REST_MSG_FORMAT_TIMES = "times";
	
	/**REST消息格式：content*/
	public static final String REST_MSG_FORMAT_CONTENT = "content";
	
	/**REST消息格式：msg*/
	public static final String REST_MSG_FORMAT_MSG_CONTENT = "msg";
	
	/**REST消息格式：个人设置状态*/
	public static final String REST_MSG_FORMAT_PROFILE_SETTING_STATUS = "pro-status";
	
	/**用户登陆成功*/
	public static final String MSG_CODE_LOGIN_SUCCESS = "success";
	
	/**验证码不正确*/
	public static final String MSG_CODE_LOGIN_WRONG_RANDOM_CODE = "error01";
	
	/**用户不存在*/
	public static final String MSG_CODE_LOGIN_NO_USER = "error02";
	
	/**用户账号被锁住*/
	public static final String MSG_CODE_LOGIN_USER_LOCKED = "error05";
	
	/**用户密码错误*/
	public static final String MSG_CODE_LOGIN_WRONG_PWD = "error06";
	
	
	/**用户注册成功*/
	public static final String MSG_CODE_REST_REGIST_SUCCESS = "success";
	
	/**手机号码已经存在*/
	public static final String MSG_CODE_REST_REGIST_TELNO_EXIST = "reg-error01";
	
	/**保存数据异常*/
	public static final String MSG_CODE_REST_REGIST_DB_EXCEPTION = "reg-error02";
	
	/**未完成个人设置*/
	public static final String MSG_CODE_REST_REGIST_PROFILE_NOT_DONE = "reg-error03";

	/**完成个人设置*/
	public static final String MSG_CODE_REST_REGIST_PROFILE_DONE = "reg-done";
	
	
	/**获取用户个人设置成功*/
	public static final String MSG_CODE_REST_GET_PROFILE_SUCCESS = "success";
	
	/**获取不到个人设置*/
	public static final String MSG_CODE_REST_GET_EMPTY_PROFILE = "empty";
	
	
	/**用户登陆成功*/
	public static final String MSG_CODE_REST_LOGIN_SUCCESS = "success";
	
	/**拒绝用户访问:不明客户端类型*/
	public static final String MSG_CODE_UNKNOWN_CLIENT_ACCESS_DENIED = "acc-denied-01";
	
	/**拒绝用户访问:未登录*/
	public static final String MSG_CODE_NO_LOGIN_ACCESS_DENIED = "acc-denied-02";
	
	/**拒绝用户访问:请求URI中客户端类型格式错误*/
	public static final String MSG_CODE_REST_WRONG_ACCESS_FORMAT = "acc-denied-03";
	
	/**空用户ID*/
	public static final String MSG_CODE_REST_LOGIN_NULL_USERID = "lgn-error00-1";
	
	/**空用户密码*/
	public static final String MSG_CODE_REST_LOGIN_NULL_PWD = "lgn-error00-2";
	
	/**解密用户密码异常*/
	public static final String MSG_CODE_REST_LOGIN_DECODE_PWD_ERR = "lgn-error00-3";
	
	/**客户端类型为空*/
	public static final String MSG_CODE_REST_LOGIN_NULL_CLIENT_TYPE = "lgn-error00-4";
	
	/**用户不存在*/
	public static final String MSG_CODE_REST_LOGIN_NO_USER = "lgn-error01";
	
	/**用户密码不存在*/
	public static final String MSG_CODE_REST_LOGIN_INCORRECT_PWD = "lgn-error02";
	
	/**用户被锁住*/
	public static final String MSG_CODE_REST_LOGIN_LOCKED_USER = "lgn-error03";
	
	/**多用户账号存在*/
	public static final String MSG_CODE_REST_LOGIN_MULTIPLE_USER = "lgn-error04";
	
	/**用户在其他客户端登录*/
	public static final String MSG_CODE_REST_LOGIN_OTHER_CLIENT = "lgn-error05";
	
	/**用户在web端登录，但是APP端已经登录*/
	public static final String MSG_CODE_REST_LOGIN_WEB_TO_APP = "lgn-error05-01";

}
