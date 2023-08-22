package org.example.ingredients;

public class IngredientResp {
    private boolean success;
    private Ingredient[] data;

    public Ingredient[] getIngredients() {
        return data;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.data = ingredients;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
