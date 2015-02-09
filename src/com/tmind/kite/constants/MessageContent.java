package com.tmind.kite.constants;

public class MessageContent {
	
	//手机号码或者客户端类型为空，请求被拒绝
	public static final String MSG_ACCESS_DENIED_FOR_NULL_TELNO_CLIENTTYPE = "手机号码或者客户端类型为空，请求被拒绝";
	
	//不同设备或者异地登录提示信息：用户已经在IOS或者Andorid端登录，此时需要在不同平台的App端登录
	public static final String MSG_LOGIN_OTHER_CLIENT_FOR_APP = "您的账号于{0}在{1}设备上登录。如果这不是您的操作，您的登录密码可能已经泄漏。请及时改密。";
	
	//不同设备或者异地登录提示信息：用户已经在Web端登录，此时需要在App端登录
	public static final String MSG_LOGIN_OTHER_CLIENT_FOR_WEB = "您的账号已经在{0}上登录，IOS或者Android App有优先权将其从{1}端退出，并在IOS或者Android APP端登录。如果这不是您的操作，您的登录密码可能已经泄漏。请及时改密。";
	
	//不同设备或者异地登录提示信息：用户已经在APP登录，此时需要在Web端登录
	public static final String MSG_LOGIN_APP_CLIENT_FOR_WEB = "您的账号已经在{0}登录，如果这不是您的操作，您的登录密码可能已经泄漏。您可以到服务关闭页面手动退出App。";
	
	//缺少访问入口标识，拒绝被访问
	public static final String MSG_ACCESS_DENIED_FOR_UNKNOWN_CLIENT = "非法访问渠道";
	
	//缺少访问入口标识，拒绝被访问
	public static final String MSG_ACCESS_DENIED_FOR_WRONG_CLIENT_FORMAT = "拒绝用户访问:请求URI中客户端类型格式错误";
	
	//未登录，拒绝被访问
	public static final String MSG_ACCESS_DENIED_FOR_NO_LOGIN = "未登录,非法访问";
	
	//未登录，拒绝被访问
	public static final String MSG_USER_PROFILE_EMPTY = "您尚未完成个人设置";
	
	//数据库操作异常
	public static final String MSG_DATABASE_EXECUTE_EXCEPTION = "数据库操作异常";
	
	//手机号码不正确
	public static final String MSG_USER_TELNO_INCORRECT = "您的手机号码不正确";
	
	//个人设置信息为空，请求被拒绝
	public static final String MSG_ACCESS_DENIED_FOR_NULL_USER_PROFILE = "个人设置信息为空，请求被拒绝";
	
	//紧急联系人信息为空，请求被拒绝
	public static final String MSG_ACCESS_DENIED_FOR_NULL_URGENT_USER_PROFILE = "紧急联系人或者紧急联系人手机号码为空，请求被拒绝";
}
