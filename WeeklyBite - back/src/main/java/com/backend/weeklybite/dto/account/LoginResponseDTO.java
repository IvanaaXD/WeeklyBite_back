package com.backend.weeklybite.dto.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String jwt;
    private String email;
    private String role;
}