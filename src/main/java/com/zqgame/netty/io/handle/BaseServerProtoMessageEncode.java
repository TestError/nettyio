package com.zqgame.netty.io.handle;

import com.google.protobuf.Message;
import com.google.protobuf.MessageLiteOrBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @description
 * @auther peng.chen
 * @create 2018/4/3 15:59
 */
public class BaseServerProtoMessageEncode extends MessageToMessageEncoder<MessageLiteOrBuilder> {

	private static Logger logger = LoggerFactory.getLogger(BaseServerProtoMessageEncode.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List out) {

		

	}
}
