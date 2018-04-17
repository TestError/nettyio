package com.zqgame.netty.io.exceptions.enums;

/**
 *
 */
public enum  ExceptionEnum {

	SERVER_ERROR("503",1,"服务异常,请稍后重试!"),

	PARAMETER_ERROR("403",2,"参数错误"),

	NULL_PROTO_NAME_ERROR("403",3,"空白的协议名!")

	,;

	private String statusCode;

	private int errorCode;

	private String description;



	ExceptionEnum(String statusCode, int errorCode, String description){
		this.statusCode = statusCode;
		this.errorCode = errorCode;
		this.description = description;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getDescription() {
		return description;
	}

}
