package com.clanout.hedwig.client.pipeline;

import com.clanout.hedwig.core.converter.MessageConverter;
import com.clanout.hedwig.core.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class StreamEncoder extends ChannelOutboundHandlerAdapter
{
    private MessageConverter messageConverter;

    public StreamEncoder(MessageConverter messageConverter)
    {
        this.messageConverter = messageConverter;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
    {
        Message message = (Message) msg;
        byte[] stream = messageConverter.encode(message);

        ByteBuf byteBuf = ctx.alloc().buffer(stream.length);
        byteBuf.writeBytes(stream);

        ctx.write(byteBuf, promise);
    }
}
