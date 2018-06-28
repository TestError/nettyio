package com.zqgame.netty.io.handle;

import com.zqgame.netty.io.common.Constant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 服务端心跳Handle
 * 和IDleHandle组合使用
 * peng.chen 2018/06/28 13:52:57
 */
public class BaseServerHeartbeatHandle extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(BaseServerHeartbeatHandle.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if (idleStateEvent.state() == IdleState.READER_IDLE){
                logger.debug("长时间未接受到消息，超时关闭 event：{}  channel:{}",evt,ctx.channel());
                ctx.close();
            }
        }else {
            ctx.fireUserEventTriggered(evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //消息转为Map
        Map<String, Object> mapMessage = (Map<String, Object>) msg;
        //
        String proto = (String) mapMessage.get(Constant.PROTO);

        //不处理心跳协议
        if (proto.equals(Constant.HEART_BEAT_PROTO)){
            return ;
        }

        ctx.fireChannelRead(msg);

    }
}
