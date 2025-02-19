package com.controller;

import com.util.JsonUtil;
import com.model.FoodItem;
import com.service.FoodItemService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RestaurantItemController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonObject error = new JsonObject();
            error.addProperty("status", "failed");
            error.addProperty("message", "Invalid restaurant ID");
            JsonUtil.writeJson(res, error);
            return;
        }

        String[] pathParts = pathInfo.split("/");
        int restaurantId = Integer.parseInt(pathParts[1]);

        FoodItemService foodItemService = new FoodItemService();
        List<FoodItem> foodItems = foodItemService.getFoodItemsByRestaurant(restaurantId);
        if(foodItems.isEmpty())
        {
            JsonObject error = new JsonObject();
            error.addProperty("status","failed");
            error.addProperty("message","No Food Items available in this restaurant");
            JsonUtil.writeJson(res,error);
            return;
        }

        JsonObject jsonResponse = new JsonObject();
        JsonArray foodItemsArray = new JsonArray();
        jsonResponse.addProperty("status","success");
        jsonResponse.addProperty("message","OK");
        for (FoodItem item : foodItems) {
            JsonObject itemJson = new JsonObject();
            itemJson.addProperty("id", item.getId());
            itemJson.addProperty("name", item.getName());
            itemJson.addProperty("item",item.getItem());
            itemJson.addProperty("price", item.getPrice());
            itemJson.addProperty("category", item.getCategory());
            itemJson.addProperty("rating", item.getRating());
            itemJson.addProperty("offer", item.getOffer());
            foodItemsArray.add(itemJson);
        }

        jsonResponse.add("foodItems", foodItemsArray);
        JsonUtil.writeJson(res, jsonResponse);
    }
}

