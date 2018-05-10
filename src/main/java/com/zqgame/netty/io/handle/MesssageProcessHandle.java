package com.zqgame.netty.io.handle;

import com.zqgame.netty.io.annotations.MessageParamType;
import com.zqgame.netty.io.common.Constant;
import com.zqgame.netty.io.context.ContextGetter;
import com.zqgame.netty.io.exceptions.BusinessException;
import com.zqgame.netty.io.exceptions.enums.ExceptionEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class MesssageProcessHandle extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(MesssageProcessHandle.class);

    /**
     * 连接上
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("new connection :{}", ctx.channel().remoteAddress());
        super.channelActive(ctx);
    }

    /**
     * 断线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        logger.debug("new connection :{}", ctx.channel().remoteAddress());
//        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        logger.debug("接受到了哦");

        Map<String, Object> mapMessage = (Map<String, Object>) msg;

        ApplicationContext applicationContext = ContextGetter.getApplicationContext();

        Map map = (Map) applicationContext.getBean(Constant.MESSAGE_MAP);

        String proto = (String) mapMessage.get(Constant.PROTO);

        Method method = (Method) map.get(proto);
        //为空报错
        if (method == null) {
            throw new BusinessException(ExceptionEnum.SERVER_ERROR, "协议为空");
        }

        Map<String, Object> message = (Map<String, Object>) mapMessage.get(Constant.MESSAGE);

        Class messageComponentClass = method.getDeclaringClass();
        Object messageComponent = applicationContext.getBean(messageComponentClass);

        Runnable task = () -> {

            try {
                Annotation[][] paramAnnotations = method.getParameterAnnotations();
                int paramCount = paramAnnotations.length;
                Object [] param = new Object[paramCount];

                for (int i=0;i<paramCount;i++){

                    var item = paramAnnotations[i];
                    param[i] = null;
                    for (var innerItem : item) {
                        if (innerItem instanceof MessageParamType) {
                            MessageParamType messageParamType = (MessageParamType) innerItem;
                            switch (messageParamType.value()) {
                                case Constant.PROTO:
                                    param[i] = proto;
                                    break;
                                case Constant.MESSAGE:
                                    param[i] = message;
                                    break;
                                case Constant.CHANNEL_HANDLER_CONTEXT:
                                    param[i] = ctx;
                                    break;
                                case Constant.ACCOUNT:
                                    break;
                                default:
                                    break;
                            }
                            break;
                        }
                    }
                }

                Object result = method.invoke(messageComponent, param);
                logger.debug("result:{}", result);

                ctx.disconnect();
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("调度异常  e:{}", e);
            }

        };

        Executor executor = (Executor) applicationContext.getBean("threadPool");
        executor.execute(task);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
