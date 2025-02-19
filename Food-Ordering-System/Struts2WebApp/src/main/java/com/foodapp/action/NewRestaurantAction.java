package com.foodapp.action;

import com.foodapp.model.Restaurant;
import com.foodapp.service.RestaurantService;
import com.opensymphony.xwork2.ActionSupport;
import java.util.HashMap;
import java.util.Map;

public class NewRestaurantAction extends ActionSupport
{
    private Map<String, Object> jsonResponse = new HashMap<>();
    private Restaurant restaurant;
    private boolean hasPermission = false;
    public String execute()
    {
        RestaurantService restaurantService = new RestaurantService();
        hasPermission = restaurantService.createRestaurant(restaurant);
        if(hasPermission){
            jsonResponse.put("status","success");
            jsonResponse.put("message","OK");
            return SUCCESS;
        }
        else {
            jsonResponse.put("status","failed");
            jsonResponse.put("message","Access Denied, 403 Forbidden access");
            jsonResponse.put("managerError","Enter valid Manager id");
            return NONE;
        }
    }

    public Map<String, Object> getJsonResponse() {
        return jsonResponse;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

}
