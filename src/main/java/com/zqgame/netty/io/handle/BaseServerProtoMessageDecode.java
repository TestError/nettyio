package com.zqgame.netty.io.handle;

import com.zqgame.netty.io.proto.NettyIoProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @description
 * @auther peng.chen
 * @create 2018/4/2 11:37
// */
public class BaseServerProtoMessageDecode extends MessageToMessageDecoder<NettyIoProto.Base> {

	private static Logger logger = LoggerFactory.getLogger(BaseServerProtoMessageDecode.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, NettyIoProto.Base msg, List<Object> out) {

		//拿到相应的协议名
		String proto = msg.getHeader().getProto();

		try {
			Class protoClass = Class.forName( proto );
		}catch (ClassNotFoundException e){
			logger.error( "找不到相应协议对应的类",e);

		}








	}
}
