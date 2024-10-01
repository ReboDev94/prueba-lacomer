package com.rebodev.prueba.service;

import com.rebodev.prueba.model.dto.UserDto;
import com.rebodev.prueba.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    User save(UserDto userDto);

    User findById(int id);

    void delete(User userDto);

    boolean existsById(Integer id);

    Page<User> findAll(Pageable pageable);
}
