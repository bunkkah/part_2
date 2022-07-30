package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import model.CreateUser;
import model.IngredientsResponse;
import model.LoginUserRequest;
import model.OrderRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiClient extends PlaceholderRestClient {

    @Step("Получение ингредиентов")
    public IngredientsResponse getIngredients() {
        return given()
                .spec(baseSpec())
                .get("ingredients")
                .body()
                .as(IngredientsResponse.class);
    }

    @Step("Создание пользователя")
    public ValidatableResponse createUser(CreateUser createUser) {

        return given()
                .spec(baseSpec())
                .body(createUser)
                .when()
                .post("auth/register")
                .then();
    }

    @Step("Удаление пользователя")
    public void deleteUser(CreateUser createUser) {
        if (createUser.getAccessToken() != null) {
            given()
                    .spec(baseSpec())
                    .when()
                    .delete("auth/user")
                    .then();
        }
    }

    @Step("Получение информации о пользователе")
    public ValidatableResponse getDataUser(String accessToken) {

        return given()
                .header("Authorization", accessToken)
                .spec(baseSpec())
                .when()
                .get("auth/user")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Step("Обновление информации о пользователе с токеном")
    public Response setDataUserWithToken(String accessToken, CreateUser createUser) {

        return given()
                .header("Authorization", accessToken)
                .spec(baseSpec())
                .when()
                .body(createUser)
                .patch("auth/user");
    }

    @Step("Обновление информации о пользователе без токена")
    public Response setDataUserWithoutToken(CreateUser createUser) {

        return given()
                .spec(baseSpec())
                .when()
                .body(createUser)
                .patch("auth/user");
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(LoginUserRequest loginUserRequest) {
        return given()
                .spec(baseSpec())
                .body(loginUserRequest)
                .when()
                .post("auth/login/")
                .then();
    }

    @Step("Создать заказ без авторизации")
    public ValidatableResponse createOrderWithoutAuthorization(OrderRequest orderRequest) {
        return given()
                .spec(baseSpec())
                .when()
                .body(orderRequest)
                .post("orders")
                .then();
    }

    @Step("Создать заказ с авторизацией")
    public ValidatableResponse createOrderWithAuthorization(String accessToken, OrderRequest orderRequest) {

        return given()
                .header("Authorization", accessToken)
                .spec(baseSpec())
                .when()
                .body(orderRequest)
                .post("orders")
                .then();
    }

    @Step("Получение заказа конкретного пользователя c авторизацией")
    public ValidatableResponse getOderUserWithAuthorization(String accessToken) {

        return given()
                .header("Authorization", accessToken)
                .spec(baseSpec())
                .get("orders")
                .then();
    }

    @Step("Получение заказа конкретного пользователя без авторизации")
    public ValidatableResponse getOderUserWithoutAuthorization() {

        return given()
                .spec(baseSpec())
                .get("orders")
                .then();
    }
}