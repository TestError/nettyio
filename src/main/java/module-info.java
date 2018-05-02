/**
 * @apiNote  java9 模块化的配置信息  尝试使用
 * peng.chen
 * @since  2018/4/3 11:29
 */
module com.zqgame.netty.io {

	requires protobuf.java;
	requires io.netty.all;
//	requires slf4j.log4j12;
	requires slf4j.api;
	requires spring.context;
	requires java.sql;
//	requires log4j;
//	requires junit;
	exports com.zqgame.netty.io.handle;
	exports com.zqgame.netty.io.proto;
	exports com.zqgame.netty.io.utils;

}