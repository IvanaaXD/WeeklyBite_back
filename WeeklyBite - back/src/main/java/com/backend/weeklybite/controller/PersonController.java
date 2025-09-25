package com.backend.weeklybite.controller;

import com.backend.weeklybite.dto.user.CreateUserDTO;
import com.backend.weeklybite.dto.user.GetUserDTO;
import com.backend.weeklybite.dto.user.UpdateUserDTO;
import com.backend.weeklybite.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/api/users")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetUserDTO> getUser(@PathVariable("id") Long id) {
        GetUserDTO user = personService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/search", params = "email")
    public ResponseEntity<GetUserDTO> getUserByEmail(@RequestParam("email") String email) {
        GetUserDTO users = personService.getUserByEmail(email);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(
            @Valid @RequestPart("user") CreateUserDTO userDTO,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePictureFile,
            @RequestPart(value = "agencyPictures", required = false) MultipartFile[] agencyPictureFiles) {
        try {
            personService.create(userDTO, profilePictureFile, agencyPictureFiles);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("{\"message\": \"Registration successful! Please check your email for an activation link.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"An unexpected error occurred during registration: " + e.getMessage() + "\"}");
        }
    }

    @PatchMapping(path = "/me", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetUserDTO> updateProfile(@Valid @RequestBody UpdateUserDTO userRequest) {
        GetUserDTO updatedUser = personService.update(userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        personService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GetUserDTO> getCurrentUser() {
        GetUserDTO user = personService.getCurrentUser();
        return ResponseEntity.ok(user);
    }

    @PostMapping(path = "/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> uploadProfilePicture(@RequestPart("profilePicture") MultipartFile profilePicture) {
        personService.uploadProfilePicture(profilePicture);
        return ResponseEntity.ok().build();
    }
}
