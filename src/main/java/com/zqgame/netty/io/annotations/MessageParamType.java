package com.zqgame.netty.io.annotations;

import com.zqgame.netty.io.common.Constant;

import java.lang.annotation.*;

/**
 * 接受消息的参数类型
 * 目前有 proto,messageMap,account,channelHandlerContext三个类型
 * proto: 协议名称
 * message: 传过来的参数
 * account: 账号  暂时保留
 * channelHandlerContext: 方便异步发送
 * @see com.zqgame.netty.io.common.Constant
 * @author peng.chen
 * @since 2018-5-10 11:48:24
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.PARAMETER})
public @interface MessageParamType {

    String value();

}
