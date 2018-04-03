package com.zqgame.netty.io.exceptions;

import com.zqgame.netty.io.common.Constant;
import com.zqgame.netty.io.exceptions.enums.ExceptionEnum;

/**
 * @description 自定义异常类, 默认关闭堆栈信息, 测试时启用堆栈信息
 * @auther peng.chen
 * @create 2018/4/3 16:45
 */
public class BusinessException extends RuntimeException {

	private ExceptionEnum exceptionEnum;

	private String detail;

	/**
	 * 不允许外部调用
	 * peng.chen 2018/04/03 17:17:42
	 */
	private BusinessException() {
	}

	;


	/**
	 * @param exceptionEnum 异常枚举
	 */
	public BusinessException(ExceptionEnum exceptionEnum) {
		new BusinessException( exceptionEnum, (String) null );
	}

	public BusinessException(ExceptionEnum exceptionEnum, Throwable cause) {
		new BusinessException( exceptionEnum, null, cause );
	}

	/**
	 * @param exceptionEnum 异常枚举
	 * @param detail        明细信息
	 */
	public BusinessException(ExceptionEnum exceptionEnum, String detail) {
		new BusinessException( exceptionEnum, detail, null );
	}


	/**
	 * @param exceptionEnum 异常枚举
	 * @param detail        明细信息
	 * @param cause         原因
	 */
	public BusinessException(ExceptionEnum exceptionEnum, String detail, Throwable cause) {
		super( detail == null ? exceptionEnum.getDescription() : new StringBuilder( exceptionEnum.getDescription() ).append( ":" ).append( detail ).toString(),
				cause,
				false,
				//System.getProperty( Constant.IS_DEBUG ) != null ? !System.getProperty( Constant.IS_DEBUG ).equals( Constant.TRUE ) : false
				false
		);


		this.exceptionEnum = exceptionEnum;
		this.detail = detail;
	}

	public ExceptionEnum getExceptionEnum() {
		return exceptionEnum;
	}

	public String getDetail() {
		return detail;
	}
}
