package ru.lightsoff.server.channels.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import ru.lightsoff.server.engine.CycleRunner;
import ru.lightsoff.server.engine.movement.MoveEvent;
import ru.lightsoff.server.security.SecurityPackage;

import javax.activation.UnsupportedDataTypeException;
import java.util.Optional;
import java.util.TreeMap;

public class GameLogicHandler extends SimpleChannelInboundHandler<SecurityPackage> {
    @Autowired
    private CycleRunner cycleRunner;
    @Autowired
    private Gson json;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SecurityPackage msg) throws Exception {
        Optional optionalBody = msg.getBody();

        if(!optionalBody.isPresent())
            throw new ClassCastException("Empty game action type from user: " + msg.getCredentials().getCredential("ID"));

        LinkedTreeMap<String, Object> treeMap = (LinkedTreeMap)optionalBody.get();

        if(!treeMap.containsKey("className"))
            throw new ClassCastException("Class name field is empty");

        if(treeMap.get("className").equals("refresh")){
            cycleRunner.refreshForPlayerById(msg.getCredentials().getCredential("ID"));
        }

        if(treeMap.get("className").equals("ru.lightsoff.server.engine.movement.MoveEvent"))
            cycleRunner.newMove(new MoveEvent(treeMap));
    }
}
