package com.tmind.kite.constants;

/**
 * 
 * @title:通用常量类 
 * @description: 定义了系统常量信息
 * @author: Yang Yang
 */
public class CommonConstants
{
	
	/** 每页显示记录数量 */
	public static final int MAX_PAGE_ITEMS = 10;
	
	/** 间隔时间：用户未回复服务器最大间隔时间，单位为分钟*/
	public static final int MAX_STANDARD_CHECK_TIME = 15;
	
	/** 默认短信条数，用户注册时分配*/
	public static final int MAX_DEFAULT_SMS_COUNTS = 20;
	
	/** 登陆尝试次数 */
	public static final int MAX_LOGIN_ATTEMPT_TIMES = 3;
	
	/** 尝试登陆失败x次后账号被锁定多分钟数 */
	public static final int LOGIN_LOCK_TIME = 20;
	
	/**文字编码字符集：UTF－8*/
	public static final String CHARSETNAME_UTF_8 = "UTF-8";
	
	/** 用户登录令牌 */
	public static final String USER_LOGIN_TOKEN = "loginToken";
	
	/** 登录成功的用户 */
	public static final String LOGIN_USER_OBJECT = "user";

	/** 用户姓名 */
	public static final String 	USER_NAME = "userName";

	/** 用户手机号码标识 */
	public static final String TEL_NUMBER = "telno";

	/** 用户密码标识 */
	public static final String USER_PASSWORD = "password";

	/** 紧急联系人姓名 */
	public static final String 	URGENT_USER_NAME = "urgentName";

	/** 紧急联系人手机号码 */
	public static final String URGENT_TEL_NO = "urgentTelNo";

	/** 服务密码 */
	public static final String SERVICE_PASSWORD = "servicePwd";

	/** 安全问题 */
	public static final String 	SECURITY_QUESTION = "securityQue";

	/** 安全问题答案 */
	public static final String SECURITY_ANSWER = "securityAns";

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
	public static final String REST_MSG_KEY_STATUS = "status";
	
	/**REST消息格式：times*/
	public static final String REST_MSG_KEY_TIMES = "times";
	
	/**REST消息格式：content*/
	public static final String REST_MSG_KEY_CONTENT = "content";
	
	/**REST消息格式：msg*/
	public static final String REST_MSG_KEY_MSG_CONTENT = "msg";
	
	/**REST消息格式：个人设置状态*/
	public static final String REST_MSG_KEY_PROFILE_SETTING_STATUS = "pro-status";
	
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
	
	
	//用户登录相关消息代码

	/**用户登陆成功*/
	public static final String MSG_CODE_REST_LOGIN_SUCCESS = "1000";
	
	/**空用户ID*/
	public static final String MSG_CODE_REST_LOGIN_NULL_USERID = "1001";
	
	/**空用户密码*/
	public static final String MSG_CODE_REST_LOGIN_NULL_PWD = "1002";
	
	/**解密用户密码异常*/
	public static final String MSG_CODE_REST_LOGIN_DECODE_PWD_ERR = "1003";
	
	/**客户端类型为空*/
	public static final String MSG_CODE_REST_LOGIN_NULL_CLIENT_TYPE = "1004";
	
	/**用户不存在*/
	public static final String MSG_CODE_REST_LOGIN_USER_NOT_EXIST = "1005";
	
	/**用户密码不不正確*/
	public static final String MSG_CODE_REST_LOGIN_INCORRECT_PWD = "1006";
	
	/**用户被锁住*/
	public static final String MSG_CODE_REST_LOGIN_LOCKED_USER = "1007";
	
	/**多用户账号存在*/
	public static final String MSG_CODE_REST_LOGIN_MULTIPLE_USER = "1008";
	
	/**用户在其他客户端登录*/
	public static final String MSG_CODE_REST_LOGIN_OTHER_CLIENT = "1009";
	
	/**用户在web端登录，但是APP端已经登录*/
	public static final String MSG_CODE_REST_LOGIN_WEB_TO_APP = "1010";
	
	
	//用户注册相关消息代码
	
