package com.tmind.kite.model;

public class User {

	private String id;
	
	//用户账号，即手机号码
	private String telNo;
	
	//用户密码，MD5加密
	private String userPwd;
	
	//用户姓名
	private String userName;
	
	//紧急联系人姓名
	private String urgentUserName;
	
	//紧急联系人电话号码
	private String urgentTelNo;
	
	//用户记录是否有效，Y：有效；N：无效
	private String activeFlag;
	
	//用户登录状态：0：未登录；1：已登录
	private String loginFlag;
	
	//用户登录失败n次后账号被锁住的时间
	private String lockedTime;
	
	//短信条数
	private String txtTimes;
	
	//用户登录的客户端，1：ISO APP；2：Android；3：微信；4：WebAPP
	private String clientType;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getUrgentUserName() {
		return urgentUserName;
	}

	public void setUrgentUserName(String urgentUserName) {
		this.urgentUserName = urgentUserName;
	}

	public String getUrgentTelNo() {
		return urgentTelNo;
	}

	public void setUrgentTelNo(String urgentTelNo) {
		this.urgentTelNo = urgentTelNo;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}

	public String getLockedTime() {
		return lockedTime;
	}

	public void setLockedTime(String lockedTime) {
		this.lockedTime = lockedTime;
	}

	public String getTxtTimes() {
		return txtTimes;
	}

	public void setTxtTimes(String txtTimes) {
		this.txtTimes = txtTimes;
	}

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	
}
