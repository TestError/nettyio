package com.zqgame.netty.io.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 测试泛型的类
 */
public class GenericityTest {

    @Test
    public void tryTest(){

        List<Integer> integers = new ArrayList<>();

        integers.add(1);
        integers.add(2);

        integers = check(integers);

        integers.forEach(integer -> {

            System.out.println(integer);

        });

    }


    private <T> List<T> check(List<T> datas){

        List<T> tempDatas = new ArrayList<>(datas.size());

        tempDatas.addAll(datas);

        for (int i=0 ; i < datas.size(); i++){
            tempDatas.set(i,datas.get(i));
        }

        Collections.shuffle(tempDatas);

        return tempDatas;

    }


}
