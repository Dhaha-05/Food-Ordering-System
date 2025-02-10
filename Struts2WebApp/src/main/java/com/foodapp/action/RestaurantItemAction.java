package com.foodapp.action;

import com.foodapp.model.FoodItem;
import com.foodapp.service.FoodItemService;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RestaurantItemAction extends ActionSupport {
    private Map<String, Object> jsonResponse = new HashMap<>();
    private int restaurantId;
    private List<FoodItem> foodItems;

    public String execute() {
        if (restaurantId <= 0) {
            jsonResponse.put("status", "failed");
            jsonResponse.put("message", "Invalid restaurant");
            return NONE;
        }

        FoodItemService foodItemService = new FoodItemService();
        foodItems = foodItemService.getFoodItemsByRestaurant(restaurantId);
        if (foodItems != null && !foodItems.isEmpty()) {
            jsonResponse.put("status", "success");
            jsonResponse.put("foodItems", foodItems);
            return SUCCESS;
        } else {
            jsonResponse.put("status", "failed");
            jsonResponse.put("message", "No food items available in this restaurant");
            return NONE;
        }
    }

    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }
}