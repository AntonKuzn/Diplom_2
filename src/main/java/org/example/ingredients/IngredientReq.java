package org.example.ingredients;
import org.example.RestConfig;
import static io.restassured.RestAssured.given;

public class IngredientReq {

    public static Ingredient[] getIngredientsArray() {
        return getIngredientResponse().getIngredients();
    }

    public static IngredientResp getIngredientResponse() {
        return given()
                .get(RestConfig.INGREDIENTS)
                .as(IngredientResp.class);
    }

    public static Ingredient getIngredientFromArray() {
        return getIngredientsArray()[0];
    }
}
