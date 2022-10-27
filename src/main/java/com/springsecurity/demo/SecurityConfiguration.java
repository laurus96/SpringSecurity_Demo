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
                        .mvcMatchers("/home").authenticated() // For /home we need to be authenticated
                        .mvcMatchers("/admin").hasRole("ADMIN") // For /admin we need that our role is set to ADMIn (in DB we save the role with ROLE_ADMIN)
                        .mvcMatchers("/login", "/").permitAll() // Here we permit to all user to access /login and /
                        .anyRequest().denyAll() // any URL request that has not already been matched on is denied access
                )
                .formLogin(Customizer.withDefaults()) // Default Spring Form for Login
                .httpBasic(Customizer.withDefaults()) // Default Spring Basic HTTP Authentication RFC7617
                .securityContext((securityContext) -> securityContext.requireExplicitSave(true)) //Saving the SecurityContext
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)) // Session Creation Always
                .rememberMe((rememberMe) -> rememberMe.userDetailsService(users)) //Remember me basic implementation Java
                .logout((logout) -> logout.deleteCookies("JSESSIONID")); // At logout we delete the Cookie

        return http.build();
    }

    /*
    * DataSource is defined in application.properties, no need to instantiate a new DataSource Bean
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
