package com.zqgame.netty.io.context;

import org.springframework.context.ApplicationContext;

/**
 * @apiNote 方便获取ApplicationContext
 * @author peng.chen
 * @since 2018-5-2 14:46:03
 */
public class ContextGetter {

    private static ApplicationContext applicationContext;

    /**
     * @apiNote  设置spring的context,在启动时调用
     * @since 2018-5-2 14:50:52
     * @param applicationContext
     */
    public static void setApplicationContext(ApplicationContext applicationContext) throws IllegalAccessException {
        if(applicationContext == null){
            throw new IllegalAccessException("applicationContext已经设置");
        }

        ContextGetter.applicationContext = applicationContext;
    }

    /**
     * @apiNote 方便应用获取spring application context
     * @since 2018-5-2 14:47:52
     * @return spring application context
     */
    public static ApplicationContext getApplicationContext() throws IllegalAccessException {

        if (applicationContext == null){
            throw new IllegalAccessException("applicationContext已经设置未设置");
        }
        return applicationContext;
    }
}
