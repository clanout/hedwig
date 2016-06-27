package com.clanout.hedwig.gateway.io;

import com.clanout.hedwig.core.converter.MessageConverter;
import com.clanout.hedwig.gateway.io.server.HedwigGatewayServer;
import com.clanout.hedwig.gson_converter.GsonMessageConverter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        MessageConverter messageConverter = new GsonMessageConverter(gson);

        HedwigGatewayServer server = new HedwigGatewayServer.Builder()
                .port(7777)
                .workerPoolSize(3)
                .backgroundPoolSize(5)
                .messageConverter(messageConverter)
                .build();

        server.start();
    }
}
