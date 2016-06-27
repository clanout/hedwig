package com.clanout.hedwig.client;

import com.clanout.hedwig.core.message.Message;

public interface MessageDelegate
{
    void onRead(Message input);
}
