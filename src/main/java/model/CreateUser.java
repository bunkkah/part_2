package model;

import com.github.javafaker.Faker;

public class CreateUser {
    public String name;
    public String password;
    public String email;
    private String accessToken;
    private String refreshToken;

    public CreateUser(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
    public CreateUser() {
    }

    public CreateUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public CreateUser setName(String name) {
        this.name = name;
        return this;
    }

    public CreateUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public CreateUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public static CreateUser getRandomUser() {
        Faker faker = new Faker();
        String name = faker.name().name();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        return new CreateUser(name, password, email);
    }
    public String getAccessToken() {
        return accessToken;
    }
    public static CreateUser getUserWithNameAndEmail() {
        Faker faker = new Faker();
        String name = faker.name().name();
        String email = faker.internet().emailAddress();
        return new CreateUser(name,email);
    }

}