package com.zin.opticaltest.entity;

public class LoginResponse {

	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_ERROR_PASSWORDERROR= "error_passwordwrong";
	public static final String STATUS_ISTIEMOUT = "isTimeOut";
	public static final String STATUS_ISNULL = "isNull";
	public static final String STATUS_ERROR_UNREGISTER = "error_unregister";
	public static final String STATUS_BY_REGISTERED = "registered";
	public static final String STATUS_ERROR = "error";
	public static final String STATUS_USERID_ISNULL = "userId_isNull";
	public static final String STATUS_UPLOAD_ERROR = "upload_error";
	
	public User user;
	
	public String status;
	
	public String info;

	public static String getStatusSuccess() {
		return STATUS_SUCCESS;
	}

	public static String getStatusErrorPassworderror() {
		return STATUS_ERROR_PASSWORDERROR;
	}

	public static String getStatusIstiemout() {
		return STATUS_ISTIEMOUT;
	}

	public static String getStatusIsnull() {
		return STATUS_ISNULL;
	}

	public static String getStatusErrorUnregister() {
		return STATUS_ERROR_UNREGISTER;
	}

	public static String getStatusByRegistered() {
		return STATUS_BY_REGISTERED;
	}

	public static String getStatusError() {
		return STATUS_ERROR;
	}

	public static String getStatusUseridIsnull() {
		return STATUS_USERID_ISNULL;
	}

	public static String getStatusUploadError() {
		return STATUS_UPLOAD_ERROR;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
