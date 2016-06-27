package com.clanout.hedwig.gateway.data;

import com.clanout.hedwig.gateway.domain.service.AuthService;
import com.clanout.hedwig.gateway.domain.model.User;

import java.util.UUID;

public class DummyAuthService implements AuthService
{
    @Override
    public User getUser(String token)
    {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setFirstname("Aditya");
        user.setLastname("Prasad");
        return user;
    }
}
