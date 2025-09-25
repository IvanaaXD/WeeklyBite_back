package com.backend.weeklybite.service.interfaces;

import com.backend.weeklybite.dto.user.CreateUserDTO;
import com.backend.weeklybite.dto.user.CreatedUserDTO;
import com.backend.weeklybite.dto.user.GetUserDTO;
import com.backend.weeklybite.dto.user.UpdateUserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IPersonService {
    CreatedUserDTO create(CreateUserDTO createUser, MultipartFile profilePictureFile, MultipartFile[] agencyPictureFiles) throws Exception;

    GetUserDTO getUserById(Long id);

    GetUserDTO getUserByEmail(String email);

    GetUserDTO update(UpdateUserDTO updateUser);

    void delete(Long id);

    void uploadProfilePicture(MultipartFile profilePicture);

    GetUserDTO getCurrentUser();
}
