package com.rebodev.prueba.model.entity.copomex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CopoMexResponse {
    private  boolean error;
    private int code_error;
    private String  error_message;
    private Address response;
}
