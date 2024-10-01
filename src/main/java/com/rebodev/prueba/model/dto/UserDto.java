package com.rebodev.prueba.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;

@Data
@ToString
@Builder
public class UserDto implements Serializable {

    private Integer id;
    @NotBlank(message = "El nombre es requerido")
    private String name;
    @NotBlank(message = "El apellido paterno es requerido")
    private String lastFirstName;
    @NotBlank(message = "El apellido materno es requerido")
    private String lastSecondName;

    @Email(message = "Debe proporcionar un correo electrónico válido")
    @NotBlank(message = "El correo electrónico es requerido")
    private String email;

    @NotBlank(message = "El código postal es requerido")
    @Pattern(regexp = "\\d{5}", message = "El código postal no es valido")
    private String cp;

    private String settementType;
    private String municipality;
    private String state;
    private String city;
}
