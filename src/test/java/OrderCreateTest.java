import org.example.RestConfig;
import org.example.ingredients.Ingredient;
import org.example.order.Order;
import org.example.order.OrderAPI;
import org.example.user.User;
import org.example.user.UserAPI;
import io.restassured.RestAssured;
import static org.hamcrest.Matchers.notNullValue;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.user.UserGeneration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.example.ingredients.IngredientReq.getIngredientFromArray;
import static org.hamcrest.core.IsEqual.equalTo;

public class OrderCreateTest {
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
    @DisplayName("Создание щакаща авторизованного пользователя и валидными ингредиентами")
    public void createOrderWithAuthorizUserAndValidIngredient() {
        User user = UserGeneration.createUserWithRandomData();
        Order order = new Order(validIngredient);
        response = userSteps.userCreate(user);
        accessToken = response.then().extract().body().path("accessToken");
        response = orderSteps.createOrderWithAuthorized(order, accessToken);
        response.then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Создание заказа неавторизованного пользователя и невалидными ингредиентами")
    public void createOrderWithoutUserLoginAndEmptyIng() {
        Order order = new Order(validIngredient);
        response = orderSteps.createOrderWithAuthorized(order, "");
        response.then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredient() {
        User user = UserGeneration.createUserWithRandomData();
        Order order = new Order();
        response = userSteps.userCreate(user);
        response = orderSteps.createOrderWithoutAuthorized(order);
        response.then()
                .statusCode(400)
                .and()
                .body("success", equalTo(false));
    }
    @Test
    @DisplayName("Создание заказа без ингредиентов с сообщением об ошибке")
    public void createOrderWithEmptyIngredient() {
        User user = UserGeneration.createUserWithRandomData();
        Order order = new Order();
        response = userSteps.userCreate(user);
        response = orderSteps.createOrderWithoutAuthorized(order);
        response.then()
                .statusCode(400)
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Заказ с ошибочным игредиентом")
    public void createOrderWithWrongIngred() {
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
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("data", notNullValue());
    }
}
