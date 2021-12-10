package com.holoride.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ROLE1 = "ADMIN";
    private static final String ROLE2 = "USER";

    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/h2-console/**").permitAll() //just for test purpose
                .antMatchers("/api/users").hasAnyRole(ROLE1, ROLE2)
                .antMatchers("/api/user").hasAnyRole(ROLE1, ROLE2)
                .antMatchers("/api/add").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .mvcMatchers("/api/users/{userId}").hasAnyRole(ROLE1, ROLE2)
                .anyRequest().authenticated().and().
                exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
