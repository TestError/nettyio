package com.zqgame.netty.io.annotations;

import java.lang.annotation.*;

/**
 * 定义消息的注解
 * @see java.lang.annotation.Annotation
 * @since 2018-5-2 16:17:57
 * @author peng.chen
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD})
public @interface Message {

    /**
     * 协议名称
     * @return
     */
    String proto();

    /**
     * 类型备用
     * @return
     */
    String type() default "";

}
