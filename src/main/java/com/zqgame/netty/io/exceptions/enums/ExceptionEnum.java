package com.zqgame.netty.io.exceptions.enums;

/**
 * @description 统一的事务异常枚举
 * @auther peng.chen
 * @create 2018/4/3 16:46
 */
public enum  ExceptionEnum {

	SERVER_ERROR("503",1,"服务异常,请稍后重试!"),

	PARAMETER_ERROR("403",2,"参数错误")

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
