package com.backend.weeklybite.dto.user;

import com.backend.weeklybite.domain.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserDTO {
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email format is invalid")
    @Size(max = 100, message = "Email cannot be longer than 100 characters")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\+?[0-9]{6,15}$", message = "Phone number is invalid. It must contain between 6 and 15 digits and can start with a '+'.")
    private String phoneNumber;

    private String profilePicture;

    @NotBlank(message = "Birth location cannot be empty")
    @Size(max = 100, message = "Birth location cannot be longer than 100 characters")
    private String birthLocation;

    @NotNull(message = "Role cannot be empty")
    private Role role;
}
