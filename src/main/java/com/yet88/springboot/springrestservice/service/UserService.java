package com.yet88.springboot.springrestservice.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserService implements UserDetailsService
{
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return new User(username, "adminPass", Collections.singleton(new SimpleGrantedAuthority("ADMIN")));
    }
    
    /*
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userRepository.findByUsername(username);
        if (user == null)
        {
            throw new UsernameNotFoundException("UserName " + username + " not found");
        }
        else
        {
            return user;
        }
    }
    */

}
