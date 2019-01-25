package com.yet88.springboot.springrestservice;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;
    
    /**
     * This is for return a 401 status code instead of 302 in case of authentication failure  
     */
    private SimpleUrlAuthenticationFailureHandler myFailureHandler;

    @Bean
    public PasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }

    // Authentication : User --> Roles
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.inMemoryAuthentication().withUser("admin").password(encoder().encode("adminPass")).roles("ADMIN").and()
                .withUser("user").password(encoder().encode("userPass")).roles("USER");
        // auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
        // .withUser("user1").password("secret1").roles("USER")
        // .and().withUser("admin1").password("secret1").roles("USER", "ADMIN");
    }
    
    // Authorization : Role -> Access
    protected void configure(HttpSecurity http) throws Exception
    {
        http.httpBasic()
        .and().authorizeRequests()
            .antMatchers("/public/**").anonymous() //Accessible for any user with or without authentication
            .antMatchers("/api/**").authenticated() //Accessible for any authenticated user
            .antMatchers("/contacts/**").hasAnyRole("USER", "ADMIN") //Accessible for authenticated users with "ADMIN" or  "USER" role
        .and().csrf().disable().headers().frameOptions().disable()
        .and().formLogin().successHandler(mySuccessHandler).failureHandler(myFailureHandler);
    }

}
