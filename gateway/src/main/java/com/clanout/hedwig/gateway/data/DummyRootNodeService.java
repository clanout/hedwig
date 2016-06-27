package com.clanout.hedwig.gateway.data;

import com.clanout.hedwig.gateway.domain.model.Node;
import com.clanout.hedwig.gateway.domain.service.RootNodeService;
import com.clanout.hedwig.gateway.domain.model.User;

import java.util.UUID;

public class DummyRootNodeService implements RootNodeService
{
    @Override
    public String createSession(User user)
    {
        return UUID.randomUUID().toString();
    }

    @Override
    public Node getAvailableNode()
    {
        return new Node("0.0.0.0", 8888);
    }
}
