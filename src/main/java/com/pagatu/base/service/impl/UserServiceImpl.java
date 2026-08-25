package com.pagatu.base.service.impl;

import com.pagatu.base.dto.UserRequestDTO;
import com.pagatu.base.dto.UserResponseDTO;
import com.pagatu.base.entity.User;
import com.pagatu.base.exception.DuplicateResourceException;
import com.pagatu.base.exception.ResourceNotFoundException;
import com.pagatu.base.mapper.UserMapper;
import com.pagatu.base.repository.UserRepository;
import com.pagatu.base.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponseDTO findById(Long id) {
        return userMapper.toResponse(getUserOrThrow(id));
    }

    @Override
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un usuario con el email: " + email));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponseDTO create(UserRequestDTO request) {
        validateUniqueness(request, null);
        User saved = userRepository.save(userMapper.toEntity(request));
        return userMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO request) {
        User existing = getUserOrThrow(id);
        validateUniqueness(request, id);
        userMapper.updateEntity(existing, request);
        return userMapper.toResponse(userRepository.save(existing));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User existing = getUserOrThrow(id);
        userRepository.delete(existing);
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un usuario con el id: " + id));
    }

    private void validateUniqueness(UserRequestDTO request, Long currentId) {
        boolean emailTaken = currentId == null
                ? userRepository.existsByEmail(request.email())
                : userRepository.existsByEmailAndIdNot(request.email(), currentId);
        if (emailTaken) {
            throw new DuplicateResourceException(
                    "Ya existe un usuario con el email: " + request.email());
        }

        boolean accountTaken = currentId == null
                ? userRepository.existsByAccountNumber(request.accountNumber())
                : userRepository.existsByAccountNumberAndIdNot(request.accountNumber(), currentId);
        if (accountTaken) {
            throw new DuplicateResourceException(
                    "Ya existe una cuenta con el número: " + request.accountNumber());
        }
    }
}
