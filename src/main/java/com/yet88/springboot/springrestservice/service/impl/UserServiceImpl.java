package com.yet88.springboot.springrestservice.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.yet88.springboot.springrestservice.dao.UserDao;
import com.yet88.springboot.springrestservice.dto.UserDto;
import com.yet88.springboot.springrestservice.model.User;
import com.yet88.springboot.springrestservice.service.UserService;

/**
 * Custom implementation of UserDetailsService to help Spring Security loading
 * user-specific data in the framework.
 * 
 * @author yilber.toledo
 *
 */
@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService
{

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Override
    public User save(UserDto user)
    {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setAge(user.getAge());
        newUser.setSalary(user.getSalary());
        return userDao.save(newUser);
    }

    @Override
    public List<User> findAll()
    {
        List<User> list = new ArrayList<>();
        userDao.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public void delete(long id)
    {
        userDao.deleteById(id);
    }

    @Override
    public User findOne(String username)
    {
        return userDao.findByUsername(username);
    }

    @Override
    public User findById(Long id)
    {
        return userDao.findById(id).get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userDao.findByUsername(username);
        if (user == null)
        {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user)
    {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

}
