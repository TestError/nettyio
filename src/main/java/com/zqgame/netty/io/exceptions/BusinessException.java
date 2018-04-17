package com.zqgame.netty.io.exceptions;

import com.zqgame.netty.io.common.SystemProperty;
import com.zqgame.netty.io.exceptions.enums.ExceptionEnum;

/**
 *
 */
public class BusinessException extends RuntimeException {

	private ExceptionEnum exceptionEnum;

	private String detailMessage;

	/**
	 * 不允许外部调用
	 * peng.chen 2018/04/03 17:17:42
	 */
	private BusinessException() {
	}

	/**
	 * @param exceptionEnum 异常枚举
	 */
	public BusinessException(ExceptionEnum exceptionEnum) {
		this( exceptionEnum, (String) null );
	}

	public BusinessException(ExceptionEnum exceptionEnum, Throwable cause) {
		this( exceptionEnum, null, cause );
	}

	/**
	 * @param exceptionEnum 异常枚举
	 * @param detailMessage        明细信息
	 */
	public BusinessException(ExceptionEnum exceptionEnum, String detailMessage) {

		this(exceptionEnum,detailMessage,null);
	}


	public ExceptionEnum getExceptionEnum() {
		return exceptionEnum;
	}

	public String getDetailMessage() {
		return detailMessage;
	}

	/**
	 * @auther peng.chen
	 * @param exceptionEnum 异常枚举
	 * @param detailMessage 明细信息
	 * @param cause         原因
	 **/
	public BusinessException(ExceptionEnum exceptionEnum, String detailMessage, Throwable cause) {
		super( detailMessage == null ? exceptionEnum.getDescription() : new StringBuilder( exceptionEnum.getDescription() ).append( ":" ).append( detailMessage ).toString(),
				cause,
				true,
				SystemProperty.IS_DEBUG );

		this.exceptionEnum = exceptionEnum;
		this.detailMessage = detailMessage;

	}

}
