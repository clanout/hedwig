package com.clanout.hedwig.gson_converter;

import com.clanout.hedwig.core.converter.MessageConverter;
import com.clanout.hedwig.core.converter.MessageDecodingException;
import com.clanout.hedwig.core.converter.MessageEncodingException;
import com.clanout.hedwig.core.message.AuthRequest;
import com.clanout.hedwig.core.message.AuthResponse;
import com.clanout.hedwig.core.message.ErrorMessage;
import com.clanout.hedwig.core.message.Message;
import com.google.gson.Gson;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GsonMessageConverter implements MessageConverter
{
    private Gson gson;
    private Map<Integer, Class<? extends Message>> types;

    public GsonMessageConverter()
    {
        this(new Gson());
    }

    public GsonMessageConverter(Gson gson)
    {
        this.gson = gson;
        types = new HashMap<>();

        types.put(ErrorMessage.TYPE, ErrorMessage.class);
        types.put(AuthRequest.TYPE, AuthRequest.class);
        types.put(AuthResponse.TYPE, AuthResponse.class);
    }

    @Override
    public <T extends Message> byte[] encode(T message) throws MessageEncodingException
    {
        try
        {
            int type = message.getType();
            String json = gson.toJson(message);

            ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + json.length());
            buffer.putInt(type);
            buffer.putInt(json.length());
            buffer.put(json.getBytes());

            return buffer.array();
        }
        catch (Exception e)
        {
            throw new MessageEncodingException();
        }
    }

    @Override
    public <T extends Message> T decode(byte[] bytes) throws MessageDecodingException
    {
        try
        {
            byte[] dataBytes = Arrays.copyOfRange(bytes, 8, bytes.length);

            ByteBuffer buffer = ByteBuffer.allocate(bytes.length).put(bytes);
            buffer.flip();

            int type = buffer.getInt();
            Class<? extends Message> typeClass = types.get(type);
            if (typeClass != null)
            {
                String json = new String(dataBytes);
                return (T) gson.fromJson(json, typeClass);
            }
            else
            {
                throw new MessageDecodingException("invalid message type");
            }
        }
        catch (MessageDecodingException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new MessageDecodingException();
        }
    }
}
