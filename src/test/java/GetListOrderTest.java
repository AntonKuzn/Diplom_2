import org.example.RestConfig;
import org.example.ingredients.Ingredient;
import org.example.order.Order;
import org.example.order.OrderAPI;
import org.example.user.User;
import org.example.user.UserAPI;
import org.example.user.UserGeneration;
import static org.example.ingredients.IngredientReq.getIngredientFromArray;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class GetListOrderTest {
    private final UserAPI userSteps = new UserAPI();
    private final OrderAPI orderSteps = new OrderAPI();
    private String accessToken;
    private Response response;
    private Ingredient validIngredient;

    @Before
    public void setUp() {
        RestAssured.baseURI = RestConfig.URL;
        validIngredient = getIngredientFromArray();
    }
    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }
    @Test
    @DisplayName("Создание заказа авторизованного пользователя ")
    public void createOrderWithAuthorizedUser() {
        User user = UserGeneration.createUserWithRandomData();
        Order order = new Order(validIngredient);
        response = userSteps.userCreate(user);
        accessToken = response.then().extract().body().path("accessToken");
        response = orderSteps.createOrderWithAuthorized(order, accessToken);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);

    }
    @Test
    @DisplayName("Создание заказа неавторизованного пользователя")
    public void createOrderWithoutUserLogin() {
        Order order = new Order(validIngredient);
        response = orderSteps.createOrderWithAuthorized(order, "");
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredient() {
        User user = UserGeneration.createUserWithRandomData();
        Order order = new Order();
        response = userSteps.userCreate(user);
        response = orderSteps.createOrderWithoutAuthorized(order);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(400);
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов с сообщением об ошибке")
    public void createOrderWithEmptyIngredientHashReturnError() {
        User user = UserGeneration.createUserWithRandomData();
        Order order = new Order();
        response = userSteps.userCreate(user);
        response = orderSteps.createOrderWithoutAuthorized(order);
        response.then()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }
    @Test
    @DisplayName("Заказ с не валидными ингредиентами")
    public void createOrderWithWrongIngredient() {
        validIngredient.set_id("MutantIngredientTokenWussHere.");
        Order order = new Order(validIngredient);
        User user = UserGeneration.createUserWithRandomData();
        response = userSteps.userCreate(user);
        accessToken = response.then().extract().body().path("accessToken");
        response = orderSteps.createOrderWithAuthorized(order, accessToken);
        response.then().
                statusCode(500);
    }
    @Test
    @DisplayName("Посмотреть все ингредиенты")
    public void getStatusOfIngredients() {
        response = orderSteps.getIngredientsData();
        response.then()
                .body("success", equalTo(true))
                .and()
                .body("data", notNullValue())
                .and()
                .statusCode(200);
    }
}
