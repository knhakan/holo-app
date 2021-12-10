package com.holoride.demo.controller;

import com.holoride.demo.dto.AuthenticationDTO;
import com.holoride.demo.exception.ResourceNotFoundException;
import com.holoride.demo.exception.UnauthorizedException;
import com.holoride.demo.model.AuthenticationRequest;
import com.holoride.demo.security.JwtUtil;
import com.holoride.demo.service.JwtUserDetailsService;
import com.holoride.demo.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/api")
public class JwtAuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUserDetailsService userDetailsService;
    @Autowired
    JwtUtil jwtTokenUtil;
    @Autowired
    UserServiceImpl userDetailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationController.class);

    AuthenticationDTO authenticationDTO = new AuthenticationDTO();

    // it creates an authentication token to be used to log in the system
    @PostMapping(value = "/authenticate")
    public ResponseEntity<AuthenticationDTO> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            LOGGER.info("User is authenticated");
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("You are not authorized");
        }

        Long userId = userDetailService.findIdByUsername(authenticationRequest.getUsername());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);
        authenticationDTO.setAuthenticationResponse(jwt);
        authenticationDTO.setUserId(userId);
        return ResponseEntity.ok(authenticationDTO);
    }

    @GetMapping("/user")
    public ResponseEntity<Long> getLoggedInUser(Principal principal) {
        Long userId;
        Boolean isThereUser = userDetailService.doesUserExist(principal.getName());
        if(Boolean.TRUE.equals(isThereUser))
         userId = userDetailsService.findCurrentUserId(principal.getName());
        else{
            throw new ResourceNotFoundException("You are not authorized");
        }
        return ResponseEntity.ok(userId);
    }
}
