package com.zqgame.netty.io.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @auther peng.chen
 * @create 2018/4/9 15:19
 */
public class BaseServerProto2MapDecode extends MessageToMessageDecoder<Map<String,Object>> {

	@Override
	protected void decode(ChannelHandlerContext ctx, Map <String, Object> msg, List<Object> out) {

	}
}
