package com.backend.weeklybite.dto.user;

import com.backend.weeklybite.domain.Person;
import com.backend.weeklybite.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;
    private String birthLocation;
    private String email;
    private Role role;

    public GetUserDTO(Person user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.profilePicture = user.getProfilePicture();
        this.birthLocation = user.getBirthLocation();
        this.email = user.getUserAccount().getEmail();
        this.role = user.getUserAccount().getRole();
    }
}
