package com.clanout.hedwig.gateway.io.server;

import com.clanout.hedwig.core.converter.MessageConverter;
import com.clanout.hedwig.gateway.domain.MessageProcessor;
import com.clanout.hedwig.gateway.io.pipeline.MessageHandler;
import com.clanout.hedwig.gateway.io.pipeline.StreamDecoder;
import com.clanout.hedwig.gateway.io.pipeline.StreamEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ExecutorService;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private MessageConverter messageConverter;
    private MessageProcessor messageProcessor;
    private ExecutorService backgroundPool;

    private StreamEncoder encoder;
    private StreamDecoder decoder;

    public ServerChannelInitializer(MessageConverter messageConverter, MessageProcessor messageProcessor,
                                    ExecutorService backgroundPool)
    {
        encoder = new StreamEncoder(messageConverter);
        decoder = new StreamDecoder(messageConverter);

        this.messageProcessor = messageProcessor;
        this.backgroundPool = backgroundPool;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        socketChannel.pipeline().addLast(decoder);
        socketChannel.pipeline().addLast(encoder);
        socketChannel.pipeline().addLast(new MessageHandler(backgroundPool, messageProcessor));
    }
}
