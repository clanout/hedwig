package com.clanout.hedwig.core.message;

public class ErrorMessage implements Message
{
    public static final int TYPE = 0x0;

    private final String id;
    private final int code;
    private final String description;
    private final long timestamp;

    public ErrorMessage(String id, int code, String description, long timestamp)
    {
        this.id = id;
        this.code = code;
        this.description = description;
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

    public int getCode()
    {
        return code;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public long getTimestamp()
    {
        return timestamp;
    }
}
