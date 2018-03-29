package com.zqgame.netty.io.test;

import com.google.protobuf.InvalidProtocolBufferException;
import com.zqgame.netty.io.proto.NettyIoProto;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 测试一下PROTObuf
 * @auther peng.chen
 * @create 2018/3/29 17:03
 */
public class ProtoTest {

	private Logger logger = LoggerFactory.getLogger(ProtoTest.class);

	@Test
	public void testNettyIoProto() {

		NettyIoProto.test.Builder testBuilder = NettyIoProto.test.newBuilder();


		for (int i = 0 ;i<10000 ;i++){
			testBuilder.addValue("helloword");
		}

		NettyIoProto.test test = testBuilder.build();

		logger.debug(test.getValue(1));

		byte[] byteArray = test.toByteArray();


		logger.debug("长度:{}", byteArray.length);

		NettyIoProto.test backtest = null;

		try {
			backtest = NettyIoProto.test.parseFrom(byteArray);
		} catch (InvalidProtocolBufferException e) {
			logger.error("错误:{}", e);
		}



		logger.debug("回来的:{}", backtest.getValue(1));

//		ProtobufEncoder

	}

}
