package com.clanout.hedwig.gateway.io.server;

import com.clanout.hedwig.core.converter.MessageConverter;
import com.clanout.hedwig.gateway.data.DummyAuthService;
import com.clanout.hedwig.gateway.data.DummyRootNodeService;
import com.clanout.hedwig.gateway.domain.MessageProcessor;
import com.clanout.hedwig.gateway.domain.service.AuthService;
import com.clanout.hedwig.gateway.domain.service.RootNodeService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class HedwigGatewayServer
{
    private static Logger LOG = LogManager.getLogger();

    private int port;
    private EventLoopGroup selectorGroup;
    private EventLoopGroup workerGroup;
    private ExecutorService backgroundPool;
    private MessageConverter messageConverter;

    private Channel channel;

    private HedwigGatewayServer(int port, int workerPoolSize, int backgroundPoolSize, MessageConverter messageConverter)
    {
        this.port = port;
        this.messageConverter = messageConverter;

        selectorGroup = createEventLoopGroup(1, "selector-%d");
        workerGroup = createEventLoopGroup(workerPoolSize, "worker-%d");
        backgroundPool = createExecutorService(backgroundPoolSize, "background-%d");
    }

    public void start() throws Exception
    {
        AuthService authService = new DummyAuthService();
        RootNodeService rootNodeService = new DummyRootNodeService();

        MessageProcessor messageProcessor = new MessageProcessor(authService, rootNodeService);

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(selectorGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ServerChannelInitializer(messageConverter, messageProcessor, backgroundPool))
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        LOG.info("HedwigGatewayServer Started...");

        channel = channelFuture.channel();
        channel.closeFuture().sync();
    }

    public void stop() throws Exception
    {
        channel.close().await();
        workerGroup.shutdownGracefully();
        selectorGroup.shutdownGracefully();
        backgroundPool.shutdownNow();
    }

    public static class Builder
    {
        private int port;
        private int workerPoolSize;
        private int backgroundPoolSize;
        private MessageConverter messageConverter;

        public Builder()
        {
            port = 7777;
            workerPoolSize = 5;
            backgroundPoolSize = 10;
        }

        public Builder port(int port)
        {
            this.port = port;
            return this;
        }

        public Builder workerPoolSize(int workerPoolSize)
        {
            this.workerPoolSize = workerPoolSize;
            return this;
        }

        public Builder backgroundPoolSize(int backgroundPoolSize)
        {
            this.backgroundPoolSize = backgroundPoolSize;
            return this;
        }

        public Builder messageConverter(MessageConverter messageConverter)
        {
            this.messageConverter = messageConverter;
            return this;
        }

        public HedwigGatewayServer build()
        {
            return new HedwigGatewayServer(port, workerPoolSize, backgroundPoolSize, messageConverter);
        }
    }

    private EventLoopGroup createEventLoopGroup(int poolSize, String threadNameFormat)
    {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(threadNameFormat)
                .build();
        return new NioEventLoopGroup(poolSize, threadFactory);
    }

    private ExecutorService createExecutorService(int poolSize, String threadNameFormat)
    {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat(threadNameFormat)
                .build();
        return Executors.newFixedThreadPool(poolSize, threadFactory);
    }
}
