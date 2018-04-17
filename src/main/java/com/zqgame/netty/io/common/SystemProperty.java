package com.zqgame.netty.io.common;

/**
 *
 */
public class SystemProperty {

	/**
	 * 是否启用DEBUG模式 主要是对于
	 */
	public final static boolean IS_DEBUG ;

	static {
		IS_DEBUG = System.getProperty( Constant.IS_DEBUG ) != null ? !System.getProperty( Constant.IS_DEBUG ).equals( Constant.TRUE ) : false;
	}




}
