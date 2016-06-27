package com.clanout.hedwig.gateway.domain.service;

import com.clanout.hedwig.gateway.domain.model.User;

public interface AuthService
{
    User getUser(String token);
}
