package com.holoride.demo.dto;


public class AuthenticationDTO {
    private Long userId;
    private String authenticationResponse;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAuthenticationResponse() {
        return authenticationResponse;
    }

    public void setAuthenticationResponse(String authenticationResponse) {
        this.authenticationResponse = authenticationResponse;
    }
}
