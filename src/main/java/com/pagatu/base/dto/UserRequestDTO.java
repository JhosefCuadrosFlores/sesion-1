package com.pagatu.base.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UserRequestDTO(
        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(max = 150, message = "El nombre completo no debe superar 150 caracteres")
        String fullName,

        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "El correo electrónico no tiene un formato válido")
        @Size(max = 150, message = "El correo electrónico no debe superar 150 caracteres")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres")
        String password,

        @Size(max = 20, message = "El teléfono no debe superar 20 caracteres")
        String phone,

        @NotBlank(message = "El número de cuenta es obligatorio")
        @Size(max = 20, message = "El número de cuenta no debe superar 20 caracteres")
        String accountNumber,

        @NotBlank(message = "El tipo de cuenta es obligatorio")
        @Pattern(regexp = "AHORROS|CORRIENTE|CTS", message = "El tipo de cuenta debe ser AHORROS, CORRIENTE o CTS")
        String accountType,

        @NotNull(message = "El saldo es obligatorio")
        @DecimalMin(value = "0.00", message = "El saldo no puede ser negativo")
        BigDecimal balance,

        @NotBlank(message = "El estado es obligatorio")
        @Pattern(regexp = "ACTIVO|INACTIVO|BLOQUEADO", message = "El estado debe ser ACTIVO, INACTIVO o BLOQUEADO")
        String status,

        @NotNull(message = "El rol es obligatorio")
        Long roleId
) {
}
