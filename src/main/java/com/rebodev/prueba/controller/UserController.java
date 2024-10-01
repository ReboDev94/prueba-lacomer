package com.rebodev.prueba.controller;

import com.rebodev.prueba.model.dto.UserDto;
import com.rebodev.prueba.model.entity.User;
import com.rebodev.prueba.model.entity.copomex.Address;
import com.rebodev.prueba.model.payload.MessageResponse;
import com.rebodev.prueba.service.IAddressService;
import com.rebodev.prueba.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IAddressService addressService;

    @PostMapping("user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody UserDto userDto) {
        Address address = addressService.getAddressByCp(userDto.getCp());
        User userSave = null;
        try {
            userDto.setCp(address.getCp());
            userDto.setCity(address.getCiudad());
            userDto.setMunicipality(address.getMunicipio());
            userDto.setState(address.getEstado());
            userDto.setSettementType(address.getTipo_asentamiento());
            userSave = userService.save(userDto);
            userDto = UserDto.builder()
                    .id(userSave.getId())
                    .name(userSave.getName())
                    .lastFirstName(userSave.getLastFirstName())
                    .lastSecondName(userSave.getLastSecondName())
                    .email(userSave.getEmail())
                    .city(userSave.getCity())
                    .cp(userSave.getCp())
                    .municipality(userSave.getMunicipality())
                    .settementType(userSave.getSettementType())
                    .state(userSave.getState())
                    .build();

            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message("Guardado exitoso")
                            .data(userDto)
                            .build(), HttpStatus.CREATED
            );
        } catch (DataAccessException e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .data(null)
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PutMapping("user/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@RequestBody UserDto userDto, @PathVariable Integer id) {
        Address address = addressService.getAddressByCp(userDto.getCp());
        User userUpdate = null;
        try {
            if (userService.existsById(id)) {
                userDto.setId(id);
                userDto.setCp(address.getCp());
                userDto.setCity(address.getCiudad());
                userDto.setMunicipality(address.getMunicipio());
                userDto.setState(address.getEstado());
                userDto.setSettementType(address.getTipo_asentamiento());

                userUpdate = userService.save(userDto);
                userDto = UserDto.builder()
                        .id(userUpdate.getId())
                        .name(userUpdate.getName())
                        .lastFirstName(userUpdate.getLastFirstName())
                        .lastSecondName(userUpdate.getLastSecondName())
                        .email(userUpdate.getEmail())
                        .city(userUpdate.getCity())
                        .cp(userUpdate.getCp())
                        .municipality(userUpdate.getMunicipality())
                        .settementType(userUpdate.getSettementType())
                        .state(userUpdate.getState())
                        .build();
                return new ResponseEntity<>(
                        MessageResponse.builder()
                                .message("Actualización exitosa")
                                .data(userDto)
                                .build(), HttpStatus.CREATED
                );
            } else {
                return new ResponseEntity<>(
                        MessageResponse.builder()
                                .message("El registro no existe")
                                .data(null)
                                .build(), HttpStatus.INTERNAL_SERVER_ERROR
                );
            }

        } catch (DataAccessException e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .data(null)
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            User userDelete = userService.findById(id);
            userService.delete(userDelete);
            return new ResponseEntity<>(userDelete, HttpStatus.NO_CONTENT);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message("No se encontró ninguna coincidencia para el registro que intenta eliminar")
                            .data(null)
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> showById(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message("No se encontró ninguna coincidencia")
                            .data(null)
                            .build(), HttpStatus.NOT_FOUND);
        }

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastFirstName(user.getLastFirstName())
                .lastSecondName(user.getLastSecondName())
                .email(user.getEmail())
                .city(user.getCity())
                .cp(user.getCp())
                .municipality(user.getMunicipality())
                .settementType(user.getSettementType())
                .state(user.getState())
                .build();

        return new ResponseEntity<>(
                MessageResponse.builder()
                        .message("")
                        .data(userDto)
                        .build(), HttpStatus.OK);
    }

    @GetMapping("user")
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return userService.findAll(pageable);
    }
}
