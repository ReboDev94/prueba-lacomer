package com.rebodev.prueba.controller;

import com.rebodev.prueba.model.dto.UserDto;
import com.rebodev.prueba.model.entity.User;
import com.rebodev.prueba.model.entity.copomex.Address;
import com.rebodev.prueba.model.payload.MessageResponse;
import com.rebodev.prueba.service.IAddressService;
import com.rebodev.prueba.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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


    @Operation(
            summary = "Guardar datos de un usuario",
            description = "Realiza el guardado de los datos de un usuario",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Guardado exitoso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "No pasa la validacion de los campos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))

                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ocurrio un error al guardar los datos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))

                    )
            }
    )
    @PostMapping("user")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody UserDto userDto) {
        Address address = addressService.getAddressByCp(userDto.getCp());
        User userSave = null;
        try {
            userDto.setCp(address.getCp());
            userDto.setCity(address.getCiudad());
            userDto.setMunicipality(address.getMunicipio());
            userDto.setState(address.getEstado());
            userDto.setSettementType(address.getTipo_asentamiento());
            userSave = userService.save(userDto);
            userDto = this.mapUserToDto(userSave);

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


    @Operation(
            summary = "Actualizar los datos de un usuario",
            description = "Realiza la actualización de los datos de un usuario",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Actualización exitosa",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "El registro no existe",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "No pasa la validacion de los campos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))

                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ocurrio un error al guardar los datos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))

                    )
            }
    )
    @PutMapping("user/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> update(@Valid @RequestBody UserDto userDto, @PathVariable Integer id) {
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
                                .build(), HttpStatus.NOT_FOUND
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

    @Operation(
            summary = "Eliminar datos de un usuario",
            description = "Realiza la eliminación de los datos de un usuario por id",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Eliminación exitosa"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No existe el registro para eliminar",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))

                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ocurrio al eliminar al usuario",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))

                    )
            }
    )
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
                            .build(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Obtener usuario",
            description = "Obtener los datos de un usuario por id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Retorna los datos correctamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No se encontró ninguna coincidencia",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))

                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ocurrio un error al obtener los datos del usuario",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))

                    )
            }
    )
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

        UserDto userDto = this.mapUserToDto(user);

        return new ResponseEntity<>(
                MessageResponse.builder()
                        .message("")
                        .data(userDto)
                        .build(), HttpStatus.OK);
    }

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Obtener los datos de todos los usuarios",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Retorna los datos correctamente"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Ocurrio un error al obtener los datos de los usuarios",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))

                    )
            }
    )
    @GetMapping("user")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {

        try {
            Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<User> paginateUsers = userService.findAll(pageable);
            Page<UserDto> users = paginateUsers.map(this::mapUserToDto);

            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message("")
                            .data(users)
                            .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message("Ocurrio un error al obtener los datos de los usuarios")
                            .data(null)
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private UserDto mapUserToDto(User user) {
        return UserDto.builder()
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
    }
}
