package com.holoride.demo;

import com.holoride.demo.controller.UserController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DemoApplicationTests {

    @Autowired
    WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserController userController;

    TestUser testUser = new TestUser();

    String username = "testuser";

    String password = "12345";

    String nonExistingUsername = "nonExistingUser";

    String nonExistingpassword = "nonExistingPassword";

    String userJSON = "{\n    \"username\" :   \"" + username + "\",\n    \"password\" :   \"" + password + "\" \n}";

    String unexistingUserJSON = "{\n    \"username\" :   \"" + nonExistingUsername + "\",\n    \"password\" :   \"" + nonExistingpassword + "\" \n}";

    String invalidUserJSON_1 = "{\n    \"username\" :   \"\",\n    \"password\" :   \"\" \n}";

    String invalidUserJSON_2 = "{\n    \"username\" :   \"\",\n    \"password\" :   \"12345\" \n}";

    String userJSONToUpdate = "{\n    \"firstName\" :   \"first\",\n    \"lastName\" :   \"last\" \n}";

    /*
       status code 200 ok
       adds new user
     */
    @Test
    @Order(1)
    public void T1_addUserTest_happy() throws Exception {

        MvcResult result = this.mockMvc.perform(post("/api/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
        String stringResult = result.getResponse().getContentAsString();
        int indexFirstName = stringResult.indexOf("\"firstName\"");
        String userIdStr = stringResult.substring(10, indexFirstName - 1);

        testUser.setUserId(Long.valueOf(userIdStr));

    }

    /* status code 400 bad request
       no username and password are provided
     */
    @Test
    @Order(2)
    public void T1_addUserTest_sad_1() throws Exception {

        MvcResult result = this.mockMvc.perform(post("/api/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(invalidUserJSON_1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    /* status code 409 conflict, the user already exists
     */
    @Test
    @Order(3)
    public void T1_addUserTest_sad_2() throws Exception {

        MvcResult result = this.mockMvc.perform(post("/api/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    /* status code 400 bad request
       username is not provided
     */
    @Test
    @Order(4)
    public void T1_addUserTest_sad_3() throws Exception {

        MvcResult result = this.mockMvc.perform(post("/api/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(invalidUserJSON_2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    /* status code 200 ok
     */
    @Test
    @Order(5)
    public void T2_authenticateTest_happy() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/authenticate")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
        String stringResult = result.getResponse().getContentAsString();
        int indexAuthenticationResponse = stringResult.indexOf("\"authenticationResponse\"");
        String userToken = stringResult.substring(indexAuthenticationResponse + 26, stringResult.length() - 2);
        testUser.setUserToken(userToken);
    }

    /* status code 401 is unauthorized
     */
    @Test
    @Order(6)
    public void T2_authenticateTest_sad() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/authenticate")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(unexistingUserJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }


    /* status code 200 ok
     */
    @Test
    @Order(7)
    public void T3_getCurrentUserIdTest_happy() throws Exception {
        String accessToken = testUser.getUserToken();
        MvcResult result = this.mockMvc.perform(get("/api/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    /* status code 403 forbidden
     */
    @Test
    @Order(8)
    public void T3_getCurrentUserIdTest_sad() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/user"))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }


    /* status code 200 ok
       update user
     */
    @Test
    @Order(9)
    public void T4_updateUserTest_happy() throws Exception {
        String accessToken = testUser.getUserToken();
        MvcResult result = this.mockMvc.perform(put("/api/users/{userId}", testUser.getUserId())
                        .content(userJSONToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    /* status code 403 forbidden
       update user without authentication
     */
    @Test
    @Order(10)
    public void T4_updateUserTest_sad() throws Exception {
        MvcResult result = this.mockMvc.perform(put("/api/users/{userId}", testUser.getUserId()))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    /* status code 200 ok
     */
    @Test
    @Order(11)
    public void T5_getAllUsersTest_happy() throws Exception {
        String accessToken = testUser.getUserToken();
        System.out.println(accessToken);
        MvcResult result = this.mockMvc.perform(get("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    /* status code 403 forbidden
       get all users without authentication
     */
    @Test
    @Order(12)
    public void T5_getAllUsersTest_sad() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    /* status code 200 ok
     */
    @Test
    @Order(13)
    public void T6_getUserByIdTest_happy() throws Exception {
        String accessToken = testUser.getUserToken();
        MvcResult result = this.mockMvc.perform(get("/api/users/{userId}", testUser.getUserId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    /* status code 403 forbidden
       get user by id without authentication
     */
    @Test
    @Order(14)
    public void T6_getUserByIdTest_sad() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/users/{userId}", testUser.getUserId()))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    /* status code 200 ok
       delete user
     */
    @Test
    @Order(16)
    public void T7_deleteUserTest_happy() throws Exception {
        String accessToken = testUser.getUserToken();
        MvcResult result = this.mockMvc.perform(get("/api/users/{userId}", testUser.getUserId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    /* status code 403 forbidden
       deletes a user not authenticated
     */
    @Test
    @Order(15)
    public void T7_deleteUserTest_sad_1() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/users/{userId}", testUser.getUserId()))
                .andExpect(status().isForbidden())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }


    /* status code 401 unauthorized
       deletes a user which user does not have authorization to delete
     */
    @Test
    @Order(17)
    public void T7_deleteUserTest_sad_2() throws Exception {
        String username = "testuser2";
        String password = "12345";
        String secondUserJSON = "{\n    \"username\" :   \"" + username + "\",\n    \"password\" :   \"" + password + "\" \n}";

        MvcResult resultFirst = this.mockMvc.perform(post("/api/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .content(secondUserJSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = testUser.getUserToken();
        MvcResult resultSecond = this.mockMvc.perform(get("/api/users/{userId}", testUser.getUserId() + 1)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isUnauthorized())
                .andReturn();
        System.out.println(resultSecond.getResponse().getContentAsString());
    }

    /* status code 404 not found
       deletes a user which does not exist
     */
    @Test
    @Order(18)
    public void T7_deleteUserTest_sad_3() throws Exception {
        String accessToken = testUser.getUserToken();
        MvcResult result = this.mockMvc.perform(get("/api/users/{userId}", testUser.getUserId() + 2)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isNotFound())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

}
