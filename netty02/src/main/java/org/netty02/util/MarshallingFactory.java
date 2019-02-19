package org.netty02.util;


import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public class MarshallingFactory {

	/**
	 * 创建Jboss Marshalling解码器MarshallingDecoder
	 * @return
	 */
	public static MarshallingDecoder decode(){
		// 首先通过Marshalling工具类获取Marshalling实例对象 参数serial标识创建的是java序列化工厂对象
		MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
		// 创建了MarshallingConfiguration对象，配置了版本号为5
		MarshallingConfiguration cofiguration = new MarshallingConfiguration();
		cofiguration.setVersion(5);
		// 根据marshallerFactory和configuration创建provider
		UnmarshallerProvider provider = new DefaultUnmarshallerProvider(factory, cofiguration);
		// 构建Netty的MarshallingDecoder对象，俩个参数分别为provider和单个消息序列化后的最大长度
		MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024 * 1024 * 10);
		return decoder;
	}
	
	public static MarshallingEncoder encode(){
		// 首先通过Marshalling工具类获取Marshalling实例对象 参数serial标识创建的是java序列化工厂对象
		MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
		// 创建了MarshallingConfiguration对象，配置了版本号为5
		MarshallingConfiguration cofiguration = new MarshallingConfiguration();
		cofiguration.setVersion(5);
		// 根据marshallerFactory和configuration创建provider
		MarshallerProvider provider = new DefaultMarshallerProvider(factory, cofiguration);

		MarshallingEncoder encoder = new MarshallingEncoder(provider);
		
		return encoder;
	
	
	}
}
