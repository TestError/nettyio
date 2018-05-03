package com.zqgame.netty.io.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 定义消息的注解的组件类
 * @see java.lang.annotation.Annotation
 * @since 2018-5-2 16:17:57
 * @author peng.chen
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.TYPE})
@Component
public @interface MessageComponent {

}
