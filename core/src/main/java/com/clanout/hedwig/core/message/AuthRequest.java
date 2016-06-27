package com.clanout.hedwig.core.message;

public final class AuthRequest implements Message
{
    public static final int TYPE = 0x1;

    private final String id;
    private final String token;
    private final long timestamp;

    public AuthRequest(String id, String token, long timestamp)
    {
        this.id = id;
        this.token = token;
        this.timestamp = timestamp;
    }

    @Override
    public String getId()
    {
        return id;
    }

    public String getToken()
    {
        return token;
    }

    @Override
    public long getTimestamp()
    {
        return timestamp;
    }

    @Override
    public int getType()
    {
        return TYPE;
    }
}
