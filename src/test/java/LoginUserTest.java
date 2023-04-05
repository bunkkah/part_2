import client.ApiClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import model.LoginUserRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class LoginUserTest {
    private CreateUser createUser;
    private ApiClient apiClient;

    @Before
    public void setUp() {
        apiClient = new ApiClient();
        createUser = CreateUser.getRandomUser();
        apiClient.createUser(createUser);
    }

    @After
    public void deleteUser() {
        apiClient.deleteUser(createUser);
    }

    @Test
    @DisplayName("Проверка авторизации пользователя")
    public void loginUserTest() {
        ValidatableResponse response = apiClient.loginUser(LoginUserRequest.from(createUser));
        response.assertThat().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    @DisplayName("Проверка авторизации пользователя с невалидными данными")
    public void LoginUserWithInvalidLoginAndPasswordTest() {
        ValidatableResponse response = new ApiClient().loginUser(LoginUserRequest.getLoginUserWithInvalidLoginAndPassword(createUser));
        response.assertThat().body("success", equalTo(false)).and().statusCode(401);
        String messageError = response.extract().path("message");
        Assert.assertEquals("Сообщение об ошибке не соответствует ожидаемому", messageError, "email or password are incorrect");
    }

}