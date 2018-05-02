package com.zqgame.netty.io.common;

/**
 *
 */
public class SystemProperty {

	/**
	 * 是否启用DEBUG模式 主要是对于
	 */
	public final static boolean IS_DEBUG ;

	public final static String APPLICATION_XML_PATH;

	static {
		IS_DEBUG = System.getProperty( Constant.IS_DEBUG ) != null ? !System.getProperty( Constant.IS_DEBUG ).equals( Constant.TRUE ) : false;
		APPLICATION_XML_PATH = System.getProperty(Constant.APPLICATION_XML_PATH) != null ? System.getProperty(Constant.APPLICATION_XML_PATH) : "application.xml";
	}




}
