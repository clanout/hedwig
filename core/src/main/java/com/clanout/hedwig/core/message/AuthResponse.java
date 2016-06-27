package com.clanout.hedwig.core.message;

public final class AuthResponse implements Message
{
    public static final int TYPE = 0x2;

    private final String id;
    private final String requestId;
    private final String sessionId;
    private final String host;
    private final int port;
    private final long timestamp;

    public AuthResponse(String id, String requestId, String sessionId, String host, int port, long timestamp)
    {
        this.id = id;
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.host = host;
        this.port = port;
        this.timestamp = timestamp;
    }

    @Override
    public int getType()
    {
        return TYPE;
    }

    @Override
    public String getId()
    {
        return id;
    }

    public String getRequestId()
    {
        return requestId;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }

    @Override
    public long getTimestamp()
    {
        return timestamp;
    }
}
