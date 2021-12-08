package com.holoride.demo.service;

import com.holoride.demo.controller.UserController;
import com.holoride.demo.model.User;
import com.holoride.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @Override
    public User addUser(User userToSave) {
        User newUser = new User();
        newUser.setUsername(userToSave.getUsername());
        String encodedPassword = new BCryptPasswordEncoder().encode(userToSave.getPassword());
        newUser.setPassword(encodedPassword);
        newUser.setRoles("ROLE_USER"); // for demo purpose, I have assigned role automatically
        return userRepository.save(newUser);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User updateUser(Long userId, User userToUpdate) {

        return userRepository.findById(userId)
                .map(user -> {
                    user.setFirstName(userToUpdate.getFirstName());
                    user.setLastName(userToUpdate.getLastName());
                    user.setAge(userToUpdate.getAge());
                    return userRepository.save(user);
                })
                .orElseGet(() -> userRepository.save(userToUpdate));
    }

    @Override
    public boolean deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean doesUserExist(String username) {
        if (userRepository.findByUsername(username).isEmpty())
            return false;
        else
            return true;
    }

    public Long findIdByUsername(String username) {
        Long userId = userRepository.findByUsername(username).get().getUserId();
        return userId;
    }

    public Boolean isUserAllowed(Long userId, Principal principal) {
        Optional<User> user = findUserById(userId);
        if  (user.isEmpty()) {
            return false;
        }
        if (user.get().getUsername() == principal.getName()) {
            return true;
        } else {
            return false;
        }
    }

}
