package com.zqgame.netty.io.test;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import com.zqgame.netty.io.proto.NettyIoProto;
import com.zqgame.netty.io.utils.ProtoBufUtil;
import javafx.beans.property.ReadOnlyFloatWrapper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Flow;

/**
 * @description
 * @auther peng.chen
 * @create 2018/3/14 13:32
 */
public class AppTest {

	private static Logger logger = LoggerFactory.getLogger(AppTest.class);




	/**
	 * @description
	 * @auther peng.chen
	 * @create 2018/3/14 13:29
	 */
	@Test
	public void testApp() throws InterruptedException,ExecutionException {



		logger.debug("test");



//		NettyIoProto.Base base = NettyIoProto.Base.newBuilder().setBody( ByteString.copyFromUtf8( "我草您吗" ) ).setHeader( NettyIoProto.Head.newBuilder().setProto( "12313" ).build() ).build();
//
//
//
//		base.getAllFields().forEach( (fieldDescriptor, o) -> {
//			logger.debug( fieldDescriptor.getName() );
//
//			logger.debug( fieldDescriptor.getFullName() );
//
//			logger.debug( fieldDescriptor.getLiteType().toString());
//
//			if(fieldDescriptor.getLiteType().toString().equals( "MESSAGE" )){
//				logger.debug("out:{}", fieldDescriptor.getMessageType().toProto());
//			}
//
//			logger.debug( o.toString() );
//
//
//
//		} );
//		NettyIoProto.Test.newBuilder().setRepeatedField(  )\

		NettyIoProto.Test test = NettyIoProto.Test.newBuilder().addValue( "1231" ).addValue( "12312" ).setItem( 123123131 ).setCode( ByteString.copyFromUtf8( "23131121" ) ).setHearer( NettyIoProto.Head.newBuilder().setProto( "12312" ).build() ).setTestEnumValue( 1 ).build();

		//logger.debug( "array:{}",test.getField( NettyIoProto.Test.getDescriptor().findFieldByName( "value" ) ).getClass().getName() );

		Map object = ProtoBufUtil.proto2Map( test );
		logger.debug( object.toString() );

//		NettyIoProto.Test.getDescriptor()
			Message obj =  ProtoBufUtil.map2Proto( NettyIoProto.Test.class,object );
			logger.debug( obj.toString() );


//
//		Message message = NettyIoProto.Test.newBuilder().setTestEnumValue( 0 ).build();
//
//		message.getDescriptorForType().getFields().forEach( fieldDescriptor -> {
//			logger.debug( fieldDescriptor.getName() );
//
//			logger.debug( fieldDescriptor.getFullName() );
//
//			logger.debug( fieldDescriptor.getLiteType().toString());
//
//			if(fieldDescriptor.getLiteType().toString().equals( "MESSAGE" )) {
//				logger.debug( "out:{}", fieldDescriptor.getMessageType().toProto() );
//			}
//		} );

//		NettyIoProto.Test.getDefaultInstance().getAllFields().forEach( (fieldDescriptor, o) -> {
//
//			logger.debug( fieldDescriptor.getName() );
//
//			logger.debug( fieldDescriptor.getFullName() );
//
//			logger.debug( fieldDescriptor.getLiteType().toString());
//
//			if(fieldDescriptor.getLiteType().toString().equals( "MESSAGE" )) {
//				logger.debug( "out:{}", fieldDescriptor.getMessageType().toProto() );
//			}
//		} );



//		assert true;
//		Assert.assertTrue(true);

	}

}
