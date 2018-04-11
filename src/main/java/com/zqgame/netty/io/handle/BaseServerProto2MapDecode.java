package com.zqgame.netty.io.handle;

import com.google.protobuf.Message;
import com.zqgame.netty.io.common.Constant;
import com.zqgame.netty.io.exceptions.BusinessException;
import com.zqgame.netty.io.exceptions.enums.ExceptionEnum;
import com.zqgame.netty.io.utils.ProtoBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 把proto对象转换为Map对象,方便上层可以不使用ProtoBuf进行开发
 * @auther peng.chen
 * @create 2018/4/9 15:19
 */
public class BaseServerProto2MapDecode extends MessageToMessageDecoder<Map<String,Object>> {

	private Logger logger = LoggerFactory.getLogger( BaseServerProto2MapDecode.class );

	@Override
	protected void decode(ChannelHandlerContext ctx, Map <String, Object> msg, List<Object> out) {

		Runnable nullProto = () -> {
			logger.error( "解析数据错误,空白的协议" );
			ctx.fireExceptionCaught( new BusinessException( ExceptionEnum.NULL_PROTO_NAME_ERROR ) );
		};


		if (!msg.containsKey(Constant.PROTO )){
			nullProto.run();
			return;
		}

		var proto = (String) msg.get( Constant.PROTO );

		if (StringUtil.isNullOrEmpty( proto )) {
			nullProto.run();
			return;
		}


		Map<String,Object> outputObject = new HashMap <String, Object>(  );
		outputObject.put( Constant.PROTO, proto );

		if (msg.containsKey( Constant.MESSAGE )){
			var message = (Message) msg.get( Constant.MESSAGE );
			var mapMessage = ProtoBufUtil.proto2Map( message );
			outputObject.put( Constant.MESSAGE,mapMessage );
		}

		out.add( outputObject );
	}
}
