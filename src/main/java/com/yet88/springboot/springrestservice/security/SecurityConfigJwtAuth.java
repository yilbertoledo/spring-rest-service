package com.yet88.springboot.springrestservice.security;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.yet88.springboot.springrestservice.Constants;

/**
 * Spring Security Class for JWT Authentication / Authorization
 * 
 * @author yilber.toledo
 *
 */

@Configuration
@ConditionalOnProperty(value = "authentication.type", havingValue = "jwt", matchIfMissing = false)
public class SecurityConfigJwtAuth extends WebSecurityConfigurerAdapter
{
    @Resource(name = "userService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.cors().and().csrf().disable()

                // Allow H2 console. TODO: Disable before release
                .headers().frameOptions().sameOrigin().and().authorizeRequests().antMatchers("/h2-console/**")
                .permitAll()
                // end H2 console configurations

                // Allow unauthenticated access to /token and /signup
                .and().authorizeRequests().antMatchers("/auth/*", "/signup").permitAll()
                //Contact resource will be available only for administrators 
                .antMatchers("/contacts/**").hasRole(Constants.ADMIN_ROLE)
                // For all other paths require authentication
                .anyRequest().authenticated().and().exceptionHandling().
                // Add handler for unauthorized requests
                authenticationEntryPoint(unauthorizedHandler)
                // Configure session management
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception
    {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public BCryptPasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }
}
