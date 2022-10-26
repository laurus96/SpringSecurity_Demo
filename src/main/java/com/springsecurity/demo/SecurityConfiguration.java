package com.springsecurity.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService users) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .securityContext((securityContext) -> securityContext.requireExplicitSave(true))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .rememberMe((rememberMe) -> rememberMe.userDetailsService(users))
                .logout((logout) -> logout.deleteCookies("JSESSIONID"));


        return http.build();
    }

    /*
    * DataSource is defined in application.properties, no need to instantiate a new DataSource Bean
    *
    * */
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        /*
        * JdbcUserDetailsManager uses the default schema, if you need to change the schema and the table
        * JdbcUserDetailsManager provide some usefully methods
        * */

        return new JdbcUserDetailsManager(dataSource);
    }




}
