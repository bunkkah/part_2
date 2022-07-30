import client.ApiClient;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import model.IngredientsResponse;
import model.OrderRequest;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrderTest {
    private CreateUser createUser;
    private ApiClient apiClient;
    private String accessToken;

    @Before
    public void setUp() {

        apiClient = new ApiClient();
        createUser = CreateUser.getRandomUser();
        ValidatableResponse response = apiClient.createUser(createUser);
        accessToken = response.extract().path("accessToken");
        IngredientsResponse ingredientsResponse = apiClient.getIngredients();
        List<String> listOfIngredients = (ingredientsResponse.filterIdByName("Биокотлета из марсианской Магнолии", "Говяжий метеорит (отбивная)"));
        ValidatableResponse responseCreate = apiClient.createOrderWithAuthorization(accessToken, new OrderRequest(new String[]{listOfIngredients.get(0), listOfIngredients.get(1)}));

    }

    @After
    public void deleteUser() {
        apiClient.deleteUser(createUser);
    }

    @Test
    @DisplayName("Проверка получение заказа для неавторизованного пользователя")
    public void getOderForUserWithoutAuthorizationTest() {
        ValidatableResponse response = apiClient.getOderUserWithoutAuthorization();
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        Assert.assertEquals("Код ответа не соответствует ожидаемому", statusCode, 401);
        Assert.assertEquals("Сообщение не соответствует ожидаемому", message, "You should be authorised");
    }

    @Test
    @DisplayName("Проверка получение заказа для авторизированного пользователя")
    public void getOderForUserWithAuthorizationTest() {
        ValidatableResponse response = apiClient.getOderUserWithAuthorization(accessToken);
        response.assertThat().body("success", equalTo(true)).and().statusCode(200);
    }
}