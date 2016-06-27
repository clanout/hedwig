package com.clanout.hedwig.client.pipeline;

import com.clanout.hedwig.client.MessageDelegate;
import com.clanout.hedwig.client.pipeline.ClientHandler;
import com.clanout.hedwig.client.pipeline.StreamDecoder;
import com.clanout.hedwig.client.pipeline.StreamEncoder;
import com.clanout.hedwig.core.converter.MessageConverter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel>
{
    private MessageConverter messageConverter;
    private MessageDelegate messageDelegate;

    public ClientChannelInitializer(MessageConverter messageConverter, MessageDelegate messageDelegate)
    {
        this.messageConverter = messageConverter;
        this.messageDelegate = messageDelegate;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception
    {
        socketChannel.pipeline().addLast(new StreamDecoder(messageConverter));
        socketChannel.pipeline().addLast(new StreamEncoder(messageConverter));
        socketChannel.pipeline().addLast(new ClientHandler(messageDelegate));
    }
}
