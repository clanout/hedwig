package com.clanout.hedwig.gateway.domain.service;

import com.clanout.hedwig.gateway.domain.model.Node;
import com.clanout.hedwig.gateway.domain.model.User;

public interface RootNodeService
{
    String createSession(User user);

    Node getAvailableNode();
}
