package com.backend.weeklybite.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatedUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;
    private String birthLocation;
}

