package com.backend.weeklybite.repository;

import com.backend.weeklybite.domain.Person;
import com.backend.weeklybite.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface PersonRepository  extends JpaRepository<Person, Long> {
    @Query("SELECT p FROM Person p WHERE p.userAccount.email = :email")
    Optional<User> findByUserAccountActiveEmail(@Param("email") String email);

    @Query("SELECT p FROM Person p WHERE p.userAccount.email = :email")
    Optional<Person> findByUserAccountEmail(@Param("email") String email);

}
