package com.zqgame.netty.io.handle;

import com.zqgame.netty.io.common.Constant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户端心跳的Handle
 * 和IDleHandle组合使用
 * peng.chen 2018/06/28 13:52:01
 */
public class BaseClientHeartbeatHandle extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(BaseClientHeartbeatHandle.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {

        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            //发送心跳包HeartBeat
            if (idleStateEvent.state() == IdleState.WRITER_IDLE){

                logger.debug("发送心跳包  channel:{}" ,ctx.channel());

                Map<String,Object> message = new HashMap<String, Object>();
                message.put(Constant.PROTO,Constant.HEART_BEAT_PROTO);
                ctx.writeAndFlush(message);
            }
        }else {
            ctx.fireUserEventTriggered(evt);
        }
    }

}
