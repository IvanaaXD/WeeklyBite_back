package com.backend.weeklybite.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String firstName;
    protected String lastName;
    protected String phoneNumber;
    protected String profilePicture;
    protected String birthLocation;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    protected UserAccount userAccount;

    public Person() { }

    public Person(Long id, String firstName, String lastName, String phoneNumber, String profilePicture, String birthLocation) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.birthLocation = birthLocation;
    }

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", profilePicture='" + profilePicture + '\'' +
                ", birthLocation=" + birthLocation +
                ", userAccount=" + userAccount +
                '}';
    }
}
