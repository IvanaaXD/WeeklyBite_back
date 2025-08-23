package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.ActivationToken;
import com.backend.weeklybite.domain.Person;
import com.backend.weeklybite.domain.User;
import com.backend.weeklybite.domain.UserAccount;
import com.backend.weeklybite.domain.enums.AccountStatus;
import com.backend.weeklybite.domain.enums.Role;
import com.backend.weeklybite.dto.user.CreateUserDTO;
import com.backend.weeklybite.dto.user.CreatedUserDTO;
import com.backend.weeklybite.dto.user.GetUserDTO;
import com.backend.weeklybite.dto.user.UpdateUserDTO;
import com.backend.weeklybite.exception.EmailServiceException;
import com.backend.weeklybite.repository.AccountRepository;
import com.backend.weeklybite.repository.ActivationTokenRepository;
import com.backend.weeklybite.repository.PersonRepository;
import com.backend.weeklybite.service.interfaces.IPersonService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PersonService implements IPersonService {

    @Autowired
    private AccountRepository allAccounts;
    @Autowired
    private PersonRepository allUsers;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ActivationTokenRepository activationTokenRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;

    public PersonService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CreatedUserDTO create(CreateUserDTO createUser, MultipartFile profilePictureFile, MultipartFile[] agencyPictureFiles) throws Exception {

        if (profilePictureFile != null && !profilePictureFile.isEmpty()) {
            String fileName = fileStorageService.storeFile(profilePictureFile);
            createUser.setProfilePicture(fileName);
        } else {
            createUser.setProfilePicture(null);
        }

        UserAccount account = createAccount(createUser.getEmail(), createUser.getPassword(), createUser.getRole());
        CreatedUserDTO createdUser = create(createUser, account);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(24);
        ActivationToken activationToken = new ActivationToken(token, account, expiryDate);
        activationTokenRepository.save(activationToken);

        try {
            emailService.sendActivationEmail(account.getEmail(), createdUser.getFirstName(), token);
        } catch (Exception e) {
            throw new EmailServiceException("Failed to send activation email: " + e.getMessage(), e);
        }
        return createdUser;
    }

    @Override
    public GetUserDTO getUserById(Long id) {
        Person user = allUsers.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person with id " + id + " not found"));
        GetUserDTO dto = modelMapper.map(user, GetUserDTO.class);
        dto.setEmail(user.getUserAccount().getEmail());
        return dto;
    }

    @Override
    public GetUserDTO getUserByEmail(String email) {
        Person user = allUsers.findByUserAccountEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Person with id " + email + " not found"));
        GetUserDTO dto = modelMapper.map(user, GetUserDTO.class);
        return dto;
    }

    @Override
    public GetUserDTO update(UpdateUserDTO updateUser) {

        Person existingPerson = authService.getAuthenticatedUser();

        existingPerson.setFirstName(updateUser.getFirstName());
        existingPerson.setLastName(updateUser.getLastName());
        existingPerson.setPhoneNumber(updateUser.getPhoneNumber());
        existingPerson.setBirthLocation(updateUser.getBirthLocation());

        Person updatedPerson = allUsers.save(existingPerson);
        GetUserDTO userResponse = modelMapper.map(updatedPerson, GetUserDTO.class);

        userResponse.setEmail(updatedPerson.getUserAccount().getEmail());
        userResponse.setRole(updatedPerson.getUserAccount().getRole());

        return userResponse;
    }

    @Override
    public void delete(Long id) {
        Person user = allUsers.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Person not found"));

        allUsers.delete(user);
    }

    private CreatedUserDTO create(CreateUserDTO createUser, UserAccount userAccount) {
        User user = new User();
        user.setFirstName(createUser.getFirstName());
        user.setLastName(createUser.getLastName());
        user.setPhoneNumber(createUser.getPhoneNumber());
        user.setProfilePicture(createUser.getProfilePicture());
        user.setUserAccount(userAccount);
        user.setBirthLocation(createUser.getBirthLocation());

        User newEventOrganizer = allUsers.save(user);
        return modelMapper.map(newEventOrganizer, CreatedUserDTO.class);
    }

    private UserAccount createAccount(String email, String password, Role role) {

        if (allAccounts.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use.");
        }

        UserAccount newAccount = new UserAccount();
        newAccount.setEmail(email);
        newAccount.setPassword(passwordEncoder.encode(password));
        newAccount.setAccountStatus(AccountStatus.PENDING);
        newAccount.setRole(role);

        return newAccount;
    }

    @Override
    public void uploadProfilePicture(MultipartFile profilePicture) {
        Person user = authService.getAuthenticatedUser();

        if (profilePicture != null && !profilePicture.isEmpty()) {
            String fileName = fileStorageService.storeFile(profilePicture);
            user.setProfilePicture(fileName);
        }

        allUsers.save(user);
    }

    @Override
    public GetUserDTO getCurrentUser() {
        Person user = authService.getAuthenticatedUser();

        GetUserDTO getUserDTO = modelMapper.map(user, GetUserDTO.class);
        getUserDTO.setEmail(user.getUserAccount().getEmail());
        getUserDTO.setRole(user.getUserAccount().getRole());
        if (getUserDTO.getProfilePicture() != null && !getUserDTO.getProfilePicture().isEmpty()) {
            getUserDTO.setProfilePicture(fileStorageService.getFileUrl(getUserDTO.getProfilePicture()));
        } else {
            getUserDTO.setProfilePicture(null);
        }
        return getUserDTO;
    }

}
