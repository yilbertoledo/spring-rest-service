package com.yet88.springboot.springrestservice.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yet88.springboot.springrestservice.dto.UserDto;
import com.yet88.springboot.springrestservice.model.User;
import com.yet88.springboot.springrestservice.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserController
{
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<User> listUser()
    {
        return userService.findAll();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User getOne(@PathVariable(value = "id") Long id)
    {
        return userService.findById(id);
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public User create(@RequestBody UserDto user)
    {
        return userService.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public User deleteUser(@PathVariable(value = "id") Long id)
    {
        userService.delete(id);
        return new User(id);
    }
}
