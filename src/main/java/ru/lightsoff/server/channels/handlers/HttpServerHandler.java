package ru.lightsoff.server.channels.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.util.Logger;
import reactor.util.Loggers;
import ru.lightsoff.server.engine.CycleRunner;
import ru.lightsoff.server.entities.Player;
import ru.lightsoff.server.security.SecurityContext;

import java.nio.channels.ConnectionPendingException;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {
    private Logger log = Loggers.getLogger(this.getClass());
    @Autowired
    WebSocketHandler webSocketHandler;
    @Autowired
    CycleRunner cycleRunner;
    @Autowired
    SecurityContext securityContext;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest){
            HttpRequest request = (HttpRequest)msg;
            log.info("Received HTTP request from" + ctx.name());

            HttpHeaders headers = request.headers();

            log.info("Connection : " +headers.get("Connection"));
            log.info("Upgrade : " + headers.get("Upgrade"));

            if(headers.get(HttpHeaderNames.UPGRADE) != null && headers.get(HttpHeaderNames.CONNECTION) != null && headers.get(HttpHeaderNames.UPGRADE).toLowerCase().equals("websocket") && headers.get(HttpHeaderNames.CONNECTION).toLowerCase().equals("upgrade")){
                ctx.pipeline().replace(this, "websocketHandler", webSocketHandler);
                System.out.println(ctx.pipeline().equals(ctx.channel().pipeline()));
                System.out.println("WebSocketHandler added to the pipeline");
                System.out.println("Opened Channel : " + ctx.channel());
                System.out.println("Handshaking....");
                //Do the Handshake to upgrade connection from HTTP to WebSocket protocol
                handleHandshake(ctx, request);
                System.out.println("Handshake is done");
            }

        }else {
            log.error(msg.toString(), new DecoderException("Unknown incoming request"));

        }
    }

    protected void handleHandshake(ChannelHandlerContext ctx, HttpRequest req) {
        WebSocketServerHandshakerFactory webSocketServerHandshakerFactory = new WebSocketServerHandshakerFactory(buildWebSocketUrl(req), null, true);
        WebSocketServerHandshaker handshaker = webSocketServerHandshakerFactory.newHandshaker(req);
        if(handshaker == null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    protected String buildWebSocketUrl(HttpRequest req){
        log.info("Req URI : " + req.uri());
        String url =  "ws://" + req.headers().get("Host") + req.uri() ;
        log.info("Constructed URL : " + url);
        return url;
    }
}
