package ru.lightsoff.server.channels.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import ru.lightsoff.server.security.Role;
import ru.lightsoff.server.security.SecurityContext;
import ru.lightsoff.server.security.SecurityPackage;

public class SecurityCheckHandler extends SimpleChannelInboundHandler<SecurityPackage> {
    @Autowired
    private SecurityContext securityContext;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SecurityPackage msg) throws Exception {
        if(securityContext.getRole(msg.getCredentials().getCredential("ID")).equals(Role.NONE))
            throw new AuthenticationCredentialsNotFoundException("User with id: " + msg.getCredentials().getCredential("ID") + " - not found");
        else
            ctx.pipeline().remove(this.getClass());
        ctx.fireChannelRead(msg);
    }


}