	/**用户注册成功*/
	public static final String MSG_CODE_REST_REGIST_SUCCESS = "2000";
	
	/**手机号码已经存在*/
	public static final String MSG_CODE_REST_REGIST_TELNO_EXIST = "2001";
	
	
	//个人设置相关消息代码 
	
	/**保存个人设置成功*/
	public static final String MSG_CODE_REST_SAVE_PROFILE_SUCCESS = "3000";
	
	/**保存个人设置失败*/
	public static final String MSG_CODE_REST_SAVE_PROFILE_FAILED = "3001";

	/**个人设置完成*/
	public static final String MSG_CODE_REST_REGIST_PROFILE_DONE = "3100";
	
	/**未完成个人设置*/
	public static final String MSG_CODE_REST_REGIST_PROFILE_NOT_DONE = "3101";
	
	/**获取用户个人设置成功*/
	public static final String MSG_CODE_REST_GET_PROFILE_SUCCESS = "3102";
	
	/**获取不到个人设置*/
	public static final String MSG_CODE_REST_EMPTY_PROFILE = "3103";
	
	/**保存紧急联系人信息成功*/
	public static final String MSG_CODE_REST_SAVE_URGENT_USER_SUCCESS = "3200";
	
	/**保存紧急联系人信息失败*/
	public static final String MSG_CODE_REST_SAVE_URGENT_USER_FAILED = "3201";
	
	/**保存服务密码成功*/
	public static final String MSG_CODE_REST_SAVE_SERVICE_PWD_SUCCESS = "3300";
	
	/**保存服务密码失败*/
	public static final String MSG_CODE_REST_SAVE_SERVICE_PWD_FAILED = "3301";
	
	/**保存安全问题成功*/
	public static final String MSG_CODE_REST_SAVE_SECURITY_QA_SUCCESS = "3400";
	
	/**保存安全问题失败*/
	public static final String MSG_CODE_REST_SAVE_SECURITY_QA_FAILED = "3401";
	
	
	//用户退出相关消息代码
	
	/**退出成功*/
	public static final String MSG_CODE_REST_LOGOUT_SUCCESS = "4000";
	
	/**退出失败,手机号不匹配*/
	public static final String MSG_CODE_REST_LOGOUT_TELNO_INCORRECT = "4001";
	
	
	//服务开启或关闭消息代码
	
	/**开启风筝服务成功*/
	public static final String MSG_CODE_REST_START_SERVICE_SUCCESS = "5000";
	
	/**开启风筝服务失败*/
	public static final String MSG_CODE_REST_START_SERVICE_FAILED = "5001";
	
	/**关闭风筝服务成功*/
	public static final String MSG_CODE_REST_STOP_SERVICE_SUCCESS = "5100";
	
	/**关闭风筝服务失败*/
	public static final String MSG_CODE_REST_STOP_SERVICE_FAILED = "5101";
	
	/**校验服务密码成功*/
	public static final String MSG_CODE_REST_VALID_SERVICE_PWD_SUCCESS = "5200";
	
	/**校验服务密码失败*/
	public static final String MSG_CODE_REST_VALID_SERVICE_PWD_FAILED = "5201";
	
	/**服务密码为空*/
	public static final String MSG_CODE_REST_EMPTY_SERVICE_PWD = "5203";
	
	/**获取不到服务开启或关闭指令*/
	public static final String MSG_CODE_REST_EMPTY_OPEN_OR_CLOSE_SERVICE_COMMAND = "5204";
	
	
	//消息格式校验相关消息代码
	
	/**拒绝用户访问:不明客户端类型*/
	public static final String MSG_CODE_UNKNOWN_CLIENT_ACCESS_DENIED = "6000";
	
	/**拒绝用户访问:未登录*/
	public static final String MSG_CODE_NO_LOGIN_ACCESS_DENIED = "6001";
	
	/**拒绝用户访问:请求URI中客户端类型格式错误*/
	public static final String MSG_CODE_REST_WRONG_ACCESS_FORMAT = "6002";
	
	
	/**保存数据异常*/
	public static final String MSG_CODE_REST_DB_EXCEPTION = "7001";

}
