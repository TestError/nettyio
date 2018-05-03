package com.zqgame.netty.io.annotations.registers;

import com.zqgame.netty.io.annotations.Message;
import com.zqgame.netty.io.annotations.MessageComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 注册
 */
public class MessageAnnotationRegister implements BeanPostProcessor {

    private static Logger logger = LoggerFactory.getLogger(MessageAnnotationRegister.class);

    @Qualifier("messageMap")
    @Autowired
    private Map messageMap;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        if(!bean.getClass().isAnnotationPresent(MessageComponent.class)){
            return bean;
        }

        Method [] methods = bean.getClass().getMethods();

        for(Method method : methods){

            Message message = method.getAnnotation(Message.class);
            if (message == null){
                continue;
            }

            logger.debug("found message:({}),method:({})",message.proto(),method);
            messageMap.put(message.proto(),method);
        }
        return bean;
    }

}
