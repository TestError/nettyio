package com.zqgame.netty.io.test;

import com.google.protobuf.*;
import com.zqgame.netty.io.proto.NettyIoProto;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * @description 测试一下PROTObuf
 * @auther peng.chen
 * @create 2018/3/29 17:03
 */
public class ProtoTest {

	private Logger logger = LoggerFactory.getLogger(ProtoTest.class);

	@Test
	public void testNettyIoProto() throws ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

		NettyIoProto.Test.Builder testBuilder = NettyIoProto.Test.newBuilder();



		for (int i = 0 ;i<10000 ;i++){
			testBuilder.addValue("helloword");
		}

		NettyIoProto.Test test = testBuilder.build();

		Object testObject = test;

		logger.debug(testObject.getClass().getName());


		Class aClass = Class.forName(testObject.getClass().getName());

		byte[] byteArray = test.toByteArray();

		Message message = (Message)aClass.getMethod("parseFrom",byte[].class).invoke(aClass,byteArray);


//		message.
		logger.debug(message.getDescriptorForType().getName());
//		messageOrBuilder
		logger.debug(message.getClass().getName());


		logger.debug(test.getValue(1));




		logger.debug("长度:{}", byteArray.length);

		NettyIoProto.Test backtest = null;

		try {
			backtest = NettyIoProto.Test.parseFrom(byteArray);
		} catch (InvalidProtocolBufferException e) {
			logger.error("错误:{}", e);
		}



		logger.debug(backtest.getClass().getName());

		logger.debug("回来的:{}", backtest.getValue(1));

//		ProtobufEncoder



	}

//	private T extends

}
