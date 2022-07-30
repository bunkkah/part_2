package model;

import org.apache.commons.lang3.RandomStringUtils;

public class OrderRequest {
    public String[] ingredients;

    public OrderRequest(String[] ingredients){
        this.ingredients = ingredients;
    }

    public OrderRequest() {

    }

    public static OrderRequest getOrderWithoutIngredients(){
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.ingredients = null;

        return orderRequest;
    }

    public static OrderRequest getOrderWithDefaultHashIngredients(){
        final String hash1 = RandomStringUtils.randomAlphabetic(10);
        final String hash2 = RandomStringUtils.randomAlphabetic(10);

        return new OrderRequest(new String[]{hash1,hash2});
    }
}