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
    private String action;
    private FoodItem foodItem;

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String execute() {
        FoodItemService foodItemService = new FoodItemService();
        if(action!=null)
        {
            switch(action){
                case "all-items":
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
                case "restaurant-items":
                    foodItems = foodItemService.getFoodItemsByRestaurant(foodItem.getRestaurantid());
                    if (foodItems != null && !foodItems.isEmpty()) {
                        jsonResponse.put("status", "success");
                        jsonResponse.put("foodItems", foodItems);
                        return SUCCESS;
                    } else {
                        jsonResponse.put("status", "failed");
                        jsonResponse.put("message", "No food items available");
                        return NONE;
                    }
                case "remove-item":
                    if(foodItemService.removeItem(foodItem.getItemid())){
                        jsonResponse.put("status","success");
                        jsonResponse.put("message","Item removed successfully");
                        return SUCCESS;
                    }
                    else {
                        jsonResponse.put("status","failed");
                        jsonResponse.put("message","Item deletion failed!!!");
                        return NONE;
                    }
                default :
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Invalid action");
                    return NONE;
            }
        }else{
            jsonResponse.put("status", "failed");
            jsonResponse.put("message", "No Action mentioned");
            return NONE;
        }
    }

    public String unauthorized() {
        jsonResponse.put("status", "failed");
        jsonResponse.put("message", "Unauthorized access");
        return "unauthorized";
    }

    public String forbidden() {
        jsonResponse.put("status", "failed");
        jsonResponse.put("message", "Forbidden access");
        return "forbidden";
    }

    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }
}