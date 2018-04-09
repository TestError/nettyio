package com.zqgame.netty.io.handle;

import com.google.protobuf.Message;
import com.zqgame.netty.io.common.Constant;
import com.zqgame.netty.io.exceptions.BusinessException;
import com.zqgame.netty.io.exceptions.enums.ExceptionEnum;
import com.zqgame.netty.io.proto.NettyIoProto;
import com.zqgame.netty.io.utils.ProtoBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 把map对象转换为 Proto对象 方便上层进行直接使用Map进行操作
 * @auther peng.chen
 * @create 2018/4/9 15:19
 */
public class BaseServerMap2ProtoEncode extends MessageToMessageEncoder <Map <String, Object>> {

	private static Logger logger = LoggerFactory.getLogger( BaseServerMap2ProtoEncode.class );

	@Override
	protected void encode(ChannelHandlerContext ctx, Map <String, Object> msg, List <Object> out) {

		Runnable nullProto = () -> {
			logger.error( "解析数据错误,空白的协议" );
			ctx.fireExceptionCaught( new BusinessException( ExceptionEnum.NULL_PROTO_NAME_ERROR ) );
		};

		if (!msg.containsKey( Constant.PROTO )) {
			nullProto.run();
			return;
		}

		var proto = (String) msg.get( Constant.PROTO );

		if (StringUtil.isNullOrEmpty( proto )) {
			nullProto.run();
			return;
		}

		var outputObject = new HashMap <String, Object>();
		outputObject.put( Constant.PROTO, proto );


		if (msg.containsKey( Constant.MESSAGE )) {

			Map <String, Object> mapObject = (Map <String, Object>) msg.get( Constant.MESSAGE );

			try {
				Message protoObject = ProtoBufUtil.map2Proto( proto, mapObject );
				outputObject.put( Constant.MESSAGE, protoObject );
			} catch (ClassNotFoundException e) {
				logger.error( "找不到相应协议", e );
				ctx.fireExceptionCaught( new BusinessException( ExceptionEnum.SERVER_ERROR, "找不到相应协议对应的类", e ) );
				return;
			}

		}

		out.add( outputObject );

	}

	/**
	 *
	 * @param protoClass
	 * @param message
	 * @return
	 */


}
