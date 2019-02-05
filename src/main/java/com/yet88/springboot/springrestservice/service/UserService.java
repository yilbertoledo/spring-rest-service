package com.yet88.springboot.springrestservice.service;

import java.util.List;

import com.yet88.springboot.springrestservice.dto.UserDto;
import com.yet88.springboot.springrestservice.model.User;

public interface UserService
{
    User save(UserDto user);

    List<User> findAll();

    void delete(long id);

    User findOne(String username);

    User findById(Long id);
}
