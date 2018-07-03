package com.zqgame.netty.io.test;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * nio 测试监听文件改动
 */
public class FileWatchTest {



    @Test
    public void test() throws IOException, InterruptedException {

        var watch = FileSystems.getDefault().newWatchService();


        var pathStr = "E:\\logs\\vip";

        var path = Paths.get(pathStr);

        path.register(watch,ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY,StandardWatchEventKinds.ENTRY_DELETE);

        var key = watch.take();

        key.pollEvents().forEach(watchEvent -> {

            System.out.println(watchEvent.count());
            System.out.println(watchEvent.kind());
            System.out.println(watchEvent.context());


            var filePathBuilder = new StringBuilder(pathStr).append("\\").append(watchEvent.context());

            try {
                var channel = new RandomAccessFile(filePathBuilder.toString(),"r").getChannel();

                var size = channel.size();
                var byteBuffer = ByteBuffer.allocate((int) size);
//                channel.read
                channel.read(byteBuffer);
                byteBuffer.flip();

                System.out.println(new String (byteBuffer.array(),"UTF-8"));
                


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });





    }

}
