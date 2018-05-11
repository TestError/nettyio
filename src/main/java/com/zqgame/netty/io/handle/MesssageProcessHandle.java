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

        //从spring里拿取存接收消息的方法的map
        ApplicationContext applicationContext = ContextGetter.getApplicationContext();
        Map map = (Map) applicationContext.getBean(Constant.MESSAGE_MAP);

        //消息转为Map
        Map<String, Object> mapMessage = (Map<String, Object>) msg;
        //
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
                            //只拿第一个,第一次执行完成后就推出循环
                            break;
                        }
                    }
                }

                Object result = method.invoke(messageComponent, param);

                if (result != null){
                    logger.debug("result:{}", result);
                }

                ctx.writeAndFlush(result);

                ctx.disconnect();
            } catch (Exception e) {
                logger.error("调度异常  e:{}", e);
            }

        };

        Executor executor = (Executor) applicationContext.getBean("threadPool");
        executor.execute(task);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        logger.error("caught Exception :{}",cause);

//        super.exceptionCaught(ctx, cause);
    }
}
