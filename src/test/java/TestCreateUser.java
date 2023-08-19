import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserGeneration;
import org.example.user.UserAPI;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestCreateUser {
    private final UserAPI userSteps = new UserAPI();
    private Response response;
    private User user;
    private String accessToken;

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }
    @Test
    @DisplayName("Создание валидного пользователя")
    @Description("Токен рабочий, пользователь создан, код 200")
    public void createUserValid() {
        user = UserGeneration.createUserWithRandomData();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response
                .then().body("accessToken", notNullValue())
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("Повторная регистрация зарегистрированного пользователя")
    @Description("Пользователь уже зарегистрирован, код 403")
    public void createUserWithRegBefore() {
        user = UserGeneration.createUserWithRandomData();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .statusCode(200);
        response = userSteps.userCreate(user);
        response.then()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }
    @Test
    @DisplayName("Создание пользователя без заполненного поля")
    @Description("Не заполнено поле почты, имени или пароля. Код 403")
    public void createUserWithEmptyNameField() {
        user = UserGeneration.createUserWithEmptyData();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
}
