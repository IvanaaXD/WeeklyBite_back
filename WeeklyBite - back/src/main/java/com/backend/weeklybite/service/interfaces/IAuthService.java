package com.backend.weeklybite.service.interfaces;

import com.backend.weeklybite.domain.Person;
import com.backend.weeklybite.domain.UserAccount;

public interface IAuthService {
    Person getAuthenticatedUser();

    UserAccount getAuthenticatedUserAccount();
}
