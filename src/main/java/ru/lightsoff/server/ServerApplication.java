package ru.lightsoff.server;

import com.google.gson.Gson;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.lightsoff.server.channels.handlers.*;
import ru.lightsoff.server.channels.initializers.HttpChannelInitializer;
import ru.lightsoff.server.conntector.SyncPlayerSynchronizer;
import ru.lightsoff.server.engine.CycleRunner;
import ru.lightsoff.server.security.SecurityContext;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
public class ServerApplication {
    @Bean
    CycleRunner cycleRunner(){
        return singleGameCycleRunner(8, 8, 40);
    }

    @Bean
    @Scope(scopeName = "prototype")
    WebSocketHandler webSocketHandler(){
        return new WebSocketHandler();
    }

    @Bean
    @Scope(scopeName = "prototype")
    HttpServerHandler httpServerHandler(){
        return new HttpServerHandler();
    }

    @Bean
    @Scope(scopeName = "prototype")
    HttpChannelInitializer httpChannelInitializer(){
        return new HttpChannelInitializer();
    }

    static CycleRunner singleGameCycleRunner(int CYCLE_SIZE, int CORE_POOL_SIZE, int FRAME_RATE){
        CycleRunner mainCycle = new CycleRunner(CYCLE_SIZE);
        Executors.newScheduledThreadPool(CORE_POOL_SIZE).scheduleAtFixedRate(
                mainCycle, 1000, FRAME_RATE, TimeUnit.MILLISECONDS
        );
        return mainCycle;
    }

    @Bean
    SyncPlayerSynchronizer asyncPlayerSynchronizer(){
        return new SyncPlayerSynchronizer(Duration.ofSeconds(5)    );
    }

    @Bean
    GameResponseHandler responseHandler(){
        return new GameResponseHandler();
    }

    @Bean
    SecurityContext securityContext(){
        return new SecurityContext();
    }

    @Bean
    @Scope(scopeName = "prototype")
    SecurityCheckHandler securityCheckHandler(){
        return new SecurityCheckHandler();
    }

    @Bean
    @Scope(scopeName = "prototype")
    JsonToSecurityPackageHandler jsonToSecurityPackageHandler(){
        return new JsonToSecurityPackageHandler();
    }


    @Bean
    @Scope(scopeName = "prototype")
    GameLogicHandler gameLogicHandler(){
        return new GameLogicHandler();
    }

    @Bean
    @Scope(scopeName = "prototype")
    Gson gson(){
        return new Gson();
    }


    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("ru.lightsoff.server");
        CycleRunner runner = ctx.getBean(CycleRunner.class);
        SyncPlayerSynchronizer playerSynchronizer = ctx.getBean(SyncPlayerSynchronizer.class);
        (new Thread(playerSynchronizer)).start();

        EventLoopGroup allGroup = new NioEventLoopGroup(8);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap = bootstrap.group(allGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(ctx.getBean(HttpChannelInitializer.class))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(9001).sync();
            future.channel().closeFuture().sync();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            allGroup.shutdownGracefully();
        }
    }

}

