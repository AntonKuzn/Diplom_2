package org.example;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestConfig {
    public static final String URL = "https://stellarburgers.nomoreparties.site/";
    public static final String INGREDIENTS = "api/ingredients";
    public static final String ORDERS = "api/orders";
    public static final String REGISTER = "api/auth/register";
    public static final String LOGIN = "api/auth/login";
    public static final String USER = "api/auth/user";
}

