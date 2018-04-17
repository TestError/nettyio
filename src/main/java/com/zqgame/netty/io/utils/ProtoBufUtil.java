package com.zqgame.netty.io.utils;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Message;
import com.zqgame.netty.io.exceptions.BusinessException;
import com.zqgame.netty.io.exceptions.enums.ExceptionEnum;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;

/**
 *  方便protobuf的对象和map对象进行转换的工具
 *  peng.chen
 *  2018/4/9 17:22
 */
public class ProtoBufUtil {

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
				Collection list = (Collection) o;
				ArrayList arrayList = new ArrayList();

				list.forEach( i ->{
					arrayList.add(protoClass2Java( fieldDescriptor, i ));
				});

				resultMap.put( fieldDescriptor.getName(), arrayList );
			} else {
				resultMap.put( fieldDescriptor.getName(), protoClass2Java( fieldDescriptor, o ) );
			}
		} );
		return resultMap;
	}

	/**
	 * 将一个Map转换为Proto对象
	 *
	 * @param protoClassName
	 * @param message
	 * @return
	 */
	public static Message map2Proto(String protoClassName, Map <String, Object> message) {

		protoClassName = protoClassName.replaceAll( "\\.(?=((?!\\.).)*$)", Matcher.quoteReplacement( "$" ) );
		Class protoClass = null;
		try {
			protoClass = Class.forName( protoClassName );
		} catch (ClassNotFoundException e) {
			throw new BusinessException( ExceptionEnum.SERVER_ERROR, e );
		}
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


		try {
			Message.Builder builder = (Message.Builder) protoClass.getMethod( "newBuilder" ).invoke( protoClass );

			Descriptor descriptor = getDescriptor( protoClass );


			descriptor.getFields().forEach( fieldDescriptor -> {

				if (message.containsKey( fieldDescriptor.getName() )) {
					builder.getField( fieldDescriptor );

					if (fieldDescriptor.isRepeated()) {

						((Iterable) message.get( fieldDescriptor.getName() )).forEach( o -> {

							builder.addRepeatedField( fieldDescriptor, java2ProtoClass( fieldDescriptor, o ) );

						} );

					} else {
						builder.setField( fieldDescriptor, java2ProtoClass( fieldDescriptor, message.get( fieldDescriptor.getName() ) ) );
					}
				}

			} );

			return builder.build();
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new BusinessException( ExceptionEnum.SERVER_ERROR, e );
		}
	}

	/**
	 * 把java的类型转换为proto类型
	 *
	 * @param fieldDescriptor
	 * @param data
	 * @return
	 */
	private static Object java2ProtoClass(Descriptors.FieldDescriptor fieldDescriptor, Object data) {

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
				var messageName = fieldDescriptor.getMessageType().getFullName();
				data = map2Proto( messageName, mapData );
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

	/**
	 * 把proto的类转换为Map
	 *
	 * @param fieldDescriptor
	 * @param data
	 * @return
	 */
	private static Object protoClass2Java(Descriptors.FieldDescriptor fieldDescriptor, Object data) {

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
				Message messageData = (Message) data;
				data = proto2Map( messageData );
				break;
			case INT:
				break;
			case LONG:
				break;
			case BOOLEAN:
				break;
			case BYTE_STRING:
				if (data instanceof ByteString) {
					data = ((ByteString) data).toByteArray();
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




}
