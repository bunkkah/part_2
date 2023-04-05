import client.ApiClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {
    private CreateUser createUser;
    private ApiClient apiClient;

    @Before
    public void setUp() {
        apiClient = new ApiClient();
        createUser = CreateUser.getRandomUser();
    }

    @After
    public void deleteUser() {
        apiClient.deleteUser(createUser);
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    public void createCourier() {
        ValidatableResponse response = apiClient.createUser(createUser);
        response.assertThat().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    @DisplayName("Проверка, что нельзя создать двух одинаковых пользователей")
    public void createIdenticalUser() {
        apiClient.createUser(createUser);
        ValidatableResponse response = apiClient.createUser(createUser);
        int statusCode = response.extract().statusCode();
        String messageError = response.extract().path("message");

        Assert.assertEquals("Код ответа не соответствует ожидаемому", statusCode, 403);
        Assert.assertEquals("Сообщение об ошибке не соответствует ожидаемому", messageError, "User already exists");

    }

    @Test
    @DisplayName("Проверка создания пользователя без указания пароля")
    public void createUserWithNameAndEmail() {
        createUser = CreateUser.getUserWithNameAndEmail();
        ValidatableResponse response = apiClient.createUser(createUser);
        int statusCode = response.extract().statusCode();
        String messageError = response.extract().path("message");

        Assert.assertEquals("Код ответа не соответствует ожидаемому", statusCode, 403);
        Assert.assertEquals("Сообщение об ошибке не соответствует ожидаемому", messageError, "Email, password and name are required fields");
    }
}