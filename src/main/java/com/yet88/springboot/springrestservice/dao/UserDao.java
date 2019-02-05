package com.yet88.springboot.springrestservice.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.yet88.springboot.springrestservice.model.User;

@Repository
public interface UserDao extends CrudRepository<User, Long>
{
    User findByUsername(String username);
}