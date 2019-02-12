package ru.lightsoff.server.channels.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import reactor.core.publisher.Mono;

public interface ResponseHandler{
    <T> ChannelFuture message(String id, T o);
    void newContext(String id, ChannelHandlerContext ctx);
    boolean contains(ChannelHandlerContext ctx);
    void disconnect(String id);
}