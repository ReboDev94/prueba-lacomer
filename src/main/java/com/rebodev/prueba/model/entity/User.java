package com.rebodev.prueba.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "last_first_name")
    private String lastFirstName;
    @Column(name = "last_second_name")
    private String lastSecondName;
    @Column(name = "email")
    private String email;
    @Column(name = "cp")
    private String cp;
    @Column(name = "settement_type")
    private String settementType;
    @Column(name = "municipality")
    private String municipality;
    @Column(name = "state")
    private String state;
    @Column(name = "city")
    private String city;


}
