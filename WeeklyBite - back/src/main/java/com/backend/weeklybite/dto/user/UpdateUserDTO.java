package com.backend.weeklybite.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserDTO {
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9]{6,15}$", message = "Phone number is invalid. It must contain between 6 and 15 digits and can start with a '+'.")
    private String phoneNumber;

    @Size(max = 100, message = "Birth location cannot be longer than 100 characters")
    private String birthLocation;
}
