package org.example.user;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.RestConfig;
import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;

public class UserAPI extends RestConfig {

    public static RequestSpecification requestSpec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(RestConfig.URL);
    }
    @Step("Create new user")
    public Response userCreate(User user) {
        return requestSpec()
                .body(user)
                .post(RestConfig.REGISTER);
    }
    @Step("Change data of user")
    public Response userDataAccountChanging(User newUser, String token) {
        return requestSpec()
                .header("Authorization", token)
                .body(newUser)
                .patch(RestConfig.USER);
    }
    @Step("User authorized by login and token")
    public Response userLoginToken(User user, String token) {
        return requestSpec()
                .header("Authorization", token)
                .body(user)
                .post(RestConfig.LOGIN);
    }
    @Step("Delete user")
    public void userDelete(String token) {
        requestSpec()
                .header("Authorization", token)
                .delete(RestConfig.USER);
    }
}
