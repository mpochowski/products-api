package com.godaddy.uk.productsapi.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Adjusts Spring Security configuration such as open/closed requests, auth type, frame options, etc.
     *
     * @param http the http configuration object
     * @throws Exception throws any Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // open registration request
                .antMatchers(HttpMethod.POST, "/users").permitAll()

                // open swagger requests
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/webjars/springfox-swagger-ui/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/csrf").permitAll()

                // any other request require authentication
                .anyRequest().authenticated()

                // allow frames from the same origin (h2 console)
                .and().headers().frameOptions().sameOrigin()

                // basic http
                .and().httpBasic()

                // disable csrf - it's generally not a good thing to disable csrf and use basic auth (cookie based)
                // either csrf should be enabled or basic auth should be replaced with other auth such as jwt or oauth2 (assumed out of scope of this project)
                .and().csrf().disable();
    }

    /**
     * Defines password encoder bean.
     *
     * @return password encoder implementation
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}