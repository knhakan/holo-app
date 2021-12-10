package com.holoride.demo.service;

import java.util.Optional;

import com.holoride.demo.model.ApplicationUserDetails;
import com.holoride.demo.model.User;
import com.holoride.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        return user.map(ApplicationUserDetails::new).get();
    }

    public Long findCurrentUserId(String username) throws UsernameNotFoundException {
        return userRepository.findIdByUsername(username);
    }
}
