package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.Person;
import com.backend.weeklybite.domain.UserAccount;
import com.backend.weeklybite.exception.UserNotFoundException;
import com.backend.weeklybite.repository.AccountRepository;
import com.backend.weeklybite.repository.PersonRepository;
import com.backend.weeklybite.service.interfaces.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private PersonRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    public AuthService() {}

    @Override
    public Person getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UsernameNotFoundException("User not authenticated.");
        }

        String userEmail = authentication.getName();

        return userRepository.findByUserAccountActiveEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user with email " + userEmail + " not found in database."));
    }

    @Override
    public UserAccount getAuthenticatedUserAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new UsernameNotFoundException("User not authenticated.");
        }

        String userEmail = authentication.getName();

        return accountRepository.findByActiveEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Authenticated user with email " + userEmail + " not found in database."));
    }
}
