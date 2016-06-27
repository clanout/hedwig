package com.clanout.hedwig.client.pipeline;

import com.clanout.hedwig.core.converter.MessageConverter;
import com.clanout.hedwig.core.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class StreamDecoder extends ChannelInboundHandlerAdapter
{
    private MessageConverter messageConverter;

    public StreamDecoder(MessageConverter messageConverter)
    {
        this.messageConverter = messageConverter;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        ByteBuf byteBuf = (ByteBuf) msg;

        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        Message message = messageConverter.decode(bytes);
        ctx.fireChannelRead(message);
    }
}
