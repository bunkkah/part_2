package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IngredientsResponse {
    private boolean success;
    @JsonProperty("data")
    private List<Ingredient> ingredients;

    public IngredientsResponse(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public IngredientsResponse() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> filterIdByName(String... names) {
        return ingredients.stream()
                .filter(item -> Arrays.asList(names).contains(item.getName()))
                .map(item -> item.getId())
                .collect(Collectors.toList());
    }
}