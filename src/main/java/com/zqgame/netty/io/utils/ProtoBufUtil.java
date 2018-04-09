package com.zqgame.netty.io.utils;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Message;
import com.zqgame.netty.io.exceptions.BusinessException;
import com.zqgame.netty.io.exceptions.enums.ExceptionEnum;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @description 方便protobuf的对象和map对象进行转换的工具
 * @auther peng.chen
 * @create 2018/4/9 17:22
 */
public class ProtoBufUtil {


	/**
	 * 将一个Map转换为Proto对象
	 * @param protoClassName
	 * @param message
	 * @return
	 */
	public static Message map2Proto(String protoClassName, Map<String,Object> message) throws ClassNotFoundException{

		Class protoClass = Class.forName( protoClassName );
		return map2Proto( protoClass,message );

	}

	/**
	 * 将一个Map转换为Proto对象
	 * @param protoClass
	 * @param message
	 * @return
	 */
	public static Message map2Proto(Class protoClass, Map<String,Object> message){

		Descriptor descriptor = getDescriptor( protoClass );
		var builder = descriptor.toProto().newBuilderForType();
		descriptor.getFields().forEach( fieldDescriptor -> {
			if(message.containsKey(fieldDescriptor.getName())){
				builder.setField( fieldDescriptor,java2ProtoClass( fieldDescriptor,message.get(fieldDescriptor.getName()) ) );
			}
		});
		return builder.build();
	}

	public static Object java2ProtoClass(Descriptors.FieldDescriptor fieldDescriptor, Object data){

			switch (fieldDescriptor.getLiteType()){
				case MESSAGE:
					var mapData = (Map<String,Object>)data;
					data = map2Proto( fieldDescriptor.toProto().getClass(),mapData );
					break;
				case BOOL:
					break;
				case ENUM:
					break;
				case BYTES:
					if (data instanceof byte[]){
						byte[] dataByteArray = (byte[]) data;
						data = ByteString.copyFrom( dataByteArray );
					}
					break;
				case FLOAT:
					break;
				case GROUP:
					var datai = (Iterable)data;
					//TODO 看一下group是什么东西 应该怎么转换
					datai.forEach( o -> {

					} );
					break;
				case INT32:
					break;
				case INT64:
					break;
				case DOUBLE:
					break;
				case SINT32:
					break;
				case SINT64:
					break;
				case STRING:
					break;
				case UINT32:
					break;
				case UINT64:
					break;
				case FIXED32:
					break;
				case FIXED64:
					break;
				case SFIXED32:
					break;
				case SFIXED64:
					break;
				default:
					break;
			}


		return data;

	}

	/**
	 * 获取描述器
	 * @param protoClass
	 * @return
	 */
	private static Descriptor getDescriptor(Class protoClass){
		try {
			return (Descriptor)protoClass.getMethod( "getDescriptorForType" ).invoke( protoClass );
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new BusinessException( ExceptionEnum.SERVER_ERROR, "解析数据错误", e );
		}
	}

	/**
	 * 将proto对象转换为Map
	 * @param message
	 * @return
	 */
	public static Map<String,Object> proto2Map(Message message){



		return null;

	}


}
