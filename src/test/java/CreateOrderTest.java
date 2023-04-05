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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class CreateOrderTest {
    private CreateUser createUser;
    private ApiClient apiClient;
    private String accessToken;

    @Before
    public void setUp() {

        apiClient = new ApiClient();
        createUser = CreateUser.getRandomUser();
        ValidatableResponse response = apiClient.createUser(createUser);
        accessToken = response.extract().path("accessToken");

    }

    @After
    public void deleteUser() {
        apiClient.deleteUser(createUser);
    }

    @Test
    @DisplayName("Проверка создания заказа пользователем без авторизации")
    public void createOderWithoutAuthorizationTest() {

        IngredientsResponse ingredientsResponse = apiClient.getIngredients();
        List<String> listOfIngredients = (ingredientsResponse.filterIdByName("Флюоресцентная булка R2-D3"));
        ValidatableResponse responseCreate = apiClient.createOrderWithoutAuthorization(new OrderRequest(new String[]{listOfIngredients.get(0)}));
        int statusCodeCreate = responseCreate.extract().statusCode();
        String name = responseCreate.extract().path("name");
        Assert.assertEquals(statusCodeCreate, 200);
        assertThat("Order is null", name, is(not(nullValue())));

    }

    @Test
    @DisplayName("Проверка создания заказа авторизованным пользователем")
    public void orderCanBeCreatedWithIngredientsWithAuthorized() {

        IngredientsResponse ingredientsResponse = apiClient.getIngredients();
        List<String> listOfIngredients = (ingredientsResponse.filterIdByName("Биокотлета из марсианской Магнолии", "Говяжий метеорит (отбивная)"));
        ValidatableResponse responseCreate = apiClient.createOrderWithAuthorization(accessToken, new OrderRequest(new String[]{listOfIngredients.get(0), listOfIngredients.get(1)}));
        int statusCodeCreate = responseCreate.extract().statusCode();
        String name = responseCreate.extract().path("name");
        Assert.assertEquals("Ожидаемый результат не соответствует фактическому", statusCodeCreate, 200);
        assertThat("Order is null", name, is(not(nullValue())));

    }

    @Test
    @DisplayName("Проверка создания заказа без ингредиентов")
    public void orderCanNotBeCreatedWithoutIngredients() {

        ValidatableResponse responseCreate = apiClient.createOrderWithoutAuthorization(OrderRequest.getOrderWithoutIngredients());
        int statusCodeCreate = responseCreate.extract().statusCode();
        String message = responseCreate.extract().path("message");
        Assert.assertEquals("Код ответа не соответствует ожидаемому", statusCodeCreate, 400);
        Assert.assertEquals("Сообщение не соответствует ожидаемому", message, "Ingredient ids must be provided");

    }

    @Test
    @DisplayName("Проверка создания заказа с неверным хешем ингредиентов")
    public void orderCanNotBeCreatedWithNonexistentIngredients() {

        ValidatableResponse responseCreate = apiClient.createOrderWithoutAuthorization(OrderRequest.getOrderWithDefaultHashIngredients());
        int statusCodeCreate = responseCreate.extract().statusCode();
        Assert.assertEquals("Код ответа не соответствует ожидаемому", statusCodeCreate, 500);

    }
}