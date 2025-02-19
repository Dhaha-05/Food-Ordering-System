package com.controller;

import com.util.JsonUtil;
import com.model.Restaurant;
import com.service.RestaurantService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RestaurantController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RestaurantService restaurantService = new RestaurantService();
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();

        JsonArray restaurantsArray = new JsonArray();

        if(restaurants.isEmpty())
        {
            JsonObject error = new JsonObject();
            error.addProperty("status", "failed");
            error.addProperty("message", "No restaurants available");
            JsonUtil.writeJson(res, error);
            return;
        }
        JsonObject success = new JsonObject();
        success.addProperty("status","success");
        success.addProperty("message","OK");
        for (Restaurant restaurant : restaurants) {
            JsonObject restaurantJson = new JsonObject();
            restaurantJson.addProperty("id", restaurant.getId());
            restaurantJson.addProperty("name", restaurant.getName());
            restaurantJson.addProperty("location", restaurant.getLocation());
            restaurantJson.addProperty("rating", restaurant.getRating());
            restaurantsArray.add(restaurantJson);
        }

        success.add("restaurants", restaurantsArray);
        JsonUtil.writeJson(res, success);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("application/json");
        System.out.println("Inside Add Restaurant Controller");
        JsonObject jsonObject = JsonUtil.readJson(req);
        Restaurant restaurant = new Restaurant();
        restaurant.setManagerid(jsonObject.get("userid").getAsInt());
        restaurant.setLocation(jsonObject.get("location").getAsString());
        restaurant.setName(jsonObject.get("name").getAsString());
        restaurant.setRating(jsonObject.get("rating").getAsDouble());
        restaurant.setPancard(jsonObject.get("pancard").getAsString());
        restaurant.setBankaccount(jsonObject.get("bankaccount").getAsString());
        restaurant.setFssailicense(jsonObject.get("fssailicense").getAsString());
        restaurant.setGstno(jsonObject.get("gstno").getAsString());
        RestaurantService restaurantService = new RestaurantService();
        try {
            if(restaurantService.createRestaurant(restaurant))
            {
                JsonObject success = new JsonObject();
                success.addProperty("status","success");
                success.addProperty("message","OK");
                JsonUtil.writeJson(res,success);
            }
            else
            {
                JsonObject failed = new JsonObject();
                failed.addProperty("status","failed");
                failed.addProperty("message","OK");
                JsonUtil.writeJson(res,failed);
            }
        } catch (Exception e) {
            System.out.println("Exception : "+e.getMessage());
            JsonObject failed = new JsonObject();
            failed.addProperty("status","failed");
            failed.addProperty("message","OK");
            JsonUtil.writeJson(res,failed);
        }
    }
}
