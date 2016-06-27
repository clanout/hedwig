package com.clanout.hedwig.core.converter;

import com.clanout.hedwig.core.message.Message;

public interface MessageConverter
{
    <T extends Message> byte[] encode(T message) throws MessageEncodingException;

    <T extends Message> T decode(byte[] bytes) throws MessageDecodingException;
}
