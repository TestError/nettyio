package com.zqgame.netty.io.message;

import com.zqgame.netty.io.annotations.Message;
import com.zqgame.netty.io.annotations.MessageComponent;
import com.zqgame.netty.io.annotations.MessageParamType;
import com.zqgame.netty.io.common.Constant;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 测试消息接受
 *
 * @author peng.chen
 * @since 2018-5-3 09:47:12
 */
@MessageComponent
public class TestMessage extends MessageBase {

    private static Logger logger = LoggerFactory.getLogger(TestMessage.class);

    /**
     * @param data 链里获取的数据
     */
    @Message(proto = "com.zqgame.netty.io.proto.NettyIoProto.Test")
    public String onCallTest(@MessageParamType(Constant.PROTO) String proto, @MessageParamType(Constant.MESSAGE) Map<String, Object> data,
                             Object nullValue, @MessageParamType(Constant.CHANNEL_HANDLER_CONTEXT) ChannelHandlerContext channelHandlerContext) {

        logger.debug("proto:{}", proto);

        logger.debug("handler:{}", channelHandlerContext);

        logger.debug("调用了哦,数据为:{}", data);

        return "helloworld";
    }

}
