package com.backend.weeklybite.dto.user;

import com.backend.weeklybite.dto.account.GetAccountDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatedUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;
    private String birthLocation;
    private GetAccountDTO userAccount;
}
