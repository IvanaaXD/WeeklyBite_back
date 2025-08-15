package com.backend.weeklybite.service.interfaces;

import com.backend.weeklybite.dto.account.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IAccountService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    GetAccountDTO getUserAccountById(Long id);

    GetAccountDTO update(Long id, UpdateAccountDTO account);

    GetAccountDTO create(CreateAccountDTO account);

    void deactivateAccount();

    void changePassword(ChangePasswordDTO passwordRequest);

    GetAccountDTO verificateAccount(VerificateAccountDTO verificateAccountDTO);

    GetAccountDTO getUserAccountByEmail(String email);
}
