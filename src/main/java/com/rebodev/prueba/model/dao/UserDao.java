package com.rebodev.prueba.model.dao;

import com.rebodev.prueba.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
}
