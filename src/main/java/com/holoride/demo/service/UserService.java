package com.holoride.demo.service;

import com.holoride.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("userDetailsService")
public interface UserService {

    User addUser(User user);

    List<User> getAllUsers();

    Optional<User> findUserById(Long userId);

    User updateUser(Long userId, User user);

    boolean deleteUser(Long userId);

    Long findIdByUsername(String username);

}
