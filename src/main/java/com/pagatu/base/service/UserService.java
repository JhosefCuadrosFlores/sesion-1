package com.pagatu.base.service;

import com.pagatu.base.dto.UserRequestDTO;
import com.pagatu.base.dto.UserResponseDTO;
import java.util.List;

public interface UserService {

    List<UserResponseDTO> findAll();

    UserResponseDTO findById(Long id);

    UserResponseDTO findByEmail(String email);

    UserResponseDTO create(UserRequestDTO request);

    UserResponseDTO update(Long id, UserRequestDTO request);

    void delete(Long id);
}
