package com.zqgame.netty.io.handle;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.zqgame.netty.io.common.Constant;
import com.zqgame.netty.io.exceptions.BusinessException;
import com.zqgame.netty.io.exceptions.enums.ExceptionEnum;
import com.zqgame.netty.io.proto.NettyIoProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 *  输入格式应该类似 Map<String,Object>{"proto":"com.zqgame.netty.io.proto.NettyIoProto.Sample","message":{"sampleProperty":"PropertyContent"}};
 *  peng.chen
 *  2018/4/3 15:59
 */
public class BaseServerProtoMessageEncode extends MessageToMessageEncoder<Map<String,Object>> {

	private static Logger logger = LoggerFactory.getLogger(BaseServerProtoMessageEncode.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, Map <String, Object> msg, List <Object> out) {

		if(!msg.containsKey( Constant.PROTO )){

			logger.error( "未带协议名" );
			ctx.fireExceptionCaught( new BusinessException( ExceptionEnum.NULL_PROTO_NAME_ERROR ) );
			return;
		}

		String proto = (String) msg.get( Constant.PROTO );

		Message message = (Message) msg.get( Constant.MESSAGE );

		NettyIoProto.Base.Builder baseBuilder = NettyIoProto.Base.newBuilder();
		NettyIoProto.Head.Builder headbuilder = NettyIoProto.Head.newBuilder();
		headbuilder.setProto( proto );
		baseBuilder.setHeader( headbuilder.build() );


		if (message !=null){
			baseBuilder.setBody( ByteString.copyFrom(message.toByteArray()));
		}

		out.add( baseBuilder.build());

	}
}
