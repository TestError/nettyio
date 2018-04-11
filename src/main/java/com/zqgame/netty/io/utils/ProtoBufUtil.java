package com.zqgame.netty.io.utils;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Message;
import com.zqgame.netty.io.exceptions.BusinessException;
import com.zqgame.netty.io.exceptions.enums.ExceptionEnum;

import java.lang.reflect.InvocationTargetException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 方便protobuf的对象和map对象进行转换的工具
 * @auther peng.chen
 * @create 2018/4/9 17:22
 */
public class ProtoBufUtil {


	/**
	 * 将一个Map转换为Proto对象
	 *
	 * @param protoClassName
	 * @param message
	 * @return
	 */
	public static Message map2Proto(String protoClassName, Map <String, Object> message) throws ClassNotFoundException {

		Class protoClass = Class.forName( protoClassName );
		return map2Proto( protoClass, message );

	}

	/**
	 * 将一个Map转换为Proto对象
	 *
	 * @param protoClass
	 * @param message
	 * @return
	 */
	public static Message map2Proto(Class protoClass, Map <String, Object> message) {

		Descriptor descriptor = getDescriptor( protoClass );
		var builder = descriptor.toProto().newBuilderForType();
		descriptor.getFields().forEach( fieldDescriptor -> {
			if (message.containsKey( fieldDescriptor.getName() )) {

				//为一组数据时
				if (fieldDescriptor.isRepeated()) {
					Iterable iterable = (Iterable) message.get( fieldDescriptor.getName() );

					iterable.forEach( o -> {

						builder.addRepeatedField( fieldDescriptor,o );

					} );

				//	builder.addRepeatedField(fieldDescriptor,iterable  );
//					int index = 0;

//					for(var i : iterable){
//						builder.setRepeatedField(fieldDescriptor.getContainingType().,index,i );
//					}

				} else {
					//为单个数据时
					builder.setField( fieldDescriptor, java2ProtoClass( fieldDescriptor, message.get( fieldDescriptor.getName() ) ) );
				}
			}
		} );
		return builder.build();
	}

	public static Object java2ProtoClass(Descriptors.FieldDescriptor fieldDescriptor, Object data) {

		switch (fieldDescriptor.getJavaType()) {
			case STRING:
				break;
			case DOUBLE:
				break;
			case FLOAT:
				break;
			case ENUM:
				break;
			case MESSAGE:
				Map <String, Object> mapData = (Map <String, Object>) data;
				data = map2Proto( fieldDescriptor.toProto().getClass(), mapData );
				break;
			case INT:
				break;
			case LONG:
				break;
			case BOOLEAN:
				break;
			case BYTE_STRING:
				if (data instanceof byte[]) {
					data = ByteString.copyFrom( (byte[]) data );
				}
				break;
		}

		return data;

	}

	public static Object protoClass2Java(Descriptors.FieldDescriptor fieldDescriptor, Object data) {

		switch (fieldDescriptor.getJavaType()) {
			case STRING:
				break;
			case DOUBLE:
				break;
			case FLOAT:
				break;
			case ENUM:
				break;
			case MESSAGE:
				Message messageData = (Message)data;
				data = proto2Map(messageData);
				break;
			case INT:
				break;
			case LONG:
				break;
			case BOOLEAN:
				break;
			case BYTE_STRING:
				if (data instanceof ByteString) {
					data = ((ByteString)data).toByteArray();
				}
				break;
		}

		return data;

	}

	/**
	 * 获取描述器
	 *
	 * @param protoClass
	 * @return
	 */
	private static Descriptor getDescriptor(Class protoClass) {
		try {
			return (Descriptor) protoClass.getMethod( "getDescriptor" ).invoke( protoClass );
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new BusinessException( ExceptionEnum.SERVER_ERROR, "解析数据错误", e );
		}
	}

	/**
	 * 将proto对象转换为Map
	 *
	 * @param message
	 * @return
	 */
	public static Map <String, Object> proto2Map(Message message) {

		Map <String, Object> resultMap = new HashMap <String, Object>();

		message.getAllFields().forEach( (fieldDescriptor, o) -> {

			if (fieldDescriptor.isRepeated()) {
				AbstractList list = (AbstractList) o;
				ArrayList arrayList = new ArrayList( list );
				resultMap.put( fieldDescriptor.getName(), arrayList );
			} else {
				resultMap.put( fieldDescriptor.getName(),protoClass2Java( fieldDescriptor,o ) );
			}
		} );
		return resultMap;
	}


}
