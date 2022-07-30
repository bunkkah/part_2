package model;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class LoginUserRequest {
    public String email;
    public String password;
    public String name;

    public LoginUserRequest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static LoginUserRequest from(CreateUser createUser) {
        return new LoginUserRequest(createUser.email, createUser.password, createUser.name);
    }
    public static LoginUserRequest getLoginUserWithInvalidLoginAndPassword(CreateUser createUser) {
        Faker faker = new Faker();
        createUser.setEmail(faker.internet().emailAddress());
        createUser.setPassword(RandomStringUtils.randomAlphabetic(5));
        createUser.setName(RandomStringUtils.randomAlphabetic(5));
        return new LoginUserRequest(createUser.email, createUser.password,createUser.name);
    }
}
