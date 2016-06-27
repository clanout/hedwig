package com.clanout.hedwig.client;

import com.clanout.hedwig.client.pipeline.ClientChannelInitializer;
import com.clanout.hedwig.core.converter.MessageConverter;
import com.clanout.hedwig.core.message.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.ArrayList;
import java.util.List;

public class HedwigConnection implements MessageDelegate
{
    private String host;
    private int port;
    private MessageConverter messageConverter;

    private State state;
    private Channel channel;
    private ConnectionStateListener connectionStateListener;

    private List<MessageListener> messageListeners;

    HedwigConnection(String host, int port, MessageConverter messageConverter)
    {
        this.host = host;
        this.port = port;
        this.messageConverter = messageConverter;

        state = State.NOT_CONNECTED;
        messageListeners = new ArrayList<>();
    }

    public void setConnectionStateListener(ConnectionStateListener connectionStateListener)
    {
        this.connectionStateListener = connectionStateListener;
    }

    public void connect() throws Exception
    {
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);

        Bootstrap bootstrap = new Bootstrap()
                .group(workerGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(host, port)
                .handler(new ClientChannelInitializer(messageConverter, this))
                .option(ChannelOption.SO_KEEPALIVE, true);

        channel = bootstrap.connect().await().channel();
        if (channel.isActive())
        {
            state = State.CONNECTED;
        }

        channel.closeFuture().addListeners(channelFuture -> {
            state = State.NOT_CONNECTED;
            if (connectionStateListener != null)
            {
                connectionStateListener.onDisconnect();
            }
        });
    }

    public boolean isConnected()
    {
        return state == State.CONNECTED;
    }

    public void write(Message message) throws ConnectionInactiveException
    {
        if (state != State.CONNECTED)
        {
            throw new ConnectionInactiveException();
        }

        channel.writeAndFlush(message);
    }

    public void close() throws Exception
    {
        if (state != State.CONNECTED)
        {
            throw new ConnectionInactiveException();
        }

        channel.close().await();
    }

    public void addMessageListener(MessageListener messageListener)
    {
        messageListeners.add(messageListener);
    }

    public void removeMessageListener(MessageListener messageListener)
    {
        messageListeners.remove(messageListener);
    }

    @Override
    public void onRead(Message input)
    {
        for (MessageListener listener : messageListeners)
        {
            listener.onMessageReceived(input);
        }
    }

    enum State
    {
        NOT_CONNECTED,
        CONNECTED
    }
}
