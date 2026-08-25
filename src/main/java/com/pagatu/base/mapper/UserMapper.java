package com.pagatu.base.mapper;

import com.pagatu.base.dto.UserRequestDTO;
import com.pagatu.base.dto.UserResponseDTO;
import com.pagatu.base.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDTO request) {
        return User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(request.password())
                .phone(request.phone())
                .accountNumber(request.accountNumber())
                .accountType(request.accountType())
                .balance(request.balance())
                .status(request.status())
                .roleId(request.roleId())
                .build();
    }

    public void updateEntity(User entity, UserRequestDTO request) {
        entity.setFullName(request.fullName());
        entity.setEmail(request.email());
        entity.setPassword(request.password());
        entity.setPhone(request.phone());
        entity.setAccountNumber(request.accountNumber());
        entity.setAccountType(request.accountType());
        entity.setBalance(request.balance());
        entity.setStatus(request.status());
        entity.setRoleId(request.roleId());
    }

    public UserResponseDTO toResponse(User entity) {
        return new UserResponseDTO(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAccountNumber(),
                entity.getAccountType(),
                entity.getBalance(),
                entity.getStatus(),
                entity.getRoleId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
