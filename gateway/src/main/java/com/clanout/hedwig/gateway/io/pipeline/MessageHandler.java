package com.clanout.hedwig.gateway.io.pipeline;

import com.clanout.hedwig.core.message.Message;
import com.clanout.hedwig.gateway.domain.MessageProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;

public class MessageHandler extends ChannelInboundHandlerAdapter
{
    private static Logger LOG = LogManager.getLogger();

    private ExecutorService backgroundPool;
    private MessageProcessor messageProcessor;

    public MessageHandler(ExecutorService backgroundPool, MessageProcessor messageProcessor)
    {
        this.backgroundPool = backgroundPool;
        this.messageProcessor = messageProcessor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        Message request = (Message) msg;
        LOG.info("[Request] " + request.getId());

        backgroundPool.execute(() -> {
            Message response = messageProcessor.process(request);
            LOG.info("[Response] " + response.getId());
            ctx.writeAndFlush(response);
        });
    }
}
