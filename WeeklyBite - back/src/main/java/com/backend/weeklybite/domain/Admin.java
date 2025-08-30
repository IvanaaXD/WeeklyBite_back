package com.backend.weeklybite.domain;

import jakarta.persistence.Entity;

@Entity
public class Admin extends Person {

    public Admin() {
        super();
    }

    public Admin(Long id, String name, String surname, String phoneNumber, String profilePicture, String birthLocation) {
        super(id, name, surname, phoneNumber, profilePicture, birthLocation);
    }

    @Override
    public String toString() {
        return "Admin{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", profilePicture='" + profilePicture + '\'' +
                ", birthLocation=" + birthLocation +
                '}';
    }
}
