package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.ActivationToken;
import com.backend.weeklybite.domain.Person;
import com.backend.weeklybite.domain.UserAccount;
import com.backend.weeklybite.domain.enums.AccountStatus;
import com.backend.weeklybite.domain.enums.Role;
import com.backend.weeklybite.dto.account.*;
import com.backend.weeklybite.repository.AccountRepository;
import com.backend.weeklybite.repository.ActivationTokenRepository;
import com.backend.weeklybite.repository.PersonRepository;
import com.backend.weeklybite.service.interfaces.IAccountService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService implements IAccountService, UserDetailsService {

    @Autowired
    private AccountRepository allAccounts;

    @Autowired
    private PersonRepository allUsers;

    @Autowired
    private AuthService authService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    @Autowired
    private @Lazy PasswordEncoder passwordEncoder;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserAccount> ret = allAccounts.findByEmail(username);
        if (!ret.isEmpty()) {
            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password(ret.get().getPassword())
                    .roles(ret.get().getRole().toString())
                    .build();
        }
        throw new UsernameNotFoundException("User not found with this username: " + username);
    }

    @Override
    public GetAccountDTO getUserAccountById(Long id) {
        UserAccount account = allAccounts.findById(id).orElse(null);
        //.orElseThrow(() -> new EntityNotFoundException("User with id "  id  " not found"));
        return modelMapper.map(account, GetAccountDTO.class);
    }

    @Override
    public GetAccountDTO update(Long id, UpdateAccountDTO account) {
        UserAccount updateAccount = allAccounts.findById(id).orElse(null);

        if (updateAccount == null) {
            return null;
        }

        updateAccount.setPassword(account.getPassword());
        updateAccount.setAccountStatus(account.getAccountStatus());
        updateAccount.setRole(account.getRole());

        UserAccount updatedAccount = allAccounts.save(updateAccount);
        return modelMapper.map(updatedAccount, GetAccountDTO.class);
    }

    @Override
    public GetAccountDTO create(CreateAccountDTO account) {
        UserAccount newAccount = new UserAccount();
        newAccount.setEmail(account.getEmail());
        newAccount.setPassword(account.getPassword());
        newAccount.setAccountStatus(AccountStatus.PENDING);
        newAccount.setRole(account.getRole().equals(Role.USER.toString()) ? Role.USER : Role.ADMIN);

        UserAccount createdAccount = allAccounts.save(newAccount);
        return modelMapper.map(createdAccount, GetAccountDTO.class);
    }

    @Override
    public void deactivateAccount() {
        UserAccount account = authService.getAuthenticatedUserAccount();
        Person user = allUsers.findByUserAccountEmail(account.getEmail()).orElse(null);

        account.setAccountStatus(AccountStatus.DEACTIVATED);
        allAccounts.save(account);
    }

    public void activateAccount(String token) {
        ActivationToken activationToken = activationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid activation token"));

        if (activationToken.isExpired()) {
            activationTokenRepository.delete(activationToken);
            throw new RuntimeException("Activation token has expired. Please register again.");
        }
        if (activationToken.isActivated()) {
            throw new RuntimeException("Account already activated.");
        }

        UserAccount user = activationToken.getUser();
        user.setAccountStatus(AccountStatus.ACTIVE);
        allAccounts.save(user);

        activationToken.setActivated(true);
        activationTokenRepository.save(activationToken);
    }

    @Override
    public void changePassword(ChangePasswordDTO passwordRequest) {
        UserAccount user = authService.getAuthenticatedUserAccount();

        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        if (passwordEncoder.matches(passwordRequest.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password cannot be the same as the old password.");
        }

        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        allAccounts.save(user);
    }

    @Override
    public GetAccountDTO verificateAccount(VerificateAccountDTO verificateAccountDTO) {
        UserAccount account = allAccounts.findByEmail(verificateAccountDTO.getEmail()).orElseGet(null);

        account.setAccountStatus(AccountStatus.ACTIVE);
        UserAccount newUserAccount = allAccounts.save(account);
        return modelMapper.map(newUserAccount, GetAccountDTO.class);
    }

    @Override
    public GetAccountDTO getUserAccountByEmail(String email) {
        UserAccount product = allAccounts.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
        return modelMapper.map(product, GetAccountDTO.class);
    }
}
