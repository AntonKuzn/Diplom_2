import org.example.user.User;
import org.example.user.UserAPI;
import org.example.user.UserGeneration;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;



public class TestUserLogin {
    private final UserAPI userApi = new UserAPI();
    private Response response;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        user = UserGeneration.createUserWithRandomData();
        response = userApi.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
    }
    @Test
    @DisplayName("Валидная авторизация пользователя")
    @Description("Пользователь авторизован, код 200")
    public void authorizWithUser() {
        response = userApi.userLoginToken(user, accessToken);
        response
                .then().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("Не валидная авторизация")
    @Description("Ошибка пароля или почты, код 401")
    public void authorizUserWithWrongPasswordAndEmail() {
        String email = user.getEmail();
        user.setEmail("wrong@email.ru");
        String password = user.getPassword();
        user.setPassword("wrongPassword");
        response = userApi.userLoginToken(user, accessToken);
        user.setEmail(email);
        user.setPassword(password);
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
    @After
    public void cleanUp() {
        if (accessToken != null) {
            userApi.userDelete(accessToken);
        }
    }
}
