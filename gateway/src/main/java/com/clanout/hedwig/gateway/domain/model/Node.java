package com.clanout.hedwig.gateway.domain.model;

public final class Node
{
    private final String host;
    private final int port;

    public Node(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }
}
