package com.holoride.demo;

public class TestUser {
    private static String userToken;
    private static Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        TestUser.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}
