package com.rebodev.prueba.service.impl;

import com.rebodev.prueba.model.dao.UserDao;
import com.rebodev.prueba.model.dto.UserDto;
import com.rebodev.prueba.model.entity.User;
import com.rebodev.prueba.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public User save(UserDto userDto) {
        User user = User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .lastFirstName(userDto.getLastFirstName())
                .lastSecondName(userDto.getLastSecondName())
                .email(userDto.getEmail())
                .build();

        return userDao.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(int id) {
        return userDao.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void delete(User user) {
        userDao.delete(user);
    }

    @Override
    public boolean existsById(Integer id) {
        return userDao.existsById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<User> findAll(Pageable pageable) {
        return userDao.findAll(pageable);
    }


}
