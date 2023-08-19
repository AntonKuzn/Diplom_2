import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.user.User;
import org.example.user.UserAPI;
import org.example.user.UserGeneration;
import static org.example.user.UserGeneration.faker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;



public class TestChangeUserInfo {
    private final UserAPI userSteps = new UserAPI();
    private Response response;
    private User user;
    private String accessToken;
    @Before
    public void setUp() {
        user = UserGeneration.createUserWithRandomData();
        response = userSteps.userCreate(user);
        accessToken = response
                .then().extract().body().path("accessToken");
    }
    @Test
    @DisplayName("Изменение данных пользователя")
    @Description("Данные имени/почты изменены. Код 200")
    public void changeAuthorizedUpdateUser() {
        user.setName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        response = userSteps.userDataAccountChanging(user, accessToken);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("Изменение пароля")
    @Description("Пароль изменён. Код 200")
    public void changeAuthorizedUserPasswor() {
        user.setPassword(faker.internet().password());
        response = userSteps.userDataAccountChanging(user, accessToken);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("Изменение пароля не авторизованного пользователя")
    @Description("Данные не изменены, код 401")
    public void changeUserDataPasswordWithoutAuthoriz() {
        user.setPassword(faker.internet().password());
        response = userSteps.userDataAccountChanging(user, "");
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
    @Test
    @DisplayName("Изменение данных не авторизованного пользователя")
    @Description("Данные не изменены, код 401")
    public void changeUnauthorizedUser() {
        user.setName(faker.name().firstName());
        user.setEmail(faker.internet().emailAddress());
        response = userSteps.userDataAccountChanging(user, "");
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
    @After
    public void cleanUp() {
        if (accessToken != null) {
            userSteps.userDelete(accessToken);
        }
    }
}
