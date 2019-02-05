package com.yet88.springboot.springrestservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.yet88.springboot.springrestservice.Constants;

/**
 * Spring Security Class for Basic Authentication / Authorization  
 * @author yilber.toledo
 *
 */
@Configuration
@ConditionalOnProperty(value = "authentication.type", havingValue = "basic", matchIfMissing = true)
public class SecurityConfigBasicAuth extends WebSecurityConfigurerAdapter
{

    @Autowired
    private MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;

    /**
     * This is for return a 401 status code instead of 302 in case of
     * authentication failure
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
        auth.inMemoryAuthentication()
                // Basic user
                .withUser(Constants.INMEMORY_ADMIN_NAME).password(encoder().encode(Constants.INMEMORY_ADMIN_PASSWD))
                .roles(Constants.ADMIN_ROLE)
                // Admin user
                .and().withUser(Constants.INMEMORY_USER_NAME).password(encoder().encode(Constants.INMEMORY_USER_PASSWD))
                .roles(Constants.USER_ROLE);
    }

    // Authorization : Role -> Access
    protected void configure(HttpSecurity http) throws Exception
    {
        // Accessible for any user with or without authentication
        http.httpBasic().and().authorizeRequests().antMatchers("/public/**").anonymous()
                // Accessible for any authenticated user
                .antMatchers("/api/**").authenticated()
                // Accessible for authenticated users with ADMIN/USER roles.
                .antMatchers("/contacts/**").hasAnyRole(Constants.USER_ROLE, Constants.ADMIN_ROLE)
                // disable CSFR
                .and().csrf().disable().headers().frameOptions().disable()
                // Enable default login form and set handlers
                .and().formLogin().successHandler(mySuccessHandler).failureHandler(myFailureHandler);
    }

}
