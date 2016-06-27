package com.clanout.hedwig.client;

import com.clanout.hedwig.core.message.Message;

public interface MessageListener
{
    void onMessageReceived(Message message);
}
