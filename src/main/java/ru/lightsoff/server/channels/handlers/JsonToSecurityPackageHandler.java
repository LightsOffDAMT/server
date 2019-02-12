package ru.lightsoff.server.channels.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import ru.lightsoff.server.security.Base64SecurityPackage;
import ru.lightsoff.server.security.SecurityContext;
import ru.lightsoff.server.security.SecurityPackage;

public class JsonToSecurityPackageHandler extends ChannelInboundHandlerAdapter {
    private Gson json = new Gson();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SecurityPackage securityPackage = null;
        if(!(msg instanceof TextWebSocketFrame))
            throw new ClassCastException("Unsupported type: " + msg.toString());

        try{
            securityPackage = json.fromJson(((TextWebSocketFrame) msg).text(), Base64SecurityPackage.class);
        } catch (JsonSyntaxException e){
            System.out.println(JsonToSecurityPackageHandler.class + ": " + e.getLocalizedMessage());
        }
        if(securityPackage != null)
            ctx.fireChannelRead(securityPackage);
    }

}
