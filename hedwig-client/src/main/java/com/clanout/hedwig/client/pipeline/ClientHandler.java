package com.clanout.hedwig.client.pipeline;

import com.clanout.hedwig.client.MessageDelegate;
import com.clanout.hedwig.core.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter
{
    private MessageDelegate messageDelegate;

    public ClientHandler(MessageDelegate messageDelegate)
    {
        this.messageDelegate = messageDelegate;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        Message input = (Message) msg;
        messageDelegate.onRead(input);
    }
}
