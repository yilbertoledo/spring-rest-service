package com.yet88.springboot.springrestservice.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

import com.yet88.springboot.springrestservice.Constants;

@Configuration
@ConditionalOnProperty(value = "authentication.type", havingValue = "digest", matchIfMissing = false)
public class SecurityConfigDigestAuth extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // register digest entry point
        http.addFilter(digestAuthenticationFilter())
                // on exception ask for digest authentication
                .exceptionHandling().authenticationEntryPoint(digestEntryPoint())
                // disable csrf
                .and().csrf().disable().headers().frameOptions().disable()
                // it indicate basic authentication is requires
                // .and().httpBasic()
                // root will be accessible directly, no need of any
                // authentication.
                .and().authorizeRequests().antMatchers("/").permitAll().anyRequest().authenticated();
    }

    DigestAuthenticationFilter digestAuthenticationFilter() throws Exception
    {
        DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
        digestAuthenticationFilter.setUserDetailsService(userDetailsServiceBean());
        digestAuthenticationFilter.setAuthenticationEntryPoint(digestEntryPoint());
        return digestAuthenticationFilter;
    }

    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean()
    {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername(Constants.INMEMORY_USER_NAME)
                .password(customPasswordEncoder().encode(Constants.INMEMORY_USER_PASSWD))
                .roles(Constants.INMEMORY_USER_ROLE).build());
        return inMemoryUserDetailsManager;

    }

    @Bean
    DigestAuthenticationEntryPoint digestEntryPoint()
    {
        DigestAuthenticationEntryPoint bauth = new DigestAuthenticationEntryPoint();
        bauth.setRealmName(Constants.DIGEST_REALM);
        bauth.setKey("MySecureKey");
        return bauth;
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception
    {
        return authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsServiceBean());
    }

    @Bean
    public PasswordEncoder customPasswordEncoder()
    {
        // We create a new plain text encoder.
        // Sadly DigestAuthenticationFilter requires access to plain text
        // passwords in order to work.
        // Another possible approach is to use the provided NoOpPasswordEncoder
        return new PasswordEncoder()
        {
            @Override
            public String encode(CharSequence rawPassword)
            {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword)
            {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }

    /*
     * Custom Encoder for specific code/decode process
     * 
     * @Bean public PasswordEncoder customPasswordEncoder() { return new
     * PasswordEncoder() {
     * 
     * @Override public String encode(CharSequence rawPassword) { String
     * passwdString = BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(4));
     * System.out.println("Encoded Password>" + passwdString); return
     * passwdString; }
     * 
     * @Override public boolean matches(CharSequence rawPassword, String
     * encodedPassword) { boolean match = BCrypt.checkpw(rawPassword.toString(),
     * encodedPassword); System.out.println("Decode Matches?>" +
     * String.valueOf(match)); return match; } }; }
     */

}
