package com.backend.weeklybite.dto.account;

import com.backend.weeklybite.domain.enums.AccountStatus;
import com.backend.weeklybite.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAccountDTO {

    private Long id;
    private String email;
    private Role role;
    private AccountStatus accountStatus;
}
