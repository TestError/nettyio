/**
 * @apiNote  java9 模块化的配置信息  尝试使用
 * peng.chen
 * @since  2018/4/3 11:29
 */
module com.zqgame.netty.io {

	requires protobuf.java;
	requires io.netty.all;

	requires slf4j.api;
	requires spring.context;
	requires spring.core;
	requires spring.beans;
	requires java.sql;
//	requires java.xml.ws.annotation;
    requires javax.annotation.api;

	exports com.zqgame.netty.io.handle;
	exports com.zqgame.netty.io.proto;
	exports com.zqgame.netty.io.utils;
	exports com.zqgame.netty.io.message;
	exports com.zqgame.netty.io.annotations.registers;

	opens com.zqgame.netty.io.annotations.registers;
	opens com.zqgame.netty.io.handle;
}