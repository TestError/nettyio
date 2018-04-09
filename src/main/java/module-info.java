/**
 * @description java9 模块化的配置信息  尝试使用
 * @auther peng.chen
 * @create 2018/4/3 11:29
 */
module com.zqgame.netty.io {

	requires protobuf.java;
	requires io.netty.all;
//	requires slf4j.log4j12;
	requires slf4j.api;
//	requires log4j;
//	requires junit;
	exports com.zqgame.netty.io.handle;
	exports com.zqgame.netty.io.proto;
	exports com.zqgame.netty.io.utils;

}