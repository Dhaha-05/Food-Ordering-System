package com.foodapp.action;

import com.foodapp.model.FoodItem;
import com.foodapp.service.FoodItemService;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class FoodItemAction extends ActionSupport {
    private Map<String, Object> jsonResponse = new HashMap<>();
    private List<FoodItem> foodItems;

    public String execute() {
        FoodItemService foodItemService = new FoodItemService();
        foodItems = foodItemService.getAllFoodItems();
        if (foodItems != null && !foodItems.isEmpty()) {
            jsonResponse.put("status", "success");
            jsonResponse.put("foodItems", foodItems);
            return SUCCESS;
        } else {
            jsonResponse.put("status", "failed");
            jsonResponse.put("message", "No food items available");
            return NONE;
        }
    }

    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }
}