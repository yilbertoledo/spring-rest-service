package com.yet88.springboot.springrestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootApplication
public class SpringRestServiceApplication
{

    public static void main(String[] args)
    {     
        SpringApplication.run(SpringRestServiceApplication.class, args);
    }
    
    /**
     * Generates BCrypt passwords
     */
    public static void printTestPassword()
    {
        System.out.println("Password=>" + BCrypt.hashpw("test-password",BCrypt.gensalt(4)));
    }

}
