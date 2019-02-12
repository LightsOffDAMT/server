package ru.lightsoff.server.channels.initializers;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.lightsoff.server.channels.handlers.GameLogicHandler;
import ru.lightsoff.server.channels.handlers.HttpServerHandler;
import ru.lightsoff.server.channels.handlers.JsonToSecurityPackageHandler;
import ru.lightsoff.server.channels.handlers.SecurityCheckHandler;

@Component
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Autowired
    ApplicationContext context;


    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(context.getBean(HttpServerHandler.class));
        pipeline.addLast(context.getBean(JsonToSecurityPackageHandler.class));
        pipeline.addLast(context.getBean(SecurityCheckHandler.class));
        pipeline.addLast(context.getBean(GameLogicHandler.class));
    }
}
