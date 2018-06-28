package com.zqgame.netty.io.common;

/**
 *
 */
public class SystemProperty {

	/**
	 * 是否启用DEBUG模式 主要是对于
	 */
	public final static boolean IS_DEBUG ;

	/**
	 * spring xml的位置
	 */
	public final static String APPLICATION_XML_PATH;

	/**
	 * 重连延时 毫秒为单位
	 */
	public final static long RECOUNNECT_DELAY_TIME = 5000L;

	/**
	 * 心跳包的时间
	 */
	public final static int HEARTBEAT_TIME = 30;

	/**
	 * 心跳超时时间
	 */
	public final static int HEARTBEAT_TIME_OUT_TIME = 90;

	static {
		IS_DEBUG = System.getProperty( Constant.IS_DEBUG ) != null ? !System.getProperty( Constant.IS_DEBUG ).equals( Constant.TRUE ) : false;
		APPLICATION_XML_PATH = System.getProperty(Constant.APPLICATION_XML_PATH) != null ? System.getProperty(Constant.APPLICATION_XML_PATH) : "application.xml";
	}




}
