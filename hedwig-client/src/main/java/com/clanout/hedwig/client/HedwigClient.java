package com.clanout.hedwig.client;

import com.clanout.hedwig.core.converter.MessageConverter;

public class HedwigClient
{
    private String host;
    private int port;
    private MessageConverter messageConverter;

    private HedwigClient(String host, int port, MessageConverter messageConverter)
    {
        this.host = host;
        this.port = port;
        this.messageConverter = messageConverter;
    }

    public HedwigConnection getConnection()
    {
        return new HedwigConnection(host, port, messageConverter);
    }

    public static class Builder
    {
        private String host;
        private int port;
        private MessageConverter messageConverter;

        public Builder()
        {
            port = -1;
        }

        public Builder host(String host)
        {
            this.host = host;
            return this;
        }

        public Builder port(int port)
        {
            this.port = port;
            return this;
        }

        public Builder messageConverter(MessageConverter messageConverter)
        {
            this.messageConverter = messageConverter;
            return this;
        }

        public HedwigClient build()
        {
            if (host == null || host.isEmpty())
            {
                throw new IllegalStateException("Host not set");
            }

            if (port == -1)
            {
                throw new IllegalStateException("Port not set");
            }

            if (messageConverter == null)
            {
                throw new IllegalStateException("MessageConverter not set");
            }

            return new HedwigClient(host, port, messageConverter);
        }
    }
}
