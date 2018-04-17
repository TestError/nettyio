package com.zqgame.netty.io.handle;

import com.google.protobuf.Message;
import com.zqgame.netty.io.common.Constant;
import com.zqgame.netty.io.exceptions.BusinessException;
import com.zqgame.netty.io.exceptions.enums.ExceptionEnum;
import com.zqgame.netty.io.proto.NettyIoProto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

/**
 *
 */
public class BaseServerProtoMessageDecode extends MessageToMessageDecoder <NettyIoProto.Base> {

	private static Logger logger = LoggerFactory.getLogger( BaseServerProtoMessageDecode.class );

	@Override
	protected void decode(ChannelHandlerContext ctx, NettyIoProto.Base msg, List <Object> out) {

		//拿到相应的协议名
		var proto = msg.getHeader().getProto();

		if (StringUtil.isNullOrEmpty(proto)){
			logger.error( "解析数据错误,空白的协议");
			ctx.fireExceptionCaught( new BusinessException( ExceptionEnum.NULL_PROTO_NAME_ERROR) );
			return;
		}

		//这里根据head里面的协议名去拿到对应的协议类型
		Class protoClass;
		try {
			protoClass = Class.forName( proto.replaceAll(  "\\.(?=((?!\\.).)*$)", Matcher.quoteReplacement( "$" )  ) );
		} catch (ClassNotFoundException e) {
			logger.error( "找不到相应协议", e );
			ctx.fireExceptionCaught( new BusinessException( ExceptionEnum.SERVER_ERROR, "找不到相应协议对应的类", e ) );
			return;
		}

		//构造输出对象
		var outObject = new HashMap <String, Object>();
		outObject.put( Constant.PROTO, proto );
		var bodyContent = msg.getBody();

		//如果body不为空,则尝试去解码
		if (bodyContent != null) {

			var body = bodyContent.toByteArray();

			Message message;
			try {
				message = (Message) protoClass.getMethod( "parseFrom", byte[].class ).invoke( protoClass, body );
			} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			logger.error( "解析数据错误", e );
			ctx.fireExceptionCaught( new BusinessException( ExceptionEnum.SERVER_ERROR, "解析数据错误", e ) );
			return;
		}
			outObject.put( Constant.MESSAGE, message );
		}

		out.add( outObject );
	}
}
