import client.ApiClient;
import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.equalTo;


public class ChangeUserDataTest {
    Faker faker = new Faker();
    public ApiClient apiClient;
    public CreateUser createUser;
    private String refreshToken;
    private String accessToken;


    @Before
    public void setUp() {
        apiClient = new ApiClient();
        createUser = CreateUser.getRandomUser();
        ValidatableResponse response = apiClient.createUser(createUser);
        refreshToken = response.extract().path("refreshToken");
        accessToken = response.extract().path("accessToken");
    }

    @After
    public void deleteUser() {
        boolean refreshNotNullAndIsEmpty = (refreshToken != null) && (refreshToken.isEmpty());
        if (refreshNotNullAndIsEmpty) apiClient.deleteUser(createUser);
    }

    @Test
    @DisplayName("Проверка изменения данных email авторизированного пользователя")
    public void changingUserEmailWithAuthorizationTest() {
        apiClient.getDataUser(accessToken);
        createUser.setEmail(faker.internet().emailAddress());
        Response response = apiClient.setDataUserWithToken(accessToken, new CreateUser(createUser.getEmail(), createUser.getName()));
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    @DisplayName("Проверка изменения данных имени авторизированного пользователя")
    public void changingUserNameWithAuthorizationTest() {
        apiClient.getDataUser(accessToken);
        createUser.setName(faker.name().firstName());
        Response response = apiClient.setDataUserWithToken(accessToken, new CreateUser(createUser.getEmail(), createUser.getName()));
        response.then().assertThat().body("success", equalTo(true)).and().statusCode(200);
    }

    @Test
    @DisplayName("Проверка изменения данных email неавторизированного пользователя")
    public void changingUserEmailWithNoAuthorizationTest() {
        apiClient.getDataUser(accessToken);
        createUser.setEmail(faker.internet().emailAddress());
        Response response = apiClient.setDataUserWithoutToken(new CreateUser(createUser.getEmail(), createUser.getName()));
        response.then().assertThat().body("success", equalTo(false)).and().statusCode(401);
    }

    @Test
    @DisplayName("Проверка изменения данных имени неавторизированного пользователя")
    public void changingUserNameWithNoAuthorizationTest() {
        apiClient.getDataUser(accessToken);
        createUser.setName(faker.name().firstName());
        Response response = apiClient.setDataUserWithoutToken(new CreateUser(createUser.getEmail(), createUser.getName()));
        response.then().assertThat().body("success", equalTo(false)).and().statusCode(401);
    }


}