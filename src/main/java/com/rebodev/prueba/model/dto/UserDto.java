package com.rebodev.prueba.model.dto;

import lombok.*;
import java.io.Serializable;

@Data
@ToString
@Builder
public class UserDto implements Serializable {
    private Integer id;
    private String name;
    private String lastFirstName;
    private String lastSecondName;
    private String email;
}
