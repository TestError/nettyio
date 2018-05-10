package com.zqgame.netty.io.test;

import org.junit.Test;

public class ArgTest {

    @Test
    public void argMethodTest(){

        Object [] array = new Object[10];

        array[0] = "QQQQ";
        array[1] = 1;

        mearg(array);

    }

    private void mearg(Object ... args){


        for (Object item : args){
            System.out.println(item);
        }

    }




}
