package com.clanout.hedwig.core.message;

public interface Message
{
    int getType();

    String getId();

    long getTimestamp();
}
