package com.clanout.hedwig.gateway.domain;

import com.clanout.hedwig.core.message.AuthRequest;
import com.clanout.hedwig.core.message.AuthResponse;
import com.clanout.hedwig.core.message.ErrorMessage;
import com.clanout.hedwig.core.message.Message;
import com.clanout.hedwig.gateway.domain.model.Node;
import com.clanout.hedwig.gateway.domain.model.User;
import com.clanout.hedwig.gateway.domain.service.AuthService;
import com.clanout.hedwig.gateway.domain.service.RootNodeService;

import javax.inject.Inject;
import java.util.UUID;

public class MessageProcessor
{
    private AuthService authService;
    private RootNodeService rootNodeService;

    @Inject
    public MessageProcessor(AuthService authService, RootNodeService rootNodeService)
    {
        this.authService = authService;
        this.rootNodeService = rootNodeService;
    }

    public Message process(Message request)
    {
        if (request.getType() == AuthRequest.TYPE)
        {
            try
            {
                AuthRequest authRequest = (AuthRequest) request;

                User user = authService.getUser(authRequest.getToken());
                String sessionId = rootNodeService.createSession(user);
                Node node = rootNodeService.getAvailableNode();

                AuthResponse authResponse = new AuthResponse(UUID.randomUUID().toString(),
                        authRequest.getId(),
                        sessionId,
                        node.getHost(),
                        node.getPort(),
                        System.currentTimeMillis());

                return authResponse;
            }
            catch (Exception e)
            {
                return new ErrorMessage("0", 0, "Unable to Process message", System.currentTimeMillis());
            }
        }
        else
        {
            return new ErrorMessage("0", 0, "Invalid message type", System.currentTimeMillis());
        }
    }
}
