package ru.lightsoff.server.channels.handlers;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.concurrent.ConcurrentHashMap;

public class GameResponseHandler implements ResponseHandler{
    private ConcurrentHashMap<String, ChannelHandlerContext> storage = new ConcurrentHashMap<>();

    @Override
    public <T> ChannelFuture message(String id, T object) {
        return storage
                .get(id)
                .channel()
                .writeAndFlush(new TextWebSocketFrame(
                        new Gson().toJson(object)
                        )
                );
    }

    @Override
    public void newContext(String id, ChannelHandlerContext context) {
        storage.put(id, context);
    }

    @Override
    public boolean contains(ChannelHandlerContext ctx) {
        return storage.contains(ctx);
    }

    @Override
    public void disconnect(String id) {
        storage.remove(id);
    }
}
