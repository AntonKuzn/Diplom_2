package org.example.order;


import io.restassured.response.Response;
import org.example.RestConfig;


import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;


public class OrderAPI extends RestConfig {
    @Step("Create order with authorized")
    public Response createOrderWithAuthorized(Order order, String token) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .baseUri(RestConfig.URL)
                .body(order)
                .post(RestConfig.ORDERS);
    }
    @Step("Create order without authorized")
    public Response createOrderWithoutAuthorized(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(RestConfig.URL)
                .body(order)
                .post(RestConfig.ORDERS);
    }
    @Step("Get data orders from user by token")
    public Response getUserDataOrder(String accessToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .baseUri(RestConfig.URL)
                .get(RestConfig.ORDERS);
    }
    @Step("Get ingredients data")
    public Response getIngredientsData() {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(RestConfig.URL)
                .get(RestConfig.INGREDIENTS);
    }
}
