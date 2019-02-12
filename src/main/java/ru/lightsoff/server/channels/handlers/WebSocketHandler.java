package ru.lightsoff.server.channels.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.lightsoff.server.engine.CycleRunner;
import ru.lightsoff.server.engine.movement.MoveEvent;
import ru.lightsoff.server.entities.Player;
import ru.lightsoff.server.security.Base64SecurityPackage;
import ru.lightsoff.server.security.Role;
import ru.lightsoff.server.security.SecurityContext;
import ru.lightsoff.server.security.SecurityPackage;

import java.util.HashSet;

public class WebSocketHandler extends ChannelInboundHandlerAdapter {
    @Autowired CycleRunner cycleRunner;
    @Autowired GameResponseHandler responseHandler;
    @Autowired SecurityContext securityContext;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {

        if (msg instanceof WebSocketFrame) {
            System.out.println("This is a WebSocket frame");
            System.out.println("Client Channel : " + ctx.channel());
            if (msg instanceof BinaryWebSocketFrame) {
                System.out.println("BinaryWebSocketFrame Received : ");
                System.out.println(((BinaryWebSocketFrame) msg).content());
            } else if (msg instanceof TextWebSocketFrame) {
                System.out.println("TextWebSocketFrame Received : ");
                System.out.println(((TextWebSocketFrame) msg).text());
                String message = ((TextWebSocketFrame) msg).text();
                if(!responseHandler.contains(ctx)) {
                    if (!securityContext.isNewUser(message)) {
                        String contextId = ctx.channel().toString();
                        responseHandler.newContext(message, ctx);
                        Base64SecurityPackage respPackage = new Base64SecurityPackage();
                        respPackage.getCredentials()
                                .setCredential("ID", message)
                                .setCredential("SECURITY_ID", contextId);
                        respPackage.setBody("here");
                        responseHandler.message(message, respPackage);
                        return;
                    }
                }else{
                    ctx.fireChannelRead(msg);
                }
            } else if (msg instanceof PingWebSocketFrame) {
                System.out.println("PingWebSocketFrame Received : ");
                System.out.println(((PingWebSocketFrame) msg).content());
            } else if (msg instanceof PongWebSocketFrame) {
                System.out.println("PongWebSocketFrame Received : ");
                System.out.println(((PongWebSocketFrame) msg).content());
            } else if (msg instanceof CloseWebSocketFrame) {
                System.out.println("CloseWebSocketFrame Received : ");
                System.out.println("ReasonText :" + ((CloseWebSocketFrame) msg).reasonText());
                System.out.println("StatusCode : " + ((CloseWebSocketFrame) msg).statusCode());
            } else {
                System.out.println("Unsupported WebSocketFrame");
            }
        }
    }
}
