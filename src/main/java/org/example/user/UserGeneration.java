package org.example.user;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class UserGeneration {

    public static Faker faker = new Faker();

    @Step("Create new user with random data")
    public static User createUserWithRandomData() {
        return new User (
                faker.name().firstName(),
                faker.internet().emailAddress(),
                faker.internet().password());
    }
    @Step("Create random  empty user ")
    public static User  createUserWithEmptyData() {
        return new User (
                "",
                "",
                "");
    }
}
