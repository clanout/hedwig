package com.clanout.hedwig.client;

import com.clanout.hedwig.core.message.AuthRequest;
import com.clanout.hedwig.core.message.AuthResponse;
import com.clanout.hedwig.gson_converter.GsonMessageConverter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.UUID;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        HedwigClient hedwigClient = new HedwigClient.Builder()
                .host("0.0.0.0")
                .port(7777)
                .messageConverter(new GsonMessageConverter(gson))
                .build();

        HedwigConnection connection = hedwigClient.getConnection();

        connection.addMessageListener(message -> {
            if(message.getType() == AuthResponse.TYPE)
            {
                AuthResponse authResponse = (AuthResponse) message;
                System.out.println("Message ID: " + authResponse.getId());
                System.out.println("Session ID: " + authResponse.getSessionId());
                System.out.println("Host: " + authResponse.getHost());
                System.out.println("Port: " + authResponse.getPort());
            }
        });

        connection.setConnectionStateListener(() -> {
            System.out.println("Connection closed");
            System.exit(0);
        });

        connection.connect();

        AuthRequest authRequest = new AuthRequest(UUID.randomUUID().toString(), "hello", System.currentTimeMillis());
        connection.write(authRequest);
    }
}
