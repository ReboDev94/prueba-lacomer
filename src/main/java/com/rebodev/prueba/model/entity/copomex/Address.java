package com.rebodev.prueba.model.entity.copomex;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Builder.Default
    private String cp = "";
    @Builder.Default
    private String tipo_asentamiento = "";
    @Builder.Default
    private String municipio = "";
    @Builder.Default
    private String estado = "";
    @Builder.Default
    private String ciudad = "";
}
