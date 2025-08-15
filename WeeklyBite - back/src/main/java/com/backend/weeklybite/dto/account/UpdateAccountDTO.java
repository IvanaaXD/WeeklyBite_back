package com.backend.weeklybite.dto.account;

import com.backend.weeklybite.domain.enums.AccountStatus;
import com.backend.weeklybite.domain.enums.Role;

public class UpdateAccountDTO {

    private String password;
    private Role role;
    private AccountStatus accountStatus;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }
}
