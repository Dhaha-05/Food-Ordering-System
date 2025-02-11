package com.foodapp.action;

import com.foodapp.model.Restaurant;
import com.foodapp.service.RestaurantService;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RestaurantAction extends ActionSupport {
    private Map<String, Object> jsonResponse = new HashMap<>();
    private List<Restaurant> restaurants;
    private Restaurant restaurant;
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String execute() {
        if(action!=null)
        {
            RestaurantService restaurantService = new RestaurantService();
            switch(action)
            {
                case "all-restaurant":
                    restaurants = restaurantService.getAllRestaurant();
                    if (restaurants != null && !restaurants.isEmpty()) {
                        jsonResponse.put("status", "success");
                        jsonResponse.put("restaurants", restaurants);
                        jsonResponse.put("message","OK");
                        return SUCCESS;
                    } else {
                        jsonResponse.put("status", "failed");
                        jsonResponse.put("message", "No restaurants available");
                        return NONE;
                    }
                case "restaurant":
                    restaurants = restaurantService.getRestaurantByManagerId(restaurant.getManagerid());
                    if(restaurants!=null && !restaurants.isEmpty())
                    {
                        jsonResponse.put("status", "success");
                        jsonResponse.put("restaurants", restaurants);
                        jsonResponse.put("message","OK");
                        return SUCCESS;
                    }else {
                        jsonResponse.put("status", "failed");
                        jsonResponse.put("message", "No restaurants available");
                        return NONE;
                    }
                case "remove-restaurant":
                    if(restaurantService.removeRestaurant(restaurant.getRestaurantid()))
                    {
                        jsonResponse.put("status", "success");
                        jsonResponse.put("message", "Selected restaurant removed successfully!!!");
                        return SUCCESS;
                    }
                    else {
                        jsonResponse.put("status","failed");
                        jsonResponse.put("message","Remove restaurant action failed");
                        return NONE;
                    }
                default:
                    jsonResponse.put("status", "error");
                    jsonResponse.put("message", "Invalid action");
                    return NONE;
            }
        }
        else {
            jsonResponse.put("status", "failed");
            jsonResponse.put("message", "Invalid action");
            return NONE;
        }

    }

//    public String createRestaurant() {
//        RestaurantService restaurantService = new RestaurantService();
//        try {
//            if (restaurantService.createRestaurant(restaurant)) {
//                jsonResponse.put("status", "success");
//                jsonResponse.put("message", "Restaurant created successfully");
//                return SUCCESS;
//            } else {
//                jsonResponse.put("status", "failed");
//                jsonResponse.put("message", "Failed to create restaurant");
//                return NONE;
//            }
//        } catch (Exception e) {
//            jsonResponse.put("status", "failed");
//            jsonResponse.put("message", e.getMessage());
//            return NONE;
//        }
//    }

    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}